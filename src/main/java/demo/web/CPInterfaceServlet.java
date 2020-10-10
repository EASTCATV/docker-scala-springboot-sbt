package demo.web;

import demo.com.unionpay.acp.comm.Constants;
import demo.com.unionpay.acp.comm.CpPost;
import demo.com.unionpay.acp.comm.util.StringUtil;
import demo.com.unionpay.acp.demo.Form05_6_1_ApplyQrCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.chinapay.secss.SecssUtil;
import org.json.JSONObject;
/**
 * @Author : yang_y_1.0.2
 * @Date : 2020/9/25 4:34 下午
 * @Version : 1.2.1
 */
@Controller
@RequestMapping("/CP/")
@CrossOrigin
public class CPInterfaceServlet {


    @Resource
    private CpPost cppost;

    /**
     * .
     */
    private static final long serialVersionUID = 1L;


    /**
     * 配置文件根路径.
     */
    private static String certBasePath = null;



    /**
     * 同步应答需要encoding的交易类型.
     */
    private static Set<String> encodingTransTypes = new HashSet<String>();

    static {
        encodingTransTypes.add("0004");
        encodingTransTypes.add("0504");
        encodingTransTypes.add("0505");
        encodingTransTypes.add("0606");
        encodingTransTypes.add("0608");
        encodingTransTypes.add("9904");
        encodingTransTypes.add("9905");

        encodingTransTypes.add("0006");
        encodingTransTypes.add("0505");
        encodingTransTypes.add("0601");
        encodingTransTypes.add("0607");
        encodingTransTypes.add("9901");
    }


    @GetMapping("getSign")
    public void doget(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> sendMap = new TreeMap<String, String>();
        // 交易保留域
        JSONObject transParamsJson = new JSONObject();
        // 机构或商户接入
        String instuId = sendMap.get(Constants.INSTU_ID);
        String merId = sendMap.get(Constants.MER_ID);
        String ownerId = null;
        if (StringUtil.isNotEmpty(instuId)) {
            ownerId = instuId;
        } else {
            ownerId = merId;
        }
        // 获得签名加密类
        SecssUtil secssUtil = getSecssUtil(ownerId);

        // 构建交易保留域
        String strTranReserved = null;
        if (transParamsJson.length() > 0) {
            strTranReserved = transParamsJson.toString();
        }
    }
    /**
     * 加载安全秘钥 .
     *
     * @param ownerId
     *            所有者id
     * @return SecssUtil .
     */
    protected SecssUtil getSecssUtil(String ownerId) {
        String path = String.format("%s/%s.properties", certBasePath, ownerId);
        SecssUtil secssUtil = new SecssUtil();
        secssUtil.init(path);
        return secssUtil;
    }
}
