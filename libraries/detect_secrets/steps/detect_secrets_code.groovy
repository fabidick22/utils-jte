void call(){
    stage("Yelp/detect-secrets: Detect secrets in the code"){
        String command = config.command?:"detect-secrets"
        String pathToScan = config.path_to_scan?:"."
        String mode = config.detect_mode?:"scan"
        String image_tag = config.image_tag ?: "latest"
        inside_sdp_image "lirantal/detect-secrets:$image_tag" {
            unstash "workspace"
            try {
                sh "${command} ${mode} ${args} ${pathToScan}"
            }catch (ex) {
                error "Error occured when running Detect secrets: ${ex.getMessage()}"
            }
        }
        println "scanning"
    }
}