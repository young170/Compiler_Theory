import java.util.ArrayList;

public class NumberLiteralDFAScanner extends DFAScanner {
    
    /**
     * "other" input chars are excluded from the inputChar ArrayList.
     * This is to create an exclusion and therefore make it easier to distinguish.
     */
    public NumberLiteralDFAScanner() {
        tokenAttribute = "number literal";

        int[][] transitionTable = {
            {1, -1, -1, -1, DFAState.REJECT.ordinal()}, // state 0
            {1, 3, 2, 3, DFAState.REJECT.ordinal()}, // state 1
            {-1, -1, -1, -1, DFAState.ACCEPT.ordinal()}, // state 2
            {3, 3, 4, 3, DFAState.REJECT.ordinal()}, // state 3
            {-1, -1, -1, -1, DFAState.ERROR.ordinal()}, // state 4
        };

        inputChar = new ArrayList<String>();
        inputChar.add("0123456789");
        inputChar.add(alphaTokens);
        inputChar.add(tokenDelimiters);

        dfa = new DFA(transitionTable, inputChar);
    }

}
