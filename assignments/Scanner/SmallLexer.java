import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

/**
 * A small lexer that scans a PASCAL-like language.
 * Given a input source code file, fills in the symbol table and outputs a file of the token pairs: name-attribute
 */
public class SmallLexer {

    private static Hashtable<String, String> symbolHashtable; // <token-name, token-attribute>

    /**
     * Processes the given source code file using the double-buffer method.
     * Using fixed sized buffers, the file is iteratively read and processed.
     * 
     * @param args The name of the input source code file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java SmallLexer <filename>");
            return;
        }

        String filename = args[0];
        
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            // initialize the double buffer, fixed size
            char[][] doubleBuffer = new char[2][4096];
            int currentBuffer = 0;
            int bytesRead;
            
            // initial read
            bytesRead = bufferedReader.read(doubleBuffer[currentBuffer]);
            
            // until EOF, read and process buffer-by-buffer by switching iteratively
            while (bytesRead != -1) {
                processBuffer(doubleBuffer[currentBuffer], bytesRead);

                currentBuffer = (currentBuffer + 1) % 2; // 0 <-> 1
                bytesRead = bufferedReader.read(doubleBuffer[currentBuffer]);
            }
            
            bufferedReader.close();
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
            e.printStackTrace();
        }

        // TODO
        // another pass through the input file
        
        // TODO
        // match each unit with an entry/token in the symbolHashtable

        // TODO
        // print pairs to file: name - attribute
    }
    
    /**
     * Given a buffer of source code, creates and fills in the corresponding tokens to the symbol table.
     * 
     * @param buf The buffer to be read, contains source code
     * @param length The length of the buffer, either up to EOF or end of buffer
     */
    public static void processBuffer(char[] buf, int length) {
        int ptr = 0;
        DFAReaderFactory NFAScanner = null;

        while (ptr < length) {
            switch (peek(buf, ptr)) {
                case '\"':
                    NFAScanner = new StringLiteralDFAReaderFactory();
                    break;

                // TODO
                // number
                    
                case '-':
                    if (peek(buf, ptr + 1) == '-') {
                        // TODO
                        // comment
                    } else {
                        // TODO
                        // subtraction operator
                    }
                    break;

                // TODO
                // identifier

                // TODO
                // operators
            
                default:
                    break;
            }

            Token token = NFAScanner.scanLexUnit(buf, ptr);
            symbolHashtable.put(token.getTokenName(), token.getTokenAttribute());

            ptr += token.getTokenName().length();
        }
    }

    /**
     * Peeks the value in the specified location of the buffer.
     * Returns '\0' if is/past EOF
     * 
     * @param buf The buffer to be read from
     * @param pos The position desired to be read in the buffer
     * @return Returns the value read at the position in the buffer
     */
    private static char peek(char[] buf, int pos) {
        return buf[pos];
    }

}
