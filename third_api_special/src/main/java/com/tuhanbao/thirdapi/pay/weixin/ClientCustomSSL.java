/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.tuhanbao.thirdapi.pay.weixin;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.tuhanbao.base.Constants;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
@SuppressWarnings("deprecation")
public class ClientCustomSSL {

    public static String doRefund(String url, String data) throws Exception {
        /**
         * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
         */

        KeyStore keyStore = KeyStore.getInstance(WeixinConstants.PKCS12);

        FileInputStream instream = new FileInputStream(new File(WeixinConfig.PROXY));// P12文件目录
        try {
            /**
             * 此处要改
             */
            keyStore.load(instream, WeixinConfig.MCH_ID.toCharArray());// 这里写密码..默认是你的MCHID
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        /**
         * 此处要改
         */
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, WeixinConfig.MCH_ID.toCharArray())// 这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{WeixinConstants.TLSV1}, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息

            for (String key : WeixinConstants.HEADER_PARAMS.keySet()) {
                httpost.addHeader(key, WeixinConstants.HEADER_PARAMS.get(key));
            }
            // httpost.addHeader("Connection", "keep-alive");
            // httpost.addHeader("Accept", "*/*");
            // httpost.addHeader("Content-Type",
            // "application/x-www-form-urlencoded; charset=UTF-8");
            // httpost.addHeader("Host", "api.mch.weixin.qq.com");
            // httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            // httpost.addHeader("Cache-Control", "max-age=0");
            // httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE
            // 8.0; Windows NT 6.0) ");

            httpost.setEntity(new StringEntity(data, Constants.UTF_8));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                String jsonStr = EntityUtils.toString(response.getEntity(), Constants.UTF_8);
                EntityUtils.consume(entity);
                return jsonStr;
            }
            finally {
                response.close();
            }
        }
        finally {
            httpclient.close();
        }
    }

}
