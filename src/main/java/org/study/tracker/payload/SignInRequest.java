package org.study.tracker.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication request")
public class SignInRequest {
  private String username;
  private String password;
}
