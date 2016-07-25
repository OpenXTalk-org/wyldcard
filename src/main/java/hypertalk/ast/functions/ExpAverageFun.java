/**
 * ExpAverageFun.java
 * @author matt.defano@gmail.com
 * 
 * Implementation for the built-in function "average"
 */

package hypertalk.ast.functions;

import hypertalk.ast.common.Value;
import hypertalk.ast.expressions.Expression;
import hypertalk.exception.HtSemanticException;

import java.util.List;

public class ExpAverageFun extends Expression {

	public final Expression expression;
	
	public ExpAverageFun (Expression expression) {
		this.expression = expression;
	}
	
	public Value evaluate () throws HtSemanticException {
		
		float sum = 0;
		List<Value> list = expression.evaluate().listValue();
		if (list.size() == 0)
			return new Value(0);
		
		for (Value item : list) {
			
			if (!item.isNumber())
				throw new HtSemanticException("Can't take the average of a non-numerical list");
				
			sum += item.floatValue();
		}
		
		return new Value(sum/list.size());
	}
}
