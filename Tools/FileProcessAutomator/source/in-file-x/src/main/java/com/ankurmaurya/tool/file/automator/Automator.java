
package com.ankurmaurya.tool.file.automator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ankurmaurya.tool.file.automator.dto.KeywordExtractorMeta;
import com.ankurmaurya.tool.file.automator.handler.FileSearchHandler;
import com.ankurmaurya.tool.file.automator.handler.DirectorySynchronizer;
import com.ankurmaurya.tool.file.automator.handler.HashValueGenerator;
import com.ankurmaurya.tool.file.automator.utils.Utility;

public class Automator {

	
	public static void main(String[] args) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);
			String menuOption;
			do {
				System.out.println("\n");
				System.out.println("------------------------- MODE MENU -------------------------");
				System.out.println("Choose!");
				System.out.println("1 : Find Keywords occurence Count in plain/text files.");
				System.out.println("2 : Generate Hash Value (SHA1,SHA256) for files.");
				System.out.println("3 : Find and Extract Keywords value from plain/text files.");
				System.out.println("4 : Find Difference between 2 folders.");
				System.out.println("5 : Exit");
				System.out.println("------------------------- MODE MENU -------------------------");

				menuOption =  scanner.nextLine();
				switch (menuOption) {
				case "1": 
					option1Handler(scanner);
					break;
				case "2": 
					option2Handler(scanner);
					break;
				case "3": 
					option3Handler(scanner);
					break;
				case "4": 
					option4Handler(scanner);
					break;
				default: 
					if(!"5".equals(menuOption)) {
						System.out.println("Invalid Choice!");
					}
                   break;
			    }
			} while (!"5".equals(menuOption));

		} catch (Exception e) {
			System.out.println("Exception main() : " + e.toString());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
	
	
	public static void option1Handler(Scanner scanner) {
		System.out.println("Please provide source folder where plain/text files exists.");
		String srcFolder = scanner.nextLine();
		System.out.println("Provide No. of keywords to be searched in files. (Value : min - 1, max - 10)");
		try {
			int keywordsCnt = Integer.parseInt(scanner.nextLine());
			String[] keywords = new String[keywordsCnt];
			for (int i = 0; i < keywords.length; i++) {
				System.out.println("Please Keyword - " + (i+1));
				keywords[i] = scanner.nextLine();
			}
			System.out.println("Searching Keywords in files, Please Wait....");
			FileSearchHandler.searchKeywordsCountInFiles(srcFolder, keywords);
			System.out.println("Searching Keywords Completed.");
		} catch (Exception e) {
			System.out.println("Exception option1Handler() : " + e.toString());
		}
	}
	
	public static void option2Handler(Scanner scanner) {
		System.out.println("Please provide source folder of files to generate Checksum.");
		String srcFolder = scanner.nextLine();
		try {
			System.out.println("Generating Checksum for files, Please Wait....");
			HashValueGenerator.generateHashValueForFiles(srcFolder);
			System.out.println("Generating Hash Value for files Completed.");
		} catch (Exception e) {
			System.out.println("Exception option2Handler() : " + e.toString());
		}
	}
	
	public static void option3Handler(Scanner scanner) {
		System.out.println("Please provide source folder where plain/text files exists.");
		String srcFolder = scanner.nextLine();
		try {
			List<KeywordExtractorMeta> keywordExtractorMetas = new ArrayList<>();
			//Skip keyword Meta Input File first line
			List<String> keywordInputLines = Utility.getFileAllLine(new File ("./Input/keywords-metadata.txt").getPath()).stream().skip(1).toList();
			System.out.println("Keyword Extractor Metas Raw Found - " + keywordInputLines.size());
			for(String keywordInputLine : keywordInputLines) {
				if(!keywordInputLine.contains("~")) {
					continue;
				}
				
				String[] keywordInputLineSplt = keywordInputLine.split("~");
				KeywordExtractorMeta keywordExtractorMeta = new KeywordExtractorMeta(keywordInputLineSplt[0], 
						keywordInputLineSplt[1], 
						keywordInputLineSplt[2], 
						keywordInputLineSplt[3].toCharArray()[0]);
				
				keywordExtractorMetas.add(keywordExtractorMeta);
			}
			System.out.println("Keyword Extractor Metas Build - " + keywordExtractorMetas.size());
			System.out.println("Searching Keywords in files, Please Wait....");
			FileSearchHandler.extractKeywordsValueInFiles(srcFolder, keywordExtractorMetas);
			System.out.println("Searching Keywords Completed.");
		} catch (Exception e) {
			System.out.println("Exception option3Handler() : " + e.toString());
		}
	}
	
	public static void option4Handler(Scanner scanner) {
		System.out.println("Please provide base(source) directory path to be evaluated.");
		String srcFolderPath = scanner.nextLine();
		System.out.println("Please provide destination directory path whose difference need to be compared with base directory.");
		String destFolderPath = scanner.nextLine();
		System.out.println("Do you want to print File Synchronization Tag while displaying report. (Value : Y[Yes[, N[No])");
		String printFileSynchTag = scanner.nextLine();
		try {
			boolean printFileSynchronizationTag = false;
			if(printFileSynchTag.equalsIgnoreCase("y")) {
				printFileSynchronizationTag = true;
			}
			
			File srcFolder = new File(srcFolderPath);
			File destFolder = new File(destFolderPath);

			DirectorySynchronizer directorySynchronizer = new DirectorySynchronizer(srcFolder, destFolder, printFileSynchronizationTag);
			directorySynchronizer.scanAndEvaluateDirectoryDifference();
			
			if(directorySynchronizer.getScannedFolderDetail() == null) {
				return;
			}
			
			String folderOption;
			do {
				System.out.println("");
				System.out.println("------------------------- DIRECTORY SCAN OPTIONS -------------------------");
				System.out.println("Choose option to manage files in Folders ");
				System.out.println("a : Show Destination Directory Difference Report.");
				System.out.println("b : Show Source Directory missing folders & files Report.");
				System.out.println("c : Show Source Directory new folders & files Report.");
				System.out.println("d : Show Source Directory modified folders & files Report.");
				System.out.println("e : Show Source Directory unmodified folders & files Report.");
				System.out.println("f : Exit");
				System.out.println("------------------------- DIRECTORY SCAN OPTIONS -------------------------");
				System.out.println("");
				
				folderOption =  scanner.nextLine();
				switch (folderOption) {
				case "a": 
					directorySynchronizer.showDirectoriesDifferenceReport();
					break;
				case "b": 
					directorySynchronizer.showSourceDirectoryMissingFoldersFilesReport();
					break;
				case "c": 
					directorySynchronizer.showSourceDirectoryNewFoldersFilesReport();
					break;
				case "d": 
					directorySynchronizer.showSourceDirectoryModifiedFoldersFilesReport();
					break;
				case "e": 
					directorySynchronizer.showSourceDirectoryUnModifiedFoldersFilesReport();
					break;
				default:
					if(!"f".equals(folderOption)) {
						System.out.println("Invalid Choice!");
					}
	               break;
			    }
			} while (!"f".equals(folderOption));
			
		} catch (Exception e) {
			System.out.println("Exception option4Handler() : " + e.toString());
		}
	}
	
	
	
	
	
	
	
	

}
