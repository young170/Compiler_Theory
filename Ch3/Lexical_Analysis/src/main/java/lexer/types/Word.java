package main.java.lexer.types;

public class Word extends Token{
    public final String lexeme;

    public Word(int t, String s) {
        super(t); // invokes the parent class constructor
        lexeme = new String(s);
    }
}
