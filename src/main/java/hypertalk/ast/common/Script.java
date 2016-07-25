/**
 * Script.java
 * @author matt.defano@gmail.com
 * 
 * Encapsulation of a HyperTalk script; might include user defined functions,
 * handlers, or loose statements.
 */

package hypertalk.ast.common;

import hypercard.runtime.RuntimeEnv;
import hypertalk.ast.functions.ArgumentList;
import hypertalk.ast.functions.UserFunction;
import hypertalk.ast.statements.StatementList;
import hypertalk.exception.HtException;
import hypertalk.exception.HtSemanticException;

import java.util.HashMap;
import java.util.Map;

public class Script {

	private Map<String, StatementList> handlers;
	private Map<String, UserFunction> functions;
	private StatementList statements = null;
	
	public Script () {
		handlers = new HashMap<>();
		functions = new HashMap<>();
	}

	public Script defineHandler (NamedBlock handler) {
		handlers.put(handler.name, handler.body);
		return this;
	}
	
	public Script defineUserFunction (UserFunction function) {
		functions.put(function.name, function);
		return this;
	}
	
	public Script defineStatementList (StatementList statements) {
		this.statements = statements;
		return this;
	}
	
	public void executeHandler (String handler) {
		if (handlers.containsKey(handler))
			RuntimeEnv.getRuntimeEnv().executeStatementList(handlers.get(handler));			
	}
	
	public void executeStatement () throws HtException {
		if (statements != null)
			statements.execute();
	}
	
	public Value executeUserFunction (String function, ArgumentList arguments) throws HtSemanticException {
		UserFunction theFunction = functions.get(function);
		
		if (theFunction != null) 
			return RuntimeEnv.getRuntimeEnv().executeUserFunction(theFunction, arguments);
		else
			throw new HtSemanticException("No such function " + function);
	}	
}
