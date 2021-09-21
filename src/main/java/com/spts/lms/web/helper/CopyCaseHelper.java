package com.spts.lms.web.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.assignment.ResultDomain;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.user.UserService;


@Component
public class CopyCaseHelper {

	@Value("${lms.assignment.submissionFolder}")
	private String StudentAssignment_FILES_PATH;

	@Value("${lms.assignment.copyCaseFolder}")
	private String copyCaseFolder;
	
	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;
	
	@Autowired
	AmazonS3ClientService amazonS3ClientService;
	
	@Autowired
	UserService userService;

	private final int MIN_LENGTH_FOR_COMPARISION = 30;

	private static final Logger logger = Logger.getLogger(CopyCaseHelper.class);

	@Async
	public String checkCopyCases(
			List<StudentAssignment> studentAssignmentFilesList,
			StudentAssignment searchBean, StudentAssignment questionFileBean)
			throws Exception {
		
		String assignmentName = searchBean.getAssignmentName();

		ArrayList<List<String>> allFileContentsList = new ArrayList<>();
		int count = 1;
		
		ArrayList<StudentAssignment> nonPDFFilesStudentsList = new ArrayList<>();
		//01-05-2020 Start
		ArrayList<String> questionFileContent = new ArrayList<String>();
		logger.info("questionFileBean.getFilePath--->"+questionFileBean.getFilePath());
		File tempCopyFolder = new File(downloadAllFolder + "/" + "copyCase"+assignmentName); 
		if (!tempCopyFolder.exists()) {
			tempCopyFolder.mkdirs();
		}
		if(questionFileBean.getFilePath().contains(",")) {
			String[] filePathArray = questionFileBean.getFilePath().split(","); 
			for(String s: filePathArray) {
				String questFile = s.replace("/\\", "/");
				questFile = s.replace("\\\\", "/");
				questFile = s.replace("\\", "/");
				questFile = s.replace("//", "/");
				File questionFile = new File(questFile);
				InputStream inpStream = amazonS3ClientService.getFileByFullPath(questFile);
				File questionFileTemp = new File(tempCopyFolder+"/"+questionFile.getName());
				FileUtils.copyInputStreamToFile(inpStream, questionFileTemp);
				ArrayList<String> questionFileLineContent = (ArrayList<String>) readLineByLine(
						null, tempCopyFolder+"/"+questionFile.getName(), new ArrayList<String>());
				if(null != questionFileLineContent)
					questionFileContent.addAll(questionFileLineContent);
			}
		}else{
			String questFile = questionFileBean.getFilePath().replace("/\\", "/");
			questFile = questionFileBean.getFilePath().replace("\\\\", "/");
			questFile = questionFileBean.getFilePath().replace("\\", "/");
			questFile = questionFileBean.getFilePath().replace("//", "/");
			File questionFile = new File(questFile);
			InputStream inpStream = amazonS3ClientService.getFileByFullPath(questFile);
			File questionFileTemp = new File(tempCopyFolder+"/"+questionFile.getName());
			FileUtils.copyInputStreamToFile(inpStream, questionFileTemp);
			ArrayList<String> questionFileLineContent = (ArrayList<String>) readLineByLine(
					null, tempCopyFolder+"/"+questionFile.getName(), new ArrayList<String>());
			//questionFileContent = questionFileLineContent;
			if(null != questionFileLineContent)
				questionFileContent.addAll(questionFileLineContent);
		}

		for (StudentAssignment studentAssignment : studentAssignmentFilesList) {
			long readStartTime = System.currentTimeMillis();
			String studentSubmittedFile = studentAssignment.getStudentFilePath();
			if(studentSubmittedFile.endsWith(".pdf") || studentSubmittedFile.endsWith(".docx")) {
				studentSubmittedFile = studentSubmittedFile.replace("/\\", "/");
				studentSubmittedFile = studentSubmittedFile.replace("\\\\", "/");
				studentSubmittedFile = studentSubmittedFile.replace("\\", "/");
				studentSubmittedFile = studentSubmittedFile.replace("//", "/");
				File studentFile = new File(studentSubmittedFile);
				InputStream inpStream = amazonS3ClientService.getFileByFullPath(studentSubmittedFile);
				File studentFileTemp = new File(tempCopyFolder+"/"+studentFile.getName());
				FileUtils.copyInputStreamToFile(inpStream, studentFileTemp);
				List<String> fileContent = readLineByLine(null,
						tempCopyFolder+"/"+studentFile.getName(), questionFileContent);
				long readEndTime = System.currentTimeMillis();
				logger.info("fileContent--->"+fileContent);
				if (fileContent != null) {
					allFileContentsList.add(fileContent);
				} else {
					allFileContentsList.add(new ArrayList<String>());
				}
			}else {
				nonPDFFilesStudentsList.add(studentAssignment);
			}
			
		}
		//FileUtils.deleteDirectory(tempCopyFolder);
		//01-05-2020 End
		HashMap<String, String> studentWithCopyAboveThreshold = new HashMap<>();
		HashMap<String, String> studentsAbove90 = new HashMap<>();
		HashMap<String, String> studentsBetween80And90 = new HashMap<String, String>();
		ArrayList<ResultDomain> copyResultList = new ArrayList<>();
	
		for (int firstIndex = 0; firstIndex < allFileContentsList.size(); firstIndex++) {
			StudentAssignment currentStudentStudentAssignment = studentAssignmentFilesList
					.get(firstIndex);
			// List<String> firstPdf = readLineByLine(null,
			// currentStudentStudentAssignment.getStudentFilePath());
			
			List<String> firstPdf = allFileContentsList.get(firstIndex);
			int numberOfLinesInFirstFile = firstPdf.size();
			
			// if (numberOfLinesInFirstFile != 0) {

			for (int secondIndex = firstIndex + 1; secondIndex < allFileContentsList
					.size(); secondIndex++) {
				int maxConseutiveLinesMatched = 0;
				int consecutiveMatchingLinesCounter = 0;

				
				StudentAssignment otherStudentStudentAssignment = studentAssignmentFilesList
						.get(secondIndex);
				// List<String> secondPdf = readLineByLine(null,
				// otherStudentStudentAssignment.getStudentFilePath());
				List<String> secondPdf = allFileContentsList.get(secondIndex);
				long comparisionStartTime = System.currentTimeMillis();

				int blankLines = 0;
				int noOfMatches = 0;

				int numberOfLinesInSecondFile = secondPdf.size();

				// Store lines matched, so that they are not considered for
				// match again
				ArrayList<String> linesMatchedInFirstFile = new ArrayList<String>();
				ArrayList<String> linesMatchedInSecondFile = new ArrayList<String>();

				for (int p = 0; p < firstPdf.size(); p++) {

					// /String firstLine =
					// StringUtils.trimToEmpty(firstPdf.get(p).replaceAll("\\s+",
					// ""));
					String firstFileLine = firstPdf.get(p);
					// int flen =firstFileLine.length();
					boolean matched = false;
					for (int j = 0; j < secondPdf.size(); j++) {
						// if (p < secondTotalStringLength) {
						// /String secondLine =
						// StringUtils.trimToEmpty(secondPdf.get(j).replaceAll("\\s+",
						// ""));
						String secondFileLine = secondPdf.get(j);

						/*
						 * if(secondLine!=null){ secondLine=secondLine.trim(); }
						 */

						// if (!StringUtils.isWhitespace(firstFileLine) &&
						// !StringUtils.isWhitespace(secondFileLine)) {

						// /int flen =StringUtils.length(firstLine);
						// /int seclen = StringUtils.length(secondLine);

						// int seclen = secondFileLine.length();

						// int max = flen>seclen?flen:seclen;
						// int dd = flen>seclen?flen-seclen:seclen-flen;
						// double dff = (dd/(double)max)*100;

						// if(dff<70){

						if (!linesMatchedInSecondFile.contains(secondFileLine)
								&& firstFileLine.contains(secondFileLine)) {
							matched = true;
							noOfMatches++;
							linesMatchedInSecondFile.add(secondFileLine);// This
																			// line
																			// should
																			// not
																			// be
																			// considered
																			// for
																			// next
																			// time
							break;
						} else if (!linesMatchedInFirstFile
								.contains(firstFileLine)
								&& secondFileLine.contains(firstFileLine)) {
							matched = true;
							noOfMatches++;
							linesMatchedInFirstFile.add(firstFileLine);// This
																		// line
																		// should
																		// not
																		// be
																		// considered
																		// for
																		// next
																		// time
							break;
						}
						// }
						// }
					}
					if (matched) {
						consecutiveMatchingLinesCounter++;
						if (maxConseutiveLinesMatched < consecutiveMatchingLinesCounter) {
							maxConseutiveLinesMatched = consecutiveMatchingLinesCounter;
						}
					} else {
						consecutiveMatchingLinesCounter = 0;
					}
				}
				// logger.info("First file " +
				// currentStudentStudentAssignment.getStudentFilePath());
				// logger.info("Second file " +
				// otherStudentStudentAssignment.getStudentFilePath());

				// logger.info("Lines in First File = " +
				// numberOfLinesInFirstFile);
				// logger.info("Lines in Second File = " +
				// numberOfLinesInSecondFile);
				// logger.info("Total Comparisions = " +
				// (numberOfLinesInFirstFile * numberOfLinesInSecondFile));
				// logger.info("Number of matches is = " + noOfMatches);

				int totalStringLength = numberOfLinesInSecondFile > numberOfLinesInFirstFile ? numberOfLinesInSecondFile
						: numberOfLinesInFirstFile;
				// double matching = noOfMatches > totalStringLength ? 100 :
				// (noOfMatches / (double) totalStringLength) * 100;

				double matching = (noOfMatches / (double) totalStringLength) * 100;
				

				/*
				 * if (matching < 50) { totalStringLength = totalStringLength -
				 * 20; matching = (noOfMatches / (double) totalStringLength) *
				 * 100; logger.info("% match is " + matching); }
				 */

				long comparisionEndTime = System.currentTimeMillis();
				

				if (matching > searchBean.getMinMatchPercent()) {
					String firstSapId = currentStudentStudentAssignment
							.getUsername();
					String secondSapId = otherStudentStudentAssignment
							.getUsername();

					

					ResultDomain copyResult = new ResultDomain();
					copyResult.setNoOfMatches(noOfMatches);
					copyResult.setMatching(matching);
					copyResult.totalStringLength = totalStringLength;
					copyResult.blankLines = blankLines;
					copyResult.firstFile = currentStudentStudentAssignment
							.getStudentFilePath();
					copyResult.secondFile = otherStudentStudentAssignment
							.getStudentFilePath();

					copyResult
							.setAssignmentName(assignmentName);
					copyResult
							.setMaxConseutiveLinesMatched(maxConseutiveLinesMatched);
					copyResult
							.setNumberOfLinesInFirstFile(numberOfLinesInFirstFile);
					copyResult
							.setNumberOfLinesInSecondFile(numberOfLinesInSecondFile);
					copyResult.setNoOfMatches(noOfMatches);

					copyResult.setUsername1(firstSapId);
					copyResult.setFirstName1(currentStudentStudentAssignment
							.getFirstname());
					copyResult.setLastName1(currentStudentStudentAssignment
							.getLastname());
					copyResult.setProgram1(currentStudentStudentAssignment
							.getProgramName());

					copyResult.setUsername2(secondSapId);
					copyResult.setFirstName2(otherStudentStudentAssignment
							.getFirstname());
					copyResult.setLastName2(otherStudentStudentAssignment
							.getLastname());
					copyResult.setProgram2(otherStudentStudentAssignment
							.getProgramName());

					copyResult.setMatchingFor80to90("No");

					if (matching >= 80.00 && matching <= 89.99) {

						studentsBetween80And90.put(firstSapId, null);
						studentsBetween80And90.put(secondSapId, null);
						copyResult.setMatchingFor80to90("Yes");
					}

					copyResultList.add(copyResult);

					studentWithCopyAboveThreshold.put(firstSapId, null);
					studentWithCopyAboveThreshold.put(secondSapId, null);

					if (matching > 90.00) {
						studentsAbove90.put(firstSapId, null);
						studentsAbove90.put(secondSapId, null);
					}

				}
				noOfMatches = 0;


			}
			/*
			 * } else {
			 * 
			 * 
			 * }
			 */
		}
		
		
		
		
		return saveToDesk(copyResultList, searchBean,
				studentWithCopyAboveThreshold, studentAssignmentFilesList,
				studentsAbove90,nonPDFFilesStudentsList);

		// return copyResultList;
	}

	private String saveToDesk(ArrayList<ResultDomain> copyResultList,
			StudentAssignment searchBean,
			HashMap<String, String> studentWithCopyAboveThreshold,
			List<StudentAssignment> StudentAssignmentFilesList,
			HashMap<String, String> studentsAboveNinety,
			ArrayList<StudentAssignment> nonPDFFilesStudentsList) {
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Copy Result");
		int rownum = 0;
		int cellnum = 0;
		Row row = sheet.createRow(rownum++);
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("Sr. No.");

		cell = row.createCell(cellnum++);
		cell.setCellValue("Assignment");
		cell = row.createCell(cellnum++);
		cell.setCellValue("Student ID 1");
		cell = row.createCell(cellnum++);
		cell.setCellValue("First Name 1");
		cell = row.createCell(cellnum++);
		cell.setCellValue("Last Name 1");
		/*
		 * cell = row.createCell(cellnum++); cell.setCellValue("Program 1");
		 * cell = row.createCell(cellnum++); cell.setCellValue("IC 1");
		 */

		cell = row.createCell(cellnum++);
		cell.setCellValue("Student ID 2");
		cell = row.createCell(cellnum++);
		cell.setCellValue("First Name 2");
		cell = row.createCell(cellnum++);
		cell.setCellValue("Last Name 2");
		/*
		 * cell = row.createCell(cellnum++); cell.setCellValue("Program 2");
		 * cell = row.createCell(cellnum++); cell.setCellValue("IC 2");
		 */
		cell = row.createCell(cellnum++);
		cell.setCellValue("Matching %");
		cell = row.createCell(cellnum++);
		cell.setCellValue("Matching % in 80 to 80.99");
		cell = row.createCell(cellnum++);
		cell.setCellValue("Max Consecutive Lines Matched");
		cell = row.createCell(cellnum++);
		cell.setCellValue("# of Lines in First File");
		cell = row.createCell(cellnum++);
		cell.setCellValue("# of Lines in Second File");
		cell = row.createCell(cellnum++);
		cell.setCellValue("# of Lines matched");

		for (ResultDomain copyResult : copyResultList) {
			cellnum = 0;
			String sapid1 = copyResult.getUsername1();
			User u1 = userService.findByUserName(sapid1);
			String sapid2 = copyResult.getUsername2();
			User u2 = userService.findByUserName(sapid2);
			row = sheet.createRow(rownum++);
			cell = row.createCell(cellnum++);
			cell.setCellValue(rownum - 1);
			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getAssignmentName());
			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getUsername1());
			cell = row.createCell(cellnum++);
			cell.setCellValue(u1.getFirstname());
			cell = row.createCell(cellnum++);
			cell.setCellValue(u1.getLastname());
			/*
			 * cell = row.createCell(cellnum++);
			 * cell.setCellValue(copyResult.getProgram1()); cell =
			 * row.createCell(cellnum++);
			 * cell.setCellValue(copyResult.getCenterName1());
			 */

			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getUsername2());
			cell = row.createCell(cellnum++);
			cell.setCellValue(u2.getFirstname());
			cell = row.createCell(cellnum++);
			cell.setCellValue(u2.getLastname());
			/*
			 * cell = row.createCell(cellnum++);
			 * cell.setCellValue(copyResult.getProgram2()); cell =
			 * row.createCell(cellnum++);
			 * cell.setCellValue(copyResult.getCenterName2());
			 */
			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getMatching());

			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getMatchingFor80to90());
			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getMaxConseutiveLinesMatched());
			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getNumberOfLinesInFirstFile());
			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getNumberOfLinesInSecondFile());
			cell = row.createCell(cellnum++);
			cell.setCellValue(copyResult.getNoOfMatches());

		}

		XSSFSheet sheet2 = workbook.createSheet("Students Below Threshold");
		rownum = 0;
		cellnum = 0;
		row = sheet2.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellValue("Sr. No.");

		cell = row.createCell(cellnum++);
		cell.setCellValue("Student ID");

		cell = row.createCell(cellnum++);
		cell.setCellValue("Name");

		for (StudentAssignment StudentAssignment : StudentAssignmentFilesList) {
			String sapid = StudentAssignment.getUsername();
			User u = userService.findByUserName(sapid);
			if (!studentWithCopyAboveThreshold.containsKey(sapid)) {
				cellnum = 0;
				row = sheet2.createRow(rownum++);
				cell = row.createCell(cellnum++);
				cell.setCellValue(rownum - 1);

				cell = row.createCell(cellnum++);
				cell.setCellValue(sapid);

				cell = row.createCell(cellnum++);
				cell.setCellValue(u.getFirstname() + " "
						+ u.getLastname());
			}
		}
		// Newly Added to add a new Sheet for students in 90 + range//
		XSSFSheet sheet3 = workbook.createSheet("Students above 90%");
		rownum = 0;
		cellnum = 0;
		row = sheet3.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellValue("Sr. No.");

		cell = row.createCell(cellnum++);
		cell.setCellValue("Student ID");

		cell = row.createCell(cellnum++);
		cell.setCellValue("Name");
		String filePath = "";

		for (StudentAssignment StudentAssignment : StudentAssignmentFilesList) {
			String sapid = StudentAssignment.getUsername();
			User u = userService.findByUserName(sapid);
			if (studentsAboveNinety.containsKey(sapid)) {
				cellnum = 0;
				row = sheet3.createRow(rownum++);
				cell = row.createCell(cellnum++);
				cell.setCellValue(rownum - 1);

				cell = row.createCell(cellnum++);
				cell.setCellValue(sapid);

				cell = row.createCell(cellnum++);
				cell.setCellValue(u.getFirstname() + " "
						+ u.getLastname());

			}
		}
		XSSFSheet sheet4 = workbook.createSheet("Students submitted Non PDF or DOCX Files");
		rownum = 0;
		cellnum = 0;
		row = sheet4.createRow(rownum++);
		cell = row.createCell(cellnum++);
		cell.setCellValue("Sr. No.");

		cell = row.createCell(cellnum++);
		cell.setCellValue("Student ID");

		cell = row.createCell(cellnum++);
		cell.setCellValue("Name");

		for (StudentAssignment StudentAssignment : nonPDFFilesStudentsList) {
			String sapid = StudentAssignment.getUsername();
			User u = userService.findByUserName(sapid);
				cellnum = 0;
				row = sheet4.createRow(rownum++);
				cell = row.createCell(cellnum++);
				cell.setCellValue(rownum - 1);

				cell = row.createCell(cellnum++);
				cell.setCellValue(sapid);

				cell = row.createCell(cellnum++);
				cell.setCellValue(u.getFirstname() + " "
						+ u.getLastname());

		}
		// End//

		try {

			File folderPath = new File(copyCaseFolder);
			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}

			String fileName = searchBean.getCourseName().replace("/", "-") + searchBean.getAssignmentName()
					+ RandomStringUtils.randomAlphanumeric(10) + "Copy_Result.xlsx";

			filePath = copyCaseFolder + "/" + fileName;

			FileOutputStream out = new FileOutputStream(new File(filePath));
			workbook.write(out);
			out.close();
			
		} catch (Exception e) {
			
			logger.error(e.getMessage());
		}

		return filePath;
	}

	public List<String> readLineByLine(String prefix, String fileName,
			ArrayList<String> questionFileContent) {
		List<String> ans = new ArrayList<>();
		PDFTextStripper stripper;
		PDDocument doc = null;
		// List<String> newOne = new ArrayList<>();
		String allFileContent = "";

		try {
			try {
			if(fileName.endsWith(".docx")) {
				String inputFile=fileName;
				String outputFile=fileName.replace(".docx", ".pdf");
				//logger.info("inputFile--->"+inputFile);
				//logger.info("outputFile--->"+outputFile);
				FileInputStream in=new FileInputStream(inputFile);
				XWPFDocument document=new XWPFDocument(in);
				File outFile=new File(outputFile);
				OutputStream out=new FileOutputStream(outFile);
				PdfOptions options=null;
				PdfConverter.getInstance().convert(document,out,options);
				fileName=outputFile;
			}
			}catch(Exception ee) {
				logger.error("PDF converter Error--->",ee);
			}
			if (fileName.endsWith(".pdf")) {

				if (prefix != null)
					doc = PDDocument.load(prefix + File.separator + fileName);
				else
					doc = PDDocument.load(fileName);

				int count = doc.getNumberOfPages();
				stripper = new PDFTextStripper();
				stripper.setStartPage(1);
				stripper.setEndPage(count);
				String x1 = stripper.getText(doc);
				// break up the file content returned as a string into
				// individual
				// lines

				ans = Arrays.asList(x1.split(stripper.getLineSeparator()));

				for (String s : ans) {
					s = s.trim();
					if (StringUtils.isBlank(s) || StringUtils.isEmpty(s)) {

					} else {
						s = s.replaceAll("\\s+", "");
						s = s.replaceAll("\\p{C}", "");
						// Join all lines of pdf
						allFileContent = allFileContent
								+ s.toUpperCase().trim();
					}
				}
			} else {
				return ans;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("FNF>>"+e.getMessage());
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IO>>"+e.getMessage());
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("exce>>"+e.getMessage());
			return null;
		} finally {
			if (doc != null)
				try {
					doc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
		}

		// Split lines by full stop or by question.
		String[] splits = allFileContent.split("\\.|\\?|\\:");
		ArrayList<String> fileContent = new ArrayList<>();

		for (String line : splits) {
			if (line.length() > MIN_LENGTH_FOR_COMPARISION
					&& (!(questionFileContent.contains(line)))) {
				fileContent.add(line);
			}
		}

		return fileContent;

	}
}
