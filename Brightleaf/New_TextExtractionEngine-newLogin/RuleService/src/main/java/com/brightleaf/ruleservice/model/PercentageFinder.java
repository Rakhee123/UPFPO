package com.brightleaf.ruleservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercentageFinder {
	List<String> strModels = null;
	String snlpClassPath = "";
	String snlpFullModelPath = "";

	public PercentageFinder() {
		snlpClassPath = Constants.SNLPBASEPATH + "/SNLP_CLASS" + "/" + Constants.SNLPJAR;
		String snlpModelPath = Constants.SNLPBASEPATH + "/SNLP_MODELS";
		strModels = new ArrayList<>();
		strModels.add(snlpModelPath + Constants.MODELPATH);
        snlpFullModelPath = snlpModelPath + "/" + "en" + Constants.POSTAGGERPATH;
	}
	
	public boolean findPercentage(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction) {
		boolean success = false;
		boolean bFlag = false;
		String text = params.getChunk();
		if(text.contains("½"))
        {
			text = text.replace("½", ".5");
            bFlag = true;
        }
		
		text = preProcString(text);
		
		MoneyFinder mf = new MoneyFinder();
		text = mf.wordToNumber(text);
		
		String strRegx1 = "\\d+(?:\\.\\d+)?(\\s*)%";
        Pattern p = Pattern.compile(strRegx1);
        Matcher m = p.matcher(text); 
        List<String> extractedValues = new ArrayList<>();
        while(m.find())
        {
            String str11 = m.group(0);
            if(str11.contains(".5") && bFlag)
            {
                str11 = str11.replace(".5", "½");
            }
            String strTmp = procPercent(str11);
            if (!extractedValues.contains(strTmp)) {
				extractedValues.add(strTmp);
			}    
        }
        if (!extractedValues.isEmpty())
        	success = true;
        for (String o : extractedValues) {
			ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
			e.setCompanyId(params.getCompanyId());
			e.setDocumentName(params.getDocumentName());
			e.setExtractedAttributeId(params.getRule().getAttributeId());
			e.setExtractedSentence(params.getSentence());
			e.setExtractedChunk(params.getChunk());
			e.setExtractedEntity(o);
			e.setAppliedRule(params.getRule().getRuleId());
			e.setPageNumber(params.getPageNumber());
			listExtraction.add(e);
		}
        return success;
	}
	
	private String preProcString(String strin)
    {
        String strRet = strin;
           
        String str1 = "(?i)(percentage|percent)";
        Pattern pattern = Pattern.compile(str1);
        Matcher m = pattern.matcher(strin); 
        if(m.find())
        {
            strRet = m.replaceAll("%");
        }

        return strRet;
    }
	
	private String procPercent(String inStr)
    {
        int idx = 0;
        String strNum = "";
        String s1 = inStr.trim();
        String retStr = "";
        
        if(inStr.contains("½"))
        {
            retStr = inStr;
        }
        else
        {
            if (Character.isDigit(inStr.charAt(0))) {
                while (Character.isDigit(s1.charAt(idx)) || s1.charAt(idx) == '.') {
                    strNum += s1.charAt(idx++);
                }
            } else {
                while (!Character.isDigit(s1.charAt(idx))) {
                    idx++;
                }
                strNum = s1.substring(idx);
            }

            if (!strNum.contains(".")) {
                strNum += ".00";
            } else {
                int n = strNum.indexOf('.');
                int n1 = strNum.length() - n;
                if (n1 == 2) {
                    strNum += "0";
                }
            }
            retStr = strNum + "%";
        }
                
        return retStr;
    }
}
