def call(String recipient) {
    // Sandbox-safe log capture (no approvals needed)
    def logText = ""
    try {
        logText = sh(script: "cat ${env.BUILD_URL}consoleText | tail -n 100", returnStdout: true).trim()
    } catch (Exception e) {
        logText = "[WARNING] Failed to capture logs: ${e.message}"
    }

    emailext(
        subject: "${currentBuild.currentResult}: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
        body: """<p><b>Project:</b> ${env.JOB_NAME}</p>
               <p><b>Status:</b> ${currentBuild.currentResult}</p>
               ${currentBuild.currentResult == 'FAILURE' ? """
               <h3>Failure Log (Last 100 lines):</h3>
               <pre>${logText}</pre>
               """ : ""}""",
        to: recipient
    )
}
