
package com.ankurmaurya.tool.file.automator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.ankurmaurya.tool.file.automator.dto.FileEqualityCheckType;
import com.ankurmaurya.tool.file.automator.dto.KeywordExtractorMeta;
import com.ankurmaurya.tool.file.automator.handler.DirectorySynchronizer;
import com.ankurmaurya.tool.file.automator.handler.FileSearchHandler;
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
				System.out.println("4 : Find and Synchronize the Difference between 2 directories.");
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
		System.out.println("Provide source folder where plain/text files exists.");
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
		System.out.println("Provide source folder of files to generate Checksum.");
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
		System.out.println("Provide source folder where plain/text files exists.");
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
		System.out.println("Provide Left(source) directory path to be evaluated.");
		String leftFolderPath = scanner.nextLine();
		System.out.println("Provide Right(destination) directory path whose difference need to be compared with Left directory.");
		String rightFolderPath = scanner.nextLine();

		System.out.println("Provide Folder-Name of any folder that need to be excluded from scanning.");
		System.out.println("Folder-Name with pattern can also be included just provide pattern name including (.*) pattern sequence.");
		System.out.println("(Values : FolderName1,FolderName2,FolderName3,FolderPattern1.*,.*FolderPattern2,.*FolderPattern3.*)");
		String excludeFoldersSeq = scanner.nextLine();

		System.out.println("Provide File-Name of any file that need to be excluded from scanning.");
		System.out.println("File-Name with pattern can also be included just provide pattern name including (.*) pattern sequence.");
		System.out.println("(Values : FileName1,FileName2,FileName3,FilePattern1.*,.*FilePattern2,.*FilePattern3.*)");
		String excludeFilesSeq = scanner.nextLine();

		System.out.println("Select File Equality Check method, Methods are Hashing or End Data Buffer or File Length. (Value : Y[Yes], N[No])");
		System.out.println("(Values : H[Hashing], EDB[End Data Buffer], FL[File Length]");
		String fileEqualityCheckMethod = scanner.nextLine(); 
		
		System.out.println("Do you want to print File Synchronization Tag while displaying report. (Value : Y[Yes], N[No])");
		String printFileSynchTag = scanner.nextLine();
		try {
			FileEqualityCheckType fileEqualityCheckType;
			if (fileEqualityCheckMethod.equalsIgnoreCase("H")) {
				fileEqualityCheckType = FileEqualityCheckType.HASHING;
			} else if (fileEqualityCheckMethod.equalsIgnoreCase("EDB")) {
				fileEqualityCheckType = FileEqualityCheckType.END_BYTES_BUFFER;
			} else {
				fileEqualityCheckType = FileEqualityCheckType.FILE_LENGTH;
			}
			
			boolean printFileSynchronizationTag = false;
			if (printFileSynchTag.equalsIgnoreCase("Y")) {
				printFileSynchronizationTag = true;
			}

			File leftFolder = new File(leftFolderPath);
			File rightFolder = new File(rightFolderPath);
			
			String[] excludeFoldersArray;
			Set<String> excludeFolders = new HashSet<>();
			Set<String> excludeFoldersWithPattern = new HashSet<>();
			if (excludeFoldersSeq.contains(",")) {
				excludeFoldersArray = excludeFoldersSeq.split(",");
			} else {
				excludeFoldersArray = new String[1];
				excludeFoldersArray[0] = excludeFoldersSeq;
			}
			for (String excludeFolder : excludeFoldersArray) {
				if (excludeFolder.contains(".*")) {
					excludeFoldersWithPattern.add(excludeFolder);
				} else {
					excludeFolders.add(excludeFolder);
				}
			}
			
			String[] excludeFilesArray;
			Set<String> excludeFiles = new HashSet<>();
			Set<String> excludeFilesWithPattern = new HashSet<>();
			if (excludeFilesSeq.contains(",")) {
				excludeFilesArray = excludeFilesSeq.split(",");
			} else {
				excludeFilesArray = new String[1];
				excludeFilesArray[0] = excludeFilesSeq;
			}
			for (String excludeFile : excludeFilesArray) {
				if (excludeFile.contains(".*")) {
					excludeFilesWithPattern.add(excludeFile);
				} else {
					excludeFiles.add(excludeFile);
				}
			}

			DirectorySynchronizer directorySynchronizer = new DirectorySynchronizer(leftFolder, rightFolder,
					excludeFolders, excludeFoldersWithPattern, excludeFiles, excludeFilesWithPattern,
					fileEqualityCheckType, printFileSynchronizationTag);
			directorySynchronizer.scanAndEvaluateDirectoryDifference();

			if (directorySynchronizer.getScannedFolderDetail() == null) {
				return;
			}

			String folderOption;
			do {
				System.out.println("");
				System.out.println("------------------------- DIRECTORY SCAN OPTIONS -------------------------");
				System.out.println("Choose option to manage difference between Left and Right directories ");
				System.out.println("a : Show File Synchronization Tag.");
				System.out.println("b : Hide File Synchronization Tag.");
				System.out.println("c : Show Right Directory Difference Report.");
				System.out.println("d : Show Left Directory missing folders & files Report.");
				System.out.println("e : Show Left Directory new folders & files Report.");
				System.out.println("f : Show Left Directory modified folders & files Report.");
				System.out.println("g : Show Left Directory unmodified folders & files Report.");
				System.out.println("h : Update (Copy only New and Updated files to the Right Directory).");
				System.out.println("i : Mirror (Create a mirror backup of the Left Directory by adapting the Right Directory to match).");
				System.out.println("z : Exit");
				System.out.println("------------------------- DIRECTORY SCAN OPTIONS -------------------------");
				System.out.println("");

				folderOption = scanner.nextLine();
				switch (folderOption) {
				case "a":
					directorySynchronizer.setPrintFileSynchronizationTag(true);
					break;
				case "b":
					directorySynchronizer.setPrintFileSynchronizationTag(false);
					break;
				case "c":
					directorySynchronizer.showDirectoriesDifferenceReport();
					break;
				case "d":
					directorySynchronizer.showLeftDirectoryMissingFoldersFilesReport();
					break;
				case "e":
					directorySynchronizer.showLeftDirectoryNewFoldersFilesReport();
					break;
				case "f":
					directorySynchronizer.showLeftDirectoryModifiedFoldersFilesReport();
					break;
				case "g":
					directorySynchronizer.showSourceDirectoryUnModifiedFoldersFilesReport();
					break;
				case "h":
					directorySynchronizer.updateRightDirectory();
					break;
				case "i":
					directorySynchronizer.mirrorRightDirectoryWithLeft();
					break;
				default:
					if (!"z".equals(folderOption)) {
						System.out.println("Invalid Choice!");
					}
					break;
				}
			} while (!"z".equals(folderOption));

		} catch (Exception e) {
			System.out.println("Exception option4Handler() : " + e.toString());
		}
	}
	
	
	
	
	
	
	
	

}
