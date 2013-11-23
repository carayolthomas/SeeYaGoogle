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
@Table(name = "Conteneur")
public class ConnectionTableConteneur {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idConteneur")
	int idConteneur;

	@Column(name = "idDocument")
	int idDocument;
	
	@Column(name = "xpathConteneur")
	String xpathConteneur;

	@Column(name = "typeConteneur")
	String typeConteneur;

	public ConnectionTableConteneur() {
	}

	public int getIdConteneur() {
		return idConteneur;
	}

	public void setIdConteneur(int idConteneur) {
		this.idConteneur = idConteneur;
	}

	public int getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(int idDocument) {
		this.idDocument = idDocument;
	}

	public String getXpathConteneur() {
		return xpathConteneur;
	}

	public void setXpathConteneur(String xpathConteneur) {
		this.xpathConteneur = xpathConteneur;
	}

	public String getTypeConteneur() {
		return typeConteneur;
	}

	public void setTypeConteneur(String typeConteneur) {
		this.typeConteneur = typeConteneur;
	}


}
