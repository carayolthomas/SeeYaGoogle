package databasetable;

//JPA Imports
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//End JPA Imports

@Entity
@Table(name = "Occurrence")
public class ConnectionTableOccurrence {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idConteneur")
	int idConteneur;

	@Column(name = "idTerme")
	int idTerme;

	@Column(name = "nbOccurrence")
	int nbOccurrence;
	
	public ConnectionTableOccurrence() {
	}

	public int getIdConteneur() {
		return idConteneur;
	}

	public void setIdConteneur(int idConteneur) {
		this.idConteneur = idConteneur;
	}

	public int getIdTerme() {
		return idTerme;
	}

	public void setIdTerme(int idTerme) {
		this.idTerme = idTerme;
	}

	public int getNbOccurrence() {
		return nbOccurrence;
	}

	public void setNbOccurrence(int nbOccurrence) {
		this.nbOccurrence = nbOccurrence;
	}

	
}
