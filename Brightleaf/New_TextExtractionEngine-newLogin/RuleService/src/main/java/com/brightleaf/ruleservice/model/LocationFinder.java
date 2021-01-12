package com.brightleaf.ruleservice.model;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class LocationFinder {
	protected final static Logger logger = Logger.getLogger(LocationFinder.class);
	List<String> strModels = null;
	String snlpClassPath = "";

	public LocationFinder() {
		snlpClassPath = Constants.SNLPBASEPATH + "/SNLP_CLASS" + "/" + Constants.SNLPJAR;
		String snlpModelPath = Constants.SNLPBASEPATH + "/SNLP_MODELS";
		strModels = new ArrayList<>();
		strModels.add(snlpModelPath + Constants.MODELPATH);
	}

	public boolean findLocation(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction) {
		boolean success = false;
		List<String> strOut = new ArrayList<>();
		locFind(params.getChunk(), strOut);
		if (strOut.isEmpty()) {
			if (params.getChunk().toLowerCase().contains("dutch law")) {
				strOut.add("Dutch Law");
            }
            if (params.getChunk().toLowerCase().contains("polish law")) {
            	strOut.add("Polish Law");
            }
            if (params.getChunk().toLowerCase().contains("english law")) {
            	strOut.add("English Law");
            }
            if (params.getChunk().toLowerCase().contains("german law")) {
            	strOut.add("German Law");
            }
		}
		if (!strOut.isEmpty())
			success = true;

		for (String o : strOut) {
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

	public void locFind(String text, List<String> strOut) {
		URL url = null;
		try {
			url = new URL(snlpClassPath);
		} catch (MalformedURLException e1) {
			logger.error("Error in locFind", e1);
		}
	
		URL[] urls = new URL[] { url };
		try (URLClassLoader cl = new URLClassLoader(urls);){
			

			Method[] methods;
			String strcrfclass = "edu.stanford.nlp.ie.crf.CRFClassifier";

			Class<?> cls = cl.loadClass(strcrfclass);
			methods = cls.getMethods();

			for (Method method : methods) {
				if (method.getName().equals("getClassifierNoExceptions")) {
					for (String strModel : strModels) {
						Object o1 = method.invoke(null, strModel);
						Method[] methods1 = o1.getClass().getMethods();
						for (Method method1 : methods1) {
							if (method1.getName().equals("classifyToCharacterOffsets")) {
								ArrayList<?> arr = (ArrayList<?>) (method1.invoke(o1, text));
								for (int i = 0; i < arr.size(); i++) {
									Object o2 = arr.get(i);
									Method[] tripleMethods = o2.getClass().getMethods();
									Method first = null;
									Method second = null;
									Method	third = null;
									for (Method tm : tripleMethods) {
										if (tm.getName().equals("first")) {
											first = tm;
										} else if (tm.getName().equals("second")) {
											second = tm;
										} else if (tm.getName().equals("third")) {
											third = tm;
										}
										if (first != null && second != null && third != null) {
											String strOrg = "";
											String f = first.invoke(o2).toString();
											if (f != null && strModel.contains("/ner/english")
													&& f.equals("LOCATION")) {
												int sec = 0; 
												int thi = 0;
												sec = Integer.parseInt(second.invoke(o2).toString());
												thi = Integer.parseInt(third.invoke(o2).toString());
												strOrg = text.substring(sec, thi);
												if (!strOrg.equals("") && !strOut.contains(strOrg)) {
														strOut.add(strOrg);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} 
		catch (Exception e) {
			logger.error("Error in locFind", e);
		}
	}
}
