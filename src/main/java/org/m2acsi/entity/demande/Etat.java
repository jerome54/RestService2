package org.m2acsi.entity.demande;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="etat")
public class Etat {

	public Etat() {
		super();
		this.idEtat = UUID.randomUUID().toString();
	}
	
	public Etat(String libelleEtat, List<Action> listeAction, List<Demande> listeDemande) {
		super();
		this.idEtat = UUID.randomUUID().toString();
		this.libelleEtat = libelleEtat;
		//this.listeAction = listeAction;
		this.listeDemande = listeDemande;
	}

	@Id
	private String idEtat;
	
	private String libelleEtat;
	
	@OneToMany(mappedBy="etatAction", cascade = CascadeType.ALL)
	private List<Action> listeAction;
	
	@OneToMany(mappedBy="etat", cascade = CascadeType.ALL)
	private List<Demande> listeDemande;

	public String getIdEtat() {
		return idEtat;
	}

	public String getLibelleEtat() {
		return libelleEtat;
	}

	public void setIdEtat(String idEtat) {
		this.idEtat = idEtat;
	}

	public void setLibelleEtat(String libelleEtat) {
		this.libelleEtat = libelleEtat;
	}
	
	
}
