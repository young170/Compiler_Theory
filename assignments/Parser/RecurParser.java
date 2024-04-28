import java.util.ArrayList;

public class RecurParser {

    private ArrayList<Token> tokenList;
    private int tokenIdx;
    private String errorMsg;

    private void error() {
        System.out.println("Parsing Failed");
        System.out.println(errorMsg);
        
        System.exit(1);
    }

    private void advanceToken() {
        tokenIdx++;
    }

    private void match(String expectedToken) {
        if (currTokenName().equals(expectedToken)) {
            advanceToken();
        } else {
            error();
        }
    }

    private String currTokenName() {
        if (tokenIdx == tokenList.size()) {
            error();
        }

        return tokenList.get(tokenIdx).getTokenName();
    }

    private String currTokenAttribute() {
        if (tokenIdx == tokenList.size()) {
            error();
        }

        return tokenList.get(tokenIdx).getTokenAttribute();
    }
    
    public RecurParser(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
        tokenIdx = 0;
    }
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java RecurParser <filename>");
            return;
        }

        SmallLexer smallLexer = new SmallLexer();
        smallLexer.setPrintTokenList(false);
        smallLexer.lex(args[0]);

        RecurParser recurParser = new RecurParser(smallLexer.getTokenList());
        recurParser.recurDescentParse();

        System.out.println("Parsing OK");
    }

    public void recurDescentParse() {
        preprocess(); // remove comments
        program();
    }

    public void preprocess() {
        for (int i = 0; i < tokenList.size(); i++) {
            if (tokenList.get(i).getTokenAttribute().equals("comment")) {
                tokenList.remove(i);
            }
        }
    }

    public void program() {
        if (currTokenName().equals("program")) {
            match("program");
            identifier();
            compoundStatement();
        } else {
            errorMsg = "program parse error";
            match("");
        }
    }

    public void statement() {
        if (currTokenName().equals("if")) { // <conditional statement>
            ifStatement();
        } else { // <simple statement>
            simpleStatement();
            match(";"); // statement terminator
        }
    }

    public void compoundStatement() {
        errorMsg = "begin missing";
        match("begin");

        errorMsg = "end missing";
        statement();
        while (!currTokenName().equals("end")) {
            statement();
        }

        match("end");
    }

    public void ifStatement() {
        match("if");
        expression();
        compoundStatement();

        while (currTokenName().equals("else_if")) { // 0 or more 'else_if'
            match("else_if");
            expression();
            compoundStatement();
        }

        if (currTokenName().equals("else")) { // optional else
            match("else");
            compoundStatement();
        }
    }

    public void simpleStatement() {
        // <assignment statement>
        if (currTokenAttribute().equals("identifier")) {
            assignmentStatement();
        }
        // <print statement>
        else if (currTokenName().equals("print_line")) {
            printStatement();
        }
        // <declaration statement>
        else if (isType(currTokenName())) {
            declarationStatement();
        }
    }

    public void assignmentStatement() {
        identifier();
        match("=");
        expression();
    }

    public void printStatement() {
        match("print_line");
        match("(");
        if (currTokenAttribute().equals("string literal")) {
            match(currTokenName());
        }
        match(")");
    }

    public boolean isType(String str) {
        if (str.equals("int")) {
            return true;
        }

        return false;
    }

    public void declarationStatement() {
        match(currTokenName());
        variableDeclaration();
        while (currTokenName().equals(",")) {
            match(",");
            variableDeclaration();
        }
    }

    public void variableDeclaration() {
        if (currTokenAttribute().equals("identifier")) {
            match(currTokenName());
        }

        if (currTokenName().equals("=")) {
            match("=");
            expression();
        }
    }

    public void identifier() {
        if (currTokenAttribute().equals("identifier")) {
            match(currTokenName());
        }
    }

    public void expression() {
        simpleExpression();
        
        if (currTokenName().equals("<") || currTokenName().equals(">")
            || currTokenName().equals("=")) {
            match(currTokenName());
            simpleExpression();
        }
    }

    public void simpleExpression() {
        term();

        while (currTokenName().equals("+") || currTokenName().equals("-")) { // add-op
            match(currTokenName());
            term();
        }
    }

    public void term() {
        factor();

        while (currTokenName().equals("*") || currTokenName().equals("/")) { // mult-op
            match(currTokenName());
            factor();
        }
    }

    public void factor() {
        if (currTokenAttribute().equals("identifier")) {
            match(currTokenName());
        } else if (currTokenAttribute().equals("number literal")) {
            match(currTokenName());
        } else if (currTokenName().equals("(")) {
            match("(");
            expression();
            match(")");
        }
    }

}
