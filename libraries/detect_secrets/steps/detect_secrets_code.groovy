void call(){
    stage("Yelp/detect-secrets: Detect secrets in the code"){
        String command = config.command?:"detect-secrets"
        String pathToScan = config.path_to_scan?:"."
        String mode = config.detect_mode?:"scan"
        String image_tag = config.image_tag ?: "latest"
        String detect_secrets_img = config.image_name ?: "lirantal/detect-secrets"
        if(config.containsKey("enable_owasp")){
            inside_sdp_image "owasp-dep-check:$image_tag", {
                unstash "workspace"
                try {
                    sh "dependency-check -h"
                }catch (ex) {
                    error "Error occured when running Detect secrets: ${ex.getMessage()}"
                }
            }
        }else{
            inside_sdp_image "$detect_secrets_img:$image_tag", {
                unstash "workspace"
                try {
                    sh "${command} ${mode} ${args} ${pathToScan}"
                }catch (ex) {
                    error "Error occured when running Detect secrets: ${ex.getMessage()}"
                }
            }
        }

        println "scanning"
    }
}