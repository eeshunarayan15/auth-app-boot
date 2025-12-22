package com.eeshu.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "roles")
public class Role extends BaseModel {

    @Column(nullable = false, unique = true)
    private String name; // ROLE_USER, ROLE_ADMIN
}
