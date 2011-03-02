package org.jboss.gradle.plugins.enforcer.rules

/**
 *
 * @author: Strong Liu
 */
class Rule {
	def name
	def expression
	def validator

	Rule(name) {
		this.name = name
	}


	boolean equals(o) {
		if ( this.is(o) ) return true;
		if ( getClass() != o.class ) return false;

		Rule rule = (Rule) o;

		if ( expression != rule.expression ) return false;
		if ( name != rule.name ) return false;
		if ( validator != rule.validator ) return false;

		return true;
	}

	int hashCode() {
		int result;
		result = (name != null ? name.hashCode() : 0);
		result = 31 * result + (expression != null ? expression.hashCode() : 0);
		result = 31 * result + (validator != null ? validator.hashCode() : 0);
		return result;
	}

	public String toString() {
		return "Rule{" +
				"expression='" + expression +
				"', name=" + name +
				", validator=" + validator +
				'}';
	}
}
