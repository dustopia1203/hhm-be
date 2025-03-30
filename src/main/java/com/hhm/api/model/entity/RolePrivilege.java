package com.hhm.api.model.entity;

import com.hhm.api.support.enums.Permission;
import com.hhm.api.support.enums.ResourceCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(
        name = "role_privilege",
        indexes = {
                @Index(name = "role_privilege_role_id_idx", columnList = "role_id"),
                @Index(name = "role_privilege_deleted_idx", columnList = "deleted"),
        }
)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RolePrivilege extends AuditableEntity {
    @Id
    @Column()
    private UUID id;

    @Column(nullable = false)
    private UUID roleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceCode resourceCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Permission permission;

    @Column(nullable = false)
    private Boolean deleted;
}
