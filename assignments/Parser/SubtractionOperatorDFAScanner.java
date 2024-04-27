import java.util.ArrayList;

public class SubtractionOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public SubtractionOperatorDFAScanner() {
        tokenAttribute = "subtraction operator";

        inputChar = new ArrayList<String>();
        inputChar.add("-");

        dfa = new DFA(transitionTable, inputChar);
    }

}
