package org.jboss.gradle.plugins.enforcer

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.jboss.gradle.plugins.enforcer.rules.Rule

/**
 *
 * @author: Strong Liu
 */
class EnforcerPlugin implements Plugin<Project> {
	static final Logger log = Logging.getLogger(EnforcerPlugin)
	public static final ENFORCER_NAME = "enforcer";
	final RuleEvaluator evaluator = new RuleEvaluator()
	Project project

	void apply(Project project) {
		this.project = project
		NamedDomainObjectContainer<Rule> rules = project.container(Rule) {name ->
			log.debug("Adding new Rule{name = $name}")
			Rule rule = new Rule(name)
			log.debug("Added new $rule")
			return rule
		}
		project.convention.plugins.enforcer = new EnforcerConvention(rules)
		//this works!!! :D
		project.afterEvaluate { Project p ->
			evaluator.evaluate(p, rules)
		}
	}
}
