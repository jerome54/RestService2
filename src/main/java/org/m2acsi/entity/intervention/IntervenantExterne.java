package org.m2acsi.entity.intervention;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
public class IntervenantExterne extends Intervenant {

	
	public IntervenantExterne() {
		super();
	}

	public IntervenantExterne(String raisonSociale, String numeroSiren, String nomEntreprise, String numeroTVA) {
		super();
		this.raisonSociale = raisonSociale;
		this.numeroSiren = numeroSiren;
		this.nomEntreprise = nomEntreprise;
		this.numeroTVA = numeroTVA;
	}

	
	private String raisonSociale;
	
	private String numeroSiren;
	
	private String nomEntreprise;
	
	private String numeroTVA;

	public String getRaisonSociale() {
		return raisonSociale;
	}

	public String getNumeroSiren() {
		return numeroSiren;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public String getNumeroTVA() {
		return numeroTVA;
	}

	public void setRaisonSociale(String raisonSociale) {
		this.raisonSociale = raisonSociale;
	}

	public void setNumeroSiren(String numeroSiren) {
		this.numeroSiren = numeroSiren;
	}

	public void setNomEntreprise(String nomEntreprise) {
		this.nomEntreprise = nomEntreprise;
	}

	public void setNumeroTVA(String numeroTVA) {
		this.numeroTVA = numeroTVA;
	}
	
	
	
	
}
