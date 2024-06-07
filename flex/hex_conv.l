%{
#include <stdio.h>
int count = 0;
%}
digit [0-9]
number {digit}+
%option noyywrap
%%
{number} {int n = atoi(yytext);
		 printf("%x", n);
		 if (n > 9) count++;}
%%
main()
{ yylex();
	fprintf(stderr, "number of replacements = %d", count);
	return 0;
}
