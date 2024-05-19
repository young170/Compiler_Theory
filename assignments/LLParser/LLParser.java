import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class LLParser {

    private ArrayList<Token> tokenList;
    private ArrayList<String> declaraedIdentifierList;
    private int tokenIdx;
    private String parsingErrorMsg;
    private Map<String, Map<String, String>> parsingTable;
    private Stack<String> parsingStack;

    private void initializeLL1ParsingStack() {
        parsingStack = new Stack<>();
        parsingStack.push("$");
        parsingStack.push("PROGRAM");
    }

    private void initializeLL1ParsingTable() {
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

        // CONDITIONAL_STMT
        Map<String, String> tableCONDITIONAL_STMT = new HashMap<>();
        tableCONDITIONAL_STMT.put("if", "if EXPRESSION COMPOUND_STMT ELSE_IF_STMT ELSE_STMT");
        parsingTable.put("CONDITIONAL_STMT", tableCONDITIONAL_STMT);

        // ELSE_STMT
        Map<String, String> tableELSE_STMT = new HashMap<>();
        tableELSE_STMT.put("end", "");
        tableELSE_STMT.put("if", "");
        tableELSE_STMT.put("else", "else COMPOUND_STMT");
        tableELSE_STMT.put("while", "");
        tableELSE_STMT.put("for", "");
        tableELSE_STMT.put("print_line", "");
        tableELSE_STMT.put("display", "");
        tableELSE_STMT.put("break", "");
        tableELSE_STMT.put("identifier", "");
        tableELSE_STMT.put("int", "");
        tableELSE_STMT.put("integer", "");
        parsingTable.put("ELSE_STMT", tableELSE_STMT);

        // ELSE_IF_STMT
        Map<String, String> tableELSE_IF_STMT = new HashMap<>();
        tableELSE_IF_STMT.put("end", "");
        tableELSE_IF_STMT.put("if", "");
        tableELSE_IF_STMT.put("else_if", "else_if EXPRESSION COMPOUND_STMT ELSE_IF_STMT");
        tableELSE_IF_STMT.put("else", "");
        tableELSE_IF_STMT.put("while", "");
        tableELSE_IF_STMT.put("for", "");
        tableELSE_IF_STMT.put("print_line", "");
        tableELSE_IF_STMT.put("display", "");
        tableELSE_IF_STMT.put("break", "");
        tableELSE_IF_STMT.put("identifier", "");
        tableELSE_IF_STMT.put("int", "");
        tableELSE_IF_STMT.put("integer", "");
        parsingTable.put("ELSE_IF_STMT", tableELSE_IF_STMT);

        // WHILE_STMT
        Map<String, String> tableWHILE_STMT = new HashMap<>();
        tableWHILE_STMT.put("while", "while EXPRESSION COMPOUND_STMT");
        parsingTable.put("WHILE_STMT", tableWHILE_STMT);

        // FOR_STMT
        Map<String, String> tableFOR_STMT = new HashMap<>();
        tableFOR_STMT.put("for", "for ( DECLARATION_STMT ; EXPRESSION ; EXPRESSION ) COMPOUND_STMT");
        parsingTable.put("FOR_STMT", tableFOR_STMT);

        // SIMPLE_STMT
        Map<String, String> tableSIMPLE_STMT = new HashMap<>();
        tableSIMPLE_STMT.put("print_line", "PRINT_STMT");
        tableSIMPLE_STMT.put("display", "DISPLAY_STMT");
        tableSIMPLE_STMT.put("break", "BREAK_STMT");
        tableSIMPLE_STMT.put("identifier", "ASSIGNMENT_STMT");
        tableSIMPLE_STMT.put("int", "DECLARATION_STMT");
        tableSIMPLE_STMT.put("integer", "DECLARATION_STMT");
        parsingTable.put("SIMPLE_STMT", tableSIMPLE_STMT);

        // ASSIGNMENT_STMT
        Map<String, String> tableASSIGNMENT_STMT = new HashMap<>();
        tableASSIGNMENT_STMT.put("identifier", "IDENTIFIER = EXPRESSION");
        parsingTable.put("ASSIGNMENT_STMT", tableASSIGNMENT_STMT);

        // PRINT_STMT
        Map<String, String> tablePRINT_STMT = new HashMap<>();
        tablePRINT_STMT.put("print_line", "print_line ( PRINT_STMT_PRIME )");
        parsingTable.put("PRINT_STMT", tablePRINT_STMT);

        // PRINT_STMT_PRIME
        Map<String, String> tablePRINT_STMT_PRIME = new HashMap<>();
        tablePRINT_STMT_PRIME.put("string_literal", "STRING_LITERAL");
        tablePRINT_STMT_PRIME.put("identifier", "IDENTIFIER");
        parsingTable.put("PRINT_STMT_PRIME", tablePRINT_STMT_PRIME);

        // DECLARATION_STMT
        Map<String, String> tableDECLARATION_STMT = new HashMap<>();
        tableDECLARATION_STMT.put("int", "TYPE VARIABLE_DECLARATION VARIABLE_DECLARATIONS");
        tableDECLARATION_STMT.put("integer", "TYPE VARIABLE_DECLARATION VARIABLE_DECLARATIONS");
        parsingTable.put("DECLARATION_STMT", tableDECLARATION_STMT);

        // VARIABLE_DECLARATIONS
        Map<String, String> tableVARIABLE_DECLARATIONS = new HashMap<>();
        tableVARIABLE_DECLARATIONS.put(";", "");
        tableVARIABLE_DECLARATIONS.put(",", ", VARIABLE_DECLARATION VARIABLE_DECLARATIONS");
        parsingTable.put("VARIABLE_DECLARATIONS", tableVARIABLE_DECLARATIONS);

        // VARIABLE_DECLARATION
        Map<String, String> tableVARIABLE_DECLARATION = new HashMap<>();
        tableVARIABLE_DECLARATION.put("identifier", "IDENTIFIER VARIABLE_DECLARATION_PRIME");
        parsingTable.put("VARIABLE_DECLARATION", tableVARIABLE_DECLARATION);

        // VARIABLE_DECLARATION_PRIME
        Map<String, String> tableVARIABLE_DECLARATION_PRIME = new HashMap<>();
        tableVARIABLE_DECLARATION_PRIME.put(",", "");
        tableVARIABLE_DECLARATION_PRIME.put("=", "= EXPRESSION");
        tableVARIABLE_DECLARATION_PRIME.put(";", "");
        parsingTable.put("VARIABLE_DECLARATION_PRIME", tableVARIABLE_DECLARATION_PRIME);

        // DISPLAY_STMT
        Map<String, String> tableDISPLAY_STMT = new HashMap<>();
        tableDISPLAY_STMT.put("display", "display ( STRING_LITERAL )");
        parsingTable.put("DISPLAY_STMT", tableDISPLAY_STMT);

        // BREAK_STMT
        Map<String, String> tableBREAK_STMT = new HashMap<>();
        tableBREAK_STMT.put("break", "break");
        parsingTable.put("BREAK_STMT", tableBREAK_STMT);

        // IDENTIFIER
        Map<String, String> tableIDENTIFIER = new HashMap<>();
        tableIDENTIFIER.put("identifier", "identifier");
        tableIDENTIFIER.put("keyword", "keyword");
        parsingTable.put("IDENTIFIER", tableIDENTIFIER);

        // EXPRESSION
        Map<String, String> tableEXPRESSION = new HashMap<>();
        tableEXPRESSION.put("(", "SIMPLE_EXPRESSION EXPRESSION_PRIME");
        tableEXPRESSION.put("identifier", "SIMPLE_EXPRESSION EXPRESSION_PRIME");
        tableEXPRESSION.put("number_literal", "SIMPLE_EXPRESSION EXPRESSION_PRIME");
        parsingTable.put("EXPRESSION", tableEXPRESSION);

        // EXPRESSION_PRIME
        Map<String, String> tableEXPRESSION_PRIME = new HashMap<>();
        tableEXPRESSION_PRIME.put("begin", "");
        tableEXPRESSION_PRIME.put(";", "");
        tableEXPRESSION_PRIME.put(")", "");
        tableEXPRESSION_PRIME.put(",", "");
        tableEXPRESSION_PRIME.put("<", "RELATIONAL_OPERATOR SIMPLE_EXPRESSION");
        tableEXPRESSION_PRIME.put(">", "RELATIONAL_OPERATOR SIMPLE_EXPRESSION");
        tableEXPRESSION_PRIME.put("==", "RELATIONAL_OPERATOR SIMPLE_EXPRESSION");
        parsingTable.put("EXPRESSION_PRIME", tableEXPRESSION_PRIME);

        // SIMPLE_EXPRESSION
        Map<String, String> tableSIMPLE_EXPRESSION = new HashMap<>();
        tableSIMPLE_EXPRESSION.put("begin", "");
        tableSIMPLE_EXPRESSION.put(";", "");
        tableSIMPLE_EXPRESSION.put("(", "TERM SIMPLE_EXPRESSION_PRIME");
        tableSIMPLE_EXPRESSION.put(")", "");
        tableSIMPLE_EXPRESSION.put("identifier", "TERM SIMPLE_EXPRESSION_PRIME");
        tableSIMPLE_EXPRESSION.put("number_literal", "TERM SIMPLE_EXPRESSION_PRIME");
        tableSIMPLE_EXPRESSION.put(",", "");
        tableSIMPLE_EXPRESSION.put("<", "");
        tableSIMPLE_EXPRESSION.put(">", "");
        tableSIMPLE_EXPRESSION.put("==", "");
        parsingTable.put("SIMPLE_EXPRESSION", tableSIMPLE_EXPRESSION);

        // SIMPLE_EXPRESSION_PRIME
        Map<String, String> tableSIMPLE_EXPRESSION_PRIME = new HashMap<>();
        tableSIMPLE_EXPRESSION_PRIME.put("begin", "");
        tableSIMPLE_EXPRESSION_PRIME.put(";", "");
        tableSIMPLE_EXPRESSION_PRIME.put(")", "");
        tableSIMPLE_EXPRESSION_PRIME.put(",", "");
        tableSIMPLE_EXPRESSION_PRIME.put("<", "");
        tableSIMPLE_EXPRESSION_PRIME.put(">", "");
        tableSIMPLE_EXPRESSION_PRIME.put("==", "");
        tableSIMPLE_EXPRESSION_PRIME.put("+", "ADDING_OPERATOR TERM SIMPLE_EXPRESSION_PRIME");
        tableSIMPLE_EXPRESSION_PRIME.put("-", "ADDING_OPERATOR TERM SIMPLE_EXPRESSION_PRIME");
        parsingTable.put("SIMPLE_EXPRESSION_PRIME", tableSIMPLE_EXPRESSION_PRIME);

        // TERM
        Map<String, String> tableTERM = new HashMap<>();
        tableTERM.put("(", "FACTOR TERM_PRIME");
        tableTERM.put("identifier", "FACTOR TERM_PRIME");
        tableTERM.put("number_literal", "FACTOR TERM_PRIME");
        parsingTable.put("TERM", tableTERM);

        // TERM_PRIME
        Map<String, String> tableTERM_PRIME = new HashMap<>();
        tableTERM_PRIME.put("begin", "");
        tableTERM_PRIME.put(";", "");
        tableTERM_PRIME.put(")", "");
        tableTERM_PRIME.put(",", "");
        tableTERM_PRIME.put("<", "");
        tableTERM_PRIME.put(">", "");
        tableTERM_PRIME.put("==", "");
        tableTERM_PRIME.put("+", "");
        tableTERM_PRIME.put("-", "");
        tableTERM_PRIME.put("*", "MULTIPLYING_OPERATOR FACTOR TERM_PRIME");
        tableTERM_PRIME.put("/", "MULTIPLYING_OPERATOR FACTOR TERM_PRIME");
        tableTERM_PRIME.put("number_literal", "");
        parsingTable.put("TERM_PRIME", tableTERM_PRIME);

        // FACTOR
        Map<String, String> tableFACTOR = new HashMap<>();
        tableFACTOR.put("(", "( EXPRESSION )");
        tableFACTOR.put("identifier", "IDENTIFIER FACTOR_PRIME");
        tableFACTOR.put("number_literal", "NUMBER_LITERAL");
        parsingTable.put("FACTOR", tableFACTOR);

        // FACTOR_PRIME
        Map<String, String> tableFACTOR_PRIME = new HashMap<>();
        tableFACTOR_PRIME.put("begin", "");
        tableFACTOR_PRIME.put(";", "");
        tableFACTOR_PRIME.put(")", "");
        tableFACTOR_PRIME.put(",", "");
        tableFACTOR_PRIME.put("++", "++");
        tableFACTOR_PRIME.put("<", "");
        tableFACTOR_PRIME.put(">", "");
        tableFACTOR_PRIME.put("==", "");
        tableFACTOR_PRIME.put("+", "");
        tableFACTOR_PRIME.put("-", "");
        tableFACTOR_PRIME.put("*", "");
        tableFACTOR_PRIME.put("/", "");
        parsingTable.put("FACTOR_PRIME", tableFACTOR_PRIME);

        // RELATIONAL_OPERATOR
        Map<String, String> tableRELATIONAL_OPERATOR = new HashMap<>();
        tableRELATIONAL_OPERATOR.put("<", "<");
        tableRELATIONAL_OPERATOR.put(">", ">");
        tableRELATIONAL_OPERATOR.put("==", "==");
        parsingTable.put("RELATIONAL_OPERATOR", tableRELATIONAL_OPERATOR);

        // ADDING_OPERATOR
        Map<String, String> tableADDING_OPERATOR = new HashMap<>();
        tableADDING_OPERATOR.put("+", "+");
        tableADDING_OPERATOR.put("-", "-");
        parsingTable.put("ADDING_OPERATOR", tableADDING_OPERATOR);

        // MULTIPLYING_OPERATOR
        Map<String, String> tableMULTIPLYING_OPERATOR = new HashMap<>();
        tableMULTIPLYING_OPERATOR.put("*", "*");
        tableMULTIPLYING_OPERATOR.put("/", "/");
        parsingTable.put("MULTIPLYING_OPERATOR", tableMULTIPLYING_OPERATOR);

        // STRING_LITERAL
        Map<String, String> tableSTRING_LITERAL = new HashMap<>();
        tableSTRING_LITERAL.put("string_literal", "string_literal");
        parsingTable.put("STRING_LITERAL", tableSTRING_LITERAL);

        // NUMBER_LITERAL
        Map<String, String> tableNUMBER_LITERAL = new HashMap<>();
        tableNUMBER_LITERAL.put("number_literal", "number_literal");
        parsingTable.put("NUMBER_LITERAL", tableNUMBER_LITERAL);

        // TYPE
        Map<String, String> tableTYPE = new HashMap<>();
        tableTYPE.put("int", "int");
        tableTYPE.put("integer", "integer");
        parsingTable.put("TYPE", tableTYPE);
    }

    public LLParser(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
        declaraedIdentifierList = new ArrayList<>();
        declaraedIdentifierList.add("identifier");
        tokenIdx = 0;
        initializeLL1ParsingTable();
        initializeLL1ParsingStack();
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
        if (llParser.topDownParse() != 0) {
            System.out.println(llParser.parsingErrorMsg);
            System.out.println("Parsing Failed");
        } else {
            System.out.println("Parsing Ok");
        }
    }

    public int topDownParse() {
        preprocess();

        Token token = getNextToken();

        while (!parsingStack.isEmpty()) { // PDA accept by empty stack
            String TOS = parsingStack.pop();
            printStack(parsingStack); // POP

            // token list is empty, but stack is not done
            if ((token.getTokenName().equals("$")) && !(TOS.equals("$"))) {
                errorProcess(TOS, token);
                return -1;
            }

            if (TOS.equals(token.getTokenName())) { // match
                logStack("MATCH", TOS, token.getTokenName());
                token = getNextToken();
            } else if (isNonTerminal(TOS)) { // generate
                Map<String, String> row = parsingTable.get(TOS);

                if (isIdentifier(token)) {
                    if (!isDeclaredIdentifier(token)) {
                        errorProcess(token.getTokenAttribute(), token);
                        return -1;
                    }
                }

                // spelling checked by Lexer
                if (isIdentifier(token) || isStringLiteral(token) || isNumberLiteral(token)) {
                    token.setTokenName(token.getTokenAttribute());
                }

                String production = row.get(token.getTokenName());
                pushProductionToStack(production, parsingStack);
            } else {
                errorProcess(TOS, token);
                // parsingErrorMsg = "Error: undefined token " + token.getTokenName();
                return -1;
            }
        }

        // stack and token list is empty
        if (token.getTokenName().equals("$")) {
            return 0;
        } else {
            parsingErrorMsg = "Error: expected EOF " + token.getTokenName();
            return -1;
        }
    }

    private void logStack(String action, String TOS, String currToken) {
        System.out.println("[" + action + "] - " + TOS + " - " + currToken);
    }

    private void preprocess() {
        removeComments();
        addDeclaredIdentifiers();
    }

    private void removeComments() {
        tokenList.removeIf(token -> "comment".equals(token.getTokenAttribute()));
    }

    private void addDeclaredIdentifiers() {
        boolean isDeclarationSTMT = false;

        for (Token token : tokenList) {
            if (isType(token)) {
                isDeclarationSTMT = true;
            } else if (isStatementTerminator(token)) {
                isDeclarationSTMT = false;
            }

            if (isIdentifier(token) && isDeclarationSTMT) {
                declaraedIdentifierList.add(token.getTokenName());
            }
        }
    }

    private void errorProcess(String errorTOS, Token errorToken) {
        if (errorTOS.equals("STMTS_PRIME")) {
            parsingErrorMsg = "keyword end not matched";
        } else if (errorTOS.equals("=") || errorToken.getTokenName().equals("while4")) {
            parsingErrorMsg = "keyword spelling error";
        } else if (errorTOS.equals("identifier")) {
            parsingErrorMsg = errorToken.getTokenName() + " not declared";
        }
    }

    private Token getNextToken() {
        if (tokenIdx < tokenList.size()) {
            return tokenList.get(tokenIdx++);
        }

        // EOF
        return new Token("$", "");
    }

    private boolean isDeclaredIdentifier(Token identifier) {
        return declaraedIdentifierList.contains(identifier.getTokenName());
    }

    private void pushProductionToStack(String production, Stack<String> stack) {
        if (!production.equals("")) {
            String[] symbols = production.split(" "); // space separated symbols

            for (int i = symbols.length - 1; i >= 0; i--) { // push to stack in reverse order
                stack.push(symbols[i]);
            }
        }

        printStack(stack); // PUSH
    }

    private void printStack(Stack<String> stack) {
        System.out.print("[");
        // stack printing method
        printStackBottomToTop(stack);
        System.out.print("]\n");
    }

    private void printStackBottomToTop(Stack<String> stack) {
        if (stack.isEmpty()) {
            return;
        }

        String element = stack.pop();

        printStackBottomToTop(stack);

        System.out.print(" " + element + " ");

        stack.push(element);
    }

    private boolean isNonTerminal(String symbol) {
        return parsingTable.containsKey(symbol);
    }

    private boolean isType(Token token) {
        return (token.getTokenName().equals("int")) || (token.getTokenName().equals("integer")) || (token.getTokenName().equals("program"));
    }

    private boolean isStatementTerminator(Token token) {
        return token.getTokenAttribute().equals("statement terminator");
    }

    private boolean isIdentifier(Token token) {
        return token.getTokenAttribute().equals("identifier");
    }

    private boolean isStringLiteral(Token token) {
        return token.getTokenAttribute().equals("string_literal");
    }

    private boolean isNumberLiteral(Token token) {
        return token.getTokenAttribute().equals("number_literal");
    }

}
