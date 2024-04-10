import java.util.ArrayList;

public class StatementTerminatorOperatorDFAScanner extends SingleOperatorDFAScanner {
    
    public StatementTerminatorOperatorDFAScanner() {
        tokenAttribute = "statement terminator";

        inputChar = new ArrayList<String>();
        inputChar.add(";");

        dfa = new DFA(transitionTable, inputChar);
    }

}
