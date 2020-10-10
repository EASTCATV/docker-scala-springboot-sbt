package demo.com.unionpay.acp.comm;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

/**
 * @Author : yang_y_1.0.2
 * @Date : 2020/9/25 5:09 下午
 * @Version : 1.2.1
 */
@Component
public class CpPost {
    public Boolean post(String url,String json) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Boolean b=true;
        HttpPost httpPost = new HttpPost(url);

        /* 设置超时 */
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).build();
        httpPost.setConfig(defaultRequestConfig);
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.setEntity(new StringEntity(json, "UTF-8"));
        CloseableHttpResponse response = null;
        String result = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();

        }
        System.out.println("result:"+result);



        return b;
    }
}
