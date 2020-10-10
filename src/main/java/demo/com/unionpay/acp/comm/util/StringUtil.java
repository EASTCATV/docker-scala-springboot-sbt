package demo.com.unionpay.acp.comm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;

/**
 * 字符串工具.
 * @author hrtc
 * .
 */
public class StringUtil {
    /**
     * 判断字符串为空.
     * @param str 字符串
     * @return 是否为空
     */
	public static boolean isEmpty(String str){
		return str==null||str.trim().length()==0?true:false;
	}

	/**
	 * 判断字符串不为空.
	 * @param str 字符串
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	/** 
     * 获取传入时间所对应的年月日时间  .
     * @param date 当前时间 .
     * @return 返回字符串格式 yyyyMMdd .
     */
    public static String getRelevantDate(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");  
	    String dateString = formatter.format(date);  
	    return dateString; 
    }

    /** 
     * 获取传入时间所对应的时分秒时间 . 
     * @param date 当前时间 .
     * @return 返回字符串格式 HHmmss .
     */  
    public static String getRelevantTime(Date date){
    	 SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");  
         String dateString = formatter.format(date);  
         return dateString; 
    }
    /**
	 * 字符串补位.
	 * @param val 字符串 .
	 * @param type r右补位l左补位 .
	 * @param size 补足长度 .
	 * @param delim 补位用字符串 .
	 * @return String .
	 */
	public static String pad(String val, String type, int size, String delim) {
		if(isEmpty(val)) return val;
		if ("r".equals(type)) {
			if (val.length() >= size) {
				return val.substring(0, size);
			} else {
				return val + repeat('r', delim, size - val.length());
			}
		} else {
			if (val.length() >= size) {
				return val.substring(val.length() - size);
			} else {
				return repeat('l', delim, size - val.length()) + val;
			}
		}
	}
	/**
	 * 重复字符串 .
	 * @param type r右补位l左补位 .
	 * @param val 字符串 .
	 * @param len 长度 .
	 * @return .
	 */
	public static String repeat(char type, String val, int len) {
		if(isEmpty(val)) return val;
		StringBuffer tBuffer = new StringBuffer();
		while (tBuffer.length() < len) {
			tBuffer.append(val);
		}

		if ('r' == type) {
			return tBuffer.substring(0, len);
		} else {
			return tBuffer.substring(tBuffer.length() - len);
		}

	}

	/**
	 * 解析map到str .
	 * @param dataMap map
	 * @return 字符串
	 */
	public static String paserMaptoStr(Map<String, Object> dataMap) {
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, Object> entry:dataMap.entrySet()){
			sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		if(sb.length()>0){
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}
	
	/**
	 * 解析响应字符串 .
	 * @param httpResonse http响应
	 * @return 字符串
	 * @throws IOException .
	 */
	public static String parseResponseToStr(HttpResponse httpResonse) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(httpResonse.getEntity().getContent(),"utf-8"));
		  StringBuffer sb = new StringBuffer();
		  String str = null;
		  while((str = br.readLine())!=null){
			  sb.append(str);
		  }
		return sb.toString();
	}

	/**
	 * 
	 * 将字符串转成map.
	 * @param respStr 字符串
	 * @return map
	 */
	public static Map<String, String> paserStrToMap(String respStr) {
		Map<String, String> data = new HashMap<String, String>();
		if (isNotEmpty(respStr)) {
			String[] strs = respStr.split("&");
			for (String str : strs) {
				if (isEmpty(str)) {
					continue;
				}
				int index = str.indexOf("=");
//				if (entryStrs.length > 1) {
//					data.put(entryStrs[0], entryStrs[1]);
//				}
				data.put(str.substring(0, index),str.substring(index+1));
			}
		}
		return data;
	}
}
