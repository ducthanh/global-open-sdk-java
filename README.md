```
Language：JAVA  
JDK version：1.6+  
Copyright：Ant financial services group  
```

#### 1 Please use the latest version

https://mvnrepository.com/artifact/com.alipay.global.sdk/global-open-sdk-java

```  
<dependency>
    <groupId>com.alipay.global.sdk</groupId>
    <artifactId>global-open-sdk-java</artifactId>
    <version>1.0.5</version>
</dependency>
```
   
#### 2 Main class file  
```
DefaultAlipayClient.java  

public DefaultAlipayClient(String gatewayUrl, String merchantPrivateKey, String alipayPublicKey);  
public <T extends AlipayResponse> T execute(AlipayRequest<T> alipayRequest);  
  
```
  
``` 
SignatureTool.java 

public static String  genSignContent(String httpMethod, String uriWithQueryString, String clientId, String timeString, String content);
public static String  sign(String reqContent, String merchantPrivateKey);  
public static boolean verify(String rspContent, String signature, String alipayPublicKey); 
 
```
  
#### 3 The sample for pay  
  
```
AlipayClient defaultAlipayClient = new DefaultAlipayClient("https://open-na.alipay.com","merchantPrivateKey", "alipayPublicKey");  
  
AlipayPayRequest alipayPayRequest = new AlipayPayRequest();  
alipayPayRequest.setClientId("T_111222333"); 
alipayPayRequest.setPath("/ams/sandbox/api/v1/payments/pay"); 
alipayPayRequest.setProductCode(ProductCodeType.AGREEMENT_PAYMENT);  
alipayPayRequest.setPaymentRequestId("pay_1029760066776669_102775765796667639");  
  
Amount paymentAmount = new Amount();  
paymentAmount.setCurrency("PHP");  
paymentAmount.setValue("10000");  
alipayPayRequest.setPaymentAmount(paymentAmount);  
  
Order order = new Order();  
order.setReferenceOrderId("102775765075669");  
order.setOrderDescription("Mi Band 3 Wrist Strap Metal Screwless Stainless Steel For Xiaomi Mi Band 3");

Amount orderAmount = new Amount();
orderAmount.setCurrency("PHP");
orderAmount.setValue("10000");       
order.setOrderAmount(orderAmount);  
alipayPayRequest.setOrder(order);  
  
PaymentMethod paymentMethod = new PaymentMethod();  
paymentMethod.setPaymentMethodType("GCASH");  
paymentMethod.setPaymentMethodId("20191029042129157232288970435238628515579670ClupeQXzXu");  
alipayPayRequest.setPaymentMethod(paymentMethod);  
  
AlipayPayResponse  alipayPayResponse = defaultAlipayClient.execute(alipayPayRequest);  
  
```  
  
  
The execute method contains the HTTP request to the gateway. 

If you're concerned about HTTP invocation performance, you can implement HTTP invocation yourself.
 
```
public class YourAlipayClient extends BaseAlipayClient{

    public YourAlipayClient(String gatewayUrl, String merchantPrivateKey, String alipayPublicKey ){
        super(gatewayUrl, merchantPrivateKey, alipayPublicKey);
    }

    @Override
    public Map<String, String> buildCustomHeader() {
        // TODO 
    }

    @Override
    public HttpRpcResult sendRequest(String requestUrl, String httpMethod, Map<String, String> header, String reqBody)throws AlipayApiException {  
        // HTTP Call
        // ...
        
        HttpRpcResult rsp = new HttpRpcResult();
        String rspSignValue = "Get from response header";
        rsp.setRspSign(rspSignValue);
        
        String responseTime = "Get from response header";
        rsp.setResponseTime(responseTime);
        
        String rspBody = "Get from response body";
        rsp.setRspBody(rspBody);
        
        return rsp;
    }
    
}

AlipayClient       yourAlipayClient  = new YourAlipayClient("https://open-na.alipay.com", "merchantPrivateKey", "alipayPublicKey");  
AlipayPayResponse  alipayPayResponse = yourAlipayClient.execute(aliPayRequest);  

```
  
#### 4 If you don't care about HTTP calls,the sample for sign and verify  
```  
String httpMethod = "POST";
String path       = "/ams/sandbox/api/v1/payments/pay";
String clientId   = "T_client";
String reqTimeStr = "2019-11-01T10:00:00+08:30";

String reqBody       = "{\"key1\":\"value1\"}";
String signReqValue  = SignatureTool.sign(httpMethod, path, clientId, reqTimeStr, reqBody, merchantPrivateKey);  
  
String rspBody    = "{\"key2\":\"value2\"}";
String rspTimeStr = "2019-11-01T10:00:01+08:30";
boolean isPass    = SignatureTool.verify(httpMethod, path, clientId, rspTimeStr, rspBody, signRsp, alipayPublicKey);
  
```
  
#### 5 For base64  

For compatibility with lower version of Java JDK, signatureTool provided a base64 encryptor DefaultBase64Encryptor by default. 

```
public class DefaultBase64Encryptor implements Base64Encryptor{

    @Override
    public String encodeToString(byte[] src) {
        return DatatypeConverter.printBase64Binary(src);
    }

    @Override
    public byte[] decode(String src) {
        return DatatypeConverter.parseBase64Binary(src);
    }

}

```  

For better performance, you can also customize the base64 tool. For example, jdk8 API Base64.

  
```
public class YourBase64Encryptor implements Base64Encryptor{

    @Override
    public String encodeToString(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    @Override
    public byte[] decode(String src) {
        return Base64.getDecoder().decode(src);
    }
    
}

Base64Encryptor yourBase64Encryptor = new YourBase64Encryptor();
SignatureTool.setBase64Encryptor(yourBase64Encryptor);

```



