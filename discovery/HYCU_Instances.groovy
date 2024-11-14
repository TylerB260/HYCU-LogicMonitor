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

// file and mover instances - alive status, version, heartbeat.
httpClient.get("https://"+host+":"+port+"/rest/v1.0/instances", ["Authorization": "Basic "+auth])

json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

for(target in json.entities){
	println target.uuid + "##" + target.name +"##" + target.hycuVersion + "####auto.clusterName="+target.clusterName+"&auto.instanceType="+target.instanceType+"&auto.ipAddress="+target.ipAddress
}

// system/controller - rich data.

httpClient.get("https://"+host+":"+port+"/rest/v1.0/dashboards/system", ["Authorization": "Basic "+auth])

json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

for(target in json.entities){
	println target.bkpCtrlName + "##" + target.bkpCtrlName +"##" + target.bkpCtrlVersion + "####auto.timezone="+target.timezone+"&auto.instanceType="+target.containerName
}

httpClient.close();

return 0;