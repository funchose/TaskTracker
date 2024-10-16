package org.study.tracker.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.study.tracker.Role;

@Data
@Schema(description = "Edit user role request")
public class EditUserRoleRequest {
  @NotNull
  Role role;
}
