package org.jboss.gradle.plugins.enforcer.rules.internal

import org.jboss.gradle.plugins.enforcer.rules.ProjectAwareValidator
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.gradle.api.logging.Logger
import org.apache.maven.artifact.versioning.ArtifactVersion
import org.apache.maven.artifact.versioning.DefaultArtifactVersion
import org.jboss.gradle.plugins.enforcer.rules.AbstractVersionValidator

/**
 *
 * @author: Strong Liu
 */
class GradleRuleValidator extends AbstractVersionValidator implements ProjectAwareValidator{
	static final Logger log = Logging.getLogger(GradleRuleValidator)
	Project project

	void validate() {
		String gradle_version = project.gradle.gradleVersion

		log.debug("Detected Gradle String: " + gradle_version);

		ArtifactVersion dectedGradleVersion = new DefaultArtifactVersion(gradle_version);

		log.debug("Parsed Version: Major: " +  dectedGradleVersion.getMajorVersion() + " Minor: " +
				 dectedGradleVersion.getMinorVersion() + " Incremental: " +  dectedGradleVersion.getIncrementalVersion() +
				" Build: " +  dectedGradleVersion.getBuildNumber() + " Qualifier: " +  dectedGradleVersion.getQualifier());

		enforceVersion(log, "Gradle", expression,  dectedGradleVersion);
	}
}
