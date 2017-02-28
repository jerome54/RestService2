package org.m2acsi.boundary.intervention;

import org.m2acsi.entity.demande.Action;
import org.m2acsi.entity.demande.Demande;
import org.m2acsi.entity.intervention.Intervenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RepositoryRestResource
public interface IntervenantRessource extends JpaRepository<Intervenant, String>{
	
}
