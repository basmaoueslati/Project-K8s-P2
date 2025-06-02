pipeline {
    agent any
    stages {
        //Continuous Integration
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        //Continuous Delivery
        stage('Docker Build&Push') {
            steps {
                withDockerRegistry(credentialsId: 'docker', url: "") {
                    sh 'docker build -t basmaoueslati/compare-appf25 .'
                    sh 'docker push basmaoueslati/compare-appf25'
                }
            }
        }
        stage('Clean Old Docker Images on Local') {
            steps {
                echo '___Cleaning up unused Docker images___'
                sh 'docker image prune -f'

                sh '''
                docker images --filter=reference='basmaoueslati/compare-appf25*' --format '{{.ID}} {{.Repository}}:{{.Tag}}' \
                  | awk '{print $1}' \
                  | xargs -r docker rmi -f
                '''
            }
        }
        //Continuous Deployment
       stage('Run Ansible Playbook') {
            steps {            
                sh 'ansible-playbook -i inventory.ini playbook.yml'
            
        }
    }

    }
}

