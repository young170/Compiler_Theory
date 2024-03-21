package main.java.lexer;

import main.java.lexer.types.*;
import main.java.lexer.readers.*;
import java.io.*;
import java.util.Hashtable;

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

    public void iterateBuffer(Lexer lexer, char[] buffer) {
        int ptr = 0;
        ReaderFactory lexReader = null;

        if (Character.isDigit(buffer[ptr])) {
            lexReader = new NumReaderFactory();
        } else if (Character.isAlphabetic(buffer[ptr])) {
            lexReader = new IdReaderFactory();
        }

        lexReader.reader(buffer, ptr);
    }

}
