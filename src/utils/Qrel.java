package utils;

public class Qrel {
	
	private String docQrel;
	private String xpathQrel;
	private String pertinanceQrel;
	
	public Qrel(String docQrel, String xpathQrel, String pertinanceQrel) {
		super();
		this.docQrel = docQrel;
		this.xpathQrel = xpathQrel;
		this.pertinanceQrel = pertinanceQrel;
	}
	public String getDocQrel() {
		return docQrel;
	}
	public void setDocQrel(String docQrel) {
		this.docQrel = docQrel;
	}
	public String getXpathQrel() {
		return xpathQrel;
	}
	public void setXpathQrel(String xpathQrel) {
		this.xpathQrel = xpathQrel;
	}
	public String getPertinanceQrel() {
		return pertinanceQrel;
	}
	public void setPertinanceQrel(String pertinanceQrel) {
		this.pertinanceQrel = pertinanceQrel;
	}
	@Override
	public String toString() {
		return "Qrel [docQrel=" + docQrel + ", xpathQrel=" + xpathQrel
				+ ", pertinanceQrel=" + pertinanceQrel + "]";
	}
}
