package main.java.lexer.readers;

public class IdReaderFactory extends ReaderFactory {
    
    public InputReader createReader() {
        return new IdReader();
    }

}
