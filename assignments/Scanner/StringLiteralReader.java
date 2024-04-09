public class StringLiteralReader implements SymbolScanner {
    
    private static final String tokenAttribute = "stirng literal";

    public Token scanLexUnit (char[] buffer, int ptr) {
        Token token = null; // new Token(str, tokenAttribute)
        String tokenName = null;

        // TODO
        // read stringbuilder to tokenName

        token = new Token(tokenName, tokenAttribute);

        return token;
    }

}
