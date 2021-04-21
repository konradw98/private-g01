pipeline {
    agent any 
    stages {
        stage('Build') { 
            steps {
                sh './gradlew build'
            }
        }
        stage('Sonar') {
            steps {
                sh './gradlew sonarqube -Dsonar.host.url=$sonarUrl -Dsonar.login=$sonarUser -Dsonar.password=$sonarPassword'
            }
        }
    }
}
