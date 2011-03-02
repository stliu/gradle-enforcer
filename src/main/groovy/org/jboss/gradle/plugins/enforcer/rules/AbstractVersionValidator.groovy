package org.jboss.gradle.plugins.enforcer.rules

import org.apache.maven.artifact.versioning.ArtifactVersion
import org.codehaus.plexus.util.StringUtils
import org.jboss.gradle.plugins.enforcer.EnforcerException
import org.apache.maven.artifact.versioning.VersionRange
import org.gradle.api.logging.Logger
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException
import org.apache.maven.artifact.versioning.Restriction

/**
 * most codes are copied from maven enforcer plugin
 * @author: Strong Liu
 */
abstract class AbstractVersionValidator implements Validator{
	/**
	 * Specify the required expression. Some examples are:
	 * <ul>
	 * <li><code>2.0.4</code> Version 2.0.4 and higher (different from Maven meaning)</li>
	 * <li><code>[2.0,2.1)</code> Versions 2.0 (included) to 2.1 (not included)</li>
	 * <li><code>[2.0,2.1]</code> Versions 2.0 to 2.1 (both included)</li>
	 * <li><code>[2.0.5,)</code> Versions 2.0.5 and higher</li>
	 * <li><code>(,2.0.5],[2.1.1,)</code> Versions up to 2.0.5 (included) and 2.1.1 or higher</li>
	 * </ul>
	 */
	public String expression = null;
	public String message = null;
	public void setExpression(String expression){
		 this.expression = expression
	}

	/**
	 * Compares the specified expression to see if it is allowed by the defined expression range.
	 *
	 * @param log the log
	 * @param variableName name of variable to use in messages (Example: "Maven" or "Java" etc).
	 * @param requiredVersionRange range of allowed versions.
	 * @param actualVersion the expression to be checked.
	 */
	public void enforceVersion(Logger log, String variableName, String requiredVersionRange, ArtifactVersion actualVersion) {
		if ( StringUtils.isEmpty(requiredVersionRange) ) {
			throw new EnforcerException(variableName + " expression can't be empty.");
		}
		else {

			VersionRange vr;
			String msg = "Detected " + variableName + " Version: " + actualVersion;

			// short circuit check if the strings are exactly equal
			if ( actualVersion.toString().equals(requiredVersionRange) ) {
				log.debug(msg + " is allowed in the range " + requiredVersionRange + ".");
			}
			else {
				try {
					vr = VersionRange.createFromVersionSpec(requiredVersionRange);

					if ( containsVersion(vr, actualVersion) ) {
						log.debug(msg + " is allowed in the range " + requiredVersionRange + ".");
					}
					else {
						if ( StringUtils.isEmpty(message) ) {
							message = msg + " is not in the allowed range " + vr + ".";
						}

						throw new EnforcerException(message);
					}
				}
				catch (InvalidVersionSpecificationException e) {
					throw new EnforcerException("The requested " + variableName + " expression " +
							requiredVersionRange + " is invalid.", e);
				}
			}
		}
	}

	/**
	 * Copied from Artifact.VersionRange. This is tweaked to handle singular ranges properly. Currently the default
	 * containsVersion method assumes a singular expression means allow everything. This method assumes that "2.0.4" ==
	 * "[2.0.4,)"
	 *
	 * @param allowedRange range of allowed versions.
	 * @param theVersion the expression to be checked.
	 * @return true if the expression is contained by the range.
	 */
	public static boolean containsVersion(VersionRange allowedRange, ArtifactVersion theVersion) {
		boolean matched = false;
		ArtifactVersion recommendedVersion = allowedRange.getRecommendedVersion();
		if ( recommendedVersion == null ) {

			for ( Iterator i = allowedRange.getRestrictions().iterator(); i.hasNext() && !matched; ) {
				Restriction restriction = (Restriction) i.next();
				if ( restriction.containsVersion(theVersion) ) {
					matched = true;
				}
			}
		}
		else {
			// only singular versions ever have a recommendedVersion
			int compareTo = recommendedVersion.compareTo(theVersion);
			matched = (compareTo <= 0);
		}
		return matched;
	}


}
