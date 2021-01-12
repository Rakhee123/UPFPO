package com.brightleaf.ruleservice.splitsentence;

import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class SplitPDFSentenceMain {
	
	protected final static Logger logger = Logger.getLogger(SplitPDFSentenceMain.class);
	
	public String readEntireTextAndPages(File file, List<String>pages, List<String>sentences) {
		PDDocument document = null;
		String fileString = "";
		try {
			document = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(0);
			pdfStripper.setEndPage(document.getNumberOfPages());
			fileString = pdfStripper.getText(document);
			splitPDFinSentences(document, pages, sentences);
		} catch (Exception e) {
			logger.error("Error in readEntireTextAndPages", e);
		} finally {
			if (document != null) {
				try {
					document.close();
				} catch (IOException e) {
					logger.error("Error in readEntireTextAndPages", e);
				}
			}
		}
		return fileString;
	}

	public void splitPDFinSentences(PDDocument document, List<String> pages, List<String> sentences) {
		try {
			int numberOfPages = document.getNumberOfPages();
			PDFTextStripper pdfStripper = new PDFTextStripper();
			boolean incompleteSentence = false;
			for (int i = 0; i < numberOfPages; i++) {
				pdfStripper.setStartPage(i + 1);
				pdfStripper.setEndPage(i + 1);
				String text = pdfStripper.getText(document);
				text = text.replaceAll("“", "\"");
				text = text.replaceAll("”", "\"");
				text = text.replaceAll("\r\n", " ");
				text = text.replaceAll("’", "'");
				pages.add(text + "|||" + (i+1));
				List<String> pageSentences = splitSentences(text); // Split the text into sentences
				int startj = 0;
				if (incompleteSentence) {
					String s = sentences.get(sentences.size() - 1);
					String pNum = s.substring(s.indexOf("|||"));
					s = s.substring(0, s.indexOf("|||"));
					s += pageSentences.get(0) + pNum;
					sentences.set(sentences.size() - 1, s);
					startj = 1;
				}
				for (int j = startj; j < pageSentences.size(); j++)
					sentences.add(pageSentences.get(j) + "|||" + (i + 1));
				String s = pageSentences.get(pageSentences.size() - 1).trim();
				if (!s.endsWith(".")) {
					incompleteSentence = true;
				} else {
					incompleteSentence = false;}
			}
		} catch (Exception e) {
			logger.error("Error in splitPDFinSentences", e);
		} finally {
			if (document != null) {
				try {
					document.close();
				} catch (IOException e) {
					logger.error("Error in splitPDFinSentences", e);
				}
			}
		}
	}

	public List<String> splitPDFinSentences(File file) {
		PDDocument document = null;
		List<String> sentences = new ArrayList<>();
		try {
			document = PDDocument.load(file);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			
			int numberOfPages = document.getNumberOfPages();
			boolean incompleteSentence = false;
			for (int i = 0; i < numberOfPages; i++) {
				pdfStripper.setStartPage(i + 1);
				pdfStripper.setEndPage(i + 1);
				String text = pdfStripper.getText(document);
				text = text.replaceAll("“", "\"");
				text = text.replaceAll("”", "\"");
				text = text.replaceAll("’", "'");
				text = text.replaceAll("\r\n", " ");
				List<String> pageSentences = splitSentences(text); // Split the text into sentences
				int startj = 0;
				if (incompleteSentence) {
					String s = sentences.get(sentences.size() - 1);
					String pNum = s.substring(s.indexOf("|||"));
					s = s.substring(0, s.indexOf("|||"));
					s += pageSentences.get(0) + pNum;
					sentences.set(sentences.size() - 1, s);
					startj = 1;
				}
				for (int j = startj; j < pageSentences.size(); j++)
					sentences.add(pageSentences.get(j)+ "|||" + (i+1));
				String s = pageSentences.get(pageSentences.size() - 1).trim();
				if (!s.endsWith(".")) {
					incompleteSentence = true;
				} else {
					incompleteSentence = false;}
			}
		} catch (Exception e) {
			logger.error("Error in splitPDFinSentences", e);
		} finally {
			if (document != null) {
				try {
					document.close();
				} catch (IOException e) {
					logger.error("Error in splitPDFinSentences", e);
				}
			}
		}
		return sentences;
	}

	public List<String> splitSentences(String text) {
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
      return sentenceSplitter(iterator, text);
	}
	
	private List<String> sentenceSplitter(BreakIterator bi, String source) {
      List<String> sentences = new ArrayList<>();
      bi.setText(source);

      int lastIndex = bi.first();
      while (lastIndex != BreakIterator.DONE) {
          int firstIndex = lastIndex;
          lastIndex = bi.next();

          if (lastIndex != BreakIterator.DONE) {
              String sentence = source.substring(firstIndex, lastIndex);
              sentences.add(sentence);
          }
      }
      return sentences;
  }
}
