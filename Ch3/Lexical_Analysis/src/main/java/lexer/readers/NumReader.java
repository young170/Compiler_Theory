package main.java.lexer.readers;

import main.java.lexer.types.*;

public class NumReader implements InputReader{

    public Token readLexer(char[] buffer, int ptr) {
        int value = 0;

        while (buffer[ptr] != ' ') { // not delimiter or invalid special characters
            value = (value * 10) + Character.getNumericValue(buffer[ptr++]);
        }

        Num num = new Num(value);
        
        return num;
    }

}
