package org.m2acsi.entity.demande;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.m2acsi.entity.intervention.Intervenant;

@Entity
@Table(name="actionIntervenant")
@AssociationOverrides({
@AssociationOverride(name ="pk.action", joinColumns = @JoinColumn(name ="id_action")),
@AssociationOverride(name ="pk.intervenant", joinColumns = @JoinColumn(name ="id_intervenant"))
        })
public class ActionIntervenant {

	@Embeddable
	public static class ActionIntervenantPk implements Serializable
	{
		
		public ActionIntervenantPk(Action action, Intervenant intervenant, int responsable) {
			super();
			this.action = action;
			this.intervenant = intervenant;
			this.responsable = responsable;
		}

		public ActionIntervenantPk()
		{
			
		}
		
		
		@ManyToOne
		@PrimaryKeyJoinColumn(name="id_action", referencedColumnName="id_action")
		private Action action;
		
		@ManyToOne
		@PrimaryKeyJoinColumn(name="id_intervenant" , referencedColumnName="id_intervenant")
		private Intervenant intervenant;
		
		private int responsable;

		public Action getAction() {
			return action;
		}

		public Intervenant getIntervenant() {
			return intervenant;
		}

		public int getResponsable() {
			return responsable;
		}

		public void setAction(Action action) {
			this.action = action;
		}

		public void setIntervenant(Intervenant intervenant) {
			this.intervenant = intervenant;
		}

		public void setResponsable(int responsable) {
			this.responsable = responsable;
		}
		
		
	}
	public ActionIntervenant() {
		super();

	}
	
	public ActionIntervenant(Action idAction, Intervenant idIntervenant, int resp) {
		super();
		this.pk = new ActionIntervenantPk(idAction, idIntervenant, resp);
	}
	
	@EmbeddedId
	private ActionIntervenantPk pk = new ActionIntervenantPk();

	public ActionIntervenantPk getPk() {
		return pk;
	}

	public void setPk(ActionIntervenantPk pk) {
		this.pk = pk;
	}
	
	
	
}
