package com.lw.config;


import net.sf.json.JSONObject;

public class Config {

	/** 数据库地址（不含数据库名称）/数据库用户名/数据库密码/数据库名称 */
	public final static String  DB_URL="jdbc:mysql://120.24.183.211:3306/";
	public final static String DB_USER ="root";
	public final static String DB_PWD="HSwxjwptLwCwz2015-6";
	
	/**权限*/
	private final static JSONObject PERMISSION=new JSONObject();
	
	/**访问域名*/
	public final static String URL="http://www.hainnujwc.com";
	/**端口（暂时不用）*/
	private final static String PORT="";
	
	/**获取当前菜单权限，-1:游客,0:学生,1:教师*/
	public static int getPermission(String flag){
		int permission=-1;//默认游客权限
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
	/**出错信息*/
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
