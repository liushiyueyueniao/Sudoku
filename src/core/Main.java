package core;

import java.util.Map;

/**
 * The main class that handles search and constraint propagation methods. It
 * serves as the entry-point.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Main {

    private static final String BLANK = "" +
            "+-----------------------+\n" +
            "| . . . | . . . | . . . |\n" +
            "| . . . | . . . | . . . |\n" +
            "| . . . | . . . | . . . |\n" +
            "|-------+-------+-------|\n" +
            "| . . . | . . . | . . . |\n" +
            "| . . . | . . . | . . . |\n" +
            "| . . . | . . . | . . . |\n" +
            "|-------+-------+-------|\n" +
            "| . . . | . . . | . . . |\n" +
            "| . . . | . . . | . . . |\n" +
            "| . . . | . . . | . . . |\n" +
            "+-----------------------+";

    /**
     * The entry point for the program. Currently it does not accept any input.
     * It won't be difficult to do that. I encourage everyone to tinker with
     * the code.
     *
     * @param args The command-line arguments (which are ignored)
     */
    public static void main(String[] args) {
        Board board = Generator.parse("" +
                "+-----------------------+\n" +
                "| 8 . . | . . . | . . . |\n" +
                "| . . 3 | 6 . . | . . . |\n" +
                "| . 7 . | . 9 . | 2 . . |\n" +
                "|-------+-------+-------|\n" +
                "| . . 5 | . . 7 | . . . |\n" +
                "| . . . | . 4 5 | 7 . . |\n" +
                "| . . . | 1 . . | . 3 . |\n" +
                "|-------+-------+-------|\n" +
                "| . . 1 | . . . | . 6 8 |\n" +
                "| . . 8 | 5 . . | . 1 . |\n" +
                "| . 9 . | . . . | 4 . . |\n" +
                "+-----------------------+"
        );
        System.out.println(board);
        System.out.println(search(board));
    }

    /**
     * Carries on constraint propagation till no further values can be
     * eliminated.
     *
     * @param board The starting point for propagation.
     * @return The end result of propagation.
     */
    private static Board propagateTillPossible(Board board) {
        while (true) {
            Board newBoard = board.propagate();
            if (newBoard == null || newBoard == board) return newBoard;
            board = newBoard;
        }
    }

    /**
     * Performs a DFS (Depth-First-Search) of the possible states. Eliminates
     * as many state possibilities as possible using constraint propagation.
     * Returns the solution state or {@code null} if no solution wa found.
     *
     * @param board The state to work with.
     * @return The solution if obtained, {@code null} otherwise.
     */
    private static Board search(Board board) {
        // The board provided is faulty
        if (board == null || board.isWrong())
            return null;

        // Solution obtained
        if (board.isSolved())
            return board;

        // Proceeding with the square with minimum candidates helps to reduce
        // the chance of failure. For example, if we proceed with 7 (say)
        // possibilities, we may fail 6 out of 7 times. However, if we
        // proceed with 2 (say), we may fail at most half of the time.
        Map.Entry<String, String> pair = board.minimumCandidatePair();

        // Try out every possibility and see how far (or deep into the search
        // space) we can go. At least one branch is guaranteed to yield a
        // solution.
        for (char c : pair.getValue().toCharArray()) {
            String value = String.valueOf(c);

            Board next = new Board(board, pair.getKey(), value);
            next = propagateTillPossible(next);
            next = search(next);

            if (next != null) {
                // Solution found!
                return next;
            }
        }
        // Should never reach here... the exit clause for faulty puzzle exits
        return null;
    }

}
