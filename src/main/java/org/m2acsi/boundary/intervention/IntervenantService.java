package org.m2acsi.boundary.intervention;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.m2acsi.boundary.demande.ActionIntervenantRessource;
import org.m2acsi.boundary.demande.ActionRessource;
import org.m2acsi.boundary.demande.DemandeRessource;
import org.m2acsi.boundary.demande.DemandeServiceUser;
import org.m2acsi.entity.demande.Action;
import org.m2acsi.entity.demande.ActionIntervenant;
import org.m2acsi.entity.demande.Demande;
import org.m2acsi.entity.intervention.Intervenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/users/intervenants",produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Intervenant.class)
public class IntervenantService {

	@Autowired
	private IntervenantRessource intDao;
	@Autowired
	private ActionIntervenantRessource actDao;
	@Autowired
	private ActionRessource actionDao;
	@Autowired
	private DemandeRessource demDao;
	
    @GetMapping(value = "/liste")
    public ResponseEntity<?> getAllFormations(){
        Iterable<Intervenant> allDemande = intDao.findAll();
        Iterator<Intervenant> d = allDemande.iterator();
        Intervenant ne = d.next();
        return new ResponseEntity<>(intervenantToRessource(allDemande),HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/{idIntervenant}")
    public ResponseEntity<?> getIntervenantById(@PathVariable("idIntervenant") String idIntervenant){
    	if(!intDao.findOne(this.getUserName()).getIdIntervenant().equals(idIntervenant))
    	{
    		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
    	}
        Intervenant allDemande = intDao.findOne(idIntervenant);
        return new ResponseEntity<>(intervenantToRessource(allDemande, true),HttpStatus.OK);
    }
    
    @GetMapping(value = "/{idIntervenant}/actions")
    public ResponseEntity<?> getActionsByIntervenant(@PathVariable("idIntervenant") String idIntervenant){
    	if(!intDao.findOne(this.getUserName()).getIdIntervenant().equals(idIntervenant))
    	{
    		return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
    	}
        List<ActionIntervenant> allDemande = actDao.recupActionByIntervenant(idIntervenant);
        HashMap<ActionIntervenant, Demande> actionDemande = new HashMap<ActionIntervenant, Demande>();
        for(int i = 0; i < allDemande.size(); i++)
        {
        	Action action = actionDao.findOne(allDemande.get(i).getPk().getAction().getIdAction());
        	actionDemande.put(allDemande.get(i), action.getDemande());
        }
        return new ResponseEntity<>(ActionIntervenantToRessource(actionDemande),HttpStatus.OK);
    }
    
    /***********************************************************************************************************************************
     * 
     *  														HATEOS
     *  
     ***********************************************************************************************************************************/
    
    //HATEOS pour les intervenants
    private Resource<Intervenant> intervenantToRessource(Intervenant intervenant, Boolean isCollection){
        Link selfLink = linkTo(IntervenantService.class).slash(intervenant.getIdIntervenant()).withSelfRel();
        if(isCollection){
            Link collectionLink = linkTo(methodOn(IntervenantService.class).getAllFormations()).slash("liste").withRel("collection ");
            return new Resource<>(intervenant, selfLink, collectionLink);
        }else{
            return new Resource<>(intervenant, selfLink);
        }
    }
    
    private Resources<Resource<Intervenant>> intervenantToRessource(Iterable<Intervenant> action){
        Link selfLink = linkTo(methodOn(IntervenantService.class).getAllFormations()).withSelfRel();
        List<Resource<Intervenant>> listFormations = new ArrayList();
        action.forEach(formation -> listFormations.add(intervenantToRessource((Intervenant)formation, true)));
        return new Resources<>(listFormations, selfLink);
    }
    
    //HATEOS pour les intervenants
    private Resource<ActionIntervenant> ActionIntervenantToRessource(ActionIntervenant intervenant, Demande de, Boolean isCollection){
        Link selfLink = linkTo(DemandeServiceUser.class).slash(de.getIdDemande()).slash(intervenant.getPk().getAction().getIdAction()).withSelfRel();
        if(isCollection){
            Link collectionLink = linkTo(methodOn(IntervenantService.class).getAllFormations()).slash(de.getIdDemande()).slash("actions").withRel("collection ");
            return new Resource<>(intervenant, selfLink, collectionLink);
        }else{
            return new Resource<>(intervenant, selfLink);
        }
    }
    
    private Resources<Resource<ActionIntervenant>> ActionIntervenantToRessource(HashMap<ActionIntervenant, Demande> action){
        Link selfLink = linkTo(methodOn(IntervenantService.class).getAllFormations()).withSelfRel();
        List<Resource<ActionIntervenant>> listFormations = new ArrayList();
        Iterator it = action.keySet().iterator();
        while(it.hasNext())
        {
        	ActionIntervenant act = (ActionIntervenant) it.next();
        	Demande de = (Demande) action.get(act);
        	listFormations.add((ActionIntervenantToRessource(act, de, true)));
        }
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
