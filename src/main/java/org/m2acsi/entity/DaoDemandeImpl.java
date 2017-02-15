package org.m2acsi.entity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DaoDemandeImpl implements DaoDemande{

	@PersistenceContext
	private EntityManager manager;
	
	
	@Override
	public List<Demande> findDemandeByStatus(String status) {
		List<Demande> listeDemandeStatus = manager.createNamedQuery("Demande.findByStatus", Demande.class).getResultList();
		return listeDemandeStatus;
	}

}
