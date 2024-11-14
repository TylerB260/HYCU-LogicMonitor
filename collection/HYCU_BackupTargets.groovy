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
httpClient.get("https://"+host+":"+port+"/rest/v1.0/targets", ["Authorization": "Basic "+auth])

json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

health_status = [
	'UNKNOWN': 0,
	'GREEN': 1,
	'YELLOW': 2,
	'RED': 3
]

for(target in json.entities){
	println target.uuid + ".advertisedCapacityInBytes=" + target.advertisedCapacityInBytes
	println target.uuid + ".controllerUsageBytes=" + target.controllerUsageBytes
	println target.uuid + ".controllerCompressedUsageBytes=" + target.controllerCompressedUsageBytes
	println target.uuid + ".controllerUtilizationPct=" + target.controllerUtilizationPct
	println target.uuid + ".totalSizeInBytes=" + target.totalSizeInBytes
	println target.uuid + ".freeSizeInBytes=" + target.freeSizeInBytes
	println target.uuid + ".avgReadBwInKBps=" + target.avgReadBwInKBps
	println target.uuid + ".avgWriteBwInKBps=" + target.avgWriteBwInKBps
	println target.uuid + ".health=" + health_status[target.health]
}

httpClient.close();

return 0;