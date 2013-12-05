package utils;

public class Query {

	private String idQuery;
	private String textQuery;
	private String narrativeQuery;
	
	public Query(String idQuery, String textQuery, String narrativeQuery) {
		super();
		this.idQuery = idQuery;
		this.textQuery = textQuery;
		this.narrativeQuery = narrativeQuery;
	}

	public String getIdQuery() {
		return idQuery;
	}

	public void setIdQuery(String idQuery) {
		this.idQuery = idQuery;
	}

	public String getTextQuery() {
		return textQuery;
	}

	public void setTextQuery(String textQuery) {
		this.textQuery = textQuery;
	}

	public String getNarrativeQuery() {
		return narrativeQuery;
	}

	public void setNarrativeQuery(String narrativeQuery) {
		this.narrativeQuery = narrativeQuery;
	}

	@Override
	public String toString() {
		return "Query [idQuery=" + idQuery + ", textQuery=" + textQuery
				+ ", narrativeQuery=" + narrativeQuery + "]";
	}
	
}
