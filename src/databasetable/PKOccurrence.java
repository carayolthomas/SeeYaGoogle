package databasetable;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PKOccurrence implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "nomTerme")
    private String nomTerme;

    @Column(name = "idConteneur")
    private int idConteneur;

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