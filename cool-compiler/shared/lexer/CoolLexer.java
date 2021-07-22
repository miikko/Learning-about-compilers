/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int IN_STRING = 2;
	private final int YYINITIAL = 0;
	private final int INVALID_STRING = 3;
	private final int IN_COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		55,
		52,
		78
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NOT_ACCEPT,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"6:8,8:2,7,8:2,4,6:18,8,6,56,6:5,1,3,2,49,47,5,50,48,59:10,51,45,46,43,44,6," +
"55,31,57,29,39,15,33,57,13,9,57:2,23,57,11,25,27,57,17,21,19,41,37,35,57:3," +
"6:4,58,6,32,60,30,40,16,34,60,14,10,60:2,24,60,12,26,28,60,18,22,20,42,38,3" +
"6,60:3,53,6,54,52,6,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,164,
"0,1,2,3,1,4,5,1:2,6,7,8,1,9,1:10,10,1:2,11,12,13:3,1:3,13:6,14,13:4,14,13:3" +
",2,1,15,1:2,16,17,18,19,20,14:16,21,22,23,24,25,1,26,27,4,28,29,30,31,32,33" +
",34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58" +
",59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,13,14,78,79,80,81" +
",82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104")[0];

	private int yy_nxt[][] = unpackFromString(105,61,
"1,2,3,4,5,6,7,8,5,9,10,57,58,135,136,148,149,135,136,152,153,135,136,154,15" +
"5,79,80,156,157,158,159,135,136,82,83,160,161,135,136,135,136,135,136,11,7," +
"12,13,14,15,16,17,18,19,20,21,22,23,135,7,24,136,-1:63,25,-1:61,26,-1:61,5," +
"-1:3,5,-1:57,27,-1:64,135:2,28:2,135:8,162:2,135:10,29:2,135:8,-1:14,135:4," +
"-1:9,136:2,59:2,136:8,163:2,136:10,60:2,136:8,-1:14,136:4,-1:44,32,-1:21,33" +
",-1:37,34,-1:76,24,-1:2,27:3,-1,27:2,-1,27:53,-1:9,135:4,141:2,135:28,-1:14" +
",135:4,-1:9,135:34,-1:14,135:4,-1:9,136:34,-1:14,136:4,1,77:3,56,77:2,53,56" +
",77:47,54,77:4,1,50,76,81,84,81:2,8,84,81:52,-1,77:3,56,77:2,-1,56,77:47,-1" +
",77:4,-1:9,135:6,85:2,135:8,87:2,135:16,-1:14,135:4,-1:9,136:6,86:2,136:8,8" +
"8:2,136:16,-1:14,136:4,-1:9,136:4,142:2,136:28,-1:14,136:4,-1:3,51,-1:58,77" +
":6,-1,77:48,-1,77:4,1,7:3,5,7:2,-1,5,7:52,-1:9,135:24,30:2,135:8,-1:14,135:" +
"4,-1:9,136:24,61:2,136:8,-1:14,136:4,-1:9,31:2,135:32,-1:14,135:4,-1:9,62:2" +
",136:20,102:2,136:10,-1:14,136:4,-1:9,135:26,35:2,135:6,-1:14,135:4,-1:9,13" +
"6:26,63:2,136:6,-1:14,136:4,-1:9,135:10,36:2,135:22,-1:14,135:4,-1:9,136:10" +
",64:2,136:22,-1:14,136:4,-1:9,135:22,101:2,135:10,-1:14,135:4,-1:9,136:22,1" +
"06:2,136:10,-1:14,136:4,-1:9,135:12,103:2,135:20,-1:14,135:4,-1:9,136:12,10" +
"8:2,136:20,-1:14,136:4,-1:9,135:6,105:2,135:26,-1:14,135:4,-1:9,136:6,110:2" +
",136:26,-1:14,136:4,-1:9,135:10,37:2,135:22,-1:14,135:4,-1:9,136:32,112:2,-" +
"1:14,136:4,-1:9,135:16,107:2,135:16,-1:14,135:4,-1:9,136:10,65:2,136:22,-1:" +
"14,136:4,-1:9,115:2,135:32,-1:14,135:4,-1:9,136:16,114:2,136:16,-1:14,136:4" +
",-1:9,135:20,38:2,135:12,-1:14,135:4,-1:9,136:14,145:2,136:18,-1:14,136:4,-" +
"1:9,135:6,39:2,135:26,-1:14,135:4,-1:9,122:2,136:32,-1:14,136:4,-1:9,135:2," +
"40:2,135:30,-1:14,135:4,-1:9,136:20,66:2,136:12,-1:14,136:4,-1:9,135:18,42:" +
"2,135:14,-1:14,135:4,-1:9,136:6,67:2,136:26,-1:14,136:4,-1:9,135:14,43:2,13" +
"5:18,-1:14,135:4,-1:9,136:2,68:2,136:30,-1:14,136:4,-1:9,135:12,121:2,135:2" +
"0,-1:14,135:4,-1:9,136:6,41:2,136:26,-1:14,136:4,-1:9,135:6,44:2,135:26,-1:" +
"14,135:4,-1:9,136:18,69:2,136:14,-1:14,136:4,-1:9,135:14,123:2,135:18,-1:14" +
",135:4,-1:9,136:14,70:2,136:18,-1:14,136:4,-1:9,135:8,146:2,135:24,-1:14,13" +
"5:4,-1:9,136:12,128:2,136:20,-1:14,136:4,-1:9,125:2,135:32,-1:14,135:4,-1:9" +
",136:6,71:2,136:26,-1:14,136:4,-1:9,135:12,45:2,135:20,-1:14,135:4,-1:9,136" +
":14,131:2,136:18,-1:14,136:4,-1:9,135:6,47:2,135:26,-1:14,135:4,-1:9,136:8," +
"147:2,136:24,-1:14,136:4,-1:9,135:30,48:2,135:2,-1:14,135:4,-1:9,132:2,136:" +
"32,-1:14,136:4,-1:9,135:10,129:2,135:22,-1:14,135:4,-1:9,136:12,72:2,136:20" +
",-1:14,136:4,-1:9,135:12,49:2,135:20,-1:14,135:4,-1:9,136:6,46:2,136:26,-1:" +
"14,136:4,-1:9,136:6,73:2,136:26,-1:14,136:4,-1:9,136:30,74:2,136:2,-1:14,13" +
"6:4,-1:9,136:10,134:2,136:22,-1:14,136:4,-1:9,136:12,75:2,136:20,-1:14,136:" +
"4,-1:9,135:22,111:2,135:10,-1:14,135:4,-1:9,136:22,118:2,136:10,-1:14,136:4" +
",-1:9,135:12,113:2,135:20,-1:14,135:4,-1:9,136:12,120:2,136:20,-1:14,136:4," +
"-1:9,135:6,117:2,135:26,-1:14,135:4,-1:9,136:6,124:2,136:26,-1:14,136:4,-1:" +
"9,135:16,109:2,135:16,-1:14,135:4,-1:9,136:16,116:2,136:16,-1:14,136:4,-1:9" +
",136:12,130:2,136:20,-1:14,136:4,-1:9,127:2,135:32,-1:14,135:4,-1:9,133:2,1" +
"36:32,-1:14,136:4,-1:9,135:12,89:2,91:2,135:18,-1:14,135:4,-1:9,136:12,90:2" +
",92:2,136:18,-1:14,136:4,-1:9,135:16,119:2,135:16,-1:14,135:4,-1:9,136:16,1" +
"26:2,136:16,-1:14,136:4,-1:9,135:4,93:2,135:28,-1:14,135:4,-1:9,136:4,94:2," +
"136:2,96:2,136:24,-1:14,136:4,-1:9,135:6,95:2,135:8,97:2,135:16,-1:14,135:4" +
",-1:9,136:6,98:2,136:8,100:2,136:16,-1:14,136:4,-1:9,135:16,143:2,135:16,-1" +
":14,135:4,-1:9,136:16,144:2,136:16,-1:14,136:4,-1:9,135:14,137:2,135:6,139:" +
"2,135:10,-1:14,135:4,-1:9,136:14,138:2,136:6,140:2,136:10,-1:14,136:4,-1:9," +
"135:4,99:2,135:28,-1:14,135:4,-1:9,136:4,104:2,136:28,-1:14,136:4,-1:9,135:" +
"28,150:2,135:4,-1:14,135:4,-1:9,136:28,151:2,136:4,-1:14,136:4");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

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
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.MULT); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -5:
						break;
					case 5:
						{ }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.MINUS); }
					case -7:
						break;
					case 7:
						{/* This rule will match everything not matched by other lexical rules. */
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -8:
						break;
					case 8:
						{ this.curr_lineno++; }
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -10:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.EQ); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.SEMI); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.LT); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.COMMA); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.DIV); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.PLUS); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.DOT); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.COLON); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.NEG); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -22:
						break;
					case 22:
						{ return new Symbol(TokenConstants.AT); }
					case -23:
						break;
					case 23:
						{ yybegin(IN_STRING); }
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.INT_CONST, AbstractTable.stringtable.addString(yytext()));
}
					case -25:
						break;
					case 25:
						{ yybegin(IN_COMMENT); this.commentNestLvl++; }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
					case -27:
						break;
					case 27:
						{ }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.IN); }
					case -29:
						break;
					case 29:
						{ return new Symbol(TokenConstants.IF); }
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.OF); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.FI); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.DARROW); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LE); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.NEW); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NOT); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.LET); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.ESAC); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.ELSE); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.THEN); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.LOOP); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.POOL); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.CASE); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.CLASS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.WHILE); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -50:
						break;
					case 50:
						{ }
					case -51:
						break;
					case 51:
						{ if (--this.commentNestLvl == 0) { yybegin(YYINITIAL); } }
					case -52:
						break;
					case 52:
						{ this.string_buf.append(yytext()); }
					case -53:
						break;
					case 53:
						{
    this.curr_lineno++;
    // Check whether newline is escaped
    if (!this.isEscaped()) {
        this.string_buf = new StringBuffer();
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
    }
    this.string_buf.append(yytext());
}
					case -54:
						break;
					case 54:
						{
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
					case -55:
						break;
					case 56:
						{ }
					case -56:
						break;
					case 57:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -57:
						break;
					case 58:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -58:
						break;
					case 59:
						{ return new Symbol(TokenConstants.IN); }
					case -59:
						break;
					case 60:
						{ return new Symbol(TokenConstants.IF); }
					case -60:
						break;
					case 61:
						{ return new Symbol(TokenConstants.OF); }
					case -61:
						break;
					case 62:
						{ return new Symbol(TokenConstants.FI); }
					case -62:
						break;
					case 63:
						{ return new Symbol(TokenConstants.NEW); }
					case -63:
						break;
					case 64:
						{ return new Symbol(TokenConstants.NOT); }
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.LET); }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.ESAC); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.ELSE); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.THEN); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.LOOP); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.POOL); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.CASE); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.CLASS); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.WHILE); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -75:
						break;
					case 76:
						{ }
					case -76:
						break;
					case 77:
						{ this.string_buf.append(yytext()); }
					case -77:
						break;
					case 79:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -78:
						break;
					case 80:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -79:
						break;
					case 81:
						{ }
					case -80:
						break;
					case 82:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -81:
						break;
					case 83:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -82:
						break;
					case 84:
						{ }
					case -83:
						break;
					case 85:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -84:
						break;
					case 86:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -85:
						break;
					case 87:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -86:
						break;
					case 88:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -87:
						break;
					case 89:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -88:
						break;
					case 90:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -89:
						break;
					case 91:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -90:
						break;
					case 92:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -91:
						break;
					case 93:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -92:
						break;
					case 94:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -93:
						break;
					case 95:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -94:
						break;
					case 96:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -95:
						break;
					case 97:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -96:
						break;
					case 98:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -97:
						break;
					case 99:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -98:
						break;
					case 100:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -99:
						break;
					case 101:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -100:
						break;
					case 102:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -101:
						break;
					case 103:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -102:
						break;
					case 104:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -103:
						break;
					case 105:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -104:
						break;
					case 106:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -105:
						break;
					case 107:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -106:
						break;
					case 108:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -107:
						break;
					case 109:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -108:
						break;
					case 110:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -109:
						break;
					case 111:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -110:
						break;
					case 112:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -111:
						break;
					case 113:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -112:
						break;
					case 114:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -113:
						break;
					case 115:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -114:
						break;
					case 116:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -115:
						break;
					case 117:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -116:
						break;
					case 118:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -117:
						break;
					case 119:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -118:
						break;
					case 120:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -119:
						break;
					case 121:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -120:
						break;
					case 122:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -121:
						break;
					case 123:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -122:
						break;
					case 124:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -123:
						break;
					case 125:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -124:
						break;
					case 126:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -125:
						break;
					case 127:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -126:
						break;
					case 128:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -127:
						break;
					case 129:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -128:
						break;
					case 130:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -129:
						break;
					case 131:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -130:
						break;
					case 132:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -131:
						break;
					case 133:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -132:
						break;
					case 134:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -133:
						break;
					case 135:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -134:
						break;
					case 136:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -135:
						break;
					case 137:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -136:
						break;
					case 138:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -137:
						break;
					case 139:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -138:
						break;
					case 140:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -139:
						break;
					case 141:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -140:
						break;
					case 142:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -141:
						break;
					case 143:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -142:
						break;
					case 144:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -143:
						break;
					case 145:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -144:
						break;
					case 146:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -145:
						break;
					case 147:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -146:
						break;
					case 148:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -147:
						break;
					case 149:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -148:
						break;
					case 150:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -149:
						break;
					case 151:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -150:
						break;
					case 152:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -151:
						break;
					case 153:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -152:
						break;
					case 154:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -153:
						break;
					case 155:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -154:
						break;
					case 156:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -155:
						break;
					case 157:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -156:
						break;
					case 158:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -157:
						break;
					case 159:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -158:
						break;
					case 160:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -159:
						break;
					case 161:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -160:
						break;
					case 162:
						{
    return new Symbol(TokenConstants.TYPEID, AbstractTable.stringtable.addString(yytext()));
}
					case -161:
						break;
					case 163:
						{
    return new Symbol(TokenConstants.OBJECTID, AbstractTable.stringtable.addString(yytext()));
}
					case -162:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
