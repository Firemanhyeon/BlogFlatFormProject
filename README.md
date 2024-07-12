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
