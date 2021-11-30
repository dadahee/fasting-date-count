pipeline {
  agent any
  stages {
    stage('deploy start'){
      steps{
        slackSend(message: "Deploy {$env.BUILD_NUMBER} Started", color: 'good', tokenCredentialId: 'slack-key')
      }
    }
    stage('git pull') {
      steps {
        git url: 'https://github.com/dadahee/fasting-date-counter', branch: 'main'
      }
    }
    stage('docker build and push') {
      steps {
        sh '''
        docker build -t 192.168.1.10:8443/fasting-service:${BUILD_NUMBER} .
        docker push 192.168.1.10:8443/fasting-service:${BUILD_NUMBER}
        
        '''
      }
    }
    stage('deploy kubernetes') {
      steps {
        sh '''
        kubectl set image deployment/fasting-service-deployment fasting-service=192.168.1.10:8443/fasting-service:${BUILD_NUMBER}
        '''
      }
    }
    stage('deploy end'){
      steps{
        slackSend(message: """${env.JOB_NAME} #${env.BUILD_NUMBNER} End""", color: 'good', tokenCredentialId: 'slack-key')
      }
    }
  }
}

