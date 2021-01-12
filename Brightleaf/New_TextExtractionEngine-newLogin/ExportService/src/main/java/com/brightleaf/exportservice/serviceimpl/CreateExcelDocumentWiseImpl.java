package com.brightleaf.exportservice.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.brightleaf.exportservice.model.DocumentBO;
import com.brightleaf.exportservice.model.ExtractedEntity;
import com.brightleaf.exportservice.model.FolderStructure;
import com.brightleaf.exportservice.service.CreateExcelDocumentWiseService;
import com.brightleaf.exportservice.utility.ExportExcelUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class CreateExcelDocumentWiseImpl implements CreateExcelDocumentWiseService {
	private static Logger logger = Logger.getLogger(CreateExcelDocumentWiseImpl.class);

	@Override
	public Resource exportTransactionDocumentwiseToExcel(final HttpServletResponse response, String getExcelData,
			String transactionId, Integer qcLevel) {

		Gson gson = new Gson();
		File file = null;
		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;
		String downloadFilePath = null;
		FileSystemResource ffggg = null;
		try {
			Type type = new TypeToken<List<ExtractedEntity>>() {
			}.getType();
			List<ExtractedEntity> aemdDbEntityList = gson.fromJson(getExcelData, type);

			String outputLocation = getOutputLocation();
			Set<String> filePaths = createExcel(outputLocation, aemdDbEntityList, qcLevel, transactionId);
			downloadFilePath = createZipFile(outputLocation, filePaths, transactionId);
			for (String zipfile : filePaths) {
				ExportExcelUtility.deleteAlreadyExistingFile(zipfile);
			}
			// for popup
			String attachmentName = FilenameUtils.getName(downloadFilePath);
			file = new File(downloadFilePath);
			response.setContentType("application/zip");
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			response.addHeader("Content-Disposition", attachmentName);
			response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
			fileInputStream = new FileInputStream(file);
			int bytesRead;
			while ((bytesRead = fileInputStream.read()) != -1) {
				response.getOutputStream().write(bytesRead);
			}
			response.flushBuffer();
			ffggg = new FileSystemResource(downloadFilePath);
			file.delete();
		} catch (Exception e) {
			logger.error("Error in exportTransactionDocumentwiseToExcel", e);
		} finally {
			closeStreamsFile(fileInputStream, outputStream);
			file.delete();
		}
		return ffggg;
	}

	private Set<String> createExcel(String outputDir, List<ExtractedEntity> extractedEntityList1, Integer qcLevel,
			String transactionId) {
		InputStream template = null;
		Set<String> listOfExportExcelFilePath = new HashSet<>();

		String exportExcelFilePath = null;

		Map<String, ExtractedEntity> extractedEntityList = getMapOfExtractedEntity(extractedEntityList1);
		try {

			for (String extractedEntityDocument : extractedEntityList.keySet()) {
				ClassLoader classLoader = CreateExcelDocumentWiseService.class.getClassLoader();
				template = classLoader.getResourceAsStream("MT_CHUNKER_XLS_WRITER_TEMPLATE.xlsx");
				Workbook workbook = new XSSFWorkbook(template);
				ExtractedEntity extractedEntityWithdrawed = extractedEntityList.get(extractedEntityDocument);
				String excelFileName = ExportExcelUtility.createExportExcelFile(workbook, extractedEntityWithdrawed,
						qcLevel, transactionId);
				exportExcelFilePath = ExportExcelUtility.getExportExcelFilePath(excelFileName, outputDir);
				FileOutputStream out = new FileOutputStream(exportExcelFilePath);
				workbook.write(out);
				out.close();
				listOfExportExcelFilePath.add(exportExcelFilePath);
			}
		} catch (IOException e) {
			logger.error("Error in createExcel", e);
		}
		return listOfExportExcelFilePath;
	}

	private Map<String, ExtractedEntity> getMapOfExtractedEntity(List<ExtractedEntity> dataCame) {

		// call sort method
		List<ExtractedEntity> sortedDocumentList = sortDocumentName(dataCame);

		List<DocumentBO> documentList = new ArrayList<>();
		for (ExtractedEntity docName : sortedDocumentList) {
			DocumentBO documentBO = new DocumentBO();
			documentBO.setDocumentName(docName.getDocumentName());
			documentList.add(documentBO);
		}
		Map<String, ExtractedEntity> documentWise = new HashMap<>();
		for (int i = 0; i <= documentList.size() - 1; i++) {
			DocumentBO documentBOName = documentList.get(i);
			String documentName = documentBOName.getDocumentName();
			ExtractedEntity dataCameForOne = sortedDocumentList.get(i);
			documentWise.put(documentName, dataCameForOne);
		}
		return documentWise;
	}

	private List<ExtractedEntity> sortDocumentName(List<ExtractedEntity> documentList) {
		for (int i = 0; i < documentList.size(); i++) {
			for (int j = i + 1; j < documentList.size(); j++) {
				String s1 = documentList.get(i).getDocumentName();
				String s2 = documentList.get(j).getDocumentName();
				s1 = s1.toLowerCase();
				s2 = s2.toLowerCase();
				if (compare(s1, s2) > 0) {
					ExtractedEntity temp = documentList.get(i);
					documentList.set(i, documentList.get(j));
					documentList.set(j, temp);
				}
			}
		}
		return documentList;
	}

	public static int compare(String o1, String o2) {
		Pattern NUMBERS = Pattern.compile("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		if (o1 == null || o2 == null) {
			return o1 == null ? o2 == null ? 0 : -1 : 1;
		}
		String[] split1 = NUMBERS.split(o1);
		String[] split2 = NUMBERS.split(o2);
		for (int i = 0; i < Math.min(split1.length, split2.length); i++) {
			char c1 = split1[i].charAt(0);
			char c2 = split2[i].charAt(0);
			int cmp = 0;

			if (Character.isDigit(c1) && Character.isDigit(c2)) {
				cmp = new BigInteger(split1[i]).compareTo(new BigInteger(split2[i]));
			}
			if (cmp == 0) {
				cmp = split1[i].compareTo(split2[i]);
			}

			if (cmp != 0) {
				return cmp;
			}
		}
		return split1.length - split2.length;
	}

	public static String createZipFile(String filePath, final Set<String> zipFileSet, final String txId)
			throws IOException {
		File file = new File(filePath);
		ZipOutputStream zos = null;
		FileOutputStream fos = null;
		String zipFilePath = null;
		try {
			if (file.isDirectory()) {
				zipFilePath = filePath + "\\" + txId + "_BY_DOCUMENT.zip";
				fos = new FileOutputStream(zipFilePath);
				zos = new ZipOutputStream(fos);
				addToZipFile(zipFileSet, zos);
				closeStreams(zos, fos, null);
			}
		} catch (FileNotFoundException e) {
			logger.error("Error in createZipFile", e);
		}
		return zipFilePath;
	}

	private static void addToZipFile(final Set<String> fileSet, ZipOutputStream zos) throws IOException {
		String fileName = "";
		File file = null;
		FileInputStream fis = null;
		try {
			if (fileSet != null && !fileSet.isEmpty()) {
				for (String fileToZip : fileSet) {
					file = new File(fileToZip);
					if (!file.getName().endsWith(".zip")
							&& (file.getName().endsWith(".xlsx") || file.getName().endsWith(".xls"))) {
						fileName = file.getAbsolutePath();
						fis = new FileInputStream(fileName);
						zos.putNextEntry(new ZipEntry(file.getName()));
						byte[] bytes = new byte[1024];
						int length;
						while ((length = fis.read(bytes)) >= 0) {
							zos.write(bytes, 0, length);
						}
					}
					closeStreams(null, null, fis);
					zos.closeEntry();
				}

			}
			closeStreams(zos, null, null);
		} catch (Exception e) {
			logger.error("Error in addToZipFile", e);
		}
	}

	private static void closeStreams(ZipOutputStream zos, FileOutputStream fos, FileInputStream fis) {
		try {
			if (null != zos) {
				zos.close();
			}
			if (null != fos) {
				fos.close();
			}
			if (null != fis) {
				fis.close();
			}
		} catch (IOException e) {
			logger.error("Error in closeStreams", e);
		}
	}

	private void closeStreamsFile(final FileInputStream fileInputStream, final OutputStream outputStream) {
		try {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (IOException e) {
			logger.error("error in closeStreamsFile", e);
		}
	}

	private String getOutputLocation() {

		String outputFilePath = FolderStructure.AE_HOME.getLocation() + System.getProperty("file.separator")
				+ "Web_Home" + System.getProperty("file.separator") + "ExcelExportLocation";
		try {
			File outputFile = new File(outputFilePath);
			if (!outputFile.exists()) {
				outputFile.mkdirs();
			}
		} catch (Exception e) {
		}
		return outputFilePath;
	}
}
