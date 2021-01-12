package com.brightleaf.ruleservice.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFinder {
	private static final String SYMBOL_BACK_SLASH = "\\";
    private static final String SYMBOL_PIPE = "|";
    private static final String SYMBOL_BACKSPACE = "\\b";
	private static final List<String> specialCharList = Arrays.asList("\\", "{", "[", "(", ")", "]", "}", "|", "+", "*", "?", ".", "$", "^", "%");
	public WordFinder() {
	}
	
	public boolean findWord(ParameterList params, List<ExtractedEntityForSingleAttribute> listExtraction) {
		String text = params.getChunk();
		
		Rule r = params.getRule();
		
		String s = r.getSearchword();
		if (s == null || s.contentEquals("")) {
			ExtractedEntityForSingleAttribute entity = new ExtractedEntityForSingleAttribute();
			entity.setAppliedRule(r.getRuleId());
			entity.setCompanyId(params.getCompanyId());
			entity.setDocumentName(params.getDocumentName());
			entity.setExtractedAttributeId(r.getAttributeId());
			entity.setExtractedChunk(params.getChunk());
			entity.setPageNumber(params.getPageNumber());
			entity.setExtractedEntity(r.getNotfound());
			listExtraction.add(entity);
			return true;
		}
		List<String> searchWords = new ArrayList<>();
		if (s.contains("|")) {
			String[] words = s.split("[|]");
			for (String w:words) {
				searchWords.add(w);
			}
		} else {
			searchWords.add(s);
		}
		
		boolean found = checkContentfromText(text, searchWords, r.getIgnoreCase());
		ExtractedEntityForSingleAttribute entity = new ExtractedEntityForSingleAttribute();
		entity.setAppliedRule(r.getRuleId());
		entity.setCompanyId(params.getCompanyId());
		entity.setDocumentName(params.getDocumentName());
		entity.setExtractedAttributeId(r.getAttributeId());
		entity.setExtractedChunk(params.getChunk());
		entity.setPageNumber(params.getPageNumber());
		
		if (found) {
			entity.setExtractedEntity(r.getFound());
		} else {
			entity.setExtractedEntity(r.getNotfound());
		}
		listExtraction.add(entity);
		return true;
	}
	
	private boolean checkContentfromText(String text, List<String> searchValues, boolean ignoreCase) {
		StringBuilder builder = new StringBuilder();
		searchValues.forEach(searchValue -> {
			char[] searchValueCharArr = searchValue.toCharArray();
			for (char searchValueChar : searchValueCharArr) {
				if (specialCharList.contains(String.valueOf(searchValueChar))) {
					builder.append(SYMBOL_BACK_SLASH).append((String.valueOf(searchValueChar)));
					builder.append(SYMBOL_PIPE);
				}
			}
			builder.append(SYMBOL_BACKSPACE).append((String.valueOf(searchValue))).append(SYMBOL_BACKSPACE);
			builder.append(SYMBOL_PIPE);
		});

		String searchPattern = builder.toString();
		if (searchPattern.endsWith(SYMBOL_PIPE)) {
			searchPattern = searchPattern.substring(0, searchPattern.length() - 1);
		}

		Pattern pattern = null;
		if (ignoreCase) {
			pattern = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);
		} else {
			pattern = Pattern.compile(searchPattern);
		}

		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return true;}
		return false;
	}
}
