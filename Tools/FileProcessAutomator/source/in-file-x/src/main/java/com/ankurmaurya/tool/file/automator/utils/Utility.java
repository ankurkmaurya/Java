

package com.ankurmaurya.tool.file.automator.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Utility {
	
	private Utility() {
	}
	
	
    //This function will read all lines of the given file
    public static List<String> getFileAllLine(String filePath) {
    	System.out.println("Reading File '" + filePath + "' lines.");
        List<String> fileLines = new ArrayList<>();
        File file = new File(filePath);
        try (FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);){
            String line;
            while ((line = br.readLine()) != null) {
                fileLines.add(line);
            }
        } catch (Exception e) {
        	System.out.println("Exception getFileAllLine() : " + e);
        }
        return fileLines;
    }
    
    
    
    //This function will save all lines in the given file path
    public static boolean saveFileAllLine(List<String> fileLines, String filePath, boolean appendToFile) {
        boolean fileSaved = false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, appendToFile));){
            for (String fileLine : fileLines) {
                bw.write(fileLine); //data line
                bw.newLine(); //new line
                bw.flush();
            }
            bw.flush();
            fileSaved = true;
        } catch (Exception e) {
        	System.out.println("Exception saveFileAllLine() : " + e.toString());
        }
        return fileSaved;
    }
    
    
    
    public static String convertToReadableBytes(long size) {
        String readableByte;
        if(size<0) {
        	//size = size * -1;
        }
        
        if (size >= 1073741824) {
        	readableByte = String.format("%.2f GB", ((double)size / 1073741824));
        } else if (size >= 1048576) {
        	readableByte = String.format("%.2f MB", ((double)size / 1048576));
        } else if (size >= 1024) {
        	readableByte = String.format("%.2f KB", ((double)size / 1024));
        } else {
            readableByte = size + " bytes";
        }
        return readableByte;
    }
    
    
    
    public static String generateFileChecksum(String filePath) {
        String checksum = "";
        try (FileInputStream fis = new FileInputStream(filePath);) {
        	MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dataBytes = new byte[1024];
            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }

            byte[] mdbytes = md.digest();
            //convert the byte to hex format
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            checksum = sb.toString();
        } catch (Exception e) {
        	System.out.println("Exception generateFileChecksum() : " + e.toString());
        }
        return checksum;
    }
    
    
    
    public static boolean matchesPattern(String inputStr, String pattern) {
        boolean matches = false;
        try {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(inputStr);
            matches = m.matches();
        } catch (Exception e) {
        	System.out.println("Exception matchesPattern() : " + e);
        }
        return matches;
    }

    
    
	
}






