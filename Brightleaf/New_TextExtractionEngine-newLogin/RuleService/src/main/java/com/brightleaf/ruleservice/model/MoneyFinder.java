package com.brightleaf.ruleservice.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class MoneyFinder {
	protected final static Logger logger = Logger.getLogger(MoneyFinder.class);
	List<String> strModels = null;
	String snlpClassPath = "";
	String snlpFullModelPath = "";

	public MoneyFinder() {
		snlpClassPath = Constants.SNLPBASEPATH + "/SNLP_CLASS" + "/" + Constants.SNLPJAR;
		String snlpModelPath = Constants.SNLPBASEPATH + "/SNLP_MODELS";
		strModels = new ArrayList<>();
		strModels.add(snlpModelPath + Constants.MODELPATH);
        snlpFullModelPath = snlpModelPath + "/" + "en" + Constants.POSTAGGERPATH;
	}
	
	public String wordToNumber(String text) {
		String strAnn1 = "edu.stanford.nlp.pipeline.AnnotationPipeline";
		String strAnn3 = "edu.stanford.nlp.pipeline.TokenizerAnnotator";
		String strAnn4 = "edu.stanford.nlp.pipeline.WordsToSentencesAnnotator";
		String strAnn5 = "edu.stanford.nlp.pipeline.POSTaggerAnnotator";
		String strAnn6 = "edu.stanford.nlp.pipeline.Annotation";

		String retStr = text;
		URL url = null;
		try {
			url = new URL(snlpClassPath);
		} catch (MalformedURLException e1) {
			logger.error("Error in wordToNumber", e1);
		}
		URL[] urls = new URL[] { url };
		try(URLClassLoader cl = new URLClassLoader(urls);) {
		
			Class<?> pl = cl.loadClass(strAnn1);
			Class<?> tk = cl.loadClass(strAnn3);
			Class<?> ws = cl.loadClass(strAnn4);
			Class<?> pt = cl.loadClass(strAnn5);
			Class<?> anno = cl.loadClass(strAnn6);

			Constructor<?> c2 = pl.getConstructor();
			Object pipeline = c2.newInstance();

			Constructor<?> c3 = tk.getDeclaredConstructor(boolean.class, String.class);
			Object tokenize = c3.newInstance(false, "en");

			Constructor<?> c4 = ws.getDeclaredConstructor(boolean.class);
			Object wordSentence = c4.newInstance(false);

			Constructor<?> c5 = pt.getDeclaredConstructor(String.class, boolean.class);
			   
			Object posTagger = c5.newInstance(snlpFullModelPath, false);

			Constructor<?> c6 = anno.getConstructor(String.class);
			Object annotation = c6.newInstance(text);

			Class<?> pLine = pipeline.getClass();
			Method[] pipelineMethods = pLine.getMethods();
			Method annotate = null;
			for (Method pM : pipelineMethods) {
				if (pM.getName().equals("addAnnotator")) {
					pM.invoke(pipeline, tokenize);
					pM.invoke(pipeline, wordSentence);
					pM.invoke(pipeline, posTagger);
				} else if (pM.getName().equals("annotate")) {
					Parameter[] p = pM.getParameters();
					String s = p[0].getType().toString();
					if (s.equals("class edu.stanford.nlp.pipeline.Annotation")) {
						annotate = pM;
					}
				}
			}
			if (annotate != null) {
				annotate.invoke(pipeline, annotation);
				Class<?> numberNormalizer = cl.loadClass("edu.stanford.nlp.ie.NumberNormalizer");
				Method[] numberNormalizerMethods = numberNormalizer.getMethods();
				Method findNumbers = null;
				Method wordToNumber = null;
				for (Method nnMethod : numberNormalizerMethods) {
					if (nnMethod.getName().equals("findNumbers"))
						findNumbers = nnMethod;
					else if (nnMethod.getName().equals("wordToNumber"))
						wordToNumber = nnMethod;
				}
				if (findNumbers != null && wordToNumber != null) {
					List<?> l = (List<?>) (findNumbers.invoke(null, annotation));
					for (int i = 0; i < l.size(); i++) {
						boolean bflag = true;
						String s1 = l.get(i).toString();
						for (int j = 0; j < s1.length(); j++) {
							if (!Character.isDigit(s1.charAt(j)) && s1.charAt(j) != '.' && s1.charAt(j) != ' ') {
									bflag = false;
							}
						}
						if (bflag)
							continue;
						String s2 = "";
						Number num = (Number) wordToNumber.invoke(null, s1);
						double d = num.doubleValue();
						if (num.toString().contains("E")) {
							DecimalFormat df = new DecimalFormat("0");
							df.setMaximumFractionDigits(340);
							s2 = df.format(d);
						} else if (Math.abs(d) == num.intValue()) {
							s2 = String.valueOf(num.intValue());
						} else {
							s2 = num.toString();
						}
						retStr = retStr.replace(s1,  s2);
					}
				}
			}
		}  catch (Exception e) {
			logger.error("Error in wordToNumber", e);
		} 
		return retStr;
	}

	public boolean findMoney(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction) {
		boolean success = false;
		String text = params.getChunk();
		text = wordToNumber(text);
		
		String strOut = "";
		String strRegx1 = "(?i)(\\$|\\€|\\¥|\\£|\\u20B9|((\\s)EURO)|((\\s)EUR)|((\\s)R)|((\\s)CAD)|((\\s)INR)|((\\s)dollars)|((\\s)dollar)|((\\s)USD)|(\\s)Rs)(\\s*)[0-9]{1,3}(?:,?[0-9])*(\\.\\d\\d*)?";
		String strRegx2 = "(?i)[0-9]{1,3}(?:,?[0-9])*(\\.\\d\\d*)?(\\s*)(\\$|\\€|\\¥|\\£|\\u20B9|EURO|EUR|(R(\\s))|CAD|INR|dollars|dollar|USD|Rs)";

		Pattern p = null;
		Matcher m = null;

		String strTmp = "";
		p = Pattern.compile(strRegx1);
		m = p.matcher(text);
		List<String> extractedValues = new ArrayList<>();
		while (m.find()) {
			strOut = m.group(0);
			strOut = strOut.replaceAll("\\s", "");
			strOut = strOut.replaceAll(",", "");
			strTmp = procCurrency(strOut);
			if (!extractedValues.contains(strTmp)) {
				extractedValues.add(strTmp);
			}
		}

		p = Pattern.compile(strRegx2);
		m = p.matcher(text);
		while (m.find()) {
			strOut = m.group(0);
			strOut = strOut.replaceAll("\\s", "");
			strOut = strOut.replaceAll(",", "");
			strTmp = procCurrency(strOut);
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

	private String procCurrency(String inStr) {
		int idx = 0;
		String strNum = "";
		String strUnit = "";
		String s1 = inStr.trim();
		String retStr = "";
		if (Character.isDigit(inStr.charAt(0))) {
			while (Character.isDigit(s1.charAt(idx)) || s1.charAt(idx) == '.') {
				strNum += s1.charAt(idx++);
			}
			strUnit = s1.substring(idx);
		} else {
			while (!Character.isDigit(s1.charAt(idx))) {
				strUnit += s1.charAt(idx++);
			}
			strNum = s1.substring(idx);
		}

		if (!strNum.contains(".")) {
			strNum += ".00";
		} else {
			int n = strNum.indexOf('.');
			n = strNum.length() - n;
			if (n == 2) {
				strNum += "0";
			}
		}
		if (strUnit.toLowerCase().startsWith("dollar") || strUnit.toLowerCase().startsWith("usd")) {
			strUnit = "$";
		}

		if (strUnit.equals("$")) {
			double amount = Double.parseDouble(strNum);
			Locale locale = new Locale("en", "US");
			NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
			retStr = currencyFormatter.format(amount);
		} else {
			if (strUnit.length() == 1) {
				if (Character.isLetter(strUnit.charAt(0))) {
					retStr = strUnit + " " + strNum;
				} else {
					retStr = strUnit + strNum;
				}
			} else {
				strUnit = strUnit.toUpperCase();
				retStr = strUnit + " " + strNum;
			}
		}

		return retStr;
	}
}
