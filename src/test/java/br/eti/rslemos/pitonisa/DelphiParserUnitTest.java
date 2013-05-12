/*******************************************************************************
 * BEGIN COPYRIGHT NOTICE
 * 
 * This file is part of program "pitonisa"
 * Copyright 2013  Rodrigo Lemos
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * END COPYRIGHT NOTICE
 ******************************************************************************/
package br.eti.rslemos.pitonisa;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DelphiParserUnitTest {
	private static final List<String> ruleNames = Arrays.asList(delphiParser.ruleNames);
	private boolean stderrUsed = false;
	
	@Before
	public void setup() {
		System.setErr(new PrintStream(System.err) {

			@Override
			public void write(int b) {
				stderrUsed = true;
				super.write(b);
			}

			@Override
			public void write(byte[] buf, int off, int len) {
				stderrUsed = true;
				super.write(buf, off, len);
			}

		});
	}
	
	@After
	public void teardown() {
		if (stderrUsed) fail("message sent to stderr");
	}
	
	@Test
	public void testUnit001() throws Exception {
		ParserRuleContext context = parse(getClass().getResourceAsStream("Unit001.pas"));
		assertThat(xpath(context, "/goal").toStringTree(ruleNames), is(equalTo("(goal (unit UNIT (ident Unit001) ; .))")));
	}

	@Test
	public void testUnit002() throws Exception {
		ParserRuleContext context = parse(getClass().getResourceAsStream("Unit002.pas"));
		assertThat(xpath(context, "/goal/unit").toStringTree(ruleNames), is(equalTo("(unit UNIT (ident Unit002) ; (interfaceSection INTERFACE) .)")));
	}

	@Test
	public void testUnit003() throws Exception {
		ParserRuleContext context = parse(getClass().getResourceAsStream("Unit003.pas"));
		assertThat(xpath(context, "/goal/unit").toStringTree(ruleNames), is(equalTo("(unit UNIT (ident Unit003) ; (implementationSection IMPLEMENTATION) .)")));
	}

	@Test
	public void testUnit004() throws Exception {
		ParserRuleContext context = parse(getClass().getResourceAsStream("Unit004.pas"));
		assertThat(xpath(context, "/goal/unit/interfaceSection").toStringTree(ruleNames), is(equalTo("(interfaceSection INTERFACE (usesClause USES (identList (ident uses0)) ;))")));
	}

	@Test
	public void testUnit005() throws Exception {
		ParserRuleContext context = parse(getClass().getResourceAsStream("Unit005.pas"));
		assertThat(xpath(context, "/goal/unit/interfaceSection").toStringTree(ruleNames), is(equalTo("(interfaceSection INTERFACE (usesClause USES (identList (ident uses0) , (ident uses1)) ;))")));
	}

	@Test
	public void testUnit006() throws Exception {
		ParserRuleContext context = parse(getClass().getResourceAsStream("Unit006.pas"));
		assertThat(xpath(context, "/goal/unit/interfaceSection").toStringTree(ruleNames), is(equalTo("(interfaceSection INTERFACE (interfaceDecl (constSection CONST (constDecl (ident const0) = (constExpr (number 10))) ; (constDecl (ident const1) = (constExpr (string 'value'))) ;)))")));
	}

	@Test
	public void testUnit007() throws Exception {
		ParserRuleContext context = parse(getClass().getResourceAsStream("Unit007.pas"));
		assertThat(xpath(context, "/goal/unit/interfaceSection").toStringTree(ruleNames), is(equalTo("(interfaceSection INTERFACE (interfaceDecl (varSection VAR (varDecl (identList (ident var0) , (ident var1)) : (type (simpleType (ordinalType (ordIdent INTEGER)))) = (constExpr (number 10))) ; (varDecl (identList (ident var2)) : (type (typeId (ident string)))) ; (varDecl (identList (ident var3)) : (type (typeId (ident Byte))) ABSOLUTE (ident var2)) ;)))")));
	}

	@Test
	public void testUnit008() throws Exception {
		ParserRuleContext context = parse(getClass().getResourceAsStream("Unit008.pas"));
		assertThat(xpath(context, "/goal/unit/interfaceSection/interfaceDecl[0]").toStringTree(ruleNames), is(equalTo("(interfaceDecl (exportedHeading (functionHeading FUNCTION (ident function0) (formalParms ( (formalParm (parameter (identList (ident parm0) , (ident parm1)) : (parameterType STRING))) ; (formalParm (parameter (identList (ident parm2)) : (parameterType (simpleType (realType CURRENCY))))) ; (formalParm (parameter (identList (ident parm3) , (ident parm4) , (ident parm5)) : (parameterType (simpleType (ordinalType (ordIdent INTEGER)))))) )) : (returnType (simpleType (ordinalType (ordIdent BOOLEAN))))) ;))")));
		assertThat(xpath(context, "/goal/unit/interfaceSection/interfaceDecl[1]").toStringTree(ruleNames), is(equalTo("(interfaceDecl (exportedHeading (functionHeading FUNCTION (ident function1) (formalParms ( )) : (returnType STRING)) ;))")));
		assertThat(xpath(context, "/goal/unit/interfaceSection/interfaceDecl[2]").toStringTree(ruleNames), is(equalTo("(interfaceDecl (exportedHeading (functionHeading FUNCTION (ident function2) (formalParms ( (formalParm (parameter (ident dt) : (parameterType (qualId (ident TDatetime))) = (constExpr (number 0)))) )) : (returnType (qualId (ident TDate)))) ;))")));
		assertThat(xpath(context, "/goal/unit/interfaceSection/interfaceDecl[3]").toStringTree(ruleNames), is(equalTo("(interfaceDecl (exportedHeading (procedureHeading PROCEDURE (ident procedure0) (formalParms ( (formalParm (parameter (identList (ident parm0) , (ident parm1)) : (parameterType (simpleType (ordinalType (ordIdent INTEGER)))))) ))) ;))")));
		assertThat(xpath(context, "/goal/unit/interfaceSection/interfaceDecl[4]").toStringTree(ruleNames), is(equalTo("(interfaceDecl (exportedHeading (procedureHeading PROCEDURE (ident procedure1) (formalParms ( ))) ;))")));
		assertThat(xpath(context, "/goal/unit/interfaceSection/interfaceDecl[5]").toStringTree(ruleNames), is(equalTo("(interfaceDecl (exportedHeading (procedureHeading PROCEDURE (ident procedure2) (formalParms ( (formalParm (parameter (identList (ident parm0)) : ARRAY OF (parameterType STRING))) ))) ;))")));
	}

	private ParserRuleContext xpath(ParserRuleContext context, String path) {
		return ANTLRXPath.xpath(context, path, ruleNames);
	}

	private ParserRuleContext parse(InputStream input) throws Exception {
		TokenSource lexer = new delphiLexer(new ANTLRInputStream(input));
		delphiParser parser = new delphiParser(new CommonTokenStream(lexer));
		return parser.goal();
	}
}
