package com.viewscenes.util;


public class RpcApiInvokeParam {
	
	private String url;
	
	private String contentType;
	
	private String chartSet;
	
	private Object postData;
	
	public RpcApiInvokeParam(String url){
		this(url, null);
	}
	
	public RpcApiInvokeParam(String url,Object postData){
		this.url = url;
		this.postData = postData;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getChartSet() {
		return chartSet;
	}

	public void setChartSet(String chartSet) {
		this.chartSet = chartSet;
	}

	public Object getPostData() {
		return postData;
	}

	public void setPostData(Object postData) {
		this.postData = postData;
	}

}
