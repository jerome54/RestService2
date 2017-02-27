package org.m2acsi.entity.intervention;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.m2acsi.entity.demande.Action;
import org.m2acsi.entity.demande.ActionIntervenant;
import org.m2acsi.entity.demande.Demande;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Table(name = "intervenant")
@JsonSubTypes({ @JsonSubTypes.Type(value=IntervenantInterne.class, name="IntervenantInterne") })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class Intervenant {
	
	public Intervenant() {
		super();
		this.idIntervenant = UUID.randomUUID().toString();
	}

	public Intervenant(String id) {
		super();
		this.idIntervenant = id;
	}
	
	public Intervenant(List<Demande> listeDemande) {
		super();
		this.idIntervenant = UUID.randomUUID().toString();
		//this.listeDemande = listeDemande;
	}

	
	@Id
	private String idIntervenant;
	
	//@ManyToMany
	//@JoinTable(name="IntervenantAction",joinColumns=@JoinColumn(name="id_intervenant"),inverseJoinColumns=@JoinColumn(name="id_action"))
	@OneToMany(fetch = FetchType.LAZY, mappedBy ="pk.action")
	private List<ActionIntervenant> listeAction;
	
	public String getIdIntervenant() {
		return idIntervenant;
	}

	public List<ActionIntervenant> getListeAction() {
		return listeAction;
	}

	public void setIdIntervenant(String idIntervenant) {
		this.idIntervenant = idIntervenant;
	}

	public void setListeAction(List<ActionIntervenant> listeAction) {
		this.listeAction = listeAction;
	}

	
	

	
	
	
	
}
