package tech.nosy.nosyemail.nosyemail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.nosy.nosyemail.nosyemail.exceptions.PasswordIsNotValidException;
import tech.nosy.nosyemail.nosyemail.model.User;
import tech.nosy.nosyemail.nosyemail.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
  private UserRepository userRepository;
  private KeycloakService keycloakService;

  @Autowired
  public UserService(UserRepository userRepository, KeycloakService keycloakClient) {
    this.userRepository = userRepository;
    this.keycloakService = keycloakClient;
  }

  public User getUserInfo(HttpServletRequest request) {

    return keycloakService.getUserInfo(request.getUserPrincipal().getName());
  }

  public void deleteUser(HttpServletRequest request) {


    String obtainedUser = request.getUserPrincipal().getName();

    keycloakService.deleteUsername(obtainedUser);
    userRepository.deleteById(obtainedUser);
  }

  public void logoutUser(HttpServletRequest request) {

    keycloakService.logoutUser(request.getUserPrincipal().getName());
  }

  public User addUser(User user) {
    if (!isValidPassword(user.getPassword())) {
      throw new PasswordIsNotValidException();
    }

    keycloakService.registerNewUser(user);
    return userRepository.saveAndFlush(user);
  }

  private boolean isValidPassword(String password) {
    return null != password && password.length() > 5;
  }
}
