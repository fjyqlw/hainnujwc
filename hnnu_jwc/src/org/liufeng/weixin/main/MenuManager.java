package org.liufeng.weixin.main;

import org.liufeng.weixin.pojo.AccessToken;
//import org.liufeng.weixin.pojo.Button;
import org.liufeng.weixin.pojo.CommonButton;
import org.liufeng.weixin.pojo.ComplexButton;
import org.liufeng.weixin.pojo.Menu;
import org.liufeng.weixin.pojo.ViewButton;
import org.liufeng.weixin.util.WeixinUtil;

/**
 * 菜单管理器类
 * 
 * @author liufeng
 * @date 2013-08-08
 */
public class MenuManager {
	//private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	public static void main(String[] args) {
		// 第三方用户唯一凭证
		String appId = "wx0442dc2fb1a7841e";
		// 第三方用户唯一凭证密钥
		String appSecret = "5fbf334424c28e63dcedb1e5e3e44283";

		// 调用接口获取access_token
		AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);

		if (null != at) {
			// 调用接口创建菜单
			int result = WeixinUtil.createMenu(getMenu(), at.getToken());

			// 判断菜单创建结果
			if (0 == result)
				System.out.println("菜单创建成功！");
			else
				System.out.println("菜单创建失败，错误码：" + result);
		}
	}

	/**
	 * 组装菜单数据
	 * 
	 * @return
	 */
	private static Menu getMenu() {
		CommonButton btn11 = new CommonButton();
		btn11.setName("我的课表");
		btn11.setType("click");
		btn11.setKey("11");

		CommonButton btn12 = new CommonButton();
		btn12.setName("我的成绩");
		btn12.setType("click");
		btn12.setKey("12");

		CommonButton btn13 = new CommonButton();
		btn13.setName("已选课程");
		btn13.setType("click");
		btn13.setKey("13");

		CommonButton btn14 = new CommonButton();
		btn14.setName("教室查询");
		btn14.setType("click");
		btn14.setKey("14");
		
		CommonButton btn15 = new CommonButton();
		btn15.setName("更多功能");
		btn15.setType("click");
		btn15.setKey("15");

		CommonButton btn21 = new CommonButton();
		btn21.setName("个人信息");
		btn21.setType("click");
		btn21.setKey("21");

		CommonButton btn22 = new CommonButton();
		btn22.setName("我的课表");
		btn22.setType("click");
		btn22.setKey("22");
		
		CommonButton btn23 = new CommonButton();
		btn23.setName("教室查询");
		btn23.setType("click");
		btn23.setKey("23");


		CommonButton btn24 = new CommonButton();
		btn24.setName("");
		btn24.setType("view");
		btn24.setUrl("http://www.hainnujwc.com/hnnu_jwxt/signin/sign.jsp");
//
//		CommonButton btn25 = new CommonButton();
//		btn25.setName("聊天唠嗑");
//		btn25.setType("click");
//		btn25.setKey("25");

		CommonButton btn31 = new CommonButton();
		btn31.setName("部门电话");
		btn31.setType("view");
		btn31.setUrl("http://www.hainnujwc.com:8080/kebiao/phone.php");

		CommonButton btn32 = new CommonButton();
		btn32.setName("作息时间");
		btn32.setType("view");
//		btn32.setKey("32");
		btn32.setUrl("http://www.hainnujwc.com/hnnu_jwxt/publicServer/zxsj.html");
		
		CommonButton btn33 = new CommonButton();
		btn33.setName("教学反馈");
		btn33.setType("view");
		btn33.setUrl("http://www.hainnujwc.com/hnnu_jwxt/publicServer/jxfk.jsp");
		
		ViewButton btn34 = new ViewButton();
		btn34.setName("海师校历");//推荐应用
		btn34.setType("view");
		btn34.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTM2MTQ0Nw==&mid=206652775&idx=1&sn=46ce4b080bfb42bf10345916691bd161#rd");
		//http://www.hainnujwc.com/hnnu_jwxt/app/app.html
		
		CommonButton btn35 = new CommonButton();
		btn35.setName("更多功能");
		btn35.setType("click");
		btn35.setKey("35");

		ComplexButton mainBtn1 = new ComplexButton();
		mainBtn1.setName("学生专区");
		mainBtn1.setSub_button(new ViewButton[] { btn11, btn12, btn13, btn14, btn15 });//

		ComplexButton mainBtn2 = new ComplexButton();
		mainBtn2.setName("教师专区");
		mainBtn2.setSub_button(new ViewButton[] { btn21, btn22,  btn23});//, btn23, btn24, btn25

		ComplexButton mainBtn3 = new ComplexButton();
		mainBtn3.setName("公共服务");
		mainBtn3.setSub_button(new ViewButton[] { btn31, btn32, btn33, btn34, btn35 });

	
		Menu menu = new Menu();
		menu.setButton(new ViewButton[] { mainBtn1, mainBtn2, mainBtn3 });

		return menu;
	}
}