package tech.nosy.nosyemail.nosyemail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.nosy.nosyemail.nosyemail.dto.TokenCollectionDto;
import tech.nosy.nosyemail.nosyemail.dto.UserDto;
import tech.nosy.nosyemail.nosyemail.service.KeycloakService;
import tech.nosy.nosyemail.nosyemail.service.UserService;
import tech.nosy.nosyemail.nosyemail.utils.TokenCollectionMapper;
import tech.nosy.nosyemail.nosyemail.utils.UserMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {

  private UserService userService;
  private KeycloakService keycloakService;

  @Autowired
  public AuthController(
      UserService userService,  KeycloakService keycloakService) {
    this.userService = userService;
    this.keycloakService = keycloakService;
  }

  @GetMapping(path = "/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    userService.logoutUser(request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping(path = "/status")
  public ResponseEntity<Boolean> isAuthenticated(@RequestBody TokenCollectionDto token)  {
    return new ResponseEntity<>(keycloakService.isAuthenticated(token.getAccessToken()), HttpStatus.OK);
  }

  @PostMapping(value = "/token")
  public ResponseEntity<TokenCollectionDto> getToken(@RequestBody @Valid UserDto userdto)
      throws IOException {
    return new ResponseEntity<>(
            TokenCollectionMapper.INSTANCE.
                    toTokenCollectionDto(keycloakService.getTokens(UserMapper.INSTANCE.toUser(userdto))), HttpStatus.OK);
  }

  @PostMapping(value = "/users")
  public ResponseEntity<UserDto> newUser(@RequestBody @Valid UserDto userdto) {
    return new ResponseEntity<>(
        UserMapper.INSTANCE.toUserDto(userService.addUser(UserMapper.INSTANCE.toUser(userdto))), HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/users")
  public ResponseEntity<String> deleteUsername(HttpServletRequest request) {
    userService.deleteUser(request);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping(value = "/users")
  public ResponseEntity<UserDto> getUserProfile(HttpServletRequest request) {
    return new ResponseEntity<>(UserMapper.INSTANCE.toUserDto(userService.getUserInfo(request)), HttpStatus.OK);
  }
}
