package com.lw.config;


import net.sf.json.JSONObject;

public class Config {

	/** ���ݿ��ַ���������ݿ����ƣ�/���ݿ��û���/���ݿ�����/���ݿ����� */
	public final static String  DB_URL="jdbc:mysql://120.24.183.211:3306/";
	public final static String DB_USER ="root";
	public final static String DB_PWD="HSwxjwptLwCwz2015-6";
	
	/**Ȩ��*/
	private final static JSONObject PERMISSION=new JSONObject();
	
	/**��������*/
	public final static String URL="http://www.hainnujwc.com";
	/**�˿ڣ���ʱ���ã�*/
	private final static String PORT="";
	
	/**��ȡ��ǰ�˵�Ȩ�ޣ�-1:�ο�,0:ѧ��,1:��ʦ*/
	public static int getPermission(String flag){
		int permission=-1;//Ĭ���ο�Ȩ��
		PERMISSION.put("11", 0);//top
		PERMISSION.put("12", 0);
		PERMISSION.put("13", 0);
		PERMISSION.put("14", 0);
		PERMISSION.put("15", 0);//bottom
		
		PERMISSION.put("21", 1);
		PERMISSION.put("22", 1);
		PERMISSION.put("23", 1);
		PERMISSION.put("24", 1);
		PERMISSION.put("25", 1);
		
		PERMISSION.put("31", -1);
		PERMISSION.put("32", -1);
		PERMISSION.put("33", -1);
		PERMISSION.put("34", -1);
		PERMISSION.put("35", -1);
		try {
			permission=PERMISSION.getInt(flag);
		} catch (Exception e) {
			permission=-1;
		}
		
		return permission;
	}
	/**������Ϣ*/
	private static String getErrorMSG(int role){
		switch (role) {
		case 0:
			
			break;

		default:
			break;
		}
		return "";
	}
}
