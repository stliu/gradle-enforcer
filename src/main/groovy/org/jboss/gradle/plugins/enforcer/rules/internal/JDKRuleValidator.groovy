package org.jboss.gradle.plugins.enforcer.rules.internal

import org.apache.commons.lang.SystemUtils
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import org.codehaus.plexus.util.StringUtils
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import org.jboss.gradle.plugins.enforcer.rules.AbstractVersionValidator

/**
 * most codes are copied from maven enforcer plugin
 * @author: Strong Liu
 */
class JDKRuleValidator extends AbstractVersionValidator {
	static final Logger log = Logging.getLogger(JDKRuleValidator)

	void validate() {
		String java_version = SystemUtils.JAVA_VERSION_TRIMMED;

		log.debug("Detected Java String: " + java_version);
		java_version = normalizeJDKVersion(java_version);
		log.debug("Normalized Java String: " + java_version);

		ArtifactVersion detectedJdkVersion = new DefaultArtifactVersion(java_version);

		log.debug("Parsed Version: Major: " + detectedJdkVersion.getMajorVersion() + " Minor: " +
				detectedJdkVersion.getMinorVersion() + " Incremental: " + detectedJdkVersion.getIncrementalVersion() +
				" Build: " + detectedJdkVersion.getBuildNumber() + " Qualifier: " + detectedJdkVersion.getQualifier());

		enforceVersion(log, "JDK", expression, detectedJdkVersion);
	}
	/**
	 * Converts a jdk string from 1.5.0-11b12 to a single 3 digit expression like 1.5.0-11
	 *
	 * @param theJdkVersion to be converted.
	 * @return the converted string.
	 */
	public static String normalizeJDKVersion(String theJdkVersion) {

		theJdkVersion = theJdkVersion.replaceAll("_|-", ".");
		String[] tokenArray = StringUtils.split(theJdkVersion, ".");
		List tokens = Arrays.asList(tokenArray);
		StringBuffer buffer = new StringBuffer(theJdkVersion.length());

		Iterator iter = tokens.iterator();
		for ( int i = 0; i < tokens.size() && i < 4; i++ ) {
			String section = (String) iter.next();
			section = section.replaceAll("[^0-9]", "");

			if ( StringUtils.isNotEmpty(section) ) {
				buffer.append(Integer.parseInt(section));

				if ( i != 2 ) {
					buffer.append('.');
				}
				else {
					buffer.append('-');
				}
			}
		}

		String version = buffer.toString();
		version = StringUtils.stripEnd(version, "-");
		return StringUtils.stripEnd(version, ".");
	}
}
