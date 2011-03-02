package org.jboss.gradle.plugins.enforcer

/**
 * 
 * @author: Strong Liu
 */
class EnforcerValidatorMissingException extends EnforcerException{
	EnforcerValidatorMissingException() {
	}

	EnforcerValidatorMissingException(String message) {
		super(message)
	}

	EnforcerValidatorMissingException(String message, Throwable cause) {
		super(message, cause)
	}
}
