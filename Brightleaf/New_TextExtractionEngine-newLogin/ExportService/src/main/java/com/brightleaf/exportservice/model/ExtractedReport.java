package com.brightleaf.exportservice.model;

public class ExtractedReport {
	private String companyName;
	private int totalTransaction;
	private int totalExtractions;
	private int correctExtraction;
	private int incorrectExtraction;
	private String accuracyPercentage;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getTotalTransaction() {
		return totalTransaction;
	}

	public void setTotalTransaction(int totalTransaction) {
		this.totalTransaction = totalTransaction;
	}

	public int getTotalExtractions() {
		return totalExtractions;
	}

	public void setTotalExtractions(int totalExtractions) {
		this.totalExtractions = totalExtractions;
	}

	public int getCorrectExtraction() {
		return correctExtraction;
	}

	public void setCorrectExtraction(int correctExtraction) {
		this.correctExtraction = correctExtraction;
	}

	public int getIncorrectExtraction() {
		return incorrectExtraction;
	}

	public void setIncorrectExtraction(int incorrectExtraction) {
		this.incorrectExtraction = incorrectExtraction;
	}

	public String getAccuracyPercentage() {
		return accuracyPercentage;
	}

	public void setAccuracyPercentage(String accuracyPercentage) {
		this.accuracyPercentage = accuracyPercentage;
	}
}
