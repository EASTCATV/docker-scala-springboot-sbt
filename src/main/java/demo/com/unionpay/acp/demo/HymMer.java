package demo.com.unionpay.acp.demo;

import demo.com.unionpay.acp.sdk.AcpService;
import demo.com.unionpay.acp.sdk.LogUtil;
import demo.com.unionpay.acp.sdk.SDKConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class HymMer
 */
public class HymMer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HymMer() {
        super();
    }

    final String encoding = "UTF-8";
	final String ACTION_FILL_ORDER = "fillOrder";
	final String ACTION_PUSH_ORDER = "pushOrder";
	final String ACTION_GET_AUTH = "getUserAuth";
	
	final String GET_USR_AUTH_URL = "https://qr.95516.com/qrcGtwWeb-web/api/userAuth?version=1.0.0&redirectUrl=";
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		response.getWriter().append("getRequestURI: ").append(request.getRequestURI());

		String urlPrefix = request.getRequestURL().toString() + "?action=";
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		String ua = request.getHeader("user-agent");
		String uaReg = ".*(UnionPay/[^ ]+ ([0-9a-zA-Z]+)).*";
		
		String action = request.getParameter("action");
//		System.out.println(action);

		if(ua == null){
			forwordErr("非银联云闪付的user-agent。", request, response);
			return;
		}
		Pattern p = Pattern.compile(uaReg);
		Matcher m = p.matcher(ua);
		if(!m.matches()){
			forwordErr("非银联云闪付的user-agent。<br>ua为：<br>" + ua, request, response);
			return;
		}
		String appUpIdentifier = m.group(1);
//		System.out.println(appUpIdentifier);
		if (action == null) {
			//0. 填写订单信息。
			request.getRequestDispatcher("pages/hym/fillOrderMer.jsp").forward(request, response);
			return;
		} else if (ACTION_FILL_ORDER.equals(action)) {
			//1. 调获取用户授权。
			Map<String, String> params = getAllRequestParam(request);
			params.remove("action");
			String redirectUrl = urlPrefix + ACTION_GET_AUTH + "&" + getQueryString(params);
			response.sendRedirect( GET_USR_AUTH_URL + URLEncoder.encode(redirectUrl, "utf-8"));
//			System.out.println(GET_USR_AUTH_URL + URLEncoder.encode(redirectUrl, "utf-8"));
			return;
		} else if(ACTION_GET_AUTH.equals(action)){ 
			//2. 获取用户授权返回处理，没问题（00、34）的话，调获取用户标识接口，成功的话显示订单信息
			String userAuthCode = request.getParameter("userAuthCode");
			String respCode = request.getParameter("respCode");
			String merId = request.getParameter("merId");

			if(!"00".equals(respCode) && !"34".equals(respCode)){
				forwordErr("获取用户信息失败。respCode=" + respCode, request, response);
				return;
			}

			String appUserId = null;
			if(userAuthCode != null){
				//3. 调获取用户标识接口
				StringBuffer errMsgBuffer = new StringBuffer();
				appUserId = getAppUsrId(merId, userAuthCode, appUpIdentifier, errMsgBuffer);
				if (appUserId == null) {
					forwordErr("调用获取用户标识接口失败，失败信息：" + errMsgBuffer.toString(), request, response);
					return;
				}
				request.setAttribute("appUserId", appUserId);
				request.setAttribute("showUserId", appUserId);
			} else {
				request.setAttribute("showUserId", "游客");
			}
			
			Map<String, String> params = getAllRequestParam(request);
			for ( String key : params.keySet() ) {
				request.setAttribute(key,params.get(key));
			}
			params.remove("action");
			params.put("action", ACTION_PUSH_ORDER);
			if(appUserId != null) params.put("appUserId", appUserId);
			String paramString = getQueryString(params);
			request.setAttribute("params", paramString);
			request.getRequestDispatcher("pages/hym/showOrderMer.jsp").forward(request, response);
			return;
		} else if(ACTION_PUSH_ORDER.equals(action)){ 
			//4. 触发订单推送接口，返回redirectUrl跳转我们前台网关。
			StringBuffer errMsgBuffer = new StringBuffer();
			String redirectUrl = orderPush(request, errMsgBuffer);
			if(redirectUrl == null) {
				forwordErr("调用订单推送接口失败，失败信息：" + errMsgBuffer.toString(), request, response);
				return;
			}
			response.sendRedirect(redirectUrl);
			return;
		} 
		forwordErr("错误访问参数？", request, response);
	}
	
	private String getQueryString(Map<String, String> params) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		for ( String key : params.keySet() ) {
			sb.append(key + "=" + URLEncoder.encode(params.get(key), "utf-8") + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	private void forwordErr(String errMsg, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setAttribute("errInfo", errMsg);
		request.getRequestDispatcher("pages/hym/err.jsp").forward(request, response);
	}
	
	private String getAppUsrId(String merId, String userAuthCode, String appUpIdentifier, StringBuffer errMsg){

		Map<String, String> contentData = new HashMap<String, String>();

		/***银联全渠道系统，产品参数，一般不需修改***/
		contentData.put("version", DemoBase.version);                  //版本号
		contentData.put("encoding", DemoBase.encoding);           //字符集编码式
		contentData.put("signMethod", SDKConfig.getConfig().getSignMethod());                           //签名方法 目前只支持01-RSA方式证书加密
		contentData.put("txnType", "00");                              //交易类型 
		contentData.put("txnSubType", "10");                           //交易子类型 
		contentData.put("bizType", "000000");                          //业务类型 代收产品
		contentData.put("channelType", "08");                          //渠道类型
		contentData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
		
		/***商户接入参数***/
		//TODO
		contentData.put("merId", merId);                   			   //商户号码
		contentData.put("orderId", new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));             			   //商户订单号，8-32位数字字母，不能含“-”或“_”，可以自行定制规则
		contentData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));         				   //订单发送时间，格式为yyyyMMddHHmmss，必须取当前时间，否则会报txnTime无效
		contentData.put("userAuthCode", userAuthCode); 
		contentData.put("appUpIdentifier", appUpIdentifier); 
		
		Map<String, String> reqData = AcpService.sign(contentData, encoding);			 //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestUrl = SDKConfig.getConfig().getBackRequestUrl();
		Map<String, String> rspData = AcpService.post(reqData,requestUrl,encoding);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, encoding)){
				LogUtil.writeLog("验证签名成功");
				String respCode = rspData.get("respCode");
				String respMsg = rspData.get("respMsg");
				String appUserId = rspData.get("appUserId");
				if(("00").equals(respCode)){
					return appUserId;
				} else {
					errMsg.append("respCode=" + respCode + ", respMsg=" + respMsg);
				}
			}else{
				LogUtil.writeErrorLog("验证签名失败");
				errMsg.append("验证签名失败");
			}
		}else{
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
			errMsg.append("未获取到返回报文或返回http状态码非200");
		}
		return null;
	}

	private String orderPush(HttpServletRequest request, StringBuffer errMsg){
		
		String frontUrl = SDKConfig.getConfig().getFrontUrl();
		String backUrl = SDKConfig.getConfig().getBackUrl();
		String merId = request.getParameter("merId");
		String txnAmt = request.getParameter("txnAmt");
		String orderId = request.getParameter("orderId");
		String txnTime = request.getParameter("txnTime");
		String appUserId = request.getParameter("appUserId");
		String withFrontUrl = request.getParameter("withFrontUrl");

		/***银联全渠道系统，产品参数，一般不需修改***/
		Map<String, String> contentData = new HashMap<String, String>();
		contentData.put("version", DemoBase.version);                  //版本号
		contentData.put("encoding", DemoBase.encoding);           //字符集编码式
		contentData.put("signMethod", "01");                           //签名方法 目前只支持01-RSA方式证书加密
		contentData.put("txnType", "01");                              //交易类型
		contentData.put("txnSubType", "01");                           //交易子类型
		contentData.put("bizType", "000000");                          //业务类型
		contentData.put("accessType", "0");                            //接入类型，商户接入固定填0，不需修改
		contentData.put("channelType", "08");                          //渠道类型
		contentData.put("currencyCode", "156");				//交易币种，境内商户固定156
		contentData.put("tradeType", "mobileWeb");					//行业码场景固定送mobileWeb

		/***商户接入参数***/
		//TODO
		contentData.put("merId", merId);                   			   //商户号码
		contentData.put("orderId", orderId);             			   //商户订单号，8-32位数字字母，不能含“-”或“_”，可以自行定制规则
		contentData.put("txnTime", txnTime);         				   //订单发送时间，格式为yyyyMMddHHmmss，必须取当前时间，否则会报txnTime无效
		contentData.put("txnAmt", txnAmt);							   //交易金额，单位分，不要带小数点
		contentData.put("appUserId", appUserId);					//app用户标识
		if("true".equals(withFrontUrl)){
			contentData.put("frontUrl", frontUrl); 
		}
		contentData.put("backUrl", backUrl); 
		
		Map<String, String> reqData = AcpService.sign(contentData, encoding);			 //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestUrl = SDKConfig.getConfig().getOrderRequestUrl();
		Map<String, String> rspData = AcpService.post(reqData,requestUrl,encoding);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		if(!rspData.isEmpty()){
			if(AcpService.validate(rspData, encoding)){
				LogUtil.writeLog("验证签名成功");
				String respCode = rspData.get("respCode");
				String respMsg = rspData.get("respMsg");
				String payUrl = rspData.get("payUrl") ;//支付跳转地址
				try {
					payUrl = URLDecoder.decode(payUrl, encoding);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
				if(("00").equals(respCode)){
					return payUrl;
				} else {
					errMsg.append("respCode=" + respCode + ", respMsg=" + respMsg);
				}
			}else{
				LogUtil.writeErrorLog("验证签名失败");
				errMsg.append("验证签名失败");
			}
		}else{
			LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
			errMsg.append("未获取到返回报文或返回http状态码非200");
		}
		return null;
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 获取请求参数中所有的信息
	 * 当商户上送frontUrl或backUrl地址中带有参数信息的时候，
	 * 这种方式会将url地址中的参数读到map中，会导多出来这些信息从而致验签失败，这个时候可以自行修改过滤掉url中的参数或者使用getAllRequestParamStream方法。
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(
			final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					// System.out.println("======为空的字段名===="+en);
					res.remove(en);
				}
			}
		}
		return res;
	}
	
	public static void main(String[] args) {

		String ua = "Mobile Safari/ UnionPay/1.0 TESTPAY";
		String uaReg = ".*UnionPay/(1.0) ([0-9a-zA-Z]+).*";
		Pattern p = Pattern.compile(uaReg);
		Matcher m = p.matcher(ua);
		if(m.matches()){
			String appId = m.group(2);
			System.out.println(appId);
		} else {
			System.out.println("bb");
		}
	}

}
