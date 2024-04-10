import java.util.ArrayList;

public abstract class DFAScanner {

    protected String tokenAttribute;
    protected ArrayList<String> inputChar;
    protected DFA dfa;
    protected int[][] transitionTable;

    public enum DFAState { REJECT, ACCEPT, ERROR }
    public static final String tokenDelimiters = "\"-+*=<>();, \t\n\r\f\u000B";
    public static final String alphaTokens = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String errorTokenAttribute = "illegal ID starting with wrong character";

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

        if (dfa.error(curr_state)) {
            return new Token(tokenName, errorTokenAttribute);
        }

        return null;
    }

}
