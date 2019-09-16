package com.hit.memoryunits;

import java.io.Serializable;

@SuppressWarnings("serial")
	public class Page<T> implements Serializable {
		
	private T content;
	private Long pageId;
	
	public Page(Long pageId, T content){
		
		this.content=content;
		this.pageId=pageId;
	}
	
	public Long getPageId() {
		return pageId;
	}
	
	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}
	
	public T getContent() {
		return content;
	}
	
	public void setContent(T content) {
		this.content = content;
	}
	
	public int hashCode(){
		return this.pageId.hashCode();
	}
	
	public boolean equals(Object obj){
		if (this.pageId.equals(obj)) return true;
		return false;
	}
	
	public String toString(){
		return this.content.toString();
	}
}
