package main.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import main.java.lexer.*;
import main.java.lexer.types.Token;
import main.java.parser.*;

/**
 * SCC: SB C Compiler
 */
public class SCC {

    private static Hashtable<String, Token> symbolTable;
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Compiler <filepath>");
            System.exit(1);
        }

        FileInputStream fileInputStream = null;

        try {
             fileInputStream = new FileInputStream(args[0]);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + args[0]);
        }

        Lexer lexer = new Lexer();
        
        try {
            lexer.scanTokens(fileInputStream);
        } catch (IOException e) {
            System.err.println("File not found: " + args[0]);
            e.printStackTrace();
        }

        // using symbol table in lexer, parse
        symbolTable = lexer.getSymbolTable();

        System.out.println(lexer.getLineCount());
        System.out.println(symbolTable.get("x"));
        
        Parser parser = new Parser();
    }

}
