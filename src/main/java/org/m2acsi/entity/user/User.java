package org.m2acsi.entity.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
	
	
	public User() {
		super();
	}

	public User(String login, String mdp) {
		super();
		this.login = login;
		this.mdp = mdp;
	}

	@Id
	private String login;
	
	private String mdp;

	public String getLogin() {
		return login;
	}

	public String getMdp() {
		return mdp;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	
	
	
}
