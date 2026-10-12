package org.team100.lib.music.commands;

import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.team100.lib.music.EventIterator;
import org.team100.lib.music.MidiUtil;
import org.team100.lib.music.Note;
import org.team100.lib.music.Polyphony;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class Play extends Command {
    private final Sequence m_sequence;
    private final Track m_track;
    private final Polyphony m_ensemble;
    private final Timer m_timer;
    private int m_i;
    private EventIterator m_iter;

    public Play(String filepath, Polyphony ensemble) {
        m_sequence = MidiUtil.sequence(filepath);
        m_track = m_sequence.getTracks()[0];
        m_ensemble = ensemble;
        m_timer = new Timer();
        m_iter = new EventIterator(filepath, 0.02);
    }

    @Override
    public void initialize() {
        m_timer.reset();
        m_timer.start();
        m_i = 0;
    }

    @Override
    public void execute() {
        System.out.println("execute");
        double nowSec = m_timer.get();
        List<MidiEvent> l = m_iter.get(nowSec);
        if (l == null) {
            System.out.println("no event for this time slice");
            return;
        }
        for (MidiEvent e : l) {
            long tick = e.getTick();
            MidiMessage m = e.getMessage();
            if (m instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) m;
                int key = sm.getData1();
                Note note = Note.get(key);
                if (note == null) {
                    System.out.printf("BAD NOTE %d", key);
                    continue;
                }
                int velocity = sm.getData2();
                int channel = sm.getChannel();
                if (velocity > 0) {
                    // attack
                    System.out.printf(
                            "Ch %d tick %d Attack key %d v %d\n",
                            channel, tick, key, velocity);
                } else {
                    // release
                    System.out.printf(
                            "Ch %d tick %d Release key %d v %d\n",
                            channel, tick, key, velocity);
                }
            } else {
                System.out.println("bad type");
            }
        }

        // System.out.printf("i %d\n", m_i);
        // MidiEvent event = m_track.get(m_i);
        // m_i++;
        // MidiMessage msg = event.getMessage();
        // if (msg instanceof MetaMessage) {
        // MetaMessage mm = (MetaMessage) msg;
        // MetaMessageType mmt = MetaMessageType.get(mm.getType());
        // System.out.printf("Meta %x %s\n", mm.getType(), mmt.name());
        // switch (mmt) {
        // case TRACK_NAME:
        // System.out.println("Track Name: " + new String(mm.getData()));
        // break;
        // case TIME_SIGNATURE:
        // System.out.println("Time Signature: " + new String(mm.getData()));
        // break;
        // case KEY_SIGNATURE:
        // System.out.println("Key Signature: " + new String(mm.getData()));
        // break;
        // case SET_TEMPO:
        // System.out.println("Set Tempo: " + new String(mm.getData()));
        // break;
        // default:
        // System.out.println("other");
        // }
        // } else if (msg instanceof ShortMessage) {
        // long tick = event.getTick();
        // ShortMessage sm = (ShortMessage) msg;
        // int channel = sm.getChannel();
        // int key = sm.getData1();
        // int velocity = sm.getData2();
        // if (velocity > 0) {
        // System.out.printf(
        // "Ch %d tick %d Attack key %d v %d\n",
        // channel, tick, key, velocity);
        // } else {
        // System.out.printf(
        // "Ch %d tick %d Release key %d v %d\n",
        // channel, tick, key, velocity);
        // }
        // } else {
        // System.out.println("Other message: %s" + msg.toString());
        // }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_timer.stop();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
