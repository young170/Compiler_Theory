import java.util.ArrayList;

public class StringLiteralDFAScanner extends DFAScanner {

    public StringLiteralDFAScanner() {
        tokenAttribute = "string literal";

        int[][] transitionTable = {
            {1, -1, DFAState.REJECT.ordinal()}, // state 0
            {2, 1, DFAState.REJECT.ordinal()}, // state 1
            {-1, 3, DFAState.REJECT.ordinal()}, // state 2
            {-1, -1, DFAState.ACCEPT.ordinal()} // state 3
        };

        inputChar = new ArrayList<String>();
        inputChar.add("\"");

        dfa = new DFA(transitionTable, inputChar);
    }

}
