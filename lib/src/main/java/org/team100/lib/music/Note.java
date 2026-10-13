package org.team100.lib.music;

import java.util.Map;
import java.util.TreeMap;

/**
 * MIDI note numbers and frequencies.
 * 
 * https://audiodev.blog/midi-note-chart/
 * https://inspiredacoustics.com/en/MIDI_note_numbers_and_center_frequencies
 * 
 * TODO: decimal places
 */
public enum Note {
    C1(24, 33),
    Cs1(25, 35),
    D1(26, 37),
    Ds1(27, 39),
    E1(28, 41),
    F1(29, 44),
    Fs1(30, 46),
    G1(31, 49),
    Gs1(32, 52),
    A1(33, 55),
    As1(34, 58),
    B1(35, 62),
    //
    C2(36, 65),
    Cs2(37, 69),
    D2(38, 73),
    Ds2(39, 78),
    E2(40, 82),
    F2(41, 87),
    Fs2(42, 92),
    G2(43, 98),
    Gs2(44, 104),
    A2(45, 110),
    As2(46, 117),
    B2(47, 123),
    //
    C3(48, 131),
    Cs3(49, 139),
    D3(50, 147),
    Ds3(51, 156),
    E3(52, 165),
    F3(53, 175),
    Fs3(54, 185),
    G3(55, 196),
    Gs3(56, 208),
    A3(57, 220),
    As3(58, 233),
    B3(59, 247),
    //
    C4(60, 262),
    Cs4(61, 277),
    D4(62, 294),
    Ds4(63, 311),
    E4(64, 330),
    F4(65, 349),
    Fs4(66, 370),
    G4(67, 392),
    Gs4(68, 415),
    A4(69, 440),
    As4(70, 466),
    B4(71, 494),
    //
    C5(72, 523),
    Cs5(73, 554),
    D5(74, 587),
    Ds5(75, 622),
    E5(76, 659),
    F5(77, 698),
    Fs5(78, 740),
    G5(79, 784),
    Gs5(80, 831),
    A5(81, 880),
    As5(82, 932),
    B5(83, 988),
    //
    C6(84, 1046),
    Cs6(85, 1109),
    D6(86, 1175),
    Ds6(87, 1244),
    E6(88, 1318),
    F6(89, 1397),
    Fs6(90, 1480),
    G6(91, 1568),
    Gs6(92, 1661),
    A6(93, 1760),
    As6(94, 1875),
    B6(95, 1975),
    //
    C7(96, 2093),
    Cs7(97, 2217),
    D7(98, 2349),
    Ds7(99, 2489),
    E7(100, 2637),
    F7(101, 2793),
    Fs7(102, 2960),
    G7(103, 3136),
    Gs7(104, 3322),
    A7(105, 3520),
    As7(106, 3729),
    B7(107, 3951),
    //
    C8(108, 4186),
    Cs8(109, 4435),
    D8(110, 4699),
    Ds8(111, 4978),
    E8(112, 5274),
    F8(113, 5588),
    Fs8(114, 5920),
    G8(115, 6272),
    Gs8(116, 6645),
    A8(117, 7040),
    As8(118, 7459),
    B8(119, 7902),
    //
    //
    C9(120, 8372),
    Cs9(121, 8870),
    D9(122, 9397),
    Ds9(123, 9956),
    E9(124, 10548),
    F9(125, 11175),
    Fs9(126, 11840),
    G9(127, 12544),
    Gs9(128, 13290);

    private static Map<Integer, Note> NOTES = new TreeMap<>();
    static {
        for (Note n : Note.values()) {
            NOTES.put(n.midi, n);
        }
    }

    /** Midi note number */
    public final int midi;
    /** Note frequency in Hz */
    public final double hz;

    Note(int midi, int hz) {
        this.midi = midi;
        this.hz = hz;
    }

    public static Note get(int midi) {
        Note note = NOTES.get(midi);
        if (note == null) {
            System.out.printf("Note out of range: %d", midi);
        }
        return note;
    }
}
