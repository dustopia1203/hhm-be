package com.hhm.api.model.entity;

import com.hhm.api.support.constants.ValidateConstraint;
import com.hhm.api.support.enums.ActiveStatus;
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

import java.util.UUID;

@Entity
@Table(
        name = "role",
        indexes = {
                @Index(name = "role_deleted_idx", columnList = "deleted"),
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AuditableEntity {
    @Id
    @Column()
    private UUID id;

    @Column(length = ValidateConstraint.Length.CODE_MAX_LENGTH, unique = true,  nullable = false)
    private String code;

    @Column(length = ValidateConstraint.Length.NAME_MAX_LENGTH, nullable = false)
    private String name;

    @Column()
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus status;

    @Column(nullable = false)
    private Boolean deleted;

    @Column(nullable = false)
    @Version
    private Long version;
}
