package com.brightleaf.exportservice.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.brightleaf.exportservice.model.Attributes;
import com.brightleaf.exportservice.model.ExtractedEntity;
import com.brightleaf.exportservice.model.ExtractedReport;
import com.brightleaf.exportservice.model.ExtractedUserReport;
import com.brightleaf.exportservice.model.QCValidation;

public class ExportExcelUtility {

	private static Logger logger = Logger.getLogger(ExportExcelUtility.class);

	private ExportExcelUtility() {
		throw new IllegalStateException("Utility class");
	}

	public static String createExportExcelFile(Workbook workbook, ExtractedEntity extractedEntityList, Integer qcLevel,
			String transactionId) {

		int rownum = 1;
		int srNo = 0;
		Sheet sheet = workbook.getSheetAt(0);
		CellStyle cellStyle = setCellStyles(workbook, IndexedColors.BLACK);
		String documentName = "";
		String attributeName = "";
		Integer appliedRule = null;
		String extractedSentence = "";
		String extractedChunk = "";
		QCValidation qcValue = null;

		logger.info(qcLevel);
		documentName = extractedEntityList.getDocumentName();
		List<Attributes> listOfAttributes = extractedEntityList.getAttributes();
		int numAttributesWriiten = 0;
		for (Attributes att : listOfAttributes) {
			attributeName = att.getAttributeName();
			appliedRule = att.getAppliedRule();
			extractedSentence = att.getExtractedSentence();
			extractedChunk = att.getExtractedChunk();
			qcValue = att.getQcValidation();
			sheet.autoSizeColumn(25);
			sheet.createFreezePane(0, 1, 0, 1);

			if (qcValue.getIgnoreResult().equals("NO")) {
				numAttributesWriiten++;
				// create excel
				int colNo = 0;
				Row row = sheet.createRow(rownum++);
				sheet.setColumnWidth(colNo, 2000);
				Cell cell1 = row.createCell(colNo++);
				cell1.setCellStyle(cellStyle);
				cell1.setCellValue(++srNo);
				sheet.setColumnWidth(colNo, 6000);
				// Attribute Name
				sheet.setColumnWidth(colNo, 6000);
				Cell cell2 = row.createCell(colNo++);
				cell2.setCellStyle(cellStyle);
				cell2.setCellValue(attributeName);
				// DocumentName
				sheet.setColumnWidth(colNo, 6000);
				Cell cell3 = row.createCell(colNo++);
				cell3.setCellStyle(cellStyle);
				cell3.setCellValue(documentName);
				// Applied Rule
				sheet.setColumnWidth(colNo, 6000);
				Cell cell5 = row.createCell(colNo++);
				cell5.setCellStyle(cellStyle);
				cell5.setCellValue("");
				if (!(appliedRule == -1 || appliedRule.equals(-1))) {
					cell5.setCellValue(appliedRule);
				}
				// Transaction id
				sheet.setColumnWidth(colNo, 6000);
				Cell cell4 = row.createCell(colNo++);
				cell4.setCellStyle(cellStyle);
				cell4.setCellValue(transactionId);
				// Extracted Sentence
				Cell cell6 = row.createCell(colNo++);
				cell6.setCellStyle(cellStyle);
				cell6.setCellValue(extractedSentence);
				sheet.setColumnWidth(colNo, 6000);
				// Extracted Chunk
				Cell cell7 = row.createCell(colNo++);
				cell7.setCellStyle(cellStyle);
				cell7.setCellValue(extractedChunk);
				sheet.setColumnWidth(colNo, 6000);
				// Application Extracted Value- Initial Value
				Cell cell8 = row.createCell(colNo++);
				cell8.setCellStyle(cellStyle);
				cell8.setCellValue(qcValue.getNewValue());
				sheet.setColumnWidth(colNo, 6000);
				// Application Extracted Value- Value Added By
				Cell cell9 = row.createCell(colNo++);
				cell9.setCellStyle(cellStyle);
				cell9.setCellValue(qcValue.getValueaddedBy());
				sheet.setColumnWidth(colNo, 6000);
			}
		}
		if (numAttributesWriiten == 0) {
			Row row = sheet.createRow(rownum++);
		}
		return documentName;
	}

	public static String getExportExcelFilePath(String documentName, String outputrDir) {
		String exportExcelFileWithPath = new StringBuffer(outputrDir.concat("\\"))
				.append(documentName.trim().replaceAll("((.pdf)|(.htm)|(.html))$", " ")).append(".xlsx").toString();
		deleteAlreadyExistingFile(exportExcelFileWithPath);
		return exportExcelFileWithPath;
	}

	private static CellStyle setCellStyles(Workbook workbook, IndexedColors indexedColors) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 9);
		font.setFontName("Calibri");
		font.setColor(indexedColors.getIndex());

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setBorderBottom(cellStyle.getBorderBottom());
		cellStyle.setBottomBorderColor(indexedColors.getIndex());
		cellStyle.setWrapText(true);
		cellStyle.setBorderRight(cellStyle.getBorderRight());
		cellStyle.setBorderLeft(cellStyle.getBorderLeft());
		cellStyle.setBorderTop(cellStyle.getBorderTop());
		cellStyle.setRightBorderColor(indexedColors.getIndex());
		cellStyle.setFont(font);
		return cellStyle;
	}

	public static void deleteAlreadyExistingFile(final String fileNameWithPath) {
		File file = new File(fileNameWithPath);
		if (file.isFile() && !file.delete()) {
			logger.error(fileNameWithPath + " NOT DELETED");
		}

	}

	public static String createConsolidatedExportExcelFile(Workbook workbook, List<ExtractedEntity> extractedEntityList,
			List<String> attributeList, String txId, Integer qcLevel) {

		int srNo = 0;
		int rowNo = 1;
		int rowCount = 0;
		Sheet sheet = workbook.getSheetAt(0);
		CellStyle cellStyle = setCellStyles(workbook, IndexedColors.BLACK);
		Row firstrow = sheet.getRow(0);
		CellStyle cellStyle1 = firstrow.getCell(0).getCellStyle();
		int firstcolNo = 2;
		logger.info("qclevel" + qcLevel);
		for (String attributeName : attributeList) {
			sheet.setColumnWidth(firstcolNo, 6000);
			Cell cell3 = firstrow.createCell(firstcolNo++);
			cell3.setCellStyle(cellStyle1);
			cell3.setCellValue(attributeName);
		}

		for (ExtractedEntity result : extractedEntityList) {
			boolean flag = false;
			int colNo = 0;
			Row row = null;
			if (rowNo == 1) {
				row = sheet.createRow(rowNo);
				rowNo += 1;
			} else {
				row = sheet.createRow(rowNo);
			}
			row.setHeight((short) 2000);
			sheet.setColumnWidth(colNo, 2000);
			Cell cell1 = row.createCell(colNo);
			colNo += 1;
			cell1.setCellStyle(cellStyle);
			srNo += 1;
			cell1.setCellValue(srNo);
			sheet.setColumnWidth(colNo, 8000);
			Cell cell2 = row.createCell(colNo);
			colNo += 1;
			cell2.setCellStyle(cellStyle);
			cell2.setCellValue(result.getDocumentName());

			LinkedHashMap<String, List<String>> maximumrows = getMaxRows1(result);
			List<Integer> maxCountList = new ArrayList<>();
			if (!maximumrows.isEmpty()) {
				for (Map.Entry m : maximumrows.entrySet()) {
					int i = 0;
					rowCount = row.getRowNum();
					int colCount = colNo;
					colNo += 1;
					List<String> values = (List<String>) m.getValue();
					for (String attributeValue : values) {
						sheet.setColumnWidth(colCount, 6000);
						if (i == 0) {
							Cell cel = row.createCell(colCount);
							cel.setCellStyle(cellStyle);
							cel.setCellValue(attributeValue);
						} else {

							Row existingRow = null;
							if (flag) {
								rowCount++;
								if (sheet.getRow(rowCount) != null) {
									existingRow = sheet.getRow(rowCount);
								} else {
									existingRow = sheet.createRow(rowCount);
									existingRow.setHeight((short) 2000);
								}
							} else {
								rowCount += 1;
								existingRow = sheet.createRow(rowCount);
								existingRow.setHeight((short) 2000);
								flag = true;
							}
							Cell cell4 = existingRow.createCell(colCount);
							cell4.setCellStyle(cellStyle);
							cell4.setCellValue(attributeValue);
						}
						i++;
					}
					maxCountList.add(rowCount);
				}
				int maxRowCount = Collections.max(maxCountList);
				rowNo = ++maxRowCount;
			}
		}
		return txId + "_CONSOLIDATED_RESULT";
	}

	private static LinkedHashMap<String, List<String>> getMaxRows1(ExtractedEntity result) {
		LinkedHashMap<String, List<String>> lkj = new LinkedHashMap<>();
		List<Attributes> attributeConsolidated = result.getAttributes();
		String attValue = "";
		// Checking if both the attribute values are ignoreresult
		int ignoreResultForatt = 0;
		for (Attributes asdf : attributeConsolidated) {
			if (asdf.getQcValidation().getIgnoreResult() == "YES"
					|| asdf.getQcValidation().getIgnoreResult().equals("YES")) {
				ignoreResultForatt++;
			}
		}
		for (Attributes asdf : attributeConsolidated) {
			String attName = asdf.getAttributeName();
			// Checking if all attribute values are ignore case than dont create a row
			if (ignoreResultForatt != attributeConsolidated.size()) {
				attValue = asdf.getQcValidation().getInitialValue();
				if (lkj.containsKey(asdf.getAttributeName())) {
					List<String> nameOfAttributeInList = lkj.get(attName);
					if (asdf.getQcValidation().getIgnoreResult() == "NO"
							|| asdf.getQcValidation().getIgnoreResult().equals("NO")) {
						nameOfAttributeInList.add(attValue);
					} else {
						nameOfAttributeInList.add("Result Ignored");
					}
					lkj.put(attName, nameOfAttributeInList);
				} else {
					List<String> initialValue1 = new ArrayList<>();
					if (asdf.getQcValidation().getIgnoreResult() == "NO"
							|| asdf.getQcValidation().getIgnoreResult().equals("NO")) {
						initialValue1.add(attValue);
					} else {
						initialValue1.add("Result Ignored");
					}
					lkj.put(attName, initialValue1);
				}
			}
		}
		return lkj;
	}

	public static String createConsolidatedFullExportExcelFile(Workbook workbook,
			List<ExtractedEntity> extractedEntityList, List<String> attributeList, String txId, Integer qcLevel) {

		int rownum = 1;
		int srNo = 0;
		Sheet sheet = workbook.getSheetAt(0);
		CellStyle cellStyle = setCellStyles(workbook, IndexedColors.BLACK);
		String documentName = "";
		String attributeName = "";
		String extractedChunk = "";
		String extractedSentence = "";
		String extractedValue = "";
		Integer ruleId = 0;

		logger.info("attributeList " + attributeList + "txId " + txId + "qcLevel " + qcLevel);
		for (ExtractedEntity result : extractedEntityList) {
			for (Attributes attbt : result.getAttributes()) {
				// Setting column names
				documentName = result.getDocumentName();
				attributeName = attbt.getAttributeName();
				ruleId = attbt.getAppliedRule();
				extractedChunk = attbt.getExtractedChunk();
				extractedSentence = attbt.getExtractedSentence();
				extractedValue = attbt.getQcValidation().getInitialValue();
				sheet.autoSizeColumn(25);
				sheet.createFreezePane(0, 1, 0, 1);

				int colNo = 0;
				Row row = sheet.createRow(rownum++);

				// start of rows
				sheet.setColumnWidth(colNo, 2000);
				Cell cell1 = row.createCell(colNo++);
				cell1.setCellStyle(cellStyle);
				cell1.setCellValue(++srNo);
				// Document Name
				sheet.setColumnWidth(colNo, 8000);
				Cell cell2 = row.createCell(colNo++);
				cell2.setCellStyle(cellStyle);
				cell2.setCellValue(documentName);
				// Attribute Name
				sheet.setColumnWidth(colNo, 8000);
				Cell cell3 = row.createCell(colNo++);
				cell3.setCellStyle(cellStyle);
				cell3.setCellValue(attributeName);
				// Rule Id
				sheet.setColumnWidth(colNo, 4000);
				Cell cell4 = row.createCell(colNo++);
				cell4.setCellStyle(cellStyle);
				cell4.setCellValue("");
				if (!(ruleId == -1 || ruleId.equals(-1))) {
					cell4.setCellValue(ruleId);
				}
				// chunk
				sheet.setColumnWidth(colNo, 8000);
				Cell cell5 = row.createCell(colNo++);
				cell5.setCellStyle(cellStyle);
				cell5.setCellValue(extractedChunk);
				// Sentence
				sheet.setColumnWidth(colNo, 8000);
				Cell cell6 = row.createCell(colNo++);
				cell6.setCellStyle(cellStyle);
				cell6.setCellValue(extractedSentence);
				// value
				sheet.setColumnWidth(colNo, 8000);
				Cell cell7 = row.createCell(colNo++);
				cell7.setCellStyle(cellStyle);
				if (attbt.getQcValidation().getIgnoreResult().equals("NO")) {
					cell7.setCellValue(extractedValue);
				} else {
					cell7.setCellValue("Result Ignored");
				}
			}
		}
		return txId + "_CONSOLIDATED_FULL_RESULT";
	}

	public static String createReportOfCompanyExportExcelFile(Workbook workbook, List<ExtractedReport> extractedData) {

		int rownum = 1;
		int srNo = 0;
		Sheet sheet = workbook.getSheetAt(0);
		CellStyle cellStyle = setCellStyles(workbook, IndexedColors.BLACK);
		String companyName = null;
		int totalTransaction = 0;
		int totalExtraction = 0;
		int correctExtraction = 0;
		int incorrectExtraction = 0;
		String accuracyPercentage = null;

		for (ExtractedReport result : extractedData) {
			companyName = result.getCompanyName();
			if (companyName == null || companyName.isEmpty())
				continue;
			totalTransaction = result.getTotalTransaction();
			totalExtraction = result.getTotalExtractions();
			correctExtraction = result.getCorrectExtraction();
			incorrectExtraction = result.getIncorrectExtraction();
			accuracyPercentage = result.getAccuracyPercentage();
			logger.info(" CompanyName " + companyName + " TotalTransaction" + totalTransaction + " TotalExtraction "
					+ totalExtraction + " CorrectExtraction " + correctExtraction + " IncorrectExtraction "
					+ incorrectExtraction + " AccuracyPercentage " + accuracyPercentage);
			sheet.autoSizeColumn(25);
			sheet.createFreezePane(0, 1, 0, 1);

			// create excel
			int colNo = 0;
			Row row = sheet.createRow(rownum++);
			sheet.setColumnWidth(colNo, 2000);
			Cell cell1 = row.createCell(colNo++);
			cell1.setCellStyle(cellStyle);
			cell1.setCellValue(++srNo);
			sheet.setColumnWidth(colNo, 6000);
			// Company Name
			sheet.setColumnWidth(colNo, 6000);
			Cell cell2 = row.createCell(colNo++);
			cell2.setCellStyle(cellStyle);
			cell2.setCellValue(companyName);

			// TotalTransaction
			sheet.setColumnWidth(colNo, 6000);
			Cell cell3 = row.createCell(colNo++);
			cell3.setCellStyle(cellStyle);
			cell3.setCellValue(totalTransaction);

			// TotalExtraction
			sheet.setColumnWidth(colNo, 6000);
			Cell cell4 = row.createCell(colNo++);
			cell4.setCellStyle(cellStyle);
			cell4.setCellValue(totalExtraction);

			// CorrectExtraction
			sheet.setColumnWidth(colNo, 6000);
			Cell cell5 = row.createCell(colNo++);
			cell5.setCellStyle(cellStyle);
			cell5.setCellValue(correctExtraction);

			// Incorrect Extraction
			sheet.setColumnWidth(colNo, 6000);
			Cell cell6 = row.createCell(colNo++);
			cell6.setCellStyle(cellStyle);
			cell6.setCellValue(incorrectExtraction);

			// AccuracyPercentage
			sheet.setColumnWidth(colNo, 6000);
			Cell cell7 = row.createCell(colNo++);
			cell7.setCellStyle(cellStyle);
			
			// percentage
			String s = accuracyPercentage;
			if (s.contains("%"))
				s = s.substring(0, s.indexOf("%")).trim();
			if (!s.contentEquals("")) {
				double perChange = Double.parseDouble(s);
				double d = (double) (perChange / 100.0);
				cell7.setCellValue(d);
				CellStyle style = workbook.createCellStyle();
				style.cloneStyleFrom(cellStyle);
				style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
				cell7.setCellStyle(style);
			} else {
				cell7.setCellValue(accuracyPercentage);
			}
		}
		return "REPORT_EXTRACTION_COMPANY_WISE";
	}

	public static String createReportOfUserExportExcelFile(Workbook workbook, List<ExtractedUserReport> extractedData) {

		int rownum = 1;
		int srNo = 0;
		Sheet sheet = workbook.getSheetAt(0);
		CellStyle cellStyle = setCellStyles(workbook, IndexedColors.BLACK);
		String companyName = null;
		String userName = null;
		String transactionId = null;
		
		int totalAttributesQC1 = 0;
		int totalChangedAttributesQC1 = 0;
		String percentQC1 = null;
		int attributeChangedByNextQCqc1 = 0;
		
		int totalAttributesQC2 = 0;
		int totalChangedAttributesQC2 = 0;
		String percentQC2 = null;
		int attributeChangedByNextQCqc2 = 0;
		
		int totalAttributesQC3 = 0;
		int totalChangedAttributesQC3 = 0;
		String percentQC3 = null;
		
		String attributeList = null;
		

		for (ExtractedUserReport result : extractedData) {
			companyName = result.getCompanyName();
			userName = result.getUserName();
			transactionId = result.getTransactionId();
			totalAttributesQC1 = result.getTotalAttributesqc1();
			totalAttributesQC2 = result.getTotalAttributesqc2();
			totalAttributesQC3 = result.getTotalAttributesqc3();
			totalChangedAttributesQC1 = result.getChangedAttributesqc1();
			totalChangedAttributesQC2 = result.getChangedAttributesqc2();
			totalChangedAttributesQC3 = result.getChangedAttributesqc3();
			attributeList = result.getChangedAttributeList();
			percentQC1 = result.getPercentqc1();
			percentQC2 = result.getPercentqc2();
			percentQC3 = result.getPercentqc3();
			attributeChangedByNextQCqc1 = result.getAttributeChangedByNextQCqc1();
			attributeChangedByNextQCqc2 = result.getAttributeChangedByNextQCqc2();
			
			logger.info(" CompanyName " + companyName + " userName" + userName + " transactionId " + transactionId
					+ " totalAttributes " + totalAttributesQC1 + " totalChangedAttributes " + totalChangedAttributesQC1
					+ " attributeList " + attributeList + " percent " + percentQC1);
			sheet.autoSizeColumn(25);
			sheet.createFreezePane(0, 1, 0, 1);

			// create excel
			int colNo = 0;
			Row row = sheet.createRow(rownum++);
			sheet.setColumnWidth(colNo, 2000);
			Cell cell1 = row.createCell(colNo++);
			cell1.setCellStyle(cellStyle);
			cell1.setCellValue(++srNo);
			sheet.setColumnWidth(colNo, 6000);

			// user Name
			sheet.setColumnWidth(colNo, 6000);
			Cell cell2 = row.createCell(colNo++);
			cell2.setCellStyle(cellStyle);
			cell2.setCellValue(userName);

			// CompanyName
			sheet.setColumnWidth(colNo, 6000);
			Cell cell3 = row.createCell(colNo++);
			cell3.setCellStyle(cellStyle);
			cell3.setCellValue(companyName);
			
			// totalAttributes
			sheet.setColumnWidth(colNo, 6000);
			Cell cell4 = row.createCell(colNo++);
			cell4.setCellStyle(cellStyle);
			cell4.setCellValue(totalAttributesQC1);
			
			// changed attributes
			sheet.setColumnWidth(colNo, 6000);
			Cell cell5 = row.createCell(colNo++);
			cell5.setCellStyle(cellStyle);
			cell5.setCellValue(totalChangedAttributesQC1);
			
			// percentage
			sheet.setColumnWidth(colNo, 6000);
			Cell cell6 = row.createCell(colNo++);
			cell6.setCellStyle(cellStyle);
			String s = percentQC1;
			if (s.contains("%"))
				s = s.substring(0, s.indexOf("%")).trim();
			if (!s.contentEquals("")) {
				double perChange = Double.parseDouble(s);
				double d = (double) (perChange / 100.0);
				cell6.setCellValue(d);
				CellStyle style = workbook.createCellStyle();
				style.cloneStyleFrom(cellStyle);
				style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
				cell6.setCellStyle(style);
			} else {
				cell6.setCellValue(percentQC1);
			}
			
			// next qc changes
			sheet.setColumnWidth(colNo, 6000);
			Cell cell7 = row.createCell(colNo++);
			cell7.setCellStyle(cellStyle);
			cell7.setCellValue(attributeChangedByNextQCqc1);
			
			// totalAttributes
			sheet.setColumnWidth(colNo, 6000);
			Cell cell8 = row.createCell(colNo++);
			cell8.setCellStyle(cellStyle);
			cell8.setCellValue(totalAttributesQC2);

			// changed attributes
			sheet.setColumnWidth(colNo, 6000);
			Cell cell9 = row.createCell(colNo++);
			cell9.setCellStyle(cellStyle);
			cell9.setCellValue(totalChangedAttributesQC2);

			// percentage
			sheet.setColumnWidth(colNo, 6000);
			Cell cell10 = row.createCell(colNo++);
			cell10.setCellStyle(cellStyle);
			s = percentQC2;
			if (s.contains("%"))
				s = s.substring(0, s.indexOf("%")).trim();
			if (!s.contentEquals("")) {
				double perChange = Double.parseDouble(s);
				double d = (double) (perChange / 100.0);
				cell10.setCellValue(d);
				CellStyle style = workbook.createCellStyle();
				style.cloneStyleFrom(cellStyle);
				style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
				cell10.setCellStyle(style);
			} else {
				cell10.setCellValue(percentQC2);
			}
			
			// next qc changes
			sheet.setColumnWidth(colNo, 6000);
			Cell cell11 = row.createCell(colNo++);
			cell11.setCellStyle(cellStyle);
			cell11.setCellValue(attributeChangedByNextQCqc2);
			
			// totalAttributes
			sheet.setColumnWidth(colNo, 6000);
			Cell cell12 = row.createCell(colNo++);
			cell12.setCellStyle(cellStyle);
			cell12.setCellValue(totalAttributesQC3);

			// changed attributes
			sheet.setColumnWidth(colNo, 6000);
			Cell cell13 = row.createCell(colNo++);
			cell13.setCellStyle(cellStyle);
			cell13.setCellValue(totalChangedAttributesQC3);

			// percentage
			sheet.setColumnWidth(colNo, 6000);
			Cell cell14 = row.createCell(colNo++);
			cell14.setCellStyle(cellStyle);
			s = percentQC3;
			if (s.contains("%"))
				s = s.substring(0, s.indexOf("%")).trim();
			if (!s.contentEquals("")) {
				double perChange = Double.parseDouble(s);
				double d = (double) (perChange / 100.0);
				cell14.setCellValue(d);
				CellStyle style = workbook.createCellStyle();
				style.cloneStyleFrom(cellStyle);
				style.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
				cell14.setCellStyle(style);
			} else {
				cell14.setCellValue(percentQC2);
			}
			
			// attributeList
			sheet.setColumnWidth(colNo, 6000);
			Cell cell15 = row.createCell(colNo++);
			cell15.setCellStyle(cellStyle);
			cell15.setCellValue(attributeList);
			
			// transactionId
			sheet.setColumnWidth(colNo, 6000);
			Cell cell16 = row.createCell(colNo++);
			cell16.setCellStyle(cellStyle);
			cell16.setCellValue(transactionId);
		}
		return "REPORT_EXTRACTION_USER_WISE";
	}
}