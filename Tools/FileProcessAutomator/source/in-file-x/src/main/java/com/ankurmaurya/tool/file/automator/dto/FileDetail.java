package com.ankurmaurya.tool.file.automator.dto;

public class FileDetail {

	private String fileName;
	private long fileSize;
	private long lastModified;
	private String fileHash;
	private FileAttribute fileAttribute;
	

	public FileDetail() {
		super();
		this.fileAttribute = FileAttribute.NOCHANGES;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public long getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	
	public String getFileHash() {
		return fileHash;
	}
	
	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}
	
	
	public FileAttribute getFileAttribute() {
		return fileAttribute;
	}

	public void setFileAttribute(FileAttribute fileAttribute) {
		this.fileAttribute = fileAttribute;
	}


}



