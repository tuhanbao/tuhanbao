package com.tuhanbao.thirdapi.pay.abc;

import java.security.PrivateKey;

public class MerchantInfo {
    //商户号
    private String merchantId;
    
    private PrivateKey key;
    
    private String merchantCertificate;

    public MerchantInfo(String merchantId, PrivateKey key, String merchantCertificate) {
        this.merchantId = merchantId;
        this.key = key;
        this.merchantCertificate = merchantCertificate;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public PrivateKey getKey() {
        return key;
    }

    public String getMerchantCertificate() {
        return merchantCertificate;
    }
}
