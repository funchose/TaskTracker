package org.study.tracker.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RestController;
import org.study.tracker.security.jwt.JwtService;
import org.study.tracker.service.UserService;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class UserController {

  AuthenticationManager authenticationManager;

  private UserService userService;

  JwtService jwtService;
//  @GetMapping("/users/{id}")
//  public Response<List<User>> getAll() {
//    List<User> userList = userService.getAllUsers();
//    Response<List<User>> response = new Response<>();
//    response.setPayload(userList);
//    return response;
//  }
}
