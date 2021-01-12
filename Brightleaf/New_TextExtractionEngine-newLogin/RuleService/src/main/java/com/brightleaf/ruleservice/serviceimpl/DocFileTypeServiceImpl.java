package com.brightleaf.ruleservice.serviceimpl;

import java.io.File;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.brightleaf.ruleservice.model.ExtractedEntityForSingleAttribute;
import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.service.FileTypeFactoryService;
import com.brightleaf.ruleservice.splitsentence.SplitPDFSentenceMain;

public class DocFileTypeServiceImpl implements FileTypeFactoryService {
	
	protected final static Logger logger = Logger.getLogger(DocFileTypeServiceImpl.class);
	
	@Override
	public void extractAttributes(File file, Integer companyId, List<ExtractedEntityForSingleAttribute> listExtraction,
			List<Rule> listRule, HttpServletRequest request, String outputDateFormat) {
		String fileName = null;
		XWPFWordExtractor we = null;
		try {
			fileName = file.getCanonicalPath();
		} catch (IOException e1) {
			logger.error("Error in extractAttributes", e1);
		}
		
		try (XWPFDocument docx = new XWPFDocument(new FileInputStream(fileName));){
			// using XWPFWordExtractor Class to get the entire text from the document
			we = new XWPFWordExtractor(docx);
			String text = we.getText();

			// Get the paragraphs from the document
			List<String> paragraphs = new ArrayList<>();

			// For paragraphs, the numbering scheme does not come in the text hence
			// manipulating the text using first.second.third approach.
			// This will be required when a paragraph is extracted to know where to stop.
			// For example, if the extracted paragraph is level one paragraph then continue
			// till level one paragraph is seen, and so on
			int first = 0;
			int second = 0;
			int third = 0;
			for (XWPFParagraph paragraph : docx.getParagraphs()) {
				if (paragraph.getStyle() == null) {
					paragraphs.add(paragraph.getText());
					continue;
				}
				String style = paragraph.getStyle().toLowerCase();
				if (style.contentEquals("heading1")) {
					first++;
					second = 0;
					third = 0;
					paragraphs.add(first + ". " + paragraph.getText());
				} else if (style.contentEquals("heading2")) {
					second++;
					third = 0;
					paragraphs.add(first + "." + second + ". " + paragraph.getText());
				} else if (style.contentEquals("heading3")) {
					third++;
					paragraphs.add(first + "." + second + "." + third + ". " + paragraph.getText());
				} else {
					paragraphs.add(paragraph.getText());
				}
			}
			// Split text in sentences
			List<String> sentences = new SplitPDFSentenceMain().splitSentences(text);
			new RuleApplicationServiceImpl().applyRules(sentences, paragraphs, listRule, companyId, fileName,
					listExtraction, null, null, text, request, outputDateFormat); //Passing pages and lines as null TO BE HANDLED LATER
		} catch (Exception e) {
			logger.error("Error in ExtractAttributes", e);
		} finally {
			if (we != null) {
				try {
					we.close();
				} catch (IOException e) {
					logger.error("Error in ExtractAttributes", e);
				}
			}
		}
	}
}