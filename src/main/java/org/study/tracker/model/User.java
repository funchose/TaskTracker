package org.study.tracker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
  @Id
  //@NotNull
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Users_id_seq")
  //@SequenceGenerator(name = "Users_id_seq", sequenceName = "Users_id_seq", allocationSize = 1)
  private Long id;

  @NotNull
  @Column(name = "username")
  private String username;
  @Column(name = "password")
  private String password;
  //@Enumerated(EnumType.STRING)
  //@OneToMany(fetch = FetchType.LAZY)
  //@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_name"))
  //private Set<Role> roles = new HashSet<>()
  //@NotNull
  //@Column(name = "role")
  //private Role role;

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    //return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    return List.of(
        //new SimpleGrantedAuthority(role.getName())
    );
  }

//  public static UserDetails build(User user) {
//    List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities().stream().toList();
//    return new UserDetails(user.getId(),
//        user.getUsername(), user.getPassword(), authorities);
//  }
}
