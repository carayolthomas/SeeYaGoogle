package utils;

public class ResultQuery {

	private String nomDocument;
	private String xpath;
	public String getNomDocument() {
		return nomDocument;
	}
	public void setNomDocument(String nomDocument) {
		this.nomDocument = nomDocument;
	}
	public String getXpath() {
		return xpath;
	}
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	
	public ResultQuery(String nomDocument, String xpath) {
		super();
		this.nomDocument = nomDocument;
		this.xpath = xpath;
	}
	
	@Override
	public String toString() {
		return "ResultQuery [nomDocument=" + nomDocument + ", xpath=" + xpath
				+ "]";
	}
}
