package org.m2acsi.entity.demande;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="typeIncident")
public class TypeIncident {

	public TypeIncident() {
		super();
		this.idTypeIncident = UUID.randomUUID().toString();
	}
	
	public TypeIncident(String libelleTypeIncident) {
		super();
		this.idTypeIncident = UUID.randomUUID().toString();
		this.libelleTypeIncident = libelleTypeIncident;
	}

	@Id
	private String idTypeIncident;
	
	private String libelleTypeIncident;
	
	@OneToMany(mappedBy="type", cascade = CascadeType.ALL)
	private List<Demande> listeDemande;

	public String getIdTypeIncident() {
		return idTypeIncident;
	}

	public String getLibelleTypeIncident() {
		return libelleTypeIncident;
	}

	public void setIdTypeIncident(String idTypeIncident) {
		this.idTypeIncident = idTypeIncident;
	}

	public void setLibelleTypeIncident(String libelleTypeIncident) {
		this.libelleTypeIncident = libelleTypeIncident;
	}

	public List<Demande> getListeDemande() {
		return listeDemande;
	}

	public void setListeDemande(List<Demande> listeDemande) {
		this.listeDemande = listeDemande;
	}
	
	
}
