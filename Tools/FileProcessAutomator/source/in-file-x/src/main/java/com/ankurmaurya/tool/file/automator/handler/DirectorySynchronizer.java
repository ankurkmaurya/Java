
package com.ankurmaurya.tool.file.automator.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ankurmaurya.tool.file.automator.dto.FileAttribute;
import com.ankurmaurya.tool.file.automator.dto.FileDetail;
import com.ankurmaurya.tool.file.automator.dto.FolderDetail;
import com.ankurmaurya.tool.file.automator.utils.Utility;



public class DirectorySynchronizer {
	
	private File srcRootDirectory;
	private File destRootDirectory;
	private boolean printFileSynchronizationTag;

	private FolderDetail scannedFolderDetail;
	
	
	public DirectorySynchronizer(File srcRootDirectory, File destRootDirectory, boolean printFileSynchronizationTag) {
		super();	
		this.srcRootDirectory = srcRootDirectory;
		this.destRootDirectory = destRootDirectory;
		this.printFileSynchronizationTag = printFileSynchronizationTag;
		
	}

	
	public FolderDetail getScannedFolderDetail() {
		return scannedFolderDetail;
	}



	public void scanAndEvaluateDirectoryDifference() {
		try {
			System.out.println("Scanning Source Directory - " + srcRootDirectory.getPath());
			System.out.println("Matching with Destination Directory - " + destRootDirectory.getPath());
			
			if(!srcRootDirectory.exists()) {
				System.out.println("Source Directory '" + srcRootDirectory.getPath() + "' does not exists.");
				return;
			}
			if(!destRootDirectory.exists()) {
				System.out.println("Destination Directory '" + destRootDirectory.getPath() + "' does not exists.");
				return;
			}

			scannedFolderDetail = compareDirectories("", srcRootDirectory, destRootDirectory);
			System.out.println("\n");
			System.out.println("Scanning completed and directory difference has been evaluated.");
		} catch (Exception e) {
			System.out.println("Exception scanAndEvaluateDirectoryDifference() :- " + e.toString());
		}
	}
	
	
	
	public void showDirectoriesDifferenceReport() {
		System.out.println("------- DIRECTORY DIFFERENCE REPORT ------- ");
		System.out.println("Following file and folder differences were found in Destination Directory with respect to Source Directory.\n");
		displayDirectorySynchronizationDetails("", scannedFolderDetail);
	}
	
	
	public void showSourceDirectoryMissingFoldersFilesReport() {
		System.out.println("------- DIRECTORY FILES MISSING REPORT ------- ");
		System.out.println("Following file and folder were found missing(Deleted) in Source Directory with respect to Destination Directory.\n");
		FolderDetail missingFolderDetail = matchTheDirectoryFileAttributes(scannedFolderDetail, List.of(FileAttribute.DELETED));
		displayDirectorySynchronizationDetails("", missingFolderDetail);
	}
	
	
	public void showSourceDirectoryNewFoldersFilesReport() {
		System.out.println("------- DIRECTORY NEW FILES REPORT ------- ");
		System.out.println("Following file and folder were found added(New) in Source Directory with respect to Destination Directory.\n");
		FolderDetail newFolderDetail = matchTheDirectoryFileAttributes(scannedFolderDetail, List.of(FileAttribute.NEW));
		displayDirectorySynchronizationDetails("", newFolderDetail);
	}
	
	
	public void showSourceDirectoryModifiedFoldersFilesReport() {
		System.out.println("------- DIRECTORY FILES MODIFIED REPORT ------- ");
		System.out.println("Following file and folder were found changed(Modified) in Source Directory with respect to Destination Directory.\n");
		FolderDetail modifiedFolderDetail = matchTheDirectoryFileAttributes(scannedFolderDetail, List.of(FileAttribute.MODIFIED));
		displayDirectorySynchronizationDetails("", modifiedFolderDetail);
	}
	
	
	public void showSourceDirectoryUnModifiedFoldersFilesReport() {
		System.out.println("------- DIRECTORY FILES UNMODIFIED REPORT ------- ");
		System.out.println("Following file and folder were found same(Equal) in Source Directory with respect to Destination Directory.\n");
		FolderDetail notChangedFolderDetail = matchTheDirectoryFileAttributes(scannedFolderDetail, List.of(FileAttribute.NOCHANGES));
		displayDirectorySynchronizationDetails("", notChangedFolderDetail);
	}
	
	
	private FolderDetail matchTheDirectoryFileAttributes(FolderDetail folderDetail, List<FileAttribute> fileAttributes) {
		FolderDetail matchedFolderDetail = new FolderDetail(folderDetail.getFolderName(), 0);
		matchedFolderDetail.setFolderAttribute(folderDetail.getFolderAttribute());
		
		//Assign Modified Attribute to Folder based on it Sub-Folders Attribute
		for(FolderDetail subFolderDetail : folderDetail.getFolderDetails()) {
			FolderDetail possibleMisingFolderDetails = matchTheDirectoryFileAttributes(subFolderDetail, fileAttributes);
			if(possibleMisingFolderDetails != null) {
				//matchedFolderDetail.setFolderAttribute(possibleMisingFolderDetails.getFolderAttribute());
				matchedFolderDetail.getFolderDetails().add(possibleMisingFolderDetails);
				long folderSize = matchedFolderDetail.getFolderSize() + possibleMisingFolderDetails.getFolderSize();
				matchedFolderDetail.setFolderSize(folderSize);
			}
		}
		
		//Assign Modified Attribute to Folder based on it Files Attribute
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

	
	
	private FolderDetail compareDirectories(String indent, File srcFolder, File destFolder) {
		FolderDetail folderDetail = null;
		try {
			System.out.println("");
			System.out.print(indent + srcFolder.getName());

			int fileCntr = 0;
			folderDetail = new FolderDetail(srcFolder.getName(), 0);

			File[] srcFiles = getListFilesOrderByFilesFirst(srcFolder.listFiles());
			File[] destFiles = getListFilesOrderByFilesFirst(destFolder.listFiles());
			if(destFiles == null) {
				folderDetail.setFolderAttribute(FileAttribute.NEW);
			}
			if(srcFiles == null) {
				folderDetail.setFolderAttribute(FileAttribute.DELETED);
			}
			
			if (srcFolder.exists() && srcFiles != null) {
				System.out.print(">>> ");
			} else {
				System.out.print("<<< ");
			}
			
			
			// SCAN FOR ALL THE FILES IN SOURCE FOLDER
			if (srcFolder.exists() && srcFiles != null) {
				for (File srcFile : srcFiles) {
					String destFilePathWithoutRoot = srcFile.getPath().replace(srcFolder.getPath(), "");
					File destFile = new File(destFolder, destFilePathWithoutRoot);

					if (!srcFile.isDirectory()) {
						if (fileCntr == 50) {
							System.out.println();
							System.out.print(indent);
							fileCntr = 0;
						}

						FileDetail fileDetail = new FileDetail();
						fileDetail.setFileName(srcFile.getName());
						fileDetail.setFileSize(srcFile.length());
						fileDetail.setLastModified(srcFile.lastModified());

						long filesSize = folderDetail.getFolderSize() + fileDetail.getFileSize();
						folderDetail.setFolderSize(filesSize);

						if (destFile.exists()) {
							if (srcFile.length() == destFile.length()) {
								String srcFileChksum = Utility.generateFileChecksum(srcFile.getPath());
								String destFileChksum = Utility.generateFileChecksum(destFile.getPath());
								if (!srcFileChksum.equals(destFileChksum)) {
									System.out.print("*");
									fileDetail.setFileAttribute(FileAttribute.MODIFIED);
								} else {
									System.out.print("=");
								}
								fileDetail.setFileHash(srcFileChksum);
							} else {
								System.out.print("*");
								fileDetail.setFileAttribute(FileAttribute.MODIFIED);
							}
						} else {
							System.out.print("+");
							fileDetail.setFileAttribute(FileAttribute.NEW);
						}
						folderDetail.getFileDetails().add(fileDetail);
						fileCntr++;
					}
				}
			}

			// SCAN FOR ALL THE FILES IN DESTINATION FOLDER
			if (destFolder.exists() && destFiles != null) {
				fileCntr = 0;
				for (File destFile : destFiles) {
					if (!destFile.isDirectory()) {
						String srcFilePathWithoutRoot = destFile.getPath().replace(destFolder.getPath(), "");
						File srcFile = new File(srcFolder, srcFilePathWithoutRoot);
						if(srcFile.exists()) {
							continue;
						}
	
						if (fileCntr == 50) {
							System.out.println();
							System.out.print(indent);
							fileCntr = 0;
						}

						FileDetail fileDetail = new FileDetail();
						fileDetail.setFileName(destFile.getName());
						fileDetail.setFileSize(destFile.length());
						fileDetail.setLastModified(destFile.lastModified());
						fileDetail.setFileAttribute(FileAttribute.DELETED);
						folderDetail.getFileDetails().add(fileDetail);
						System.out.print("-");
						fileCntr++;
						
						//Add the file size to parent folder only if the parent folder does not exists in Source folder
						if(folderDetail.getFolderAttribute() != null && 
						   folderDetail.getFolderAttribute().equals(FileAttribute.DELETED)) {
						  long filesSize = folderDetail.getFolderSize() + fileDetail.getFileSize();
						  folderDetail.setFolderSize(filesSize);
						}
						
					}
				}
			}

			// SCAN FOR ALL THE SUB-FOLDERS IN SOURCE FOLDER
			if (srcFolder.exists() && srcFiles != null) {
				for (File srcFile : srcFiles) {
					String destFilePathWithoutRoot = srcFile.getPath().replace(srcFolder.getPath(), "");
					File destFile = new File(destFolder, destFilePathWithoutRoot);

					if (srcFile.isDirectory()) {
						FolderDetail subFolderDetail = compareDirectories(indent + "| ", srcFile, destFile);

						if (!destFile.exists()) {
							folderDetail.setFolderAttribute(FileAttribute.NEW);
						}
						if (subFolderDetail != null) {
							folderDetail.getFolderDetails().add(subFolderDetail);
							long foldersSize = folderDetail.getFolderSize() + subFolderDetail.getFolderSize();
							folderDetail.setFolderSize(foldersSize);
						}
					}
				}
			}

			// SCAN FOR ALL THE SUB-FOLDERS IN DESTINATION FOLDER
			if (destFolder.exists() && destFiles != null) {
				for (File destFile : destFiles) {
					String srcFilePathWithoutRoot = destFile.getPath().replace(destFolder.getPath(), "");
					File srcFile = new File(srcFolder, srcFilePathWithoutRoot);

					if (destFile.isDirectory() && !srcFile.exists()) {
						FolderDetail subFolderDetail = compareDirectories(indent + "| ", srcFile, destFile);
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
	
	
	
	private void displayDirectorySynchronizationDetails(String indentSpace, FolderDetail folderDetail) {
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
	
	

}



