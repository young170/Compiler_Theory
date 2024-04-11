import java.util.ArrayList;

public class ErrorDFAScanner extends DFAScanner {
    
    /**
     * "other" input chars are excluded from the inputChar ArrayList.
     * This is to create an exclusion and therefore make it easier to distinguish.
     */
    public ErrorDFAScanner(char error_ch) {
        tokenAttribute = "illegal ID starting with wrong character";

        int[][] transitionTable = {
            {1, -1, -1, DFAState.REJECT.ordinal()}, // state 0
            {1, 2, 1, DFAState.REJECT.ordinal()}, // state 1
            {-1, -1, -1, DFAState.ACCEPT.ordinal()}, // state 2
        };

        inputChar = new ArrayList<String>();
        inputChar.add(Character.toString(error_ch));
        inputChar.add(tokenDelimiters);

        dfa = new DFA(transitionTable, inputChar);
    }

}
