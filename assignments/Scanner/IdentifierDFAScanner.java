import java.util.ArrayList;

public class IdentifierDFAScanner extends DFAScanner {
    
    /**
     * "other" input chars are excluded from the inputChar ArrayList.
     * This is to create an exclusion and therefore make it easier to distinguish.
     */
    public IdentifierDFAScanner() {
        tokenAttribute = "identifier";

        int[][] transitionTable = {
            {1, -1, -1, DFAState.REJECT.ordinal()}, // state 0
            {1, 1, 2, DFAState.REJECT.ordinal()}, // state 1
            {-1, -1, -1, DFAState.ACCEPT.ordinal()}, // state 2
        };

        boolean[][] advanceTable = {
            {true, true, true},
            {true, true, false},
            {false, false, false}
        };

        inputChar = new ArrayList<String>();
        inputChar.add("$" + alphaTokens);
        inputChar.add("_.0123456789");

        dfa = new DFA(transitionTable, advanceTable, inputChar);
    }

}
