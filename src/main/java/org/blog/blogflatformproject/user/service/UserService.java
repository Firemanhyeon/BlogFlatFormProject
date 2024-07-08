package org.blog.blogflatformproject.user.service;

import lombok.RequiredArgsConstructor;
import org.blog.blogflatformproject.user.domain.Role;
import org.blog.blogflatformproject.user.domain.User;
import org.blog.blogflatformproject.user.dto.FileDto;
import org.blog.blogflatformproject.user.repository.RoleRepository;
import org.blog.blogflatformproject.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final PasswordEncoder passwordEncoder;

    //로그인
    public User login(User user){
        System.out.println("서비스호출");
        User foundUser = userRepository.findByUsername(user.getUsername());
        if(foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())){
            return foundUser;
        } else {
            return null;
        }
    }
    //회원가입
    public User regUser(User user) {
        Role userRole = roleRepository.findByRoleName("USER");
        // 사용자에게 역할 할당
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setRegistrationDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    //아이디중복체크
    public boolean checkDuplicated(String username){
        if(userRepository.findByUsername(username)==null){
            return false;
        }else{
            return true;
        }
    }
    //회원이름으로 회원정보 불러오기.
    public User findByUserName(String username){
        return userRepository.findByUsername(username);
    }
    //회원아이디로 회원정보불러오기
    public User findByUserId(Long userId){
        return userRepository.findById(userId).orElse(null);
    }
    //
    //파일 삭제
    public boolean fileDelete(String imagePath){
        Path filePath = Paths.get(imagePath);
        try{
           return Files.deleteIfExists(filePath);
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
    //파일저장
    @Transactional
    public FileDto fileUpload(MultipartFile imageFile){
        if(!imageFile.isEmpty()){
            try{
                String uploadDir="/Users/jeonghohyeon/Desktop/blogUserImage";
                String fileName= UUID.randomUUID().toString();
                Path filePath = Paths.get(uploadDir+"/"+fileName);
                Files.copy(imageFile.getInputStream(),filePath);

                FileDto fileDTO = new FileDto();
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
    //회원 이름 수정
    @Transactional
    public User updateName(String name , String username){
        User user = findByUserName(username);
        if(user!=null){
            user.setName(name);
            return userRepository.save(user);
        }
        return null;
    }
    //imagePath 로 회원정보 불러오기
    public User findByImgPath(String imgPath){
        return userRepository.findByImagePath(imgPath);
    }
    //회원정보저장,수정
    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }
    //회원탈퇴
    @Transactional
    public void deleteUser(String username){
        userRepository.deleteByUsername(username);
    }
}
