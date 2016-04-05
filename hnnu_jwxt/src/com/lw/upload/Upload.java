package com.lw.upload;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class Upload {
    public static JSONObject doUpload(HttpServletRequest request ){
    	JSONObject json=new JSONObject();
		json.put("error", 1);
    	//定义上载文件的最大字节
    			int MAX_SIZE = 5*1024*1024;
    			// 创建根路径的保存变量
    			String rootPath;
    			//声明文件读入类
    			DataInputStream in = null;
    			FileOutputStream fileOut = null;
    			//取得客户端的网络地址
//    			String remoteAddr = request.getRemoteAddr();
//    			//获得服务器的名字
//    			String serverName = request.getServerName();
//
//    			//取得互联网程序的绝对地址
//    			String realPath = request.getRealPath(serverName);
//    			System.out.println("realPath="+realPath);
//    			realPath = realPath.substring(0, realPath.lastIndexOf("\\"));
//    			System.out.println("realPath="+realPath);
//    			//创建文件的保存目录
//    			rootPath = realPath + "\\upload\\";
    			rootPath =  "C:\\lw\\jxfkfile\\";
    			System.out.println("rootPath="+rootPath);
    			//取得客户端上传的数据类型
    			String contentType = request.getContentType();
    			System.out.println("contentType="+contentType);
    			try {
    				String timestamp=System.currentTimeMillis()+"";
    				if (contentType.indexOf("multipart/form-data") >= 0) {
    					//读入上传的数据
    					in = new DataInputStream(request.getInputStream());
    					int formDataLength = request.getContentLength();
    					if (formDataLength > MAX_SIZE) {
    						System.out.println("<P>上传的文件字节数不可以超过" + MAX_SIZE + "</p>");

            				json.put("error", 1);
            				json.put("errorinfo", "<P>上传的文件字节数不可以超过" + MAX_SIZE + "</p>");
            				
    						return json;
    					}
    					//保存上传文件的数据
    					byte dataBytes[] = new byte[formDataLength];
    					int byteRead = 0;
    					int totalBytesRead = 0;
    					//上传的数据保存在byte数组
    					while (totalBytesRead < formDataLength) {
    						byteRead = in.read(dataBytes, totalBytesRead,
    								formDataLength);
    						totalBytesRead += byteRead;
    					}
    					//根据byte数组创建字符串
    					String file = new String(dataBytes);
    					//out.println(file);
    					//取得上传的数据的文件名
    					String saveFile = file.substring(file
    							.indexOf("filename=\"") + 10);
    					saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
    					saveFile = saveFile.substring(
    							saveFile.lastIndexOf("\\") + 1,
    							saveFile.indexOf("\""));
    					String geshi=saveFile.substring(saveFile.lastIndexOf("."));
    					int lastIndex = contentType.lastIndexOf("=");
    					//取得数据的分隔字符串
    					String boundary = contentType.substring(lastIndex + 1,
    							contentType.length());
    					
    					//创建保存路径的文件名
    					String fileName = rootPath +timestamp +geshi;//saveFile;

        				json.put("filename", timestamp+geshi);
    					//out.print(fileName);
    					int pos;
    					pos = file.indexOf("filename=\"");
    					pos = file.indexOf("\n", pos) + 1;
    					pos = file.indexOf("\n", pos) + 1;
    					pos = file.indexOf("\n", pos) + 1;
    					int boundaryLocation = file.indexOf(boundary, pos) - 4;
    					//out.println(boundaryLocation);
    					//取得文件数据的开始的位置
    					int startPos = ((file.substring(0, pos)).getBytes()).length;
    					//out.println(startPos);
    					//取得文件数据的结束的位置
    					int endPos = ((file.substring(0, boundaryLocation))
    							.getBytes()).length;
    					//out.println(endPos);
    					//检查上载文件是否存在
    					File checkFile = new File(fileName);
    					if (checkFile.exists()) {
    						System.out.println("<p>" + saveFile + "文件已经存在.</p>");
    					}
    					//检查上载文件的目录是否存在
    					File fileDir = new File(rootPath);
    					if (!fileDir.exists()) {
    						fileDir.mkdirs();
    					}
    					//创建文件的写出类
    					fileOut = new FileOutputStream(fileName);
    					//保存文件的数据
    					fileOut.write(dataBytes, startPos, (endPos - startPos));
    					fileOut.close();
    					System.out.println(saveFile + "文件成功上载.</p>");
    					json.put("error", 0);
    				} else {
    					String content = request.getContentType();
    					System.out.println("<p>上传的数据类型不是multipart/form-data</p>");

        				json.put("error", 1);
        				json.put("errorinfo", "<p>上传的数据类型不是multipart/form-data</p>");
    				}
    			} catch (Exception ex) {
    				//throw new ServletException(ex.getMessage());
    				System.out.println(ex.toString());
    				json.put("error", 1);
    				json.put("errorinfo", ex.toString());
    				
    			}
    			return json;
    }
}
