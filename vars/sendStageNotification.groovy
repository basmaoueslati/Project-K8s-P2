def call(Map args) {
    def recipient = args.recipient
    def stageName = args.stageName
    
    // Capture failure details safely
    def failureReport = ""
    if (currentBuild.currentResult == 'FAILURE') {
        def rawLog = sh(
            script: "curl -s ${env.BUILD_URL}consoleText | tail -n 200",
            returnStdout: true
        ).trim()
        
        // Simplified error extraction (removed problematic grep)
        def errorLines = rawLog.readLines().findAll { line ->
            line.contains('ERROR') || line.contains('FAILED') || line.contains('Exception')
        }.join('\n')
        
        failureReport = """
            <h3>🚨 Failure Details</h3>
            <div style="background:#f8f8f8;padding:10px;border-left:4px solid #e74c3c;">
                ${errorLines ? "<pre style='white-space:pre-wrap;'>${errorLines}</pre>" : "No specific error patterns detected"}
            </div>
        """
    }

    emailext(
        subject: "${currentBuild.currentResult}: ${env.JOB_NAME} [${env.BUILD_NUMBER}] - ${stageName}",
        body: """[REST OF YOUR EMAIL TEMPLATE UNCHANGED]""",
        to: recipient,
        mimeType: 'text/html'
    )
}
