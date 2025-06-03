def call(String recipient) {
    emailext(
        subject: "${currentBuild.currentResult}: ${env.JOB_NAME} [${env.BUILD_NUMBER}] - ${env.STAGE_NAME}",
        body: """<p><b>Project:</b> ${env.JOB_NAME}</p>
               <p><b>Status:</b> ${currentBuild.currentResult}</p>
               ${currentBuild.currentResult == 'FAILURE' ? "<pre>${BUILD_LOG, maxLines=100}</pre>" : ""}""",
        to: recipient
    )
}