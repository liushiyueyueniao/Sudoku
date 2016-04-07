package edu.lsp.Sudoku.Sudoku;

import java.util.LinkedHashMap;
import java.util.Map;

import static edu.lsp.Sudoku.Sudoku.Constants.*;

/**
 * Handles the parsing of various formats and produces a Board.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Parser {

	/**
	 * Parses a given String and tries to generate a Board with the required
	 * state and mappings.
	 * <p>
	 * This parser will only accept numbers 1 through 9 (or Constants#SIZE) and
	 * map them to their respective squares. '0' and '.' represent blank
	 * squares. <b>All other characters are ignored.</b>
	 *
	 * @param input
	 *            The input String to parse.
	 * @return The corresponding Board.
	 * @throws IllegalArgumentException
	 *             If the input is invalid.
	 */
	public static Board parse(String input) throws IllegalArgumentException {
		Map<String, String> state = new LinkedHashMap<>(NUMBER_OF_SQUARES);
		int i = 0;
		for (String square : SQUARES) {
			try {
				char c;
				do {
					c = input.charAt(i++);
				} while (!(CANDIDATES.indexOf(c) > -1 || c == '0' || c == '.'));
				state.put(square, CANDIDATES.indexOf(c) > -1 ? String.valueOf(c) : CANDIDATES);
			} catch (StringIndexOutOfBoundsException ignore) {
				throw new IllegalArgumentException("Input cannot be parsed.");
			}
		}
		return new Board(state);
	}

	/**
	 * Parses the given trusted array. It is used internally to generate the
	 * initial Board randomly.
	 *
	 * @param trustedArray
	 *            The trusted array to parse.
	 * @return The corresponding Board.
	 */
	protected static Board parse(int[][] trustedArray) {
		Map<String, String> state = new LinkedHashMap<>(NUMBER_OF_SQUARES);
		int i = 0, j = 0;
		for (String square : SQUARES) {
			char c = (char) ('0' + trustedArray[i][j++]);
			state.put(square, CANDIDATES.indexOf(c) > -1 ? String.valueOf(c) : CANDIDATES);
			if (j == SIZE) {
				j = 0;
				i++;
			}
		}
		return new Board(state);
	}
}
