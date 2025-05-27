package com.ankurmaurya.tool.file.automator.dto;

public class KeywordExtractorMeta {

	private String keyword;
	private String searchDirection;
	private String headerName;
	private char occurence;
	
	
	public KeywordExtractorMeta(String keyword, String searchDirection, String headerName, char occurence) {
		super();
		this.keyword = keyword;
		this.searchDirection = searchDirection;
		this.headerName = headerName;
		this.occurence = occurence;
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getSearchDirection() {
		return searchDirection;
	}


	public void setSearchDirection(String searchDirection) {
		this.searchDirection = searchDirection;
	}


	public String getHeaderName() {
		return headerName;
	}


	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}


	public char getOccurence() {
		return occurence;
	}


	public void setOccurence(char occurence) {
		this.occurence = occurence;
	}
	

	
}



