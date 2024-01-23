pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "m2"
        'org.jenkinsci.plugins.docker.commons.tools.DockerTool' 'docker'
    }

    stages {
        stage('Build') {
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
        stage('Deploy') {
                    steps {
                        nexusArtifactUploader(
                                nexusVersion: 'nexus3',
                                protocol: 'http',
                                nexusUrl: '10.152.183.207:32145',
                                groupId: 'com.org.os',
                                version: '0.0.1-SNAPSHOT',
                                repository: 'maven-snapshots',
                                credentialsId: 'nexus-pass',
                                artifacts: [
                                    [artifactId: 'OrderService',
                                     classifier: 'debug',
                                     file: 'target/OrderService-0.0.1-SNAPSHOT.jar',
                                     type: 'jar']
                                ]
                             )
                    }
        }
        stage('Build and push docker image') {
            steps {
            script {
                docker.withTool('docker') {
                docker.withRegistry('https://hub.docker.com', 'dockerhub-login') {
                    build('thaimore/order-service:latest').push()
                }
                }
            }
            }
        }
    }
}