package com.me.service.iotservice;

import com.huaweicloud.sdk.core.auth.AbstractCredentials;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.core.region.Region;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.CreateMessageRequest;
import com.huaweicloud.sdk.iotda.v5.model.CreateMessageResponse;
import com.huaweicloud.sdk.iotda.v5.model.DeviceMessageRequest;
import org.springframework.stereotype.Service;

@Service
public class IotService {
    private String ak = "HPUACEVJDUKJBISQOI3C";//System.getenv("CLOUD_SDK_AK");
    private String sk = "oxf4p6XE6Ms06efkI6qD74FcAGtarSkYKZ4ajBdI";//System.getenv("CLOUD_SDK_SK");
    private String iotdaEndpoint = "1b177f15b8.st1.iotda-app.cn-north-4.myhuaweicloud.com";
    public void sendMessage(String context){
        String deviceId = "684fd5a6d582f2001831e667_myNodeId2";
        ICredential auth = new BasicCredentials()
                .withDerivedPredicate(AbstractCredentials.DEFAULT_DERIVED_PREDICATE)
                .withAk(ak)
                .withSk(sk);

        IoTDAClient client = IoTDAClient.newBuilder()
                .withCredential(auth)
                .withRegion(new Region("cn-north-4", iotdaEndpoint))
                .build();
        CreateMessageRequest request = new CreateMessageRequest();
        request.withDeviceId(deviceId);
        DeviceMessageRequest body = new DeviceMessageRequest();
        body.withMessage(context);
        request.withBody(body);
        try {
            CreateMessageResponse response = client.createMessage(request);
            System.out.println(response.toString());
        } catch (ConnectionException | RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getRequestId());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }
    }
}
