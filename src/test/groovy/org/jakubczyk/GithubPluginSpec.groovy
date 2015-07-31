package org.jakubczyk

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification


class GithubPluginSpec extends Specification {

    def "test"() {
        given:
        Project project = ProjectBuilder.builder().withName("dmmy").build()

        when:
        def a = 5
        project.pluginManager.apply GithubPlugin

        then:
        project.tasks["ghpr"]
    }
}
