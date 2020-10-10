/**
 * 项目名称(中文)
 * 项目名称(英文)
 * Copyright (c) 2016 ChinaPay Ltd. All Rights Reserved.
 */
package demo.com.unionpay.acp.comm;

/**
 * @author hrtc .
 */
public class Constants {

    /**
     * 默认编码.
     */
    public static final String ENCODING = "UTF-8";

    /**
     * 文件分隔符.
     */
    public static final String FILE_SPLIT_STR = "/";
    /**
     * 默认错误码.
     */
    public static final String DEFAULT_ERROR_CODE = "9999";

    /******** 报文字段 ********/
    /**
     * 卡信息字段.
     */
    public static final String CARD_TRAN_DATA = "CardTranData";
    /**
     * 交易保留域.
     */
    public static final String TRAN_RESERVED = "TranReserved";
    /**
     * 风控保留域.
     */
    public static final String RISK_DATA = "RiskData";

    /**
     * 响应信息.
     */
    public static final String RESP_MSG = "respMsg";
    /**
     * 响应码.
     */
    public static final String RESP_CODE = "respCode";

    /**
     * 签名.
     */
    public static final String SIGNATURE = "Signature";

    /**
     * .
     */
    public static final String MER_ID = "MerId";
    /**
     * .
     */
    public static final String INSTU_ID = "InstuId";
    /******** demo特殊字段 ********/
    /**
     * 特殊字段前缀.
     */
    public static final String SPEC_PRIFEX = "__";
    /**
     * 请求参数-定制接口类型.
     */
    public static final String SPEC_INTERFACE_TYPE = "__interfaceType";
    /**
     * 请求参数-交易类型.
     */
    public static final String SPEC_TRAN_TYPE = "__tranType";
    /**
     * 请求参数-交易子类型.
     */
    public static final String SPEC_SUB_TRAN_TYPE = "__subTransType";
    /**
     * 请求参数-调用方法.
     */
    public static final String SPEC_METHOD = "__method";
    /**
     * 请求参数-通知类型 0前台 1后台 默认是后台.
     */
    public static final String SPEC_NOTIFY_TYPE = "__notifyType";

    /**
     * 请求参数-请求地址.
     */
    public static final String SPEC_REQUEST_URL = "__requestUrl";
    /**
     * 响应参数-发送map.
     */
    public static final String SEND_MAP = "sendMap";
    /**
     * 响应参数-响应map.
     */
    public static final String RESULT_MAP = "resultMap";
    /**
     * 响应参数-接口缓存.
     */
    public static final String INTERFACE_CACHE = "interfaceCache";
    /**
     * 响应参数-报文.
     */
    public static final String PACKET = "packet";

    /**
     * 响应参数-请求地址.
     */
    public static final String REQUEST_URL = "requestUrl";
    /**
     * 方法-生成请求页面.
     */
    public static final String METHOD_GEN_REQUEST_PAGE = "genRequestPage";
    /**
     * 方法-组报文页面.
     */
    public static final String METHOD_PACK = "pack";
    /**
     * 方法-解包.
     */
    public static final String METHOD_UNPACK = "unpack";
    /**
     * 方法-发送.
     */
    public static final String METHOD_SEND = "send";
    
    /**
     * 通知类型-前台.
     */
    public static final String NOTIFY_TYPE_FRONT = "0";
    /**
     * 通知类型-后台.
     */
    public static final String NOTIFY_TYPE_BACK = "1";
}
