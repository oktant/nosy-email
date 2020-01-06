package tech.nosy.nosyemail.nosyemail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;

import javax.transaction.Transactional;
import java.util.Set;

@Transactional
@Repository
@CrossOrigin
public interface InputSystemRepository extends JpaRepository<InputSystem, String> {

  InputSystem findInputSystemByEmailAndInputSystemName(String email, String input_system_name);
  void deleteInputSystemByEmailAndInputSystemName(String email, String name);
  Set<InputSystem> getInputSystemByEmail(String email);
}
