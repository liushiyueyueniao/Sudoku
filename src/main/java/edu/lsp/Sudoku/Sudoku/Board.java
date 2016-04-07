package edu.lsp.Sudoku.Sudoku;

import static edu.lsp.Sudoku.Sudoku.Constants.CANDIDATES;
import static edu.lsp.Sudoku.Sudoku.Constants.PEERS;
import static edu.lsp.Sudoku.Sudoku.Constants.SIZE;
import static edu.lsp.Sudoku.Sudoku.Constants.UNIT;
import static edu.lsp.Sudoku.Sudoku.Constants.UNITS;
import java.util.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a particular Board "state", with immutable mappings.
 * These can be considered as nodes in the search tree without any links. The
 * creation of Nodes is done separately in Main.java, during search.
 *
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Board {

	/**
	 * This immutable map stores the relations between the squares and the
	 * possible values.
	 */
	private final Map<String, String> candidateMap;

	/**
	 * Creates a new Board with the given trusted map. Used internally by Main.
	 *
	 * @param trustedCandidateMap
	 *            The trusted map to be used.
	 */
	protected Board(final Map<String, String> trustedCandidateMap) {
		candidateMap = Collections.unmodifiableMap(trustedCandidateMap);
	}

	/**
	 * It is used to create a new Board from a pre-existing one with just one
	 * change to be effected.
	 *
	 * @param previous
	 *            The previous state/node in the tree.
	 * @param square
	 *            The square to manipulate.
	 * @param trustedValue
	 *            The value to assign to the square.
	 */
	protected Board(final Board previous, String square, String trustedValue) {
		Map<String, String> temporaryMap = new LinkedHashMap<>(previous.candidateMap);
		temporaryMap.put(square, trustedValue);
		candidateMap = Collections.unmodifiableMap(temporaryMap);
	}

	/**
	 * Performs constraint propagation. It is basically removing the
	 * possibilities based on marked squares. Those with only one possible
	 * candidate end up being marked.
	 *
	 * @return The result of applying constraint propagation.
	 */
	public Board propagate() {
		int eliminations = 0;
		Map<String, String> cMap = new LinkedHashMap<>(this.candidateMap);
		for (Map.Entry<String, String> entry : cMap.entrySet()) {
			String square = entry.getKey();
			String candidates = entry.getValue();
			// check for wrong solution
			if (candidates.isEmpty()) {
				return null;
			}
			// check for finalised
			if (candidates.length() == 1) {
				for (String peer : PEERS.get(square)) {
					String peerValues = cMap.get(peer);
					if (peerValues.length() > 1 && peerValues.contains(candidates)) {
						eliminations++;
						peerValues = peerValues.replace(candidates, "");
						cMap.put(peer, peerValues);
					}
				}
			}
		}
		return eliminations == 0 ? this : new Board(cMap);
	}

	/**
	 * Returns <code>true</code> if every square is marked and has only one
	 * candidate.
	 *
	 * @return <code>true</code> if every square is marked and has only one
	 *         candidate.
	 */
	public boolean isSolved() {
		for (String values : candidateMap.values()) {
			if (values.length() > 1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns <code>true</code> if there is any repetition in any of the units.
	 *
	 * @return <code>true</code> if there is any repetition in any of the units.
	 */
	public boolean isWrong() {
		for (List<String> unit : UNITS) {
			for (char number : CANDIDATES.toCharArray()) {
				int count = 0;
				for (String square : unit) {
					String candidates = candidateMap.get(square);
					if (candidates.length() == 1 && candidates.charAt(0) == number)
						count++;
				}
				if (count > 1)
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns the square with the minimum number of candidates. This is useful
	 * in search to reduce the rate of choosing the wrong branch.
	 *
	 * @return The square with the minimum number of candidates.
	 */
	public Map.Entry<String, String> minimumCandidatePair() {
		Map.Entry<String, String> minimum = null;
		int number = SIZE + 1;
		for (Map.Entry<String, String> entry : candidateMap.entrySet()) {
			String candidates = entry.getValue();
			if (number > candidates.length() && candidates.length() > 1) {
				number = candidates.length();
				minimum = entry;
			}
		}
		return minimum;
	}

	/**
	 * Returns the current Board state as a String. The unmarked squared are
	 * represented by a '.'.
	 *
	 * @return The current Board state as a String.
	 */
	public String toString() {
		StringJoiner fullJoiner = new StringJoiner("\n", "\n+-----------------------+\n",
				"\n+-----------------------+\n");
		StringJoiner lineJoiner = new StringJoiner(" ", "| ", " |");
		int i = 1, j = 1;
		for (String value : candidateMap.values()) {
			lineJoiner.add(value.length() == 1 ? value : ".");
			if (i % SIZE == 0) {
				fullJoiner.add(lineJoiner.toString());
				lineJoiner = new StringJoiner(" ", "| ", " |");
				if (j % UNIT == 0 && j != SIZE) {
					fullJoiner.add("|-------+-------+-------|");
				}
				j++;
			} else if (i % UNIT == 0) {
				lineJoiner.add("|");
			}
			i++;
		}
		return fullJoiner.toString();
	}
}
