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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.Before;
import org.junit.Test;

import br.eti.rslemos.pitonisa.delphiParser.GoalContext;
import br.eti.rslemos.pitonisa.delphiParser.ImplementationSectionContext;
import br.eti.rslemos.pitonisa.delphiParser.InterfaceSectionContext;
import br.eti.rslemos.pitonisa.delphiParser.UnitContext;

public class DelphiUnitParserUnitTest extends AbstractDelphiParserUnitTest {
	private GoalContext goal;

	@Before
	public void setupUnit() throws Exception {
		goal = parse(
				"(*******************************************************************************\n" +
				" * MULTI LINE COMMENT STYLE                                                    *\n" +
				" *******************************************************************************)\n" +
				"{\n" +
				" MULTI LINE COMMENT STYLE \n" +
				"}\n" +
				"\n" +
				"UNIT unitname;\n" +
				"\n" +
				"INTERFACE\n" +
				"USES\n" +
				"  uses0, uses1;\n" +
				"\n" +
				"  CONST const0 = 10; const1 = 'value';\n" +
				"\n" +
				"  VAR\n" +
				"    var0, var1: INTEGER = 10;\n" +
				"    var2: string;\n" +
				"    var3: Byte ABSOLUTE var2;\n" +
				"\n" +
				"  FUNCTION  function0  (parm0, parm1 : STRING; parm2 : CURRENCY; parm3, parm4, parm5 : INTEGER): BOOLEAN;\n" +
				"  FUNCTION  function1 () : STRING;\n" +
				"  FUNCTION  function2 (dt:TDatetime=0): TDate;\n" +
				"  PROCEDURE procedure0 (parm0, parm1 : INTEGER);\n" +
				"  PROCEDURE procedure1();\n" +
				"  PROCEDURE procedure2 (parm0: ARRAY OF STRING);\n" +
				"\n" +
				"\n" +
				"IMPLEMENTATION\n" +
				"USES\n" +
				"  uses0, uses1, uses2;\n" +
				"\n" +
				"CONST\n"+
				"  iconst0 : STRING = 'value';\n" +
				"  iconst1 : STRING = 'value';\n" +
				"  iconst2 : INTEGER = 10;\n" +
				"\n" +
				"VAR\n"+
				"  var0 : TIniFile;\n" +
				"  var1 : ARRAY OF STRING;\n" +
				"  var2 : ARRAY[1..5] OF STRING;\n" +
				"\n" +
				"PROCEDURE procedure0 (parm0, parm1 : INTEGER);\n" +
				"BEGIN\n" +
				"INHERITED;\n" +
				"END;\n" +
				"\n" +
				"FUNCTION function1 () : STRING;" +
				"BEGIN\n" +
				"INHERITED;\n" +
				"END;\n" +
				".\n" +
				"\n" 
			);
	}
	
	@Test
	public void testUnitname() throws Exception {
		assertThat(unit().ident().toStringTree(ruleNames), is(equalTo("(ident unitname)")));
	}

	@Test
	public void testHasInterfaceSection() throws Exception {
		assertThat(interfaceSection(), is(not(nullValue(ParserRuleContext.class))));
	}

	@Test
	public void testHasImplementationSection() throws Exception {
		assertThat(implementationSection(), is(not(nullValue(ParserRuleContext.class))));
	}

	@Test
	public void testInterfaceUsesClause() throws Exception {
		assertThat(interfaceSection().usesClause().toStringTree(ruleNames), is(equalTo("(usesClause USES (identList (ident uses0) , (ident uses1)) ;)")));
	}

	@Test
	public void testInterfaceConstSection() throws Exception {
		assertThat(interfaceSection().interfaceDecl(0).constSection().toStringTree(ruleNames), is(equalTo("(constSection CONST (constDecl (ident const0) = (constExpr (number 10))) ; (constDecl (ident const1) = (constExpr (string 'value'))) ;)")));
	}

	@Test
	public void testInterfaceVarSection() throws Exception {
		assertThat(interfaceSection().interfaceDecl(1).varSection().toStringTree(ruleNames), is(equalTo("(varSection VAR (varDecl (identList (ident var0) , (ident var1)) : (type (simpleType (ordinalType (ordIdent INTEGER)))) = (constExpr (number 10))) ; (varDecl (identList (ident var2)) : (type (typeId (ident string)))) ; (varDecl (identList (ident var3)) : (type (typeId (ident Byte))) ABSOLUTE (ident var2)) ;)")));
	}

	@Test
	public void testInterfactExportedFunction() throws Exception {
		assertThat(interfaceSection().interfaceDecl(2).exportedHeading().functionHeading().toStringTree(ruleNames), is(equalTo("(functionHeading FUNCTION (ident function0) (formalParms ( (formalParm (parameter (identList (ident parm0) , (ident parm1)) : (parameterType STRING))) ; (formalParm (parameter (identList (ident parm2)) : (parameterType (simpleType (realType CURRENCY))))) ; (formalParm (parameter (identList (ident parm3) , (ident parm4) , (ident parm5)) : (parameterType (simpleType (ordinalType (ordIdent INTEGER)))))) )) : (returnType (simpleType (ordinalType (ordIdent BOOLEAN)))))")));
	}
	
	@Test
	public void testInterfactExportedProcedure() throws Exception {
		assertThat(interfaceSection().interfaceDecl(5).exportedHeading().procedureHeading().toStringTree(ruleNames), is(equalTo("(procedureHeading PROCEDURE (ident procedure0) (formalParms ( (formalParm (parameter (identList (ident parm0) , (ident parm1)) : (parameterType (simpleType (ordinalType (ordIdent INTEGER)))))) )))")));
	}
	
	@Test
	public void testInterfactExportedFunctionWithReturnTypeString() throws Exception {
		assertThat(interfaceSection().interfaceDecl(3).exportedHeading().functionHeading().toStringTree(ruleNames), is(equalTo("(functionHeading FUNCTION (ident function1) (formalParms ( )) : (returnType STRING))")));
	}
	
	@Test
	public void testInterfactExportedFunctionWithParameterTypeQualifiedId() throws Exception {
		assertThat(interfaceSection().interfaceDecl(4).exportedHeading().functionHeading().toStringTree(ruleNames), is(equalTo("(functionHeading FUNCTION (ident function2) (formalParms ( (formalParm (parameter (ident dt) : (parameterType (qualId (ident TDatetime))) = (constExpr (number 0)))) )) : (returnType (qualId (ident TDate))))")));
	}
	
	@Test
	public void testInterfactExportedProcedureWithFormalParametersWithoutParameters() throws Exception {
		assertThat(interfaceSection().interfaceDecl(6).exportedHeading().procedureHeading().toStringTree(ruleNames), is(equalTo("(procedureHeading PROCEDURE (ident procedure1) (formalParms ( )))")));
	}
	
	@Test
	public void testInterfactExportedProcedureWithParameterArrayOfString() throws Exception {
		assertThat(interfaceSection().interfaceDecl(7).exportedHeading().procedureHeading().toStringTree(ruleNames), is(equalTo("(procedureHeading PROCEDURE (ident procedure2) (formalParms ( (formalParm (parameter (identList (ident parm0)) : ARRAY OF (parameterType STRING))) )))")));
	}

	@Test
	public void testImplementationUsesClause() throws Exception {
		assertThat(implementationSection().usesClause().toStringTree(ruleNames), is(equalTo("(usesClause USES (identList (ident uses0) , (ident uses1) , (ident uses2)) ;)")));
	}

	@Test
	public void testImplementationConstSection() throws Exception {
		assertThat(implementationSection().declSection(0).constSection().toStringTree(ruleNames), is(equalTo("(constSection CONST (constDecl (ident iconst0) : (type (stringType STRING)) = (typedConstant (constExpr (string 'value')))) ; (constDecl (ident iconst1) : (type (stringType STRING)) = (typedConstant (constExpr (string 'value')))) ; (constDecl (ident iconst2) : (type (simpleType (ordinalType (ordIdent INTEGER)))) = (typedConstant (constExpr (number 10)))) ;)")));
	}

	@Test
	public void testImplementationVarSection() throws Exception {
		assertThat(implementationSection().declSection(1).varSection().varDecl(0).toStringTree(ruleNames), is(equalTo("(varDecl (identList (ident var0)) : (type (typeId (ident TIniFile))))")));
	}

	@Test
	public void testImplementationVarSectionDynamicArrayOf() throws Exception {
		assertThat(implementationSection().declSection(1).varSection().varDecl(1).toStringTree(ruleNames), is(equalTo("(varDecl (identList (ident var1)) : ARRAY OF (type (stringType STRING)))")));
	}

	@Test
	public void testImplementationVarSectionStaticArrayOf() throws Exception {
		assertThat(implementationSection().declSection(1).varSection().varDecl(2).toStringTree(ruleNames), is(equalTo("(varDecl (identList (ident var2)) : ARRAY [ (subrangeType 1 .. 5) ] OF (type (stringType STRING)))")));
	}

	@Test
	public void testImplementationProcedureHeading() throws Exception {
		assertThat(implementationSection().declSection(2).procedureDeclSection().procedureDecl().procedureHeading().toStringTree(ruleNames), is(equalTo("(procedureHeading PROCEDURE (ident procedure0) (formalParms ( (formalParm (parameter (identList (ident parm0) , (ident parm1)) : (parameterType (simpleType (ordinalType (ordIdent INTEGER)))))) )))")));
	}
	
	@Test
	public void testImplementationFunctionHeading() throws Exception {
		assertThat(implementationSection().declSection(3).procedureDeclSection().functionDecl().functionHeading().toStringTree(ruleNames), is(equalTo("(functionHeading FUNCTION (ident function1) (formalParms ( )) : (returnType STRING))")));
	}
	
	private GoalContext goal() {
		return goal;
	}

	private UnitContext unit() {
		return goal().unit();
	}
	
	private InterfaceSectionContext interfaceSection() {
		return unit().interfaceSection();
	}
	
	private ImplementationSectionContext implementationSection() {
		return unit().implementationSection();
	}

}

