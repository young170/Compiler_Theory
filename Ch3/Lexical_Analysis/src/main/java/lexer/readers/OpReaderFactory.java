package main.java.lexer.readers;

public class OpReaderFactory extends ReaderFactory {
    
    public InputReader createReader() {
        return new OpReader();
    }

}
