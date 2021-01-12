package com.brightleaf.ruleservice.model;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class OrganizationFinder {

	protected final static Logger logger = Logger.getLogger(OrganizationFinder.class);
	List<String> strModels = null;
	String snlpClassPath = "";

	public OrganizationFinder() {
		snlpClassPath = Constants.SNLPBASEPATH + "/SNLP_CLASS" + "/" + Constants.SNLPJAR;
		String snlpModelPath = Constants.SNLPBASEPATH + "/SNLP_MODELS";
		strModels = new ArrayList<>();
		strModels.add(snlpModelPath + Constants.MODELPATH);
	}

	public boolean findOrganization(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction) {
		List<String> strOut = new ArrayList<>();
		boolean success = false;
		String strin = params.getChunk();
		List<String> saveStrings = new ArrayList<>();
		String retStr = preProc(strin, saveStrings);
		orgFind(retStr, strOut);

		List<String> strOutOrgs = new ArrayList();
		PostProc(strin, saveStrings, strOut, strOutOrgs);

		if (!strOut.isEmpty())
			success = true;

		for (String o : strOutOrgs) {
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

	private void orgFind(String strin, List<String> strOut) {
		URLClassLoader cl = null;
		try {
			URL url = new URL(snlpClassPath);
			URL[] urls = new URL[] { url };
			cl = new URLClassLoader(urls);
		} catch (MalformedURLException e) {
			logger.error("Error in orgFind", e);
			return;
		}

		Method[] methods;
		String strcrfclass = "edu.stanford.nlp.ie.crf.CRFClassifier";
		try {
			Class<?> cls = cl.loadClass(strcrfclass);
			methods = cls.getMethods();
		} catch (ClassNotFoundException e) {
			if (cl != null)
				try {
					cl.close();
				} catch (IOException e1) {
					logger.error("Error in orgFind", e1);
				}
			return;
		}

		try {
			for (Method method : methods) {
				if (method.getName().equals("getClassifierNoExceptions")) {
					for (String strModel : strModels) {
						Object o1 = method.invoke(null, strModel);
						Method[] methods1 = o1.getClass().getMethods();
						for (Method method1 : methods1) {
							if (method1.getName().equals("classifyToCharacterOffsets")) {
								ArrayList<?> arr = (ArrayList<?>) (method1.invoke(o1, strin));
								for (int i = 0; i < arr.size(); i++) {
									Object o2 = arr.get(i);
									Method[] tripleMethods = o2.getClass().getMethods();
									Method first = null;
									Method second = null;
									Method third = null;
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
													&& (f.equals("ORGANIZATION") || f.equals("PERSON"))) {
												int sec = 0;
												int thi = 0;
												sec = Integer.parseInt(second.invoke(o2).toString());
												thi = Integer.parseInt(third.invoke(o2).toString());
												String strtmp1 = strin.substring(sec, thi);
												if (strtmp1.equals("ORGANIZATION"))
													strOrg = strtmp1;
												else
													strOrg = organizationOrPerson(strin, strtmp1);

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
		} catch (Exception e) {
			logger.error("Error in orgFind", e);
		} finally {
			if (cl != null)
				try {
					cl.close();
				} catch (IOException e) {
					logger.error("Error in orgFind", e);
				}
		}
	}

	private String organizationOrPerson(String strin, String strtmp) {
		String strRet = "";
		String strRegx1 = "(?i)" + strtmp
				+ "(\\s)*(corporation|corp|incorporated|inc|ltd|llc|l\\.l\\.c\\.|pvt|private|limited|lp|l\\.p\\.)";

		strRet = strtmp;
		Pattern p;
		Matcher m;
		p = Pattern.compile(strRegx1);
		m = p.matcher(strin);
		if (m.find()) {
			MatchResult mr = m.toMatchResult();
			strRet = mr.group(0);
		}
		return strRet;
	}

	private String preProc(String strin, List<String> saveStr) {
		String strRet = "";

		String[] strinWords = strin.split(" ");
		for (String str : strinWords) {
			if (str.equals(""))
				continue;
			char ch = str.charAt(0);
			if (Character.isAlphabetic(ch))
				strRet = strRet + str.substring(0, 1).toUpperCase();
			else
				strRet = strRet + str.substring(0, 1);
			if (str.substring(0, 1).equals("(") && str.length() > 1) {
				ch = str.charAt(1);
				if (Character.isAlphabetic(ch))
					strRet = strRet + str.substring(1, 2).toUpperCase();
				else
					strRet = strRet + str.substring(1, 2);
				if (str.length() > 2)
					strRet = strRet + str.substring(2).toLowerCase();
			} else {
				if (str.length() > 1)
					strRet = strRet + str.substring(1).toLowerCase();
			}
			strRet = strRet + " ";
		}

		Pattern p;
		Matcher m;
		String strRegx1 = "(?i),(\\s)*(inc|ltd|llc|l\\.l\\.c\\.|pvt|private|limited|lp|l\\.p\\.)";
		p = Pattern.compile(strRegx1);
		m = p.matcher(strRet);
		while (m.find()) {
			if (!saveStr.contains(","))
				saveStr.add(",");
			String s = m.group(0);
			String s1 = s.substring(1);
			strRet = strRet.replace(s, s1);
		}

		String strtmp = strRet;
		int j = 0;
		strRet = "";
		while (j < strtmp.length()) {
			while (j < strtmp.length() && strtmp.charAt(j) != '(') {
				strRet += strtmp.charAt(j++);
			}
			String tmpstr = "";
			while (j < strtmp.length() && strtmp.charAt(j) != ')') {
				tmpstr += strtmp.charAt(j++);
			}
			if (j < strtmp.length() && strtmp.charAt(j) == ')' && !tmpstr.contains(" ")) {
				tmpstr = tmpstr.replace("(", "\\(");
				tmpstr += "\\)";
				saveStr.add(tmpstr);
			}
			j++;
			if (tmpstr.contains(" ")) {
				strRet += tmpstr + ")";
			}
		}

		return strRet;
	}

	private void PostProc(String saveOrigStr, List<String> saveStr, List<String> strInOrgs, List<String> strOutOrgs) {
		for (int k = 0; k < strInOrgs.size(); k++) {
			String s1 = strInOrgs.get(k);
			String s = "(?i)";
			String[] words = s1.split(" ");
			String s2 = s1;
			boolean done = false;
			for (int i = 0; i < words.length; i++) {
				if ("".equals(words[i]))
					continue;
				s = s + words[i] + "(\\s)*";
			}
			Pattern p1 = Pattern.compile(s);
			Matcher m1 = p1.matcher(saveOrigStr);
			if (m1.find()) {
				done = true;
				s2 = m1.group(0);
			}

			s = "(?i)";

			int ctr = 0;
			while (!done) {
				int place = 1;
				while (place < words.length) {
					for (int i = 0; i < words.length; i++) {
						if (i == place) {
							s = s + saveStr.get(ctr) + "(\\s)*";
						}
						if ("".equals(words[i]))
							continue;
						s = s + words[i] + "(\\s)*";
					}
					p1 = Pattern.compile(s);
					m1 = p1.matcher(saveOrigStr);
					if (m1.find()) {
						done = true;
						s2 = m1.group(0);
						break;
					}
					place++;
					s = "(?i)";
				}
				if (!done) {
					ctr++;
					if (ctr == saveStr.size())
						done = true;
				}
			}
			strOutOrgs.add(s2);
		}
	}
}
