package org.m2acsi.boundary.user;

import org.m2acsi.entity.demande.Action;
import org.m2acsi.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRessource extends JpaRepository<User, String>{

}
