package br.com.tmvolpato.ms.domain.repository;

import br.com.tmvolpato.ms.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

    boolean existsUserByEmail(String email);

}
