pipeline{
    agent {label 'node1'}
    
    environment {
        docker_img_name = 'adhmabdein/mynodeimg'
    }
    
    stages{
        stage ('pull code from github'){
            steps{
                script{
                    git "https://github.com/AdhmAbdein/nodejs-project.git"
                }
            }
        }
         stage('Login to docker hub'){
            steps{
                script{
                    withCredentials([usernamePassword(credentialsId:'docker_hub' , usernameVariable:'d_hub_usr' , passwordVariable:'d_hub_pass')]){
                        sh 'docker login -u ${d_hub_usr} -p ${d_hub_pass}'
                    }
                }
            }
            
        }
        stage('CI - Build docker image'){
            steps{
                script{
                    sh 'docker build -t ${docker_img_name} -f project-code/Dockerfile . '
                }   
            }
        }
        stage('CI - Test inside container(temperory) (npm test)'){
            steps{
                script{
                    sh 'docker run --rm -d ${docker_img_name} /bin/sh -c " ls -R /app && npm test"'

                }
           }
        }
        stage('push image to docker hub'){
            steps{
                script{
                    sh 'docker push ${docker_img_name}'
                }
            }
        }
        stage('CD - Deploy in k8s'){
            steps{
                script{
                    dir('project-code'){
                       sh 'kubectl apply -f k8-pv.yml'
                       sh 'kubectl apply -f k8-pvc.yml'
                       sh 'kubectl apply -f k8-deploy.yml'
                       sh 'kubectl apply -f k8-svc.yml'

                        
                    }
                }
            }
        }
    }
    post {
        always{
            archiveArtifacts artifacts : '**/test-result.xm' , allowEmptyArchive: true
            //**/: This means "any directory" (recursively)
        }
    }
}