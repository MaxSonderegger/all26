package org.team100.lib.music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Walks through the MIDI timeline, yielding a list of events for each time
 * step.
 */
public class EventIterator {
    private final Sequence m_sequence;
    private final Track[] m_tracks;
    /** Event index for each track. */
    private int[] m_iEvent;
    private Map<Double, List<MidiEvent>> m_events;

    public EventIterator(String filepath, double dt) {
        m_sequence = MidiUtil.sequence(filepath);
        System.out.printf("DivisionType %f\n", m_sequence.getDivisionType());
        // resolution is ticks per quarter note
        int ticksPerBeat = m_sequence.getResolution();
        m_tracks = m_sequence.getTracks();
        m_iEvent = new int[m_tracks.length];
        for (int i = 0; i < m_tracks.length; ++i) {
            m_iEvent[i] = 0;
        }
        // walk through all the events in all the tracks, and populate
        // a map of events indexed by time slice.
        m_events = new TreeMap<>();
        // current tempo
        double bpm = 0;
        System.out.println("LOADING");
        for (int ti = 0; ti < m_tracks.length; ++ti) {
            Track t = m_tracks[ti];
            for (int ei = 0; ei < t.size(); ++ei) {
                MidiEvent e = t.get(ei);
                long tick = e.getTick();
                double sec = MidiUtil.ticksToSec(tick, bpm, ticksPerBeat);
                MidiMessage m = e.getMessage();
                if (m instanceof MetaMessage) {
                    MetaMessage mm = (MetaMessage) m;
                    if (mm.getType() == 0x51) { // Tempo meta event
                        byte[] data = mm.getData();
                        int mpq = ((data[0] & 0xFF) << 16) |
                                ((data[1] & 0xFF) << 8) |
                                (data[2] & 0xFF);
                        bpm = 60_000_000.0 / mpq;
                        System.out.println("Tempo found at tick " + e.getTick() + ": " + bpm + " BPM");
                    }
                    continue;
                }
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
                    System.out.printf("store t %f\n", sec);
                    List<MidiEvent> eList = m_events.computeIfAbsent(sec, x -> new ArrayList<>());
                    eList.add(e);
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
                }
            }
        }
        System.out.println("FINISHED");

    }

    public List<MidiEvent> get(double t) {
        System.out.printf("get %f\n", t);
        return m_events.get(t);
    }

}
