import java.util.ArrayList;

public class CommaPunctuationOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public CommaPunctuationOperatorDFAScanner() {
        tokenAttribute = "punctuation - comma";

        inputChar = new ArrayList<String>();
        inputChar.add(",");

        dfa = new DFA(transitionTable, inputChar);
    }

}
