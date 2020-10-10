package demo.com.unionpay.acp.demo;

import demo.com.unionpay.acp.sdk.SDKConfig;
import org.springframework.stereotype.Component;

/**
 * @Author : yang_y_1.0.2
 * @Date : 2020/9/18 2:09 下午
 * @Version : 1.2.1
 */
@Component
public class common {
    public common(){
        SDKConfig.getConfig().loadPropertiesFromSrc();
    }
}
