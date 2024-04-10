import java.util.ArrayList;

public class LeftParenthesisOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public LeftParenthesisOperatorDFAScanner() {
        tokenAttribute = "left parenthesis";

        inputChar = new ArrayList<String>();
        inputChar.add("(");

        dfa = new DFA(transitionTable, inputChar);
    }

}
