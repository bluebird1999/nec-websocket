package com.globe_sh.cloudplatform.websocket.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.globe_sh.cloudplatform.websocket.entity.EventMessage;
import com.globe_sh.cloudplatform.websocket.entity.EventStatusMessage;

import com.globe_sh.cloudplatform.websocket.entity.DataRules;

public class SessionManager {

	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SessionManager.class);
	private static SessionManager instance;
	private Map<String, WebSocketSession> session = new HashMap<String, WebSocketSession>();
	private Map<String, DataRules> rules = new HashMap<String, DataRules>();
    private EventMessage msg = null;
    private Map<Integer,EventStatusMessage> statusMap = null;
    private int complicatedSearch = 0;
    
	private SessionManager() {
	}

	public static synchronized SessionManager getInstance() {
		if (instance == null)
			instance = new SessionManager();
		return instance;
	}
	
	public String processClientMessage(String sid, String jsonStr)
	{
		DataRules bean = new DataRules();
		logger.info("Gt new raw client message " + jsonStr);
		try{
			bean.fromJsonString(jsonStr);	
						
			if( !session.containsKey(sid) )	{
				bean.setStatus(1);
			}
			else {
				if( bean.getOperate().equals("setup") ) {
					if( bean.getFactory()==0 ) {
						bean.setStatus(2);
					}
					else {		
						logger.info("Add new valid rule set for session id: " + sid);
						rules.put(sid, bean);
					}
				}
				else if ( bean.getOperate().equals("clear") ) {
					logger.info("Remove the rule set for session id: " + sid);
					rules.remove(sid);		
				}
				else {
					logger.info("Error, client didn't pass the correct operate code, code=: " + bean.getOperate());
					bean.setStatus(3);
				}
			}
		} catch(Exception jmse) {
			logger.error("New message processing found exception: ",jmse);
		}				
		
		return bean.getJsonString();
	}
	
	public void processData(String newMsg) {
		logger.info("Get new message from the server, starting the process...");		
		try{
			msg = new EventMessage();
			msg.fromJsonString(newMsg);
			List<EventStatusMessage> esm = msg.getStatusList();
			
			//irate the rules map
			for (Map.Entry<String,DataRules> entry : rules.entrySet()) {
				DataRules dr = entry.getValue();
				List<EventStatusMessage> newEsm = new ArrayList<EventStatusMessage>();
				
				//only if the factory is set and equal to the message.
				if ( dr.getFactory() == msg.getFactory() ) { 
					//search for the available data
					if ( dr.getCodesList()!= null )	{							
						List<Integer> codes = dr.getCodesList();
	 					if( complicatedSearch == 0 )
							reBuildMsgMap(esm);
						for(Integer st: codes) {
							if( statusMap.containsKey(st) ) {
								newEsm.add( statusMap.get(st));
								logger.info("find this code=" + st);
							}							
						}
					}
					else {
						logger.info("all messages!");
						newEsm = esm;		
					}
				}
				else {
					logger.info("factory is not equal!");
				}
				//send searched data 
				if( newEsm.size() > 0 )	{
					msg.setStatusList(newEsm);
					sendData(entry.getKey(), msg.getForwardJsonString() );
				}				
			}
			//reset the search label.
			complicatedSearch = 0;
		} catch(Exception jmse) {
			logger.error("New message processing found exception: ",jmse);
		}				
		logger.info("Finish the new message process and forward...");
	}
	
	public void sendData(String sid, String data) {
		try{
			if( session.containsKey(sid)) {
				session.get(sid).sendMessage( new TextMessage(data) );
			}			
		} catch(Exception jmse) {
			logger.error("Sending data found eception: ",jmse);
		}		
	}
	
	public int addSession(WebSocketSession ss) {
		if( session.containsKey(ss.getId()) )
			return 1;
		//add session now
		logger.info("Add new session: " + ss.getId());
		session.put(ss.getId(), ss);
		
		return 0;
	}
	
	public void removeSession(WebSocketSession ss) {
		logger.info("Remove session id: " + ss.getId());
		if( session.containsKey(ss.getId()) ) {
			session.remove(ss.getId());
			logger.info("Removed session: " + ss.getId());
		}
		//add session now
		if( rules.containsKey(ss.getId()) ) {
			logger.info("Removed rules: " + ss.getId());
			rules.remove(ss.getId());
		}
	}	
	
	public void reBuildMsgMap( List<EventStatusMessage> esm) {
		statusMap = new HashMap<Integer,EventStatusMessage>();
		for(EventStatusMessage obj : esm) {
			statusMap.put(obj.getAttributeId(), obj);
		}
		complicatedSearch = 1;
	}
}
