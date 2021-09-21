package com.spts.lms.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnzipUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(UnzipUtil.class);

	/*private static final String INPUT_ZIP_FILE = "G:\\ipFiles\\ArchiveFile_NMMPSMU_1718_CL_FMBAGEN_07_0B_04.zip";
	private static final String OUTPUT_FOLDER = "G:\\outputzip\\ArchiveFile_NMMPSMU_1718_CL_FMBAGEN_07_0B_04";

	public static void main(String[] args) {
		UnzipUtil unZip = new UnzipUtil();
		// unZip.unZipIt(INPUT_ZIP_FILE, OUTPUT_FOLDER);
		unZip.unzip(INPUT_ZIP_FILE, OUTPUT_FOLDER);
	}*/

	/**
	 * Unzip it
	 * 
	 * @param zipFile
	 *            input zip file
	 * @param output
	 *            zip file output folder
	 */
	public void unZipIt(String zipFile, String outputFolder) {

		ZipInputStream zis = null;
		ZipEntry ze = null;

		try {
			zis = new ZipInputStream(new FileInputStream(zipFile));

			ze = zis.getNextEntry();

			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zip file content

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator
						+ fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				IOUtils.copy(zis, fos);

				IOUtils.closeQuietly(fos);
				ze = zis.getNextEntry();
			}

			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (zis != null)
				try {
					zis.closeEntry();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			IOUtils.closeQuietly(zis);
		}
	}

	public static void unzip(String source, String destination) {

		//String password = "password";
		logger.info("Source File" + source);
		try {
			ZipFile zipFile = new ZipFile(source);
			/*if (zipFile.isEncrypted()) {
				zipFile.setPassword(password);
			}*/
			zipFile.extractAll(destination);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		logger.info("Done" + source);
	}

}