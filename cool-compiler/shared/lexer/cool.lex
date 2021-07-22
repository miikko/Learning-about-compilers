/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    int commentNestLvl = 0;
    boolean handleEOF = true;

    // Used when compressing string literals
    String[] specialSubstrings = {"\\b", "\\t", "\\n", "\\f"};
    char[] specialChars = {'\b', '\t', '\n', '\f'};

    private int curr_lineno = 1;
    int get_curr_lineno() {
	    return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	    filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	    return filename;
    }

    // Check whether the next character to be added to string_buf is escaped
    boolean isEscaped() {
        int scanIndex = this.string_buf.length() - 1;
        int backslashCounter = 0;
        while (scanIndex >= 0 && this.string_buf.charAt(scanIndex) != '\n' && this.string_buf.charAt(scanIndex) == '\\') {
            scanIndex--;
            backslashCounter++;
        }
        return backslashCounter % 2 != 0;
    }

    // Converts substrings "//b|t|n|f" into character literals
    String compressSpecialChars(String str) {
        for (int i = 0; i < specialChars.length; i++) {
            char specialChar = specialChars[i];
            String specialSubstr = specialSubstrings[i];
            int substrStart = str.indexOf(specialSubstr);
            while (substrStart != -1) {
                str = str.substring(0, substrStart) + specialChar + str.substring(substrStart + 2);
                substrStart = str.indexOf(specialSubstr, substrStart + 1);
            }
        }
        return str;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    if (this.handleEOF) {
        this.handleEOF = false;
        switch(yy_lexical_state) {
            case YYINITIAL:
                /* nothing special to do in the initial state */
                break;
            case IN_COMMENT:
                return new Symbol(TokenConstants.ERROR, "EOF in comment");
            case IN_STRING:
                return new Symbol(TokenConstants.ERROR, "EOF in string constant");
            default:
                break;
        }
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%state IN_COMMENT IN_STRING INVALID_STRING

%%

<YYINITIAL, IN_COMMENT>"(*" { yybegin(IN_COMMENT); this.commentNestLvl++; }
<IN_COMMENT>"*)" { if (--this.commentNestLvl == 0) { yybegin(YYINITIAL); } }
<IN_COMMENT>[^\n] { }
<YYINITIAL>--.* { }
<YYINITIAL>"*)" { return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }

<YYINITIAL, IN_COMMENT>\n { this.curr_lineno++; }
[ \f\r\t\b\013]+ { }

<YYINITIAL>(I|i)(N|n)(H|h)(E|e)(R|r)(I|i)(T|t)(S|s) { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>(L|l)(O|o)(O|o)(P|p) { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>(P|p)(O|o)(O|o)(L|l) { return new Symbol(TokenConstants.POOL); }
<YYINITIAL>(C|c)(A|a)(S|s)(E|e) { return new Symbol(TokenConstants.CASE); }
<YYINITIAL>(E|e)(S|s)(A|a)(C|c) { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>(N|n)(O|o)(T|t) { return new Symbol(TokenConstants.NOT); }
<YYINITIAL>(I|i)(N|n) { return new Symbol(TokenConstants.IN); }
<YYINITIAL>(C|c)(L|l)(A|a)(S|s)(S|s) { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>(I|i)(F|f) { return new Symbol(TokenConstants.IF); }
<YYINITIAL>(F|f)(I|i) { return new Symbol(TokenConstants.FI); }
<YYINITIAL>(O|o)(F|f) { return new Symbol(TokenConstants.OF); }
<YYINITIAL>(N|n)(E|e)(W|w) { return new Symbol(TokenConstants.NEW); }
<YYINITIAL>(I|i)(S|s)(V|v)(O|o)(I|i)(D|d) { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>(E|e)(L|l)(S|s)(E|e) { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>(W|w)(H|h)(I|i)(L|l)(E|e) { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>(L|l)(E|e)(T|t) { return new Symbol(TokenConstants.LET); }
<YYINITIAL>(T|t)(H|h)(E|e)(N|n) { return new Symbol(TokenConstants.THEN); }
<YYINITIAL>t(R|r)(U|u)(E|e) { return new Symbol(TokenConstants.BOOL_CONST, true); }
<YYINITIAL>f(A|a)(L|l)(S|s)(E|e) { return new Symbol(TokenConstants.BOOL_CONST, false); }

<YYINITIAL>"*" { return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"=>" { return new Symbol(TokenConstants.DARROW); }
<YYINITIAL>"(" { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")" { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>";" { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>"-" { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"<" { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"," { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>"/" { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"+" { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"<-" { return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"." { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"<=" { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"=" { return new Symbol(TokenConstants.EQ); }
<YYINITIAL>":" { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>"~" { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>"{" { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>"}" { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"@" { return new Symbol(TokenConstants.AT); }

<YYINITIAL>\" { yybegin(IN_STRING); }
<IN_STRING>([^\n\"])* { this.string_buf.append(yytext()); }
<IN_STRING>\n {
    this.curr_lineno++;
    // Check whether newline is escaped
    if (!this.isEscaped()) {
        this.string_buf = new StringBuffer();
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
    }
    this.string_buf.append(yytext());
}
<IN_STRING>\" {
    int len = this.string_buf.length();
    if (this.isEscaped()) {
        this.string_buf.append('"');
    } else {
        // Unescape "regular" characters
        String res = this.string_buf.toString().replaceAll("\\\\([^btnf])", "$1");
        res = this.compressSpecialChars(res);
        this.string_buf = new StringBuffer();
        yybegin(YYINITIAL);
        if (res.length() >= CoolLexer.MAX_STR_CONST) {
            return new Symbol(TokenConstants.ERROR, "String constant too long");
        } else if (res.contains("\0")) {
            return new Symbol(TokenConstants.ERROR, "String contains null character");
        }
        return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(res));
    }
}

<YYINITIAL>[A-Z][A-Za-z_0-9]* {
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
<YYINITIAL>[0-9]+ {
    return new Symbol(TokenConstants.INT_CONST, AbstractTable.stringtable.addString(yytext()));
}
<YYINITIAL>[a-z][A-Za-z_0-9]* {
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}

. {/* This rule will match everything not matched by other lexical rules. */
    return new Symbol(TokenConstants.ERROR, yytext());
}
