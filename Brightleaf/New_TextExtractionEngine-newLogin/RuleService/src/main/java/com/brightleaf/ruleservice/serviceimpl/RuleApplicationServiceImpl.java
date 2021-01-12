package com.brightleaf.ruleservice.serviceimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.brightleaf.ruleservice.model.AddressFinder;
import com.brightleaf.ruleservice.model.CustomizeList;
import com.brightleaf.ruleservice.model.CustomizeListFinder;
import com.brightleaf.ruleservice.model.DateFinder;
import com.brightleaf.ruleservice.model.ExtractedEntityForSingleAttribute;
import com.brightleaf.ruleservice.model.LocationFinder;
import com.brightleaf.ruleservice.model.MoneyFinder;
import com.brightleaf.ruleservice.model.OrganizationFinder;
import com.brightleaf.ruleservice.model.ParameterList;
import com.brightleaf.ruleservice.model.PercentageFinder;
import com.brightleaf.ruleservice.model.Rule;
import com.brightleaf.ruleservice.model.SpaceHandling;
import com.brightleaf.ruleservice.model.WordFinder;
import com.brightleaf.ruleservice.model.WordToDigitConverter;
import com.brightleaf.ruleservice.util.AttributeUtil;

@Service
public class RuleApplicationServiceImpl {

	protected final static Logger logger = Logger.getLogger(RuleApplicationServiceImpl.class);

	private static final String BEFORE = "before";
	private static final String STAR_QSTN = "(.*?)";

	// This function will apply every rule on paragraph or sentence depending upon
	// the type of attribute. Once entity is
	// extracted using a certain rule, other rules for that attribute will be
	// ignored.
	// Once the list of extracted entities is formed, the return list (which is
	// returned from this function) is formed
	// which will contain every attribute no matter whether any extraction is done
	// for that attribute or not.
	public void applyRules(List<String> sentences, List<String> paragraphs, List<Rule> listRule, Integer companyId,
			String docName, List<ExtractedEntityForSingleAttribute> listExtraction, List<String> pages,
			List<String> lines, String fileString, HttpServletRequest request, String outputDateFormat) {

		int count = listRule.size();
		int i = 0;
		List<CustomizeList> inCustomList = new ArrayList<>();
		List<CustomizeList> customList = new ArrayList<>();
		List<Rule> applyRule = new ArrayList<>();
		Iterator<Rule> iterator = listRule.iterator();
		while (iterator.hasNext()) {
			applyRule.add(iterator.next().clone());
		}
		while (i < count) {
			Rule r = applyRule.get(i);
			Integer attribId = applyRule.get(i).getAttributeId();
			if (attribId == -1) {
				i++;
				continue;
			}
			AttributeUtil attributeUtil = new AttributeUtil();
			JSONObject attributeJSON = attributeUtil.getAttributeByAttributeId(attribId, request);
			String attributeType = (String) attributeJSON.get("attributeType");
			boolean found = false;
			if (attributeType.contentEquals("Customized Attribute")) { // For "Customize List" type of attributes, get
																		// the list from database
				for (CustomizeList l : customList) {
					if (l.getAttributeId() == attribId) {
						found = true;
						break;
					}
				}
				if (!found) {
					attributeUtil.getCustomList(request, attribId, inCustomList);
					// Customized list names can contain multiple names pipe separated so make proper list here
					for (CustomizeList c: inCustomList) {
						if (c.getName().contains("|")) {
							String[] splitStr = c.getName().split("[|]");
							for (String str: splitStr) {
								CustomizeList c1 = new CustomizeList();
								c1.setAttributeId(c.getAttributeId());
								c1.setCustomizeListId(c.getCustomizeListId());
								c1.setDefaultValue(c.getDefaultValue());
								c1.setName(str);
								c1.setValue(c.getValue());
								customList.add(c1);
							}
						} else {
							CustomizeList c1 = new CustomizeList();
							c1.setAttributeId(c.getAttributeId());
							c1.setCustomizeListId(c.getCustomizeListId());
							c1.setDefaultValue(c.getDefaultValue());
							c1.setName(c.getName());
							c1.setValue(c.getValue());
							customList.add(c1);
						}
					}
				}
			}

			boolean success = false;
			Object obj = attributeJSON.get("paragraph");
			if (obj != null && (obj.toString().toLowerCase().contentEquals("para")
					|| obj.toString().toLowerCase().contentEquals("yes"))) {
				success = applyParagraphRule(paragraphs, r, companyId, docName, listExtraction, attributeJSON,
						fileString, pages, lines, customList, outputDateFormat);
			} else {
				success = applySentenceRule(sentences, r, companyId, docName, listExtraction, attributeJSON, customList,
						outputDateFormat);
			}
			i++;
			if (success) {
				int j = i;
				while (j < applyRule.size()) {
					if (applyRule.get(j).getAttributeId().equals(attribId)) {
						Rule r1 = applyRule.get(j);
						r1.setAttributeId(-1);
						applyRule.set(j, r1);
					}
					j++;
				}
			}
		}
	}

	// Apply the rules of extraction depending upon the type of attribute. These are
	// digester functions according to old system
	private boolean applyExtractionRules(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction,
			JSONObject attributeJSON, List<CustomizeList> customList, String outputDateFormat) {
		boolean success = false;
		// Before applying Stanford NLP, make sure the text is properly ordered.
		// Arrange the words properly
		SpaceHandling sh = SpaceHandling.getInstance();
		params.setChunk(sh.processText(params.getChunk()));

		Rule r = params.getRule();

		// hitting Document service to get the TypeOfAttribute
		Object obj = attributeJSON.get("attributeType");
		String attributeType = "";
		if (obj != null) {
			attributeType = (String) obj;
		}

		if (attributeType.contains("Organization Finder")) {
			OrganizationFinder of = new OrganizationFinder();
			success = of.findOrganization(params, listExtraction);
		} else if (attributeType.contains("Date Extractor")) {
			DateFinder df = new DateFinder();
			success = df.findDate(params, "", listExtraction, outputDateFormat);
		} else if (attributeType.contains("Address Finder")) {
			AddressFinder af = new AddressFinder();
			success = af.findAddress(params, listExtraction);
		} else if (attributeType.contains("Location Finder")) {
			LocationFinder lf = new LocationFinder();
			success = lf.findLocation(params, listExtraction);
		} else if (attributeType.contains("Currency Extractor")) {
			MoneyFinder mf = new MoneyFinder();
			success = mf.findMoney(params, listExtraction);
		} else if (attributeType.contains("Percentage Extractor")) {
			PercentageFinder pf = new PercentageFinder();
			success = pf.findPercentage(params, listExtraction);
		} else if (attributeType.contains("Word to Digit Converter")) {
			WordToDigitConverter wdc = new WordToDigitConverter();
			success = wdc.findDigits(params, listExtraction);
		} else if (attributeType.contains("Find Word")) {
			WordFinder wf = new WordFinder();
			wf.findWord(params, listExtraction);
		} else if (attributeType.contains("Customized Attribute")) {
			CustomizeListFinder cl = new CustomizeListFinder();
			cl.findFromCustomizeList(params, listExtraction, customList);
		} else {
			ExtractedEntityForSingleAttribute e = new ExtractedEntityForSingleAttribute();
			e.setCompanyId(params.getCompanyId());
			e.setDocumentName(params.getDocumentName());
			e.setExtractedAttributeId(r.getAttributeId());
			e.setExtractedSentence(params.getSentence());
			e.setExtractedChunk(params.getChunk());
			e.setExtractedEntity(params.getChunk());
			e.setAppliedRule(r.getRuleId());
			e.setPageNumber(params.getPageNumber());
			listExtraction.add(e);
			success = true;
		}
		return success;
	}

	private String initializeRule(Rule rs, String befAf) {
		String r = "";

		try {
			if (befAf.contentEquals(BEFORE)) {
				String op = rs.getOpBefore1();
				if (op != null && op != "") {
					String op1 = rs.getOpBefore2();
					if (op1 != null && !op1.equals("") && !op.equals(op1)) {
						r = "(";
					}
				}
			} else {
				String op = rs.getOpAfter1();
				if (op != null && op != "") {
					String op1 = rs.getOpAfter2();
					if (op1 != null && !op1.equals("") && !op.equals(op1)) {
						r = "(";
					}
				}
			}
			String prevOp = "";
			for (int befI = 1; befI <= 5; befI++) {
				String s = "";
				switch (befI) {
				case 1:
					if (befAf.contentEquals(BEFORE)) {
						s = rs.getTextBefore1();
					} else {
						s = rs.getTextAfter1();
					}
					break;
				case 2:
					if (befAf.contentEquals(BEFORE)) {
						s = rs.getTextBefore2();
					} else {
						s = rs.getTextAfter2();
					}
					break;
				case 3:
					if (befAf.contentEquals(BEFORE)) {
						s = rs.getTextBefore3();
					} else {
						s = rs.getTextAfter3();
					}
					break;
				case 4:
					if (befAf.contentEquals(BEFORE)) {
						s = rs.getTextBefore4();
					} else {
						s = rs.getTextAfter4();
					}
					break;
				case 5:
					if (befAf.contentEquals(BEFORE)) {
						s = rs.getTextBefore5();
					} else {
						s = rs.getTextAfter5();
					}
					break;
				default:
					logger.warn("no case matched in switch");
				}

				if (s != null && !s.equals("")) {
					// Apply special character
//					if (s.equals("."))
//						r += "\\.";
//					else
//						r += s;
					String tmpStr = applySpecialCharacterEscape(s);
					tmpStr = addSpaces(tmpStr);
					r += tmpStr;
					if (befI < 5) {
						String o = "";
						switch (befI) {
						case 1:
							if (befAf.contentEquals(BEFORE)) {
								o = rs.getOpBefore1();
							} else {
								o = rs.getOpAfter1();
							}
							break;
						case 2:
							if (befAf.contentEquals(BEFORE)) {
								o = rs.getOpBefore2();
							} else {
								o = rs.getOpAfter2();
							}
							break;
						case 3:
							if (befAf.contentEquals(BEFORE)) {
								o = rs.getOpBefore3();
							} else {
								o = rs.getOpAfter3();
							}
							break;
						case 4:
							if (befAf.contentEquals(BEFORE)) {
								o = rs.getOpBefore4();
							} else {
								o = rs.getOpAfter4();
							}
							break;
						default:
							logger.warn("no case matched in switch");
						}

						if (o != null && !o.equals("")) {
							o = o.toLowerCase();
							if (o.equals("or")) {
								if (prevOp.equals(""))
									r += "|";
								else {
									if (!prevOp.equals(o))
										r += ")|";
									else
										r += "|";
								}
							} else if (o.equals("and")) {
								if (prevOp.equals(""))
									r += STAR_QSTN;
								else {
									if (!prevOp.equals(o))
										r += ")(.*?)";
									else
										r += STAR_QSTN;
								}

							}
						}
						prevOp = o;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in InitializeRule", e);
		}
		return r;
	}

	private String addSpaces(String regEx) {
		char[] arr = regEx.toCharArray();
        String newRegex = "";
        int ctr = 0;
        while (ctr < arr.length) {
            char ch = arr[ctr];
            if (ch == '\\') {
                newRegex += ch;
                ctr++;
                if (ctr < arr.length) {
                	newRegex += ch;
                	ctr++;
                }
                continue;
            }
            if (Character.isLetterOrDigit(ch)) {
            	newRegex += ch + "\\s*";
            } else {
                newRegex += ch;
            }
            ctr++;
        }
        return newRegex;
	}

	private String applySpecialCharacterEscape(String s) {
		String[] specialChars = { "\\", ".", "^", "$", "*", "+", "-", "?", "(", ")", "[", "]", "{", "}", "|" };
		String retStr = s;
		for (String tmp : specialChars) {
			if (retStr.contains(tmp)) {
				retStr = retStr.substring(0, retStr.indexOf(tmp)) + "\\" + retStr.substring(retStr.indexOf(tmp));
			}
		}

		return retStr;
	}

	// Normal rule is the rule where text before attribute and text after attribute
	// is specified
	// This is similar to existing system
	private String applyNormalRule(Rule r, String s) {
		try {
			Pattern p1;
			// Apply before rule first
			Boolean ignoreCase = r.getIgnoreCase();
			if (ignoreCase == null)
				ignoreCase = false;
			logger.info("ignoreCase=" + ignoreCase.toString());
			String rule = initializeRule(r, BEFORE);
			if (rule.contentEquals("")) {
				rule = "(.*)(\\.)";
			}
			if (ignoreCase)
				p1 = Pattern.compile("(?i)" + rule);
			else
				p1 = Pattern.compile(rule);

			Matcher m1 = p1.matcher(s);
			if (m1.find()) {
				// If before rule can be applied, try to apply after rule
				MatchResult mr = m1.toMatchResult();
				String strRet = mr.group(0);
				logger.info("text before =" + strRet);
				String s1 = s.substring(s.indexOf(strRet) + strRet.length());
				rule = initializeRule(r, "after");
				if (rule.contentEquals(""))
					rule = "\\.";
				if (ignoreCase)
					p1 = Pattern.compile("(?i)" + STAR_QSTN + "(" + rule + ")");
				else
					p1 = Pattern.compile(STAR_QSTN + "(" + rule + ")");
				m1 = p1.matcher(s1);
				if (m1.find()) {
					mr = m1.toMatchResult();
					Boolean merge = r.getMerge();
					if (merge == null)
						merge = false;
					logger.info("merge =" + merge);
					logger.info("mr.group(0) =" + mr.group(0));
					logger.info("mr.group(1) =" + mr.group(1));
					if (r.getMerge() != null && r.getMerge() == true) {
						strRet = strRet + mr.group(0);
					} else {
						strRet = mr.group(1);
					}
					return strRet;
				}
			}
		} catch (Exception e) {
			logger.error("Error in applyNormalRule", e);
		}
		return "";
	}

	// If regex is specified then apply the regex rule
	private String applyRegExRule(Rule r, String s, String regex) {
		try {
			Pattern p1 = Pattern.compile(regex);
			Matcher m1 = p1.matcher(s);
			logger.info("Rule" + r);
			if (m1.find()) {
				MatchResult mr = m1.toMatchResult();
				return mr.group(0);
			}
		} catch (Exception e) {
			logger.error("Error in applyRegExRule", e);
		}
		return "";
	}

	// If the attribute is to be extracted from sentence, apply rules on sentences
	private boolean applySentenceRule(List<String> sentences, Rule r, int companyId, String docName,
			List<ExtractedEntityForSingleAttribute> listExtraction, JSONObject attributeJSON,
			List<CustomizeList> customList, String outputDateFormat) {
		String strRet = "";
		boolean success = false;
		for (String s1 : sentences) {
			// SHAMA Added page number logic ******************
			String s = "";
			int pageNumber = -1;
			if (s1.contains("|||")) {
				s = s1.substring(0, s1.indexOf("|||"));
				String strPageNum = s1.substring(s1.indexOf("|||") + 3);
				try {
					pageNumber = Integer.parseInt(strPageNum);
				} catch (Exception e) {
					logger.error("Error in applySentenceRule", e);
				}
			}
			// SHAMA*******************
			String reg = r.getRegex();
			// If regEx is not null, apply the regRule
			if (reg != null && !reg.contentEquals("")) {
				strRet = applyRegExRule(r, s, reg);
			} else { // Else apply normal rule
				strRet = applyNormalRule(r, s);
			}
			if (!strRet.contentEquals("")) {
				ParameterList params = new ParameterList();
				params.setChunk(strRet);
				params.setCompanyId(companyId);
				params.setDocumentName(docName);
				params.setRule(r);
				params.setSentence(s);
				params.setPageNumber(pageNumber);
				if (applyExtractionRules(params, listExtraction, attributeJSON, customList, outputDateFormat)) {
					success = true;
					break;
				}
			}
		}

		return success;
	}

	// If attribute is paragraph, apply rules on paragraphs
	private boolean applyParagraphRule(List<String> paragraphs, Rule r, int companyId, String docName,
			List<ExtractedEntityForSingleAttribute> listExtraction, JSONObject attributeJSON, String fileString,
			List<String> pages, List<String> lines, List<CustomizeList> customList, String outputDateFormat) {
		String befRule = r.getTextBefore1();
		String aftRule = r.getTextAfter1();
		boolean success = false;

		// Attribute type is paragraph but instead of getting entire paragraph, sentence
		// start is given and number
		// of sentences is given like "This is the start|4". Means start from the
		// sentence which starts at "This is the start"
		// and take 4 sentences after that.
		if (befRule != null && befRule.contains("|")) {
			String[] parts = befRule.split("[|]");
			if (parts.length == 2 && StringUtils.isNumeric(parts[1])) {
				int numOfLines = Integer.parseInt(parts[1]);

				for (int i = 0; i < lines.size(); i++) {
					String s1 = lines.get(i);
					Pattern p1;
					int pageNumber = -1;
					// Apply before rule first
					// SHAMA Apply special character
					String tmpStr1 = applySpecialCharacterEscape(befRule.substring(0, befRule.indexOf('|')));
					if (r.getIgnoreCase())
						p1 = Pattern.compile("(?i)" + tmpStr1);
					else
						p1 = Pattern.compile(tmpStr1);
					Matcher m1 = p1.matcher(s1);
					if (m1.find()) {
						if (s1.contains("|||")) {
							String strPageNum = s1.substring(s1.indexOf("|||") + 3);
							try {
								pageNumber = Integer.parseInt(strPageNum);
							} catch (Exception e) {
								logger.error("Error in applyParagraphRule", e);
							}
						}

						String retStr = "";
						int index = -1;
						if (r.getIgnoreCase()) {
							index = s1.toLowerCase().indexOf(m1.group().toLowerCase());
						} else {
							index = s1.indexOf(m1.group());
						}
						if (index >= 0)
							retStr = s1.substring(index);
						else
							retStr = s1;
						if (retStr.contains("|||"))
							retStr = retStr.substring(0, retStr.indexOf("|||"));

						for (int j = 1; j <= numOfLines; j++) {
							if ((i + j) < lines.size()) {
								String tmpStr = lines.get(i + j);
								if (tmpStr.contains("|||"))
									retStr += " " + tmpStr.substring(0, tmpStr.indexOf("|||"));
								else
									retStr += tmpStr;
							}
						}
						ParameterList params = new ParameterList();
						params.setChunk(retStr);
						params.setCompanyId(companyId);
						params.setDocumentName(docName);
						params.setRule(r);
						params.setSentence(retStr);
						params.setPageNumber(pageNumber);
						applyExtractionRules(params, listExtraction, attributeJSON, customList, outputDateFormat);
						return true;
					}
				}
			}
		} else if (befRule != null && aftRule != null && !befRule.contentEquals("") && !aftRule.contentEquals("")) {
			// search this pattern in the entire text of the document and the retrieved text
			// becomes chunk
			Pattern p2;

			// SHAMA Apply special character
			String tmpStr = applySpecialCharacterEscape(befRule);
			String paraRule = "(" + tmpStr + ")" + STAR_QSTN;
			tmpStr = applySpecialCharacterEscape(aftRule);
			paraRule += "(" + tmpStr + ")";
			logger.info("In before after rule rule = " + paraRule);
			if (r.getIgnoreCase())
				p2 = Pattern.compile("(?i)" + paraRule);
			else
				p2 = Pattern.compile(paraRule);

			Matcher m2 = p2.matcher(fileString);
			if (m2.find()) {
				ParameterList params = new ParameterList();
				String retStr = m2.group();
				if (r.getMerge() != null && r.getMerge() == false)
					retStr = m2.group(2).trim();
				logger.info("paragraph before after rule: " + retStr);
				params.setChunk(retStr);
				params.setCompanyId(companyId);
				params.setDocumentName(docName);
				params.setRule(r);
				params.setSentence(retStr);
				int pageNumber = getPageNumber(pages, retStr);
				params.setPageNumber(pageNumber);
				applyExtractionRules(params, listExtraction, attributeJSON, customList, outputDateFormat);
				return true;
			}
		} else { // Normal paragraph handling

			String reg = r.getRegex();
			// If regEx is not null, apply the regRule
			if (reg != null && !reg.contentEquals("")) {
				// for (String s : paragraphs) {
				String retStr = applyRegExRule(r, fileString, reg);
				if (!retStr.contentEquals("")) {
					int pageNumber = getPageNumber(pages, retStr);
					ParameterList params = new ParameterList();
					params.setChunk(retStr);
					params.setCompanyId(companyId);
					params.setDocumentName(docName);
					params.setRule(r);
					params.setSentence(retStr);
					params.setPageNumber(pageNumber);
					applyExtractionRules(params, listExtraction, attributeJSON, customList, outputDateFormat);
					success = true;
				}
			} else {

				boolean cont = false;
				String retStr = "";
				String numberPattern = "";
				String regEx = "";
				int paraNumber = 0;
				int pageNumber = 0;
				for (String s : paragraphs) {
					// If the number pattern before paragraph is of the type 3.4.5 then this
					// paragraph will end when next paragraph with
					// paragraph number 3.4.6 comes
					// If the number pattern before paragraph is of the type 3.4 then this paragraph
					// will end when next paragraph with
					// paragraph number 3.5 comes
					// If the number pattern before paragraph is of the type 3. or 3 then this
					// paragraph will end when next paragraph with
					// paragraph number 4. or 4 comes
					if (cont) {
						Pattern p = Pattern.compile(regEx);
						Matcher m;
						if (s.contains(" "))
							m = p.matcher(s.substring(0, s.indexOf(' ')));
						else
							m = p.matcher(s);
						if (m.find()) {
							numberPattern = m.group();
							long count = regEx.chars().filter(ch -> ch == '.').count();
							int pnum = (count > 0)
									? Integer.parseInt(numberPattern.substring(numberPattern.lastIndexOf('.') + 1))
									: Integer.parseInt(numberPattern);
							if (pnum > paraNumber)
								break;
						} else { // This is required because if the selected paragraph is of the type 1.12
									// and next paragraph comes with 2. then it should end
							if (regEx.contains(".")) {
								String tmpRegEx = regEx;
								boolean nextParaFound = false;
								while (tmpRegEx.contains(".")) {
									tmpRegEx = tmpRegEx.substring(0, tmpRegEx.lastIndexOf('.') - 1);
									p = Pattern.compile(tmpRegEx);
									if (s.contains(" "))
										m = p.matcher(s.substring(0, s.indexOf(' ')));
									else
										m = p.matcher(s);
									if (m.find()) {
										nextParaFound = true;
										break;
									}
								}
								if (nextParaFound)
									break;
							}
						}
						if (s.contains("|||")) {
							retStr += " " + s.substring(0, s.lastIndexOf("|||"));
						} else {
							retStr += " " + s;
						}
						continue;
					}

					if (befRule.startsWith("(") && befRule.endsWith(")"))
						befRule = befRule.substring(1, befRule.length() - 1);
					if (s.toLowerCase().contains(befRule.toLowerCase())) {
						int idx = s.toLowerCase().indexOf(befRule.toLowerCase());
						String befStr = s.substring(0, idx);
						String[] words = befStr.split(" ");
						if (words.length < 3) {
							regEx = checkPattern(befStr); // Check the paragraph numbering pattern and apply the same
															// pattern
															// till one gets the next paragraph
							if (!regEx.contentEquals("")) {
								Pattern p = Pattern.compile(regEx);
								Matcher m = p.matcher(s.substring(0, s.indexOf(' ')));
								if (m.find())
									numberPattern = m.group();
								long count = regEx.chars().filter(ch -> ch == '.').count();
								paraNumber = (count > 0)
										? Integer.parseInt(numberPattern.substring(numberPattern.lastIndexOf('.') + 1))
										: Integer.parseInt(numberPattern);
								cont = true;
							}
							Boolean merge = r.getMerge();
							if (merge == null)
								merge = false;
							if (s.contains("|||")) {
								retStr = s.substring(0, s.lastIndexOf("|||"));
								if (!merge) {
									retStr = retStr.substring(idx + befRule.length()).trim();
								}
								String tmp = s.substring(s.lastIndexOf("|||") + 3);
								tmp = tmp.trim();
								try {
									pageNumber = Integer.parseInt(tmp);
								} catch (Exception e) {
									logger.error("Error in applyParagraphRule", e);
								}
							} else {
								retStr = s;
								if (!merge) {
									retStr = retStr.substring(idx + befRule.length()).trim();
								}
							}
							success = true;
							if (!cont)
								break;
						}
					}
				}
				if (!retStr.contentEquals("")) {
					ParameterList params = new ParameterList();
					params.setChunk(retStr);
					params.setCompanyId(companyId);
					params.setDocumentName(docName);
					params.setRule(r);
					params.setSentence(retStr);
					params.setPageNumber(pageNumber);
					applyExtractionRules(params, listExtraction, attributeJSON, customList, outputDateFormat);
				}
			}
		}
		return success;
	}

	private int getPageNumber(List<String> pages, String retStr) {
		int pageNumber = -1;
		for (int ctr = 0; ctr < pages.size(); ctr++) {
			String pageString = pages.get(ctr);
			pageString = pageString.replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("“", "\"")
					.replaceAll("”", "\"").replaceAll("’", "'");
			if (pageString.contains(retStr)) {
				if (pageString.contains("|||")) {
					String strPageNum = pageString.substring(pageString.indexOf("|||") + 3);
					try {
						pageNumber = Integer.parseInt(strPageNum);
					} catch (Exception e) {
						logger.error("Error in applyParagraphRule", e);
					}
				}
				break;
			} else {
				if ((ctr + 1) < pages.size()) {
					String newStr = "";
					String p = pages.get(ctr+1).replaceAll("\r\n", " ").replaceAll("\n", " ").replaceAll("“", "\"")
							.replaceAll("”", "\"").replaceAll("’", "'");
					if (pageString.contains("|||")) {
						newStr = pageString.substring(pageString.indexOf("|||") + 3);
						newStr += " " + p;
					} else {
						newStr = pageString + " " + p;
					}
					if (newStr.contains(retStr)) {
						pageNumber = ctr;
						break;
					}
				}
			}
		}
		return pageNumber;
	}

	// For paragraph, check the number pattern before the text.
	private String checkPattern(String str) {
		String regex = "[0-9]+[.][0-9]+[.][0-9]+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return regex;
		}
		regex = "[0-9]+[.][0-9]+";
		p = Pattern.compile(regex);
		m = p.matcher(str);
		if (m.find()) {
			return regex;
		}
		regex = "[0-9]+";
		p = Pattern.compile(regex);
		m = p.matcher(str);
		if (m.find()) {
			return regex;
		}
		return "";
	}
}
