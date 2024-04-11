public class SingleOperatorDFAScanner extends DFAScanner {
    
    protected int[][] transitionTable = new int[][] {
        {1, -1, DFAState.REJECT.ordinal()}, // state 0
        {-1, 2, DFAState.REJECT.ordinal()}, // state 1
        {-1, -1, DFAState.ACCEPT.ordinal()}, // state 2
    };

}
