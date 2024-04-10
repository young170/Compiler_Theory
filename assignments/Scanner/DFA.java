import java.util.ArrayList;

public class DFA {

    private int[][] transitionTable;
    private boolean[][] advanceTable;
    private ArrayList<String> inputChar;

    public DFA(int[][] transitionTable, ArrayList<String> inputChar) {
        this.transitionTable = transitionTable;

        this.advanceTable = new boolean[transitionTable.length][transitionTable[0].length];
        for (int i = 0; i < advanceTable.length - 1; i++) {
            for (int j = 0; j < advanceTable[i].length; j++) {
                advanceTable[i][j] = true;
            }
        }

        for (int i = 0; i < advanceTable[advanceTable.length - 1].length; i++) {
            advanceTable[advanceTable.length - 1][i] = false;
        }

        this.inputChar = inputChar;
    }

    public DFA(int[][] transitionTable, boolean[][] advanceTable, ArrayList<String> inputChar) {
        this.transitionTable = transitionTable;
        this.advanceTable = advanceTable;
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

    /**
     * Checks whether there exists a transition from a state on a given input.
     * Initially, the transition table is filled with -1 for states with no transitions.
     * 
     * @param state State of the DFA
     * @param sym Input character of the DFA
     * @return Whether there exists a transition from given state via given input char
     */
    public boolean advance(int state, char sym) {
        return advanceTable[state][stateInputCharIdx(state, sym)];
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
