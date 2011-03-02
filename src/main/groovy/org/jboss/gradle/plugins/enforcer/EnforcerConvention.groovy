package org.jboss.gradle.plugins.enforcer

import org.jboss.gradle.plugins.enforcer.rules.Rule

/**
 *
 * @author: Strong Liu
 */
class EnforcerConvention {
	def rules;
	/**
	 * default is "fail", another two options are "fail-fast" and "skip"
	 */
	def failPlan = "fail"
	EnforcerConvention(rules) {
		this.rules = rules
	}
	def jdk(exp){
		createNativeRule("jdk",exp)
	}
	def gradle(exp){
		createNativeRule("gradle",exp)
	}
	private void createNativeRule(name,exp){
		Rule r = new Rule(name)
		r.expression=exp
		rules.addObject(r.name,r)
	}
	def enforcer(Closure closure) {
		rules.configure closure
	}
}