import java.util.ArrayList;

public class AdditionOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public AdditionOperatorDFAScanner() {
        tokenAttribute = "plus operator";

        inputChar = new ArrayList<String>();
        inputChar.add("+");

        dfa = new DFA(transitionTable, inputChar);
    }

}
