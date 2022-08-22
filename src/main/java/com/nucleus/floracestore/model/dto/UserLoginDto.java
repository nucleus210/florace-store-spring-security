package com.nucleus.floracestore.model.dto;

import com.nucleus.floracestore.model.validators.IUniqueUserName;
import com.nucleus.floracestore.model.validators.IValidEmail;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class UserLoginDto {
    @NotBlank
    @Size(min = 4, max = 20)
    @IUniqueUserName
    private String username;
    @IValidEmail
    @NotNull
    private String emailAddress;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
}
