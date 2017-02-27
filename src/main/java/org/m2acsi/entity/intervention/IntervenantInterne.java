package org.m2acsi.entity.intervention;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Entity
@JsonTypeName("IntervenantInterne")
public class IntervenantInterne extends Intervenant {

	
	public IntervenantInterne() {
		super();
	}

	public IntervenantInterne(String nomIntervenantInterne, String prenomIntervenantInterne, String departement) {
		super();
		this.nomIntervenantInterne = nomIntervenantInterne;
		this.prenomIntervenantInterne = prenomIntervenantInterne;
		this.departement = departement;
	}

	private String nomIntervenantInterne;
	
	private String prenomIntervenantInterne;
	
	private String departement;

	public String getNomIntervenantInterne() {
		return nomIntervenantInterne;
	}

	public String getPrenomIntervenantInterne() {
		return prenomIntervenantInterne;
	}

	public String getDepartement() {
		return departement;
	}

	public void setNomIntervenantInterne(String nomIntervenantInterne) {
		this.nomIntervenantInterne = nomIntervenantInterne;
	}

	public void setPrenomIntervenantInterne(String prenomIntervenantInterne) {
		this.prenomIntervenantInterne = prenomIntervenantInterne;
	}

	public void setDepartement(String departement) {
		this.departement = departement;
	}
	
	
	
	
}
