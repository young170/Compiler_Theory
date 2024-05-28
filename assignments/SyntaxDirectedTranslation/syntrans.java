import java.util.ArrayList;

public class syntrans {

    private ArrayList<Token> tokenList;

    public syntrans(ArrayList<Token> tokenList) {
        this.tokenList = tokenList;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java syntrans <filename>");
            return;
        }

        SmallLexer smallLexer = new SmallLexer();
        smallLexer.setPrintTokenList(false);
        smallLexer.lex(args[0]);

        syntrans synTrans = new syntrans(smallLexer.getTokenList());
        synTrans.translate();
    }

    public void translate() {
        ASTNode root = new ASTNode("program", "root");

        int i = 0;
        while (i < tokenList.size()) {
            if (tokenList.get(i).getTokenAttribute().equals("identifier")
                    && tokenList.get(i + 1).getTokenName().equals("=")) {
                i = handleAssignment(i, root);
            }

            if (tokenList.get(i).getTokenName().equals("print_line")) {
                i = handlePrint(i, root);
            }

            i++; // skip ';' or ','
        }
    }

    public int handleAssignment(int i, ASTNode root) {
        String op = "+";
        int value = 0;
        ASTNode varAstNode;

        int varIdx = isDeclared(tokenList.get(i).getTokenName(), root);
        if (varIdx != -1) {
            varAstNode = root.getChildren().get(varIdx);
        } else {
            varAstNode = new ASTNode(tokenList.get(i).getTokenName(), Integer.toString(value));
        }

        i++; // skip identifier

        if (tokenList.get(i).getTokenName().equals("=")) {
            i++; // skip '='

            while (i < tokenList.size() && !(tokenList.get(i).getTokenName().equals(";")
                    || tokenList.get(i).getTokenName().equals(","))) {
                if (tokenList.get(i).getTokenAttribute().equals("number_literal")) {
                    int numTokenValue = Integer.parseInt(tokenList.get(i).getTokenName());
                    varAstNode.setValue(Integer.toString(applyOp(op, Integer.parseInt(varAstNode.getValue()), numTokenValue)));
                    i++;
                } else if (tokenList.get(i).getTokenAttribute().equals("identifier")) {
                    int identifierTokenValue = Integer.parseInt(root.getChildren().get(isDeclared(tokenList.get(i).getTokenName(), root)).getValue());
                    varAstNode.setValue(Integer.toString(applyOp(op, Integer.parseInt(varAstNode.getValue()), identifierTokenValue)));
                    i++;
                }

                if (isOperator(tokenList.get(i).getTokenName())) {
                    op = tokenList.get(i).getTokenName();
                    i++;
                }
            }
        }

        root.addChild(varAstNode);

        return i;
    }

    public int handlePrint(int i, ASTNode root) {
        i++; // skip 'print_line'

        if (tokenList.get(i).getTokenName().equals("(")) {
            i++; // skip '('

            if (tokenList.get(i).getTokenAttribute().equals("string_literal")) {
                printStringLiteral(tokenList.get(i).getTokenName());
            } else if (tokenList.get(i).getTokenAttribute().equals("identifier")) {
                printValue(Integer.parseInt(root.getChildren().get(isDeclared(tokenList.get(i).getTokenName(), root)).getValue()));
            }
        }

        return i;
    }

    private void printValue(int val) {
        printer(Integer.toString(val));
    }

    private void printStringLiteral(String tokenName) {
        printer(tokenName.substring(1, tokenName.length() - 1));
    }

    private void printer(String str) {
        System.out.println(str);
    }

    private int isDeclared(String tokenName, ASTNode root) {
        for (int i = 0; i < root.getChildren().size(); i++) {
            if (root.getChildren().get(i).getType().equals(tokenName)) {
                return i;
            }
        }

        return -1;
    }

    private boolean isOperator(String tokenName) {
        switch (tokenName) {
            case "*":
            case "/":
            case "+":
            case "-":
                return true;
        
            default:
                return false;
        }
    }

    private int applyOp(String op, int val1, int val2) {
        int val;

        switch (op) {
            case "*":
                val = val1 * val2;
                break;
            case "/":
                val = val1 / val2;
                break;
            case "+":
                val = val1 + val2;
                break;
            case "-":
                val = val1 - val2;
                break;
        
            default:
                val = 0;
                break;
        }

        return val;
    }
}
