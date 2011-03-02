package org.jboss.gradle.plugins.enforcer

import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.jboss.gradle.plugins.enforcer.rules.Rule
import org.jboss.gradle.plugins.enforcer.rules.internal.GradleRuleValidator
import org.jboss.gradle.plugins.enforcer.rules.internal.JDKRuleValidator

/**
 *
 * @author: Strong Liu
 */
class RuleValidatorRegistry {
	static final Logger log = Logging.getLogger(RuleValidatorRegistry)
	static Map registryCache = new HashMap()

	static RuleValidatorRegistry getInstance(Project project) {
		def instance = registryCache.get(project)
		if ( instance == null ) {
			instance = new RuleValidatorRegistry(project)
			registryCache.put(project, instance)
		}
		return instance
	}

	Project project
	Map nativeValidatorMap = new HashMap()
	Map validatorMap = new HashMap()

	private RuleValidatorRegistry(project) {
		this.project = project
		nativeValidatorMap.put("jdk", JDKRuleValidator.name)
		nativeValidatorMap.put("gradle", GradleRuleValidator.name)
	}

	def get(Rule rule) {
		log.debug("finding validator for $rule,look up validator instance from cache first")
		//look up validator instance from cache first
		def validator = validatorMap.get(rule)
		if ( validator == null ) {
			//try to initialize the validator defined in the Rule
			def validatorClass = rule.validator
			validator = initializeClass(validatorClass)
			//finally trying the native validator
			if ( validator == null ) {
				validatorClass = nativeValidatorMap.get(rule.name)
				validator = initializeClass(validatorClass)
				if ( validator == null ) {
					throw new EnforcerValidatorMissingException("could not find validator for $rule")
				}
			}
			validatorMap.put(rule, validator)
		}
		return validator
	}
	//load and initialize clazz

	def initializeClass(clazz) {
		if ( clazz == null || clazz.trim().equals("") ) {
			return null
		}
		def obj = initializeClassFromProjectClasspath(clazz)
		if ( obj == null ) {
			obj = initializeClassFromPluginClasspath(clazz)
		}
		return obj
	}

	def initializeClassFromProjectClasspath(clazz) {
		//todo
		return null
	}

	def initializeClassFromPluginClasspath(clazz) {
		if ( clazz == JDKRuleValidator.name ) {
			return new JDKRuleValidator()
		}
		else if ( clazz == GradleRuleValidator.name ) {
			return new GradleRuleValidator()
		}
		else {return null}
	}


}
