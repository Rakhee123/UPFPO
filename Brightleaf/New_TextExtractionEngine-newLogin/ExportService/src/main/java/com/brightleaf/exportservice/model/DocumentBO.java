package com.brightleaf.exportservice.model;

public class DocumentBO implements Comparable<DocumentBO> {
	/** The documentName. */
	private String documentName;

	/**
	 * @return the documentName
	 */
	public String getDocumentName() {
		return documentName;
	}

	/**
	 * @param documentName the documentName to set
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((documentName == null) ? 0 : documentName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentBO other = (DocumentBO) obj;
		if (documentName == null) {
			if (other.documentName != null) {
				return false;
				}
		} else if (!documentName.equals(other.documentName)) {
			return false;
			}
		return true;
	}

	public int compareTo(DocumentBO documentBO) {
		return getDocumentName().compareTo(documentBO.getDocumentName());
	}
}