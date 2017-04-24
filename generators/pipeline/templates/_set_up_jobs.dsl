String mainFolder = 'microservices'
String projectFolder = '<%= dasherizedBaseName.toLowerCase() %>'
String basePath = mainFolder + "/" +  projectFolder
String gitUrl = '<%= gitURL %>'

pipelineJob("$basePath/1. pre-configure") {
    scm {
        git("$gitUrl")
    }

    parameters {
        stringParam('GIT_URL' , "$gitUrl", 'Git repository url of your application.')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipeline/jobs/PreConfigure.groovy'))
            sandbox()
        }
    }
}

pipelineJob("$basePath/2. create-deployment") {
    scm {
        git("$gitUrl")
    }

    parameters {
        stringParam('GIT_URL' , "$gitUrl", 'Git repository url of your application.')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipeline/jobs/CreateDeployment.groovy'))
            sandbox()
        }
    }
}

pipelineJob("$basePath/3. update-service") {
    scm {
        git("$gitUrl")
    }

    triggers {
        scm('H/15 * * * *')
    }

    logRotator {
        numToKeep(10)
        artifactNumToKeep(10)
    }

    parameters {
        stringParam('GIT_URL' , "$gitUrl", 'Git repository url of your application.')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipeline/jobs/UpdateService.groovy'))
            sandbox()
        }
    }
}
