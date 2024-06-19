package org.blog.blogflatformproject.user.repository;

import org.blog.blogflatformproject.user.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleName(String name);
}
