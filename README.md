<div align=center><h1>📚 STACKS</h1></div>

<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <br>
  
  <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> 
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> 
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> 
  <img src="https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white">
  <br>
  

  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <br>

  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
  <br>

  <img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white">
  <br>
  
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <br>
</div>
## 데이터베이스 구조
```sql
CREATE TABLE `user` (
                        `user_id` BIGINT NOT NULL AUTO_INCREMENT,
                        `username` VARCHAR(50) NOT NULL UNIQUE,
                        `password` VARCHAR(100) NOT NULL,
                        `name` VARCHAR(100) NOT NULL,
                        `email` VARCHAR(100) NOT NULL,
                        `email_status` TINYINT NOT NULL DEFAULT 0,
                        `registration_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                        `image_name` VARCHAR(100) NULL,
                        `image_path` VARCHAR(100) NULL,
                        PRIMARY KEY (`user_id`)
);

CREATE TABLE `role` (
                        `roles_id` BIGINT NOT NULL AUTO_INCREMENT,
                        `roles_name` VARCHAR(50) NOT NULL UNIQUE,
                        PRIMARY KEY (`roles_id`)
);

CREATE TABLE `user_role` (
                             `roles_id` BIGINT NOT NULL,
                             `user_id` BIGINT NOT NULL,
                             PRIMARY KEY (`roles_id`, `user_id`),
                             FOREIGN KEY (`roles_id`) REFERENCES `role` (`roles_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                             FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `blog` (
                        `blog_id` BIGINT NOT NULL AUTO_INCREMENT,
                        `user_id` BIGINT NULL,
                        `blog_name` VARCHAR(50) NOT NULL,
                        `blog_description` VARCHAR(100) NULL,
                        `blog_registration_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`blog_id`),
                        FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `board` (
                         `board_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `blog_id` BIGINT NOT NULL,
                         `category_id` BIGINT NOT NULL,
                         `board_title` VARCHAR(50) NOT NULL,
                         `board_content` TEXT NOT NULL,
                         `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                         `temporary_YN` TINYINT NOT NULL DEFAULT 1,
                         `open_YN` TINYINT NOT NULL DEFAULT 1,
                         `visit_count` INT NOT NULL DEFAULT 0,
                         `series_id` BIGINT NULL,
                         `first_image_path` VARCHAR(100) NULL,
                         PRIMARY KEY (`board_id`),
                         FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (`series_id`) REFERENCES `series` (`series_id`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `likes` (
                         `like_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `board_id` BIGINT NOT NULL,
                         `user_id` BIGINT NULL,
                         PRIMARY KEY (`like_id`),
                         UNIQUE KEY (`board_id`, `user_id`),
                         FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `category` (
                        `category_id` BIGINT NOT NULL AUTO_INCREMENT,
                        `category_name` VARCHAR(50) NOT NULL,
                        `pre_category` BIGINT NULL,
                        PRIMARY KEY (`category_id`),
                        FOREIGN KEY (`pre_category`) REFERENCES `category` (`category_id`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `follow` (
                          `follow_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `follower_user_id` BIGINT NULL,
                          `following_user_id` BIGINT NULL,
                          PRIMARY KEY (`follow_id`),
                          UNIQUE KEY (`follower_user_id`, `following_user_id`),
                          FOREIGN KEY (`follower_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                          FOREIGN KEY (`following_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `reply` (
                         `reply_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `board_id` BIGINT NOT NULL,
                         `reply_content` VARCHAR(100) NOT NULL,
                         `reply_created` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                         `pre_reply_id` BIGINT NULL,
                         PRIMARY KEY (`reply_id`),
                         FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (`pre_reply_id`) REFERENCES `reply` (`reply_id`) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE `image` (
                         `image_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `board_id` BIGINT NOT NULL,
                         `image_original_name` VARCHAR(255) NOT NULL,
                         `image_name` VARCHAR(255) NOT NULL,
                         `image_path` VARCHAR(100) NOT NULL,
                         PRIMARY KEY (`image_id`),
                         FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `series` (
                          `series_id` BIGINT NOT NULL AUTO_INCREMENT,
                          `blog_id` BIGINT NOT NULL,
                          `series_title` VARCHAR(255) NOT NULL,
                          PRIMARY KEY (`series_id`),
                          FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `tag` (
                       `tag_id` BIGINT NOT NULL AUTO_INCREMENT,
                       `tag_name` VARCHAR(30) NOT NULL,
                       PRIMARY KEY (`tag_id`)
);

CREATE TABLE `tag_board` (
                             `tag_id` BIGINT NOT NULL,
                             `board_id` BIGINT NOT NULL,
                             PRIMARY KEY (`tag_id`, `board_id`),
                             FOREIGN KEY (`tag_id`) REFERENCES `tag` (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                             FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `views` (
                         `view_id` BIGINT NOT NULL AUTO_INCREMENT,
                         `user_id` BIGINT NULL,
                         `board_id` BIGINT NOT NULL,
                         `view_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (`view_id`),
                         UNIQUE KEY (`user_id`, `board_id`),
                         FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE,
                         FOREIGN KEY (`board_id`) REFERENCES `board` (`board_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE refresh_token (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               value VARCHAR(255) NOT NULL
);
```

# 개인 미션형 프로젝트
# LikeLion_Blog
[Techit] 백엔드 스쿨 10기: velog 클론 프로젝트

## 기능 목록

### 1. 회원가입
- [x] 회원 가입 폼
- [x] 같은 ID, Email Check API
- [x] 회원 등록 기능
- [x] 회원 가입 후 로그인 폼으로 이동

### 2. 로그인
- [x] 로그인 폼
- [x] 로그인 기능
    - [x] 로그인 성공 후 `/`로 이동
    - [x] 로그인 실패 후 다시 로그인 폼으로 이동 (오류 메시지 출력)
- [x] Spring Security 를 이용한 로그인 구현
    - [x] Form Login
    - [x] JWT Login
    - [ ] OAuth2 로그인

### 3. 사이트 상단
- [x] 사이트 로고가 좌측 상단에 보여짐
- [x] 로그인 여부에 따른 우측 정보 표시
    - [x] 로그인하지 않았을 경우 로그인 링크
    - [x] 로그인했을 경우 사용자 이름
        - [x] 사용자 이름 클릭 시 설정, 해당 사용자 블로그 이동 링크, 임시 저장글 목록 보기, 로그아웃 링크 표시

### 4. 로그아웃
- [x] 로그아웃 기능

### 5. 메인 페이지 (/)
- [x] 블로그 글 목록 보기
    - [x] 최신 순
    - [x] 조회수 높은 순
    - [x] 좋아요 순
- [ ] 페이징 처리 또는 무한 스크롤 구현
- [ ] 검색 기능
    - [ ] 제목
    - [ ] 내용
    - [ ] 사용자 이름

### 6. 블로그 글 쓰기
- [x] 블로그 제목, 내용, 사진 입력 기능
- [x] "출간하기" 버튼
    - [x] 블로그 썸네일(이미지)
    - [x] 공개 유무
    - [x] 시리즈 설정
    - [x] "출간하기" 클릭 시 글 등록
- [x] "임시저장" 버튼

### 7. 임시 글 저장 목록 보기
- [x] 로그인 시 임시글 저장 목록 보기 링크 표시
- [x] 임시글 저장 목록 표시
    - [x] 글 제목 클릭 시 글 수정 가능
    - [x] "임시저장" 및 "출간하기" 기능

### 8. 특정 사용자 블로그 글 보기 (/@사용자아이디)
- [x] 사용자 정보 보기
- [x] 사용자 글 목록 보기
    - [x] 최신 순
    - [x] 조회수 높은 순
    - [x] 좋아요 높은 순
- [x] 사용자 태그 목록 보기

### 9. 시리즈 목록 보기
- [x] 시리즈 목록 보기 기능
- [x] 시리즈 제목 클릭 시 시리즈에 포함된 블로그 글 목록 보기

### 10. 블로그 글 상세 보기
- [x] 메인 페이지에서 제목 클릭 시 블로그 글 상세 보기
- [x] 특정 사용자 블로그에서 제목 클릭 시 블로그 글 상세 보기
- [x] 시리즈에 속한 블로그 글 목록에서 제목 클릭 시 블로그 글 상세 보기

### 11. 사용자 정보 보기
- [x] 로그인 사용자 이름 클릭 시 사용자 정보 보기
    - [x] 사용자 이름
    - [x] 이메일
    - [x] 회원 탈퇴 링크

### 12. 회원 탈퇴
- [x] 회원 탈퇴 확인 폼
- [x] 폼에서 확인 시 회원 탈퇴 (회원 정보 삭제)

### 13. 댓글 목록 보기
- [x] 블로그 글 상세 보기에서 댓글 목록 표시
- [x] 댓글과 대댓글 최신 순 표시
- [x] 댓글 최대 20개 페이징 처리

### 14. 댓글 달기
- [x] 블로그에 댓글 달기
- [x] 댓글에 대댓글 달기

### 15. 블로그 글에 좋아요 하기
- [x] 블로그 글에 좋아요 기능

### 16. 블로그 팔로우 하기
- [x] 블로그에 팔로우 기능

### 17. 이외의 기능
- [ ] 벨로그 사이트 분석 (URL 및 파라미터 등)
- [x] 벨로그와 유사하거나 더 편리한 기능 구현
- [ ] 프론트 개발 학습 및 React.js 등 프론트 개발 별도 구현 제안
- [x] 벨로그의 특별한 기능 추가 구현
