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
        symbolTable.put("for", keywordAttribute);
        symbolTable.put("int", keywordAttribute);
        symbolTable.put("integer", keywordAttribute);
        symbolTable.put("display", keywordAttribute);
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

        if (printTokenList) { // printing token list controller
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

            // DFAScanner attributes
            String tokenAttribute = new String();
            int[][] transitionTable = new int[0][0];
            ArrayList<String> inputChar = new ArrayList<String>();

            // skip whitespace
            if (Character.isWhitespace(lookahead)) {
                ptr++;
                continue;
            }

            if (lookahead == '\"') {
                tokenAttribute = "string literal";

                transitionTable = new int[4][3];
                transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()}; // state 0
                transitionTable[1] = new int[]{2, 1, DFAScanner.DFAState.REJECT.ordinal()};  // state 1
                transitionTable[2] = new int[]{-1, 3, DFAScanner.DFAState.REJECT.ordinal()}; // state 2
                transitionTable[3] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()}; // state 3

                inputChar.add("\"");
            } else if (Character.isDigit(lookahead)) {
                tokenAttribute = "number literal";

                transitionTable = new int[5][5];
                transitionTable[0] = new int[]{1, -1, -1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{1, 3, 2, 3, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, -1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};
                transitionTable[3] = new int[]{3, 3, 4, 3, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[4] = new int[]{-1, -1, -1, -1, DFAScanner.DFAState.ERROR.ordinal()};

                inputChar.add("0123456789");
                inputChar.add(DFAScanner.alphaTokens);
                inputChar.add(DFAScanner.tokenDelimiters);
            } else if (lookahead == '-') {
                if (peek(buf, ptr + 1) == '-') { // comment
                    tokenAttribute = "comment";

                    transitionTable = new int[4][4];
                    transitionTable[0] = new int[]{1, -1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[1] = new int[]{2, -1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[2] = new int[]{2, 3, 2, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[3] = new int[]{-1, -1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                    inputChar.add("-");
                    inputChar.add("\n");
                } else { // subtraction operatorr
                    tokenAttribute = "subtraction operator";

                    transitionTable = new int[3][3];
                    transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                    inputChar.add("-");
                }
            } else if (lookahead == '+') {
                if (peek(buf, ptr + 1) == '+') { // increment operator
                    tokenAttribute = "increment operator";

                    transitionTable = new int[4][3];
                    transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[1] = new int[]{2, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[2] = new int[]{-1, 3, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[3] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                    inputChar.add("+");
                } else { // plus operator
                    tokenAttribute = "plus operator";

                    transitionTable = new int[3][3];
                    transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                    inputChar.add("+");
                }
            } else if (lookahead == '*') {
                tokenAttribute = "multiplication operator";

                transitionTable = new int[3][3];
                transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add("*");
            } else if (lookahead == '=') {
                if (peek(buf, ptr + 1) == '=') { // equality operator
                    tokenAttribute = "equality operator";

                    transitionTable = new int[4][3];
                    transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[1] = new int[]{2, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[2] = new int[]{-1, 3, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[3] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                    inputChar.add("=");
                } else { // assignment operator
                    tokenAttribute = "assignment operator";

                    transitionTable = new int[3][3];
                    transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                    transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                    inputChar.add("=");
                }
            } else if (lookahead == '<') {
                tokenAttribute = "less than operator";

                transitionTable = new int[3][3];
                transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add("<");
            } else if (lookahead == '>') {
                tokenAttribute = "greater than operator";

                transitionTable = new int[3][3];
                transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add(">");
            } else if (lookahead == '(') {
                tokenAttribute = "left parenthesis";

                transitionTable = new int[3][3];
                transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add("(");
            } else if (lookahead == ')') {
                tokenAttribute = "right parenthesis";

                transitionTable = new int[3][3];
                transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add(")");
            } else if (lookahead == ';') {
                tokenAttribute = "statement terminator";

                transitionTable = new int[3][3];
                transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add(";");
            } else if (lookahead == ',') {
                tokenAttribute = "punctuation - comma";

                transitionTable = new int[3][3];
                transitionTable[0] = new int[]{1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{-1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add(",");
            } else if (lookahead == '$' || Character.isAlphabetic(lookahead)) {
                tokenAttribute = "identifier";

                transitionTable = new int[3][4];
                transitionTable[0] = new int[]{1, -1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{1, 1, 2, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add("$" + DFAScanner.alphaTokens);
                inputChar.add("_.0123456789");
            } else { // error state
                tokenAttribute = "illegal ID starting with wrong character";

                transitionTable = new int[3][4];
                transitionTable[0] = new int[]{1, -1, -1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[1] = new int[]{1, 2, 1, DFAScanner.DFAState.REJECT.ordinal()};
                transitionTable[2] = new int[]{-1, -1, -1, DFAScanner.DFAState.ACCEPT.ordinal()};

                inputChar.add(Character.toString(lookahead));
                inputChar.add(DFAScanner.tokenDelimiters);
            }

            scanner = new DFAScanner(tokenAttribute, transitionTable, inputChar);
            Token token = scanner.scanLexUnit(buf, ptr);

            // check if token is a keyword (null check first)
            if ((symbolTable.get(token.getTokenName()) == null) || (symbolTable.get(token.getTokenName()) != keywordAttribute)) {
                symbolTable.put(token.getTokenName(), token.getTokenAttribute());
            }

            tokenList.add(new Token(token.getTokenName(), symbolTable.get(token.getTokenName())));

            printTokenPair(token);

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
