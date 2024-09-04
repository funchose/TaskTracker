package org.study.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.study.tracker.service.UserService;

@RestController
public class UserController {
  @Autowired
  private UserService userService;
}
