package com.brightleaf.mongoservice.model;

public class ReportExtractCompanyWise {
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
	public void setTotalTransaction(int count) {
		this.totalTransaction = count;
	}
	public int getTotalExtractions() {
		return totalExtractions;
	}
	public void setTotalExtractions(int count) {
		this.totalExtractions = count;
	}
	public int getCorrectExtraction() {
		return correctExtraction;
	}
	public void setCorrectExtraction(int count) {
		this.correctExtraction = count;
	}
	public int getIncorrectExtraction() {
		return incorrectExtraction;
	}
	public void setIncorrectExtraction(int count) {
		this.incorrectExtraction = count;
	}
	public String getAccuracyPercentage() {
		return accuracyPercentage;
	}
	public void setAccuracyPercentage(String accuracyPercentage) {
		this.accuracyPercentage = accuracyPercentage;
	}
}
