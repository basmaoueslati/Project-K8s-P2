def call(Map args) {  // Accept a Map of named parameters
    def recipient = args.recipient
    def stageName = args.stageName
    
    // Sandbox-safe log capture
    def logText = sh(
        script: "curl -s ${env.BUILD_URL}consoleText | tail -n 100",
        returnStdout: true
    ).trim()

    emailext(
        subject: "${currentBuild.currentResult}: ${env.JOB_NAME} [${env.BUILD_NUMBER}] - ${stageName}",
        body: """<p><b>Project:</b> ${env.JOB_NAME}</p>
               <p><b>Stage:</b> ${stageName}</p>
               <p><b>Status:</b> ${currentBuild.currentResult}</p>
               ${currentBuild.currentResult == 'FAILURE' ? """
               <h3>Failure Log:</h3>
               <pre>${logText}</pre>
               """ : ''}""",
        to: recipient
    )
}
