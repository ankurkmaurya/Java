
package com.ankurmaurya.tool.file.automator.dto;

public class KeywordExtractorMetaTabular {

	private String keywordTblHdr;
	private int startIndex;
	private int endIndex;
	private String keywordValue;
	
	
	public KeywordExtractorMetaTabular(String keywordTblHdr) {
		super();
		this.keywordTblHdr = keywordTblHdr;
		this.startIndex = 0;
		this.endIndex = 0;
		this.keywordValue = "";
	}


	public String getKeywordTblHdr() {
		return keywordTblHdr;
	}

	public void setKeywordTblHdr(String keywordTblHdr) {
		this.keywordTblHdr = keywordTblHdr;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getKeywordValue() {
		return keywordValue;
	}

	public void setKeywordValue(String keywordValue) {
		this.keywordValue = keywordValue;
	}
	
	
}



