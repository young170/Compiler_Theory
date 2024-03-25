package main.java.lexer;

import java.util.*;
import main.java.lexer.types.*;

public class Env {
    
    private Hashtable<String, Token> table;
    protected Env prev;

    public Env(Env p) {
        table = new Hashtable<String, Token>();
        prev = p;
    }

    public void insert(String s, Token sym) {
        table.put(s, sym);
    }

    public Token lookup(String s) {
        for (Env env = this; env != null; env = env.prev) {
            Token found = env.lookup(s);

            if (found != null) {
                return found;
            }
        }

        return null;
    }

}
