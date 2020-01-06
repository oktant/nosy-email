package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemHasChildrenException;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemNameIsMandatoryException;
import tech.nosy.nosyemail.nosyemail.exceptions.InputSystemNotFoundException;
import tech.nosy.nosyemail.nosyemail.model.InputSystem;
import tech.nosy.nosyemail.nosyemail.repository.InputSystemRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class InputSystemService {

  private InputSystemRepository inputSystemRepository;

  @Autowired
  public InputSystemService(
      InputSystemRepository inputSystemRepository) {
    this.inputSystemRepository = inputSystemRepository;
  }

  public Set<InputSystem> getListOfInputSystems(String name, String username) {
    if(name!=null && !name.isEmpty()){
      Set<InputSystem> inputSystems=new HashSet<>();
      inputSystems.add(inputSystemRepository.findInputSystemByEmailAndInputSystemName(username, name));
      return inputSystems;
    }
    return inputSystemRepository.getInputSystemByEmail(username);
  }

  public void deleteInputSystem(String inputSystemName, String email) {
    InputSystem checkInputSystem = inputSystemRepository.findInputSystemByEmailAndInputSystemName(email, inputSystemName);
    if (checkInputSystem == null) {
      throw new InputSystemNotFoundException();
    }
    if (checkInputSystem.getEmailTemplate()!=null && !checkInputSystem.getEmailTemplate().isEmpty()) {
      throw new InputSystemHasChildrenException();
    }
    inputSystemRepository.deleteInputSystemByEmailAndInputSystemName(email, inputSystemName);
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
