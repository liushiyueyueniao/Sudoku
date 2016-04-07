package edu.lsp.Sudoku.Sudoku;

import java.util.Random;

import static edu.lsp.Sudoku.Sudoku.Constants.*;

/**
 * This class is responsible for randomly generating Game states.
 * <p>
 * This uses a 2D array instead of a map because the former is easier to
 * manipulate and transform.
 * <p>
 * To obtain a map from the 2D array, call {@link Parser#parse(int[][])
 * <code>Parser.parse(int[][])</code>}.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Generator {

	/**
	 * This method generated a shuffled array containing all the values filled
	 * in. In order to make a Sudoku, values must be removed (set to 0).
	 *
	 * @return A 2D array with a solved Sudoku grid.
	 */
	public static int[][] generateSolved() {
		int[][] array = new int[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				array[i][j] = (i * UNIT + i / UNIT + j) % SIZE + 1;
			}
		}
		Random random = new Random();
		int limit = random.nextInt(Constants.MAX_SHUFFLE);
		for (int i = 0; i < limit; i++) {
			if (random.nextBoolean())
				transpose(array);
			if (random.nextBoolean())
				shuffleSquareRows(array);
			if (random.nextBoolean())
				shuffleSingleRows(array);
		}
		return array;
	}

	/**
	 * Transposes the given square matrix/2D array.
	 *
	 * @param array
	 *            The array to be transposed.
	 */
	public static void transpose(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < i; j++) {
				int temp = array[i][j];
				array[i][j] = array[j][i];
				array[j][i] = temp;
			}
		}
	}

	/**
	 * Shuffles square rows in their entirety, i.e. moves 3 rows at a time.
	 *
	 * @param array
	 *            The array to be transformed.
	 */
	private static void shuffleSquareRows(int[][] array) {
		Random random = new Random();
		for (int i = 0; i < UNIT - 1; i++) {
			int j = 1 + i + random.nextInt(UNIT - 1 - i);
			swapSquareRows(array, i, j);
		}
	}

	/**
	 * Shuffles single rows within each square row.
	 *
	 * @param array
	 *            The array to be transformed.
	 */
	private static void shuffleSingleRows(int[][] array) {
		Random random = new Random();
		for (int i = 0; i < UNIT; i++) {
			int start = i * UNIT;
			int limit = start + UNIT - 1;
			for (int j = start; j < limit; j++) {
				int k = start + 1 + random.nextInt(limit - j);
				swapSingleRows(array, j, k);
			}
		}
	}

	/**
	 * Swaps two rows within a square.
	 *
	 * @param array
	 *            The array to be transformed.
	 * @param i
	 *            The first row.
	 * @param j
	 *            The second row.
	 */
	private static void swapSingleRows(int[][] array, int i, int j) {
		int[] temp = new int[SIZE];
		System.arraycopy(array[i], 0, temp, 0, SIZE);
		System.arraycopy(array[j], 0, array[i], 0, SIZE);
		System.arraycopy(temp, 0, array[j], 0, SIZE);
	}

	/**
	 * Swaps 6 rows at a time. Swaps the ith square row with the jth one.
	 *
	 * @param array
	 *            The array to be transformed.
	 * @param i
	 *            The first row.
	 * @param j
	 *            The second row.
	 */
	private static void swapSquareRows(int[][] array, int i, int j) {
		// if (i == j) return;
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

	// private static String toString(int[][] array) {
	// StringBuilder builder = new StringBuilder();
	// for (int[] row : array) {
	// builder.append(Arrays.toString(row)).append("\n");
	// }
	// return builder.toString();
	// }

}
