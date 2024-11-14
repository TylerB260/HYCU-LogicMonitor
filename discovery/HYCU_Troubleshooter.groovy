import com.santaba.agent.groovyapi.http.*;
import groovy.json.JsonSlurper;

// variables //

host = hostProps.get("hycu.host") ?: hostProps.get("system.hostname")
port = hostProps.get("hycu.port") ?: 8443

user = hostProps.get("hycu.user")
pass = hostProps.get("hycu.pass")

auth = (user+":"+pass).bytes.encodeBase64().toString()

// brains //

httpClient = HTTP.open(host, port, true);
httpClient.get("https://"+host+":"+port+"/rest/v1.0/administration/license", ["Authorization": "Basic "+auth])

json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

if(httpClient.getResponseBody()){	
	json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

	if(json.message.type == "ERROR"){
		println json.message.title+"##"+json.message.titleDescriptionEn+"##"+json.message.extraDetailsDescriptionEn
	}
}else{
	println "connectivity##Failure to connect to HYCU.##Check the DNS/IP address and port (default 8443, same as the web interface) you are using."
}

return 0;