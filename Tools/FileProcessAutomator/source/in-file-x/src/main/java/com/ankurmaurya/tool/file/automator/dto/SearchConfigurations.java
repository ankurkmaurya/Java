package com.ankurmaurya.tool.file.automator.dto;

import java.util.List;

public class SearchConfigurations {

	private String searchMode;
	private String searchTargetKeyword;
	private String searchAreaUpperBoundLinesFromTarget;
	private String searchAreaLowerBoundLinesFromTarget;
	private String outputRemoveStringInFileName;
	
	private List<KeywordExtractorMeta> keywordExtractorMetas;
	
	
	public String getSearchMode() {
		return searchMode;
	}
	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}
	
	
	public String getSearchTargetKeyword() {
		return searchTargetKeyword;
	}
	public void setSearchTargetKeyword(String searchTargetKeyword) {
		this.searchTargetKeyword = searchTargetKeyword;
	}
	
	
	public String getSearchAreaUpperBoundLinesFromTarget() {
		return searchAreaUpperBoundLinesFromTarget;
	}
	public void setSearchAreaUpperBoundLinesFromTarget(String searchAreaUpperBoundLinesFromTarget) {
		this.searchAreaUpperBoundLinesFromTarget = searchAreaUpperBoundLinesFromTarget;
	}
	
	
	public String getSearchAreaLowerBoundLinesFromTarget() {
		return searchAreaLowerBoundLinesFromTarget;
	}
	public void setSearchAreaLowerBoundLinesFromTarget(String searchAreaLowerBoundLinesFromTarget) {
		this.searchAreaLowerBoundLinesFromTarget = searchAreaLowerBoundLinesFromTarget;
	}
	
	
	public String getOutputRemoveStringInFileName() {
		return outputRemoveStringInFileName;
	}
	public void setOutputRemoveStringInFileName(String outputRemoveStringInFileName) {
		this.outputRemoveStringInFileName = outputRemoveStringInFileName;
	}
	
	
	public List<KeywordExtractorMeta> getKeywordExtractorMetas() {
		return keywordExtractorMetas;
	}
	public void setKeywordExtractorMetas(List<KeywordExtractorMeta> keywordExtractorMetas) {
		this.keywordExtractorMetas = keywordExtractorMetas;
	}
	
	
}






