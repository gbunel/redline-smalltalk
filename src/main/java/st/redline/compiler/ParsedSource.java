package st.redline.compiler;

import java.io.File;
import java.util.*;

public class ParsedSource implements Translatable {

	private final Stack<Map<String, Token>> variables;

	private String sourcePath;
	private Translator translator;
	private String outputPath;
	private ClassDefinition classDefinition;
	private ClassComment classComment;
	private List<Method> classMethods = new ArrayList<Method>();
	private List<Method> instanceMethods = new ArrayList<Method>();
	private boolean classSection = false;
	private MessagePattern messagePattern;
	private ClassInstanceVariables classInstanceVariables;
	private Pragma[] pragmas = new Pragma[2];
	private List<Variable> temporaries;
	private Statements statements;
	private MethodComment methodComment;
	private String sourceFolder;

	public ParsedSource() {
		variables = new Stack<Map<String, Token>>();
		enterMethodContext();
	}

	public void outputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public String outputPath() {
		return outputPath;
	}

	public void sourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
		this.sourceFolder = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator)+1);
	}

	public String sourcePath() {
		return sourcePath;
	}

	public boolean isDefinedClass(Token className) {
		String name = className.toString();
		return (new File(sourceFolder + name + ".st").exists())
			|| name.equals("ProtoObject")
			|| name.equals("Object")
			|| name.equals("Smalltalk");
	}

	public boolean isDefinedEnvironment(Token environment) {
		String name = environment.toString();
		return true;  // TODO.jcl for now this makes compiler thing any value is a primary -> variable is valid.
		// this needs to change.
	}

	public boolean isDefinedVariable(Token variable) {
		String key = variable.toString();
		if (variables().containsKey(key))
			return true;
		return isVariableDefinedInPreviousContext(key);
	}

	private boolean isVariableDefinedInPreviousContext(String key) {
		for (Map<String, Token> context : variables) {
			if (context.containsKey(key))
				return true;
		}
		return false;
	}

	public void defineVariable(Token variable) {
		defineVariable(variable.toString(), variable);
	}

	public void defineVariables(Token input) {
		for (String string : input.toString().split(" "))
			if (string.length() > 0)
				defineVariable(string, input);
	}

	public void defineVariable(String key, Token variable) {
		if (key.charAt(0) == '\'')
			key = key.substring(1);
		if (key.charAt(key.length()-1) == '\'')
			key = key.substring(0, key.length()-1);
		variables().put(key, variable);
	}

	private void defineVariables(List<Variable> variables) {
		for (Variable variable : variables)
			defineVariable(variable.token());
	}

	public void enterMethodContext() {
		variables.push(new HashMap<String, Token>());
	}

	public void exitMethodContext() {
		variables.pop();
	}

	public void enterBlockContext() {
		variables.push(new HashMap<String, Token>());
	}

	public void exitBlockContext() {
		variables.pop();
	}

	public Map<String, Token> variables() {
		return variables.peek();
	}

	public void add(ClassDefinition classDefinition) {
		if (this.classDefinition != null)
			System.err.println("Warning: overwriting class definition.");

		this.classDefinition = classDefinition;
		defineVariables(classDefinition.rawInstanceVariableNames());
		defineVariables(classDefinition.rawClassVariableNames());
	}

	public void add(ClassInstanceVariables classInstanceVariables) {
		this.classInstanceVariables = classInstanceVariables;
	}

	public boolean hasClassDefinition() {
		return this.classDefinition != null;
	}

	public String definedClassName() {
		return classDefinition.subclass();
	}

	public void add(ClassComment classComment) {
		if (this.classComment != null)
			System.err.println("Warning: overwriting class comment.");
		this.classComment = classComment;
	}

	public void add(MethodComment methodComment) {
		this.methodComment = methodComment;
		classSection = methodComment.isForClass();
	}

	public void add(MessagePattern messagePattern) {
		this.messagePattern = messagePattern;
	}

	public void add(Pragma pragma, boolean first) {
		pragmas[(first) ? 0 : 1] = pragma;
	}

	public void add(List<Variable> temporaries) {
		this.temporaries = temporaries;
	}

	public void add(Statements statements) {
		this.statements = statements;
	}

	public void add(Method method) {
		method.add(messagePattern);
		method.add(pragmas);
		method.add(temporaries);
		method.add(statements);

		if (classSection)
			classMethods.add(method);
		else
			instanceMethods.add(method);

		messagePattern = null;
		pragmas = new Pragma[2];
		temporaries = null;
		statements = null;
	}

	public void apply(Translator translator) {
		this.translator = translator;
		visitClassDefinition();
		visitMethods();
	}

	private void visitMethods() {
		if (classMethods.size() > 0 || instanceMethods.size() > 0)
			translator.visitMethods(classMethods, instanceMethods);
	}

	private void visitClassDefinition() {
		if (classDefinition != null)
			translator.visitClassDefinition(classDefinition, classInstanceVariables);
	}
}