package com.dawan.cinema.controllers.web.forms;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Formulaire de changement de son propre mot de passe.
 */
@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeForm {

    @NotBlank(message = "Le mot de passe actuel est obligatoire")
    private String currentPassword;

    @NotBlank(message = "Le nouveau mot de passe est obligatoire")
    @Size(min = 8, message = "8 caractères minimum")
    private String newPassword;

    private String confirmPassword;

    @AssertTrue(message = "Les deux mots de passe ne correspondent pas")
    public boolean isConfirmationValide() {
        return newPassword == null || newPassword.equals(confirmPassword);
    }
}