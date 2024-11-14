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
	println target.uuid + ".alive=" + (target.alive ? 1 : 0)
	println target.uuid + ".lastPing=" + target.lastPing
}

// system/controller - rich data.

httpClient.get("https://"+host+":"+port+"/rest/v1.0/dashboards/system", ["Authorization": "Basic "+auth])

json = (new JsonSlurper()).parseText(httpClient.getResponseBody());

for(target in json.entities){
	println target.bkpCtrlName + ".storageSizeInBytes=" + target.storageSizeInBytes
	println target.bkpCtrlName + ".storageUsageInBytes=" + target.storageUsageInBytes
	println target.bkpCtrlName + ".systemDiskUsage=" + target.systemDiskUsage
	println target.bkpCtrlName + ".dataDiskUsage=" + target.dataDiskUsage
	
	println target.bkpCtrlName + ".vCpuUsagePct=" + target.vCpuUsagePct
	println target.bkpCtrlName + ".vCpuUsageAvg=" + target.vCpuUsageAvg
	println target.bkpCtrlName + ".vCpuUsagePeak=" + target.vCpuUsagePeak
	
	println target.bkpCtrlName + ".memorySizeInBytes=" + target.memorySizeInBytes
	println target.bkpCtrlName + ".memoryUsagePct=" + target.memoryUsagePct
	println target.bkpCtrlName + ".memoryUsageAvg=" + target.memoryUsageAvg
	println target.bkpCtrlName + ".memoryUsagePeak=" + target.memoryUsagePeak
}

httpClient.close();

return 0;