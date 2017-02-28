package org.m2acsi.boundary.demande;

import org.m2acsi.entity.demande.Demande;
import org.m2acsi.entity.demande.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EtatRessource extends JpaRepository<Etat, String>  {
	
	public Etat findByLibelleEtat(String libelle);

}
