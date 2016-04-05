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
 * 核心服务类
 * 
 * @author 练威
 * @date 2016-03-20
 * @version 1.1
 */
public class CoreService {

	// private Bining bin = new Bining();
	/**
	 * 语音类别代码 1 绑定; 2 解绑; 1100 课表; 1200 成绩; 1201 历年成绩; 1202 学年成绩; 1300 已选课程;
	 * 1400 教室查询; 15001 游戏; 15002 应用; 3100 校历&考试时间;
	 */
	private static int vType = 0;
	

	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @return
	 */
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		try {
			NewsMessage newsMessage = new NewsMessage();
			List<Article> articleList = new ArrayList<Article>();
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			// 回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);

			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

				String ContenText = requestMap.get("Content");

				/** 记录消息 */
                try {
					RequestRESService rrs=new RequestRESService();
					RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
					JSONObject inJson=new JSONObject();
					/**方法名称*/
					inJson.put(ERROR_KEY.METHOD, Methods.msgRecord);
					/**构造参数*/
					JSONObject paramJson=new JSONObject();
					paramJson.put("openID", fromUserName);
					paramJson.put("content", ContenText);
					inJson.put(ERROR_KEY.PARAMTER, paramJson);
					/**发起接口请求*/
					rrd.requestRES(inJson.toString());
					
				} catch (Exception e) {
					System.out.println("接口模块出错"+e.toString());
				}
				// respContent = "好消息！可以查成绩啦！验证登录后可以查看历年成绩和学期/学年成绩。";
				// textMessage.setContent(respContent);
				// respMessage = MessageUtil.textMessageToXml(textMessage);
				/** 语音识别方法，这里作为过滤关键字用，返回关键字类型 */
				int vType = getVoiceType(ContenText);
				return resMSG(vType, msgType, newsMessage, fromUserName,
						toUserName, respMessage, articleList, respContent,
						textMessage);
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 音频消息;
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {

				respContent = requestMap.get("Recognition");

				int vType = getVoiceType(respContent);

				return resMSG(vType, msgType, newsMessage, fromUserName,
						toUserName, respMessage, articleList, respContent,
						textMessage);

			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respContent = "终于等到你！海师大教务处欢迎您！\ue056";
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
					Connection conn = null;
					ResultSet rs = null;
					Statement stmt = null;
					try {
						//JSONObject json = Bining.getBiningInfo(fromUserName);
						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.biningInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						

						String sql = "", sql2 = "", Id = "";
						switch (outJson.getInt("error")) {
						case 0:// 已绑定
							switch (outJson.getInt("role")) {
							case 0:// 学生
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
									/** 删除成绩表 */
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
									/** 删除课表 */
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
							case 1:// 教师
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
								/** 删除课表 */
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
						case 1:// 未绑定
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
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
					if (eventKey.equals("11")) {

						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// 正常(菜单正常、已绑定、权限正确)
								// 权限正确
								// 创建图文消息
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
								sql.delete(0, sql.length());// 释放StringBuffer内存
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
							article1.setTitle("今日课表");
							article1.setDescription("");
							article1.setPicUrl("");
							article1.setUrl(Config.URL
									+ ":8080/kebiao/kebiao_today.php?openID="
									+ fromUserName);
							articleList.add(article1);

							Article article2 = new Article();
							article2.setTitle("学期课表");
							article2.setDescription("");
							article2.setPicUrl("");
							article2.setUrl(Config.URL
									+ ":8080/kebiao/index.php?openID="
									+ fromUserName);
							articleList.add(article2);
							// 设置图文消息个数
							newsMessage.setArticleCount(articleList.size());
							// 设置图文消息包含的图文集合
							newsMessage.setArticles(articleList);
							// 将图文消息对象转换成xml字符串
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// 未绑定

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// 菜单关闭
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "您没有权限！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** 反馈到后台日志 */
                            System.out.println(outJson);
							respContent = "系统繁忙！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("12")) {

						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// 正常(菜单正常、已绑定、权限正确)
								// 权限正确
								// 创建图文消息
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);

							Article article0 = new Article();
							article0.setTitle("成绩查询");
							article0.setDescription("");
							// 将图片置为空
							article0.setPicUrl("");
							article0.setUrl("");

							Article article1 = new Article();
							article1.setTitle("历年成绩");
							article1.setDescription("");
							// 将图片置为空
							article1.setPicUrl("");
							article1.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/lncj.jsp?openID="
									+ fromUserName + "&update=1");

							Article article2 = new Article();
							article2.setTitle("学年/学期成绩");
							article2.setDescription("");
							article2.setPicUrl("");
							article2.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/selxq.jsp?openID="
									+ fromUserName + "&update=1");

							Article article4 = new Article();
							article4.setTitle("最高成绩");
							article4.setDescription("");
							article4.setPicUrl("");
							article4.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/zgcj.jsp?openID="
									+ fromUserName + "&update=1");

							Article article5 = new Article();
							article5.setTitle("未通过成绩");
							article5.setDescription("");
							article5.setPicUrl("");
							article5.setUrl(Config.URL
									+ "/hnnu_jwxt/cj/wtgcj.jsp?openID="
									+ fromUserName + "&update=0");

							Article article6 = new Article();
							article6.setTitle("成绩统计");
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

						case ERROR_INFO.USER_UNBINING:// 未绑定

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// 菜单关闭
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "您没有权限！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** 反馈到后台日志 */
                            System.out.println(outJson);
							respContent = "系统繁忙！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("13")) {

						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// 正常(菜单正常、已绑定、权限正确)
								// 权限正确
								// 创建图文消息
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);
							Article article = new Article();
							article.setTitle("已选课程");
							article.setDescription("已选的专业选修包括公选课");
							article.setPicUrl("");
							article.setUrl(Config.URL
									+ "/hnnu_jwxt/XuanKeChaXun/XuanKeChaXun.jsp?openID="
									+ fromUserName);
							articleList.add(article);
							// 设置图文消息个数
							newsMessage.setArticleCount(articleList.size());
							// 设置图文消息包含的图文集合
							newsMessage.setArticles(articleList);
							// 将图文消息对象转换成xml字符串
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// 未绑定

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// 菜单关闭
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "您没有权限！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** 反馈到后台日志 */
                            System.out.println(outJson);
							respContent = "系统繁忙！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("14")) {


						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// 正常(菜单正常、已绑定、权限正确)
								// 权限正确
								// 创建图文消息
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);
							Article article = new Article();
							article.setTitle("教室查询");
							article.setDescription("查询可用空教室信息");
							article.setPicUrl("");
							article.setUrl(Config.URL
									+ "/hnnu_jwxt/EmptyClassRooms/selxq.jsp?openID="
									+ fromUserName);
							articleList.add(article);
							// 设置图文消息个数
							newsMessage.setArticleCount(articleList.size());
							// 设置图文消息包含的图文集合
							newsMessage.setArticles(articleList);
							// 将图文消息对象转换成xml字符串
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// 未绑定

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// 菜单关闭
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "您没有权限！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** 反馈到后台日志 */
                            System.out.println(outJson);
							respContent = "系统繁忙！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("15")) {

						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// 正常(菜单正常、已绑定、权限正确)
								// 权限正确
								// 创建图文消息
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);
							Article article1 = new Article();
							article1.setTitle("娱乐游戏");
							article1.setDescription("");
							// 将图片置为空
							article1.setPicUrl("");
							article1.setUrl(Config.URL + "/games/index.html");

							articleList.add(article1);
							newsMessage.setArticleCount(articleList.size());
							newsMessage.setArticles(articleList);
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// 未绑定

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// 菜单关闭
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "您没有权限！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** 反馈到后台日志 */
                            System.out.println(outJson);
							respContent = "系统繁忙！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("21")) {

						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// 正常(菜单正常、已绑定、权限正确)
								// 权限正确
								// 创建图文消息
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);

							Article article1 = new Article();
							article1.setTitle("个人信息");
							article1.setDescription("");
							// 将图片置为空
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

						case ERROR_INFO.USER_UNBINING:// 未绑定

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// 菜单关闭
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;

                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "您没有权限！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** 反馈到后台日志 */
                            System.out.println(outJson);
							respContent = "系统繁忙！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("22")) {

						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// 正常(菜单正常、已绑定、权限正确)
								// 权限正确
								// 创建图文消息
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
							article1.setTitle("今日课表");
							article1.setDescription("");
							article1.setPicUrl("");
							article1.setUrl(Config.URL
									+ ":8080/kebiao/teacher_todaykebiao.php?openID="
									+ fromUserName);
							articleList.add(article1);

							Article article2 = new Article();
							article2.setTitle("学期课表");
							article2.setDescription("");
							article2.setPicUrl("");
							article2.setUrl(Config.URL
									+ ":8080/kebiao/teacher_kebiao.php?openID="
									+ fromUserName);
							articleList.add(article2);
							// 设置图文消息个数
							newsMessage.setArticleCount(articleList.size());
							// 设置图文消息包含的图文集合
							newsMessage.setArticles(articleList);
							// 将图文消息对象转换成xml字符串
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// 未绑定

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// 菜单关闭
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "您没有权限！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;

						default:
							/** 反馈到后台日志 */
                            System.out.println(outJson);
							respContent = "系统繁忙！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("23")) {

						JSONObject outJson = null;
						/**获取用户绑定信息*/
						try {
							RequestRESService rrs=new RequestRESService();
							RequestRESDelegate rrd=rrs.getPort(RequestRESDelegate.class);
							JSONObject inJson=new JSONObject();
							/**方法名称*/
							inJson.put(ERROR_KEY.METHOD, Methods.permissionInfo);
							/**构造参数*/
							JSONObject paramJson=new JSONObject();
							paramJson.put("eventKey", eventKey);
							paramJson.put("openID", fromUserName);
							inJson.put(ERROR_KEY.PARAMTER, paramJson);
							/**发起接口请求*/
							outJson=JSONObject.fromObject(rrd.requestRES(inJson.toString()));
							System.out.println(outJson);
						} catch (Exception e) {
							System.out.println("接口模块出错"+e.toString());
							throw e;
						}
						
						
						switch (outJson.getInt(ERROR_KEY.ERROR)) {
						case ERROR_INFO.SUCCESS:// 正常(菜单正常、已绑定、权限正确)
								// 权限正确
								// 创建图文消息
							newsMessage.setToUserName(fromUserName);
							newsMessage.setFromUserName(toUserName);
							newsMessage.setCreateTime(new Date().getTime());
							newsMessage
									.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
							newsMessage.setFuncFlag(0);
							Article article = new Article();
							article.setTitle("教室查询");
							article.setDescription("查询可用空教室信息");
							article.setPicUrl("");
							article.setUrl(Config.URL
									+ "/hnnu_jwxt/EmptyClassRoomsTch/selxq.jsp?openID="
									+ fromUserName);
							articleList.add(article);
							// 设置图文消息个数
							newsMessage.setArticleCount(articleList.size());
							// 设置图文消息包含的图文集合
							newsMessage.setArticles(articleList);
							// 将图文消息对象转换成xml字符串
							respMessage = MessageUtil
									.newsMessageToXml(newsMessage);

							break;

						case ERROR_INFO.USER_UNBINING:// 未绑定

							respMessage = AlertBining(newsMessage, articleList,
									fromUserName, toUserName);
							break;
						case ERROR_INFO.MENU_CLOSE:// 菜单关闭
							respContent = outJson.getJSONObject(ERROR_KEY.DATA).getString("message");
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
                        case ERROR_INFO.NO_PERMISSION:
                        	respContent = "您没有权限！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
                        	break;
						default:
							/** 反馈到后台日志 */
                            System.out.println(outJson);
							respContent = "系统繁忙！";
							textMessage.setContent(respContent);
							respMessage = MessageUtil
									.textMessageToXml(textMessage);
							break;
						}

					} else if (eventKey.equals("24")) {
						respContent = "开发中。。。！";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("25")) {
						respContent = "开发中。。。";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("31")) {
						// 创建图文消息
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage
								.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
						Article article = new Article();
						article.setTitle("海师校历");
						article.setDescription("海南师范大学2015-2016学年度校历");
						article.setPicUrl(Config.URL + "/rc/hnnu_jwc/a.jpg");
						article.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTM2MTQ0Nw==&mid=206652775&idx=1&sn=46ce4b080bfb42bf10345916691bd161#rd");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
					} else if (eventKey.equals("32")) {
						newsMessage.setToUserName(fromUserName);
						newsMessage.setFromUserName(toUserName);
						newsMessage.setCreateTime(new Date().getTime());
						newsMessage
								.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
						newsMessage.setFuncFlag(0);
						Article article1 = new Article();
						article1.setTitle("作息时间");
						article1.setDescription("海南师范大学作息时间表");
						// 将图片置为空
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
						article1.setTitle("教学反馈");
						article1.setDescription("教学反馈");
						// 将图片置为空
						article1.setPicUrl("");
						article1.setUrl(Config.URL
								+ "/hnnu_jwxt/publicServer/jxfk.jsp");

						articleList.add(article1);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
					} else if (eventKey.equals("34")) {
						respContent = "打开APP";
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
						article0.setTitle("【公共服务】-更多功能");
						article0.setDescription("");
						// 将图片置为空
						article0.setPicUrl("");
						article0.setUrl("");

						Article article1 = new Article();
						article1.setTitle("取消绑定");
						article1.setDescription("");
						// 将图片置为空
						article1.setPicUrl("");
						article1.setUrl(Config.URL
								+ "/hnnu_jwxt/bining/unbining.jsp?openID="
								+ fromUserName);

						Article article2 = new Article();
						article2.setTitle("办事指南");
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
		if (vString.indexOf("绑定") != -1) {
			vType = 1;
		}
		if (vString.indexOf("解绑") != -1 || vString.indexOf("取消") != -1) {
			vType = 2;
		}
		if (vString.indexOf("课表") != -1) {
			vType = 1100;
		}

		if (vString.indexOf("成绩") != -1) {
			vType = 1200;
		}

		if (vString.indexOf("历年成绩") != -1) {
			vType = 1201;
		}
		if (vString.indexOf("学年成绩") != -1) {
			vType = 1202;
		}
		if (vString.indexOf("已选课程") != -1) {
			vType = 1300;
		}
		if (vString.indexOf("教室查询") != -1) {
			vType = 1400;
		}
		if ("游戏".equals(vString)) {
			vType = 15001;
		}
		if ("应用".equals(vString)) {
			vType = 15002;
		}
		if ("校历".equals(vString) || "期末".equals(vString)
				|| "考试".equals(vString) || "时间".equals(vString)) {
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
				respContent = "您已经绑定过了，请勿重复绑定！";

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
				respContent = "您还未绑定过，无需解绑定！";

				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			} else {
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				Article article0 = new Article();
				article0.setTitle("取消绑定");
				article0.setDescription("教务系统账号与您的微信号解绑定");
				// 将图片置为空
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

		case 1100:// 课表!

//
//			JSONObject json2 = PermissionManger.getPermissionInfo(
//					11, fromUserName);
//			switch (json2.getInt("error")) {
//			case 0:// 正常
//					// 权限正确
//					// 创建图文消息
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
//					sql.delete(0, sql.length());// 释放StringBuffer内存
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
//				article1.setTitle("今日课表");
//				article1.setDescription("");
//				article1.setPicUrl("");
//				article1.setUrl(Config.URL
//						+ ":8080/kebiao/kebiao_today.php?openID="
//						+ fromUserName);
//				articleList.add(article1);
//
//				Article article2 = new Article();
//				article2.setTitle("学期课表");
//				article2.setDescription("");
//				article2.setPicUrl("");
//				article2.setUrl(Config.URL
//						+ ":8080/kebiao/index.php?openID="
//						+ fromUserName);
//				articleList.add(article2);
//				// 设置图文消息个数
//				newsMessage.setArticleCount(articleList.size());
//				// 设置图文消息包含的图文集合
//				newsMessage.setArticles(articleList);
//				// 将图文消息对象转换成xml字符串
//				respMessage = MessageUtil
//						.newsMessageToXml(newsMessage);
//
//				break;
//
//			case 1001:// 未绑定
//
//				respMessage = AlertBining(newsMessage, articleList,
//						fromUserName, toUserName);
//				break;
//			case 1000:// 菜单关闭
//				respContent = json.getString("errorinfo");
//				textMessage.setContent(respContent);
//				respMessage = MessageUtil
//						.textMessageToXml(textMessage);
//				break;
//
//			default:
//				/** 反馈到后台日志 */
//
//				respContent = "系统繁忙！";
//				textMessage.setContent(respContent);
//				respMessage = MessageUtil
//						.textMessageToXml(textMessage);
//				break;
//			}

		

			break;

		case 1200:// 成绩！||成绩查询
			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 0) {
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				Article article0 = new Article();
				article0.setTitle("成绩查询");
				article0.setDescription("");
				// 将图片置为空
				article0.setPicUrl("");
				article0.setUrl("");

				Article article1 = new Article();
				article1.setTitle("历年成绩");
				article1.setDescription("");
				// 将图片置为空
				article1.setPicUrl("");
				article1.setUrl(Config.URL + "/hnnu_jwxt/cj/lncj.jsp?openID="
						+ fromUserName + "&update=1");

				Article article2 = new Article();
				article2.setTitle("学年/学期成绩");
				article2.setDescription("");
				article2.setPicUrl("");
				article2.setUrl(Config.URL + "/hnnu_jwxt/cj/selxq.jsp?openID="
						+ fromUserName + "&update=1");

				Article article4 = new Article();
				article4.setTitle("最高成绩");
				article4.setDescription("");
				article4.setPicUrl("");
				article4.setUrl(Config.URL + "/hnnu_jwxt/cj/zgcj.jsp?openID="
						+ fromUserName + "&update=1");

				Article article5 = new Article();
				article5.setTitle("未通过成绩");
				article5.setDescription("");
				article5.setPicUrl("");
				article5.setUrl(Config.URL + "/hnnu_jwxt/cj/wtgcj.jsp?openID="
						+ fromUserName + "&update=1");

				Article article6 = new Article();
				article6.setTitle("成绩统计");
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

		case 1201:// 历年成绩!

			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 0) {
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				Article article1 = new Article();
				article1.setTitle("历年成绩");
				article1.setDescription("");
				// 将图片置为空
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
		case 1202:// 学年/学期成绩

			json = Bining.getBiningInfo(fromUserName);

			if (json.getInt("error") == 0) {
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				Article article2 = new Article();
				article2.setTitle("学年/学期成绩");
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
				// 创建图文消息
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				Article article = new Article();
				article.setTitle("已选课程");
				article.setDescription("已选的专业选修包括公选课");
				article.setPicUrl("");
				article.setUrl(Config.URL
						+ "/hnnu_jwxt/XuanKeChaXun/XuanKeChaXun.jsp?openID="
						+ fromUserName);
				articleList.add(article);
				// 设置图文消息个数
				newsMessage.setArticleCount(articleList.size());
				// 设置图文消息包含的图文集合
				newsMessage.setArticles(articleList);
				// 将图文消息对象转换成xml字符串
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
			// 创建图文消息
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);
			Article article = new Article();
			article.setTitle("海师校历");
			article.setDescription("海南师范大学2015-2016学年度校历");
			article.setPicUrl(Config.URL + "/rc/hnnu_jwc/a.jpg");
			article.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTM2MTQ0Nw==&mid=206652775&idx=1&sn=46ce4b080bfb42bf10345916691bd161#rd");
			articleList.add(article);
			// 设置图文消息个数
			newsMessage.setArticleCount(articleList.size());
			// 设置图文消息包含的图文集合
			newsMessage.setArticles(articleList);
			// 将图文消息对象转换成xml字符串
			respMessage = MessageUtil.newsMessageToXml(newsMessage);

			break;

		default:
			if (msgtype.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				respContent = "暂无相关解答，您的消息已收录！\ue056";

			} else {
				respContent = "木有听清楚哦！\ue412";

			}
			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
			break;
		}
		return respMessage;
	}

	/** 提示绑定 */
	private static String AlertBining(NewsMessage newsMessage,
			List<Article> articleList, String fromUserName, String toUserName) {
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		newsMessage.setFuncFlag(0);

		Article article0 = new Article();
		article0.setTitle("绑定");
		article0.setDescription("登录教务系统并与您的微信号绑定");
		// 将图片置为空
		article0.setPicUrl("");
		article0.setUrl(Config.URL + "/hnnu_jwxt/bining/bining.jsp?openID="
				+ fromUserName);

		articleList.add(article0);
		newsMessage.setArticleCount(articleList.size());
		newsMessage.setArticles(articleList);

		return MessageUtil.newsMessageToXml(newsMessage);
	}
}
