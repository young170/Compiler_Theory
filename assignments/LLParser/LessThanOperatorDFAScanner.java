import java.util.ArrayList;

public class LessThanOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public LessThanOperatorDFAScanner() {
        tokenAttribute = "less than operator";

        inputChar = new ArrayList<String>();
        inputChar.add("<");

        dfa = new DFA(transitionTable, inputChar);
    }

}
