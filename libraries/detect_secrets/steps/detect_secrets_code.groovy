void call(){
    stage("Yelp/detect-secrets: Detect secrets in the code"){
        String command = config.command?:"detect-secrets"
        String args = config.args?:""
        String custom_command = config.custom_command
        String pathToScan = config.path_to_scan?:"."
        String mode = config.detect_mode?:"scan"
        String image_tag = config.image_tag ?: "latest"
        String detect_secrets_img = config.image_name ?: "detect-secrets"
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
            String cmd = custom_command ? custom_command : "${command} ${mode} ${args} ${pathToScan}"
            inside_sdp_image "$detect_secrets_img:$image_tag", {
                unstash "workspace"
                try {
                    sh cmd
                }catch (ex) {
                    error "Error occured when running Detect secrets: ${ex.getMessage()}"
                }
            }
        }

        println "scanning"
    }
}