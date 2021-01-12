package com.brightleaf.ruleservice.serviceimpl;

import java.io.File;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.brightleaf.ruleservice.model.ExtractedEntityForSingleAttribute;
import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.service.FileTypeFactoryService;
import com.brightleaf.ruleservice.splitpdfparagraph.SplitPDFParagraphMain;
import com.brightleaf.ruleservice.splitsentence.SplitPDFSentenceMain;
@Service
public class PdfFileTypeServiceImpl implements FileTypeFactoryService {
	
	protected final static Logger logger = Logger.getLogger(PdfFileTypeServiceImpl.class);
	
	@Override
	public void extractAttributes(File file, Integer companyId, List<ExtractedEntityForSingleAttribute> listExtraction,
			List<Rule> listRule, HttpServletRequest request, String outputDateFormat) {
		
		// Split text in sentences
		SplitPDFSentenceMain sentSplit = new SplitPDFSentenceMain();
		// Read entire text
		List<String> pages = new ArrayList<>();
		List<String> sentences = new ArrayList<>();
		String fileString = sentSplit.readEntireTextAndPages(file, pages, sentences); 
		String fileStringToCheck = fileString.replaceAll("\r\n", " ");
		fileStringToCheck = fileStringToCheck.replaceAll("\n", " ");
		fileStringToCheck = fileStringToCheck.replaceAll("“", "\"");
		fileStringToCheck = fileStringToCheck.replaceAll("”", "\"");
		fileStringToCheck = fileStringToCheck.replaceAll("’", "'");

		// Split text in paragraphs
		SplitPDFParagraphMain paraSplit = new SplitPDFParagraphMain();
		List<String> lines = new ArrayList<>();
		List<String> paragraphs = paraSplit.splitParagraphs(file, lines);

		String docName = "";
		try {
			docName = file.getCanonicalPath();
		} catch (IOException e) {
			logger.error("Error in ExtractAttributes", e);
		}
		
		// Apply rules
		if (!docName.contentEquals(""))
			new RuleApplicationServiceImpl().applyRules(sentences, paragraphs, listRule, companyId, docName,
					listExtraction, pages, lines, fileStringToCheck, request, outputDateFormat);
	}
}
