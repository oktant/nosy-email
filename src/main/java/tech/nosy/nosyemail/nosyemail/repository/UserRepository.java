package tech.nosy.nosyemail.nosyemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.nosy.nosyemail.nosyemail.model.User;

public interface UserRepository extends JpaRepository<User, String> {}
