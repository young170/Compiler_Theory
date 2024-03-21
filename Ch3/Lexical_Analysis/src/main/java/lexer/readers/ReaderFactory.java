package main.java.lexer.readers;

import main.java.lexer.types.*;

public abstract class ReaderFactory {
    
    abstract InputReader createReader();

    public Token reader(char[] buffer, int ptr) {
        InputReader inputReader = createReader();
        return inputReader.readLexer(buffer, ptr);
    }

}
