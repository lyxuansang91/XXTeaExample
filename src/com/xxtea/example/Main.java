package com.xxtea.example;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.xxtea.XXTEA;



public class Main {
	
	public static List<File> getAllFilesInFolder(String filePath) {
		List<File> results = new ArrayList<File>();
		try {
			File[] files = new File(filePath).listFiles();
			for(File file : files) {
				if(file.isFile()) {
					results.add(file);
				} else {
					results.addAll(getAllFilesInFolder(file.getAbsolutePath()));
				}
			}
			
		} catch(Exception ex) {
			System.err.println(ex.getMessage());
		}
		return results;
	}
	
	public static boolean encryptFile(File file, String fileName, String key) throws IOException {
		byte[] fileData = new byte[(int) file.length()];
	    //System.out.println(file.length());
	    DataInputStream dis = new DataInputStream(new FileInputStream(file));
	    dis.readFully(fileData);
	    dis.close();
	    
	    OutputStream os = new FileOutputStream(new File(file.getParent() + "/" + fileName + ".txt"));
//	    
	    byte[] encrypt_data = XXTEA.encrypt(fileData, key);
//	    System.out.println(encrypt_data.length);
	    os.write(encrypt_data);
	    os.close();
//	    
	    byte[] decrypt_data = XXTEA.decrypt(encrypt_data, key);
	    
	    return Arrays.equals(fileData, decrypt_data);
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//mã hóa một ảnh
		File file = new File("Resources/cen_play_icon.png"); //đường dẫn file
		if(file.isFile()) {
			boolean isEncrypted = encryptFile(file, "cen_play_icon", "HoangBac17891");
			System.out.println(isEncrypted);
		}
		
		//mã hóa cả folder
		
//		List<File> results = getAllFilesInFolder("Resources"); //đường dẫn thư mục
//		for(File result: results) {
//			String filePath = result.getName();
//			String extension = "";
//			String name = "";
//			String path = result.getParent();
//			int last_dot = filePath.lastIndexOf('.');
//			if(last_dot > 0) {
//				extension = filePath.substring(last_dot + 1);
//				name = filePath.substring(0, last_dot);
//				
//				if(extension.equalsIgnoreCase("png")) {
//					boolean isEncrypted = encryptFile(result, name, "1234567890"); 	
//					if(isEncrypted) {
//						result.delete();
//						//System.out.println(isEncrypted);
//					}
//				}
//			}
//		}
	}		

}
