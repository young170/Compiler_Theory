public abstract class DFAReaderFactory {
    
    abstract SymbolScanner createDFAReader();

    public Token scanLexUnit(char[] buffer, int ptr) {
        SymbolScanner symbolScanner = createDFAReader();

        return symbolScanner.scanLexUnit(buffer, ptr);
    }

}
