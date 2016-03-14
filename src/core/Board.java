package core;

import java.util.*;

import static core.Constants.*;

/**
 * @author Subhomoy Haldar
 * @version 1.0
 */
public class Board {

    private final Map<String, String> candidateMap;

    protected Board(final Map<String, String> trustedCandidateMap) {
        candidateMap = Collections.unmodifiableMap(trustedCandidateMap);
    }

    protected Board(final Board previous, String square, String trustedValue) {
        Map<String, String> temporaryMap
                = new LinkedHashMap<>(previous.candidateMap);
        temporaryMap.put(square, trustedValue);
        candidateMap = Collections.unmodifiableMap(temporaryMap);
    }

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
                    if (peerValues.length() > 1 &&
                            peerValues.contains(candidates)) {
                        eliminations++;
                        peerValues = peerValues.replace(candidates, "");
                        cMap.put(peer, peerValues);
                    }
                }
            }
        }
        return eliminations == 0 ? this : new Board(cMap);
    }

    public boolean isSolved() {
        for (String values : candidateMap.values()) {
            if (values.length() > 1) {
                return false;
            }
        }
        return true;
    }

    public boolean isWrong() {
        for (List<String> unit : UNITS) {
            for (char number : CANDIDATES.toCharArray()) {
                int count = 0;
                for (String square : unit) {
                    String candidates = candidateMap.get(square);
                    if (candidates.length() == 1 &&
                            candidates.charAt(0) == number)
                        count++;
                }
                if (count > 1) return true;
            }
        }
        return false;
    }

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

    public String toString() {
        StringJoiner fullJoiner = new StringJoiner(
                "\n",
                "\n+-----------------------+\n",
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
