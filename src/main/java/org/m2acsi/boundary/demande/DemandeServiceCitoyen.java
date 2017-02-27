package org.m2acsi.boundary.demande;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.m2acsi.boundary.demande.DemandeRessource;
import org.m2acsi.entity.demande.Action;
import org.m2acsi.entity.demande.Demande;

@RestController
@RequestMapping(value="/citoyen/demandes",produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Demande.class)
public class DemandeServiceCitoyen {
	
	@Autowired
    DemandeRessource demDao;
	@Autowired
	ActionRessource actDao;
	
	/**
	 * Get de la liste totale des demandes
	 * @return la liste complète des demandes
	 */
    /*@GetMapping(value = "/liste")
    public ResponseEntity<?> getAllFormations(){
        Iterable<Demande> allDemande = demDao.findAll();
        Iterator<Demande> d = allDemande.iterator();
        Demande ne = d.next();
        return new ResponseEntity<>(demandeToRessource(allDemande),HttpStatus.OK);
    }*/
    
    /**
     * get d'une demande à partir de son id
     * @param id
     * @return la demande
     */
    @GetMapping(value="/{demandeId}")
    public ResponseEntity<?> getOneDemande(@PathVariable("demandeId") String id, @RequestHeader("token") String token){
    	Demande demande = demDao.findOne(id);
    	if(!demande.getToken().equals(token))
    	{
    		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    	}
    	return Optional.ofNullable(demDao.findOne(id)).map(d -> new ResponseEntity<>(demandeToRessource(d,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    
    /**
     * get des la liste des actions d'une demandes
     * @param id
     * @return la liste des actions d'une demande
     */
    @GetMapping(value="/{demandeId}/actions")
    public ResponseEntity<?> getActionsByDemande(@PathVariable("demandeId") String id, @RequestHeader("token") String token){
    	Demande demande = demDao.findOne(id);
    	if(!demande.getToken().equals(token))
    	{
    		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    	}
    	Iterable<Action> demandeAction = actDao.recupActionParDemande(id);
    	return new ResponseEntity<>(actionToRessource(demandeAction, id, token), HttpStatus.OK);
    }
    
    /**
     * get d'une action selon son id pour une demande
     * @param id
     * @param idAction
     * @return l'action selon l'id pour une demande
     */
    @GetMapping(value="/{demandeId}/{idAction}")
    public ResponseEntity<?> getActionByDemandeById(@PathVariable("demandeId") String id, @PathVariable("idAction") String idAction, @RequestHeader("token") String token){
    	Demande demande = demDao.findOne(id);
    	if(!demande.getToken().equals(token))
    	{
    		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    	}
    	Iterable<Action> demandeAction = actDao.recupActionParDemandeParId(id, idAction);
    	return new ResponseEntity<>(actionToRessource(demandeAction, id, token), HttpStatus.OK);
    }
    
    
    /**
     * Post d'une demande
     * @param demande
     * @return création d'une demande
     */
    @PostMapping
    public ResponseEntity<?> saveDemande(@RequestBody Demande demande){
        Demande sauvegarde = demDao.save(demande);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(DemandeServiceCitoyen.class).slash(sauvegarde.getIdDemande()).toUri());
        return new ResponseEntity<>(sauvegarde, responseHeaders, HttpStatus.CREATED);
    }
    
    /**
     * put d'une demande lorsque l'état est début
     * @param demandeId
     * @param demande
     * @return le update d'une demande en état début
     */
    @RequestMapping(method=RequestMethod.PUT, value="/{demandeId}")
    public ResponseEntity<?> updateDemande(@PathVariable("demandeId") String demandeId, @RequestBody Demande demande){
            Optional<Demande> body = Optional.ofNullable(demande);
            if(!body.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if(!demDao.exists(demandeId)){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Demande initiale = demDao.findByIdDemande(demandeId);
            if(demande.getEtat() != null)
            {
            	return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            if(!initiale.getEtat().equals("DEBUT"))
            {
            	return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            if(demande.getAdresse() == null)
            {
            	demande.setAdresse(initiale.getAdresse());
            }
            
            if(demande.getNom() == null)
            {
            	demande.setNom(initiale.getNom());
            }
            
            if(demande.getPrenom() == null)
            {
            	demande.setPrenom(initiale.getPrenom());
            }
            demande.setIdDemande(demandeId);
            Demande d = demDao.save(demande);
            return new ResponseEntity<>(HttpStatus.OK);
}
    

    /***********************************************************************************************************************************
     * 
     *  														HATEOS
     *  
     ***********************************************************************************************************************************/
    
    //HATEOS pour les demandes
    private Resource<Demande> demandeToRessource(Demande demande, Boolean isCollection){
        Link selfLink = linkTo(DemandeServiceCitoyen.class).slash(demande.getIdDemande()).withSelfRel();
        /*if(isCollection){
            Link collectionLink = linkTo(methodOn(DemandeServiceCitoyen.class).getAllFormations()).slash("liste").withRel("collection ");
            return new Resource<>(demande, selfLink, collectionLink);
        }else{*/
            return new Resource<>(demande, selfLink);
        //}
    }
    
    
    private Resources<Resource<Demande>> demandeToRessource(Iterable<Demande> demande){
        Link selfLink = linkTo(methodOn(DemandeServiceCitoyen.class)).slash("liste").withSelfRel();
        List<Resource<Demande>> listFormations = new ArrayList();
        demande.forEach(formation -> listFormations.add(demandeToRessource(formation, false)));
        return new Resources<>(listFormations, selfLink);
    }

    //HATEOS pour les actions
    private Resource<Action> actionToRessource(Action action, Boolean isCollection, String idDemande, String token){
        Link selfLink = linkTo(DemandeServiceCitoyen.class).slash(idDemande).slash(action.getIdAction()).withSelfRel();
        if(isCollection){
            Link collectionLink = linkTo(methodOn(DemandeServiceCitoyen.class).getActionsByDemande(idDemande, token)).slash("liste").slash(idDemande).withRel("collection ");
            return new Resource<>(action, selfLink, collectionLink);
        }else{
            return new Resource<>(action, selfLink);
        }
    }
    
    private Resources<Resource<Action>> actionToRessource(Iterable<Action> action, String idDemande, String token){
        Link selfLink = linkTo(methodOn(DemandeServiceCitoyen.class).getActionsByDemande(idDemande, token)).withSelfRel();
        List<Resource<Action>> listFormations = new ArrayList();
        action.forEach(formation -> listFormations.add(actionToRessource((Action)formation, false, idDemande, token)));
        return new Resources<>(listFormations, selfLink);
    }
}
