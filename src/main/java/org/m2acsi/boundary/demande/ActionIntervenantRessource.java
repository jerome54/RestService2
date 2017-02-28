package org.m2acsi.boundary.demande;

import java.util.List;

import org.m2acsi.entity.demande.Action;
import org.m2acsi.entity.demande.ActionIntervenant;
import org.m2acsi.entity.intervention.Intervenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ActionIntervenantRessource extends JpaRepository<ActionIntervenant, String>{

	@Query("SELECT a from org.m2acsi.entity.demande.ActionIntervenant a where a.pk.action.idAction = :idAction")
	List<ActionIntervenant> recupIntervenantParAction(@Param("idAction")String id);
	
	@Query("SELECT a from org.m2acsi.entity.demande.ActionIntervenant a where a.pk.action.idAction = :idAction and a.pk.responsable = :responsable")
	List<ActionIntervenant> recupIntervenantParActionResp(@Param("idAction")String id, @Param("responsable") int responsable);
	
	@Query("SELECT a from org.m2acsi.entity.demande.ActionIntervenant a where a.pk.intervenant.idIntervenant = :idIntervenant")
	List<ActionIntervenant> recupActionByIntervenant(@Param("idIntervenant") String idIntervenant);
}
