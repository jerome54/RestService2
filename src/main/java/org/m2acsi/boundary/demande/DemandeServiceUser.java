package org.m2acsi.boundary.demande;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.m2acsi.boundary.intervention.IntervenantRessource;
import org.m2acsi.entity.demande.Action;
import org.m2acsi.entity.demande.ActionIntervenant;
import org.m2acsi.entity.demande.Demande;
import org.m2acsi.entity.demande.Etat;
import org.m2acsi.entity.intervention.Intervenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/users/demandes",produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Demande.class)
public class DemandeServiceUser {

	@Autowired
    DemandeRessource demDao;
	@Autowired
	ActionRessource actDao;
	@Autowired
	ActionIntervenantRessource intDao;
	@Autowired
	IntervenantRessource interDao;
	@Autowired
	EtatRessource etatDao;
	
	/**
	 * Get de la liste totale des demandes
	 * @return la liste complète des demandes
	 */
    @GetMapping(value = "/liste")
    public ResponseEntity<?> getAllFormations(){
        Iterable<Demande> allDemande = demDao.findAll();
        Iterator<Demande> d = allDemande.iterator();
        Demande ne = d.next();
        return new ResponseEntity<>(demandeToRessource(allDemande),HttpStatus.OK);
    }
    
    /**
     * get d'une demande à partir de son id
     * @param id
     * @return la demande
     */
    @GetMapping(value="/{demandeId}")
    public ResponseEntity<?> getOneDemande(@PathVariable("demandeId") String id){
    	Demande demande = demDao.findOne(id);
    	return Optional.ofNullable(demDao.findOne(id)).map(d -> new ResponseEntity<>(demandeToRessource(d,true),HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
     * get des la liste des intervenants d'une action d'une demandes
     * @param id
     * @return liste des intervenants
     */
    @GetMapping(value="/{demandeId}/{actionId}/intervenants")
    public ResponseEntity<?> getIntervenantByAction(@PathVariable("demandeId") String id, @PathVariable("actionId") String idAction){
    	List<Action> demandeAction = actDao.recupActionParDemandeParId(id, idAction);
    	List<ActionIntervenant> listeActionIntervenant = intDao.recupIntervenantParAction(idAction);
    	List<Intervenant> listeIntervenant = new ArrayList<Intervenant>();
    	for(int i = 0; i < listeActionIntervenant.size(); i++)
    	{
    		listeIntervenant.add(interDao.findOne(listeActionIntervenant.get(i).getPk().getIntervenant().getIdIntervenant()));
    	}
    	return new ResponseEntity<>(intervenantToRessource(listeIntervenant, idAction, id), HttpStatus.OK);
    }
    
    /**
     * get des la liste des intervenants d'une action d'une demandes
     * @param id
     * @return liste des intervenants
     */
    @GetMapping(value="/{demandeId}/{actionId}/intervenant")
    public ResponseEntity<?> getIntervenantByActionIntervenantDep(@PathVariable("demandeId") String id, @PathVariable("actionId") String idAction, @PathParam("responsable") int responsable){
    	List<Action> demandeAction = actDao.recupActionParDemandeParId(id, idAction);
    	List<ActionIntervenant> listeActionIntervenant = intDao.recupIntervenantParActionResp(idAction, responsable);
    	List<Intervenant> listeIntervenant = new ArrayList<Intervenant>();
    	for(int i = 0; i < listeActionIntervenant.size(); i++)
    	{
    		listeIntervenant.add(interDao.findOne(listeActionIntervenant.get(i).getPk().getIntervenant().getIdIntervenant()));
    	}
    	return new ResponseEntity<>(intervenantToRessource(listeIntervenant, idAction, id), HttpStatus.OK);
    }
    
    /**
     * get de la liste des demandes par statuts
     * @param etat
     * @return la liste des demandes selon un statut
     */
    @GetMapping(value="/")
    public ResponseEntity<?> getDemandeByStatus(@PathParam("etat") String etat){
    	//List<Demande> demandeStatus = demDao.findByEtat(etat);
    	Etat etatCherche = etatDao.findByLibelleEtat(etat);
    	List<Demande> demandeStatus = demDao.findByEtat(etatCherche);
    	return new ResponseEntity<>(demandeToRessource(demandeStatus), HttpStatus.OK);
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
    	 Demande demande = demDao.findOne(id);
    	 action.setEtatAction(etatDao.findOne(action.getEtatAction().getIdEtat()));
    	 String test = this.getUserName();
    	 Intervenant interTest = interDao.findOne(this.getUserName());
    	 ActionIntervenant actInter = new ActionIntervenant(action, interDao.findOne(this.getUserName()), 0);

    	 if(demande.getEtat().getLibelleEtat().equals("Fermé"))
    	 {
    		 return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    	 }
    	 
    	 if((demande.getEtat().getLibelleEtat().equals("Approuvé") && demande.getEtat().getLibelleEtat().equals("Rejeté")) && 
    			 	!action.getEtatAction().getLibelleEtat().equals("Création commande"))
    	 {
    		 return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    	 }
    	 
    	 if((demande.getEtat().getLibelleEtat().equals("Début")) && 
 			 	(!action.getEtatAction().getLibelleEtat().equals("Etude") && !action.getEtatAction().getLibelleEtat().equals("Etude détaillée")))
    	 {
    		 return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    	 }
    	 
    	 if((demande.getEtat().getLibelleEtat().equals("Etude") || demande.getEtat().getLibelleEtat().equals("Etude détaillée")) && 
 			 	(!action.getEtatAction().getLibelleEtat().equals("Approuvé") && !action.getEtatAction().getLibelleEtat().equals("Rejeté")))
    	 {
    		 return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    	 }
    	 action.setDemande(demDao.findOne(id));
    	 Action sauvegarde = actDao.save(action);
    	 ActionIntervenant actionIntervenant = intDao.save(actInter);
    	 demande.setEtat(action.getEtatAction());
    	 Demande sauvegardeDemande = demDao.save(demande);
         HttpHeaders responseHeaders = new HttpHeaders();
         responseHeaders.setLocation(linkTo(DemandeServiceCitoyen.class).slash(sauvegarde.getIdAction()).toUri());
         return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
    
    /**
     * Post d'une action pour une demande
     * @param action
     * @param id
     * @return création d'une action pour demande
     */
    @PutMapping(value="/{demandeId}/{actionId}")
    public ResponseEntity<?> updateIntervenantResponsable(@RequestBody Intervenant intervenant, @PathVariable("demandeId") String id, @PathVariable("actionId") String idAction)
    {
    	Intervenant inter = interDao.findOne(intervenant.getIdIntervenant());
    	Action action = actDao.findOne(idAction);
    	ActionIntervenant actInter = new ActionIntervenant(action, inter, 1);
    	try
    	{
    		ActionIntervenant sauvegarde = intDao.save(actInter);
    	} catch(Exception sql)
    	{
    		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    	}
    	return new ResponseEntity<>(null, HttpStatus.OK);
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
        initiale.setToken(null);
        
        Etat etat = etatDao.findByLibelleEtat("Fermé");
        initiale.setEtat(etat);
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
        Link selfLink = linkTo(DemandeServiceUser.class).slash(demande.getIdDemande()).withSelfRel();
        if(isCollection){
            Link collectionLink = linkTo(methodOn(DemandeServiceUser.class).getAllFormations()).slash("liste").withRel("collection ");
            return new Resource<>(demande, selfLink, collectionLink);
        }else{
            return new Resource<>(demande, selfLink);
        }
    }
    
    
    private Resources<Resource<Demande>> demandeToRessource(Iterable<Demande> demande){
        Link selfLink = linkTo(methodOn(DemandeServiceUser.class).getAllFormations()).slash("liste").withSelfRel();
        List<Resource<Demande>> listFormations = new ArrayList();
        demande.forEach(formation -> listFormations.add(demandeToRessource(formation, false)));
        return new Resources<>(listFormations, selfLink);
    }

    //HATEOS pour les actions
    private Resource<Action> actionToRessource(Action action, Boolean isCollection, String idDemande){
        Link selfLink = linkTo(DemandeServiceUser.class).slash(idDemande).slash(action.getIdAction()).withSelfRel();
        if(isCollection){
            Link collectionLink = linkTo(methodOn(DemandeServiceUser.class).getActionsByDemande(idDemande)).slash("liste").slash(idDemande).withRel("collection ");
            return new Resource<>(action, selfLink, collectionLink);
        }else{
            return new Resource<>(action, selfLink);
        }
    }
    
    private Resources<Resource<Action>> actionToRessource(Iterable<Action> action, String idDemande){
        Link selfLink = linkTo(methodOn(DemandeServiceUser.class).getActionsByDemande(idDemande)).withSelfRel();
        List<Resource<Action>> listFormations = new ArrayList();
        action.forEach(formation -> listFormations.add(actionToRessource((Action)formation, false, idDemande)));
        return new Resources<>(listFormations, selfLink);
    }
    
    //HATEOS pour les intervenants
    private Resource<Intervenant> intervenantToRessource(Intervenant intervenant, Boolean isCollection, String idAction, String idDemande){
        Link selfLink = linkTo(DemandeServiceUser.class).slash(idDemande).slash(idAction).slash(intervenant.getIdIntervenant()).withSelfRel();
        if(isCollection){
            Link collectionLink = linkTo(methodOn(DemandeServiceUser.class).getActionsByDemande(idDemande)).slash("liste").slash(idDemande).withRel("collection ");
            return new Resource<>(intervenant, selfLink, collectionLink);
        }else{
            return new Resource<>(intervenant, selfLink);
        }
    }
    
    private Resources<Resource<Intervenant>> intervenantToRessource(Iterable<Intervenant> action, String idAction, String idDemande){
        Link selfLink = linkTo(methodOn(DemandeServiceUser.class).getActionsByDemande(idDemande)).slash("intervenants").withSelfRel();
        List<Resource<Intervenant>> listFormations = new ArrayList();
        action.forEach(formation -> listFormations.add(intervenantToRessource((Intervenant)formation, false, idAction,idDemande)));
        return new Resources<>(listFormations, selfLink);
    }
    
    /***********************************************************************************************************************************
     * 
     *  												INFORMATIONS RELATIVES USERS
     *  
     ***********************************************************************************************************************************/
    
    private boolean possedePrivilege(String privilege)
    {
    	return getAuthorities().contains(new SimpleGrantedAuthority(privilege));
    }
    
    private Collection<SimpleGrantedAuthority> getAuthorities()
    {
    	Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    	
    	return authorities;
    }
    
    private String getUserName()
    {
    	String name = (String) SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	return name;
    }
}
