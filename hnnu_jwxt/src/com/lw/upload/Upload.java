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
    	//���������ļ�������ֽ�
    			int MAX_SIZE = 5*1024*1024;
    			// ������·���ı������
    			String rootPath;
    			//�����ļ�������
    			DataInputStream in = null;
    			FileOutputStream fileOut = null;
    			//ȡ�ÿͻ��˵������ַ
//    			String remoteAddr = request.getRemoteAddr();
//    			//��÷�����������
//    			String serverName = request.getServerName();
//
//    			//ȡ�û���������ľ��Ե�ַ
//    			String realPath = request.getRealPath(serverName);
//    			System.out.println("realPath="+realPath);
//    			realPath = realPath.substring(0, realPath.lastIndexOf("\\"));
//    			System.out.println("realPath="+realPath);
//    			//�����ļ��ı���Ŀ¼
//    			rootPath = realPath + "\\upload\\";
    			rootPath =  "C:\\lw\\jxfkfile\\";
    			System.out.println("rootPath="+rootPath);
    			//ȡ�ÿͻ����ϴ�����������
    			String contentType = request.getContentType();
    			System.out.println("contentType="+contentType);
    			try {
    				String timestamp=System.currentTimeMillis()+"";
    				if (contentType.indexOf("multipart/form-data") >= 0) {
    					//�����ϴ�������
    					in = new DataInputStream(request.getInputStream());
    					int formDataLength = request.getContentLength();
    					if (formDataLength > MAX_SIZE) {
    						System.out.println("<P>�ϴ����ļ��ֽ��������Գ���" + MAX_SIZE + "</p>");

            				json.put("error", 1);
            				json.put("errorinfo", "<P>�ϴ����ļ��ֽ��������Գ���" + MAX_SIZE + "</p>");
            				
    						return json;
    					}
    					//�����ϴ��ļ�������
    					byte dataBytes[] = new byte[formDataLength];
    					int byteRead = 0;
    					int totalBytesRead = 0;
    					//�ϴ������ݱ�����byte����
    					while (totalBytesRead < formDataLength) {
    						byteRead = in.read(dataBytes, totalBytesRead,
    								formDataLength);
    						totalBytesRead += byteRead;
    					}
    					//����byte���鴴���ַ���
    					String file = new String(dataBytes);
    					//out.println(file);
    					//ȡ���ϴ������ݵ��ļ���
    					String saveFile = file.substring(file
    							.indexOf("filename=\"") + 10);
    					saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
    					saveFile = saveFile.substring(
    							saveFile.lastIndexOf("\\") + 1,
    							saveFile.indexOf("\""));
    					String geshi=saveFile.substring(saveFile.lastIndexOf("."));
    					int lastIndex = contentType.lastIndexOf("=");
    					//ȡ�����ݵķָ��ַ���
    					String boundary = contentType.substring(lastIndex + 1,
    							contentType.length());
    					
    					//��������·�����ļ���
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
    					//ȡ���ļ����ݵĿ�ʼ��λ��
    					int startPos = ((file.substring(0, pos)).getBytes()).length;
    					//out.println(startPos);
    					//ȡ���ļ����ݵĽ�����λ��
    					int endPos = ((file.substring(0, boundaryLocation))
    							.getBytes()).length;
    					//out.println(endPos);
    					//��������ļ��Ƿ����
    					File checkFile = new File(fileName);
    					if (checkFile.exists()) {
    						System.out.println("<p>" + saveFile + "�ļ��Ѿ�����.</p>");
    					}
    					//��������ļ���Ŀ¼�Ƿ����
    					File fileDir = new File(rootPath);
    					if (!fileDir.exists()) {
    						fileDir.mkdirs();
    					}
    					//�����ļ���д����
    					fileOut = new FileOutputStream(fileName);
    					//�����ļ�������
    					fileOut.write(dataBytes, startPos, (endPos - startPos));
    					fileOut.close();
    					System.out.println(saveFile + "�ļ��ɹ�����.</p>");
    					json.put("error", 0);
    				} else {
    					String content = request.getContentType();
    					System.out.println("<p>�ϴ����������Ͳ���multipart/form-data</p>");

        				json.put("error", 1);
        				json.put("errorinfo", "<p>�ϴ����������Ͳ���multipart/form-data</p>");
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
