pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "m2"
    }

    stages {
        stage('Build') {
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
        stage('Test') {
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true clean test"
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
                        withCredentials([usernamePassword(credentialsId: 'dockerhub-login', usernameVariable:'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                            sh 'docker build -t thaimore/order-service:latest . --build-arg=LOCATION=classpath:/deploy.properties --build-arg=JAR_FILE=target/OrderService-0.0.1-SNAPSHOT.jar'
                            sh 'docker push thaimore/order-service:latest'
                        }
                    }
        }
    }
}