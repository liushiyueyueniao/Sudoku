package edu.lsp.Sudoku.Sudoku;

import java.util.*;

/**
 * This class generates and stores constants that will be used frequently
 * throughout the project.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Constants {
    /**
     * The side of the smaller square unit.
     */
    public static final int UNIT = 3;
    /**
     * The number of squares in one unit.
     */
    public static final int SIZE = UNIT * UNIT;
    /**
     * The total number of squares in the game.
     */
    public static final int NUMBER_OF_SQUARES = SIZE * SIZE;
    /**
     * The String containing all the possible candidates.
     */
    public static final String CANDIDATES;
    /**
     * The list of all the squares in the game.
     */
    public static final List<String> SQUARES;
    /**
     * The list of all units (rows, columns, squares) in the game.
     */
    public static final List<List<String>> UNITS;
    /**
     * The map between every square and its set of peers.
     */
    public static final Map<String, Set<String>> PEERS;

    /**
     * The maximum number of times the Generator will run the shuffle loop.
     */
    protected static final int MAX_SHUFFLE = 20;

    static {
        // All the possibilities linearly stored in this String
        StringBuilder builder = new StringBuilder(SIZE);
        for (int i = 1; i <= SIZE; i++) {
            builder.append(i);
        }
        CANDIDATES = builder.toString();

        // Generate the square labels
        List<String> squareList = new ArrayList<>(NUMBER_OF_SQUARES);
        char row = 'A';
        char col;
        for (int i = 0; i < SIZE; i++, row++) {
            col = '1';
            for (int j = 0; j < SIZE; j++, col++) {
                String square = "" + row + col;
                squareList.add(square);
            }
        }
        SQUARES = Collections.unmodifiableList(squareList);

        // Generate the units
        List<List<String>> temporaryLists = new ArrayList<>(SIZE * 3);
        // First, the rows
        row = 'A';
        for (int i = 0; i < SIZE; i++, row++) {
            List<String> squares = new ArrayList<>(SIZE);
            col = '1';
            for (int j = 0; j < SIZE; j++, col++) {
                squares.add("" + row + col);
            }
            temporaryLists.add(squares);
        }
        // Second, the columns
        col = '1';
        for (int i = 0; i < SIZE; i++, col++) {
            row = 'A';
            List<String> squares = new ArrayList<>(SIZE);
            for (int j = 0; j < SIZE; j++, row++) {
                squares.add("" + row + col);
            }
            temporaryLists.add(squares);
        }
        // Third, the squares
        int xOffset = 0;
        int yOffset = 0;
        while (xOffset < UNIT && yOffset < UNIT) {
            List<String> squares = new ArrayList<>(SIZE);
            for (int i = 0; i < UNIT; i++) {
                for (int j = 0; j < UNIT; j++) {
                    col = (char) ('1' + xOffset * UNIT + j);
                    row = (char) ('A' + yOffset * UNIT + i);
                    squares.add("" + row + col);
                }
            }
            temporaryLists.add(squares);
            xOffset++;
            if (xOffset == UNIT) {
                xOffset = 0;
                yOffset++;
            }
        }
        UNITS = Collections.unmodifiableList(temporaryLists);

        // The peers for each square
        Map<String, Set<String>> peerMap = new HashMap<>(NUMBER_OF_SQUARES);
        for (String square : SQUARES) {
            Set<String> peers = new HashSet<>(SIZE * 3 + 2 * (SIZE - UNIT));
            UNITS.stream()
                    .filter(unit -> unit.contains(square))
                    .forEach(peers::addAll);
            peers.remove(square);
            peerMap.put(square, peers);
        }
        PEERS = Collections.unmodifiableMap(peerMap);
    }
}
