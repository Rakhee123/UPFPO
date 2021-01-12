package com.brightleaf.exportservice.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.brightleaf.exportservice.model.Attributes;
import com.brightleaf.exportservice.model.ExtractedEntity;
import com.brightleaf.exportservice.model.FolderStructure;
import com.brightleaf.exportservice.service.CreateExcelConsolidatedFullService;
import com.brightleaf.exportservice.utility.ExportExcelUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class CreateExcelConsolidatedFullServiceImpl implements CreateExcelConsolidatedFullService {

	protected final static Logger logger = Logger.getLogger(CreateExcelConsolidatedFullServiceImpl.class);

	@Override
	public Resource exportTransactionFullConsolidatedToExcel(final HttpServletResponse response, String getExcelData,
			String transactionId, Integer qcLevel) {
		Gson gson = new Gson();
		String path = null;
		File file = null;
		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;
		FileSystemResource ffggg = null;
		try {
			Type type = new TypeToken<List<ExtractedEntity>>() {
			}.getType();
			List<ExtractedEntity> aemdDbEntityList = gson.fromJson(getExcelData, type);
			String outputLocation = getOutputLocation();
			path = createConsolidatedFullExcel(transactionId, outputLocation, aemdDbEntityList, qcLevel);
			// for popup
			String attachmentName = FilenameUtils.getName(path);
			file = new File(path);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
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
			ffggg = new FileSystemResource(path);
//			file.delete();
		} catch (Exception e) {
		} finally {
			closeStreamsFile(fileInputStream, outputStream);
			file.delete();
		}
		return ffggg;
	}

	private String createConsolidatedFullExcel(String txId, String outputDir,
			List<ExtractedEntity> extractedConsolidatedEntityList, Integer qcLevel) {
		InputStream template = null;
		ClassLoader classLoader = CreateExcelConsolidatedFullServiceImpl.class.getClassLoader();
		template = classLoader.getResourceAsStream("MT_TRANSACTION_CONSOLIDATED_SUMMARY_XLS_WRITER.xlsx");
		String exportExcelFilePath = null;
		try {
			Workbook workbook = new XSSFWorkbook(template);
			List<String> attributListRetrieve = getAttributeList(extractedConsolidatedEntityList);

			String excelFileName = ExportExcelUtility.createConsolidatedFullExportExcelFile(workbook,
					extractedConsolidatedEntityList, attributListRetrieve, txId, qcLevel);
			exportExcelFilePath = ExportExcelUtility.getExportExcelFilePath(excelFileName, outputDir);
			FileOutputStream out = new FileOutputStream(exportExcelFilePath);
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			logger.error("Error in createConsolidatedFullExcel", e);
		}
		return exportExcelFilePath;
	}

	private List<String> getAttributeList(List<ExtractedEntity> extractedConsolidatedEntityList) {
		List<String> setOfAttributeNames = new ArrayList<>();
		for (ExtractedEntity extractedEntityDocument : extractedConsolidatedEntityList) {
			List<Attributes> attributeConsolidated = extractedEntityDocument.getAttributes();
			for (Attributes sdfsf : attributeConsolidated) {
				if (!setOfAttributeNames.contains(sdfsf.getAttributeName())) {
					setOfAttributeNames.add(sdfsf.getAttributeName());
				}
			}
		}
		return setOfAttributeNames;
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
