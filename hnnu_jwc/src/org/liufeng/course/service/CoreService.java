package org.liufeng.course.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.liufeng.course.message.resp.Article;
import org.liufeng.course.message.resp.NewsMessage;
import org.liufeng.course.message.resp.TextMessage;
import org.liufeng.course.util.MessageUtil;

import com.lw.bining.Bining;
import com.lw.config.Config;
import com.lw.config.Methods;
import com.lw.dbpool.DBPhnnujwc;
import com.lw.dbpool.DBPstukebiao;
import com.lw.dbpool.DBPstuscore;
import com.lw.dbpool.DBPtchkebiao;
import com.lw.errorinfo.ERROR_INFO;
import com.lw.errorinfo.ERROR_KEY;
import com.lw.jiami.CryptAES;
import com.lw.methods.RequestRES;
import com.lw.methods.RequestRESDelegate;
import com.lw.methods.RequestRESService;
import com.lw.permission.PermissionManger;

/**
 * ���ķ�����
 * 
 * @author ����
 * @date 2016-03-20
 * @version 1.1
 */
public class CoreService {

	// private Bining bin = new Bining();
	/**
	 * ���������� 1 ��; 2 ���; 1100 �α�; 1200 �ɼ�; 1201 ����ɼ�; 1202 ѧ��ɼ�; 1300 ��ѡ�γ�;
	 * 1400 ���Ҳ�ѯ; 15001 ��Ϸ; 15002 Ӧ��; 3100 У��&����ʱ��;
	 */
	private static int vType = 0;
	

	/**
	 * ����΢�ŷ���������
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			NewsMessage newsMessage = new NewsMessage();
			List<Article> articleList = new ArrayList<Article>();
			// Ĭ�Ϸ��ص��ı���Ϣ����
			String respContent = "�������쳣�����Ժ��ԣ�";
			// xml�������
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// ���ͷ��ʺţ�open_id��
			String fromUserName = requestMap.get("FromUserName");
			// �����ʺ�
			String toUserName = requestMap.get("ToUserName");
			// ��Ϣ����
			String msgType = requestMap.get("MsgType");

			// �ظ��ı���Ϣ
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			// �ı���Ϣ
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				String ContenText = requestMap.get("Content");

				/** ��¼��Ϣ */
                try {
					RequestRESService rrs=new RequestRESService();
					RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
					JSONObject inJson=new JSONObject();
					/**��������*/
					inJson.put(ERROR_KEY.METHOD, Methods.msgRecord);
					/**�������*/
					JSONObject paramJson=new JSONObject();
					paramJson.put("openID", fromUserName);
					paramJson.put("content", ContenText);
					inJson.put(ERROR_KEY.PARAMTER, paramJson);
					/**����ӿ�����*/
					rrd.requestRES(inJson.toString());
					
				} catch (Exception e) {
					System.out.println("�ӿ�ģ�����"+e.toString());
				}
				// respContent = "����Ϣ�����Բ�ɼ�������֤��¼����Բ鿴����ɼ���ѧ��/ѧ��ɼ���";
				// textMessage.setContent(respContent);
				// respMessage = MessageUtil.textMessageToXml(textMessage);
				/** ����ʶ�𷽷���������Ϊ���˹ؼ����ã����عؼ������� */
				int vType = getVoiceType(ContenText);
				return resMSG(vType, msgType, newsMessage, fromUserName,
						toUserName, respMessage, articleList, respContent,
						textMessage);
			}
			// ͼƬ��Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "�����͵���ͼƬ��Ϣ��";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// ����λ����Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "�����͵��ǵ���λ����Ϣ��";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// ������Ϣ
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "�����͵���������Ϣ��";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// ��Ƶ��Ϣ;
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {

				respContent = requestMap.get("Recognition");

				int vType = getVoiceType(respContent);

				return resMSG(vType, msgType, newsMessage, fromUserName,
						toUserName, respMessage, articleList, respContent,
						textMessage);

			}
			// �¼�����
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// �¼�����
				String eventType = requestMap.get("Event");
				// ����
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "���ڵȵ��㣡��ʦ����񴦻�ӭ����\ue056";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// ȡ������
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO ȡ�����ĺ��û����ղ������ںŷ��͵���Ϣ����˲���Ҫ�ظ���Ϣ
					Connection conn = null;
					ResultSet rs = null;
					Statement stmt = null;
					try {
						//JSONObject json = Bining.getBiningInfo(fromUserName);
						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.biningInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						

						String sql = "", sql2 = "", Id = "";
						switch (outJson.getInt("error")) {
						case 0:// �Ѱ�
							switch (outJson.getInt("role")) {
							case 0:// ѧ��
								try {

									sql = "delete from user where openID='"
											+ fromUserName + "'";
									sql2 = "select id from user where openID='"
											+ fromUserName + "'";

									// rs =
									// DBhnnujwc.getInstance().querySQL(sql2);
									conn = DBPhnnujwc.getPool().getConnection();
									stmt = conn.createStatement();
									stmt.executeQuery(sql2);
									while (rs.next()) {
										Id = rs.getString("id");
									}
									try {
										rs.close();
									} catch (Exception e) {
										// TODO: handle exception
									}

									// DBhnnujwc.getInstance().execSQL(sql);
									stmt.executeUpdate(sql);
									try {
										stmt.close();
										stmt = null;
									} catch (Exception e) {
										// TODO: handle exception
									}
									try {
										conn.close();
										conn = null;
									} catch (Exception e) {
										// TODO: handle exception
									}
									/** ɾ���ɼ��� */
									sql = "drop table if exists cj" + Id;
									// DBstuscore.getInstance().execSQL(sql);
									conn = DBPstuscore.getPool()
											.getConnection();
									stmt = conn.createStatement();
									stmt.executeUpdate(sql);
									try {
										stmt.close();
									} catch (Exception e) {
										// TODO: handle exception
									}
									try {
										conn.close();
									} catch (Exception e) {
										// TODO: handle exception
									}
									/** ɾ���α� */
									sql = "drop table if exists kb" + Id;
									// DBstukebiao.getInstance().execSQL(sql);
									conn = DBPstukebiao.getPool()
											.getConnection();
									stmt = conn.createStatement();
									stmt.executeUpdate(sql);
									try {
										stmt.close();
									} catch (Exception e) {
										// TODO: handle exception
									}
									try {
										conn.close();
									} catch (Exception e) {
										// TODO: handle exception
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
								break;
							case 1:// ��ʦ
								sql = "delete from user where openID='"
										+ fromUserName + "'";

								sql2 = "select id from user where openID='"
										+ fromUserName + "'";

								//rs = DBhnnujwc.getInstance().querySQL(sql2);
                                 conn=DBPhnnujwc.getPool().getConnection();
                                 stmt=conn.createStatement();
                                 rs=stmt.executeQuery(sql2);
								while (rs.next()) {
									Id = rs.getString("id");
								}
                                try {
									rs.close();
								} catch (Exception e) {
									// TODO: handle exception
								}
								//DBhnnujwc.getInstance().execSQL(sql);
								stmt.executeUpdate(sql);
								try {
									stmt.close();
								} catch (Exception e) {
									// TODO: handle exception
								}
								try {
									conn.close();
								} catch (Exception e) {
									// TODO: handle exception
								}
								/** ɾ���α� */
								sql = "drop table if exists tch" + Id;
								//DBtchkebiao.getInstance().execSQL(sql);
								
								conn=DBPtchkebiao.getPool().getConnection();
								stmt=conn.createStatement();
								stmt.executeUpdate(sql);
								break;

							default:
								break;
							}
							break;
						case 1:// δ��
							break;
						case 2:
							break;
						default:
							break;

						}

					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
				// �Զ���˵�����¼�
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// �¼�KEYֵ���봴���Զ���˵�ʱָ����KEYֵ��Ӧ
					String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("11")) {

						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// ����(�˵��������Ѱ󶨡�Ȩ����ȷ)
								// Ȩ����ȷ
								// ����ͼ����Ϣ
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);

							String xh = "",
							xm = "",
							psd = "";

							Connection conn = null;
							ResultSet rs = null;
							Statement stmt = null;
							try {

								StringBuffer sql = new StringBuffer();
								sql.append("select * from userbining where openID='");
								sql.append(fromUserName);
								sql.append("'");

								conn = DBPhnnujwc.getPool().getConnection();
								stmt = conn.createStatement();
								rs = stmt.executeQuery(sql.toString());

								// rs = DBhnnujwc.getInstance().querySQL(
								// sql.toString());
								sql.delete(0, sql.length());// �ͷ�StringBuffer�ڴ�
								while (rs.next()) {
									xh = rs.getString("id");
									xm = rs.getString("name");
									psd = rs.getString("psd");
								}
								rs.close();
							} catch (Exception e) {
								xh = e.toString();
							} finally {
								try {
									rs.close();
								} catch (Exception e2) {
									// TODO: handle exception
								}
								try {
									stmt.close();
								} catch (Exception e2) {
									// TODO: handle exception
								}
								try {
									conn.close();
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
							Article article0 = new Article();
							article0.setTitle("");
							article0.setPicUrl(Config.URL
									+ ":8080/kebiao/images/kb.png");
							article0.setUrl("");
							articleList.add(article0);

							Article article1 = new Article();
							article1.setTitle("���տα�");
							article1.setDescription("");
							article1.setPicUrl("");
							article1.setUrl(Config.URL
									+ ":8080/kebiao/kebiao_today.php?openID="
									+ fromUserName);
							articleList.add(article1);

							Article article2 = new Article();
							article2.setTitle("ѧ�ڿα�");
							article2.setDescription("");
							article2.setPicUrl("");
							article2.setUrl(Config.URL
									+ ":8080/kebiao/index.php?openID="
									+ fromUserName);
							articleList.add(article2);
							// ����ͼ����Ϣ����
							newsMessage.setArticleCount(articleList.size());
							// ����ͼ����Ϣ������ͼ�ļ���
							newsMessage.setArticles(articleList);
							// ��ͼ����Ϣ����ת����xml�ַ���
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// δ��

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// �˵��ر�
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "��û��Ȩ�ޣ�";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** ��������̨��־ */
                            System.out.println(outJson);
							respContent = "ϵͳ��æ��";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("12")) {

						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// ����(�˵��������Ѱ󶨡�Ȩ����ȷ)
								// Ȩ����ȷ
								// ����ͼ����Ϣ
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);

							Article article0 = new Article();
							article0.setTitle("�ɼ���ѯ");
							article0.setDescription("");
							// ��ͼƬ��Ϊ��
							article0.setPicUrl("");
							article0.setUrl("");

							Article article1 = new Article();
							article1.setTitle("����ɼ�");
							article1.setDescription("");
							// ��ͼƬ��Ϊ��
							article1.setPicUrl("");
							article1.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/lncj.jsp?openID="
									+ fromUserName + "&update=1");

							Article article2 = new Article();
							article2.setTitle("ѧ��/ѧ�ڳɼ�");
							article2.setDescription("");
							article2.setPicUrl("");
							article2.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/selxq.jsp?openID="
									+ fromUserName + "&update=1");

							Article article4 = new Article();
							article4.setTitle("��߳ɼ�");
							article4.setDescription("");
							article4.setPicUrl("");
							article4.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/zgcj.jsp?openID="
									+ fromUserName + "&update=1");

							Article article5 = new Article();
							article5.setTitle("δͨ���ɼ�");
							article5.setDescription("");
							article5.setPicUrl("");
							article5.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/wtgcj.jsp?openID="
									+ fromUserName + "&update=0");

							Article article6 = new Article();
							article6.setTitle("�ɼ�ͳ��");
							article6.setDescription("");
							article6.setPicUrl("");
							article6.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/cjtj.jsp?openID="
									+ fromUserName);

							articleList.add(article0);
							articleList.add(article1);
							articleList.add(article2);
							// articleList.add(article3);
							articleList.add(article4);
							articleList.add(article5);
							articleList.add(article6);
							newsMessage.setArticleCount(articleList.size());
							newsMessage.setArticles(articleList);
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// δ��

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// �˵��ر�
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "��û��Ȩ�ޣ�";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** ��������̨��־ */
                            System.out.println(outJson);
							respContent = "ϵͳ��æ��";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("13")) {

						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// ����(�˵��������Ѱ󶨡�Ȩ����ȷ)
								// Ȩ����ȷ
								// ����ͼ����Ϣ
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);
							Article article = new Article();
							article.setTitle("��ѡ�γ�");
							article.setDescription("��ѡ��רҵѡ�ް�����ѡ��");
							article.setPicUrl("");
							article.setUrl(Config.URL
									+ "/hnnu_jwxt/XuanKeChaXun/XuanKeChaXun.jsp?openID="
									+ fromUserName);
							articleList.add(article);
							// ����ͼ����Ϣ����
							newsMessage.setArticleCount(articleList.size());
							// ����ͼ����Ϣ������ͼ�ļ���
							newsMessage.setArticles(articleList);
							// ��ͼ����Ϣ����ת����xml�ַ���
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// δ��

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// �˵��ر�
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "��û��Ȩ�ޣ�";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** ��������̨��־ */
                            System.out.println(outJson);
							respContent = "ϵͳ��æ��";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("14")) {


						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// ����(�˵��������Ѱ󶨡�Ȩ����ȷ)
								// Ȩ����ȷ
								// ����ͼ����Ϣ
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);
							Article article = new Article();
							article.setTitle("���Ҳ�ѯ");
							article.setDescription("��ѯ���ÿս�����Ϣ");
							article.setPicUrl("");
							article.setUrl(Config.URL
									+ "/hnnu_jwxt/EmptyClassRooms/selxq.jsp?openID="
									+ fromUserName);
							articleList.add(article);
							// ����ͼ����Ϣ����
							newsMessage.setArticleCount(articleList.size());
							// ����ͼ����Ϣ������ͼ�ļ���
							newsMessage.setArticles(articleList);
							// ��ͼ����Ϣ����ת����xml�ַ���
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// δ��

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// �˵��ر�
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "��û��Ȩ�ޣ�";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** ��������̨��־ */
                            System.out.println(outJson);
							respContent = "ϵͳ��æ��";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("15")) {

						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// ����(�˵��������Ѱ󶨡�Ȩ����ȷ)
								// Ȩ����ȷ
								// ����ͼ����Ϣ
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);
							Article article1 = new Article();
							article1.setTitle("������Ϸ");
							article1.setDescription("");
							// ��ͼƬ��Ϊ��
							article1.setPicUrl("");
							article1.setUrl(Config.URL + "/games/index.html");

							articleList.add(article1);
							newsMessage.setArticleCount(articleList.size());
							newsMessage.setArticles(articleList);
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// δ��

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// �˵��ر�
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "��û��Ȩ�ޣ�";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** ��������̨��־ */
                            System.out.println(outJson);
							respContent = "ϵͳ��æ��";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("21")) {

						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// ����(�˵��������Ѱ󶨡�Ȩ����ȷ)
								// Ȩ����ȷ
								// ����ͼ����Ϣ
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);

							Article article1 = new Article();
							article1.setTitle("������Ϣ");
							article1.setDescription("");
							// ��ͼƬ��Ϊ��
							article1.setPicUrl("");
							article1.setUrl(Config.URL
									+ "/hnnu_jwxt/teacher/teacherinfo.jsp?openID="
									+ fromUserName + "&update=1");

							articleList.add(article1);
							newsMessage.setArticleCount(articleList.size());
							newsMessage.setArticles(articleList);
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// δ��

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// �˵��ر�
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "��û��Ȩ�ޣ�";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** ��������̨��־ */
                            System.out.println(outJson);
							respContent = "ϵͳ��æ��";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("22")) {

						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// ����(�˵��������Ѱ󶨡�Ȩ����ȷ)
								// Ȩ����ȷ
								// ����ͼ����Ϣ
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);

							String xh = "112",
							xm = "",
							psd = "";

							Connection conn = null;
							ResultSet rs = null;
							Statement stmt = null;
							try {

								StringBuffer sql = new StringBuffer();
								sql.append("select * from userbining_teacher where openID='");
								sql.append(fromUserName);
								sql.append("'");

								// rs = DBhnnujwc.getInstance().querySQL(
								// sql.toString());
								conn = DBPhnnujwc.getPool().getConnection();
								stmt = conn.createStatement();
								rs = stmt.executeQuery(sql.toString());

								while (rs.next()) {
									xh = rs.getString("id");
									xm = rs.getString("name");
									psd = rs.getString("psd");
								}
								sql.delete(0, sql.length());
							} catch (Exception e) {
								xh = e.toString();
							} finally {
								try {
									rs.close();
								} catch (Exception e2) {
									// TODO: handle exception
								}
								try {
									stmt.close();
								} catch (Exception e2) {
									// TODO: handle exception
								}
								try {
									conn.close();
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
							Article article0 = new Article();
							article0.setTitle("");
							article0.setPicUrl(Config.URL
									+ ":8080/kebiao/images/kb.png");
							article0.setUrl("");
							articleList.add(article0);

							Article article1 = new Article();
							article1.setTitle("���տα�");
							article1.setDescription("");
							article1.setPicUrl("");
							article1.setUrl(Config.URL
									+ ":8080/kebiao/teacher_todaykebiao.php?openID="
									+ fromUserName);
							articleList.add(article1);

							Article article2 = new Article();
							article2.setTitle("ѧ�ڿα�");
							article2.setDescription("");
							article2.setPicUrl("");
							article2.setUrl(Config.URL
									+ ":8080/kebiao/teacher_kebiao.php?openID="
									+ fromUserName);
							articleList.add(article2);
							// ����ͼ����Ϣ����
							newsMessage.setArticleCount(articleList.size());
							// ����ͼ����Ϣ������ͼ�ļ���
							newsMessage.setArticles(articleList);
							// ��ͼ����Ϣ����ת����xml�ַ���
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// δ��

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// �˵��ر�
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "��û��Ȩ�ޣ�";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;

						default:
							/** ��������̨��־ */
                            System.out.println(outJson);
							respContent = "ϵͳ��æ��";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("23")) {

						JSONObject outJson = null;
						/**��ȡ�û�����Ϣ*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**��������*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**�������*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**����ӿ�����*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("�ӿ�ģ�����"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// ����(�˵��������Ѱ󶨡�Ȩ����ȷ)
								// Ȩ����ȷ
								// ����ͼ����Ϣ
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);
							Article article = new Article();
							article.setTitle("���Ҳ�ѯ");
							article.setDescription("��ѯ���ÿս�����Ϣ");
							article.setPicUrl("");
							article.setUrl(Config.URL
									+ "/hnnu_jwxt/EmptyClassRoomsTch/selxq.jsp?openID="
									+ fromUserName);
							articleList.add(article);
							// ����ͼ����Ϣ����
							newsMessage.setArticleCount(articleList.size());
							// ����ͼ����Ϣ������ͼ�ļ���
							newsMessage.setArticles(articleList);
							// ��ͼ����Ϣ����ת����xml�ַ���
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// δ��

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// �˵��ر�
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "��û��Ȩ�ޣ�";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** ��������̨��־ */
                            System.out.println(outJson);
							respContent = "ϵͳ��æ��";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("24")) {
						respContent = "�����С�������";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("25")) {
						respContent = "�����С�����";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("31")) {
						// ����ͼ����Ϣ
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage
								.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
						Article article = new Article();
						article.setTitle("��ʦУ��");
						article.setDescription("����ʦ����ѧ2015-2016ѧ���У��");
						article.setPicUrl(Config.URL + "/rc/hnnu_jwc/a.jpg");
						article.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTM2MTQ0Nw==&mid=206652775&idx=1&sn=46ce4b080bfb42bf10345916691bd161#rd");
						articleList.add(article);
						// ����ͼ����Ϣ����
						newsMessage.setArticleCount(articleList.size());
						// ����ͼ����Ϣ������ͼ�ļ���
						newsMessage.setArticles(articleList);
						// ��ͼ����Ϣ����ת����xml�ַ���
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
					} else if (eventKey.equals("32")) {
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage
								.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
						Article article1 = new Article();
						article1.setTitle("��Ϣʱ��");
						article1.setDescription("����ʦ����ѧ��Ϣʱ���");
						// ��ͼƬ��Ϊ��
						article1.setPicUrl("");
						article1.setUrl(Config.URL
								+ "/hnnu_jwxt/publicServer/zxsj.html");

						articleList.add(article1);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
					} else if (eventKey.equals("33")) {
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage
								.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
						Article article1 = new Article();
						article1.setTitle("��ѧ����");
						article1.setDescription("��ѧ����");
						// ��ͼƬ��Ϊ��
						article1.setPicUrl("");
						article1.setUrl(Config.URL
								+ "/hnnu_jwxt/publicServer/jxfk.jsp");

						articleList.add(article1);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
					} else if (eventKey.equals("34")) {
						respContent = "��APP";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("35")) {

						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage
								.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);

						Article article0 = new Article();
						article0.setTitle("����������-���๦��");
						article0.setDescription("");
						// ��ͼƬ��Ϊ��
						article0.setPicUrl("");
						article0.setUrl("");

						Article article1 = new Article();
						article1.setTitle("ȡ����");
						article1.setDescription("");
						// ��ͼƬ��Ϊ��
						article1.setPicUrl("");
						article1.setUrl(Config.URL
								+ "/hnnu_jwxt/bining/unbining.jsp?openID="
								+ fromUserName);

						Article article2 = new Article();
						article2.setTitle("����ָ��");
						article2.setDescription("");
						article2.setPicUrl("");
						article2.setUrl(Config.URL
								+ ":8080/kebiao/banshizhinan.html");

						articleList.add(article0);
						articleList.add(article2);
						articleList.add(article1);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return respMessage;
	}

	public static int getVoiceType(String vString) {
		vType = 0;
		if (vString.indexOf("��") != -1) {
			vType = 1;
		}
		if (vString.indexOf("���") != -1 || vString.indexOf("ȡ��") != -1) {
			vType = 2;
		}
		if (vString.indexOf("�α�") != -1) {
			vType = 1100;
		}

		if (vString.indexOf("�ɼ�") != -1) {
			vType = 1200;
		}

		if (vString.indexOf("����ɼ�") != -1) {
			vType = 1201;
		}
		if (vString.indexOf("ѧ��ɼ�") != -1) {
			vType = 1202;
		}
		if (vString.indexOf("��ѡ�γ�") != -1) {
			vType = 1300;
		}
		if (vString.indexOf("���Ҳ�ѯ") != -1) {
			vType = 1400;
		}
		if ("��Ϸ".equals(vString)) {
			vType = 15001;
		}
		if ("Ӧ��".equals(vString)) {
			vType = 15002;
		}
		if ("У��".equals(vString) || "��ĩ".equals(vString)
				|| "����".equals(vString) || "ʱ��".equals(vString)) {
			vType = 3100;
		}

		return vType;
	}

	public static String resMSG(int vType, String msgtype,
			NewsMessage newsMessage, String fromUserName, String toUserName,
			String respMessage, List<Article> articleList, String respContent,
			TextMessage textMessage) {
		// msgtype=msgtype;
		JSONObject json = null;
		switch (vType) {
		case 1:
			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 0) {
				respContent = "���Ѿ��󶨹��ˣ������ظ��󶨣�";

				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			} else {
				respMessage = AlertBining(newsMessage, articleList,
						fromUserName, toUserName);
			}
			json.clear();
			json = null;
			break;
		case 2:
			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 1) {
				respContent = "����δ�󶨹��������󶨣�";

				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			} else {
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				Article article0 = new Article();
				article0.setTitle("ȡ����");
				article0.setDescription("����ϵͳ�˺�������΢�źŽ��");
				// ��ͼƬ��Ϊ��
				article0.setPicUrl("");
				article0.setUrl(Config.URL
						+ "/hnnu_jwxt/bining/unbining.jsp?openID="
						+ fromUserName);

				articleList.add(article0);
				newsMessage.setArticleCount(articleList.size());
				newsMessage.setArticles(articleList);
				respMessage = MessageUtil.newsMessageToXml(newsMessage);
			}
			json.clear();
			json = null;
			break;

		case 1100:// �α�!

//
//			JSONObject json2 = PermissionManger.getPermissionInfo(
//					11, fromUserName);
//			switch (json2.getInt("error")) {
//			case 0:// ����
//					// Ȩ����ȷ
//					// ����ͼ����Ϣ
//				newsMessage.setToUserName(fromUserName);
//				newsMessage.setFromUserName(toUserName);
//				newsMessage.setCreateTime(new Date().getTime());
//				newsMessage
//						.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//				newsMessage.setFuncFlag(0);
//
//				String xh = "",
//				xm = "",
//				psd = "";
//
//				Connection conn = null;
//				ResultSet rs = null;
//				Statement stmt = null;
//				try {
//
//					StringBuffer sql = new StringBuffer();
//					sql.append("select * from userbining where openID='");
//					sql.append(fromUserName);
//					sql.append("'");
//
//					conn = DBPhnnujwc.getPool().getConnection();
//					stmt = conn.createStatement();
//					rs = stmt.executeQuery(sql.toString());
//
//					// rs = DBhnnujwc.getInstance().querySQL(
//					// sql.toString());
//					sql.delete(0, sql.length());// �ͷ�StringBuffer�ڴ�
//					while (rs.next()) {
//						xh = rs.getString("id");
//						xm = rs.getString("name");
//						psd = rs.getString("psd");
//					}
//					rs.close();
//				} catch (Exception e) {
//					xh = e.toString();
//				} finally {
//					try {
//						rs.close();
//					} catch (Exception e2) {
//						// TODO: handle exception
//					}
//					try {
//						stmt.close();
//					} catch (Exception e2) {
//						// TODO: handle exception
//					}
//					try {
//						conn.close();
//					} catch (Exception e2) {
//						// TODO: handle exception
//					}
//				}
//				Article article0 = new Article();
//				article0.setTitle("");
//				article0.setPicUrl(Config.URL
//						+ ":8080/kebiao/images/kb.png");
//				article0.setUrl("");
//				articleList.add(article0);
//
//				Article article1 = new Article();
//				article1.setTitle("���տα�");
//				article1.setDescription("");
//				article1.setPicUrl("");
//				article1.setUrl(Config.URL
//						+ ":8080/kebiao/kebiao_today.php?openID="
//						+ fromUserName);
//				articleList.add(article1);
//
//				Article article2 = new Article();
//				article2.setTitle("ѧ�ڿα�");
//				article2.setDescription("");
//				article2.setPicUrl("");
//				article2.setUrl(Config.URL
//						+ ":8080/kebiao/index.php?openID="
//						+ fromUserName);
//				articleList.add(article2);
//				// ����ͼ����Ϣ����
//				newsMessage.setArticleCount(articleList.size());
//				// ����ͼ����Ϣ������ͼ�ļ���
//				newsMessage.setArticles(articleList);
//				// ��ͼ����Ϣ����ת����xml�ַ���
//				respMessage = MessageUtil
//						.newsMessageToXml(newsMessage);
//
//				break;
//
//			case 1001:// δ��
//
//				respMessage = AlertBining(newsMessage, articleList,
//						fromUserName, toUserName);
//				break;
//			case 1000:// �˵��ر�
//				respContent = json.getString("errorinfo");
//				textMessage.setContent(respContent);
//				respMessage = MessageUtil
//						.textMessageToXml(textMessage);
//				break;
//
//			default:
//				/** ��������̨��־ */
//
//				respContent = "ϵͳ��æ��";
//				textMessage.setContent(respContent);
//				respMessage = MessageUtil
//						.textMessageToXml(textMessage);
//				break;
//			}

		

			break;

		case 1200:// �ɼ���||�ɼ���ѯ
			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 0) {
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				Article article0 = new Article();
				article0.setTitle("�ɼ���ѯ");
				article0.setDescription("");
				// ��ͼƬ��Ϊ��
				article0.setPicUrl("");
				article0.setUrl("");

				Article article1 = new Article();
				article1.setTitle("����ɼ�");
				article1.setDescription("");
				// ��ͼƬ��Ϊ��
				article1.setPicUrl("");
				article1.setUrl(Config.URL + "/hnnu_jwxt/cj/lncj.jsp?openID="
						+ fromUserName + "&update=1");

				Article article2 = new Article();
				article2.setTitle("ѧ��/ѧ�ڳɼ�");
				article2.setDescription("");
				article2.setPicUrl("");
				article2.setUrl(Config.URL + "/hnnu_jwxt/cj/selxq.jsp?openID="
						+ fromUserName + "&update=1");

				Article article4 = new Article();
				article4.setTitle("��߳ɼ�");
				article4.setDescription("");
				article4.setPicUrl("");
				article4.setUrl(Config.URL + "/hnnu_jwxt/cj/zgcj.jsp?openID="
						+ fromUserName + "&update=1");

				Article article5 = new Article();
				article5.setTitle("δͨ���ɼ�");
				article5.setDescription("");
				article5.setPicUrl("");
				article5.setUrl(Config.URL + "/hnnu_jwxt/cj/wtgcj.jsp?openID="
						+ fromUserName + "&update=1");

				Article article6 = new Article();
				article6.setTitle("�ɼ�ͳ��");
				article6.setDescription("");
				article6.setPicUrl("");
				article6.setUrl(Config.URL + "/hnnu_jwxt/cj/cjtj.jsp?openID="
						+ fromUserName);

				articleList.add(article0);
				articleList.add(article1);
				articleList.add(article2);
				// articleList.add(article3);
				articleList.add(article4);
				articleList.add(article5);
				articleList.add(article6);
				newsMessage.setArticleCount(articleList.size());
				newsMessage.setArticles(articleList);
				respMessage = MessageUtil.newsMessageToXml(newsMessage);

			} else {
				respMessage = AlertBining(newsMessage, articleList,
						fromUserName, toUserName);
			}
			json.clear();
			json = null;

			break;

		case 1201:// ����ɼ�!

			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 0) {
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				Article article1 = new Article();
				article1.setTitle("����ɼ�");
				article1.setDescription("");
				// ��ͼƬ��Ϊ��
				article1.setPicUrl("");
				article1.setUrl(Config.URL + "/hnnu_jwxt/cj/lncj.jsp?openID="
						+ fromUserName + "&update=1");

				articleList.add(article1);
				newsMessage.setArticleCount(articleList.size());
				newsMessage.setArticles(articleList);
				respMessage = MessageUtil.newsMessageToXml(newsMessage);

			} else {
				respMessage = AlertBining(newsMessage, articleList,
						fromUserName, toUserName);
			}
			json.clear();
			json = null;

			break;
		case 1202:// ѧ��/ѧ�ڳɼ�

			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 0) {
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				Article article2 = new Article();
				article2.setTitle("ѧ��/ѧ�ڳɼ�");
				article2.setDescription("");
				article2.setPicUrl("");
				article2.setUrl(Config.URL + "/hnnu_jwxt/cj/selxq.jsp?openID="
						+ fromUserName + "&update=1");

				articleList.add(article2);
				newsMessage.setArticleCount(articleList.size());
				newsMessage.setArticles(articleList);
				respMessage = MessageUtil.newsMessageToXml(newsMessage);

			} else {
				respMessage = AlertBining(newsMessage, articleList,
						fromUserName, toUserName);
			}
			json.clear();
			json = null;

			break;
		case 1300:

			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 0) {
				// ����ͼ����Ϣ
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				Article article = new Article();
				article.setTitle("��ѡ�γ�");
				article.setDescription("��ѡ��רҵѡ�ް�����ѡ��");
				article.setPicUrl("");
				article.setUrl(Config.URL
						+ "/hnnu_jwxt/XuanKeChaXun/XuanKeChaXun.jsp?openID="
						+ fromUserName);
				articleList.add(article);
				// ����ͼ����Ϣ����
				newsMessage.setArticleCount(articleList.size());
				// ����ͼ����Ϣ������ͼ�ļ���
				newsMessage.setArticles(articleList);
				// ��ͼ����Ϣ����ת����xml�ַ���
				respMessage = MessageUtil.newsMessageToXml(newsMessage);

			} else {
				respMessage = AlertBining(newsMessage, articleList,
						fromUserName, toUserName);
			}
			json.clear();
			json = null;

			break;
		case 1400:

			break;

		case 3100:
			// ����ͼ����Ϣ
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);
			Article article = new Article();
			article.setTitle("��ʦУ��");
			article.setDescription("����ʦ����ѧ2015-2016ѧ���У��");
			article.setPicUrl(Config.URL + "/rc/hnnu_jwc/a.jpg");
			article.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTM2MTQ0Nw==&mid=206652775&idx=1&sn=46ce4b080bfb42bf10345916691bd161#rd");
			articleList.add(article);
			// ����ͼ����Ϣ����
			newsMessage.setArticleCount(articleList.size());
			// ����ͼ����Ϣ������ͼ�ļ���
			newsMessage.setArticles(articleList);
			// ��ͼ����Ϣ����ת����xml�ַ���
			respMessage = MessageUtil.newsMessageToXml(newsMessage);

			break;

		default:
			if (msgtype.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				respContent = "������ؽ��������Ϣ����¼��\ue056";

			} else {
				respContent = "ľ�������Ŷ��\ue412";

			}
			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
			break;
		}
		return respMessage;
	}

	/** ��ʾ�� */
	private static String AlertBining(NewsMessage newsMessage,
			List<Article> articleList, String fromUserName, String toUserName) {
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setFuncFlag(0);

		Article article0 = new Article();
		article0.setTitle("��");
		article0.setDescription("��¼����ϵͳ��������΢�źŰ�");
		// ��ͼƬ��Ϊ��
		article0.setPicUrl("");
		article0.setUrl(Config.URL + "/hnnu_jwxt/bining/bining.jsp?openID="
				+ fromUserName);

		articleList.add(article0);
		newsMessage.setArticleCount(articleList.size());
		newsMessage.setArticles(articleList);

		return MessageUtil.newsMessageToXml(newsMessage);
	}
}
