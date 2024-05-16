import java.util.ArrayList;

public class AssignmentOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public AssignmentOperatorDFAScanner() {
        tokenAttribute = "assignment operator";

        inputChar = new ArrayList<String>();
        inputChar.add("=");

        dfa = new DFA(transitionTable, inputChar);
    }

}
