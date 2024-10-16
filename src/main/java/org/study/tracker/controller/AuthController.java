package org.study.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.study.tracker.payload.SignInRequest;
import org.study.tracker.payload.SignUpRequest;
import org.study.tracker.responses.JwtResponse;
import org.study.tracker.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
  private final AuthenticationService authenticationService;

  @Transactional
  @PostMapping("/auth/sign-up")
  @Operation(summary = "Registers a new user",
      responses = @ApiResponse(responseCode = "200", description = "User is registered"))
  public void signUp(@RequestBody @Valid SignUpRequest request) {
    authenticationService.signUp(request);
    logger.info("User " + request.getUsername() + " was registered");
  }

  @Transactional
  @PostMapping("/auth/sign-in")
  @Operation(summary = "Authenticates and authorizes an existing user",
      responses = @ApiResponse(responseCode = "200", description = "User is signed in"))
  public JwtResponse signIn(@RequestBody @Valid SignInRequest request) {
    return authenticationService.signIn(request);
  }
}
