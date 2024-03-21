package main.java.lexer.readers;

import main.java.lexer.types.Token;

/**
 * InputReader
 */
public interface InputReader {

    Token readLexer(char[] buffer, int ptr);

}