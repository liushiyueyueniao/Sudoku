package core;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static core.Constants.*;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Generator {
    public static void main(String[] args) {
        int[][] array = generateArray();
        System.out.println(toString(array));
        transpose(array);
        System.out.println(toString(array));
        shuffleSquareRows(array);
        System.out.println(toString(array));

    }

    public static Board parse(String input) {
        Map<String, String> state = new LinkedHashMap<>(NUMBER_OF_SQUARES);
        int i = 0;
        for (String square : SQUARES) {
            char c;
            do
                c = input.charAt(i++);
            while (!(Character.isDigit(c) || c == '.'));
            state.put(square, CANDIDATES.indexOf(c) > -1
                    ? String.valueOf(c)
                    : CANDIDATES);
        }
        return new Board(state);
    }

    public static Board generateMap() {
        double probability = 0.7;
        Map<String, String> state = new LinkedHashMap<>(NUMBER_OF_SQUARES);
        char row = 'A';
        char col;
        for (int i = 0; i < SIZE; i++, row++) {
            col = '1';
            for (int j = 0; j < SIZE; j++, col++) {
                String candidates = (Math.random() <= probability)
                        ? String.valueOf((i * UNIT + i / UNIT + j) % SIZE + 1)
                        : CANDIDATES;

                state.put("" + row + col, candidates);
            }
        }
        return new Board(state);
    }

    public static int[][] generateArray() {
        int[][] array = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                array[i][j] = (i * UNIT + i / UNIT + j) % SIZE + 1;
            }
        }
        return array;
    }

    public static void transpose(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < i; j++) {
                int temp = array[i][j];
                array[i][j] = array[j][i];
                array[j][i] = temp;
            }
        }
    }

    private static void shuffleSquareRows(int[][] array) {
        Random random = new Random();
        for (int i = 0; i < UNIT - 1; i++) {
            int j = 1 + i + random.nextInt(UNIT - 1 - i);
            swapSquareRows(array, i, j);
        }
    }

    private static void swapSquareRows(int[][] array, int i, int j) {
        //if (i == j) return;
        int[][] temp = new int[UNIT][SIZE];
        int iStart = i * UNIT;
        int jStart = j * UNIT;
        int iLimit = iStart + UNIT;
        int jLimit = jStart + UNIT;
        // copy to temp
        for (int k = iStart, l = 0; k < iLimit; k++, l++) {
            System.arraycopy(array[k], 0, temp[l], 0, SIZE);
        }
        // copy to array[i] & following
        for (int k = iStart, l = jStart; k < iLimit; k++, l++) {
            System.arraycopy(array[l], 0, array[k], 0, SIZE);
        }
        // copy to array[j] & following
        for (int k = jStart, l = 0; k < jLimit; k++, l++) {
            System.arraycopy(temp[l], 0, array[k], 0, SIZE);
        }
    }

    private static String toString(int[][] array) {
        StringBuilder builder = new StringBuilder();
        for (int[] row : array) {
            builder.append(Arrays.toString(row)).append("\n");
        }
        return builder.toString();
    }

}
