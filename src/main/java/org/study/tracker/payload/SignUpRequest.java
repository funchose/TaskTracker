package org.study.tracker.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Registration request")
public class SignUpRequest {
  @Schema(description = "Username", example = "Bob")
  @Size(min = 5, max = 50,
      message = "Username shall contain from 5 to 50 symbols")
  @NotBlank(message = "Username cannot be blank")
  private String username;

  @Schema(description = "Password", example = "password1!2@")
  @Size(min = 7, max = 255,
      message = "Password length shall not be less than 7 and more than 255 symbols")
  @NotBlank(message = "Password cannot be blank")
  private String password;
}
