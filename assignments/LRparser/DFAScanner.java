import java.util.ArrayList;

public class DFAScanner {

    private String tokenAttribute;
    private ArrayList<String> inputChar;
    private DFA dfa;
    private int[][] transitionTable;

    public enum DFAState { REJECT, ACCEPT, ERROR }
    public static final String tokenDelimiters = "\"-+*=<>();, \t\n\r\f\u000B";
    public static final String alphaTokens = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String errorTokenAttribute = "illegal ID starting with wrong character";

    public DFAScanner(String tokenAttribute, int[][] transitionTable, ArrayList<String> inputChar) {
        this.tokenAttribute = tokenAttribute;
        this.transitionTable = transitionTable;
        this.inputChar = inputChar;

        this.dfa = new DFA(transitionTable, inputChar);
    }

    public Token scanLexUnit (char[] buffer, int ptr) {
        String tokenName = "";

        int curr_state = 0;
        char ch = buffer[ptr];

        while (!(dfa.accept(curr_state)) && !(dfa.error(curr_state))) {
            int newState = dfa.transition(curr_state, ch);

            if (dfa.advance(curr_state, ch)) {
                tokenName += ch;
                ch = buffer[++ptr];
            }

            curr_state = newState;
        }

        if (dfa.accept(curr_state)) {
            return new Token(tokenName, tokenAttribute);
        }

        return new Token(tokenName, errorTokenAttribute);
    }

}
