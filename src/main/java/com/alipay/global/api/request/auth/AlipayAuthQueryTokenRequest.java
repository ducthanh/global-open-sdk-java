package com.alipay.global.api.request.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.alipay.global.api.net.HttpMethod;
import com.alipay.global.api.request.AlipayRequest;
import com.alipay.global.api.response.auth.AlipayAuthQueryTokenResponse;

public class AlipayAuthQueryTokenRequest extends AlipayRequest<AlipayAuthQueryTokenResponse> {

    @JSONField(serialize = false)
    private final static String httpMethod =  HttpMethod.POST.name();

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getHttpMethod() {
        return httpMethod;
    }

    @Override
    public Class<AlipayAuthQueryTokenResponse> getResponseClass() {
        return AlipayAuthQueryTokenResponse.class;
    }

}
