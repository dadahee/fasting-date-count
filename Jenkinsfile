pipeline {
  agent any
  stages {
    stage('git pull') {
      steps {
        git url: 'https://github.com/dadahee/fasting-date-counter', branch: 'main'
      }
    }
    stage('docker build and push') {
      steps {
        sh '''
        docker build -t 192.168.1.10:8443/fasting-service:${env.BUILD_NUMBER} .
        docker push 192.168.1.10:8443/fasting-service:${env.BUILD_NUMBER}
        
        '''
      }
    }
    stage('deploy kubernetes') {
      steps {
        sh '''
        kubectl set image deployment/fasting-service-deployment fasting-service=192.168.1.10:8443/fasting-service:${env.BUILD_NUMBER}
        '''
      }
    }
  }
}
