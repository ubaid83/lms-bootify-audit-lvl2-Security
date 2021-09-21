package com.spts.lms.beans.amazon;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.KeyGenerator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SSECustomerKey;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.Transfer.TransferState;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.TransferProgress;
import com.amazonaws.util.IOUtils;

@Component
public class AmazonS3ClientService {
	
//	@Value("${baseDir}")
//    String baseDirectory;
	
	@Value("${workStoreDir}")
    String baseFolder;
	
    private String awsS3AudioBucket;
    
    private AmazonS3 amazonS3;
    
    private static SSECustomerKey SSE_KEY;
    private static KeyGenerator KEY_GENERATOR;
    
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientService.class);

    @Autowired
    public AmazonS3ClientService(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket) 
    {
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion.getName()).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    }


    @Async
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }
    
    @Async
    public void uploadFileToS3Bucket(MultipartFile multipartFile, String filePath)
    {
        String fileName = multipartFile.getOriginalFilename();
        
        try {
            //creating the file in the server (temporarily)
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            PutObjectRequest putObjectRequest = new PutObjectRequest(awsS3AudioBucket, filePath+"/"+fileName, file);

//            if (enablePublicReadAccess) {
//                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
//            }
            amazonS3.putObject(putObjectRequest);
            //removing the file created in the server
            file.delete();
        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
            logger.error("exception",ex);
            throw new  AmazonServiceException("File not Uploaded");
        }
    }
   
    @Async
    public Map<String,String> uploadFileToS3BucketWithRandomString(MultipartFile multipartFile, String filePath)
    {
        String fileName = multipartFile.getOriginalFilename();
        fileName = fileName.replaceAll("'", "_");
        fileName = fileName.replaceAll(",", "_");
        fileName = fileName.replaceAll("&", "and");
        fileName = fileName.replaceAll(" ", "_");

        fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + RandomStringUtils.randomAlphanumeric(10)
        + fileName.substring(fileName.lastIndexOf("."), fileName.length());
        
        Map<String,String> mapper = new HashMap<String, String>();
        try {
            //creating the file in the server (temporarily)
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            PutObjectRequest putObjectRequest = new PutObjectRequest(awsS3AudioBucket, filePath+"/"+fileName, file);

//            if (enablePublicReadAccess) {
//                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
//            }
            amazonS3.putObject(putObjectRequest);
            //removing the file created in the server
            file.delete();
            
        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
            logger.error("exception",ex);
            mapper.put("ERROR", "File not Uploaded");
        }
        mapper.put("SUCCESS", fileName);
        return mapper;
    }
    
    
    
    @Async
    public void uploadFile(File uploadFile, String filePath) {
    amazonS3.putObject(awsS3AudioBucket, filePath+"/"+uploadFile.getName(), uploadFile); 
    }

    @Async
    public void uploadFile(String name,byte[] content, String filePath)  {
        File file = new File("/tmp/"+name);
        file.canWrite();
        file.canRead();
        FileOutputStream iofs = null;
        try {
            iofs = new FileOutputStream(file);
            iofs.write(content);
            amazonS3.putObject(awsS3AudioBucket, filePath+"/"+file.getName(), file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @Async
    public byte[] getFile(String path) {
    	if(path.startsWith("/")) {
    		path = StringUtils.substring(path, 1);
		}
    	path = path.replace("/\\", "/");
    	path = path.replace("\\\\","/");
		path = path.replace("\\","/");
		path = path.replace("//","/");
		logger.info("path---->"+path);
        S3Object obj = amazonS3.getObject(awsS3AudioBucket, path);
        S3ObjectInputStream stream = obj.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(stream);
            obj.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Async
    public InputStream getFileByFullPath(String path) {
    	if(path.startsWith("/")) {
    		path = StringUtils.substring(path, 1);
		}
    	path = path.replace("/\\", "/");
    	path = path.replace("\\\\","/");
		path = path.replace("\\","/");
		path = path.replace("//","/");
		logger.info("path---->"+path);
        S3Object obj = amazonS3.getObject(awsS3AudioBucket, path);
        S3ObjectInputStream stream = obj.getObjectContent();
        
       return stream;
    }
    
    @Async
    public InputStream getFileForDownloadS3Object(String key,String folderPath) {
    	if(folderPath.startsWith("/")) {
    		folderPath = StringUtils.substring(folderPath, 1);
		}
    	if(folderPath.endsWith("/")) 
		{
			folderPath = folderPath.substring(0, folderPath.length() - 1);
		}
    	folderPath = folderPath.replace("/\\", "/");
    	folderPath = folderPath.replace("\\\\","/");
    	folderPath = folderPath.replace("\\","/");
    	folderPath = folderPath.replace("//","/");
		logger.info("path---->"+folderPath);
    	logger.info(folderPath+"/"+key);
    	S3Object obj = amazonS3.getObject(awsS3AudioBucket, folderPath + "/" + key);
    	S3ObjectInputStream stream = obj.getObjectContent();
    		
    	return stream;
    }
    
    @Async
    public void deleteFileFromS3Bucket(String fileName) 
    {
    	logger.info("fileName---->"+fileName);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, fileName));
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
        }
    }
    
    public boolean createFolder(String folderName, String folderPath) {
    	try {
    		if(folderPath.endsWith("/")) 
    		{
    			folderPath = folderPath.substring(0, folderPath.length() - 1);
    		}
    		if(folderPath.startsWith("/")) 
    		{
    			folderPath = folderPath.substring(1, folderPath.length());
    		}
			// create meta-data for your folder and set content-length to 0
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(0);
			// create empty content
			InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
			// create a PutObjectRequest passing the folder name suffixed by /
			PutObjectRequest putObjectRequest = new PutObjectRequest(awsS3AudioBucket, folderPath + "/" + folderName + "/", emptyContent, metadata);
			// send request to S3 to create folder
			amazonS3.putObject(putObjectRequest);
			return true;
    	}catch (AmazonServiceException ex){
    		logger.error("exception",ex);
    		return false;
    	}
	}
    
    public boolean uploadDir(String sourcePathLocal,String destPathS3, boolean recursive) {
    	//sourcePathLocal is server localPath
    	//before upload destPathS3 must be created.
    	logger.info("sourcePathLocal----->"+sourcePathLocal);
    	logger.info("destPathS3----->"+destPathS3);
		TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
		try {
			MultipleFileUpload xfer = xfer_mgr.uploadDirectory(awsS3AudioBucket,destPathS3, new File(sourcePathLocal), recursive);
			// loop with Transfer.isDone()
			XferMgrProgress.showTransferProgress(xfer);
			// or block with Transfer.waitForCompletion()
			XferMgrProgress.waitForCompletion(xfer);
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			return false;
		}
		return true;
	}
    
    public boolean downloadDir(String sourcePathS3,String dest) {
    	//dest is server localPath
		TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
		logger.info("dest---->"+dest);
		try {
			MultipleFileDownload xfer = xfer_mgr.downloadDirectory(awsS3AudioBucket, sourcePathS3, new File(dest));
			// loop with Transfer.isDone()
			XferMgrProgress.showTransferProgress(xfer);
			// or block with Transfer.waitForCompletion()
			XferMgrProgress.waitForCompletion(xfer);
			
		} catch (AmazonServiceException e) {
			logger.info("e.getErrorMessage--------->"+e.getErrorMessage());
			System.err.println(e.getErrorMessage());
			return false;
		}
		return true;
	}
    public boolean copyObject(String source, String destination){
		try {	
			amazonS3.copyObject(awsS3AudioBucket, source, awsS3AudioBucket, destination);
			return true;
	    } catch (AmazonServiceException e) {
	        System.err.println(e.getErrorMessage());
	        return false;
	    }
    }
    
   public boolean exists(String folderPath) {
	   if(folderPath.endsWith("/")) 
	   {
		   folderPath = folderPath.substring(0, folderPath.length() - 1);
	   }
	   ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(awsS3AudioBucket).withPrefix(folderPath).withDelimiter("/");
	   ListObjectsV2Result listing = amazonS3.listObjectsV2(req);
	   for (String commonPrefix : listing.getCommonPrefixes()) {
	        System.out.println("common--->"+commonPrefix);
	        if(commonPrefix.equals(folderPath+"/")) {
	        	System.out.println("exists--->"+commonPrefix);
				  return true; 
			 } 
	   }
	   return false;
   }
   
   public boolean deleteDir(String folderPath) {
	   if(!folderPath.endsWith("/")) 
	   {
		   folderPath = folderPath+"/";
	   }
	   ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(awsS3AudioBucket).withPrefix(folderPath);
	   ListObjectsV2Result listing = amazonS3.listObjectsV2(req);
	   for (S3ObjectSummary summary: listing.getObjectSummaries()) {
		   System.out.println("keyss---->"+summary.getKey());
		   amazonS3.deleteObject(awsS3AudioBucket, summary.getKey());
	   }
	   return false;
   }
   
   
   
   @Async
	public void deleteFile(final String keyName) {
		logger.info("Deleting file with name= " + keyName);
		final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(awsS3AudioBucket, keyName);
		amazonS3.deleteObject(deleteObjectRequest);
		logger.info("File deleted successfully.");
	}
   
   
	public boolean uploadLibDir(File localSourcePathFile, String destPath, boolean recursive) {

		try {

			TransferManager tm = new TransferManager(amazonS3);

			MultipleFileUpload upload = tm.uploadDirectory(awsS3AudioBucket, destPath, localSourcePathFile,
					recursive);
			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	@Async
	public Map<String,String> uploadFileToS3BucketAndGetUrl(String bucketName,MultipartFile multipartFile, String filePath, boolean enablePublicReadAccess)
	{
		Map<String,String> mapper = new HashMap<String, String>();
		String fileName = multipartFile.getOriginalFilename();
		
		try {
			//creating the file in the server (temporarily)
			fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + RandomStringUtils.randomAlphanumeric(10) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(multipartFile.getBytes());
			fos.close();
			 
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath+"/"+fileName, file);

			if (enablePublicReadAccess) {
				putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
			}
			amazonS3.putObject(putObjectRequest);
			String objectUrl = amazonS3.getUrl(bucketName, filePath+"/"+fileName).toString();
			mapper.put("objectUrl", objectUrl);
			//removing the file created in the server
			file.delete();
		} catch (IOException | AmazonServiceException ex) {
			logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
			logger.error("exception",ex);
			mapper.put("ERROR", "File not Uploaded");
		}
		return mapper;
	}
	
	public String existsFile(String folderPath,String fileName) {
		   if(folderPath.endsWith("/")) 
		   {
			   folderPath = folderPath.substring(0, folderPath.length() - 1);
		   }
		   ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(awsS3AudioBucket).withPrefix(folderPath);
		   ListObjectsV2Result listing = amazonS3.listObjectsV2(req);
		   for (S3ObjectSummary summary: listing.getObjectSummaries()) {
			   if(summary.getKey().contains(fileName)) {
				   System.out.println("file exists--->"+summary.getKey());
				   return summary.getKey();
			   }
		   }
		   return "";
	   }
}
