package com.brightleaf.ruleservice.splitpdfparagraph;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

public class SplitPDFParagraphMain {

	protected final static Logger logger = Logger.getLogger(SplitPDFParagraphMain.class);

	public List<String> splitParagraphs(File file, List<String> lines) {
		List<String> paragraphs = new ArrayList<>();
		try (PDDocument document = PDDocument.load(file);) {

			ParagraphSplitter stripper = new ParagraphSplitter();
			stripper.setSortByPosition(true);
			stripper.setStartPage(0);
			stripper.setEndPage(document.getNumberOfPages());
			stripper.setDropThreshold(2.6f);
			stripper.setPara(paragraphs);
			stripper.setLines(lines);
			Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
			stripper.writeText(document, dummy); // Split the text into paragraphs
			paragraphs = stripper.getPara();
			String lastParaString = stripper.getLastParaString();
			if (lastParaString != null && !lastParaString.contentEquals(""))
				paragraphs.add(lastParaString);
		} catch (Exception e) {
			logger.error("Error in splitParagraphs", e);
		}
		return paragraphs;
	}
}
