package org.m2acsi.boundary.demande;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.m2acsi.entity.demande.Action;
import org.m2acsi.entity.demande.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ActionRessource extends JpaRepository<Action, String>{
	
	@Query("SELECT a from org.m2acsi.entity.demande.Action a where a.demande.idDemande = :id")
	public List<Action> recupActionParDemande(@Param("id")String id);
	
	@Query("SELECT new Action(idAction, nomAction, dateAction) from org.m2acsi.entity.demande.Action a where a.demande.idDemande = :id and a.idAction = :idAction")
	public List<Action> recupActionParDemandeParId(@Param("id")String id, @Param("idAction") String idAction);

}
