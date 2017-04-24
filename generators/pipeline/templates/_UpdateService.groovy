String project = 'microservices'
String repository = '<%= dasherizedBaseName.toLowerCase() %>'


node {

    stage "Build Service" // --------------------------------------

    git url: GIT_URL
    env.EPOCH="latest"
    env.GIT_HASH=stringFromOutput('git rev-parse HEAD | cut -c-8')
    env.TAG_LIST="1.0-${env.GIT_HASH}-${env.EPOCH}"
    env.REPOSITORY="${project}/${repository}"
    env.ACCOUNT_NUMBER=stringFromOutput("aws iam get-user | awk '/arn:aws:/{print \$2}' | cut -d \\: -f 5")
    env.REGION=stringFromOutput("aws configure get default.region")
    env.AWS_TAG="${env.ACCOUNT_NUMBER}.dkr.ecr.${env.REGION}.amazonaws.com/${env.REPOSITORY}:${env.TAG_LIST}"

    sh 'echo ${AWS_TAG}'
    sh "sed -i.bak 's,image:.*,image: ${AWS_TAG},g' kubernetes/<%= dasherizedBaseName.toLowerCase() %>-app.yml"

    // Build the java code with gradle
    docker.image('gradle:3.5-jdk8').inside {
      buildService()
    }

    step([$class: "JUnitResultArchiver", testResults: "build/**/TEST-*.xml"])

    stage "Bake Service's Docker image" // ------------------------

    // Bake the Docker Image
    def localImage = docker.build("${env.AWS_TAG}", ".")
    sh 'docker images'

    // Login into Amazon ECR
    sh '$(aws ecr get-login)'

    // Push Docker Image to Amazon ECR Repository
    localImage.push()

    stage "Update Service on Kubernetes" // ------------------------------

    sh "kubectl apply -f kubernetes --kubeconfig=${env.KUBE_CONFIG_PATH}"
}

def buildService() {
    def command = './gradlew build integration jbehave'
    sh command
}

def stringFromOutput(String command) {
    sh command + ' > .variable'
    String content = readFile '.variable'
    return content.trim()
}
