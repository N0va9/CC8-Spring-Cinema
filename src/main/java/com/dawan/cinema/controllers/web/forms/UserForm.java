package com.dawan.cinema.controllers.web.forms;

import com.dawan.cinema.dtos.UserDto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet du formulaire d'administration d'un compte.
 * Mot de passe obligatoire à la création, facultatif en modification (vide = inchangé).
 */
@Getter
@Setter
@NoArgsConstructor
public class UserForm implements AdminForm<UserDto> {

    private static final int MIN_PASSWORD_LENGTH = 8;

    private Long id;

    @NotBlank(message = "L'identifiant est obligatoire")
    @Size(min = 3, max = 50, message = "L'identifiant doit faire entre 3 et 50 caractères")
    private String username;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @NotBlank(message = "L'e-mail est obligatoire")
    @Email(message = "L'e-mail n'est pas valide")
    private String email;

    private String password;

    private String confirmPassword;

    /** Obligatoire à la création ; si renseigné, au moins 8 caractères. */
    @AssertTrue(message = "Mot de passe obligatoire à la création (8 caractères minimum)")
    public boolean isPasswordValide() {
        if (password == null || password.isEmpty()) {
            return id != null;
        }
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    /** La confirmation doit être identique au mot de passe. */
    @AssertTrue(message = "Les deux mots de passe ne correspondent pas")
    public boolean isConfirmationValide() {
        return password == null || password.isEmpty() || password.equals(confirmPassword);
    }

    /** Préremplit le formulaire (jamais le mot de passe). */
    public static UserForm from(UserDto dto) {
        UserForm form = new UserForm();
        form.setId(dto.id());
        form.setUsername(dto.username());
        form.setFirstName(dto.firstName());
        form.setLastName(dto.lastName());
        form.setEmail(dto.email());
        return form;
    }

    @Override
    public UserDto toDto() {
        return new UserDto(id, username, firstName, lastName, email, null, password);
    }
}