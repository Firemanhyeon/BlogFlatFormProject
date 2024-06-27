package org.blog.blogflatformproject.user.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.Role;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDTO;
import org.blog.blogflatformproject.user.repository.RoleRepository;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    //로그인
    public User login(User user){
        User user1 = userRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());

        return user1;
    }
    //회원가입
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
    //아이디중복체크
    public boolean findByUserName(String username){
        if(userRepository.findByUsername(username)==null){
            return false;
        }else{
            return true;
        }
    }
    //회원id로 회원정보 불러오기.
    public User findByUserId(Long id){
        return userRepository.findById(id).orElse(null);
    }

    //파일저장
    public FileDTO fileUpload(MultipartFile imageFile){
        if(!imageFile.isEmpty()){
            try{
                String uploadDir="/Users/jeonghohyeon/Desktop/blogUserImage";
                String fileName= UUID.randomUUID().toString();
                Path filePath = Paths.get(uploadDir+"/"+fileName);
                Files.copy(imageFile.getInputStream(),filePath);

                FileDTO fileDTO = new FileDTO();
                fileDTO.setImagePath(filePath.toString());
                fileDTO.setImageName(imageFile.getOriginalFilename());
                return fileDTO;
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }
}
