package com.lw.errorinfo;

/**
 * error info: =========================================== ��λ�� ��Ŀ
 * ------------------------------- 1 hsresws/hsreswsbak 2 hnnu_jwc 3 hnnu_jwxt 4
 * GroupSentMSG 5 ZXMicroCampus
 */

public class ERROR_INFO {
	/**
	 * ͨ�÷�����
	 * */
	/** �ɹ� */
	public final static int SUCCESS = 0;
	/** ϵͳ��æ */
	public final static int SYSTEM_BUSY = 1;
	/** �����ڲ����� */
	public final static int PROGRAM_ERROR = 2;

	/**
	 * ������ ˵�� -----hsresws/hsreswsbak----
	 * ----------------------------------------
	 * 
	 * */

	/** ��δ��� */
	public final static int IN_PARAM_ERR = 1001;
	/** sqlִ�д��� */
	public final static int SQL_EXEC_ERR = 1002;
	
	
	
	/** �˵��ر� */
	public final static int MENU_CLOSE = 2000;
	/** �û�δ�� */
	public final static int USER_UNBINING = 2001;
	/** û��Ȩ�� */
	public final static int NO_PERMISSION = 2002;
	/** �������ڲ����� */
	public final static int SERVER_ERR = 2003;
	/** ��Ϣ��¼����д�����ݿ���� */
	public final static int MSG_RECORD_ERR = 2004;

}
