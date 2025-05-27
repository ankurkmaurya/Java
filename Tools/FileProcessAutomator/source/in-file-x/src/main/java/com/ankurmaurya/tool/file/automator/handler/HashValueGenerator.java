

package com.ankurmaurya.tool.file.automator.handler;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import com.ankurmaurya.tool.file.automator.utils.Utility;

public class HashValueGenerator {
	
	private HashValueGenerator() {
		
	}
	
	public static void generateHashValueForFiles(String srcFolder) {
		try {
			List<String> filesHashList = new ArrayList<>();	
			File searchFolder = new File(srcFolder);
			File[] searchFiles = searchFolder.listFiles();
			File searchReport = new File(searchFolder, "File_Hash_Value.csv");
			
			//Save Header Line
			StringBuilder headerLine = new StringBuilder();
			headerLine.append("File Name").append(",");
			headerLine.append("File Path").append(",");
			headerLine.append("SHA256").append(",");
			headerLine.append("SHA1");
			Utility.saveFileAllLine(List.of(headerLine.toString()), searchReport.getPath(), false);

			for (File searchFile : searchFiles) {
				getFolderFilesHash(searchFile, filesHashList);
			}
			Utility.saveFileAllLine(filesHashList, searchReport.getPath(), true);
		} catch (Exception e) {
			System.out.println("Exception searchKeywordsInFiles() : " + e.toString());
		}
	}
	
	private static void getFolderFilesHash(File srcFile, List<String> filesHashList) {
		if(srcFile.isDirectory()) {
			for(File srcSubFile : srcFile.listFiles()) {
				getFolderFilesHash(srcSubFile, filesHashList);
			}
			return;
		}
		StringBuilder dataLine = new StringBuilder();
		dataLine.append(srcFile.getName()).append(",");
		dataLine.append(srcFile.getPath()).append(",");
		dataLine.append(getSHA256(srcFile)).append(",");
		dataLine.append(getSHA1(srcFile));
		filesHashList.add(dataLine.toString());
	}
	
	
	private static String getSHA256(File srcFile) {
		String checksum = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			checksum = checksum(srcFile.getPath(), md);
		} catch (Exception e) {
			System.out.println("Exception getSHA256() : " + e.toString());
		}
		return checksum;
	}
	
	private static String getSHA1(File srcFile) {
		String checksum = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			checksum = checksum(srcFile.getPath(), md);
		} catch (Exception e) {
			System.out.println("Exception getSHA1() : " + e.toString());
		}
		return checksum;
	}
	
	private static String checksum(String filepath, MessageDigest md) {
		try {
			// file hashing with DigestInputStream
			try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
				while (dis.read() != -1)
					; // empty loop to clear the data
				md = dis.getMessageDigest();
			}
			
			// bytes to hex
			StringBuilder result = new StringBuilder();
			for (byte b : md.digest()) {
				result.append(String.format("%02x", b));
			}
			return result.toString();

		} catch (Exception e) {
			System.out.println("Exception checksum() : " + e.toString());
		}
		return "";
	}
	
	

}
