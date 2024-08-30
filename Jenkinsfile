pipeline {
    agent any

    environment {
        NODEJS_HOME = tool name: 'NodeJS 14'
        PATH = "${NODEJS_HOME}\\bin;${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sarikaya38/playwrightKeyWord.git', credentialsId: 'github-pat'
            }
        }

        stage('Install Dependencies') {
            steps {
                bat 'npm install'
                bat 'npx playwright install'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'npx playwright test'
            }
        }
    }

    post {
        always {
            echo 'Archiving test results...'
            archiveArtifacts artifacts: '**/test-results/**/*', allowEmptyArchive: true
        }
        failure {
            echo 'Build failed.'
        }
    }
}
