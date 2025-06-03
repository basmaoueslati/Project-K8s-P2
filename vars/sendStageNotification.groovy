def call(Map args) {
    def recipient = args.recipient
    def stageName = args.stageName
    
    // Capture full failure details (sandbox-safe)
    def failureReport = ""
    if (currentBuild.currentResult == 'FAILURE') {
        failureReport = """
            <h3>🚨 Failure Details</h3>
            <div style="background:#f8f8f8;padding:10px;border-left:4px solid #e74c3c;">
                <p><b>Error Phase:</b> ${stageName}</p>
                <p><b>Failed Command:</b> <code>${getFailedCommand()}</code></p>
                <h4>Full Error Log:</h4>
                <pre style="white-space:pre-wrap;">${sh(script: "curl -s ${env.BUILD_URL}consoleText | tail -n 200", returnStdout: true)}</pre>
            </div>
        """
    }

    emailext(
        subject: "${currentBuild.currentResult}: ${env.JOB_NAME} [${env.BUILD_NUMBER}] - ${stageName}",
        body: """<div style="font-family:Arial,sans-serif;max-width:800px;">
               <h2 style="color:#${currentBuild.currentResult == 'SUCCESS' ? '27ae60' : 'e74c3c'};">
                   ${currentBuild.currentResult == 'SUCCESS' ? '✅' : '❌'} 
                   ${env.JOB_NAME} - ${stageName}
               </h2>
               <hr>
               <p><b>🔹 Project:</b> ${env.JOB_NAME}</p>
               <p><b>🔹 Stage:</b> ${stageName}</p>
               <p><b>🔹 Build #:</b> ${env.BUILD_NUMBER}</p>
               <p><b>🔹 Status:</b> <span style="color:#${currentBuild.currentResult == 'SUCCESS' ? '27ae60' : 'e74c3c'};font-weight:bold;">
                   ${currentBuild.currentResult}
               </span></p>
               <p><b>🔹 Duration:</b> ${currentBuild.durationString}</p>
               ${failureReport}
               <hr>
               <p style="color:#7f8c8d;font-size:0.9em;">
                   <a href="${env.BUILD_URL}" style="color:#3498db;">View Full Build Log</a> | 
                   Sent at ${new Date().format("yyyy-MM-dd HH:mm")}
               </p>
               </div>""",
        to: recipient,
        mimeType: 'text/html'
    )
}

// Helper function to identify the failed command
def getFailedCommand() {
    def log = sh(script: "curl -s ${env.BUILD_URL}consoleText | grep -B 1 'ERROR\|FAILED\|Exception' | head -n 1", returnStdout: true).trim()
    return log ?: "Unable to detect specific failed command"
}
