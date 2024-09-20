package org.study.tracker.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.study.tracker.payload.SignInRequest;
import org.study.tracker.payload.SignUpRequest;
import org.study.tracker.responses.JwtResponse;
import org.study.tracker.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
  private final AuthenticationService authenticationService;

  @PostMapping("/sign-up")
  public JwtResponse signUp(@RequestBody @Valid SignUpRequest request) {
    return authenticationService.signUp(request);
  }

  @PostMapping("/sign-in")
  public JwtResponse signIn(@RequestBody @Valid SignInRequest request) {
    return authenticationService.signIn(request);
  }

}
