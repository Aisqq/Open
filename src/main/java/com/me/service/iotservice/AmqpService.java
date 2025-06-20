package com.me.service.iotservice;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.me.service.IotDeviceServer;
import org.apache.qpid.jms.JmsConnection;
import org.apache.qpid.jms.JmsConnectionListener;
import org.apache.qpid.jms.message.JmsInboundMessageDispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.jms.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class AmqpService {
    @Autowired
    private List<IotDeviceServer> iotDeviceServers;
    private static final Logger log = LoggerFactory.getLogger(AmqpService.class);
    private final Connection connection;
    private final Session session;
    private final Destination queue;
    private MessageConsumer consumer;
    private final ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(5000)
    );

    @Autowired
    public AmqpService(Connection connection, Session session, Destination queue) throws JMSException {
        this.connection = connection;
        this.session = session;
        this.queue = queue;
        ((JmsConnection) connection).addConnectionListener(myJmsConnectionListener);
        initConsumer();
        connection.start();
    }

    private void initConsumer() throws JMSException {
        if (consumer != null) {
            consumer.close();
        }
        consumer = session.createConsumer(queue);
        consumer.setMessageListener(this::processMessage);
        log.info("消息监听器已注册，等待消息...");
    }

    /**
     * event_time:数据记录时间->recordTime
     * service_id：传感器类型->deviceType
     * deviceId:设备唯一标识
     * @param message
     */
    private void processMessage(Message message) {
        executorService.submit(() -> {
            try {
                String mesStr = message.getBody(String.class);
                JSONObject jsonObject = JSON.parseObject(mesStr);
                JSONObject notifyData = jsonObject.getJSONObject("notify_data");
                JSONObject body = notifyData.getJSONObject("body");
                List<Map> services = body.getJSONArray("services").toJavaList(Map.class);
                if (!services.isEmpty()) {
                    Map<String, Object> service = services.get(0);
                    System.out.println("Service ID: " + service.get("service_id"));
                    System.out.println("Event Time: " + service.get("event_time"));
                    Map<String, Object> properties = (Map<String, Object>) service.get("properties");
                    properties.put("deviceType", service.get("service_id"));
                    properties.put("recordTime", service.get("event_time"));
                    System.out.println("\nProperties:");
                    for (IotDeviceServer iotDeviceServer : iotDeviceServers) {
                        if (iotDeviceServer.findDeviceType((String) properties.get("deviceType"))) {
                            iotDeviceServer.addData(properties);
                            break;
                        }
                    }
                }

                log.info("接收到消息: {}", mesStr);
            } catch (Exception e) {
                log.error("处理消息异常", e);
            }
        });
    }

    private JmsConnectionListener myJmsConnectionListener = new JmsConnectionListener() {
        @Override
        public void onConnectionEstablished(URI remoteURI) {
            log.info("连接已建立，remoteUri:{}", remoteURI);
        }

        @Override
        public void onConnectionFailure(Throwable error) {
            log.error("连接失败，尝试重新初始化消费者", error);
            reconnect();
        }

        @Override
        public void onConnectionInterrupted(URI remoteURI) {
            log.info("连接中断，remoteUri:{}", remoteURI);
        }

        @Override
        public void onConnectionRestored(URI remoteURI) {
            log.info("连接已恢复，remoteUri:{}", remoteURI);
            try {
                initConsumer();
            } catch (JMSException e) {
                log.error("恢复连接后初始化消费者异常", e);
            }
        }

        @Override
        public void onInboundMessage(JmsInboundMessageDispatch envelope) {
            log.debug("入站消息:{}", envelope);
        }

        @Override
        public void onSessionClosed(Session session, Throwable cause) {
            log.warn("会话关闭，session:{}，原因:{}", session, cause);
            reconnect();
        }

        @Override
        public void onConsumerClosed(MessageConsumer consumer, Throwable cause) {
            log.warn("消费者关闭，consumer:{}，原因:{}", consumer, cause);
            reconnect();
        }

        @Override
        public void onProducerClosed(MessageProducer producer, Throwable cause) {
            log.warn("生产者关闭，producer:{}，原因:{}", producer, cause);
        }

        // 统一的重连逻辑
        private void reconnect() {
            try {
                // 添加适当的延迟，避免频繁重连
                Thread.sleep(5000);
                log.info("尝试重新初始化消费者...");
                initConsumer();
            } catch (Exception e) {
                log.error("重连失败，将在下次连接事件时再次尝试", e);
            }
        }
    };
    // 关闭资源的方法
    public void shutdown() {
        try {
            if (consumer != null) consumer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
            if (executorService != null) executorService.shutdown();
            log.info("AmqpService 已关闭");
        } catch (Exception e) {
            log.error("关闭资源异常", e);
        }
    }
}