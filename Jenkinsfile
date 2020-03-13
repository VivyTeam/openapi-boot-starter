pipeline {
    agent any
    
    tools {
        jdk 'jdk-11'
    }
    
    options {
        ansiColor('xterm')
        disableConcurrentBuilds()
    }

    stages {
        stage('checkout') {
            steps {
                checkout scm
            }
        }

        stage('build') {
            steps {
                sh "./gradlew build -x test"
            }
        }

        stage('test') {
            steps {
                sh "./gradlew test"
            }
        }
    }
}
