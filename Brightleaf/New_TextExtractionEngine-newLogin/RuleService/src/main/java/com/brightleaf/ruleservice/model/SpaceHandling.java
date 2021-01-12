package com.brightleaf.ruleservice.model;

import static java.lang.Math.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import javafx.util.Pair;

//Make this class singleton so that the dictionary is read only once and the costs are calculated once
@SuppressWarnings("restriction")
public class SpaceHandling {
	
	protected final static Logger logger = Logger.getLogger(SpaceHandling.class);
	int maxword = 0;
	HashMap<String, Double> wordcost = new HashMap<>();
	private static SpaceHandling spaceHandlingInstance = null;

	private SpaceHandling() {
		String dictionaryLocation = Constants.DICTIONARIESPATH + "/en/en_words.txt";
		maxword = loadDictionary(dictionaryLocation);
	}
	
	public static SpaceHandling getInstance() 
    { 
        if (spaceHandlingInstance == null) 
        	spaceHandlingInstance = new SpaceHandling(); 
  
        return spaceHandlingInstance; 
    } 

	private int loadDictionary(String dictFullPath) {
		int ret = 0;

		List<String> dictWords = new ArrayList<>();
		
		try {
			URL url =  new URL(dictFullPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
			while (true) {
				String w = reader.readLine();
				if (w == null) {
					break;
				}
				dictWords.add(w);
			}
			for (int i = 0; i < dictWords.size(); i++) {
				String k = dictWords.get(i);
				Double val = log((i + 1) * log(dictWords.size()));
				if (k.length() > ret) {
					ret = k.length();
				}
				wordcost.put(k, val);
			}
		} catch (IOException e) {
			logger.error("Error in loadDictionary", e);
		}

		return ret;
	}
	
	public String processText(String strToken) {
		if (maxword == 0)
			return strToken;
        String retStr = "";
        String saveString = strToken;
        String originalStr = strToken;
        List<String> terms = new ArrayList<>();
        
        String emailRegex = "(mailto:|From: |To: |Cc: |)([A-Za-z]+[A-Za-z\\s]*)[<|][A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}[>|]";
        strToken = extractTerms(strToken, terms, emailRegex);
    	
    	emailRegex = "(mailto:|From: |To: |Cc: |)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
    	strToken = extractTerms(strToken, terms, emailRegex);
  
        Matcher m = Pattern.compile("[a-zA-Z]+[a-zA-Z\\s]*").matcher(strToken);
        while (m.find()) {
        	terms.add(m.group());
        }

        String splitStr = "";
        
        for (String checkString: terms) {
            int index = originalStr.indexOf(checkString.trim());
            splitStr += originalStr.substring(0, index);
            originalStr = originalStr.substring(index);
            String s = checkString.toLowerCase().replaceAll(" ", "").replaceAll("\\r\\n", "");
            List<String> tmp = split(s);
            for (int i = 0; i < tmp.size(); i++) {
            	String str = (String) tmp.get(i);
                if (str.endsWith("s")
                        && ((i + 1) < tmp.size()) && ((String) tmp.get(i + 1)).equals("hall")) {
                    tmp.set(i, str.substring(0, str.length() - 1));
                    tmp.set(i + 1, "s" + ((String) tmp.get(i + 1)));
                    str = (String) tmp.get(i);
                }
				int ctr1 = originalStr.toLowerCase().indexOf(str);
				if (ctr1 >= 0) {
					for (int j = ctr1, k = 0; k < str.length(); j++, k++) {
						if (Character.isUpperCase(originalStr.charAt(j))) {
							char[] splitChar = str.toCharArray();
							splitChar[k] = Character.toUpperCase(splitChar[k]);
							str = new String(splitChar);
						}
					}
				}
				splitStr += str + " ";
			}
			splitStr = splitStr.trim();
			if (checkString.endsWith(" ")) {
				Pattern trailing = Pattern.compile("\\s+$");
				Matcher match = trailing.matcher(checkString);
				if (match.find())
					splitStr += match.group();
			}
			originalStr = originalStr.substring(originalStr.indexOf(checkString) + checkString.length());
        }
        retStr = splitStr + originalStr;
        return (postProc(saveString, retStr));
    }

    private String extractTerms(String strToken, List<String> terms, String emailRegex) {
    	boolean done = false;
    	String retStr = strToken;
		Pattern tempPattern;
		Matcher tempMatcher;
		while (!done) {
			tempPattern = Pattern.compile(emailRegex);
			tempMatcher = tempPattern.matcher(retStr);

			if (tempMatcher.find()) {
				String s = tempMatcher.group();
				
		    	String[] splitStr = retStr.split(s);
				if (splitStr.length > 0) {
					Matcher m = Pattern.compile("[a-zA-Z]+[a-zA-Z\\s]*").matcher(splitStr[0]);
					while (m.find()) {
						terms.add(m.group());
					}
				}
				retStr = retStr.replaceFirst(splitStr[0], "");
				retStr = retStr.replaceFirst(s, "");
				
			} else {
				done = true;}
		}
    	
		return retStr;
	}

    private List<String> split(String s) {
        List<Double> cost = new ArrayList<>();
        cost.add(0.0);
        for (int i = 1; i < s.length() + 1; i++) {
            Pair<Double, Integer> p = bestMatch(i, cost, s);
            cost.add((Double) p.getKey());
        }

        List<String> out = new ArrayList<>();
        int i = s.length();
        while (i > 0) {
            Pair<Double, Integer> p = bestMatch(i, cost, s);
            double c = (double) p.getKey();
            int k = (int) p.getValue();
            if (c == cost.get(i)) {
                out.add(s.substring(i - k, i));
            }
            i -= k;
        }
        Collections.reverse(out);
        return out;
    }

    private Pair<Double, Integer> bestMatch(int index, List<Double> cost, String s) {
        Pair<Double, Integer> pair;
        int tmpval = 0;
        if ((index - maxword) > 0) {
            tmpval = (index - maxword);
        }

        double[] candidates = new double[index - tmpval];

        for (int j = tmpval, ctr = 0; j < index; j++, ctr++) {
            candidates[ctr] = (double) cost.get(j);
        }
        List<Pair<Double, Integer>> pairList = new ArrayList<>();

        Double minKey = Double.POSITIVE_INFINITY;
        int retVal = 0;
        //Reverse candidates array
        for (int i = 0; i < candidates.length / 2; i++) {
            double temp = candidates[i];
            candidates[i] = candidates[candidates.length - 1 - i];
            candidates[candidates.length - 1 - i] = temp;
        }

        for (int k = 0; k < candidates.length; k++) {
            double c = candidates[k];

            double t;
            String find = s.substring(index - k - 1, index);
            if (wordcost.containsKey(find)) {
                t = wordcost.get(find);
            } else {
                t = 9e99;
            }

            double key = t + c;

            pairList.add(new Pair<Double, Integer>(key, k + 1));
        }

        for (int j = 0; j < pairList.size(); j++) {
            double key = (double) pairList.get(j).getKey();
            int val = (int) pairList.get(j).getValue();

            if (key < minKey) {
                minKey = key;
                retVal = val;
            }
        }

        pair = new Pair<>(minKey, retVal);
        return pair;
    }

    private String postProc(String origStr, String strin) {
        String strTmp = strin.trim();
        Pattern p = Pattern.compile("\\p{Punct}");
        String[] tmpOut = strTmp.split(" ");
        String[] origSplit = origStr.split(" ");
        int j = 0;
        while (j < tmpOut.length) {
            if (tmpOut[j].equals("")) {
                j++;
                continue;
            }
            String s1 = Character.toString(tmpOut[j].charAt(0));
            Matcher m = p.matcher(s1);
            boolean puncFound = m.find();
            if (!Character.isUpperCase(tmpOut[j].charAt(0)) && !Character.isDigit(tmpOut[j].charAt(0)) && !puncFound) {
                j++;
                continue;
            }
            boolean found = false;
            for (int k = 0; k < origSplit.length; k++) {
                if (origSplit[k].equals(tmpOut[j])) {
                    found = true;
                    break;
                }
            }
            if (found) {
                j++;
                continue;
            }
            if (j + 1 < tmpOut.length) {
                for (int k1 = 0; k1 < origSplit.length; k1++) {
                    if (origSplit[k1].equals(tmpOut[j] + tmpOut[j + 1])) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                tmpOut[j] = tmpOut[j] + tmpOut[j + 1];
                tmpOut[j + 1] = "";
                j++;
                continue;
            }
            if (j + 2 < tmpOut.length) {
                for (int k1 = 0; k1 < origSplit.length; k1++) {
                    if (origSplit[k1].equals(tmpOut[j] + tmpOut[j + 1] + tmpOut[j + 2])) {
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                tmpOut[j] = tmpOut[j] + tmpOut[j + 1] + tmpOut[j + 2];
                tmpOut[j + 1] = "";
                tmpOut[j + 2] = "";
            }
            j++;
        }
        strTmp = "";
        for (int k = 0; k < tmpOut.length; k++) {
            if (!tmpOut[k].equals("")) {
                strTmp = strTmp + tmpOut[k] + " ";
            }
        }
        strTmp = strTmp.trim();
        return strTmp;
    }
}
