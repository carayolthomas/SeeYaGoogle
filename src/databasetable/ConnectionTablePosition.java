package databasetable;

//JPA Imports
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//End JPA Imports

@Entity
@Table(name = "Position")
public class ConnectionTablePosition {

	@EmbeddedId
	PKPosition pkPosition;
	
	@Column(name = "position")
	int position;

	public PKPosition getPKPosition() {
		return pkPosition;
	}
	
	public void setPKPosition(PKPosition pkPos) {
		pkPosition = pkPos;
	}
	
	
	public ConnectionTablePosition() {
	}


	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "ConnectionTablePosition [idConteneur=" + pkPosition.getIdConteneur()
				+ ", idTerme=" + pkPosition.getIdTerme()
				+ ", position=" + position + "]";
	}

	
}
