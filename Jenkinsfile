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
        docker build -t 192.168.1.10:8443/fasting-service:1.2 .
        docker push 192.168.1.10:8443/fasting-service:1.2
        
        '''
      }
    }
    stage('deploy kubernetes') {
      steps {
        sh '''
        kubectl set image deployment/fasting-service-deployment 192.168.1.10:8443/fasting-service:1.1=192.168.1.10:8443/fasting-service:1.2
        '''
      }
    }
    /*
    stage('k8s deploy'){
      steps {
        kubernetesDeploy(kubeconfigId: 'kubeconfig',
                         configs: 'Kubernetes/service/fasting-service.yaml')
      }
    }
    */
    
  }
}
