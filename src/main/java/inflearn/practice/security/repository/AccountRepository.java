package inflearn.practice.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import inflearn.practice.security.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
	Optional<AccountEntity> findByName(final String name);
}
