package com.hj.basic.rpc.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author tangj
 * @description
 * @since 2019/1/12 16:31
 */
@Slf4j
public class HttpInvocationHandler implements InvocationHandler {

    private static final String ip = "127.0.0.1:8080/api/app/56";

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            HttpGet httpGet = new HttpGet(ip);
            response = httpClient.execute(httpGet);

            if (Objects.nonNull(response) && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();

                byte b[] = new byte[1024];
                StringBuilder stringBuilder = new StringBuilder();
                while (inputStream.read(b) != -1) {
                    String str = new String(b);
                    stringBuilder.append(str);
                }

                log.info("发送请求成功，得到响应：{}", stringBuilder.toString());
            }
        } catch (Exception e) {
            log.error("http error.ip:{}", ip);
        } finally {
            if (Objects.nonNull(httpClient)) {
                httpClient.close();
            }
            if (Objects.nonNull(response)) {
                response.close();
            }
            if (Objects.isNull(inputStream)) {
                inputStream.close();
            }
        }

        return method.invoke(proxy, args);
    }
}
