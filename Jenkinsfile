pipeline {
  agent any
  stages {
    stage('git pull') {
      steps {
        git url: 'https://github.com/dadahee/fasting-date-counter', branch: 'main'
      }
    }
    stage('k8s deploy'){
      steps {
        kubernetesDeploy(kubeconfigId: 'kubeconfig',
                         configs: 'fasting-service.yaml')
      }
    }    
  }
}
