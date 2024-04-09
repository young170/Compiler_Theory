public class StringLiteralDFAReaderFactory extends DFAReaderFactory {
    
    @Override
    SymbolScanner createDFAReader() {
        return new StringLiteralReader();
    }

}
