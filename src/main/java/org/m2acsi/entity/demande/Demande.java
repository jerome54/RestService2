package org.m2acsi.entity.demande;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.m2acsi.entity.intervention.Intervenant;

/**
 * Classe concernant une demande
 * @author 
 *
 */
@Entity
@Table(name = "Demande")
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
	 * Libelle demande
	 */
	private String libelleDemande;
	
	@ManyToOne
	@JoinColumn(name="id_type_incident")
	private TypeIncident type;
	
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
	@ManyToOne
	@JoinColumn(name="id_etat")
	private Etat etat;
	
	/**
	 * liste des actions reliées à la demande
	 */
	@OneToMany(mappedBy="demande", cascade = CascadeType.ALL)
	private List<Action> listeAction;
	
	private String token;
	
	
	
	/*------------------CONSTRUCTEUR-------------------*/
	
	/**
	 * Constructeur d'une demande
	 * @param idDemande l'id
	 * @param nom le nom du citoyen
	 * @param prenom le prenom du citoyen
	 * @param adresse du citoyen
	 * @param etat de la demande
	 */
	public Demande(String nom, String prenom, String adresse, String libelleDemande) {
		super();
		this.idDemande = UUID.randomUUID().toString();
		this.nom = nom;
		this.libelleDemande = libelleDemande;
		this.prenom = prenom;
		this.adresse = adresse;
		this.token = UUID.randomUUID().toString().replaceAll("-", "");
	}


	/**
	 * Constructeur vide
	 */
	//JPA
	public Demande() {
		super();
		this.idDemande = UUID.randomUUID().toString();
		this.token = UUID.randomUUID().toString().replaceAll("-", "");
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


	public Etat getEtat() {
		return etat;
	}


	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public List<Action> getListeAction() {
		return listeAction;
	}

	public void setListeAction(List<Action> listeAction) {
		this.listeAction = listeAction;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	/*---------------------ToString-----------------------*/
	@Override
	public String toString() {
		return "Demande [idDemande=" + idDemande + ", nom=" + nom + ", prenom=" + prenom + ", adresse=" + adresse
				+ ", etat=" + etat + "]";
	}
	
	
	
	
}
