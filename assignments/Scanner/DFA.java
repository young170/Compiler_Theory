import java.util.ArrayList;

public class DFA {

    private int[][] transitionTable;
    private ArrayList<String> inputChar;

    public DFA(int[][] transitionTable, ArrayList<String> inputChar) {
        this.transitionTable = transitionTable;
        this.inputChar = inputChar;
    }

    public boolean accept(int state) {
        int dfaStateIdx = transitionTable[state].length - 1;

        if (transitionTable[state][dfaStateIdx] == DFAScanner.DFAState.ACCEPT.ordinal()) {
            return true;
        }

        return false;
    }

    public boolean error(int state) {
        int dfaStateIdx = transitionTable[state].length - 1;

        if (transitionTable[state][dfaStateIdx] == DFAScanner.DFAState.ERROR.ordinal()) {
            return true;
        }

        return false;
    }

    public int transition(int state, char sym) {
        // i = inputChar.size(), if "other"
        return transitionTable[state][transitionByInputCharIdx(state, sym)];
    }

    public boolean advance(int state, char sym) {
        if (transitionTable[transitionTable[state][transitionByInputCharIdx(state, sym)]][transitionTable[state].length - 1]
                == DFAScanner.DFAState.REJECT.ordinal()) {
            return true;
        }

        return false;
    }

    private int transitionByInputCharIdx(int state, char sym) {
        int i = 0;

        for (i = 0; i < inputChar.size(); i++) {
            if (inputChar.get(i).contains(String.valueOf(sym))) {
                break;
            }
        }

        return i;
    }

}
