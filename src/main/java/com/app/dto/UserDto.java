package com.app.dto;


import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank
    @Email
    private String username;

    @Setter(AccessLevel.NONE)
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}", message = "enter a solid password.")
    @NotBlank(message = "Password is a required field.")
    private String password;

    @Setter(AccessLevel.NONE)
    @NotNull(message = "Password should match.")
    private String confirmPassword;

    @NotBlank(message = "First name is a required field.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long.")
    private String firstname;

    @NotBlank(message = "Last name is a required field.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters long.")
    private String lastname;

    @NotBlank(message = "Phone is a required field.")
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125  +1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"                                  // +111 123 456 789
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Phone Number is required field and may be in any valid phone number format.")
    private String phone;

    @NotNull(message = "Please select a role.")
    private RoleDto role;

    @NotNull(message = "Please select a company.")
    private CompanyDto company;

    private boolean isOnlyAdmin;

    public void setPassword(String password) {
        this.password = password;
        checkConfirmPassword();
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkConfirmPassword();
    }

    private void checkConfirmPassword() {
        if(this.password != null && !password.equals(confirmPassword)) this.confirmPassword = null;
    }


}
