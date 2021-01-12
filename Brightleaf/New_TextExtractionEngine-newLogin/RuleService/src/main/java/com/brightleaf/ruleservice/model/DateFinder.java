package com.brightleaf.ruleservice.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.time.SUTime.Temporal;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;

public class DateFinder {

	protected final static Logger logger = Logger.getLogger(DateFinder.class);

	private String defssutime = "";
	private String holidaysutime = "";
	private String _sutime = "";
	private String snlpClassPath = "";

	public DateFinder() {
		defssutime = Constants.SUTIMEPATH + "/defs.sutime.txt";
		holidaysutime = Constants.SUTIMEPATH + "/english.holidays.sutime.txt";
		_sutime = Constants.SUTIMEPATH + "/english.sutime.txt";
		snlpClassPath = Constants.SNLPBASEPATH + "/SNLP_CLASS" + "/" + Constants.SNLPJAR;
	}
	
	public boolean findDate(ParameterList params, String referenceDate,
			List<ExtractedEntityForSingleAttribute> listExtraction, String outputDateFormat) {
		boolean success = false;
		String strToProcess = preProcess(params.getChunk());
		if (referenceDate == null || referenceDate.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			referenceDate = dateFormat.format(new Date());
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dateFormat.parse(referenceDate);
			} catch (Exception e) {
				referenceDate = dateFormat.format(new Date());
			}
		}

		Properties props = new Properties();
		String sutimeRules = defssutime + "," + holidaysutime + "," + _sutime;
		props.setProperty("sutime.rules", sutimeRules);
		props.setProperty("sutime.binders", "0");
		props.setProperty("sutime.markTimeRanges", "true");
		props.setProperty("sutime.includeRange", "true");
		try {
			AnnotationPipeline pipeline = null;
			pipeline = new AnnotationPipeline();
			pipeline.addAnnotator(new TokenizerAnnotator(false));
			pipeline.addAnnotator(new TimeAnnotator("sutime", props));

			Annotation annotation = new Annotation(strToProcess);
			annotation.set(CoreAnnotations.DocDateAnnotation.class, referenceDate);
			pipeline.annotate(annotation);
			List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
			for (CoreMap cm : timexAnnsAll) {
				Temporal temporal = cm.get(TimeExpression.Annotation.class).getTemporal();
				String dateStr = temporal.toString();
				if (dateStr.contains("(") && dateStr.contains(")")) {
					dateStr = dateStr.replace("(", "");
					dateStr = dateStr.replace(")", "");
					String[] dates = dateStr.split(",");
					for (String date : dates) {
						String outDateStr = postProcess(date, outputDateFormat);
						if (!outDateStr.contentEquals("")) {
							if (!alreadyExists(listExtraction, params, outDateStr)) {
								ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
								e.setCompanyId(params.getCompanyId());
								e.setDocumentName(params.getDocumentName());
								e.setExtractedAttributeId(params.getRule().getAttributeId());
								e.setExtractedSentence(params.getSentence());
								e.setExtractedChunk(params.getChunk());
								e.setExtractedEntity(outDateStr);
								e.setAppliedRule(params.getRule().getRuleId());
								e.setPageNumber(params.getPageNumber());
								listExtraction.add(e);
								success = true;
							}
						}
					}
				} else {
					String outDateStr = postProcess(dateStr, outputDateFormat);
					if (!outDateStr.contentEquals("")) {
						if (!alreadyExists(listExtraction, params, outDateStr)) {
							ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
							e.setCompanyId(params.getCompanyId());
							e.setDocumentName(params.getDocumentName());
							e.setExtractedAttributeId(params.getRule().getAttributeId());
							e.setExtractedSentence(params.getSentence());
							e.setExtractedChunk(params.getChunk());
							e.setExtractedEntity(outDateStr);
							e.setAppliedRule(params.getRule().getRuleId());
							e.setPageNumber(params.getPageNumber());
							listExtraction.add(e);
							success = true;
						}
					}
				}

			}

		} catch (Exception e) {
			logger.error("Error in findDate", e);
		}

		return success;
	}

	public boolean findDate1(ParameterList params, String referenceDate,
			List<ExtractedEntityForSingleAttribute> listExtraction, String outputDateFormat) {
		boolean success = false;
		String strToProcess = preProcess(params.getChunk());
		if (referenceDate == null || referenceDate.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			referenceDate = dateFormat.format(new Date());
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				dateFormat.parse(referenceDate);
			} catch (Exception e) {
				referenceDate = dateFormat.format(new Date());
			}
		}
		URLClassLoader cl = null;
		try {
			URL url = new URL(snlpClassPath);
			URL[] urls = new URL[] { url };
			cl = new URLClassLoader(urls);

			String strAnn1 = "edu.stanford.nlp.pipeline.AnnotationPipeline";
			Class<?> pl = cl.loadClass(strAnn1);
			Constructor<?> c2 = pl.getConstructor();
			Object pipeline = c2.newInstance();

			String strAnn3 = "edu.stanford.nlp.pipeline.TokenizerAnnotator";
			Class<?> tk = cl.loadClass(strAnn3);
			Constructor<?> c3 = tk.getDeclaredConstructor(boolean.class, String.class);
			Object tokenize = c3.newInstance(false, "en");

			Properties props = new Properties();
			String sutimeRules = defssutime + "," + holidaysutime + "," + _sutime;
			props.setProperty("sutime.rules", sutimeRules);
			props.setProperty("sutime.binders", "0");
			props.setProperty("sutime.markTimeRanges", "true");
			props.setProperty("sutime.includeRange", "true");

			String strAnn4 = "edu.stanford.nlp.time.TimeAnnotator";
			Class<?> tik = cl.loadClass(strAnn4);
			//Constructor<?> c4 = tik.getDeclaredConstructor(String.class, Properties.class);
			Constructor<?> c4 = tik.getConstructor();
			Object timeAnno = c4.newInstance();//"sutime", props);//new TimeAnnotator("sutime", props);//

			String strAnn6 = "edu.stanford.nlp.pipeline.Annotation";
			Class<?> anno = cl.loadClass(strAnn6);
			Constructor<?> c6 = anno.getConstructor(String.class);
			Method[] mArr = anno.getMethods();
			Object annotation = c6.newInstance(strToProcess);

			String s1 = "edu.stanford.nlp.ling.CoreAnnotations";
			Class<?> coreAnno = cl.loadClass(s1);
			Class<?>[] classes = coreAnno.getClasses();
			Class<?> docDA = null;
			for (Class<?> c : classes) {
				if (c.getName().contentEquals("edu.stanford.nlp.ling.CoreAnnotations$DocDateAnnotation")) {
					docDA = c;
					break;
				}
			}
			for (Method m : mArr) {
				if (m.getName().contentEquals("set")) {
					m.invoke(annotation, docDA, referenceDate);
					break;
				}
			}
			Class<?> pLine = pipeline.getClass();
			Method[] pipelineMethods = pLine.getMethods();
			Method annotate = null;
			for (Method pM : pipelineMethods) {
				if (pM.getName().equals("addAnnotator")) {
					pM.invoke(pipeline, tokenize);
					pM.invoke(pipeline, timeAnno);
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
				Class<?> timeAnnotations = cl.loadClass("edu.stanford.nlp.time.TimeAnnotations");
				Class<?>[] classList = timeAnnotations.getClasses();
				Class<?> timexAnno = null;
				for (Class<?> c : classList) {
					if (c.getName().equals("edu.stanford.nlp.time.TimeAnnotations$TimexAnnotations")) {
						timexAnno = c;
						break;
					}
				}
				if (timexAnno != null) {
					if (timexAnno != null) {
						for (Method m : mArr) {
							if (m.getName().contentEquals("get")) {
								Object o = m.invoke(annotation, timexAnno);
								@SuppressWarnings("unchecked")
								List<CoreMap> timexAnnsAll = (List<CoreMap>) o;
								for (CoreMap cm : timexAnnsAll) {
									Temporal temporal = cm.get(TimeExpression.Annotation.class).getTemporal();
									String dateStr = temporal.toString();
									if (dateStr.contains("(") && dateStr.contains(")")) {
										dateStr = dateStr.replace("(", "");
										dateStr = dateStr.replace(")", "");
										String[] dates = dateStr.split(",");
										for (String date : dates) {
											String outDateStr = postProcess(date, outputDateFormat);
											if (!outDateStr.contentEquals("")) {
												if (!alreadyExists(listExtraction, params, outDateStr)) {
													ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
													e.setCompanyId(params.getCompanyId());
													e.setDocumentName(params.getDocumentName());
													e.setExtractedAttributeId(params.getRule().getAttributeId());
													e.setExtractedSentence(params.getSentence());
													e.setExtractedChunk(params.getChunk());
													e.setExtractedEntity(outDateStr);
													e.setAppliedRule(params.getRule().getRuleId());
													e.setPageNumber(params.getPageNumber());
													listExtraction.add(e);
													success = true;
												}
											}
										}
									} else {
										String outDateStr = postProcess(dateStr, outputDateFormat);
										if (!outDateStr.contentEquals("")) {
											if (!alreadyExists(listExtraction, params, outDateStr)) {
												ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
												e.setCompanyId(params.getCompanyId());
												e.setDocumentName(params.getDocumentName());
												e.setExtractedAttributeId(params.getRule().getAttributeId());
												e.setExtractedSentence(params.getSentence());
												e.setExtractedChunk(params.getChunk());
												e.setExtractedEntity(outDateStr);
												e.setAppliedRule(params.getRule().getRuleId());
												e.setPageNumber(params.getPageNumber());
												listExtraction.add(e);
												success = true;
											}
										}
									}
								}
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return success;
	}

	private boolean alreadyExists(List<ExtractedEntityForSingleAttribute> listExtraction, ParameterList params,
			String outDateStr) {
		for (ExtractedEntityForSingleAttribute e : listExtraction) {
			if (e.getCompanyId() == params.getCompanyId() && e.getDocumentName().contentEquals(params.getDocumentName())
					&& e.getExtractedAttributeId() == params.getRule().getAttributeId()
					&& e.getExtractedSentence().contentEquals(params.getSentence())
					&& e.getExtractedChunk().contentEquals(params.getChunk())
					&& e.getExtractedEntity().contentEquals(outDateStr)
					&& e.getAppliedRule() == params.getRule().getRuleId()
					&& e.getPageNumber() == params.getPageNumber()) {
				return true;
			}
		}
		return false;
	}

	private String postProcess(String dateStr, String outputDateFormat) {
		String retStr = "";
		if (dateStr.contains("XX"))
			return retStr;
		Pattern p = Pattern.compile("(\\d{4})(-)(0[1-9]|1[0-2])(-)(0[1-9]|[12]\\d|30|31)");
		Matcher m = p.matcher(dateStr);
		if (m.find())
			retStr = dateStr;
		else {
			p = Pattern.compile("(\\d{4})(-)(0[1-9]|1[0-2])");
			m = p.matcher(dateStr);
			if (m.find())
				retStr = dateStr + "-01";
		}
		if (retStr.contentEquals(""))
			return retStr;
				
		// Date is in YYYY-MM-DD format. Convert it to required format
		String convertedDate = retStr;
		if (outputDateFormat != null) {
			p = Pattern.compile("(?i)(dd)(-|/|\\.)(mm)(-|/|\\.)(yyyy)");
			m = p.matcher(outputDateFormat);
			String year = retStr.substring(0, 4);
			String month = retStr.substring(5, 7);
			String day = retStr.substring(8);
			String seperator = outputDateFormat.substring(2, 3);
			if (m.find()) {
				convertedDate = day + seperator + month + seperator + year; 
			} else {
				p = Pattern.compile("(?i)(mm)(-|/|\\.)(dd)(-|/|\\.)(yyyy)");
				m = p.matcher(outputDateFormat);
				if (m.find()) {
					convertedDate = month + seperator + day + seperator + year; 
				} else {
					p = Pattern.compile("(?i)(yyyy)(-|/|\\.)(mm)(-|/|\\.)(dd)");
					m = p.matcher(outputDateFormat);
					if (m.find()) {
						seperator = outputDateFormat.substring(4, 5);
						convertedDate = year + seperator + month + seperator + day; 
					}
				}
			}
		}
		return convertedDate;
	}

	private String preProcess(String strin) {
		String retStr = strin;// .toLowerCase();
		Pattern p = Pattern.compile("\\d\\d*(st|nd|rd|th)+");
		Matcher m = p.matcher(retStr);
		if (m.find()) {
			String strtmp = m.group(0);
			p = Pattern.compile("\\d\\d*");
			m = p.matcher(strtmp);
			if (m.find()) {
				retStr = retStr.replace(strtmp, m.group(0));
			}
		}
		retStr = retStr.replaceAll("(?i)d(\\s*)a(\\s*)y(\\s*)o(\\s*)f", "");
		retStr = retStr.replaceAll("\\((.*?)\\)", "");
		retStr = retStr.replaceAll(",(\\s|,)*", ", ");
		retStr = retStr.replaceAll("_", " ");

		// Replace XX.XX.XXXX format to XX-XX-XX format
		p = Pattern.compile("(\\d\\d*)(\\.)(\\d\\d*)(\\.)(\\d\\d*)");
		m = p.matcher(retStr);
		while (m.find()) {
			String s = m.group(0);
			String s1 = s.replaceAll("\\.", "-");
			if (retStr.contains(s))
				retStr = retStr.replace(s, s1);
		}
		// Check if any date format has 2 digit year. If yes then suffix it with 20 or
		// 19.
		String DMYFormat = "(\\b(0?[1-9]|[12]\\d|30|31)(\\/|\\-|\\.)(\\s|,)*(0?[1-9]|1[0-2])(\\/|\\-|\\.)(\\s|,)*(\\d{2})\\b)";
		p = Pattern.compile(DMYFormat);
		m = p.matcher(retStr);
		while (m.find()) {
			String s = m.group(0);
			String s1 = s.substring(s.length() - 2);
			s1 = "20" + s1;
			if (retStr.contains(s))
				retStr = retStr.replace(s, s1);
		}
		String MDYFormat = "(\\b(0?[1-9]|1[0-2])(\\/|\\.|\\-)(\\s|,)*(0?[1-9]|[12]\\d|30|31)(\\s|,)*(\\/|\\.|\\-)(\\d{2})\\b)";
		p = Pattern.compile(MDYFormat);
		m = p.matcher(retStr);
		while (m.find()) {
			String s = m.group(0);
			String s1 = s.substring(s.length() - 2);
			s1 = "20" + s1;
			if (retStr.contains(s))
				retStr = retStr.replace(s, s1);
		}
		String YMDFormat = "(\\b(\\d{2})(\\/|\\.|\\-)(\\s|,)*(0?[1-9]|1[0-2])(\\/|\\.|\\-)(\\s|,)*(0?[1-9]|[12]\\d|30|31)\\b)";
		p = Pattern.compile(YMDFormat);
		m = p.matcher(retStr);
		while (m.find()) {
			String s = m.group(0);
			String s1 = s.substring(0, 2);
			s1 = "20" + s1;
			if (retStr.contains(s))
				retStr = retStr.replace(s, s1);
		}
		String monthList = "january|february|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec";
		String DMYMonthWithDelimiter = "(?i)(\\b(\\d+)(\\s|\\.|\\/|\\-|,)*(?<Month>" + monthList
				+ ")(\\s|\\/|\\.|\\-|,)*(\\d{2})(\\s)+)";
		p = Pattern.compile(DMYMonthWithDelimiter);
		m = p.matcher(retStr);
		while (m.find()) {
			String s = m.group(0).trim();
			String s1 = s.substring(s.length() - 2);
			s1 = "20" + s1;
			String s2 = s.replace(s.substring(s.length() - 2), s1);
			s2 = s2.replaceAll("(\\.|\\/|\\-)", " ");
			if (retStr.contains(s))
				retStr = retStr.replace(s, s2);
			p = Pattern.compile("(?i) (" + monthList + ")(\\s)");
			m = p.matcher(retStr);
			while (m.find()) {
				retStr = retStr.replace(m.group(0).trim(), m.group(0).trim() + ", ");
			}
		}
		String MDYWithMonth = "(?i)(\\b(?<Month>" + monthList
				+ ")(\\s|\\/|\\.|\\-)*(?<Date>\\d+)(\\s|,)(\\s*)(\\d{2})(\\s)+)";
		p = Pattern.compile(MDYWithMonth);
		m = p.matcher(retStr);
		while (m.find()) {
			String s = m.group(0);
			String s1 = s.substring(s.length() - 2);
			s1 = "20" + s1;
			if (retStr.contains(s))
				retStr = retStr.replace(s, s1);
		}
		String dateWithoutDelimiters = "(?i)(\\d+)(" + monthList + ")(\\d+)";
		p = Pattern.compile(dateWithoutDelimiters);
		m = p.matcher(retStr);
		while (m.find()) {
			String s = m.group(0);
			String s1 = m.group(1);
			String s2 = m.group(2);
			String s3 = m.group(3);
			if (retStr.contains(s))
				retStr = retStr.replace(s, s1 + " " + s2 + ", " + s3);
		}

		return retStr;
	}
}
