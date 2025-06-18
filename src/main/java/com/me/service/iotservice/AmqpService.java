package com.me.service.iotservice;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
public class AmqpService   {

    private static final Logger log = LoggerFactory.getLogger(AmqpService.class);

    private final Connection connection;
    private final Session session;
    private final Destination queue;
    private MessageConsumer consumer;
    // 线程池配置
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
    }
    private void initConsumer() throws JMSException {
        if (consumer != null) {
            consumer.close();
        }
        consumer = session.createConsumer(queue);
    }
    // 接收消息方法（供定时任务调用）
    public void receiveMessage() {
        try {
            Message message = consumer.receive(5000); // 超时5秒
            if (message != null) {
                processMessage(message);
            }
        } catch (JMSException e) {
            log.error("接收消息异常", e);
            try {
                // 异常时重新初始化消费者
                initConsumer();
            } catch (JMSException ex) {
                log.error("重新初始化消费者异常", ex);
            }
        }
    }

    // 处理消息
    private void processMessage(Message message) {
        executorService.submit(() -> {
            try {
                String mesStr = message.getBody(String.class);
                JSONObject jsonObject = JSON.parseObject(mesStr);
                JSONObject notifyData = jsonObject.getJSONObject("notify_data");
                JSONObject body = notifyData.getJSONObject("body");
                List<Map> services = body.getJSONArray("services").toJavaList(Map.class);
                // 处理services数据
                if (!services.isEmpty()) {
                    Map<String, Object> service = services.get(0);

                    System.out.println("Service ID: " + service.get("service_id"));
                    System.out.println("Event Time: " + service.get("event_time"));
                    // 提取properties并转换为Map
                    Map<String, Object> properties = (Map<String, Object>) service.get("properties");
                    System.out.println("\nProperties:");
                    for (Map.Entry<String, Object> entry : properties.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                    System.out.println("\n完整的service Map:");
                    System.out.println(service);
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
            log.error("连接失败", error);
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
        }
        @Override
        public void onConsumerClosed(MessageConsumer consumer, Throwable cause) {
            log.warn("消费者关闭，consumer:{}，原因:{}", consumer, cause);
        }
        @Override
        public void onProducerClosed(MessageProducer producer, Throwable cause) {
            log.warn("生产者关闭，producer:{}，原因:{}", producer, cause);
        }
    };
}
