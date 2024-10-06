package org.study.tracker.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Authentication request")
public class SignInRequest {
  @NotBlank(message = "Username cannot be blank")
  private String username;
  @NotBlank(message = "Password cannot be blank")
  private String password;
}
