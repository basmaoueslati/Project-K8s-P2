def call(String recipient, String stageName) {
    // Sandbox-safe log capture (last 100 lines)
    def logText = sh(
        script: "curl -s ${env.BUILD_URL}consoleText | tail -n 100",
        returnStdout: true
    ).trim()

    // Determine emoji for status
    def statusEmoji = (currentBuild.currentResult == 'SUCCESS') ? '✅' : '❌'

    emailext(
        subject: "${currentBuild.currentResult}: ${env.JOB_NAME} [${env.BUILD_NUMBER}] - ${stageName}",
        body: """<div style="font-family: Arial, sans-serif; max-width: 600px;">
               <h2 style="color: #1a5276;">${statusEmoji} ${env.JOB_NAME} - ${stageName}</h2>
               <hr style="border: 1px solid #eee;">
               <p><b>🔹 Project:</b> ${env.JOB_NAME}</p>
               <p><b>🔹 Stage:</b> ${stageName}</p>
               <p><b>🔹 Build #:</b> ${env.BUILD_NUMBER}</p>
               <p><b>🔹 Status:</b> <span style="color: ${currentBuild.currentResult == 'SUCCESS' ? '#27ae60' : '#e74c3c'}; font-weight: bold;">
                  ${currentBuild.currentResult}
               </span></p>
               <p><b>🔹 Duration:</b> ${currentBuild.durationString.replace(' and counting', '')}</p>
               <p><b>🔹 URL:</b> <a href="${env.BUILD_URL}">View Build</a></p>

               ${currentBuild.currentResult == 'FAILURE' ? """
               <hr style="border: 1px solid #eee;">
               <h3 style="color: #c0392b;">🚨 Failure Report</h3>
               <div style="background: #f9f9f9; border-left: 4px solid #e74c3c; padding: 10px; font-family: monospace; white-space: pre-wrap;">
               ${logText}
               </div>
               """ : ''}

               <hr style="border: 1px solid #eee;">
               <p style="color: #7f8c8d; font-size: 0.9em;">
               Sent automatically by Jenkins at ${new Date().format("yyyy-MM-dd HH:mm")}
               </p>
               </div>""",
        to: recipient,
        replyTo: 'no-reply@yourcompany.com',
        mimeType: 'text/html'
    )
}
