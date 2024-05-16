import java.util.ArrayList;

public class GreaterThanOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public GreaterThanOperatorDFAScanner() {
        tokenAttribute = "greater than operator";

        inputChar = new ArrayList<String>();
        inputChar.add(">");

        dfa = new DFA(transitionTable, inputChar);
    }

}
