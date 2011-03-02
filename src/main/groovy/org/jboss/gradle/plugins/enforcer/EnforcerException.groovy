package org.jboss.gradle.plugins.enforcer

import org.gradle.api.GradleException

/**
 * 
 * @author: Strong Liu
 */
class EnforcerException extends GradleException{

	EnforcerException() {
	}

	EnforcerException(String message) {
		super(message)
	}

	EnforcerException(String message, Throwable cause) {
		super(message, cause)
	}
}
