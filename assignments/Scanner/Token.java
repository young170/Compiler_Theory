public class Token {
    
    private String tokenName;
    private String tokenAttribute;

    public Token(String tokenName, String tokenAttribute) {
        this.tokenName = tokenName;
        this.tokenAttribute = tokenAttribute;
    }

    public String getTokenName() { return tokenName; }
    public String getTokenAttribute() { return tokenAttribute; }
}
