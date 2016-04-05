package com.lw.methods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import com.lw.dbpool.DBPhnnujwc;
import com.lw.dbpool.DBPstukebiao;
import com.lw.dbpool.DBPstuscore;
import com.lw.dbpool.DBPtchkebiao;
import net.sf.json.JSONObject;

@javax.jws.WebService(targetNamespace = "http://methods.lw.com/", serviceName = "RequestRESService", portName = "RequestRESPort", wsdlLocation = "WEB-INF/wsdl/RequestRESService.wsdl")
public class RequestRESDelegate {

	com.lw.methods.RequestRES requestRES = new com.lw.methods.RequestRES();

	public String requestRES(String inJsonStr) {
		return requestRES.requestRES(inJsonStr);
	}

}