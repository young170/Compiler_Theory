import java.util.ArrayList;

public class RightParenthesisOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public RightParenthesisOperatorDFAScanner() {
        tokenAttribute = "right parenthesis";

        inputChar = new ArrayList<String>();
        inputChar.add(")");

        dfa = new DFA(transitionTable, inputChar);
    }

}
