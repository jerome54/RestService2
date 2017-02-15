package org.m2acsi.boundary;


import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.m2acsi.boundary.DemandeRessource;
import org.m2acsi.entity.Action;
import org.m2acsi.entity.Demande;

@RestController
@RequestMapping(value="/demandes",produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Demande.class)
public class DemandeService {
	
	@Autowired
    DemandeRessource demDao;
	@Autowired
	ActionRessource actDao;
	
	/**
	 * Get de la liste totale des demandes
	 * @return la liste complète des demandes
	 */
    @GetMapping(value = "/liste")
    public ResponseEntity<?> getAllFormations(){
        Iterable<Demande> allDemande = demDao.findAll();
        return new ResponseEntity<>(demandeToRessource(allDemande),HttpStatus.OK);
    }
    
    /**
     * get d'une demande à partir de son id
     * @param id
     * @return la demande
     */
    @GetMapping(value="/{demandeId}")
    public ResponseEntity<?> getOneDemande(@PathVariable("demandeId") String id){
    	return Optional.ofNullable(demDao.findOne(id)).map(d -> new ResponseEntity<>(demandeToRessource(d,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * get de la liste des demandes par statuts
     * @param etat
     * @return la liste des demandes selon un statut
     */
    @GetMapping(value="/")
    public ResponseEntity<?> getDemandeByStatus(@PathParam("etat") String etat){
    	List<Demande> demandeStatus = demDao.findByEtat(etat);
    	return new ResponseEntity<>(demandeToRessource(demandeStatus), HttpStatus.OK);
    }
    
    /**
     * get des la liste des actions d'une demandes
     * @param id
     * @return la liste des actions d'une demande
     */
    @GetMapping(value="/{demandeId}/actions")
    public ResponseEntity<?> getActionsByDemande(@PathVariable("demandeId") String id){
    	Iterable<Action> demandeAction = actDao.recupActionParDemande(id);
    	return new ResponseEntity<>(actionToRessource(demandeAction, id), HttpStatus.OK);
    }
    
    /**
     * get d'une action selon son id pour une demande
     * @param id
     * @param idAction
     * @return l'action selon l'id pour une demande
     */
    @GetMapping(value="/{demandeId}/{idAction}")
    public ResponseEntity<?> getActionByDemandeById(@PathVariable("demandeId") String id, @PathVariable("idAction") String idAction){
    	Iterable<Action> demandeAction = actDao.recupActionParDemandeParId(id, idAction);
    	return new ResponseEntity<>(actionToRessource(demandeAction, id), HttpStatus.OK);
    }
    
    /**
     * Post d'une action pour une demande
     * @param action
     * @param id
     * @return création d'une action pour demande
     */
    @PostMapping(value="/{demandeId}")
    public ResponseEntity<?> saveAction(@RequestBody Action action, @PathVariable("demandeId") String id)
    {
    	 action.setDemande(demDao.findOne(id));
    	 Action sauvegarde = actDao.save(action);
         HttpHeaders responseHeaders = new HttpHeaders();
         responseHeaders.setLocation(linkTo(DemandeService.class).slash(sauvegarde.getIdAction()).toUri());
         return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
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
        responseHeaders.setLocation(linkTo(DemandeService.class).slash(sauvegarde.getIdDemande()).toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    /**
     * put d'une demande lorsque l'état est début
     * @param demandeId
     * @param demande
     * @return le update d'une demande en état début
     */
    @RequestMapping(method=RequestMethod.PUT, value="/{demandeId}")
    public ResponseEntity<?> updateFormation(@PathVariable("demandeId") String demandeId, @RequestBody Demande demande){
            Optional<Demande> body = Optional.ofNullable(demande);
            if(!body.isPresent()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if(!demDao.exists(demandeId)){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Demande initiale = demDao.findByIdDemande(demandeId);
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
    
    /**
     * delete/cloture d'une demande
     * @param demandeId
     * @param demande
     * @return cloture d'une demande
     */
    @RequestMapping(method=RequestMethod.DELETE, value="/{demandeId}")
    public ResponseEntity<?> cloturerFormation(@PathVariable("demandeId") String demandeId){
        if(!demDao.exists(demandeId)){
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
         
        Demande initiale = demDao.findByIdDemande(demandeId);
        initiale.setEtat("FERME");
        Demande d = demDao.save(initiale);
    	return new ResponseEntity<>(HttpStatus.OK);
    }
    /***********************************************************************************************************************************
     * 
     *  														HATEOS
     *  
     ***********************************************************************************************************************************/
    
    //HATEOS pour les demandes
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

    //HATEOS pour les actions
    private Resource<Action> actionToRessource(Action action, Boolean isCollection, String idDemande){
        Link selfLink = linkTo(DemandeService.class).slash(idDemande).slash(action.getIdAction()).withSelfRel();
        if(isCollection){
            Link collectionLink = linkTo(methodOn(DemandeService.class).getActionsByDemande(idDemande)).withRel("collection ");
            return new Resource<>(action, selfLink, collectionLink);
        }else{
            return new Resource<>(action, selfLink);
        }
    }
    
    private Resources<Resource<Action>> actionToRessource(Iterable<Action> action, String idDemande){
        Link selfLink = linkTo(methodOn(DemandeService.class).getActionsByDemande(idDemande)).withSelfRel();
        List<Resource<Action>> listFormations = new ArrayList();
        action.forEach(formation -> listFormations.add(actionToRessource((Action)formation, false, idDemande)));
        return new Resources<>(listFormations, selfLink);
    }
}
