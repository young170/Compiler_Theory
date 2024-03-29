package main.java.lexer;

import main.java.lexer.types.*;
import main.java.lexer.readers.*;
import java.io.*;
import java.util.Hashtable;

public class Lexer {

    private char[][] buffers;
    private int lineCount;
    private Env env;

    private static final String digit_pattern = "\\d";
    private static final String alphabet_pattern = "[a-zA-Z]";
    private static final String operator_pattern = "[\\+\\-\\*/%=!^&|<>]";

    private void reserve(Word w) {
        env.getTable().put(w.lexeme, w);
    }

    public Lexer() {
        // insert constants to symbol table at initialization
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.FALSE, "false"));

        env = new Env(null); // init new symbol table tree, environment
    }

    public void scanTokens(FileInputStream inputStream) throws IOException {
        InputStreamReader reader = new InputStreamReader(inputStream);

        buffers = new char[2][4096];
        int currentBufferIndex = 0;
        ReaderFactory lexReader = null;

        while (true) {
            // Read data into the current buffer
            int bytesRead = reader.read(buffers[currentBufferIndex]);
            if (bytesRead == -1) {
                break; // EOF
            }
            
            // Iterate through the buffer
            for (int ptr = 0; ptr < bytesRead; ptr++) {
                char[] buffer = buffers[currentBufferIndex];
                char currentChar = buffer[ptr];

                if (currentChar == '\n') {
                    lineCount++;
                }

                lexReader = streamSpellChecker(currentChar);
                
                // Process the token
                if (lexReader != null) {
                    Token token = lexReader.reader(buffer, ptr);

                    if (token != null) {
                        env.getTable().put(token.toString(), token);
                        ptr += token.toString().length() - 1; // Skip processed characters
                    }
                }
            }
            
            // Switch to the next buffer
            currentBufferIndex = (currentBufferIndex + 1) % 2;
        }
    }

    private static ReaderFactory streamSpellChecker(char c) {
        if (isPartOfPattern(digit_pattern, c)) {
            return new NumReaderFactory();
        } else if (isPartOfPattern(alphabet_pattern, c)) {
            return new IdReaderFactory();
        } else if (isPartOfPattern(operator_pattern, c)) {
            return new OpReaderFactory();
        }
        
        return null;
    }

    public static boolean isPartOfPattern(String pattern, char ch) {
        return pattern.indexOf(ch) != -1;
    }

    public Hashtable<String, Token> getSymbolTable()    { return env.getTable(); }
    public int getLineCount()                           { return lineCount; }

}
