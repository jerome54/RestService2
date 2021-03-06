package org.m2acsi.boundary.demande;

import org.m2acsi.entity.demande.Demande;
import org.m2acsi.entity.demande.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface DemandeRessource extends JpaRepository<Demande, String>  {
	public List<Demande> findByEtat(Etat status);
	
	public Demande findByIdDemande(String idDemande);
}
	
	
		
	
