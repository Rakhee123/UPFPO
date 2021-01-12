package com.brightleaf.ruleservice.splitpdfparagraph;

import org.apache.pdfbox.text.TextPosition;

public class PositionWrapper {
	private boolean isLineStart = false;
	private boolean isParagraphStart = false;
	private boolean isPageBreak = false;
	private boolean isHangingIndent = false;
	private boolean isArticleStart = false;
	
	private TextPosition position = null;
	
	/**
	 * returns the underlying TextPosition object
	 * @return
	 */
	protected TextPosition getTextPosition(){
		return position;
	}
	

    public boolean isLineStart() {
        return isLineStart;
    }


    /**
     * sets the isLineStart() flag to true
     */
    public void setLineStart() {
        this.isLineStart = true;
    }


    public boolean isParagraphStart() {
        return isParagraphStart;
    }


    /**
     * sets the isParagraphStart() flag to true.
     */
    public void setParagraphStart() {
        this.isParagraphStart = true;
    }


	public boolean isArticleStart() {
		return isArticleStart;
	}


	/**
	 * sets the isArticleStart() flag to true.
	 */
	public void setArticleStart() {
		this.isArticleStart = true;
	}


	public boolean isPageBreak() {
		return isPageBreak;
	}

    
    /**
     * sets the isPageBreak() flag to true
     */
   

    public boolean isHangingIndent() {
        return isHangingIndent;
    }


    /**
     * sets the isHangingIndent() flag to true
     */
    public void setHangingIndent() {
    	this.isHangingIndent = (isHangingIndent) ? true: false;
    }


    /**
     * constructs a PositionWrapper around the specified TextPosition object.
     * @param position
     */
    public PositionWrapper(TextPosition position){
        this.position = position;
    }
}
