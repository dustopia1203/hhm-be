package com.hhm.api.model.entity;

import com.hhm.api.support.constants.ValidateConstraint;
import com.hhm.api.support.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "user_information",
        indexes = {
                @Index(name = "user_information_user_id_idx", columnList = "user_id")
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserInformation extends AuditableEntity {
    @Id
    @Column()
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(length = ValidateConstraint.Length.NAME_MAX_LENGTH)
    private String firstName;

    @Column(length = ValidateConstraint.Length.NAME_MAX_LENGTH)
    private String lastName;

    @Column(length = ValidateConstraint.Length.NAME_MAX_LENGTH)
    private String middleName;

    @Column(length = ValidateConstraint.Length.PHONE_MAX_LENGTH)
    private String phone;

    @Column()
    private Instant dateOfBirth;

    @Column()
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = ValidateConstraint.Length.ADDRESS_MAX_LENGTH)
    private String address;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    @Version
    private Long version;
}
