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
httpClient.get("https://"+host+":"+port+"/rest/v1.0/administration/clusters", ["Authorization": "Basic "+auth])

json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

health_status = [
	'GREY': 0,
	'GREEN': 1,
	'YELLOW': 2,
	'RED': 3
]

for(target in json.entities){
	println target.uuid + ".health=" + health_status[target.health]
}

httpClient.close();

return 0;