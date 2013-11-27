package databasetable;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PKPosition implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "idTerme")
    private int idTerme;

    @Column(name = "idConteneur")
    private int idConteneur;

	public int getIdTerme() {
		return idTerme;
	}

	public void setIdTerme(int idTerme) {
		this.idTerme = idTerme;
	}

	public int getIdConteneur() {
		return idConteneur;
	}

	public void setIdConteneur(int idConteneur) {
		this.idConteneur = idConteneur;
	}
}