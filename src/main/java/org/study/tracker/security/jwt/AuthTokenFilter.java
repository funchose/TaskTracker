package org.study.tracker.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.study.tracker.service.UserService;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
  public static final String BEARER_PREFIX = "Bearer ";
  public static final String HEADER_NAME = "Authorization";

  private final JwtService jwtService;
  private final UserService userService;
  @Autowired
  @Getter
  @Setter
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain) throws ServletException,
      IOException {
    var authHeader = request.getHeader(HEADER_NAME);
    if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader,
        BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }
    var jwt = authHeader.substring(BEARER_PREFIX.length());
    var username = jwtService.extractUserName(jwt);
    if ((StringUtils.isNotEmpty(username)) && SecurityContextHolder.getContext()
        .getAuthentication() == null) {
      UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
      if (jwtService.isTokenValid(jwt, userDetails)) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
      }
    }
    filterChain.doFilter(request, response);
  }
}
