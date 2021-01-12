package com.brightleaf.exportservice.serviceimpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.brightleaf.exportservice.model.ExtractedReport;
import com.brightleaf.exportservice.model.ExtractedUserReport;
import com.brightleaf.exportservice.model.FolderStructure;
import com.brightleaf.exportservice.service.CreateCompanyWiseReportService;
import com.brightleaf.exportservice.utility.ExportExcelUtility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class CreateCompanyWiseReportServiceImpl implements CreateCompanyWiseReportService {

	protected final static Logger logger = Logger.getLogger(CreateExcelConsolidatedServiceImpl.class);

	@Override
	public Resource exportCreateCompanyWiseReportToExcel(HttpServletResponse response, String getExcelData) {
		Gson gson = new Gson();
		File file = null;
		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;
		String path = null;
		FileSystemResource ffggg = null;
		try {
			Type type = new TypeToken<List<ExtractedReport>>() {
			}.getType();
			List<ExtractedReport> EntityList = gson.fromJson(getExcelData, type);
			String outputLocation = getOutputLocation();
			path = createExcelCompany(outputLocation, EntityList);
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
			file.delete();
		} catch (Exception e) {
			logger.error("Error in companywise report", e);
		} finally {
			closeStreamsFile(fileInputStream, outputStream);
			file.delete();
		}
		return ffggg;
	}

	@Override
	public Resource exportCreateUserWiseReportToExcel(HttpServletResponse response, String getExcelData) {
		Gson gson = new Gson();
		File file = null;
		FileInputStream fileInputStream = null;
		OutputStream outputStream = null;
		String path = null;
		FileSystemResource ffggg = null;
		try {
			Type type = new TypeToken<List<ExtractedUserReport>>() {
			}.getType();
			List<ExtractedUserReport> EntityList = gson.fromJson(getExcelData, type);
			String outputLocation = getOutputLocation();
			path = createExcelUser(outputLocation, EntityList);
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
			file.delete();
		} catch (Exception e) {
			logger.error("Error in userwise report", e);
		} finally {
			closeStreamsFile(fileInputStream, outputStream);
			file.delete();
		}
		return ffggg;
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
			logger.error("Error in getOutputLocation", e);
		}
		return outputFilePath;
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
			logger.error("Error in closeStreamsFile", e);
		}
	}

	private String createExcelCompany(String outputDir, List<ExtractedReport> extractedCompanyList) {
		InputStream template = null;
		ClassLoader classLoader = CreateCompanyWiseReportServiceImpl.class.getClassLoader();
		template = classLoader.getResourceAsStream("COMPANY_EXTRACTION_XLS_WRITER.xlsx");
		String exportExcelFilePath = null;
		try {
			Workbook workbook = new XSSFWorkbook(template);
			String excelFileName = ExportExcelUtility.createReportOfCompanyExportExcelFile(workbook,
					extractedCompanyList);
			exportExcelFilePath = ExportExcelUtility.getExportExcelFilePath(excelFileName, outputDir);
			FileOutputStream out = new FileOutputStream(exportExcelFilePath);
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			logger.error("Error in createConsolidatedExcel", e);
		}
		return exportExcelFilePath;
	}

	private String createExcelUser(String outputDir, List<ExtractedUserReport> extractedCompanyList) {
		InputStream template = null;
		ClassLoader classLoader = CreateCompanyWiseReportServiceImpl.class.getClassLoader();
		template = classLoader.getResourceAsStream("USER_EXTRACTION_XLS_WRITER.xlsx");
		String exportExcelFilePath = null;
		try {
			Workbook workbook = new XSSFWorkbook(template);
			String excelFileName = ExportExcelUtility.createReportOfUserExportExcelFile(workbook, extractedCompanyList);
			exportExcelFilePath = ExportExcelUtility.getExportExcelFilePath(excelFileName, outputDir);
			FileOutputStream out = new FileOutputStream(exportExcelFilePath);
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			logger.error("Error in createConsolidatedExcel", e);
		}
		return exportExcelFilePath;
	}
}
