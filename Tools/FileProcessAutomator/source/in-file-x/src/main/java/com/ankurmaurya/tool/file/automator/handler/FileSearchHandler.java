

package com.ankurmaurya.tool.file.automator.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ankurmaurya.tool.file.automator.dto.KeywordExtractorMeta;
import com.ankurmaurya.tool.file.automator.dto.KeywordExtractorMetaTabular;
import com.ankurmaurya.tool.file.automator.utils.Utility;

public class FileSearchHandler {
	
	private FileSearchHandler() {
	}

	public static void searchKeywordsCountInFiles(String srcFolder, String[] keywords) {
		try {
			File searchFolder = new File(srcFolder);
			File searchReport = new File(searchFolder, "Search_Report.csv");

			//Save Header Line
			StringBuilder headerLine = new StringBuilder();
			headerLine.append("File Name").append(",");
			headerLine.append(List.of(keywords).stream().collect(Collectors.joining(",")));
			Utility.saveFileAllLine(List.of(headerLine.toString()), searchReport.getPath(), false);

			File[] searchFiles = searchFolder.listFiles();
			for (File searchFile : searchFiles) {
				if (searchFile.isDirectory()) {
					continue;
				}
				String srchFileName = searchFile.getName();
				int[] keywordCounter = new int[keywords.length];
				List<String> fileLines = Utility.getFileAllLine(searchFile.getPath());
				for (int k = 0; k < keywords.length; k++) {
					for(String fileLine : fileLines) {
						if(fileLine.contains(keywords[k])) {
							keywordCounter[k]=keywordCounter[k]+1;
						}
					}
				}
				
				//Save Data Line
				StringBuilder dataLine = new StringBuilder();
				dataLine.append(srchFileName).append(",");
				dataLine.append(Arrays.stream(keywordCounter).boxed().map(e->Integer.toString(e)).collect(Collectors.joining(",")));
				Utility.saveFileAllLine(List.of(dataLine.toString()), searchReport.getPath(), true);
			}
		} catch (Exception e) {
			System.out.println("Exception searchKeywordsCountInFiles() : " + e.toString());
		}
	}
	
	
	
	public static void extractKeywordsValueInFiles(String srcFolder, List<KeywordExtractorMeta> keywordExtractorMetas) {
		try {
			File searchFolder = new File(srcFolder);
			File searchReport = new File(searchFolder, "Extracted_Report.csv");
			File[] searchFiles = searchFolder.listFiles();
			
			
			//Save Header Line
			StringBuilder headerLine = new StringBuilder();
			headerLine.append("File Name").append(",");
			headerLine.append(keywordExtractorMetas.stream().map(e->e.getHeaderName()).collect(Collectors.joining(",")));
			Utility.saveFileAllLine(List.of(headerLine.toString()), searchReport.getPath(), false);

			for (File searchFile : searchFiles) {
				if (searchFile.isDirectory()) {
					continue;
				}
				String srchFileName = searchFile.getName();
				System.out.println("Extracting data from file - " + srchFileName);
				Map<String, String> extractedKeywordsData = new HashMap<>();

				//Search and Extract Data
				List<String> fileLines = Utility.getFileAllLine(searchFile.getPath());
				for(KeywordExtractorMeta keywordExtractorMeta : keywordExtractorMetas) {
					System.out.print("Searching Keyword - " + keywordExtractorMeta.getKeyword());
					String extractedData = extractKeywordValues(keywordExtractorMeta, fileLines);
					extractedKeywordsData.put(keywordExtractorMeta.getHeaderName(), extractedData);
					System.out.println(" Value - " + extractedData);
				}
				
				
				//Save Extracted Data
				StringBuilder dataLine = new StringBuilder();
				dataLine.append(srchFileName).append(",");
				dataLine.append(keywordExtractorMetas.stream().map(e-> extractedKeywordsData.get(e.getHeaderName())).collect(Collectors.joining(",")));
				Utility.saveFileAllLine(List.of(dataLine.toString()), searchReport.getPath(), true);
				
				System.out.println("");
			}
		} catch (Exception e) {
			System.out.println("Exception extractKeywordsValueInFiles() : " + e.toString());
		}
	}
	
	
	private static String extractKeywordValues(KeywordExtractorMeta keywordExtractorMeta, List<String> fileLines) {
		int lineSkip = 1;
		String keywordSearchDirection = keywordExtractorMeta.getSearchDirection();
		if(keywordSearchDirection.contains("H") || keywordSearchDirection.contains("h")) {
			String lineSkipStr = keywordSearchDirection.toLowerCase().replace("h", "");
			if(lineSkipStr.length()>0) {
				keywordSearchDirection = "H";
				lineSkip += Integer.parseInt(lineSkipStr);
			}
		}
		
		if(keywordSearchDirection.contains("T") || keywordSearchDirection.contains("t")) {
			String lineSkipStr = keywordSearchDirection.toLowerCase().replace("t", "");
			if(lineSkipStr.length()>0) {
				keywordSearchDirection = "T";
				lineSkip += Integer.parseInt(lineSkipStr);
			}
		}
		
		StringBuilder extractedDataSB = new StringBuilder();
		//To Extract Data from the tabular formatted lines as the keyword found (T)
		if(keywordSearchDirection.equals("t") || keywordSearchDirection.equals("T")) {
			String extData = extractTabularData(fileLines, keywordExtractorMeta, lineSkip);
			extractedDataSB.append(extData);
		} else {
			//To Extract Data from the lines as the keyword found (V/H)
			for(int i=0; i<fileLines.size(); i++) {
				String fileLine = fileLines.get(i);
				String extData = "";
				
				//To Extract Data from the same line as the keyword found
				if(fileLine.contains(keywordExtractorMeta.getKeyword()) 
						&& (keywordSearchDirection.equals("v") || keywordSearchDirection.equals("V"))) {
					extData = fileLine.substring(fileLine.indexOf(keywordExtractorMeta.getKeyword()));
					extData = extData.replace(keywordExtractorMeta.getKeyword(), "").trim();
					extractedDataSB.append(extData);
					if(keywordExtractorMeta.getOccurence()=='s' || keywordExtractorMeta.getOccurence()=='S') {
						break;
					} else {
						extractedDataSB.append(extData.length() > 0 ? "|" : "");
					}
				}
				
				//To Extract Data from the next line as the keyword found
				if(fileLine.contains(keywordExtractorMeta.getKeyword()) 
						&& (keywordSearchDirection.equals("h") || keywordSearchDirection.equals("H"))) {
					i += lineSkip;
					extData = fileLines.get(i).trim();
					extractedDataSB.append(extData);
					if(keywordExtractorMeta.getOccurence()=='s' || keywordExtractorMeta.getOccurence()=='S') {
						break;
					} else {
						extractedDataSB.append(extData.length() > 0 ? "|" : "");
					}
				}
			}
		}
		
		//Remove last Pipe '|' separator
		if(extractedDataSB.length() > 0 && extractedDataSB.charAt(extractedDataSB.length()-1) == '|') {
			extractedDataSB.deleteCharAt(extractedDataSB.length()-1);
		}
		
		return extractedDataSB.toString();
	}
	
	
	private static String extractTabularData(List<String> fileLines, KeywordExtractorMeta keywordExtractorMeta, int lineSkip) {
		StringBuilder extractedDataSB = new StringBuilder();
		List<KeywordExtractorMetaTabular> keywordExtractorMetaTabulars = new ArrayList<>();
		String keyWrdStr = keywordExtractorMeta.getKeyword();
		
		String[] keyWrds= null;
		if(keyWrdStr.contains("---")) {
			keyWrds = keyWrdStr.split("---");
		} else {
			keyWrds = new String[1];
			keyWrds[0] = keyWrdStr;
		}
		
		//Build the Tabular Keyword extractor 
		for(String keyWrd : keyWrds) {
			keywordExtractorMetaTabulars.add(new KeywordExtractorMetaTabular(keyWrd));
		}
		
		boolean tblHdrFound = false;
		//Find the point of keyword occurence and extract the tabular data below
		for(int i=0; i<fileLines.size(); i++) {
			String fileLine = fileLines.get(i);
			
			if(fileLine.length() == 0 && !tblHdrFound) {
				continue;
			}
			
			
			/*
			 * 
			 * Find the keyword Match Line and derive the start and end index 
			 * to extract the column data
			 * 
			 */
			String dataLine = fileLine;
			if(!tblHdrFound && keywordExtractorMetaTabulars.stream()
					.filter(e -> dataLine.contains(e.getKeywordTblHdr())).count() == keywordExtractorMetaTabulars.size()) {
				tblHdrFound = true;
				
				for(KeywordExtractorMetaTabular keywordExtractorMetaTabular : keywordExtractorMetaTabulars) {
					String tblHdr = keywordExtractorMetaTabular.getKeywordTblHdr();
					int tblHdrStartIndex  = fileLine.indexOf(tblHdr);
					keywordExtractorMetaTabular.setStartIndex(tblHdrStartIndex);
					
					char[] fileLineChars = fileLine.toCharArray();
					int possibleEndIndex = tblHdrStartIndex + tblHdr.length();
					for (int c = possibleEndIndex+1 ; c < fileLineChars.length; c++) {
						if(fileLineChars[c] != ' ') {
							break;
						}
						possibleEndIndex = c;
					}
					
					if(possibleEndIndex >= tblHdrStartIndex) {
					   keywordExtractorMetaTabular.setEndIndex(possibleEndIndex);
					}
				}
				
				i += lineSkip;
			}
			
			
			
			/*
			 * 
			 * Keyword match found (Tabular Header Found)
			 * Extract the keyword values from Tabular body
			 * 
			 */
			if(tblHdrFound && !keywordExtractorMetaTabulars.isEmpty()) {
				fileLine = fileLines.get(i);
				if(fileLine.length() == 0) {
					tblHdrFound = false;
					keywordExtractorMetaTabulars.clear();
					continue;
				}
				char[] fileLineChars = fileLine.toCharArray();
				
				for(KeywordExtractorMetaTabular keywordExtractorMetaTabular : keywordExtractorMetaTabulars) {
					String keyValue = new String(Arrays.copyOfRange(fileLineChars, keywordExtractorMetaTabular.getStartIndex(), keywordExtractorMetaTabular.getEndIndex()+1));
					keywordExtractorMetaTabular.setKeywordValue(keyValue);
					
					extractedDataSB.append(keywordExtractorMetaTabular.getKeywordTblHdr().trim())
					  .append(":")
					  .append(keywordExtractorMetaTabular.getKeywordValue().trim())
					  .append(";");
				}
				//Remove last Pipe ';' separator
				if(extractedDataSB.length() > 0 && extractedDataSB.charAt(extractedDataSB.length()-1) == ';') {
					extractedDataSB.deleteCharAt(extractedDataSB.length()-1);
				}
				
				extractedDataSB.append("|");
				
				if(keywordExtractorMeta.getOccurence()=='s' || keywordExtractorMeta.getOccurence()=='S') {
					break;
				}
			}
		}
		
		//Remove last Pipe '|' separator
		if(extractedDataSB.length() > 0 && extractedDataSB.charAt(extractedDataSB.length()-1) == '|') {
			extractedDataSB.deleteCharAt(extractedDataSB.length()-1);
		}
		return extractedDataSB.toString();
	}
	

	
}


