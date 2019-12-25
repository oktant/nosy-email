package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemHasChildrenException;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemNameIsMandatoryException;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemNotFoundException;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;

import java.util.Set;

@Service
public class InputSystemService {

  private InputSystemRepository inputSystemRepository;

  @Autowired
  public InputSystemService(
      InputSystemRepository inputSystemRepository) {
    this.inputSystemRepository = inputSystemRepository;
  }

  public Set<InputSystem> getListOfInputSystems(String username) {
    return inputSystemRepository.getInputSystemByEmail(username);
  }

  public void deleteInputSystem(String inputSystemId, String email) {
    InputSystem checkInputSystem = inputSystemRepository.findByInputSystemIdAndEmail(email, inputSystemId);
    if (checkInputSystem == null) {
      throw new InputSystemNotFoundException();
    }
    if (checkInputSystem.getEmailTemplate()!=null && !checkInputSystem.getEmailTemplate().isEmpty()) {
      throw new InputSystemHasChildrenException();
    }
    inputSystemRepository.deleteById(inputSystemId);
  }

  public InputSystem saveInputSystem(InputSystem inputSystem, String email) {
    if (inputSystem.getInputSystemName() == null
        || inputSystem.getInputSystemName().trim().equals("")) {
      throw new InputSystemNameIsMandatoryException();
    }
    inputSystem.setEmail(email);
    return inputSystemRepository.save(inputSystem);
  }
}
