package org.m2acsi.entity.securité;

import java.util.UUID;

import org.m2acsi.entity.demande.Demande;

public class Token {
	
	public Token()
	{
		super();
		this.hashToken = UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public Token(Demande demande) {
		super();
		this.hashToken = UUID.randomUUID().toString().replaceAll("-", "");
		this.demande = demande;
	}

	private String hashToken;
	
	private Demande demande;
	
	
	
}
