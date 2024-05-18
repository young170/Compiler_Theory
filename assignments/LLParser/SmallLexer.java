import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        symbolTable.put("break", keywordAttribute);
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

            tokenList = new ArrayList<>();
            symbolTable = new HashMap<>();
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
        DFAScanner scanner;

        Map<Character, String> singleOperatorMap = Map.of(
            '*', "multiplication operator",
            '<', "less than operator",
            '>', "greater than operator",
            '(', "left parenthesis",
            ')', "right parenthesis",
            ';', "statement terminator",
            ',', "punctuation - comma"
        );

        int[][] singleOperatorTransitionTable = {
            {1, -1, DFAScanner.DFAState.REJECT.ordinal()},
            {-1, 2, DFAScanner.DFAState.REJECT.ordinal()},
            {-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()}
        };

        while (ptr < length) {
            char lookahead = peek(buf, ptr);

            String tokenAttribute = "";
            int[][] transitionTable = new int[0][0];
            ArrayList<String> inputChar = new ArrayList<>();

            if (Character.isWhitespace(lookahead)) {
                ptr++;
                continue;
            }

            if (lookahead == '\"') {
                tokenAttribute = "string_literal";

                transitionTable = new int[][]{
                    {1, -1, DFAScanner.DFAState.REJECT.ordinal()},
                    {2, 1, DFAScanner.DFAState.REJECT.ordinal()},
                    {-1, 3, DFAScanner.DFAState.REJECT.ordinal()},
                    {-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()}
                };

                inputChar.add("\"");
            } else if (Character.isDigit(lookahead)) {
                tokenAttribute = "number_literal";

                transitionTable = new int[][]{
                    {1, -1, -1, -1, DFAScanner.DFAState.REJECT.ordinal()},
                    {1, 3, 2, 3, DFAScanner.DFAState.REJECT.ordinal()},
                    {-1, -1, -1, -1, DFAScanner.DFAState.ACCEPT.ordinal()},
                    {3, 3, 4, 3, DFAScanner.DFAState.REJECT.ordinal()},
                    {-1, -1, -1, -1, DFAScanner.DFAState.ERROR.ordinal()}
                };

                inputChar.add("0123456789");
                inputChar.add(DFAScanner.alphaTokens);
                inputChar.add(DFAScanner.tokenDelimiters);
            } else if (lookahead == '-') {
                if (peek(buf, ptr + 1) == '-') {
                    tokenAttribute = "comment";

                    transitionTable = new int[][]{
                        {1, -1, -1, DFAScanner.DFAState.REJECT.ordinal()},
                        {2, -1, -1, DFAScanner.DFAState.REJECT.ordinal()},
                        {2, 3, 2, DFAScanner.DFAState.REJECT.ordinal()},
                        {-1, -1, -1, DFAScanner.DFAState.ACCEPT.ordinal()}
                    };

                    inputChar.add("-");
                    inputChar.add("\n");
                } else {
                    tokenAttribute = "subtraction operator";
                    transitionTable = singleOperatorTransitionTable;
                    inputChar.add("-");
                }
            } else if (lookahead == '+') {
                if (peek(buf, ptr + 1) == '+') {
                    tokenAttribute = "increment operator";

                    transitionTable = new int[][]{
                        {1, -1, DFAScanner.DFAState.REJECT.ordinal()},
                        {2, -1, DFAScanner.DFAState.REJECT.ordinal()},
                        {-1, 3, DFAScanner.DFAState.REJECT.ordinal()},
                        {-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()}
                    };

                    inputChar.add("+");
                } else {
                    tokenAttribute = "plus operator";
                    transitionTable = singleOperatorTransitionTable;
                    inputChar.add("+");
                }
            } else if (lookahead == '=') {
                if (peek(buf, ptr + 1) == '=') {
                    tokenAttribute = "equality operator";

                    transitionTable = new int[][]{
                        {1, -1, DFAScanner.DFAState.REJECT.ordinal()},
                        {2, -1, DFAScanner.DFAState.REJECT.ordinal()},
                        {-1, 3, DFAScanner.DFAState.REJECT.ordinal()},
                        {-1, -1, DFAScanner.DFAState.ACCEPT.ordinal()}
                    };

                    inputChar.add("=");
                } else {
                    tokenAttribute = "assignment operator";
                    transitionTable = singleOperatorTransitionTable;
                    inputChar.add("=");
                }
            } else if (singleOperatorMap.containsKey(lookahead)) {
                tokenAttribute = singleOperatorMap.get(lookahead);
                transitionTable = singleOperatorTransitionTable;
                inputChar.add(String.valueOf(lookahead));
            } else if (lookahead == '$' || Character.isAlphabetic(lookahead)) {
                tokenAttribute = "identifier";

                transitionTable = new int[][]{
                    {1, -1, -1, DFAScanner.DFAState.REJECT.ordinal()},
                    {1, 1, 2, DFAScanner.DFAState.REJECT.ordinal()},
                    {-1, -1, -1, DFAScanner.DFAState.ACCEPT.ordinal()}
                };

                inputChar.add("$" + DFAScanner.alphaTokens);
                inputChar.add("_.0123456789");
            } else {
                tokenAttribute = "illegal ID starting with wrong character";

                transitionTable = new int[][]{
                    {1, -1, -1, DFAScanner.DFAState.REJECT.ordinal()},
                    {1, 2, 1, DFAScanner.DFAState.REJECT.ordinal()},
                    {-1, -1, -1, DFAScanner.DFAState.ACCEPT.ordinal()}
                };

                inputChar.add(Character.toString(lookahead));
                inputChar.add(DFAScanner.tokenDelimiters);
            }

            scanner = new DFAScanner(tokenAttribute, transitionTable, inputChar);
            Token token = scanner.scanLexUnit(buf, ptr);

            if ((symbolTable.get(token.getTokenName()) == null) || (!symbolTable.get(token.getTokenName()).equals(keywordAttribute))) {
                symbolTable.put(token.getTokenName(), token.getTokenAttribute());
            }

            tokenList.add(new Token(token.getTokenName(), symbolTable.get(token.getTokenName())));
            ptr += token.getTokenName().length();
        }
    }

    public ArrayList<Token> getTokenList() {
        return tokenList;
    }

    public HashMap<String, String> getSymbolTable() {
        return symbolTable;
    }

    public void setPrintTokenList(Boolean bool) {
        printTokenList = bool;
    }
}
