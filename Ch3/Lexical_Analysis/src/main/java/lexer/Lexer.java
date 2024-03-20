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
        // insert constants to symbol table at initialization
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
            lexer.buffers = lexer.loadFileIntoBuffers(fileInputStream);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // TODO
        // iterate over buffers using lexmeBegin and forward based on FSM
        /*
         * while (forward_ptr has not reached an eof marker) {
         *      lexer.iterateBuffer(lexer, lexer.buffers);
         *      *** which would iterate using both buffers as buffer pair
         * }
         */
        lexer.iterateBuffer(lexer, lexer.buffers[0]);
        lexer.iterateBuffer(lexer, lexer.buffers[1]);
    }

    public char[][] loadFileIntoBuffers(InputStream inputStream) throws IOException {
        char[][] buffers = new char[2][4096];
        InputStreamReader reader = new InputStreamReader(inputStream);
        
        int bytesRead1 = reader.read(buffers[0], 0, 4096);
        int bytesRead2 = reader.read(buffers[1], 0, 4096);

        // TODO
        // if bytesRead<1,2> is less than 4096, then mark eof
        if (bytesRead1 < buffers[0].length) {
            buffers[0][bytesRead1] = '\0';
        } else if (bytesRead2 < buffers[1].length) {
            buffers[0][bytesRead2] = '\0';
        }

        return buffers;
    }

    private static int matchNumPattern(Lexer lexer, Pattern pattern, char targetChar, char[] buffer, int ptr) {
        if (pattern.matcher(String.valueOf(targetChar)).matches()) {
            int v = 0;

            do {
                v = 10 * v + Character.digit(targetChar, 10);
                targetChar = buffer[ptr++];
            } while (Character.isDigit(targetChar));

            Num num = new Num(v);
            lexer.words.put(Integer.toString(v), num);
        }
        
        return ptr;
    }

    private static int matchAlphaPattern(Lexer lexer, Pattern pattern, char targetChar, char[] buffer, int ptr) {
        if (pattern.matcher(String.valueOf(targetChar)).matches()) {
            StringBuffer b = new StringBuffer();

            do {
                b.append(targetChar);
                targetChar = buffer[ptr++];
            } while (Character.isLetter(targetChar));

            String s = b.toString();

            Word w = (Word) lexer.words.get(s);
            if (w == null) { // entry already in symbol table
                w = new Word(Tag.ID, s);
                lexer.words.put(s, w);
            }
        }

        return ptr;
    }

    public void iterateBuffer(Lexer lexer, char[] buffer) {
        Pattern alphaPattern = Pattern.compile("[a-zA-Z]+");
        Pattern numPattern = Pattern.compile("[0-9]+");

        int lexmeBegin = 0; // Starting pointer of current token in buffer
        int forward = 0; // Ending pointer of current token in buffer
        while (lexmeBegin < buffer.length) {
            char currentChar = buffer[forward++];

            if (currentChar == ' ' || currentChar == '\t') {
                if (lexmeBegin != forward) { // end of token
                    lexmeBegin = forward;
                }

                continue;
            } else if (currentChar == '\n') {
                lexer.lineCount++;
                continue;
            }

            matchNumPattern(lexer, numPattern, currentChar, buffer, currentChar);
            matchAlphaPattern(lexer, alphaPattern, currentChar, buffer, currentChar);
        }
    }

}
