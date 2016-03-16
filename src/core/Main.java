package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The main class that handles search and constraint propagation methods. It
 * serves as the entry-point.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Main {

    /**
     * A handy template for reference.
     */
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
        // The following is supposed to be the world's "hardest" Sudoku puzzle.
        // It isn't really so. It has MORE THAN 1 SOLUTION (335 to be exact)
        // My code finds all 335 of them.
//        Board board = Parser.parse("" +
//                "+-----------------------+\n" +
//                "| 8 . . | . . . | . . . |\n" +
//                "| . . 3 | 6 . . | . . . |\n" +
//                "| . 7 . | . 9 . | 2 . . |\n" +
//                "|-------+-------+-------|\n" +
//                "| . . 5 | . . 7 | . . . |\n" +
//                "| . . . | . 4 5 | 7 . . |\n" +
//                "| . . . | 1 . . | . 3 . |\n" +
//                "|-------+-------+-------|\n" +
//                "| . . 1 | . . . | . 6 8 |\n" +
//                "| . . 8 | 5 . . | . 1 . |\n" +
//                "| . 9 . | . . . | 4 . . |\n" +
//                "+-----------------------+"
//        );
        Board board = Parser.parse("" +
                "+-----------------------+\n" +
                "| . 7 4 | 3 . 2 | . . . |\n" +
                "| . . . | . . 5 | . 4 . |\n" +
                "| . . . | 6 . 7 | 9 . . |\n" +
                "|-------+-------+-------|\n" +
                "| . 5 6 | . . . | 7 9 . |\n" +
                "| 3 . . | . . . | . . 5 |\n" +
                "| . 2 7 | . . . | 6 8 . |\n" +
                "|-------+-------+-------|\n" +
                "| . . 5 | 7 . 1 | . . . |\n" +
                "| . 1 . | 2 . . | . . . |\n" +
                "| . . . | 4 . 8 | 1 6 . |\n" +
                "+-----------------------+"
        );
        System.out.println(board);
        List<Board> solutions = new ArrayList<>();
        search(board, solutions);
        if (solutions.isEmpty()) {
            System.out.println("No solution found. Input is invalid.");
        } else if (solutions.size() == 1) {
            System.out.println(solutions.get(0));
        } else {
            System.out.println("Invalid Sudoku : multiple solutions.");
            System.out.println("Number of solutions = " + solutions.size());
            System.out.println("The solutions are ... ");
            System.out.println(solutions);

        }
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
     *
     * @param board The state to work with.
     */
    private static void search(Board board, List<Board> solutions) {
        // The board provided is faulty
        if (board == null || board.isWrong())
            return;

        // Solution obtained
        if (board.isSolved()) {
            solutions.add(board);
            return;
        }

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
            search(next, solutions);

        }
    }

}
