package com.globe_sh.cloudplatform.websocket.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.globe_sh.cloudplatform.common.util.StaticMethod;

public class DataRules implements Serializable {

	private static final long serialVersionUID = -1355737059240580486L;
	
	private String operate;
	private int status;				//0:unset, 1:set
	private String factory;	
	private String station;
	private String device;	
	private int dataBlock;
	private List<Integer> codesList;

	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getStation() {
		return station;
	}	
	public void setStation(String station) {
		this.station = station;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public int getDataBlock() {
		return dataBlock;
	}
	public void setDataBlock(int dataBlock) {
		this.dataBlock = dataBlock;
	}	
	public List<Integer> getCodesList() {
		return codesList;
	}
	public void setCodesList(List<Integer> codesList) {
		this.codesList = codesList;
	}
	
	@Override
	public String toString() {
		String str = "EventMessage ["
				+ "operate=" + operate
				+ ", status=" + status
				+ ", data_block=" + dataBlock 
				+ ", device=" + device 
				+ ", station=" + station
				+ ", factory=" + factory
				+ "]\n";
		
		for(Integer codesMessage : codesList) {
			str = str + String.valueOf(codesMessage) + "\n";
		}
		
		return str;
	}
	
	public void fromJsonString(String text) {
		JSONObject json = JSONObject.parseObject(text);
		fromJsonObject(json);
	}
	
	public void fromJsonObject(JSONObject json) {
		try{
			if( json.containsKey("operate"))
				this.operate = json.getString("operate");
			if( json.containsKey("device"))
				this.device = json.getString("device");
			if( json.containsKey("data_block"))
				this.dataBlock = json.getIntValue("data_block");
			if( json.containsKey("station"))
				this.station = json.getString("station");
			if( json.containsKey("factory"))
				this.factory = json.getString("factory");
			this.status = 0;
			
			if( json.containsKey("data") )
			{
				if( !json.getJSONArray("data").equals(null) ) {
					JSONArray statusArray = json.getJSONArray("data");
					codesList = new ArrayList<Integer>();
					Integer obj = null;
					for(int i = 0; i < statusArray.size(); i++) {
						obj = statusArray.getIntValue(i);
						codesList.add(obj);
					}
				}
			}
		} catch(Exception jmse) {
		}			
	}
	
	public String getJsonString() {
		JSONObject json = new JSONObject();
		try{
			JSONArray statusArray = new JSONArray();
			json.put("operate", this.operate);
			json.put("status", this.status);
			json.put("data_block", this.dataBlock);
			json.put("device", this.device);
			json.put("station", this.station);
			json.put("factory", this.factory);
			
			if( codesList!=null )
			{
				for(Integer obj : codesList) {
					statusArray.add(obj);
				}
				json.put("data", statusArray);
			}
		} catch(Exception jmse) {
		}		
		return json.toJSONString();
	}
}