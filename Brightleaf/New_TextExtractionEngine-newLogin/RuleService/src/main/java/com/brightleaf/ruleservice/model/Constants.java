package com.brightleaf.ruleservice.model;

public class Constants {

	private Constants() {
		throw new IllegalStateException("Utility class");
	}

	public static final String SNLPBASEPATH = "file:///" + FolderStructure.AE_HOME.getLocation()
			+ "/Digester_Home/Digester_Functions";
	public static final String SUTIMEPATH = "file:///" + FolderStructure.AE_HOME.getLocation()
			+ "/Digester_Home/Digester_Functions/SNLP_MODELS/edu/stanford/nlp/models/sutime";
	public static final String SNLPJAR = "stanford-corenlp-3.9.2.jar";//"stanford-corenlp-3.8.0.jar";
	public static final String MODELPATH = "/en/ner/english.all.3class.distsim.crf.ser.gz";
	public static final String POSTAGGERPATH = "/pos-tagger/english-left3words/english-left3words-distsim.tagger";
	public static final String DICTIONARIESPATH = "file:///" + FolderStructure.AE_HOME.getLocation()
			+ "/Digester_Home/Digester_Functions/DICTIONARIES";
}
