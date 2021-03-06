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

}
