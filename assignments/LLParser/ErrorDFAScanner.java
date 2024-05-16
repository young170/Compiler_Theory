import java.util.ArrayList;

public class ErrorDFAScanner extends DFAScanner {
    
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
