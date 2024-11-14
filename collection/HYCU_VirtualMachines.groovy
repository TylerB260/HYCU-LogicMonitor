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
httpClient.get("https://"+host+":"+port+"/rest/v1.0/vms", ["Authorization": "Basic "+auth])

json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

compliance_status = [
	'GREY': 0,
	'GREEN': 1,
	'YELLOW': 2,
	'RED': 3
]

discovery_status = [
	'UNKNOWN': 0,
	'OK': 1,
	'ERROR': 2
]

protection_status = [
	'UNDEFINED': 0,
	'PROTECTED_DELETED': 0,
	'PROTECTED': 1,
	'UNPROTECTED': 2
]

for(target in json.entities){
	println target.uuid + ".protectionStatus=" + protection_status[target.status]
	println target.uuid + ".complianceStatus=" + compliance_status[target.compliancyStatus]
	println target.uuid + ".discoveryStatus=" + discovery_status[target.discoveryStatus]
}

httpClient.close();

return 0;