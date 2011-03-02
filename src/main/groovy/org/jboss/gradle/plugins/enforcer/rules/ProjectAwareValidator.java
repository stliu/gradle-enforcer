package org.jboss.gradle.plugins.enforcer.rules;

import org.gradle.api.Project;

/**
 * Validator can implement this interface to get the Project
 * @author: Strong Liu
 */
public interface ProjectAwareValidator {
	void setProject(Project project);

}
