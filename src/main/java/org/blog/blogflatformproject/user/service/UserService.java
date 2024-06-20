package org.blog.blogflatformproject.user.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.Role;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.repository.RoleRepository;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public User login(User user){
        User user1 = userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());

        return user1;
    }

    public User regUser(User user) {
        Role userRole = roleRepository.findByRoleName("일반사용자");
        // 사용자에게 역할 할당
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setRegistrationDate(LocalDate.now());
        System.out.println(user.getRegistrationDate());
        User user1 = userRepository.save(user);
        return user1;
    }

    public boolean findByUserName(String username){
        if(userRepository.findByUsername(username)==null){
            return false;
        }else{
            return true;
        }
    }
}
