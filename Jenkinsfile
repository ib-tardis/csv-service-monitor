def env_branch_name = env.BRANCH_NAME
def dev_branch_name = "develop"
def stg_branch_name = "stage"

// JFrog Docker registry
def api = "user-auth-api"
def registryHost="imranbagwan.jfrog.io"
def registryName="docker-apps"
def registryCredential = 'jfrog_creds'
def registryUrl="https://${registryHost}/artifactory/${registryName}/${api}/"

// SonarQube Detaisl for generating Links
def sonarQubeServer="https://product-sonarqube.wrtloyalty.com/"
def sonarQubeProject="XLS_WRT.xls-user-authentication-api"
def sonarQubeProjectPath="dashboard?id=${sonarQubeProject}"
def sonarQubeProjectDash="${sonarQubeServer}${sonarQubeProjectPath}"

isPayloadAvail = false;
jsonData = ''

if ( currentBuild.rawBuild.getCauses()[0].toString().contains('UserIdCause') ){
    env.BUILD_CAUSE = 'MANUALTRIGGER'
} else {
	env.BUILD_CAUSE = 'SCMTRIGGER'
}


pipeline {
    
    agent any
    
    tools{
        jdk 'OpenJDK15'
    }
    
     environment{ 
      JAVA_HOME = "${tool 'OpenJDK14'}"
	  PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }
    
    // To enable jenkins -> Teams notification
    options {
        office365ConnectorWebhooks([[
                    startNotification: true,
					notifySuccess: true,
					notifyFailure: true,
					notifyAborted: true,
					notifyUnstable: true,
                    notifyBackToNormal: true,					
					name: 'Jenkins Teams Build Notification',
                    url: ''
            ]])	
    }
    
    stages{
        
        // Using Generic-webhook-trigger
        stage('Git Webhook Setup For Develop Environment(Git WebHook)'){		 
		 when {
			expression { return env.BUILD_CAUSE == 'SCMTRIGGER'}
		 }
		 steps{
		    script{	
				properties([
					pipelineTriggers([
							[
								$class: 'GenericTrigger',
								genericVariables: [
								 [key: 'data', value: '$', expressionType: 'JSONPath', defaultValue: ''],
								 [key: 'ref', value: '$.ref'],
								],

								token: 'pslApi',
								causeString: 'Triggered on branch $ref by GitHub',
								regexpFilterText: '$ref',
    							regexpFilterExpression: 'refs/heads/' + stg_branch_name
							]
						])
					])
					
					jsonData = readJSON text: data
					if(jsonData != ''){
						isPayloadAvail = true
					}
				}
			}
		}
 
        // Posting chnaged files details to JIRA
	   	stage("Jira Integration (Adding Git commit details to JIRA)") {
			when {
				allOf {
					expression { return env.BUILD_CAUSE == 'SCMTRIGGER'}
					expression { return env.BRANCH_NAME == dev_branch_name}
					expression { return isPayloadAvail != false }
				}
			}
            steps{
    		    script{
                    def storyId = ""
                    
                    def jiraComment = """
[~${jsonData.pusher.email.tokenize('@')[0]}] pushed new changes to branch [*${env_branch_name}*|${jsonData.repository.html_url}/tree/${env_branch_name}] of [*${jsonData.repository.name}*|${jsonData.repository.html_url}]
"""

                    jsonData.commits.eachWithIndex { commit, index ->
                      
                        if(commit.message.startsWith("PROJECTKEY-")){
                            storyId = commit.message.tokenize( ' ' )[0]
                            def commitMessage = commit.message.replaceAll("\\{","\\\\{").replaceAll("\\}","\\\\}")
                            
                            def formatedCommitMessageHead = "{panel:title=${commitMessage}|borderStyle=dashed|borderColor=#ccc|titleBGColor=#F7D6C1|bgColor=#FFFFCE}"
                            def formatedCommitMessageFooter = "{panel}"
                            
                            def formatedCommitMessageBody= "" 
                            if(commit.added.size() > 0){
                              formatedCommitMessageBody <<= "\n(+) *Files Added* \n ${commit.added.join('\n')}"
                            }

                            if(commit.removed.size() > 0){
                              formatedCommitMessageBody <<= "\n(-) *Files removed* \n ${commit.removed.join('\n')}"
                            }

                            if(commit.modified.size() > 0){
                              formatedCommitMessageBody <<= "\n(i) *Files modified* \n ${commit.modified.join('\n')}"
                            }

                            formatedCommitMessageBody <<= "\nCommit Id: ${commit.id}\n\n[View Commit|${commit.url}]"
                            formatedCommitMessageBody <<= "\n"
                            
                            def formatedCommitMessage = """${formatedCommitMessageHead}
                                 ${formatedCommitMessageBody}
                                 ${formatedCommitMessageFooter}"""
                            
                            jiraComment <<= "${formatedCommitMessage}"
                        } 
                    }

                    def comapre_url ="{panel}[Compare url|${jsonData.compare}] | [SonarQube Report|${sonarQubeProjectDash}]{panel}\n"
                    jiraComment <<= "\n${comapre_url}"
                    jiraAddComment site: 'JIRA', idOrKey: storyId, input: [body: jiraComment]
                }
            }
        }
		
        // Using sonarqube for static code review
		stage('SonarQube Analysis') {
			when {
				allOf {
					expression { return env.BUILD_CAUSE == 'SCMTRIGGER'}
					expression { return env.BRANCH_NAME == dev_branch_name}
				}
			 }
			steps{
				script{
				    def scannerHome = tool 'SonarScanner';
				    withSonarQubeEnv('SONARQUBE_SCANNER') {
				      sh "${scannerHome}/bin/sonar-scanner"
				    }
				}
			}
		}
        
        // Building application
        stage('Build monitor-service') {
			when {
				allOf {
					expression { return env.BUILD_CAUSE == 'SCMTRIGGER'}
					expression { return env.BRANCH_NAME == dev_branch_name}
				}
			 }
			withMaven {
              sh "mvn clean install"
            }
		}
        
        
        // Creating docker image
        stage('Creation of docker image'){
			when {
				allOf {
					expression { return env.BUILD_CAUSE == 'SCMTRIGGER'}
					expression { return env.BRANCH_NAME == dev_branch_name}
				}
			}
			steps{
			    script{
					echo 'Build the DockerWorkspace...'
					myDockerImage = docker.build "$registryHost/$registryName/$api"
				}
			}
		}
		
        // Cleaning up jfrog registry, (Using free tier account)
		stage('JFrog Docker registry cleanup') {
			when {
				allOf {
					expression { return env.BUILD_CAUSE == 'SCMTRIGGER'}
					expression { return env.BRANCH_NAME == dev_branch_name}
				}
			}
			steps{
				httpRequest httpMode: 'DELETE',
				url: "${registryUrl}",
				authentication: 'jfrog_creds',
				ignoreSslErrors: true,
				validResponseCodes: '200:404'
			}
	    }
		
        // Pushing docker code JFrog artifactory
		stage('Push to docker registry (JFrog)') {
			when {
				allOf {
					expression { return env.BUILD_CAUSE == 'SCMTRIGGER'}
					expression { return env.BRANCH_NAME == dev_branch_name}
				}
			}
			steps{
				script {
					myDockerImage.push("$BUILD_NUMBER")
					myDockerImage.push('latest')
				}
			}
	    }
		
        // Cleaning up locally created docker resources
		stage('Remove local docker image/resources') {
			when {
				allOf {
					expression { return env.BUILD_CAUSE == 'SCMTRIGGER'}
					expression { return env.BRANCH_NAME == dev_branch_name}
				}
			}
			steps{
				
				sh """
					docker rmi $registryHost/$registryName/$api:$BUILD_NUMBER
					docker rmi $registryHost/$registryName/$api:latest
					"""
			}
    	}
    }
}