import java.util.ArrayList;

public class CommentDFAScanner extends DFAScanner {
    
    /**
     * "other" input chars are excluded from the inputChar ArrayList.
     * This is to create an exclusion and therefore make it easier to distinguish.
     */
    public CommentDFAScanner() {
        tokenAttribute = "comment";

        int[][] transitionTable = {
            {1, -1, -1, DFAState.REJECT.ordinal()}, // state 0
            {2, -1, -1, DFAState.REJECT.ordinal()}, // state 1
            {2, 3, 2, DFAState.REJECT.ordinal()}, // state 2
            {-1, -1, -1, DFAState.ACCEPT.ordinal()}, // state 3
        };

        boolean[][] advanceTable = {
            {true, true, true},
            {true, true, true},
            {true, false, true},
            {false, false, false}
        };

        inputChar = new ArrayList<String>();
        // TODO
        // test "\n\0" in cases files don't end with \n
        inputChar.add("-");
        inputChar.add("\n");

        dfa = new DFA(transitionTable, advanceTable, inputChar);
    }

}
