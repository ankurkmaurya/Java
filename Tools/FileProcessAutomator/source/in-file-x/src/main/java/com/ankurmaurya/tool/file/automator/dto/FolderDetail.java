package com.ankurmaurya.tool.file.automator.dto;

import java.util.ArrayList;
import java.util.List;

public class FolderDetail {

	private String folderName;
	private long folderSize;
	
	private FileAttribute folderAttribute;
	private List<FileDetail> fileDetails;
	private List<FolderDetail> folderDetails;
	
	
	
	public FolderDetail(String folderName, long folderSize) {
		super();
		this.folderName = folderName;
		this.folderSize = folderSize;
		
		this.folderAttribute = FileAttribute.NOCHANGES;
		this.fileDetails = new ArrayList<>();
		this.folderDetails = new ArrayList<>();
	}
	
	

	public String getFolderName() {
		return folderName;
	}
	
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	
	
	public long getFolderSize() {
		return folderSize;
	}
	
	public void setFolderSize(long folderSize) {
		this.folderSize = folderSize;
	}
	
	
	
	public List<FileDetail> getFileDetails() {
		return fileDetails;
	}
	
	public void setFileDetails(List<FileDetail> fileDetails) {
		this.fileDetails = fileDetails;
	}
	
	
	
	public List<FolderDetail> getFolderDetails() {
		return folderDetails;
	}
	
	public void setFolderDetails(List<FolderDetail> folderDetails) {
		this.folderDetails = folderDetails;
	}
	
	
	
	public FileAttribute getFolderAttribute() {
		return folderAttribute;
	}

	public void setFolderAttribute(FileAttribute folderAttribute) {
		this.folderAttribute = folderAttribute;
	}

	
}



