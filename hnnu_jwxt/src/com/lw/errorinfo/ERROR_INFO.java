package com.lw.errorinfo;

/**
 * error info: =========================================== 首位数 项目
 * ------------------------------- 1 hsresws/hsreswsbak 2 hnnu_jwc 3 hnnu_jwxt 4
 * GroupSentMSG 5 ZXMicroCampus
 */

public class ERROR_INFO {
	/**
	 * 通用返回码
	 * */
	/** 成功 */
	public final static int SUCCESS = 0;
	/** 系统繁忙 */
	public final static int SYSTEM_BUSY = 1;
	/** 程序内部错误 */
	public final static int PROGRAM_ERROR = 2;

	/**
	 * 返回码 说明 -----hsresws/hsreswsbak----
	 * ----------------------------------------
	 * 
	 * */

	/** 入参错误 */
	public final static int IN_PARAM_ERR = 1001;
	/** sql执行错误 */
	public final static int SQL_EXEC_ERR = 1002;

	/** 菜单关闭 */
	public final static int MENU_CLOSE = 2000;
	/** 用户未绑定 */
	public final static int USER_UNBINING = 2001;
	/** 没有权限 */
	public final static int NO_PERMISSION = 2002;
	/** 服务器内部错误 */
	public final static int SERVER_ERR = 2003;
	/** 消息记录出错：写入数据库出错 */
	public final static int MSG_RECORD_ERR = 2004;
	/** 获取权限出错：没有相关eventKey */
	public final static int NO_EVENTKEY_ERR = 2005;

	public static String getDetail(int error) {
		String detail = "";
		switch (error) {
		case SUCCESS:
			detail = "请求成功";
			break;
		case SYSTEM_BUSY:
			detail = "系统繁忙";
			break;
		case PROGRAM_ERROR:
			detail = "程序内部错误";
			break;
		case IN_PARAM_ERR:
			detail = "入参错误";
			break;
		case SQL_EXEC_ERR:
			detail = "sql执行错误";
			break;
		case MENU_CLOSE:
			detail = "菜单关闭";
			break;
		case USER_UNBINING:
			detail = "用户未绑定";
			break;
		case NO_PERMISSION:
			detail = "没有权限";
			break;
		case SERVER_ERR:
			detail = "服务器内部错误";
			break;
		case MSG_RECORD_ERR:
			detail = "消息记录出错：写入数据库出错";
			break;
		case NO_EVENTKEY_ERR:
			detail = "获取权限出错：没有相关eventKey";
			break;
		default:
			break;
		}

		return detail;
	}

}
