package com.mp.fintech.entity;

import com.mp.fintech.constants.ApplicationConstants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    @NotNull(message = ApplicationConstants.NAME_CANNOT_NULL)
    private String name;

    @NotEmpty
    @NotNull(message = ApplicationConstants.PWD_CANNOT_NULL)
    private String password;

    @NotEmpty
    @NotNull(message = ApplicationConstants.EMAIL_CANNOT_NULL)
    private String email;

    @NotEmpty
    @NotNull(message = ApplicationConstants.ROLE_CANNOT_NULL)
    private String roles;

}
