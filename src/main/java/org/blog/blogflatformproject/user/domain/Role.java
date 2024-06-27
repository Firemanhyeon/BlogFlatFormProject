package org.blog.blogflatformproject.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Table(name = "role")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column(name = "roles_id")
    private Long roleId;

    @Column(name = "roles_name")
    private String roleName;

}
