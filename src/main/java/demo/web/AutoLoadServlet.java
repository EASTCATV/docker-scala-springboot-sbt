package demo.web;

import demo.com.unionpay.acp.sdk.SDKConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;



public class AutoLoadServlet extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		SDKConfig.getConfig().loadPropertiesFromSrc();// 从classpath加载acp_sdk.properties文件
		super.init();
	}
}
