package com.brightleaf.executeservice.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.brightleaf.executeservice.model.FolderStructure;
import com.brightleaf.executeservice.service.ExecuteService;
@Service
public class ExecuteServiceImpl implements ExecuteService{
	
	protected final static Logger logger = Logger.getLogger(ExecuteServiceImpl.class);
	
	
	//upload files to Temp loacation
	@Override
	public String uploadFilesFromLocal(final List<MultipartFile> filesUploaded, String transactionId) {

		
		String uploadPath = createTemporaryFolderToUploadFiles(transactionId);
		for (MultipartFile multipartFile : filesUploaded) {
			try {
				String originalFilename = multipartFile.getOriginalFilename();
				//SHAMA Since getOriginalFilename() is browser dependant and it may or may not contain full path
				if (originalFilename.contains("\\")) {
					originalFilename = originalFilename.substring(originalFilename.lastIndexOf('\\') + 1);
				} else if (originalFilename.contains("/")) {
					originalFilename = originalFilename.substring(originalFilename.lastIndexOf('/') + 1);
				}
				InputStream inputStream = multipartFile.getInputStream();
				File ff = new File(uploadPath,originalFilename);
				OutputStream out = new FileOutputStream(ff);
				IOUtils.copy(inputStream, out);
				logger.info("Uploaded from local to temp");
				inputStream.close();
				out.close();
			} catch (IOException e) {
				logger.error("Error in uploadFilesFromLocal", e);
			}
		}
		return "Uploaded from local";
	}

	//create temporary folder for upload files
	private String createTemporaryFolderToUploadFiles(final String transactionId) {

		File file = new File(FolderStructure.AE_HOME.getLocation());
		if (!file.exists()) {
			boolean result=file.mkdir();
			if(result)
			{
				logger.info("mkdir AE_HOME successful");
			}
		}
		else{
			logger.info("AE_HOME exists");
		}
		file = new File(FolderStructure.AE_HOME.getLocation() + FolderStructure.SOURCE_LOCATION.getLocation() + transactionId);
		if (!file.exists()) {
			boolean result1=file.mkdir();
			if(result1)
			{
				logger.info("mkdir for transaction " + transactionId + " successful");
			}
		}
		else{
			logger.info("transaction " + transactionId + " exists");
			}
		return file.getAbsolutePath();
	}

	//create transactionId
	@Override
	public String generateTransactionId(int companyId, String identifier) {
		StringBuilder stringBuilder = new StringBuilder();
		if (identifier != null && !identifier.contentEquals("") && !identifier.toLowerCase().contentEquals("undefined")) {
			stringBuilder.append(identifier + "_");
		}
		stringBuilder.append("TXN_");
		stringBuilder.append(companyId);
		stringBuilder.append("_");
		synchronized (this) {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			stringBuilder.append(time);
		}

		return stringBuilder.toString();
	}
}
