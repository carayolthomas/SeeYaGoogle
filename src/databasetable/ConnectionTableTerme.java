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
@Table(name = "Terme")
public class ConnectionTableTerme {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idTerme")
	int idTerme;

	@Column(name = "nomTerme")
	String nomTerme;
	
	@Column(name = "idConteneur")
	int idConteneur;

	public ConnectionTableTerme() {
	}

	public int getIdTerme() {
		return idTerme;
	}

	public void setIdTerme(int idTerme) {
		this.idTerme = idTerme;
	}

	public String getNomTerme() {
		return nomTerme;
	}

	public void setNomTerme(String nomTerme) {
		this.nomTerme = nomTerme;
	}

	public int getIdConteneur() {
		return idConteneur;
	}

	public void setIdConteneur(int idConteneur) {
		this.idConteneur = idConteneur;
	}

	
}
