import java.util.ArrayList;

public class CommentDFAScanner extends DFAScanner {
    
    public CommentDFAScanner() {
        tokenAttribute = "comment";

        int[][] transitionTable = {
            {1, -1, -1, DFAState.REJECT.ordinal()}, // state 0
            {2, -1, -1, DFAState.REJECT.ordinal()}, // state 1
            {2, 3, 2, DFAState.REJECT.ordinal()}, // state 2
            {-1, -1, -1, DFAState.ACCEPT.ordinal()}, // state 3
        };

        inputChar = new ArrayList<String>();
        // TODO
        // test "\n\0" in cases files don't end with \n
        inputChar.add("-");
        inputChar.add("\n");

        dfa = new DFA(transitionTable, inputChar);
    }

}
