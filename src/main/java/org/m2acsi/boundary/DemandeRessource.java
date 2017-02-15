package org.m2acsi.boundary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

import org.m2acsi.entity.Demande;

@RepositoryRestResource
public interface DemandeRessource extends JpaRepository<Demande, String>  {
	
	public List<Demande> findDemandeByStatus(String status);
}
	
	
		
	
