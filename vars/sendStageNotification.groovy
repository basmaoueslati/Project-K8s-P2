def call(String recipient) {
    // Get build log safely (alternative to BUILD_LOG)
    def logText = currentBuild.rawBuild.getLog(100).join('\n')
    
    emailext(
        subject: "${currentBuild.currentResult}: ${env.JOB_NAME} [${env.BUILD_NUMBER}] - ${env.STAGE_NAME}",
        body: """<p><b>Project:</b> ${env.JOB_NAME}</p>
               <p><b>Status:</b> ${currentBuild.currentResult}</p>
               ${currentBuild.currentResult == 'FAILURE' ? """
               <h3>Failure Log:</h3>
               <pre>${logText}</pre>
               """ : ""}""",
        to: recipient
    )
}
