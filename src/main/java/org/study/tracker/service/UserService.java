package org.study.tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.study.tracker.model.User;
import org.study.tracker.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  //public List<User> getAllUsers() {
//    return userRepository.findAll();
//  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public User create(User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new RuntimeException("This user already exists");
    }
    return save(user);
    //return userRepository.save(new User(555L, request.getUsername(), request.getPassword()));
  }

  public User getByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
  }

  public UserDetailsService userDetailsService() {
    return this::getByUsername;
  }

  public User getCurrentUser() {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    return getByUsername(username);
  }

//  public void getAdmin() {
//    var user = getCurrentUser();
//    user.setRoles(user.getRoles().add(RoleEnum.ROLE_ADMIN));
//  }

}
