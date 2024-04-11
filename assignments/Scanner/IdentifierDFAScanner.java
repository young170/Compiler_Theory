import java.util.ArrayList;

public class IdentifierDFAScanner extends DFAScanner {
    
    public IdentifierDFAScanner() {
        tokenAttribute = "identifier";

        int[][] transitionTable = {
            {1, -1, -1, DFAState.REJECT.ordinal()}, // state 0
            {1, 1, 2, DFAState.REJECT.ordinal()}, // state 1
            {-1, -1, -1, DFAState.ACCEPT.ordinal()}, // state 2
        };

        inputChar = new ArrayList<String>();
        inputChar.add("$" + alphaTokens);
        inputChar.add("_.0123456789");

        dfa = new DFA(transitionTable, inputChar);
    }

}
