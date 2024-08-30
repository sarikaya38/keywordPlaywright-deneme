pipeline {
    agent any

    environment {
        NODEJS_HOME = tool name: 'NodeJS 14'
        PATH = "${NODEJS_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sarikaya38/playwrightKeyWord.git', credentialsId: 'your-credentials-id'
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'npm install'
                sh 'npx playwright install'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'npx playwright test'
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
