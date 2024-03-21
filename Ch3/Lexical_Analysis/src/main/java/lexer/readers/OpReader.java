package main.java.lexer.readers;

import main.java.lexer.types.*;
import main.java.lexer.Lexer;

public class OpReader implements InputReader {

    public Token readLexer(char[] buffer, int ptr) {
        StringBuilder sb = new StringBuilder();

        char ch = buffer[ptr];
        String operators = "+-*/%=!^&|<>";
        String operatorParts = "=!<>|^&";

        if (Lexer.isPartOfPattern(operators, ch)) {
            sb.append(ch);
            ptr++;

            while (ptr < buffer.length && Lexer.isPartOfPattern(operatorParts, buffer[ptr])) {
                sb.append(buffer[ptr++]);
            }

            String op = sb.toString();
            return new Word(Tag.OP, op);
        }

        return null;
    }

}
