pipeline {
  agent any
  stages {
    stage('git pull') {
      steps {
        // https://github.com/dadahee/fasting-date-counter will replace by sed command before RUN
        git url: 'https://github.com/dadahee/fasting-date-counter', branch: 'main'
      }
    }
    stage('k8s deploy'){
      steps {
        kubernetesDeploy(kubeconfigId: 'kubeconfig',
                         configs: '*.yaml')
      }
    }    
  }
}