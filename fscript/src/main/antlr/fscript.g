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

import org.objectweb.fractal.fscript.expressions.*;
import org.objectweb.fractal.fscript.statements.*;
}

class FScriptParser1 extends Parser;
options {
    k = 2;
    importVocab = FScriptLexer;
}

{
    /**
     * Interprets a string litteral found in FScript source code as a
     * Java <code>String</code> value.
     */
    private String stringValue(String litteral) {
        // Remove enclosing quotes.
        String str = litteral.substring(1, litteral.length() - 1);
        // Replace quoted chars with their actual values. We need two
        // quoting levels for backslashes in the first argument: one
        // for standard Java strings, and one because this parameter
        // is interpreted as a regexp.
        str = str.replaceAll("\\\\n", "\n");
        str = str.replaceAll("\\\\r", "\r");
        str = str.replaceAll("\\\\t", "\t");
        str = str.replaceAll("\\\\'", "'");
        str = str.replaceAll("\\\\\"", "\"");
        return str;
    }
}

/**
 * Top-level production for FPath expressions. Alias for orExpression.
 */
expression returns [ Expression result ]
{
    result = null;
}
    : result=orExpression
    ;

orExpression returns [ Expression result ]
{
    result = null;
    List<Expression> clauses = new ArrayList<Expression>();
}
    :        result=andExpression { clauses.add(result); }
        ( OR result=andExpression { clauses.add(result); } )*
        {
            if (clauses.size() == 1) {
                result = clauses.get(0);
            } else {
                result = new OrExpression(clauses);
            }
        }
    ;

andExpression returns [ Expression result ]
{
    result = null;
    List<Expression> clauses = new ArrayList<Expression>();
}
    :         result=comparisonExpression { clauses.add(result); }
        ( AND result=comparisonExpression { clauses.add(result); } )*
        {
            if (clauses.size() == 1) {
                result = clauses.get(0);
            } else {
                result = new AndExpression(clauses);
            }
        }
    ;

comparisonExpression returns [ Expression result ]
{
    result = null;
    Expression rhs = null;
}
    : result=additiveExpression
    ( EQUAL   rhs=additiveExpression { result = new CallExpression("==", result, rhs); }
    | NEQUAL  rhs=additiveExpression { result = new CallExpression("!=", result, rhs); }
    | LESS    rhs=additiveExpression { result = new CallExpression("<",  result, rhs); }
    | GREATER rhs=additiveExpression { result = new CallExpression(">",  result, rhs); }
    | LEQ     rhs=additiveExpression { result = new CallExpression("<=", result, rhs); }
    | GEQ     rhs=additiveExpression { result = new CallExpression(">=", result, rhs); }
    )* // FIXME Sould be '?' but ANTLR then produces errors during parsing...
    ;

additiveExpression returns [ Expression result ]
{
    result = null;
    Expression rhs = null;
}
    :       result=multiplyExpression
    ( PLUS  rhs=multiplyExpression { result = new CallExpression("+", result, rhs); }
    | MINUS rhs=multiplyExpression { result = new CallExpression("-", result, rhs); }
    )*
    ;

multiplyExpression returns [ Expression result ]
{
    result = null;
    Expression rhs = null;
}
    : result=unaryExpression
    ( STAR rhs=unaryExpression { result = new CallExpression("*", result, rhs); }
    | DIV  rhs=unaryExpression { result = new CallExpression("div", result, rhs); }
    )*
    ;

unaryExpression returns [ Expression result ]
{
    result = null;
}
    : result=atom
    | MINUS result=atom  { result = new CallExpression("minus", result); }
    ;

atom returns [ Expression result ]
{
    result = null;
    Step step = null;
    List<Step> steps = new ArrayList<Step>();
}
    : result=literal
    | result=paren
    | result=call ( SLASH step=locationStep { steps.add(step); } )*
        {
            if (steps.size() > 0) {
                result = new FPathExpression(result, steps);
            }
        }
    | result=variableReference ( SLASH step=locationStep { steps.add(step); } )*
        {
            if (steps.size() > 0) {
                result = new FPathExpression(result, steps);
            }
        }
;

relativePath returns [ Expression result ]
{
    result = null;
    Step step = null;
    List<Step> steps = new ArrayList<Step>();
}
    :       step=locationStep { steps.add(step); }
    ( SLASH step=locationStep { steps.add(step); } )*
        {
            result = new FPathExpression(new CallExpression("current"), steps);
        }
    ;

variableReference returns [ Expression result ]
{
    result = null;
}
    : DOLLAR n:NAME { result = new VariableExpression(n.getText()); }
    ;

locationStep returns [Step step]
{
    step = null;
    String axis = null;
    Expression pred = null;
    List<Expression> predicates = new ArrayList<Expression>();
}
    :  a:NAME                             { axis = a.getText(); }
       AXIS_SEP
       pred=nodeTest                      { if (pred != null) { predicates.add(pred); } }
       ( LBRACKET pred=predicate RBRACKET { predicates.add(pred); } )*
    {
        step = new Step(axis, predicates);
    }
    ;

nodeTest returns [ Expression result ]
{
    result = null;
    Expression target = null;
}
    : ( id:NAME { target = new ConstantExpression(id.getText()); }   
      | STAR    { target = null; }
      | target=variableReference
      )
    {
        if (target != null) {
            Expression name = new CallExpression("name", new CallExpression("current"));
            result = new CallExpression("==", target, name);
        } else {
            result = null;
        }
    }
    ;

predicate returns [ Expression result ]
{
    result = null;
}
    : result=expression
    | result=relativePath
    ;

literal returns [ Expression result ]
{
    result = null;
}
    : n:NUMBER { result = new ConstantExpression(Double.valueOf(n.getText())); }
    | s:STRING { result = new ConstantExpression(stringValue(s.getText())); }
    ;

paren returns [ Expression result ]
{
    result = null;
}
    : LPAREN result=expression RPAREN
    ;

call returns [ Expression result ]
{
    result = null;
    List<Expression> args = new ArrayList<Expression>();
}
    : n:NAME LPAREN (args=arguments)? RPAREN
        {
            result = new CallExpression(n.getText(), args);
        }
    | DOT
        {
        	result = new CallExpression("current");
        }
    ;

arguments returns [ List<Expression> result ]
{
    result = new ArrayList<Expression>();
    Expression expr = null;
}
    :       expr=expression { result.add(expr); }
    ( COMMA expr=expression { result.add(expr); } )*
    ;

definitions returns [ List<UserDefinedProcedure> definitions ]
{
    definitions = new ArrayList<UserDefinedProcedure>();
    UserDefinedProcedure proc = null;
}
    : ( proc=procedureDefinition { definitions.add(proc); } )+
    ;

procedureDefinition returns [ UserDefinedProcedure proc ]
{
    proc = null;
    List<String> params = null;
    boolean isAction = false;
    String name = null;
    Statement body = null;
}
    : ( FUNCTION { isAction = false; }
      | ACTION   { isAction = true; }
      )
      n:NAME     { name = n.getText(); }
      params=parameters
      body=blockStatement
    {
        proc = new UserDefinedProcedure(name, isAction, params, body);
    }
    ;

parameters returns [ List<String> result ]
{
    result = new ArrayList<String>();
}
    : LPAREN
    (         p1:NAME { result.add(p1.getText()); }
      ( COMMA p2:NAME { result.add(p2.getText()); } )*
    )?
    RPAREN
    ;

statement returns [ Statement result ]
{
    result = null;
    Expression expr = null;
    Statement stat1 = null, stat2 = null;
}
    : stat1=blockStatement
    { result = stat1; }

    | expr=expression SEMI
    { result = new ExpressionStatement(expr); }

    | IF LPAREN expr=expression RPAREN
      stat1=blockStatement ( ELSE stat2=blockStatement )?
    { result = new IfStatement(expr, stat1, stat2); }

    | VAR n1:NAME ( ASSIGN expr=expression )? SEMI
    { result = new AssignmentStatement(true, n1.getText(), expr); }

    | n2:NAME ASSIGN expr=expression SEMI
    { result = new AssignmentStatement(false, n2.getText(), expr); }

    | RETURN expr=expression SEMI
    { result = new ReturnStatement(expr); }

    | FOR iter:NAME COLON expr=expression stat1=blockStatement
    { result = new ForeachStatement(iter.getText(), expr, stat1); }
    ;

blockStatement returns [ Statement result ]
{
    result = null;
    Statement step = null;
    List<Statement> steps = new ArrayList<Statement>();
}
    : LCURLY ( step=statement { steps.add(step); } )+ RCURLY
    { result = new BlockStatement(steps); }
    ;
