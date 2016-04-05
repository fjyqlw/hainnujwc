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
	/** ��ȡȨ�޳���û�����eventKey */
	public final static int NO_EVENTKEY_ERR = 2005;

	public static String getDetail(int error) {
		String detail = "";
		switch (error) {
		case SUCCESS:
			detail = "����ɹ�";
			break;
		case SYSTEM_BUSY:
			detail = "ϵͳ��æ";
			break;
		case PROGRAM_ERROR:
			detail = "�����ڲ�����";
			break;
		case IN_PARAM_ERR:
			detail = "��δ���";
			break;
		case SQL_EXEC_ERR:
			detail = "sqlִ�д���";
			break;
		case MENU_CLOSE:
			detail = "�˵��ر�";
			break;
		case USER_UNBINING:
			detail = "�û�δ��";
			break;
		case NO_PERMISSION:
			detail = "û��Ȩ��";
			break;
		case SERVER_ERR:
			detail = "�������ڲ�����";
			break;
		case MSG_RECORD_ERR:
			detail = "��Ϣ��¼����д�����ݿ����";
			break;
		case NO_EVENTKEY_ERR:
			detail = "��ȡȨ�޳���û�����eventKey";
			break;
		default:
			break;
		}

		return detail;
	}

}
