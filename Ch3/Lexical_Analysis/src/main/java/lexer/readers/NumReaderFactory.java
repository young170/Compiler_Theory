package main.java.lexer.readers;

public class NumReaderFactory extends ReaderFactory {
    
    public InputReader createReader() {
        return new NumReader();
    }

}
