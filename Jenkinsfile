pipeline {
    agent any 
    stages {
        stage('Sonar') {
            steps {
                sh './gradlew sonarqube -Dsonar.host.url=$sonarUrl -Dsonar.login=$sonarUser -Dsonar.password=$sonarPassword'
            }
        }
    }
}
