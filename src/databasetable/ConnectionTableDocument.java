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
@Table(name = "Document")
public class ConnectionTableDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idDocument")
	int idDocument;

	@Column(name = "nomDocument")
	String nomDocument;

	public ConnectionTableDocument() {
	}

	public int getIdDocument() {
		return idDocument;
	}

	public String getNomDocument() {
		return nomDocument;
	}

	public void setNomDocument(String nomDocument) {
		this.nomDocument = nomDocument;
	}

	public void setIdDocument(int idDocument) {
		this.idDocument = idDocument;
	}
}
