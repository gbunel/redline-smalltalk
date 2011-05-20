/*
Redline Smalltalk is licensed under the MIT License

Redline Smalltalk Copyright (c) 2010 James C. Ladd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Please see DEVELOPER-CERTIFICATE-OF-ORIGIN if you wish to contribute a patch to Redline Smalltalk.
*/
package st.redline.smalltalk.interpreter;

import java.util.ArrayList;
import java.util.List;

public class UnaryExpression implements MessageExpression {

	private final List<UnarySelector> unarySelectors;
	private BinaryExpression binaryExpression;
	private KeywordExpression keywordExpression;

	public UnaryExpression() {
		unarySelectors = new ArrayList<UnarySelector>();
	}

	public void add(UnarySelector unarySelector) {
		unarySelectors.add(unarySelector);
	}

	public void add(BinaryExpression binaryExpression) {
		this.binaryExpression = binaryExpression;
	}

	public void add(KeywordExpression keywordExpression) {
		this.keywordExpression = keywordExpression;
	}

	public void accept(NodeVisitor visitor) {
		if (binaryExpression != null && keywordExpression != null)
			throw new IllegalStateException("Unary expression should not have both a binary and keyword expression.");
		visitor.visit(this);
		for (UnarySelector unarySelector : unarySelectors)
			unarySelector.accept(visitor);
		if (binaryExpression != null)
			binaryExpression.accept(visitor);
		else if (keywordExpression != null)
			keywordExpression.accept(visitor);
		visitor.visitEnd(this);
	}
}
