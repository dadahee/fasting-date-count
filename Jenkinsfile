pipeline {
  agent any
  stages {
    stage('git scm update') {
      steps {
        git url: 'https://github.com/dadahee/fasting-date-counter.git', branch: 'main'
      }
    }
    stage('docker build and push') {
      steps {
        sh '''
        docker build -t 192.168.1.10:8443/fasting-service:1.1 .
        docker rmi $(docker images -f "dangling=true" -q)
        docker push 192.168.1.10:8443/fasting-service:1.1
        '''
      }
    }
    stage('deploy kubernetes') {
      steps {
        sh '''
        kubectl apply -f Kubernetes/service/fasting-service.yaml
        '''
      }
    }
  }
}
