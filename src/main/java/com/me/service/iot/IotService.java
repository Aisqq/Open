package com.me.service.iot;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IotService {
    @Value("${iot.ak}")
    private String ak ;
    @Value("${iot.sk}")
    private String sk ;
    @Value("${iot.url}")
    private String iotdaEndpoint ;
    @Value("iot.deviceId")
    private String deviceId;
    public void sendMessage(String context){
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
