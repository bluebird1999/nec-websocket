package com.globe_sh.cloudplatform.websocket.message;

import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.globe_sh.cloudplatform.websocket.manager.SessionManager;

@Component
@EnableJms
public class ActiveMQListener {

	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ActiveMQListener.class);
    
    @JmsListener(destination = "websocket-queue")
    public void listener(byte[] msg){
    	String message = new String(msg);
        logger.info("Message received " + message);
		
		SessionManager.getInstance().processData(message);
    }
}
