package databasetable;

//JPA Imports
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

//End JPA Imports


@Entity
@Table(name = "Occurrence")
public class ConnectionTableOccurrence implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	PKOccurrence pkOccurrence;

	@Column(name = "nbOccurrence")
	int nbOccurrence;
	
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
	
}


