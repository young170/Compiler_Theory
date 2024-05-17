public class LLParser {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java LLParser <filename>");
            return;
        }

        SmallLexer smallLexer = new SmallLexer();
        smallLexer.setPrintTokenList(true);
        smallLexer.lex(args[0]);

        LLParser llParser = new LLParser();
        

        System.out.println("Parsing OK");
    }

    
}
