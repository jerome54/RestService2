package org.m2acsi.boundary;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.m2acsi.boundary.DemandeRessource;
import org.m2acsi.entity.DaoDemandeImpl;
import org.m2acsi.entity.Demande;

@RestController
@RequestMapping(value="/demandes",produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Demande.class)
public class DemandeService {
	
	@Autowired
    DemandeRessource demDao;
	
    @GetMapping
    public ResponseEntity<?> getAllFormations(){
        Iterable<Demande> allDemande = demDao.findAll();
        return new ResponseEntity<>(demandeToRessource(allDemande),HttpStatus.OK);
    }
    
    @GetMapping(value="/{demandeId}")
    public ResponseEntity<?> getOneDemande(@PathVariable("demandeId") String id){
    	return Optional.ofNullable(demDao.findOne(id)).map(d -> new ResponseEntity<>(demandeToRessource(d,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping(value="/")
    public ResponseEntity<?> getDemandeByStatus(@PathParam("etat") String etat){
    	DaoDemandeImpl dao = new DaoDemandeImpl();
    	Iterable<Demande> demandeByStatus = dao.findDemandeByStatus(etat);
    	return new ResponseEntity<>(demandeToRessource(demandeByStatus), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<?> saveDemande(@RequestBody Demande demande){
        Demande sauvegarde = demDao.save(demande);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(DemandeService.class).slash(sauvegarde.getIdDemande()).toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    
    /***********************************************************************************************************************************
     * 
     *  														HATEOS
     *  
     ***********************************************************************************************************************************/
    
    private Resource<Demande> demandeToRessource(Demande demande, Boolean isCollection){
        Link selfLink = linkTo(DemandeService.class).slash(demande.getIdDemande()).withSelfRel();
        if(isCollection){
            Link collectionLink = linkTo(methodOn(DemandeService.class).getAllFormations()).withRel("collection ");
            return new Resource<>(demande, selfLink, collectionLink);
        }else{
            return new Resource<>(demande, selfLink);
        }
    }
    
    
    private Resources<Resource<Demande>> demandeToRessource(Iterable<Demande> demande){
        Link selfLink = linkTo(methodOn(DemandeService.class).getAllFormations()).withSelfRel();
        List<Resource<Demande>> listFormations = new ArrayList();
        demande.forEach(formation -> listFormations.add(demandeToRessource(formation, false)));
        return new Resources<>(listFormations, selfLink);
    }

}
