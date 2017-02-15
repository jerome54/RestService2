package org.m2acsi.boundary;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.m2acsi.entity.Action;
import org.m2acsi.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ActionRessource extends JpaRepository<Action, String>{

	//@Query("SELECT id_action, etat_action, nom_action, date_action, id_demande from action where id_demande = :id")
	//public List<Action> findByIdDemande(String id);
	
	@Query("SELECT new Action(idAction, nomAction, etatAction, dateAction) from org.m2acsi.entity.Action a where a.demande.idDemande = :id")
	public List<Action> recupActionParDemande(@Param("id")String id);
	
	@Query("SELECT new Action(idAction, nomAction, etatAction, dateAction) from org.m2acsi.entity.Action a where a.demande.idDemande = :id and a.idAction = :idAction")
	public List<Action> recupActionParDemandeParId(@Param("id")String id, @Param("idAction") String idAction);
}
