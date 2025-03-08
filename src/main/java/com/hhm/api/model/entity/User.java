package com.hhm.api.model.entity;

import com.hhm.api.support.constants.ValidateConstraint;
import com.hhm.api.support.enums.AccountType;
import com.hhm.api.support.enums.ActiveStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditableEntity {
    @Id
    @Column
    private UUID id;

    @Column(length = ValidateConstraint.LENGTH.USERNAME_MAX_LENGTH, unique = true, nullable = false)
    private String username;

    @Column(length = ValidateConstraint.LENGTH.EMAIL_MAX_LENGTH, unique = true,  nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus status;

    @Column()
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    @Version
    private Long version;
}
