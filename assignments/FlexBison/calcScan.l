/* calcScan.l */
%{
#include "calcParser.tab.h"
%}

%x COMMENT_CHECK

%%

[ \t\n]+                  { /* ignore whitespace */ }
"--"[^\n]*                { /* ignore comments */ }
"program"                 { return PROGRAM; }
"begin"                   { return BEGIN_BLOCK; }
"end"                     { return END_BLOCK; }
"int"                     { return INT; }
"print_line"              { return PRINT_LINE; }
"if"                      { return IF; }
"else"                    { return ELSE; }
">"                       { return GT; }
"("                       { return '('; }
")"                       { return ')'; }
";"                       { return ';'; }
","                       { return ','; }
"+"                       { return PLUS; }
"-"                       { BEGIN(COMMENT_CHECK); }
"*"                       { return TIMES; }
"/"                       { return DIVIDE; }
[0-9]+                    { yylval.ival = atoi(yytext); return NUMBER; }
[a-zA-Z_][a-zA-Z0-9_]*    { yylval.sval = strdup(yytext); return IDENTIFIER; }
\"[^\"]*\"                { yylval.sval = strdup(yytext); return STRING; }
.                         { return yytext[0]; }

<COMMENT_CHECK>{
    "-"                    { BEGIN(INITIAL); /* ignore this as part of comment */ }
    .                      { unput(yytext[0]); BEGIN(INITIAL); return MINUS; }
}

%%

int yywrap(void) {
    return 1;
}
