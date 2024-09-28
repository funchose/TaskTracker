package org.study.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.study.tracker.payload.SignInRequest;
import org.study.tracker.payload.SignUpRequest;
import org.study.tracker.responses.JwtResponse;
import org.study.tracker.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
  private final AuthenticationService authenticationService;

  @PostMapping("/auth/sign-up")
  @Operation(summary = "Registers a new user",
      responses = @ApiResponse(responseCode = "200", description = "User is registered"))
  public void signUp(@RequestBody @Valid SignUpRequest request) {
    authenticationService.signUp(request);
  }

  @PostMapping("/auth/sign-in")
  @Operation(summary = "Authenticates and authorizes an existing user",
      responses = @ApiResponse(responseCode = "200", description = "User is signed in"))
  public JwtResponse signIn(@RequestBody @Valid SignInRequest request) {
    return authenticationService.signIn(request);
  }

}
