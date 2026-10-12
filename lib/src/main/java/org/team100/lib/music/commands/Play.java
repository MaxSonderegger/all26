package org.team100.lib.music.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.team100.lib.music.MetaMessageType;
import org.team100.lib.music.Polyphony;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class Play extends Command {
    private final Sequence m_sequence;
    private final Track m_track;
    private final Polyphony m_ensemble;
    private final Timer m_timer;
    private int m_i;

    public Play(String filepath, Polyphony ensemble) {
        m_sequence = sequence(filepath);
        m_track = m_sequence.getTracks()[0];
        m_ensemble = ensemble;
        m_timer = new Timer();
    }

    @Override
    public void initialize() {
        m_timer.reset();
        m_timer.start();
        m_i = 0;
    }

    @Override
    public void execute() {
        System.out.printf("i %d\n", m_i);
        MidiEvent event = m_track.get(m_i);
        m_i++;
        MidiMessage msg = event.getMessage();
        if (msg instanceof MetaMessage) {
            MetaMessage mm = (MetaMessage) msg;
            MetaMessageType mmt = MetaMessageType.get(mm.getType());
            System.out.printf("Meta %x %s\n", mm.getType(), mmt.name());
            switch (mmt) {
                case TRACK_NAME:
                    System.out.println("Track Name: " + new String(mm.getData()));
                    break;
                case TIME_SIGNATURE:
                    System.out.println("Time Signature: " + new String(mm.getData()));
                    break;
                case KEY_SIGNATURE:
                    System.out.println("Key Signature: " + new String(mm.getData()));
                    break;
                case SET_TEMPO:
                    System.out.println("Set Tempo: " + new String(mm.getData()));
                    break;
                default:
                    System.out.println("other");
            }
        } else if (msg instanceof ShortMessage) {
            long tick = event.getTick();
            ShortMessage sm = (ShortMessage) msg;
            int channel = sm.getChannel();
            int key = sm.getData1();
            int velocity = sm.getData2();
            if (velocity > 0) {
                System.out.printf(
                        "Ch %d tick %d Attack  key %d v %d\n",
                        channel, tick, key, velocity);
            } else {
                System.out.printf(
                        "Ch %d tick %d Release key %d v %d\n",
                        channel, tick, key, velocity);
            }
        } else {
            System.out.println("Other message: %s" + msg.toString());
        }
    }

    @Override
    public boolean isFinished() {
        return (1000000 * m_timer.get()) > m_sequence.getMicrosecondLength();
    }

    @Override
    public void end(boolean interrupted) {
        m_timer.stop();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }

    private static Sequence sequence(String filepath) {
        Path path = Filesystem.getDeployDirectory().toPath().resolve(filepath);
        System.out.printf("Trying to load path %s\n", path.toAbsolutePath().toString());
        try (InputStream is = Files.newInputStream(path)) {
            return MidiSystem.getSequence(is);
        } catch (IOException | InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

}
