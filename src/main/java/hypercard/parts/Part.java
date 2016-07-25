/**
 * Part.java
 * @author matt.defano@gmail.com
 * 
 * Part interface; defines the set of methods common to all parts. 
 */

package hypercard.parts;

import javax.swing.JComponent;

import hypertalk.ast.common.PartType;
import hypertalk.ast.common.Value;
import hypertalk.ast.functions.ArgumentList;
import hypertalk.exception.HtSemanticException;
import hypertalk.exception.NoSuchPropertyException;
import hypertalk.exception.PropertyPermissionException;
import hypercard.parts.model.PartModel;

public interface Part {
	
	PartType getType();
	String getName();
	int getId();
	JComponent getComponent();
	
	PartModel getPartModel();
	Value getProperty (String property) throws NoSuchPropertyException;
	void setProperty (String property, Value value) throws NoSuchPropertyException, PropertyPermissionException;
	
	Value getValue();
	void setValue(Value v);
		
	void sendMessage(String message) throws HtSemanticException;
	Value executeUserFunction(String function, ArgumentList arguments) throws HtSemanticException;
    
    void partOpened();
}
