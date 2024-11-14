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
httpClient.get("https://"+host+":"+port+"/rest/v1.0/volumegroups", ["Authorization": "Basic "+auth])

json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

for(target in json.entities){
	println target.uuid + "##" + target.name +"##" + target.protectionGroupName + "####auto.externalHypervisorId="+target.externalHypervisorId+"&auto.externalHypervisorName="+target.externalHypervisorName+"&auto.externalHypervisorType="+target.externalHypervisorType+"&auto.protectionGroupName="+target.protectionGroupName
}

httpClient.close();

return 0;