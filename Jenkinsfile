pipeline {
  agent any
  stages {
    stage('git scm update') {
      steps {
        git url: 'https://github.com/dadahee/fasting-date-counter.git', branch: 'deploy'
      }
    }
    stage('docker build and push') {
      steps {
        sh '''
        docker build -t 192.168.1.10:8443/fasting-service:1.2 .
        docker rmi $(docker images -f "dangling=true" -q)
        docker push 192.168.1.10:8443/fasting-service:1.2
        '''
      }
    }
    stage('deploy kubernetes') {
      steps {
        sh '''
        sed -i 's/fasting-service:1.1/fasting-service:1.2/' Kubernetes/service/fasting-service.yaml
        kubectl apply -f Kubernetes/service/fasting-service.yaml
        '''
      }
    }
  }
}
