/*
 * Copyright 2007, 2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
header {
package net.sf.morph2.transform.copiers.dsl;

import java.util.List;
import java.util.ArrayList;

import net.sf.morph2.transform.Copier;
import net.sf.morph2.util.ClassUtils;
}

class MorphParser extends Parser;
options {
	k = 2;
	defaultErrorHandler = false;
	buildAST = false;
	exportVocab = Morph;
//	codeGenBitsetTestThreshold=999;
}

tokens {
	TYPE;
}

/* public */ parse[DSLDefinedCopier parent] returns [List result = new ArrayList()]
	:	( classCopier[parent, result] )+
	;

protected classCopier[DSLDefinedCopier parent, List list]
{
	CopierDef copierDef = null;
	Class leftClass = null;
	Direction direction = null;
	Class rightClass = null;
}
	:	leftClass=javaClass direction=direction rightClass=javaClass
		{ copierDef = new CopierDef(parent, leftClass, direction, rightClass); }
		LCURLY copierChild[copierDef] ( COMMA copierChild[copierDef] )* RCURLY
		{ list.add(copierDef); }
	;

// this rule is a little weird merging property in/ex-clusion and mapping, but it's unambiguous this way:
protected copierChild[CopierDef copierDef]
{
	String p1 = null;
	Direction direction = null;
	String p2 = null;
}
	:	STAR { copierDef.setMatchProperties(true); }
	|	BANG p1=property { copierDef.getIgnoreProperties().add(p1); }
	|	SHORT_LEFT p1=property { copierDef.getPropertyMap(Direction.LEFT).put(p1, p1); }
	|	p1=property
		( SHORT_RIGHT { direction = Direction.RIGHT; p2 = p1; } | ( direction=direction p2=property ) )? {
			if (direction == null) {
				copierDef.getIncludeProperties().add(p1);
			} else {
				copierDef.getPropertyMap(direction).put(p1, p2);
			}
		}
	;

protected property returns [String s = null]
{ StringBuffer buf = new StringBuffer(); }
	:	( i:IDENT { buf.append(i.getText()); }
		| n:NUMBER { buf.append(n.getText()); }
		)
		( t:~(STAR | BIDI | LEFT | SHORT_LEFT | RIGHT | SHORT_RIGHT | COMMA | LCURLY | RCURLY | BANG) { buf.append(t.getText()); } )*
		{ s = buf.toString(); }
	;

protected javaClass returns [Class c = null]
{ StringBuffer sb = new StringBuffer(); String n = null; }
	:	i:IDENT { sb.append(i.getText()); }
		(DOT ii:IDENT { sb.append('.').append(ii.getText()); } )*
		{ c = ClassUtils.convertToClass(sb.toString()); }
	;

protected direction returns [Direction d = null]
	:	BIDI { d = Direction.BIDI; }
	|	RIGHT { d = Direction.RIGHT; }
	|	LEFT { d = Direction.LEFT; }
	;

//-------------------------------------------------------
class MorphLexer extends Lexer;

options {
	k = 2;
    charVocabulary='\u0003'..'\uFFFF';
}

BIDI : ':' ;
RIGHT : "=>";
SHORT_RIGHT : "->";
LEFT : "<=";
SHORT_LEFT : "<-";
COMMA : ',' ;
LCURLY : '{' ;
RCURLY : '}' ;
STAR : '*' ;
BANG : '!' ;
DOT : '.' ;

WS	:	(	' '
		|	'\t'
		|	'\f'
		|	NL
		) {$setType(Token.SKIP);}
	;

// Single-line comments
SL_COMMENT
	:	"//"
		(~('\n'|'\r'))* (NL)?
		{$setType(Token.SKIP);}
	;

// multiple-line comments
ML_COMMENT
	:	"/*"
		(	/*	'\r' '\n' can be matched in one alternative or by matching
				'\r' in one iteration and '\n' in another.  I am trying to
				handle any flavor of newline that comes in, but the language
				that allows both "\r\n" and "\r" and "\n" to all be valid
				newline is ambiguous.  Consequently, the resulting grammar
				must be ambiguous.  I'm shutting this warning off.
			 */
			options {
				generateAmbigWarnings=false;
			}
		:
			{ LA(2)!='/' }? '*'
		|	NL
		|	~('*'|'\n'|'\r')
		)*
		"*/"
		{$setType(Token.SKIP);}
	;

protected NL
	: ( {LA(2) == '\n'}? '\r' '\n'
       | '\r'
       | '\n'
     )
     { newline(); }
   ;

// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
IDENT
options { testLiterals = true; }
	:	('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'$'|'/'|'*')*
	;

NUMBER : ('0'..'9')+ ;

MISC
	:	('('|')'|'['|']'|'\''|'"')+
	;