package org.study.tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.tracker.model.User;
import org.study.tracker.payload.SignInRequest;
import org.study.tracker.payload.SignUpRequest;
import org.study.tracker.responses.JwtResponse;
import org.study.tracker.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserService userService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public JwtResponse signUp(SignUpRequest signUpRequest) {
    var user = User.builder()
        .username(signUpRequest.getUsername())
        .password(passwordEncoder.encode(signUpRequest.getPassword()))
        .build();
//    Set<Role> roles = new HashSet<>();
//    Role userRole = new Role("user");
//        .orElseThrow(() -> new RuntimeException("Role not found"));
//    roles.add(userRole);
//    user.setRole(userRole);
    userService.create(user);
    var jwt = jwtService.generateToken(userService.userDetailsService().loadUserByUsername(signUpRequest.getUsername()));
    return new JwtResponse(jwt);
  }

  public JwtResponse signIn(SignInRequest signInRequest) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        signInRequest.getUsername(),
        signInRequest.getPassword()
    ));
    var user = userService.userDetailsService().loadUserByUsername(signInRequest.getUsername());
    var jwt = jwtService.generateToken(user);
    return new JwtResponse(jwt);
  }
}
