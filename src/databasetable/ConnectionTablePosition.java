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
@Table(name = "Position")
public class ConnectionTablePosition {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idConteneur")
	int idConteneur;

	@Column(name = "idTerme")
	int idTerme;
	
	@Column(name = "position")
	int position;

	public ConnectionTablePosition() {
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	
}
