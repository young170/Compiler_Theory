import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SmallLexer {

    private HashMap<String, String> symbolTable; // <token-name, token-attribute>
    private ArrayList<Token> tokenList;
    private final String keywordAttribute = "keyword";
    private boolean printTokenList = true;

    private void reserveSymbolTable() {
        symbolTable.put("program", keywordAttribute);
        symbolTable.put("begin", keywordAttribute);
        symbolTable.put("end", keywordAttribute);
        symbolTable.put("if", keywordAttribute);
        symbolTable.put("else_if", keywordAttribute);
        symbolTable.put("else", keywordAttribute);
        symbolTable.put("while", keywordAttribute);
        symbolTable.put("int", keywordAttribute);
        symbolTable.put("print_line", keywordAttribute);
    }

    private void printTokenPair(Token token) {
        System.out.println(token.getTokenName() + "\t\t" + symbolTable.get(token.getTokenName()));
    }

    private char peek(char[] buf, int pos) {
        if (pos >= buf.length) {
            return '\0';
        }

        return buf[pos];
    }

    public void lex(String filename) {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            tokenList = new ArrayList<Token>();
            symbolTable = new HashMap<String, String>();
            reserveSymbolTable();

            while ((line = bufferedReader.readLine()) != null) {
                line += '\n';
                scanBuffer(line.toCharArray(), line.length());
            }
            
            bufferedReader.close();
            fileReader.close();
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
            e.printStackTrace();
        }

        if (printTokenList) {
            for (Token token : tokenList) {
                printTokenPair(token);
            }
        }
    }

    public void scanBuffer(char[] buf, int length) {
        int ptr = 0;
        DFAScanner scanner = null;

        while (ptr < length) {
            char lookahead = peek(buf, ptr);

            // skip whitespace
            if (Character.isWhitespace(lookahead)) {
                ptr++;
                continue;
            }

            if (lookahead == '\"') {
                scanner = new StringLiteralDFAScanner();
            } else if (Character.isDigit(lookahead)) {
                scanner = new NumberLiteralDFAScanner();
            } else if (lookahead == '-') {
                if (peek(buf, ptr + 1) == '-') {
                    scanner = new CommentDFAScanner();
                } else {
                    scanner = new SubtractionOperatorDFAScanner();
                }
            } else if (lookahead == '+') {
                scanner = new AdditionOperatorDFAScanner();
            } else if (lookahead == '*') {
                scanner = new MultiplicationOperatorDFAScanner();
            } else if (lookahead == '=') {
                scanner = new AssignmentOperatorDFAScanner();
            } else if (lookahead == '<') {
                scanner = new LessThanOperatorDFAScanner();
            } else if (lookahead == '>') {
                scanner = new GreaterThanOperatorDFAScanner();
            } else if (lookahead == '(') {
                scanner = new LeftParenthesisOperatorDFAScanner();
            } else if (lookahead == ')') {
                scanner = new RightParenthesisOperatorDFAScanner();
            } else if (lookahead == ';') {
                scanner = new StatementTerminatorOperatorDFAScanner();
            } else if (lookahead == ',') {
                scanner = new CommaPunctuationOperatorDFAScanner();
            } else if (lookahead == '$' || Character.isAlphabetic(lookahead)) {
                scanner = new IdentifierDFAScanner();
            } else { // error state
                scanner = new ErrorDFAScanner(lookahead);
            }

            Token token = scanner.scanLexUnit(buf, ptr);

            // check if token is a keyword (null check first)
            if ((symbolTable.get(token.getTokenName()) == null) || (symbolTable.get(token.getTokenName()) != keywordAttribute)) {
                symbolTable.put(token.getTokenName(), token.getTokenAttribute());
            }

            tokenList.add(new Token(token.getTokenName(), symbolTable.get(token.getTokenName())));

            ptr += token.getTokenName().length();
        }
    }

    public ArrayList<Token> getTokenList() {
        return tokenList;
    }

    public void setPrintTokenList(Boolean bool) {
        printTokenList = bool;
    }

}
