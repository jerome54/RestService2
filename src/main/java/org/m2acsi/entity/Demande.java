package org.m2acsi.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Classe concernant une demande
 * @author 
 *
 */
@Entity(name="Demande")
@Table(name = "Demande")
@NamedQueries({ 
    @NamedQuery(name = "Demande.findByStatus", query = "SELECT idDemande, nom, prenom, adresse, etat FROM demande WHERE demande.etat = :etat")
})
public class Demande {
	
	/**
	 * Classe énum pour les états d'une demande
	 */
	public static enum EtatDemande {

		/** état initial de la demande */
		DEBUT("debut"),

		/** état final de la demande. */
		FIN("fin"),

		/** la demande est à l'etude. */
		ETUDE("etude"),
		
		/** la demande nécessite une étude détaillé. */
		ETUDE_DETAIL("etude detaillee"),
		
		/** la demande à été approuvée. */
		APPROUVE("approuvee"),
		
		/** la demande est rejetée. */
		REJET("rejet");

		/** TL'état de la demande. */
		private String nomEtat;

		/**
		 * Instance d'un etat.
		 * 
		 * @param nom le nom de l'état
		 */
		private EtatDemande(String nom) {
			this.nomEtat = nom;
		}

		/**
		 * Get le nom de l'etat.
		 * 
		 * @return le nom de l'état
		 */
		public String getNomEtat() {
			return nomEtat;
		}

	};  
	
	/* 
	 * fin de la classe énum
	*/
	
	
	/*----------------ATTRIBUTS-----------------*/
	
	/**
	 * Id demande
	 */
	//@Column(name="idDemande")
	@Id
	private String idDemande;
	
	/**
	 * Nom citoyen
	 */
	//@Column(name="nom")
	private String nom;
	
	/**
	 * Prénom citoyen
	 */
	//@Column(name="prenom")
	private String prenom;
	
	/**
	 * Adresse citoyen
	 */
	//@Column(name="adresse")
	private String adresse;
	
	/**
	 * Etat demande (avec la classe enum)
	 */
	//@Column(name="etat")
	@Enumerated(EnumType.STRING)
	private EtatDemande etat;

	
	
	/*------------------CONSTRUCTEUR-------------------*/
	
	/**
	 * Constructeur d'une demande
	 * @param idDemande l'id
	 * @param nom le nom du citoyen
	 * @param prenom le prenom du citoyen
	 * @param adresse du citoyen
	 * @param etat de la demande
	 */
	public Demande(String nom, String prenom, String adresse, EtatDemande etat) {
		super();
		this.idDemande = UUID.randomUUID().toString();
		this.nom = nom;
		this.prenom = prenom;
		this.adresse = adresse;
		this.etat = etat;
	}


	/**
	 * Constructeur vide
	 */
	public Demande() {
		super();
		this.idDemande = UUID.randomUUID().toString();
	}

	
	/*------------------------GETTER & SETTER-------------------*/

	public String getIdDemande() {
		return idDemande;
	}


	public void setIdDemande(String idDemande) {
		this.idDemande = idDemande;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public EtatDemande getEtat() {
		return etat;
	}


	public void setEtat(EtatDemande etat) {
		this.etat = etat;
	}

	/*---------------------ToString-----------------------*/
	@Override
	public String toString() {
		return "Demande [idDemande=" + idDemande + ", nom=" + nom + ", prenom=" + prenom + ", adresse=" + adresse
				+ ", etat=" + etat + "]";
	}
	
	
	
	
}
