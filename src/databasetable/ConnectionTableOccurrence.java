package databasetable;

//JPA Imports
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

//End JPA Imports


@Entity
@Table(name = "Occurrence")
public class ConnectionTableOccurrence implements Serializable, Comparable<ConnectionTableOccurrence> {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	PKOccurrence pkOccurrence;

	@Column(name = "nbOccurrence")
	int nbOccurrence;
	
	@Column(name = "tf")
	float tf;
	
	@Column(name = "idf")
	double idf;
	
	@Column(name = "tfidf")
	double tfidf;
	
	public ConnectionTableOccurrence() {
	}

	public int getNbOccurrence() {
		return nbOccurrence;
	}

	public void setNbOccurrence(int nbOccurrence) {
		this.nbOccurrence = nbOccurrence;
	}
	
	public PKOccurrence getPkOccurrence() {
		return pkOccurrence;
	}

	public void setPkOccurrence(PKOccurrence pkOccurrence) {
		this.pkOccurrence = pkOccurrence;
	}

	public float getTf() {
		return tf;
	}

	public void setTf(float tf) {
		this.tf = tf;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public double getTfidf() {
		return tfidf;
	}

	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}

	@Override
	public String toString() {
		return "ConnectionTableOccurrence [idConteneur=" + pkOccurrence.getIdConteneur()
				+ ", idDocument=" + pkOccurrence.getIdDocument()
				+ ", nomTerme=" + pkOccurrence.getNomTerme()
				+ ", nbOccurrence=" + nbOccurrence + ", tf=" + tf + ", idf="
				+ idf + ", tfidf=" + tfidf + "]";
	}

	@Override
	public int compareTo(ConnectionTableOccurrence o) {
		if(this.getTfidf() > o.getTfidf()) {
			return -1;
		} else {
			if(this.getTfidf() == o.getTfidf()) {
				return 0;
			} else {
				return 1;
			}
		}
	}
	
	
	
}


