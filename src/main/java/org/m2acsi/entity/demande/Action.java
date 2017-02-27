package org.m2acsi.entity.demande;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.m2acsi.entity.intervention.Intervenant;


@Entity
@Table(name = "action")
public class Action {

	public Action(String idAction, String nomAction, Etat etatAction, String dateAction) {
		super();
		this.idAction = idAction;
		this.nomAction = nomAction;
		this.etatAction = etatAction;
		this.dateAction = dateAction;
	}

	public Action(String idAction, String nomAction, String dateAction) {
		super();
		this.idAction = idAction;
		this.nomAction = nomAction;
		this.dateAction = dateAction;
	}
	/**
	 * constructeur vide
	 */
	//JPA
	public Action() {
		super();
		this.idAction = UUID.randomUUID().toString();
	}

	/**
	 * 
	 * @param idAction
	 * @param nomAction
	 * @param etatAction
	 * @param dateAction
	 */
	public Action(String nomAction, Etat etatAction, String dateAction) {
		super();
		this.idAction = UUID.randomUUID().toString();
		this.nomAction = nomAction;
		this.etatAction = etatAction;
		this.dateAction = dateAction;
	}
	
	
	

	/**
	 * id unique de l'action
	 */
	@Id
	private String idAction;
	
	/**
	 * nom de l'action
	 */
	private String nomAction;
	
	/**
	 * etat courant de l'action
	 */
	@ManyToOne
	@JoinColumn(name="id_etat")
	private Etat etatAction;
	
	/**
	 * date de l'action
	 */
	private String dateAction;
	
	//@ManyToMany(mappedBy="listeAction", cascade = CascadeType.ALL)
	@OneToMany(fetch = FetchType.LAZY, mappedBy ="pk.intervenant")
	private List<ActionIntervenant> listeIntervenants;

	/**
	 * demande reliée à l'action
	 */
	@ManyToOne
	@JoinColumn(name="id_demande")
	private Demande demande;
	
	/**
	 * getter id action
	 * @return idAction
	 */
	public String getIdAction() {
		return idAction;
	}

	/**
	 * getter nomAction
	 * @return nomAction
	 */
	public String getNomAction() {
		return nomAction;
	}

	/**
	 * getter etatAction
	 * @return etatAction
	 */
	public Etat getEtatAction() {
		return etatAction;
	}

	/**
	 * getter dateAction
	 * @return dateAction
	 */
	public String getDateAction() {
		return dateAction;
	}

	/**
	 * setter idAction
	 * @param idAction
	 */
	public void setIdAction(String idAction) {
		this.idAction = idAction;
	}

	/**
	 * setter nomAction
	 * @param nomAction
	 */
	public void setNomAction(String nomAction) {
		this.nomAction = nomAction;
	}

	/**
	 * setter etatAction
	 * @param etatAction
	 */
	public void setEtatAction(Etat etatAction) {
		this.etatAction = etatAction;
	}

	/**
	 * setter dateAction
	 * @param dateAction
	 */
	public void setDateAction(String dateAction) {
		this.dateAction = dateAction;
	}

	/**
	 * getter de demande
	 * @return demande
	 */
	public Demande getDemande() {
		return demande;
	}

	/**
	 * setter de demande
	 * @param demande
	 */
	public void setDemande(Demande demande) {
		this.demande = demande;
	}

	public List<ActionIntervenant> getListeIntervenants() {
		return listeIntervenants;
	}

	public void setListeIntervenants(List<ActionIntervenant> listeIntervenants) {
		this.listeIntervenants = listeIntervenants;
	}
	
	
	
}
