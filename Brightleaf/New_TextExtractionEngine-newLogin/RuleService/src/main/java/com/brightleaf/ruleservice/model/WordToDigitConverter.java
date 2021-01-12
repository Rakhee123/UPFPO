package com.brightleaf.ruleservice.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class WordToDigitConverter {
	
	protected final static Logger logger = Logger.getLogger(WordToDigitConverter.class);
	
	List<String> strModels = null;
	String snlpClassPath = "";
	String snlpFullModelPath = "";

	public WordToDigitConverter() {
		snlpClassPath = Constants.SNLPBASEPATH + "/SNLP_CLASS" + "/" + Constants.SNLPJAR;
		String snlpModelPath = Constants.SNLPBASEPATH + "/SNLP_MODELS";
		strModels = new ArrayList<>();
		strModels.add(snlpModelPath + Constants.MODELPATH);
		snlpFullModelPath = snlpModelPath + "/" + "en" + Constants.POSTAGGERPATH;
	}

	public boolean findDigits(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction) {
		DateFinder df = new DateFinder();
		boolean success = false;
		List<ExtractedEntityForSingleAttribute> lst = new ArrayList<>();
		df.findDate(params, "", lst, null);
		if (lst.size() == 2) {
			try {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				Date date1 = format.parse(lst.get(0).getExtractedEntity());
				Date date2 = format.parse(lst.get(1).getExtractedEntity());
				long diff = date2.getTime() - date1.getTime();
				diff = diff / (24 * 60 * 60 * 1000);
				String retStr = (diff > 1) ? (diff + " Days") : "1 Day";
				ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
				e.setCompanyId(params.getCompanyId());
				e.setDocumentName(params.getDocumentName());
				e.setExtractedAttributeId(params.getRule().getAttributeId());
				e.setExtractedSentence(params.getSentence());
				e.setExtractedChunk(params.getChunk());
				e.setExtractedEntity(retStr);
				e.setAppliedRule(params.getRule().getRuleId());
				e.setPageNumber(params.getPageNumber());
				listExtraction.add(e);
				success = true;
			} catch (Exception e) {
				logger.error("Error in findDigits", e);
			}
		}
		if (success)
			return success;
		lst.clear();

		String s = preProcEnglishString(params.getChunk());
		
		while(s != ""){
			String sUnit = getEnglishUnit(s); 
            if(sUnit.equals(""))
                break;

            String strProc = s.substring(0, s.toLowerCase().indexOf(sUnit.toLowerCase()) + sUnit.length());
            String reg = "\\s+[\\w]+[-]";
    		Pattern p0 = Pattern.compile(reg);
    		Matcher m0 = p0.matcher(strProc);
    		while (m0.find() ) {
    			String replace = m0.group();
    			strProc = strProc.replace(replace, replace.substring(0, replace.length()-1) + " ");
    			m0 = p0.matcher(strProc);
    		}
            MoneyFinder mf = new MoneyFinder();
    		strProc = mf.wordToNumber(strProc);
    		List<String> listOut = new ArrayList<>();
    		findNumbers(strProc, listOut, sUnit);
    		if (!listOut.isEmpty()) {
    			for (String ee : listOut) {
    				ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
    				e.setCompanyId(params.getCompanyId());
    				e.setDocumentName(params.getDocumentName());
    				e.setExtractedAttributeId(params.getRule().getAttributeId());
    				e.setExtractedSentence(params.getSentence());
    				e.setExtractedChunk(params.getChunk());
    				String retStr = ee;
    				retStr += params.getChunk().toLowerCase().contains("business") ? " Business" : "";
    				retStr += " " + sUnit;
    				e.setExtractedEntity(retStr);
    				e.setAppliedRule(params.getRule().getRuleId());
    				e.setPageNumber(params.getPageNumber());
    				listExtraction.add(e);
    			}
    			success = true;
    		}
    		s = s.substring(s.toLowerCase().indexOf(sUnit.toLowerCase()) + sUnit.length()).trim();    
		}
		
		return success;
	}

	private void findNumbers(String s, List<String> lst, String sUnit) {
		String outStr = "";    
        Pattern p = Pattern.compile("(\\(|\\[|\\{)(\\d*)(\\)|\\]|\\})");
        Matcher m = p.matcher(s);
        while (m.find()) {
            outStr = m.group(0);
            outStr = outStr.substring(1);
            outStr = outStr.substring(0, outStr.length() - 1);
            lst.add(outStr);
        } 
		if (lst.isEmpty()) {
	        p = Pattern.compile("(\\d+(\\.\\d+)?)");
	        m = p.matcher(s);
	        while (m.find()) {
	            outStr = m.group(0);
	            Pattern p1 = Pattern.compile("(?i)("+ outStr + ")(\\s*)" + sUnit);
	            Matcher m1 = p1.matcher(s);
	            if (m1.find()) {
	            	if (!lst.contains(outStr))
	            		lst.add(outStr);
	            }
	        } 
		}
	}

	private static String preProcEnglishString(String strin) {
		String retStr = strin;
		Pattern p = Pattern.compile("(?i)(anniversary|anniversaries)");
		Matcher m = p.matcher(strin);
		if (m.find()) {
			int idx = m.toMatchResult().start();
			if (idx != -1) {
				String[] stmp = strin.split(m.group(0));
				stmp[0] = stmp[0].trim();
				if (stmp[0].endsWith("st") || stmp[0].endsWith("ST")) {
					stmp[0] = stmp[0].substring(0, stmp[0].length() - 2);
				}
				stmp[0] += " years ";
				retStr = stmp[0] + stmp[1];
			}
		}

		p = Pattern.compile("(?i)(business)");
		m = p.matcher(strin);
		if (m.find()) {
			retStr = m.replaceAll("");
		}

		return retStr;
	}

	private static String getEnglishUnit(String strin) {
		String retStr = "";

		String str1 = "(?i)(days|day|months|month|years|year|weeks|week)";

		Pattern p = Pattern.compile(str1);
		Matcher m = p.matcher(strin);
		if (m.find()) {
			retStr = m.group(0);
			retStr = retStr.toLowerCase();
			retStr = Character.toUpperCase(retStr.charAt(0)) + retStr.substring(1);
		}
		return retStr;
	}
}
