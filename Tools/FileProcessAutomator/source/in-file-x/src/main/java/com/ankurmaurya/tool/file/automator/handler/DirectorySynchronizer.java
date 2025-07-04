
package com.ankurmaurya.tool.file.automator.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.ankurmaurya.tool.file.automator.dto.FileAttribute;
import com.ankurmaurya.tool.file.automator.dto.FileDetail;
import com.ankurmaurya.tool.file.automator.dto.FileEqualityCheckType;
import com.ankurmaurya.tool.file.automator.dto.FolderDetail;
import com.ankurmaurya.tool.file.automator.utils.Utility;



public class DirectorySynchronizer {
	
	private File leftRootDirectory;
	private File rightRootDirectory;
	
	private Set<String> excludeFolders;
	private Set<String> excludeFoldersWithPattern;
	private Set<String> excludeFiles;
	private Set<String> excludeFilesWithPattern;
	
	private FileEqualityCheckType fileEqualityCheckType;
	
	private boolean confirmFileEqualityByHashing;
	private boolean printFileSynchronizationTag;
	
	private FolderDetail scannedFolderDetail;
	
	
	public DirectorySynchronizer(File leftRootDirectory, File rightRootDirectory, 
			Set<String> excludeFolders, Set<String> excludeFoldersWithPattern,
			Set<String> excludeFiles, Set<String> excludeFilesWithPattern,
			FileEqualityCheckType fileEqualityCheckType,
			boolean printFileSynchronizationTag) {
		
		super();	
		this.leftRootDirectory = leftRootDirectory;
		this.rightRootDirectory = rightRootDirectory;
		
		this.excludeFolders = excludeFolders;
		this.excludeFoldersWithPattern = excludeFoldersWithPattern;
		this.excludeFiles = excludeFiles;
		this.excludeFilesWithPattern = excludeFilesWithPattern;

		this.confirmFileEqualityByHashing = confirmFileEqualityByHashing;
		this.printFileSynchronizationTag = printFileSynchronizationTag;
	}


	public void setPrintFileSynchronizationTag(boolean printFileSynchronizationTag) {
		this.printFileSynchronizationTag = printFileSynchronizationTag;
	}

	public FolderDetail getScannedFolderDetail() {
		return scannedFolderDetail;
	}



	public void scanAndEvaluateDirectoryDifference() {
		try {
			System.out.println("Scanning Left Directory - " + leftRootDirectory.getPath());
			System.out.println("Matching with Right Directory - " + rightRootDirectory.getPath());
			
			if(!leftRootDirectory.exists()) {
				System.out.println("Left Directory '" + leftRootDirectory.getPath() + "' does not exists.");
				return;
			}
			if(!rightRootDirectory.exists()) {
				System.out.println("Right Directory '" + rightRootDirectory.getPath() + "' does not exists.");
				return;
			}

			scannedFolderDetail = compareDirectories("", leftRootDirectory, rightRootDirectory);
			System.out.println("\n");
			System.out.println("Scanning completed and directory difference has been evaluated.");
		} catch (Exception e) {
			System.out.println("Exception scanAndEvaluateDirectoryDifference() :- " + e.toString());
		}
	}
	
	
	
	public void showDirectoriesDifferenceReport() {
		System.out.println("------- DIRECTORY DIFFERENCE REPORT ------- ");
		System.out.println("Following file and folder differences were found in Right Directory with respect to Left Directory.\n");
		displayDirectorySynchronizationDetails("", scannedFolderDetail);
	}
	
	
	public void showLeftDirectoryMissingFoldersFilesReport() {
		System.out.println("------- DIRECTORY FILES MISSING REPORT ------- ");
		System.out.println("Following file and folder were found missing(Deleted) in Left Directory with respect to Right Directory.\n");
		FolderDetail missingFolderDetail = matchTheDirectoryFileAttributes(scannedFolderDetail, List.of(FileAttribute.DELETED));
		displayDirectorySynchronizationDetails("", missingFolderDetail);
	}
	
	
	public void showLeftDirectoryNewFoldersFilesReport() {
		System.out.println("------- DIRECTORY NEW FILES REPORT ------- ");
		System.out.println("Following file and folder were found added(New) in Left Directory with respect to Right Directory.\n");
		FolderDetail newFolderDetail = matchTheDirectoryFileAttributes(scannedFolderDetail, List.of(FileAttribute.NEW));
		displayDirectorySynchronizationDetails("", newFolderDetail);
	}
	
	
	public void showLeftDirectoryModifiedFoldersFilesReport() {
		System.out.println("------- DIRECTORY FILES MODIFIED REPORT ------- ");
		System.out.println("Following file and folder were found changed(Modified) in Left Directory with respect to Right Directory.\n");
		FolderDetail modifiedFolderDetail = matchTheDirectoryFileAttributes(scannedFolderDetail, List.of(FileAttribute.MODIFIED));
		displayDirectorySynchronizationDetails("", modifiedFolderDetail);
	}
	
	
	public void showSourceDirectoryUnModifiedFoldersFilesReport() {
		System.out.println("------- DIRECTORY FILES UNMODIFIED REPORT ------- ");
		System.out.println("Following file and folder were found same(Equal) in Source Directory with respect to Destination Directory.\n");
		FolderDetail notChangedFolderDetail = matchTheDirectoryFileAttributes(scannedFolderDetail, List.of(FileAttribute.NOCHANGES));
		displayDirectorySynchronizationDetails("", notChangedFolderDetail);
	}
	
	
	public void updateRightDirectory() {
		System.out.println("------- UPDATE RIGHT DIRECTORY ------- ");
		System.out.println("Copying only New and Updated files/folders to the Right Directory\n");
		synchronizeDifferenceInDirectories(scannedFolderDetail, List.of(FileAttribute.NEW, FileAttribute.MODIFIED), 
				                           leftRootDirectory, rightRootDirectory);
	}
	
	
	public void mirrorRightDirectoryWithLeft() {
		System.out.println("------- MIRROR RIGHT DIRECTORY ------- ");
		System.out.println("Copy New, Updated files/folders and Delete the missing files/folders in the Right Directory\n");
		synchronizeDifferenceInDirectories(scannedFolderDetail, List.of(FileAttribute.NEW, FileAttribute.MODIFIED, FileAttribute.DELETED), 
				                           leftRootDirectory, rightRootDirectory);
	}
	
	
	
	private void synchronizeDifferenceInDirectories(FolderDetail folderDetail, List<FileAttribute> fileAttributes,
			File leftDirectory, File rightDirectory) {

		// Synchronize the Sub-Folders Difference
		for (FolderDetail subFolderDetail : folderDetail.getFolderDetails()) {
			File leftSubDirectory = new File(leftDirectory, subFolderDetail.getFolderName());
			File rightSubDirectory = new File(rightDirectory, subFolderDetail.getFolderName());

			if (!rightSubDirectory.exists()) {
				System.out.println("Creating New Folder in Right Directory : " + rightSubDirectory.getPath());
				boolean dirCreated = rightSubDirectory.mkdirs();
				System.out.println("New Folder Created : " + dirCreated);
			}

			synchronizeDifferenceInDirectories(subFolderDetail, fileAttributes, leftSubDirectory, rightSubDirectory);
		}

		// Synchronize the Files Difference
		for (FileDetail fileDetail : folderDetail.getFileDetails()) {
			FileAttribute fileAttr = fileDetail.getFileAttribute();
			if (!isMatchesWithAnyFileAttributes(fileAttr, fileAttributes)) {
				continue;
			}

			File leftFile = new File(leftDirectory, fileDetail.getFileName());
			File rightFile = new File(rightDirectory, fileDetail.getFileName());
			try {
				if (fileAttr.equals(FileAttribute.NEW) && leftFile.exists()) {
					System.out.print("Copying New File : " + leftFile.getName());
					Files.copy(leftFile.toPath(), rightFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					if (rightFile.exists()) {
						System.out.println(", Status : SUCCESS");
					} else {
						System.out.println(", Status : FAILED");
					}
				} else if (fileAttr.equals(FileAttribute.MODIFIED) && leftFile.exists()) {
					System.out.print("Updating File : " + leftFile.getName());
					Files.copy(leftFile.toPath(), rightFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					if (rightFile.exists()) {
						System.out.println(", Status : SUCCESS");
					} else {
						System.out.println(", Status : FAILED");
					}
				} else if (fileAttr.equals(FileAttribute.DELETED) && !leftFile.exists() && rightFile.exists()) {
					System.out.print("Deleting File : " + rightFile.getName());
					boolean rfDeleted = rightFile.delete();
					if (rfDeleted && !rightFile.exists()) {
						System.out.println(", Status : SUCCESS");
					} else {
						System.out.println(", Status : FAILED");
					}
				}
			} catch (Exception e) {
				System.out.println("Exception Copying file :- " + e.toString());
			}
		}

		// Delete Folder which does not exists in Left Directory but exists in Right Directory
		if (!leftDirectory.exists() && rightDirectory.exists()
				&& folderDetail.getFolderAttribute().equals(FileAttribute.DELETED)
				&& isMatchesWithAnyFileAttributes(FileAttribute.DELETED, fileAttributes)) {
			System.out.print("Deleting Folder : " + leftDirectory.getName());
			boolean rfldDeleted = rightDirectory.delete();
			if (rfldDeleted && !rightDirectory.exists()) {
				System.out.println(", Status : SUCCESS");
			} else {
				System.out.println(", Status : FAILED");
			}
		}

	}
	
	
	
	private FolderDetail matchTheDirectoryFileAttributes(FolderDetail folderDetail, List<FileAttribute> fileAttributes) {
		FolderDetail matchedFolderDetail = new FolderDetail(folderDetail.getFolderName(), 0);
		if(folderDetail.getFolderAttribute()!=null) {
		  matchedFolderDetail.setFolderAttribute(folderDetail.getFolderAttribute());
		}

		//Match the Directory Folder Attribute based on it Sub-Folders Attribute
		for(FolderDetail subFolderDetail : folderDetail.getFolderDetails()) {
			FolderDetail possibleMisingFolderDetails = matchTheDirectoryFileAttributes(subFolderDetail, fileAttributes);
			if(possibleMisingFolderDetails != null) {
				matchedFolderDetail.getFolderDetails().add(possibleMisingFolderDetails);
				long folderSize = matchedFolderDetail.getFolderSize() + possibleMisingFolderDetails.getFolderSize();
				matchedFolderDetail.setFolderSize(folderSize);
			}
		}
		
		//Match the Directory File Attribute based on it Files Attribute
		for(FileDetail fileDetail : folderDetail.getFileDetails()) {
			FileAttribute fileAttr = fileDetail.getFileAttribute();
			
			if(!isMatchesWithAnyFileAttributes(fileAttr, fileAttributes)) { 
				continue;
			}
			
			FileDetail missingFileDetail = new FileDetail();
			missingFileDetail.setFileName(fileDetail.getFileName());
			missingFileDetail.setFileSize(fileDetail.getFileSize());
			missingFileDetail.setLastModified(fileDetail.getLastModified());
			missingFileDetail.setFileAttribute(fileDetail.getFileAttribute());

			long misingFileSize = matchedFolderDetail.getFolderSize() + missingFileDetail.getFileSize();
			matchedFolderDetail.setFolderSize(misingFileSize);
			
			matchedFolderDetail.getFileDetails().add(missingFileDetail);
		}
		
		//Do not return details of folder which does not match the given FileAttribute condition
		if(matchedFolderDetail.getFileDetails().isEmpty() && 
			matchedFolderDetail.getFolderDetails().isEmpty() &&
			!isMatchesWithAnyFileAttributes(matchedFolderDetail.getFolderAttribute(), fileAttributes)) {
			matchedFolderDetail = null;
		}

		return matchedFolderDetail;
	}
	
	
	
	private boolean isMatchesWithAnyFileAttributes(FileAttribute fileAttribute, List<FileAttribute> fileAttributesToBeMatched) {
		boolean attributesMatched = false;
		for(FileAttribute fileAttributeToBeMatched : fileAttributesToBeMatched) {
			if(fileAttributeToBeMatched.equals(fileAttribute)) {
				attributesMatched = true;
				break;
			}
		}
		return attributesMatched;
	}

	
	
	private FolderDetail compareDirectories(String indent, File leftFolder, File rightFolder) {
		FolderDetail folderDetail = null;
		try {
			System.out.println("");
			System.out.print(indent + leftFolder.getName());

			int fileCntr = 0;
			int batchCntr = 0;
			folderDetail = new FolderDetail(leftFolder.getName(), 0);

			File[] leftFiles = getListFilesOrderByFilesFirst(leftFolder);
			File[] rightFiles = getListFilesOrderByFilesFirst(rightFolder);
			if(rightFiles == null) {
				folderDetail.setFolderAttribute(FileAttribute.NEW);
			}
			if(leftFiles == null) {
				folderDetail.setFolderAttribute(FileAttribute.DELETED);
			}
			
			if (leftFolder.exists() && leftFiles != null) {
				System.out.print(">>> ");
			} else {
				System.out.print("<<< ");
			}
			

			if (isFolderToBeExcluded(leftFolder.getName())){
				System.out.print("X");
				return folderDetail;
			}
			
			// SCAN FOR ALL THE FILES IN SOURCE FOLDER
			if (leftFolder.exists() && leftFiles != null) {
				for (File leftFile : leftFiles) {
					String rightFilePathWithoutRoot = leftFile.getPath().replace(leftFolder.getPath(), "");
					File rightFile = new File(rightFolder, rightFilePathWithoutRoot);
					if (leftFile.isDirectory()){
						continue;
					}
					if (isFileToBeExcluded(leftFile.getName())){
						System.out.print("X");
						continue;
					}

					if (batchCntr == 50) {
						System.out.println("(" + fileCntr + ")");
						System.out.print(indent);
						batchCntr = 0;
					}

					FileDetail fileDetail = new FileDetail();
					fileDetail.setFileName(leftFile.getName());
					fileDetail.setFileSize(leftFile.length());
					fileDetail.setLastModified(leftFile.lastModified());

					long filesSize = folderDetail.getFolderSize() + fileDetail.getFileSize();
					folderDetail.setFolderSize(filesSize);
					
					if (!rightFile.exists()) {
						System.out.print("+");
						fileDetail.setFileAttribute(FileAttribute.NEW);
					} else if (leftFile.length() != rightFile.length()) {
						System.out.print("*");
						fileDetail.setFileAttribute(FileAttribute.MODIFIED);
					} else {
						boolean filesAreEqual;
						if(FileEqualityCheckType.HASHING.equals(fileEqualityCheckType)) {
							//Check if the two files are equal by file data Hash
							filesAreEqual = isTwoFilesHashesAreEqual(leftFile, rightFile);
						} 
						else if(FileEqualityCheckType.END_BYTES_BUFFER.equals(fileEqualityCheckType)) {
							//Check if the two files ends are equal
							filesAreEqual = isTwoFilesEndBytesAreEqual(leftFile, rightFile);
						} 
						else {
							//Check if the two files are equal by there size (length)
							filesAreEqual = leftFile.length() == rightFile.length();
						}
						
						if (filesAreEqual) {
							System.out.print("=");
						} else {
							System.out.print("*");
							fileDetail.setFileAttribute(FileAttribute.MODIFIED);
						}
					}

					folderDetail.getFileDetails().add(fileDetail);
					batchCntr++;
					fileCntr++;
				}
			}

			// SCAN FOR ALL THE FILES IN DESTINATION FOLDER
			if (rightFolder.exists() && rightFiles != null) {
				fileCntr = 0;
				batchCntr = 0;
				for (File rightFile : rightFiles) {
					if (rightFile.isDirectory()){
						continue;
					}
					
					String leftFilePathWithoutRoot = rightFile.getPath().replace(rightFolder.getPath(), "");
					File leftFile = new File(leftFolder, leftFilePathWithoutRoot);
					if(leftFile.exists()) {
						continue;
					}
					
					if (isFileToBeExcluded(leftFile.getName())){
						System.out.print("X");
						continue;
					}

					if (batchCntr == 50) {
						System.out.println("(" + fileCntr + ")");
						System.out.print(indent);
						batchCntr = 0;
					}

					FileDetail fileDetail = new FileDetail();
					fileDetail.setFileName(rightFile.getName());
					fileDetail.setFileSize(rightFile.length());
					fileDetail.setLastModified(rightFile.lastModified());
					fileDetail.setFileAttribute(FileAttribute.DELETED);
					folderDetail.getFileDetails().add(fileDetail);
					System.out.print("-");
					batchCntr++;
					fileCntr++;
					
					//Add the file size to parent folder only if the parent folder does not exists in Source folder
					if(folderDetail.getFolderAttribute() != null && 
					   folderDetail.getFolderAttribute().equals(FileAttribute.DELETED)) {
					  long filesSize = folderDetail.getFolderSize() + fileDetail.getFileSize();
					  folderDetail.setFolderSize(filesSize);
					}
				}
			}

			// SCAN FOR ALL THE SUB-FOLDERS IN SOURCE FOLDER
			if (leftFolder.exists() && leftFiles != null) {
				for (File leftFile : leftFiles) {
					String rightFilePathWithoutRoot = leftFile.getPath().replace(leftFolder.getPath(), "");
					File rightFile = new File(rightFolder, rightFilePathWithoutRoot);

					if (!leftFile.isDirectory()) {
						continue;
					}

					FolderDetail subFolderDetail = compareDirectories(indent + "| ", leftFile, rightFile);

					if (!rightFile.exists()) {
						folderDetail.setFolderAttribute(FileAttribute.NEW);
					}
					if (subFolderDetail != null) {
						folderDetail.getFolderDetails().add(subFolderDetail);
						long foldersSize = folderDetail.getFolderSize() + subFolderDetail.getFolderSize();
						folderDetail.setFolderSize(foldersSize);
					}
				}
			}

			// SCAN FOR ALL THE SUB-FOLDERS IN DESTINATION FOLDER
			if (rightFolder.exists() && rightFiles != null) {
				for (File rightFile : rightFiles) {
					String leftFilePathWithoutRoot = rightFile.getPath().replace(rightFolder.getPath(), "");
					File leftFile = new File(leftFolder, leftFilePathWithoutRoot);
					
					if (!rightFile.isDirectory() || leftFile.exists()) {
						continue;
					}
					
					FolderDetail subFolderDetail = compareDirectories(indent + "| ", leftFile, rightFile);
					subFolderDetail.setFolderAttribute(FileAttribute.DELETED);
					
					if (subFolderDetail != null) {
						folderDetail.getFolderDetails().add(subFolderDetail);
						//Add the file size to parent folder only if the parent folder does not exists in Source folder
						if(folderDetail.getFolderAttribute() != null && 
						   folderDetail.getFolderAttribute().equals(FileAttribute.DELETED)) {
							long foldersSize = folderDetail.getFolderSize() + subFolderDetail.getFolderSize();
							folderDetail.setFolderSize(foldersSize);
						}
					}
				}
			}
			
			//Assign Modified Attribute to Folder based on it Files Attribute
			for(FileDetail fileDetail : folderDetail.getFileDetails()) {
				FileAttribute fileAttr = fileDetail.getFileAttribute();
				if(fileAttr != null && (fileAttr.equals(FileAttribute.DELETED) || 
				   fileAttr.equals(FileAttribute.MODIFIED) || 
				   fileAttr.equals(FileAttribute.NEW))){
					folderDetail.setFolderAttribute(FileAttribute.MODIFIED);
					break;
				}
			}
			//Assign Modified Attribute to Folder based on it Sub-Folders Attribute
			for(FolderDetail subFolderDetail : folderDetail.getFolderDetails()) {
				FileAttribute folderAttr = subFolderDetail.getFolderAttribute();
				if(folderAttr != null && (folderAttr.equals(FileAttribute.DELETED) || 
				   folderAttr.equals(FileAttribute.MODIFIED) || 
				   folderAttr.equals(FileAttribute.NEW))){
					folderDetail.setFolderAttribute(FileAttribute.MODIFIED);
					break;
				}
			}
			
		} catch (Exception e) {
			System.out.println("Exception compareDirectories() :- " + e.toString());
		}
		return folderDetail;
	}
	
	
	
	private boolean isTwoFilesEndBytesAreEqual(File leftFile, File rightFile) {
		boolean bytesAreEqual = false;
		try {
			byte[] leftFileEndBytes = Utility.getFileEndByteBuffer(leftFile.getPath());
			byte[] rightFileEndBytes = Utility.getFileEndByteBuffer(rightFile.getPath());
			bytesAreEqual = Arrays.equals(leftFileEndBytes, rightFileEndBytes) && 
					       leftFile.length() == rightFile.length();
		} catch (Exception e) {
			System.out.println("Exception isTwoFilesEndBytesAreEqual() :- " + e.toString());
		}
		return bytesAreEqual;
	}
	
	
	
	private boolean isTwoFilesHashesAreEqual(File leftFile, File rightFile) {
		boolean bytesAreEqual = false;
		try {
			String leftFileChksum = Utility.generateFileChecksum(leftFile.getPath());
			String rightFileChksum = Utility.generateFileChecksum(rightFile.getPath());
			bytesAreEqual = leftFileChksum.equals(rightFileChksum);
		} catch (Exception e) {
			System.out.println("Exception isTwoFilesHashesAreEqual() :- " + e.toString());
		}
		return bytesAreEqual;
	}
	
	
	
	private void displayDirectorySynchronizationDetails(String indentSpace, FolderDetail folderDetail) {
		if(folderDetail == null) {
			System.out.println("No Directory matched the criteria.");
			return;
		}
		
		
		long deletedFiles = folderDetail.getFileDetails().stream()
				.filter(e-> e.getFileAttribute() != null && FileAttribute.DELETED.equals(e.getFileAttribute())).count();
		long newFiles = folderDetail.getFileDetails().stream()
				.filter(e-> e.getFileAttribute() != null && FileAttribute.NEW.equals(e.getFileAttribute())).count();
		long modifiedFiles = folderDetail.getFileDetails().stream()
				.filter(e-> e.getFileAttribute() != null && FileAttribute.MODIFIED.equals(e.getFileAttribute())).count();
		long notChangedFile = folderDetail.getFileDetails().size() - (newFiles + modifiedFiles + deletedFiles);
		
		
		String fileStats = "";
		String folderStats = indentSpace;
		if(FileAttribute.NEW.equals(folderDetail.getFolderAttribute())) {
			folderStats+="[+]";
		} else if(FileAttribute.DELETED.equals(folderDetail.getFolderAttribute())) {
			folderStats+="[-]";
		} else if(FileAttribute.MODIFIED.equals(folderDetail.getFolderAttribute())) {
			folderStats+="[*]";
		} else {
			folderStats+="[=]";
		}
		folderStats+=folderDetail.getFolderName();
		folderStats+=" (" + Utility.convertToReadableBytes(folderDetail.getFolderSize()) +") ";

		if(!folderDetail.getFileDetails().isEmpty()) {
			fileStats = "{Files: New(+)" + newFiles + ", Modified(*)" + modifiedFiles + ", Equal(=)" + notChangedFile + ", Deleted(-)" + deletedFiles + "}";
		}
		System.out.println(folderStats + fileStats);
		
		
		if(printFileSynchronizationTag) {
			for(FileDetail fileDetail : folderDetail.getFileDetails()) {
				String fileDiffTag = "| ";
				if(FileAttribute.NEW.equals(fileDetail.getFileAttribute())) {
					fileDiffTag += "(+)";
				} else if(FileAttribute.MODIFIED.equals(fileDetail.getFileAttribute())) {
					fileDiffTag += "(*)";
				} else if(FileAttribute.DELETED.equals(fileDetail.getFileAttribute())) {
					fileDiffTag += "(-)";
				} else {
					fileDiffTag += "(=)";
				}
				System.out.println(indentSpace + fileDiffTag + fileDetail.getFileName() + " (" + Utility.convertToReadableBytes(fileDetail.getFileSize())  + ")");
			}
		}

		for(FolderDetail subFolderDetail : folderDetail.getFolderDetails()) {
			displayDirectorySynchronizationDetails(indentSpace + "| ", subFolderDetail);
		}
	}
	
	
	/*
	private File[] getListFilesOrderByFilesFirst(File[] files) {
		List<File> fileList = new ArrayList<>();
		List<File> folderList = new ArrayList<>();
		
		if(files == null) {
			return files;
		}
		
		for (File file : files) {
			
			if (file.isDirectory()) {
				folderList.add(file);
			} else {
				fileList.add(file);
			}
		}
		fileList.addAll(folderList);
		File[] orderedFiles = new File[fileList.size()];
		return fileList.toArray(orderedFiles);
	}
	*/
	
	private File[] getListFilesOrderByFilesFirst(File parentFolder) throws IOException {
		List<File> fileList = new ArrayList<>();
		List<File> folderList = new ArrayList<>();
		
		if(!parentFolder.exists() || !parentFolder.isDirectory()) {
			return null;
		}
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(parentFolder.toPath())) {
			int a = 1;
	        for (Path path : stream) {
	        	System.out.print(a);
	            if (Files.isDirectory(path)) {
	            	folderList.add(path.toFile());
	            	System.out.println(" D");
	            } else {
	            	fileList.add(path.toFile());
	            	System.out.println(" F");
	            }
	            a++;
	        }
	    }
		fileList.addAll(folderList);
		File[] orderedFiles = new File[fileList.size()];
		return fileList.toArray(orderedFiles);
	}
	
	
	
	private boolean isFileToBeExcluded(String fileName) {
		boolean fileShouldBeExcluded = false;
		if(!excludeFiles.isEmpty()) {
			fileShouldBeExcluded = excludeFiles.contains(fileName);
		}
		if(!excludeFilesWithPattern.isEmpty() && !fileShouldBeExcluded) {
			fileShouldBeExcluded = excludeFilesWithPattern.stream().anyMatch(e->Utility.matchesPattern(fileName, e));
		}
		return fileShouldBeExcluded;
	}
	
	
	
	private boolean isFolderToBeExcluded(String folderName) {
		boolean folderShouldBeExcluded = false;
		if(!excludeFolders.isEmpty()) {
			folderShouldBeExcluded = excludeFolders.contains(folderName);
		}
		if(!excludeFoldersWithPattern.isEmpty() && !folderShouldBeExcluded) {
			folderShouldBeExcluded = excludeFoldersWithPattern.stream().anyMatch(e->Utility.matchesPattern(folderName, e));
		}
		return folderShouldBeExcluded;
	}
	
	
	
}







