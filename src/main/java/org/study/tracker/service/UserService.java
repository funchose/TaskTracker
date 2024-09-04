package org.study.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.tracker.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  UserRepository userRepository;
}
