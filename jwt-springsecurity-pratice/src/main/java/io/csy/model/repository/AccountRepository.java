package io.csy.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.csy.model.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
	
    boolean existsByAccountEmail(String accountEmail);

    Optional<AccountEntity> findAllByAccountEmail(String accountEmail);
	
}
