package core;

import java.util.Map;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        Board board = Generator.parse("" +
                "+-----------------------+\n" +
                "| 7 . 4 | 6 . . | . . . |\n" +
                "| . . 8 | . 2 . | 7 5 . |\n" +
                "| . 5 . | . 4 8 | . . . |\n" +
                "|-------+-------+-------|\n" +
                "| . . . | 5 . 7 | 2 . . |\n" +
                "| . 2 . | 4 . . | . . 8 |\n" +
                "| 5 9 . | . . . | . 7 . |\n" +
                "|-------+-------+-------|\n" +
                "| . . 2 | . . 6 | . 8 . |\n" +
                "| 8 . 6 | . . 5 | 1 . 7 |\n" +
                "| . . . | . 3 4 | . . 2 |\n" +
                "+-----------------------+"
        );
        System.out.println(board);
        System.out.println(search(board));
    }

    private static Board propagateTillPossible(Board board) {
        while (true) {
            Board newBoard = board.propagate();
            if (newBoard == null || newBoard == board) return newBoard;
            board = newBoard;
        }
    }

    private static Board search(Board board) {
        if (board == null || board.isWrong())
            return null;
        if (board.isSolved())
            return board;
        Map.Entry<String, String> pair = board.minimumCandidatePair();
        for (char c : pair.getValue().toCharArray()) {
            String value = String.valueOf(c);
            Board next = new Board(
                    board, pair.getKey(), value
            );
            next = propagateTillPossible(next);
            next = search(next);
            if (next != null) {
                return next;
            }
        }
        return null;
    }

}
