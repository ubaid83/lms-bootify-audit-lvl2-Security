package com.spts.lms.plagscan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.amazon.AmazonS3ClientService;

@Component
public class PlagScanner extends Retrieve {

	private static final Logger logger = Logger.getLogger(PlagScanner.class);

	
	@Value("${plagscanUser:''}")
	private String plagscanUser;

	@Value("${plagscanKey:''}")
	private String plagscanKey;
	
	@Autowired
	AmazonS3ClientService amazonS3ClientService;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	static private String url = "https://api.plagscan.com/v3/documents/";

	public String sendFileAndReturnPID(File file) {

		HttpCommonsPost lApacheHttpCommonsPost = new HttpCommonsPost();
		lApacheHttpCommonsPost.setURL("https://api.plagscan.com");
		try {

			Part[] lDataArr = { new StringPart("USER", plagscanUser),
					new StringPart("KEY", plagscanKey),
					new StringPart("VERSION", "2.0"),
					new StringPart("METHOD", "submit"),
					new FilePart("DATA", file) };
			String ret = lApacheHttpCommonsPost
					.doPostRequest((org.apache.commons.httpclient.methods.multipart.Part[]) lDataArr);
			JAXBContext jaxbContext = JAXBContext.newInstance(Xml.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(ret);
			Xml x = (Xml) unmarshaller.unmarshal(reader);

			logger.info("The Process Id is : " + x.getPID());

			String plageLevel = String.valueOf(this.getPlagLevelForFile(
					String.valueOf(x.getPID()), plagscanKey, url));
			logger.info("The Process Id is : " + x.getPID() + "PlagLevel"
					+ plageLevel);
			return plageLevel;

		} catch (Exception e) {
			logger.error("Exception", e);
			return "error";
		}

	}

	/*
	 * public File multipartToFile(String multipart) throws
	 * IllegalStateException, IOException { File convFile = new File(multipart);
	 * return convFile; }
	 */

	public File multipartToFile(String multipart) throws IllegalStateException,
			IOException {

		File convFile = new File(multipart);
		System.out.println("convFile " + multipart);
		// multipart.transferTo(convFile);
		System.out.println("CONF FILE " + convFile);
		return convFile;
	}

	public File multipartToFileForS3(String multipart) throws IllegalStateException, IOException {
		multipart = multipart.replace("/\\", "/");
		multipart = multipart.replace("\\\\","/");
		multipart = multipart.replace("\\","/");

		File dest = new File(multipart);
		System.out.println("convFile " + multipart);
		InputStream inpStream = amazonS3ClientService.getFileByFullPath(multipart);
		File convFile = new File(downloadAllFolder + File.separator + dest.getName());
		FileUtils.copyInputStreamToFile(inpStream, convFile);
		// multipart.transferTo(convFile);
		System.out.println("CONF FILE " + convFile);
		return convFile;
	}
	/*
	 * public static void main(String[] args){ if(args.length<1) {
	 * System.err.println("Expecting filename of file to submit as parameter!");
	 * System.exit(1); }
	 * 
	 * Submit ob = new Submit(); int count=0;
	 * 
	 * try { File folder = new File("E://pdf"); File[] listOfFiles =
	 * folder.listFiles(); List <String > listOfPid=new ArrayList<String>(); for
	 * (File file : listOfFiles) { String pid = ob.sendFileAndReturnPID(file);
	 * System.out.println("pid "+pid); listOfPid.add(pid); ob.GenerateHTML(pid);
	 * 
	 * 
	 * } System.out.println("THE LIST OF PIDs   " ); for(Object o:listOfPid) {
	 * count++; System.out.print(" pid"+count +" - "+o+" ,"); }
	 * System.out.println("in all "+count); } catch(Exception
	 * e){System.err.println("File not found?\r\n"+e);} }
	 */
}