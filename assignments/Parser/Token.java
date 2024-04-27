public class Token {
    
    private String tokenName;
    private String tokenAttribute;

    public Token(String tokenName, String tokenAttribute) {
        this.tokenName = tokenName;
        this.tokenAttribute = tokenAttribute;
    }

    public String getTokenName() { return tokenName; }
    public String getTokenAttribute() { return tokenAttribute; }

    public void setTokenName(String tokenName) { this.tokenName = tokenName; }
    public void setTokenAttribute(String tokenAttribute) { this.tokenAttribute = tokenAttribute; }
}
