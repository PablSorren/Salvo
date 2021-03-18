package com.codeoftheweb.salvo;

import org.hibernate.secure.spi.JaccPermissionDeclarations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface GameRepository extends JpaRepository<Game, Long> {

}
