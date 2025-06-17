package com.me.config;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.transports.TransportOptions;
import org.apache.qpid.jms.transports.TransportSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

@Configuration
public class AmqpConfig {

    @Value("${amqp.accessKey}")
    private String accessKey;

    @Value("${amqp.password}")
    private String password;

    @Value("${amqp.baseUrl}")
    private String baseUrl;

    @Value("${amqp.queueName}")
    private String queueName;

    @Bean
    public ConnectionFactory connectionFactory() throws Exception {
        String connectionUrl = "amqps://" + baseUrl + ":5671?amqp.vhost=default&amqp.idleTimeout=8000&amqp.saslMechanisms=PLAIN";

        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("connectionfactory.HwConnectionURL", connectionUrl);
        hashtable.put("queue.HwQueueName", queueName);
        hashtable.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.qpid.jms.jndi.JmsInitialContextFactory");

        Context context = new InitialContext(hashtable);
        return (JmsConnectionFactory) context.lookup("HwConnectionURL");
    }

    @Bean
    public Connection connection(ConnectionFactory connectionFactory) throws Exception {
        long timeStamp = System.currentTimeMillis();
        String userName = "accessKey=" + accessKey + "|timestamp=" + timeStamp;

        Connection connection = connectionFactory.createConnection(userName, password);

        // 信任服务端
        TransportOptions to = new TransportOptions();
        to.setTrustAll(true);
        ((JmsConnectionFactory) connectionFactory).setSslContext(
                TransportSupport.createJdkSslContext(to));

        return connection;
    }

    @Bean
    public Session session(Connection connection) throws Exception {
        connection.start();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Bean
    public Topic topic(Session session) throws JMSException {
        return session.createTopic(queueName);
    }

}
