package org.jboss.gradle.plugins.enforcer.rules;

/**
 * @author: Strong Liu
 */
public interface Validator {
	void setExpression(String expression);
	void validate();
}
