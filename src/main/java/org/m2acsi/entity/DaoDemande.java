package org.m2acsi.entity;

import java.util.List;

public interface DaoDemande {

	public List<Demande> findDemandeByStatus(String status);
}
