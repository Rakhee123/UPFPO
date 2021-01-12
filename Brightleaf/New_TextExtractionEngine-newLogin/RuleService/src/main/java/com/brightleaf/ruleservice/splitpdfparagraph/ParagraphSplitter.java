package com.brightleaf.ruleservice.splitpdfparagraph;

import java.io.IOException;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class ParagraphSplitter extends PDFTextStripper {
	protected final static Logger logger = Logger.getLogger(ParagraphSplitter.class);

	private List<String> listPara = null;
	private List<String> listLines = null;
	private PositionWrapper lastPosition = null;
	private PositionWrapper lastLineStartPosition = null;
	private String paraString = "";
	
	public ParagraphSplitter() throws IOException {
		//constructor
	}
	
	public List<String> getPara() {
		return listPara;
	}
	
	public void setPara(List<String> lp) {
		listPara = lp;
	}
	
	public List<String> getLines() {
		return listLines;
	}
	
	public void setLines(List<String> lines) {
		listLines = lines;
	}
	
	public String getLastParaString() {
		return paraString;
	}

	@Override
	protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
		int pNum = super.getCurrentPageNo();
		if (str != null && !str.contentEquals("") && !str.trim().contentEquals("")) {
			str = str.replaceAll("“", "\"");
			str = str.replaceAll("”", "\"");
			str = str.replaceAll("’", "'");
			str = str.replaceAll("\r\n", " ");
			listLines.add(str + "|||" + pNum);
		} else {
			return;}
		float maxHeight = -1;
		TextPosition maxtp = null;
		for (TextPosition tp : textPositions) {
			float f = tp.getHeightDir();
			if (f > maxHeight) {
				maxtp = tp;
				maxHeight = f;
			}
		}
		PositionWrapper current = new PositionWrapper(maxtp);
		
		isParagraphSeparation(current, lastPosition, lastLineStartPosition);
		if (current.isParagraphStart()) {
			char ch = str.toCharArray()[0];
			if (Character.isAlphabetic(ch) && Character.isLowerCase(ch)) {
				String pageNum = paraString.substring(paraString.indexOf("|||"));
				paraString = paraString.substring(0, paraString.indexOf("|||"));
				paraString += str + pageNum;
			} else {
				
				paraString = paraString.trim();
				if (paraString != "") {
					listPara.add(paraString);
				}
				paraString = str + "|||" + pNum;
			}
		} else {
			String pageNum = paraString.substring(paraString.indexOf("|||"));
			paraString = paraString.substring(0, paraString.indexOf("|||"));
			paraString += str + pageNum;
		}
		lastLineStartPosition = current;
	}

	protected void isParagraphSeparation(PositionWrapper position, PositionWrapper lastPosition,
			PositionWrapper lastLineStartPosition) {
	    //logger.info("last position: "+lastPosition);
		boolean result = false;
		if (lastLineStartPosition == null) {
			result = true;
		} else {
			float yGap = Math.abs(
					position.getTextPosition().getYDirAdj() - lastLineStartPosition.getTextPosition().getYDirAdj());
			float myYGap = Math.abs(
					lastLineStartPosition.getTextPosition().getY() + lastLineStartPosition.getTextPosition().getHeight() -
					position.getTextPosition().getY());
			float xGap = (position.getTextPosition().getXDirAdj()
					- lastLineStartPosition.getTextPosition().getXDirAdj());
			if (yGap < 0.0025) {
				position.setHangingIndent();
			} else if (myYGap > (getDropThreshold() * position.getTextPosition().getHeightDir() + 0.05)) {
				result = true;
			} else if (xGap > (getIndentThreshold() * position.getTextPosition().getWidthOfSpace())) {
				// text is indented, but try to screen for hanging indent
				if (!lastLineStartPosition.isParagraphStart()) {
					result = true;
				} else {
					position.setHangingIndent();
				}
			} else if (xGap < -position.getTextPosition().getWidthOfSpace()) {
				// text is left of previous line. Was it a hanging indent?
				if (!lastLineStartPosition.isParagraphStart()) {
					result = true;
				}
			} else if (Math.abs(xGap) < (0.25 * position.getTextPosition().getWidth())) {
				// current horizontal position is within 1/4 a char of the last
				// line start. We'll treat them as lined up.
				if (lastLineStartPosition.isHangingIndent()) {
					position.setHangingIndent();
				} else if (lastLineStartPosition.isParagraphStart()) {
					// check to see if the previous line looks like
					// any of a number of standard list item formats
					Pattern liPattern = matchListItemPatternn(lastLineStartPosition);
					if (liPattern != null) {
						Pattern currentPattern = matchListItemPatternn(position);
						if (liPattern == currentPattern) {
							result = true;
						}
					}
				}
			}
		}
		if (result) {
			position.setParagraphStart();
		}
	}

	protected Pattern matchListItemPatternn(PositionWrapper pw) {
		TextPosition tp = pw.getTextPosition();
		String txt = tp.getUnicode();
		return matchPattern(txt, getListItemPatterns());
	}
}
