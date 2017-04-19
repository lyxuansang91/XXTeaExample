package com.xxtea.example;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.xxtea.XXTEA;

public class Main {

	public static List<File> getAllFilesInFolder(String filePath) {
		List<File> results = new ArrayList<File>();
		try {
			File[] files = new File(filePath).listFiles();
			for (File file : files) {
				if (file.isFile()) {
					results.add(file);
				} else {
					results.addAll(getAllFilesInFolder(file.getAbsolutePath()));
				}
			}

		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		return results;
	}

	public static boolean encryptFile(File file, String fileName, String key) throws IOException {
		byte[] fileData = new byte[(int) file.length()];
		// System.out.println(file.length());
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(fileData);
		dis.close();

		OutputStream os = new FileOutputStream(new File(file.getParent() + "/" + fileName + ".txt"));
		//
		byte[] encrypt_data = XXTEA.encrypt(fileData, key);
		// System.out.println(encrypt_data.length);
		os.write(encrypt_data);
		os.close();
		//
		byte[] decrypt_data = XXTEA.decrypt(encrypt_data, key);

		return Arrays.equals(fileData, decrypt_data);
	}

	public static boolean decryptFile(File file, String fileName, String key) {
		try {
			byte[] fileData = new byte[(int) file.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
			dis.readFully(fileData);
			dis.close();
			byte[] decrypt_data = XXTEA.decrypt(fileData, key);
			if (decrypt_data == null)
				return false;

			System.out.println("decrypt data length:" + decrypt_data.length);
			OutputStream os = new FileOutputStream(new File(file.getParent() + "/" + fileName + ".png"));
			os.write(decrypt_data);
			os.close();
			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		// return false;

	}

	public static List<String> getAllFileName(String fileName) {
		List<String> result = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(new FileInputStream(fileName));
			while (sc.hasNextLine()) {
				String next_line = sc.nextLine();
				// System.out.println(next_line);
				result.add(next_line);
			}
			sc.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return result;

	}

	public static boolean inBlackListFile(String rs, List<String> blackListFileNames) {
		for (String fileName : blackListFileNames) {
			if (rs.equalsIgnoreCase(fileName))
				return true;
		}
		return false;
	}

	public static String changeToMacroStyle(String filePath, String extension) {
		String result = filePath; 
		int last_dot = filePath.lastIndexOf('.');
		String name = ""; 
		if(last_dot > 0) name = filePath.substring(0, last_dot); 
		
		if(extension.equalsIgnoreCase("txt") || extension.equalsIgnoreCase("png")) {
			result = name; 
		}
		result = result.replace("/", "_").replace(".", "_").toUpperCase();
		return String.format("#define %s \"%s\"", result, filePath);
	}

	public static void generateImageH() throws IOException {
		
		List<File> results = getAllFilesInFolder("Resources"); // đường dẫn thư
																// mục
		BufferedWriter bw = new BufferedWriter(new FileWriter("Image.h"));
		bw.write("#ifndef Image_h\n");
		bw.write("#define Image_h\n\n\n");

		for (File result : results) {
			String filePath = result.getName();
			String path = result.getParent();
			String extension = "";
			String name = "";
			int last_dot = filePath.lastIndexOf('.');
			if (last_dot > 0) {
				extension = filePath.substring(last_dot + 1);
				name = filePath.substring(0, last_dot);
				
				String rs = String.format("%s/%s.%s", path, name, extension);
				rs = rs.replace("C:\\Users\\sang\\workspace\\XXTeaExample\\Resources\\", "");
				rs = rs.replace("Resources/", "");
				rs = rs.replace("\\", "/");
				String macro_rs = changeToMacroStyle(rs, extension);
				bw.write(macro_rs);
				bw.newLine();
				System.out.println(macro_rs);

			}
		}
		bw.write("\n#endif /* Image_h */");
		bw.close();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// mã hóa một ảnh
		// File file = new File("Resources/cen_play_icon.png"); //đường dẫn file
		// if(file.isFile()) {
		// boolean isEncrypted = encryptFile(file, "cen_play_icon",
		// "HoangBac17891");
		// System.out.println(isEncrypted);
		// }

		// mã hóa cả folder

		generateImageH();

		// List<String> blacklistFileNames = getAllFileName("blacklist.txt");
		//
		// for(File result: results) {
		// String filePath = result.getName();
		// String extension = "";
		// String name = "";
		// String path = result.getParent();
		// int last_dot = filePath.lastIndexOf('.');
		// if(last_dot > 0) {
		// extension = filePath.substring(last_dot + 1);
		// name = filePath.substring(0, last_dot);
		//
		// if(extension.equalsIgnoreCase("png")) {
		// String rs = path + "/" + name + ".png";
		// rs =
		// rs.replace("C:\\Users\\sang\\workspace\\XXTeaExample\\Resources\\",
		// "");
		// rs = rs.replace("Resources/", "");
		// rs = rs.replace("\\", "/");
		// if(!inBlackListFile(rs, blacklistFileNames)) {
		// //not in black list
		// boolean isEncrypted = encryptFile(result, name, "com.game.imon");
		// if(isEncrypted) {
		// result.delete();
		// }
		// }
		// }
		// }
		// }
	}
}
