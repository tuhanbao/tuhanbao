import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

import com.sztx.se.core.mq.producer.RabbitTemplateProxy;
import com.sztx.se.core.mq.source.DynamicCreateMqProducerManager;
import com.sztx.se.core.mq.source.DynamicMqMessageSender;
import com.tuhanbao.base.util.log.LogManager;

public class TestMQ {
    
    public static final String DEMOWEB_EXCHANGE = "exchange.systemframework.demo-web";
    
    public static final String ORDER_MESSAGE_ROUTINGKEY = "order";
    
    public static final String GOODS_MESSAGE_ROUTINGKEY = "goods";
    
    public static void main(String args[]) {
        String host = "localhost";
        int port = 5672;
        String username = "guest";
        String password = "guest";
        String virtualHost = "Test";
        boolean publisherConfirms = true;
        String exchange = "amq2.direct3";
        String routingKey = "";
        boolean isDefault = true;
        
//        mq.server.virtualhost=systemframework
//                mq.producer.exchange=exchange.systemframework.demo-web
//                mq.producer.routing.key=
//                mq.listener.queue=queue.systemframework.project1
//                mq.listener.auto.startup=true
//                mq.producer.isDefault=true
        
        SimpleMessageConverter messageConverter = new SimpleMessageConverter();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        Exchange exchangeImpl = new DirectExchange(exchange);
        try {
            rabbitAdmin.declareExchange(exchangeImpl);
        }
        catch (Exception e) {
            
        }
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        rabbitTemplate.setExchange(exchange);
//        rabbitTemplate.setRoutingKey(routingKey);
        rabbitTemplate.setConfirmCallback(new ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack) {
                String correlationDataId = correlationData.getId();

                if (ack) {
                    LogManager.info("Mq message " + correlationDataId + " send success");
                } else {
                    LogManager.info("Mq message " + correlationDataId + " send failure");
                }
            }
        });
        
        RabbitTemplateProxy rabbitTemplateProxy = new RabbitTemplateProxy();
        rabbitTemplateProxy.setRabbitTemplate(rabbitTemplate);
        rabbitTemplateProxy.setIsDefault(isDefault);
        
        DynamicMqMessageSender mqMessageSender = new DynamicMqMessageSender();
        mqMessageSender.setDefaultTargetRabbitTemplate(rabbitTemplateProxy);
        
        DynamicCreateMqProducerManager dynamicCreateMqProducerManager = new DynamicCreateMqProducerManager();
        dynamicCreateMqProducerManager.setDynamicMqMessageSender(mqMessageSender);
        
        mqMessageSender.sendMessage("haha");
        
    }
}
