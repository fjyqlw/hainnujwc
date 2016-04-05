package com.lw.webservice;

import java.sql.ResultSet;
import net.sf.json.JSONObject;
import com.lw.db.DBhnnujwc;
import com.lw.db.DBstukebiao;
import com.lw.db.DBstuscore;
import com.lw.db.DBtchkebiao;

@javax.jws.WebService(targetNamespace = "http://webservice.lw.com/", serviceName = "SevService", portName = "SevPort", wsdlLocation = "WEB-INF/wsdl/SevService.wsdl")
public class SevDelegate {

	com.lw.webservice.Sev sev = new com.lw.webservice.Sev();

	public String login(String id, String pwd) {
		return sev.login(id, pwd);
	}

	public String getKebiaoData(String userinfoString) {
		return sev.getKebiaoData(userinfoString);
	}

	public String getScoreData(String userinfoString) {
		return sev.getScoreData(userinfoString);
	}

}