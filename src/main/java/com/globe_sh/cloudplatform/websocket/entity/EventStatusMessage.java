package com.globe_sh.cloudplatform.websocket.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.alibaba.fastjson.JSONObject;
import com.globe_sh.cloudplatform.common.util.StaticVariable;

public class EventStatusMessage implements Serializable {

	private static final long serialVersionUID = -7577685733460657229L;

	private String dataBatchId;
	private int attributeId;
	private String attributeValue;
	
	public EventStatusMessage() {
		
	}
	
	public EventStatusMessage(String dataBatchId, int attributeId, String attributeValue) {
		this.dataBatchId = dataBatchId;
		this.attributeId = attributeId;
		this.attributeValue = attributeValue;
	}

	public String getDataBatchId() {
		return dataBatchId;
	}
	public void setDataBatchId(String dataBatchId) {
		this.dataBatchId = dataBatchId;
	}
	public int getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	public String toString() {
		return "EventStatusMessage [data_batch_id=" + dataBatchId + ", code=" + attributeId + ", value="
				+ attributeValue + "]";
	}
}
