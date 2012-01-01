header {
/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2007 ARMINES
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.parser;

import java.util.List;
import java.util.ArrayList;
}

/**
 * Lexer for FScript.
 */
class FScriptLexer extends Lexer;
options { k = 2; defaultErrorHandler=false; }

// Keywords
tokens {
  ACTION   = "action";
  FUNCTION = "function";
  VAR      = "var";
  DIV      = "div";
  IF       = "if";
  ELSE     = "else";
  FOR      = "for";
  RETURN   = "return";
}

NAME     : LETTER (NAME_CHAR)*;
NUMBER   : (DIGIT)+ (DOT (DIGIT)* )?;
STRING   : DQ_STRING | SQ_STRING;
PLUS     : '+';
MINUS    : '-';
STAR     : '*';
EQUAL    : "==";
NEQUAL   : "!=";
LESS     : '<';
GREATER  : '>';
LEQ      : "<=";
GEQ      : ">=";
COMMA    : ',';
LPAREN   : '(';
RPAREN   : ')';
LBRACKET : '[';
RBRACKET : ']';
SLASH    : '/';
DOT      : '.';
DOLLAR   : '$';
AT       : '@';
AXIS_SEP : "::";
ASSIGN   : "=";
SEMI     : ";";
COLON    : ":";
LCURLY   : "{";
RCURLY   : "}";
AND      : "&&";
OR       : "||";
NS_UNION : '|';
NS_INTER : '&';
NS_DIFF  : '\\';

// Whitespace -- ignored
WS  :   ( ' '
        | '\t'
        | '\f'
        | (options {generateAmbigWarnings=false;} :
                "\r\n" | '\r' | '\n' )
          { newline(); }
        )+
        { $setType(Token.SKIP); }
    ;

// Single-line comments
SL_COMMENT
    :   "//"
        (~('\n'|'\r'))* ('\n'|'\r'('\n')?)
        {$setType(Token.SKIP); newline();}
    ;

protected DQ_STRING: ('"'  (ESC | ~('\\'|'"'|'\n'))* '"' );
protected SQ_STRING: ('\'' (ESC | ~('\\'|'\''|'\n'))* '\'');
protected ESC: '\\' ('n' | 'r' | 't' | '\'' | '"');
protected DIGIT: ('0'..'9');
protected NAME_CHAR: (LETTER | DIGIT | MINUS);
protected LETTER :   '\u0041'..'\u005a'
    | '\u005f'
    | '\u0061'..'\u007a'
    | '\u00c0'..'\u00d6'
    | '\u00d8'..'\u00f6'
    | '\u00f8'..'\u00ff'
    | '\u0100'..'\u1fff'
    | '\u3040'..'\u318f'
    | '\u3300'..'\u337f'
    | '\u3400'..'\u3d2d'
    | '\u4e00'..'\u9fff'
    | '\uf900'..'\ufaff'
    ;
