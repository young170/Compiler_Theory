import java.util.ArrayList;

public class MultiplicationOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public MultiplicationOperatorDFAScanner() {
        tokenAttribute = "multiplication operator";

        inputChar = new ArrayList<String>();
        inputChar.add("*");

        dfa = new DFA(transitionTable, inputChar);
    }

}
