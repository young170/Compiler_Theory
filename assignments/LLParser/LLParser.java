import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LLParser {

    private ArrayList<Token> tokenList;
    private int tokenIdx;
    private String parsingResultMsg;
    private Map<String, Map<String, String>> parsingTable;

    public LLParser(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
        tokenIdx = 0;
        initializeParsingTable();
    }

    private void initializeParsingTable() {
        parsingTable = new HashMap<>();

        // PROGRAM
        Map<String, String> tablePROGRAM = new HashMap<>();
        tablePROGRAM.put("program", "program IDENTIFIER COMPOUND_STMT");
        parsingTable.put("PROGRAM", tablePROGRAM);

        // COMPOUND_STMT
        Map<String, String> tableCOMPOUND_STMT = new HashMap<>();
        tableCOMPOUND_STMT.put("begin", "begin STMTS end");
        parsingTable.put("COMPOUND_STMT", tableCOMPOUND_STMT);

        // STMTS
        Map<String, String> tableSTMTS = new HashMap<>();
        tableSTMTS.put("if", "STMT STMTS_PRIME");
        tableSTMTS.put("while", "STMT STMTS_PRIME");
        tableSTMTS.put("for", "STMT STMTS_PRIME");
        tableSTMTS.put("identifier", "STMT STMTS_PRIME");
        tableSTMTS.put("print_line", "STMT STMTS_PRIME");
        tableSTMTS.put("display", "STMT STMTS_PRIME");
        tableSTMTS.put("break", "STMT STMTS_PRIME");
        tableSTMTS.put("int", "STMT STMTS_PRIME");
        tableSTMTS.put("integer", "STMT STMTS_PRIME");
        parsingTable.put("STMTS", tableSTMTS);

        // STMTS_PRIME
        Map<String, String> tableSTMTS_PRIME = new HashMap<>();
        tableSTMTS_PRIME.put("if", "STMTS");
        tableSTMTS_PRIME.put("while", "STMTS");
        tableSTMTS_PRIME.put("for", "STMTS");
        tableSTMTS_PRIME.put("identifier", "STMTS");
        tableSTMTS_PRIME.put("print_line", "STMTS");
        tableSTMTS_PRIME.put("display", "STMTS");
        tableSTMTS_PRIME.put("break", "STMTS");
        tableSTMTS_PRIME.put("int", "STMTS");
        tableSTMTS_PRIME.put("integer", "STMTS");
        tableSTMTS_PRIME.put("end", "");  // epsilon transition
        parsingTable.put("STMTS_PRIME", tableSTMTS_PRIME);

        // STMT
        Map<String, String> tableSTMT = new HashMap<>();
        tableSTMT.put("if", "CONDITIONAL_STMT");
        tableSTMT.put("while", "WHILE_STMT");
        tableSTMT.put("for", "FOR_STMT");
        tableSTMT.put("identifier", "SIMPLE_STMT ;");
        tableSTMT.put("print_line", "SIMPLE_STMT ;");
        tableSTMT.put("display", "SIMPLE_STMT ;");
        tableSTMT.put("break", "SIMPLE_STMT ;");
        tableSTMT.put("int", "SIMPLE_STMT ;");
        tableSTMT.put("integer", "SIMPLE_STMT ;");
        parsingTable.put("STMT", tableSTMT);

        // IDENTIFIER
        Map<String, String> tableIDENTIFIER = new HashMap<>();
        tableIDENTIFIER.put("Test3", "Test3");
        parsingTable.put("IDENTIFIER", tableIDENTIFIER);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java LLParser <filename>");
            return;
        }

        SmallLexer smallLexer = new SmallLexer();
        smallLexer.setPrintTokenList(false);
        smallLexer.lex(args[0]);

        LLParser llParser = new LLParser(smallLexer.getTokenList());
        llParser.topDownParse();

        // Parsing Result
        System.out.println(llParser.parsingResultMsg);
    }

    public void topDownParse() {
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push("PROGRAM");

        Token token = getNextToken();

        while (!stack.isEmpty()) { // PDA accept by empty stack
            String top = stack.pop();
            System.out.println("[STACK][POP]: " + top);

            if (isNonTerminal(top)) { // generate
                Map<String, String> row = parsingTable.get(top);

                if (row == null || !row.containsKey(token.getTokenName())) {
                    parsingResultMsg = "Error: unexpected token " + token.getTokenName();
                    return;
                }

                String production = row.get(token.getTokenName());
                if (!production.equals("")) {
                    String[] symbols = production.split(" "); // space separated symbols

                    for (int i = symbols.length - 1; i >= 0; i--) { // push to stack in reverse order
                        stack.push(symbols[i]);
                        System.out.println("[STACK][PUSH]: " + symbols[i]);
                    }
                }
            } else if (top.equals(token.getTokenName())) { // match
                System.out.println("[MATCH]: " + top + "-" + token.getTokenName());
                token = getNextToken();
            } else if (top.equals("")) {
                continue;
            } else {
                parsingResultMsg = "Error: unexpected token " + token.getTokenName();
                return;
            }
        }

        if (token.getTokenName().equals("$")) {
            parsingResultMsg = "Parsing Ok";
        } else {
            parsingResultMsg = "Error: unexpected token " + token.getTokenName();
        }
    }

    private boolean isNonTerminal(String symbol) {
        return parsingTable.containsKey(symbol);
    }

    private Token getNextToken() {
        if (tokenIdx < tokenList.size()) {
            return tokenList.get(tokenIdx++);
        }

        // EOF
        return new Token("$", "");
    }
}
