package org.jboss.gradle.plugins.enforcer

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.jboss.gradle.plugins.enforcer.rules.ProjectAwareValidator
import org.jboss.gradle.plugins.enforcer.rules.Rule

/**
 *
 * @author: Strong Liu
 */
class RuleEvaluator {
	def evaluate(Project project, NamedDomainObjectContainer<Rule> rules) {
		final def failPlan = project.convention.plugins.enforcer.failPlan
		if ( failPlan == "skip" ) {
			return
		}
		final RuleValidatorRegistry registry = RuleValidatorRegistry.getInstance(project)
		def message = ""
		rules.all { Rule rule ->
			if ( rule.expression == null || rule.expression.length() == 0 ) {
				throw new EnforcerException("$rule doesn't have a expression to be evaluated")
			}
			def validator = registry.get(rule)
			validator.expression = rule.expression
			if ( validator instanceof ProjectAwareValidator ) {
				((ProjectAwareValidator) validator).project = project
			}

			try {
				validator.validate()
			}
			catch (EnforcerException e) {
				if ( failPlan == "fail-fast" ) {
					throw e
				}
				else if ( failPlan == "fail" ) {
					message += e.message
					message += "\n"
				}

			}

		}
		if ( message != "") {
			throw new EnforcerException(message)
		}
	}

}
