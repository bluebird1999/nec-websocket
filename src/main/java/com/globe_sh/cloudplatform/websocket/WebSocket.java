package com.globe_sh.cloudplatform.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
 
@SpringBootApplication
public class WebSocket extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
       SpringApplication.run(WebSocket.class, args);
	}

}