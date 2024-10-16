package org.study.tracker.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.tracker.Role;
import org.study.tracker.model.User;
import org.study.tracker.payload.SignInRequest;
import org.study.tracker.payload.SignUpRequest;
import org.study.tracker.responses.JwtResponse;
import org.study.tracker.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

  private final UserService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public void signUp(SignUpRequest signUpRequest) {
    var user = User.builder()
        .username(signUpRequest.getUsername())
        .password(passwordEncoder.encode(signUpRequest.getPassword()))
        .role(Role.ROLE_USER)
        .build();
    userService.create(user);
  }

  public JwtResponse signIn(SignInRequest signInRequest) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        signInRequest.getUsername(),
        signInRequest.getPassword()
    ));
    logger.info("User " + signInRequest.getUsername() + " was signed in");
    var user = userService.userDetailsService().loadUserByUsername(signInRequest.getUsername());
    var jwt = jwtService.generateToken(user);
    return new JwtResponse(jwt);
  }
}
