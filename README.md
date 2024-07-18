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

# 데이터베이스 구조

```sql
create table blog.blog
(
    blog_id                bigint auto_increment
        primary key,
    user_id                bigint       null,
    blog_name              varchar(255) null,
    blog_description       varchar(255) null,
    blog_registration_date date         null,
    constraint fk_blog_user
        foreign key (user_id) references blog.user (user_id)
            on update cascade on delete cascade
);



create table blog.board
(
    board_id         bigint auto_increment
        primary key,
    blog_id          bigint         not null,
    category_id      bigint         not null,
    board_title      varchar(255)   null,
    board_content    varchar(10000) null,
    created_at       date           null,
    temporary_yn     bit            null,
    open_yn          bit            null,
    visit_count      int default 0  not null,
    series_id        bigint         null,
    first_image_path varchar(255)   null,
    constraint board_ibfk_1
        foreign key (blog_id) references blog.blog (blog_id)
            on update cascade on delete cascade,
    constraint board_ibfk_2
        foreign key (category_id) references blog.category (category_id)
            on update cascade on delete cascade,
    constraint board_ibfk_3
        foreign key (series_id) references blog.series (series_id)
            on update cascade on delete set null
);

create index blog_id
    on blog.board (blog_id);

create index category_id
    on blog.board (category_id);

create index series_id
    on blog.board (series_id);


create table category
(
    category_id   bigint auto_increment
        primary key,
    category_name varchar(255) null,
    pre_category  bigint       null,
    constraint category_ibfk_1
        foreign key (pre_category) references blog.category (category_id)
            on update cascade on delete set null
);
create table blog.follow
(
    follow_id    bigint auto_increment
        primary key,
    follower_id  bigint null,
    following_id bigint null,
    constraint chk_follows_unique_users
        unique (follower_id, following_id),
    constraint FKmow2qk674plvwyb4wqln37svv
        foreign key (follower_id) references blog.user (user_id),
    constraint FKqme6uru2g9wx9iysttk542esm
        foreign key (following_id) references blog.user (user_id)
);

create table blog.likes
(
    like_id  bigint auto_increment
        primary key,
    board_id bigint not null,
    user_id  bigint null,
    constraint board_id
        unique (board_id, user_id),
    constraint likes_ibfk_1
        foreign key (board_id) references blog.board (board_id)
            on update cascade on delete cascade,
    constraint likes_ibfk_2
        foreign key (user_id) references blog.user (user_id)
            on update cascade on delete set null
);

create index user_id
    on blog.likes (user_id);

create table blog.refresh_token
(
    id      bigint auto_increment
        primary key,
    user_id bigint       not null,
    value   varchar(255) not null
);

create table blog.reply
(
    reply_id      bigint auto_increment
        primary key,
    board_id      bigint                              not null,
    reply_content varchar(255)                        null,
    reply_created timestamp default CURRENT_TIMESTAMP null,
    pre_reply_id  bigint                              null,
    user_id       bigint                              null,
    constraint reply_ibfk_1
        foreign key (board_id) references blog.board (board_id)
            on update cascade on delete cascade,
    constraint reply_ibfk_2
        foreign key (pre_reply_id) references blog.reply (reply_id)
            on update cascade on delete set null
);

create index board_id
    on blog.reply (board_id);

create index pre_reply_id
    on blog.reply (pre_reply_id);

create table blog.role
(
    roles_id   bigint auto_increment
        primary key,
    roles_name varchar(255) null,
    constraint roles_name
        unique (roles_name)
);

create table blog.series
(
    series_id    bigint auto_increment
        primary key,
    blog_id      bigint       not null,
    series_title varchar(255) not null,
    constraint series_ibfk_1
        foreign key (blog_id) references blog.blog (blog_id)
            on update cascade on delete cascade
);

create index blog_id
    on blog.series (blog_id);

create table blog.social_login_info
(
    id         bigint auto_increment
        primary key,
    provider   varchar(255)                        not null,
    social_Id  varchar(255)                        not null,
    created_At timestamp default CURRENT_TIMESTAMP not null,
    uuid       varchar(255)                        not null
);

create table blog.social_user
(
    id         bigint auto_increment
        primary key,
    social_id  varchar(255) not null,
    provider   varchar(255) not null,
    username   varchar(255) null,
    email      varchar(255) null,
    avatar_url varchar(255) null
);

create table blog.tag
(
    tag_id   bigint auto_increment
        primary key,
    tag_name varchar(255) null
);

create table blog.tag_board
(
    tag_id   bigint not null,
    board_id bigint not null,
    primary key (tag_id, board_id),
    constraint tag_board_ibfk_1
        foreign key (tag_id) references blog.tag (tag_id)
            on update cascade on delete cascade,
    constraint tag_board_ibfk_2
        foreign key (board_id) references blog.board (board_id)
            on update cascade on delete cascade
);

create index board_id
    on blog.tag_board (board_id);

create table blog.user
(
    user_id           bigint auto_increment
        primary key,
    username          varchar(255) not null,
    password          varchar(255) not null,
    name              varchar(255) not null,
    email             varchar(255) not null,
    email_status      bit          null,
    registration_date date         null,
    image_name        varchar(255) null,
    image_path        varchar(255) null,
    social_id         varchar(255) null,
    provider          varchar(50)  null,
    constraint username
        unique (username)
);

create table blog.user_role
(
    roles_id bigint not null,
    user_id  bigint not null,
    primary key (roles_id, user_id),
    constraint user_role_ibfk_1
        foreign key (roles_id) references blog.role (roles_id)
            on update cascade on delete cascade,
    constraint user_role_ibfk_2
        foreign key (user_id) references blog.user (user_id)
            on update cascade on delete cascade
);

create index user_id
    on blog.user_role (user_id);

create table blog.user_roles
(
    user_user_id  bigint not null,
    roles_role_id bigint not null,
    primary key (user_user_id, roles_role_id),
    constraint FKkv46dn3qakjvsk7ra33nd5sns
        foreign key (user_user_id) references blog.user (user_id)
);

create table blog.views
(
    view_id      bigint auto_increment
        primary key,
    view_created datetime(6) null,
    board_id     bigint      null,
    user_id      bigint      null,
    constraint FK6xwaoc6lyg1841pysjlep8ess
        foreign key (user_id) references blog.user (user_id)
            on update cascade on delete cascade,
    constraint FKmqpiy3lq3dq4x7h1mvt5obxyl
        foreign key (board_id) references blog.board (board_id)
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
    - [x] OAuth2 로그인

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

### 17. 내가 읽은 블로그 목록 보기
- [x] 내가 들어간 블로그 다시보기 목록

### 18. 이외의 기능
- [ ] 벨로그 사이트 분석 (URL 및 파라미터 등)
- [x] 벨로그와 유사하거나 더 편리한 기능 구현
- [ ] 프론트 개발 학습 및 React.js 등 프론트 개발 별도 구현 제안
- [x] 벨로그의 특별한 기능 추가 구현


#### 트러블슈팅
1.
>상위카테고리에 맞는 하위카테고리를 불러오는 js에서 하위카테고리가 불러와지지않는 트러블 발생.
>해결방법: 상위카테고리가 먼저 다 그려지기 전에 하위카테고리가 상위카테고리의 값을 가져가지않고 ajax호출하나 싶어서 콘솔로 확인. 하지만 잘 가져왔음. 
>개발자도구창에서 network탭에 들어가 해당 상위카테고리를 불러오는 응답과 하위카테고리를 불러오는 응답을 확인. 
>하위카테고리를 불러올때 board에 대한 정보들이 전부 들어가는 상황 발견. 
>방법1.카테고리 엔티티에 정의된 Set타입의 board 리스트를 지워주었다. 카테고리에서 board 객체를 쓸 일이 있을까? (카테고리별 게시글가져오기 같은 경우엔 가져와야하지않나?)
>방법2. service단에서 하위카테고리 id와 그에 맞는 이름만 return 하도록 설계해도 될거같다.
>방법3. id와 name만 담는 새로운 categoryDTO를 생성하여 거기에 새로 담아주기
![model객체 js에서 쓰기 트러블해결1](https://github.com/user-attachments/assets/8eed354f-67d7-4f98-a231-9f6d23b53a52)

2.
> 컨트롤러에서 Board엔티티 객체자체를 보내서 js의 변수에 할당하려고 했다. 하지만 오류가 났다. 인코딩을 할수 없다. 
> 이 오류가 전에도 한번 났었는데 그 경우 가져올 값이 id한개 뿐이라서 그냥 input hidden타입에 thymeleaf를 이용하여 값을 할당하고 제이쿼리 언어로 그 값을 가져오는 형태로 해결했는데 이번엔 Board객체 전체를 보내야 하는 상황에서 해당 객체의 필드를 전부 input창에 넣어줄 수도 없는 경우였다. 문제의 원인은 entity객체를 보내서 생긴 문제였다. view와 controller 사이에 값을 교환할때는 DTO를 통해서 보내야한다는 말이다. 그럼 새로운 DTO를 각각의 요청에 필요한 값을 담은 DTO를 전부 선언해주어야하냐?̊̈ 그에대한 답은 여기서 찾았다.![[스크린샷 2024-06-22 오후 8.17.56.png]]
![model객체 js에서 쓰기 트러블해결2](https://github.com/user-attachments/assets/11e1871e-1a80-4911-8663-c6cd657daf08)
