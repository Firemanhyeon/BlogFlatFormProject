package org.blog.blogflatformproject.user.repository;

import org.blog.blogflatformproject.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    User findByImagePath(String imagePath);
    void deleteByUsername(String username);
}
