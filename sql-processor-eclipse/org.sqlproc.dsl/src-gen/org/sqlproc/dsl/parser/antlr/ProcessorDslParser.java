/*
* generated by Xtext
*/
package org.sqlproc.dsl.parser.antlr;

import com.google.inject.Inject;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.sqlproc.dsl.services.ProcessorDslGrammarAccess;

public class ProcessorDslParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private ProcessorDslGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	
	@Override
	protected org.sqlproc.dsl.parser.antlr.internal.InternalProcessorDslParser createParser(XtextTokenStream stream) {
		return new org.sqlproc.dsl.parser.antlr.internal.InternalProcessorDslParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "Artifact";
	}
	
	public ProcessorDslGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(ProcessorDslGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
}
