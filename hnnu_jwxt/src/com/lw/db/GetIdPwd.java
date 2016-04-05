package com.lw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.lw.jiami.CryptAES;

public class GetIdPwd {

	public Map getIdPwd(String openID){
		Map user_map=new HashMap();

		user_map.put("userStatus", -1);
CryptAES cAes=new CryptAES();
        
		
		Connection connection = null;
		ResultSet rs=null;
		try {
			PreparedStatement ps=null;
			String sql = "",sql2="";
			int role=-1;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(
					"jdbc:mysql://120.24.183.211:3306/hnnu_jwc", DB.sql_user,
					DB.sql_pwd);
            sql2="select * from user where openID='"+openID+"'";
            ps = connection.prepareStatement(sql2);

			rs=ps.executeQuery();
			while(rs.next()){
				role=rs.getInt("role");
			}
            ps=null;
            if (1==role) {//教师
            	sql = "select * from userbining_teacher where openID='"+openID+"'";//
            	 
			}else {//学生
				sql = "select * from userbining where openID='"+openID+"'";//
			}
            
            
			
			 ps = connection.prepareStatement(sql);

			rs=ps.executeQuery();
			while(rs.next()){
				user_map.put("id", rs.getString("id"));

				String psdString=cAes.AES_Decrypt("LW#CWZ@HS_jwc&@@", rs.getString("psd").toString());
				user_map.put("psd", psdString);
				user_map.put("userStatus", 1);
			}
			
			
			connection.close();
			return user_map;
		}
		 catch (Exception e) {
				user_map.put("userStatus", 0);
				user_map.put("error", e.toString());
			System.out.println("error===="+e.toString());
		}finally{
			cAes=null;
		}
		
		return user_map;
	}

}
