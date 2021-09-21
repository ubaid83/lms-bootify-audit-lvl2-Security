package com.spts.lms.web.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;

import com.spts.lms.beans.library.Library;

@Service
public class UnzipFiles {

	/*
	 * public static void main(String[] args) { String zipFilePath =
	 * "E:/BA (HONS) Liberal Arts RE EXAM.zip";
	 * 
	 * String destDir = "E:/BA (HONS) Liberal Arts RE EXAM/";
	 * 
	 * unzip(zipFilePath, destDir); }
	 */
	public boolean unzip(String zipFilePath, String destDir) {
		boolean isSuccess = false;
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				isSuccess = true;
				ze = zis.getNextEntry();
				
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;

	}

}