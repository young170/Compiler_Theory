package main.java.lexer.readers;

import main.java.lexer.types.*;

public class IdReader implements InputReader{

    public Token readLexer(char[] buffer, int ptr) {
        StringBuffer b = new StringBuffer();

        while (buffer[ptr] != ' ') { // not delimiter or invalid special characters
            b.append(buffer[ptr++]);
        }

        String s = b.toString();
        Word id = new Word(Tag.ID, s);
        
        return id;
    }

}
