package org.jakubczyk

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseException
import groovyx.net.http.Method
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class GithubPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.task("ghpr", type: PullRequestTask)
    }

    static class PullRequestTask extends DefaultTask {


        @TaskAction
        def action() {
            def authToken = project.properties["authToken"]
            try {
                def ret = null
                def http = new HTTPBuilder("https://api.github.com/")

                // perform a POST request, expecting JSON response
                http.request(Method.POST, ContentType.JSON) {
                    uri.path = "repos/pjakubczyk/robospock-plugin/statuses/6de2d671f93045517d34c636abc79dfb84ade673"
                    headers.'Authorization' = "token $authToken"
                    headers.'User-Agent' = 'Mozilla/5.0'
                    body = [
                            state      : 'pending',
                            target_url : 'http://robospock.org/',
                            description: 'gradle plugin working ' + System.currentTimeMillis(),
                            context    : 'continuous-integration/gradle'
                    ]

                    response.success = { resp ->
                        println "Success! ${resp.status}"
                    }

                    response.failure = { resp, json ->
                        println "Request failed with status ${resp.status}"
                        println "Failure body: ${json}"
                    }
                }
                return ret

            } catch (HttpResponseException ex) {
                ex.printStackTrace()
                return null
            } catch (ConnectException ex) {
                ex.printStackTrace()
                return null
            }
        }

    }
}
