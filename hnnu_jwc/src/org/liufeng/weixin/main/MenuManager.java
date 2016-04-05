package org.liufeng.weixin.main;

import org.liufeng.weixin.pojo.AccessToken;
//import org.liufeng.weixin.pojo.Button;
import org.liufeng.weixin.pojo.CommonButton;
import org.liufeng.weixin.pojo.ComplexButton;
import org.liufeng.weixin.pojo.Menu;
import org.liufeng.weixin.pojo.ViewButton;
import org.liufeng.weixin.util.WeixinUtil;

/**
 * �˵���������
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class MenuManager {
	//private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) {
		// �������û�Ψһƾ֤
		String appId = "wx0442dc2fb1a7841e";
		// �������û�Ψһƾ֤��Կ
		String appSecret = "5fbf334424c28e63dcedb1e5e3e44283";

		// ���ýӿڻ�ȡaccess_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		if (null != at) {
			// ���ýӿڴ����˵�
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// �жϲ˵��������
			if (0 == result)
				System.out.println("�˵������ɹ���");
			else
				System.out.println("�˵�����ʧ�ܣ������룺" + result);
		}
	}

	/**
	 * ��װ�˵�����
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("�ҵĿα�");
		btn11.setType("click");
		btn11.setKey("11");

		CommonButton btn12 = new CommonButton();
		btn12.setName("�ҵĳɼ�");
		btn12.setType("click");
		btn12.setKey("12");

		CommonButton btn13 = new CommonButton();
		btn13.setName("��ѡ�γ�");
		btn13.setType("click");
		btn13.setKey("13");

		CommonButton btn14 = new CommonButton();
		btn14.setName("���Ҳ�ѯ");
		btn14.setType("click");
		btn14.setKey("14");
		
		CommonButton btn15 = new CommonButton();
		btn15.setName("���๦��");
		btn15.setType("click");
		btn15.setKey("15");

		CommonButton btn21 = new CommonButton();
		btn21.setName("������Ϣ");
		btn21.setType("click");
		btn21.setKey("21");

		CommonButton btn22 = new CommonButton();
		btn22.setName("�ҵĿα�");
		btn22.setType("click");
		btn22.setKey("22");
		
		CommonButton btn23 = new CommonButton();
		btn23.setName("���Ҳ�ѯ");
		btn23.setType("click");
		btn23.setKey("23");


		CommonButton btn24 = new CommonButton();
		btn24.setName("");
		btn24.setType("view");
		btn24.setUrl("http://www.hainnujwc.com/hnnu_jwxt/signin/sign.jsp");
//
//		CommonButton btn25 = new CommonButton();
//		btn25.setName("�������");
//		btn25.setType("click");
//		btn25.setKey("25");

		CommonButton btn31 = new CommonButton();
		btn31.setName("���ŵ绰");
		btn31.setType("view");
		btn31.setUrl("http://www.hainnujwc.com:8080/kebiao/phone.php");

		CommonButton btn32 = new CommonButton();
		btn32.setName("��Ϣʱ��");
		btn32.setType("view");
//		btn32.setKey("32");
		btn32.setUrl("http://www.hainnujwc.com/hnnu_jwxt/publicServer/zxsj.html");
		
		CommonButton btn33 = new CommonButton();
		btn33.setName("��ѧ����");
		btn33.setType("view");
		btn33.setUrl("http://www.hainnujwc.com/hnnu_jwxt/publicServer/jxfk.jsp");
		
		ViewButton btn34 = new ViewButton();
		btn34.setName("��ʦУ��");//�Ƽ�Ӧ��
		btn34.setType("view");
		btn34.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTM2MTQ0Nw==&mid=206652775&idx=1&sn=46ce4b080bfb42bf10345916691bd161#rd");
		//http://www.hainnujwc.com/hnnu_jwxt/app/app.html
		
		CommonButton btn35 = new CommonButton();
		btn35.setName("���๦��");
		btn35.setType("click");
		btn35.setKey("35");

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("ѧ��ר��");
		mainBtn1.setSub_button(new ViewButton[] { btn11, btn12, btn13, btn14, btn15 });//

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("��ʦר��");
		mainBtn2.setSub_button(new ViewButton[] { btn21, btn22,  btn23});//, btn23, btn24, btn25

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("��������");
		mainBtn3.setSub_button(new ViewButton[] { btn31, btn32, btn33, btn34, btn35 });

	
		Menu menu = new Menu();
		menu.setButton(new ViewButton[] { mainBtn1, mainBtn2, mainBtn3 });

		return menu;
	}
}