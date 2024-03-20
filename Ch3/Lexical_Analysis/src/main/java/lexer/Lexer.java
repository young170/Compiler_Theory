package main.java.lexer;

import main.java.lexer.types.*;
import java.io.*;
import java.util.Hashtable;
import java.util.regex.Pattern;

public class Lexer {

    private char[][] buffers;
    private int lineCount;

    // temporary symbol table
    private Hashtable<String, Token> words = new Hashtable<String, Token>();

    void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    public Lexer() {
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.FALSE, "false"));
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Lexer <filepath>");
            System.exit(1);
        }

        String filePath = args[0];
        Lexer lexer = new Lexer();

        // load file data into both buffer pairs
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            lexer.buffers = loadFileIntoBuffers(fileInputStream);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // iterate over buffers using lexmeBegin and forward based on FSM
        iterateBuffer(lexer, lexer.buffers[0]);
        iterateBuffer(lexer, lexer.buffers[1]);
    }

    private static char[][] loadFileIntoBuffers(InputStream inputStream) throws IOException {
        char[][] buffers = new char[2][4096];
        InputStreamReader reader = new InputStreamReader(inputStream);
        
        int bytesRead1 = reader.read(buffers[0], 0, 4096);
        int bytesRead2 = reader.read(buffers[1], 0, 4096);

        // if bytesRead<1,2> is less than 4096, then mark eof

        return buffers;
    }

    private static void iterateBuffer(Lexer lexer, char[] buffer) {
        Pattern alphaPattern = Pattern.compile("[a-zA-Z]+");
        Pattern numPattern = Pattern.compile("[0-9]+");

        int lexmeBegin = 0; // Starting pointer to char in buffer
        while (lexmeBegin < buffer.length) {
            char currentChar = buffer[lexmeBegin++];

            if (currentChar == ' ' || currentChar == '\n') {
                continue;
            } else if (currentChar == '\n') {
                lexer.lineCount++;
                continue;
            }

            if (numPattern.matcher(String.valueOf(currentChar)).matches()) {
                int v = 0;

                do {
                    v = 10 * v + Character.digit(currentChar, 10);
                    currentChar = buffer[lexmeBegin++];
                } while (Character.isDigit(currentChar));

                // create entry of Num in symbol table
            }

            if (alphaPattern.matcher(String.valueOf(currentChar)).matches()) {
                StringBuffer b = new StringBuffer();

                do {
                    b.append(currentChar);
                    currentChar = buffer[lexmeBegin++];
                } while (Character.isLetter(currentChar));

                String s = b.toString();

                Word w = (Word) lexer.words.get(s);
                if (w != null) { // entry already in symbol table
                    continue;
                }

                w = new Word(Tag.ID, s);
                lexer.words.put(s, w);
            }
        }
    }

}
