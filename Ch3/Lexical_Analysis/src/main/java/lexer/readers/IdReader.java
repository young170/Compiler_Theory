package main.java.lexer.readers;

import main.java.lexer.types.*;

public class IdReader implements InputReader {

    public Token readLexer(char[] buffer, int ptr) {
        StringBuilder sb = new StringBuilder();

        // not necessary
        // already checked in iterateBuffer()
        if (Character.isLetter(buffer[ptr])) {
            sb.append(buffer[ptr++]);

            while (ptr < buffer.length &&
                    (Character.isLetterOrDigit(buffer[ptr]) || buffer[ptr] == '_' || buffer[ptr] == '-' || buffer[ptr] == '$')) {
                sb.append(buffer[ptr++]);
            }
        }

        String lexeme = sb.toString();
        if (!lexeme.isEmpty()) {
            return new Word(Tag.ID, lexeme);
        }

        return null;
    }
    
}
