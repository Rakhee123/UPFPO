package com.brightleaf.executeservice.serviceimpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.stereotype.Service;

import com.brightleaf.executeservice.model.FolderStructure;
import com.brightleaf.executeservice.service.TempToSourceService;

@Service
public class TempToSourceServiceImpl implements TempToSourceService {

	protected final static Logger logger = Logger.getLogger(TempToSourceServiceImpl.class);

	@Override
	public List<String> moveTempToSource(String transactionId, int companyId) {
		String sourceLocation = null;
		sourceLocation = FolderStructure.AE_HOME.getLocation() + FolderStructure.SOURCE_LOCATION.getLocation()
				+ transactionId;

		File file = new File(sourceLocation);
		File[] listFiles = null;
		List<String> docList = new ArrayList<>();

		List<String> uploadingFiles = new ArrayList<>();
		if (file.isDirectory()) {
			listFiles = file.listFiles();
		}

		String appendText = "_processed";
		for (File sourceFile : listFiles) {
			uploadingFiles.add(sourceFile.getName());
			try {
				String s2 = null;
//				String PATH = "C:\\Tomcat-Dev" + "/webapps/";
				String PATH = System.getProperty("catalina.base") + "/webapps/";
				logger.info(PATH);
				String s1 = PATH + "sourcepdf/" + companyId + "/";
				logger.info("file getting uploaded to path" + s1);
				File directory = new File(s1);
				if (!directory.exists()) {
					directory.mkdir();
				}
				s2 = s1 + transactionId + "/";
				File directory1 = new File(s2);
				if (!directory1.exists()) {
					directory1.mkdir();
				}
				s2 += sourceFile.getName();

				AccessPermission ap = new AccessPermission();
				StandardProtectionPolicy spp = new StandardProtectionPolicy("aa1234", "aa1234", ap);
				try {
					PDDocument document = PDDocument.load(new File(sourceLocation + "/" + sourceFile.getName()));
					document.protect(spp);
					document.save(s2);
					document.close();
				} catch (Exception e) {
					appendText = "_unprocessed";
					File dest = new File(s2);
					FileUtils.copyFile(sourceFile, dest);
				}
			} catch (Exception e) {
				logger.error("Error in moveTempToSource", e);
			}
		}

		String renameFile = sourceLocation + appendText;
		File processedFile = new File(renameFile);

		boolean result = file.renameTo(processedFile);
		if (result) {
			logger.info("Operation Success");
		}
		File[] listOfFiles = processedFile.listFiles();

		for (File processedFile1 : listOfFiles) {
			if (processedFile1.isFile()) {
				docList.add(processedFile1.getAbsolutePath());
			}
		}
		return docList;
	}
}
