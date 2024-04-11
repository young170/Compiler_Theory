import java.util.ArrayList;

public class DFA {

    private int[][] transitionTable;
    private ArrayList<String> inputChar;

    public DFA(int[][] transitionTable, ArrayList<String> inputChar) {
        this.transitionTable = transitionTable;
        this.inputChar = inputChar;
    }

    public boolean accept(int state) {
        int isAcceptIdx = transitionTable[state].length - 1;

        if (transitionTable[state][isAcceptIdx] == DFAScanner.DFAState.ACCEPT.ordinal()) {
            return true;
        }

        return false;
    }

    public boolean error(int state) {
        int isErrorIdx = transitionTable[state].length - 1;

        if (transitionTable[state][isErrorIdx] == DFAScanner.DFAState.ERROR.ordinal()) {
            return true;
        }

        return false;
    }

    public int transition(int state, char sym) {
        // i = inputChar.size(), if "other"
        return transitionTable[state][stateInputCharIdx(state, sym)];
    }

    public boolean advance(int state, char sym) {
        if (transitionTable[transitionTable[state][stateInputCharIdx(state, sym)]][transitionTable[state].length - 1] == DFAScanner.DFAState.REJECT.ordinal()) {
            return true;
        }

        return false;
    }

    private int stateInputCharIdx(int state, char sym) {
        int i = 0;

        for (i = 0; i < inputChar.size(); i++) {
            if (inputChar.get(i).contains(String.valueOf(sym))) {
                break;
            }
        }

        return i;
    }

}
