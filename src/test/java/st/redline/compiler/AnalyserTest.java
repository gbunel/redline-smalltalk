/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.compiler;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AnalyserTest {

	static String CLASS_NAME = "Example";
	static String PACKAGE_NAME = "com.domain";

	Analyser analyser;
	ProgramAnalyser delegate;

	@Before
	public void setup() {
		analyser = new Analyser(CLASS_NAME, PACKAGE_NAME, false);
		delegate = mock(ProgramAnalyser.class);
		analyser.currentDelegate(delegate);
	}

	@Test
	public void shouldDelegateVisitOfBinaryObjectDescriptionNode() {
		Primary primary = mock(Primary.class);
		BinaryObjectDescription binaryObjectDescription = new BinaryObjectDescription(primary);
		binaryObjectDescription.accept(analyser);
		verify(delegate).visit(binaryObjectDescription);
	}

	@Test
	public void shouldDelegateVisitOfBinaryExpressionNode() {
		BinaryExpression binaryExpression = new BinaryExpression();
		binaryExpression.accept(analyser);
		verify(delegate).visitBegin(binaryExpression);
		verify(delegate).visitEnd(binaryExpression);
	}

	@Test
	public void shouldDelegateVisitOfArrayConstantNode() {
		ArrayConstant arrayConstant = new ArrayConstant(mock(Array.class),  42);
		arrayConstant.accept(analyser);
		verify(delegate).visit(arrayConstant, 42);
	}

	@Test
	public void shouldDelegateVisitOfArrayNode() {
		Array array = new Array();
		array.accept(analyser);
		verify(delegate).visitBegin(array);
		verify(delegate).visitEnd(array);
	}

	@Test
	public void shouldDelegateVisitOfAssignmentExpressionNode() {
		AssignmentExpression assignmentExpression = new AssignmentExpression(mock(Identifier.class), mock(Expression.class));
		assignmentExpression.accept(analyser);
		verify(delegate).visitBegin(assignmentExpression);
		verify(delegate).visitEnd(assignmentExpression);
	}

	@Test
	public void shouldDelegateVisitOfUnaryExpressionNode() {
		UnaryExpression unaryExpression = new UnaryExpression();
		unaryExpression.accept(analyser);
		verify(delegate).visitBegin(unaryExpression);
		verify(delegate).visitEnd(unaryExpression);
	}

	@Test
	public void shouldDelegateVisitOfUnarySelectorNode() {
		UnarySelector unarySelector = new UnarySelector("yourself", 32);
		analyser.visit(unarySelector, "yourself", 32);
		verify(delegate).visit(unarySelector, "yourself", 32);
	}

	@Test
	public void shouldDefaultToProgramAnalyserDelegate() {
		assertTrue(new Analyser(CLASS_NAME, PACKAGE_NAME, false).currentDelegate() instanceof ProgramAnalyser);
	}

	@Test
	public void shouldDelegateGetOfClassBytes() {
		analyser.classBytes();
		verify(delegate).classBytes();
	}

	@Test
	public void shouldDelegateVisitOfSelfNode() {
		// Self node is a synthetic node generated by Identifier.
		Self self = new Self();
		analyser.visit(self, 21);
		verify(delegate).visit(self, 21);
	}

	@Test
	public void shouldDelegateVisitOfSuperNode() {
		// Super node is a synthetic node generated by Identifier.
		Super aSuper = new Super();
		analyser.visit(aSuper, 21);
		verify(delegate).visit(aSuper, 21);
	}

	@Test
	public void shouldDelegateVisitOfTrueNode() {
		// True node is a synthetic node generated by Identifier.
		True aTrue = new True();
		analyser.visit(aTrue, 21);
		verify(delegate).visit(aTrue, 21);
	}

	@Test
	public void shouldDelegateVisitOfFalseNode() {
		// False node is a synthetic node generated by Identifier.
		False aFalse = new False();
		analyser.visit(aFalse, 21);
		verify(delegate).visit(aFalse, 21);
	}

	@Test
	public void shouldDelegateVisitOfNilNode() {
		// Nil node is a synthetic node generated by Identifier.
		Nil nil = new Nil();
		analyser.visit(nil, 21);
		verify(delegate).visit(nil, 21);
	}

	@Test
	public void shouldDelegateVisitOfIdentifierNode() {
		Identifier identifier = new Identifier("val", 32);
		analyser.visit(identifier, "val", 32);
		verify(delegate).visit(identifier, "val", 32);
	}

	@Test
	public void shouldDelegateVisitOfCascadeNode() {
		// Cascade node is a synthetic node not generated by Parser.
		Cascade cascade = new Cascade();
		analyser.visitBegin(cascade);
		analyser.visitEnd(cascade);
		verify(delegate).visitBegin(cascade);
		verify(delegate).visitEnd(cascade);
	}

	@Test
	public void shouldDelegateVisitOfSimpleExpressionNode() {
		SimpleExpression simpleExpression = new SimpleExpression();
		simpleExpression.accept(analyser);
		verify(delegate).visitBegin(simpleExpression);
		verify(delegate).visitEnd(simpleExpression);
	}

	@Test
	public void shouldDelegateVisitOfStatementsNode() {
		Statements statements = new Statements(null, null);
		statements.accept(analyser);
		verify(delegate).visitBegin(statements);
		verify(delegate).visitEnd(statements);
	}

	@Test
	public void shouldDelegateVisitOfProgramNode() {
		Program program = new Program(null, null);
		program.accept(analyser);
		verify(delegate).visitBegin(program);
		verify(delegate).visitEnd(program);
	}

	@Test
	public void shouldDelegateVisitOfTemporaryNode() {
		Temporary temporary = new Temporary("x", 32);
		temporary.accept(analyser);
		verify(delegate).visit(temporary, "x", 32);
	}
}
