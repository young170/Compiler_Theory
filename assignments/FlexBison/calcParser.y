/* calcParser.y */
%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_VARS 100

typedef struct {
    char name[100];
    int value;
} Variable;

Variable symbol_table[MAX_VARS];
int var_count = 0;

void yyerror(const char *s);
int yylex(void);
int lookup(char *var);
void insert(char *var, int value);
int get_value(char *var);
void set_value(char *var, int value);

%}

%union {
    int ival;
    char *sval;
}

%token <ival> NUMBER
%token <sval> IDENTIFIER STRING
%token INT PRINT_LINE IF ELSE BEGIN_BLOCK END_BLOCK PROGRAM
%token PLUS MINUS TIMES DIVIDE GT

%type <ival> expression term factor condition
%type <sval> var_declaration assignment statement

%%

program:
    PROGRAM IDENTIFIER BEGIN_BLOCK statements END_BLOCK { }
    ;

statements:
    statements statement
    | statement
    ;

statement:
    INT var_declaration ';' { }
    | assignment ';' { }
    | PRINT_LINE '(' expression ')' ';' { printf("%d\n", $3); }
    | PRINT_LINE '(' STRING ')' ';' { printf("%s\n", $3); }
    | IF '(' condition ')' BEGIN_BLOCK statements END_BLOCK ELSE BEGIN_BLOCK statements END_BLOCK { }
    | IF '(' condition ')' BEGIN_BLOCK statements END_BLOCK { }
    ;

var_declaration:
    IDENTIFIER '=' NUMBER { insert($1, $3); }
    | IDENTIFIER '=' NUMBER ',' var_declaration { insert($1, $3); }
    | IDENTIFIER { insert($1, 0); }
    ;

assignment:
    IDENTIFIER '=' expression {
        if (get_value($1) != -999) {
            set_value($1, $3);
        } else {
            exit(1);
        }
    }
    ;

expression:
    expression PLUS term { $$ = $1 + $3; }
    | expression MINUS term { $$ = $1 - $3; }
    | term { $$ = $1; }
    ;

term:
    term TIMES factor { $$ = $1 * $3; }
    | term DIVIDE factor { $$ = $1 / $3; }
    | factor { $$ = $1; }
    ;

factor:
    NUMBER { $$ = $1; }
    | IDENTIFIER { $$ = get_value($1); }
    | '(' expression ')' { $$ = $2; }
    ;

condition:
    expression GT expression { $$ = $1 > $3; }
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Error: %s\n", s);
}

int main(void) {
    return yyparse();
}

void insert(char *var, int value) {
    if (var_count < MAX_VARS) {
        strcpy(symbol_table[var_count].name, var);
        symbol_table[var_count].value = value;
        var_count++;
    } else {
        yyerror("Symbol table overflow");
    }
}

int get_value(char *var) {
    for (int i = 0; i < var_count; i++) {
        if (strcmp(symbol_table[i].name, var) == 0) {
            return symbol_table[i].value;
        }
    }
    yyerror("Undefined variable");
    return -999;
}

void set_value(char *var, int value) {
    for (int i = 0; i < var_count; i++) {
        if (strcmp(symbol_table[i].name, var) == 0) {
            symbol_table[i].value = value;
            return;
        }
    }
    yyerror("Undefined variable");
}
