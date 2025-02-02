# The Next Home

## :pushpin: 프로젝트 구성
### 프로젝트 설명
The Next Home은 부동산 데이터와 교통 정보를 결합하여, 사용자가 편리하게 주택 매물을 탐색할 수 있는 웹 기반 서비스입니다.<br>

### 팀 구성
|이름|역할|
|:---:|:---:|
|김자연(팀장)|매물 검색 및 필터링, 매물 상세조회, 외부 API 연동, 이메일 인증 개발|
|정현수|회원관리, QnA 게시판, 관심매물, 인증 프로세스, 거래금액 추이 시각화, 챗봇 시스템 연동|

### Notion
https://twisty-stag-36c.notion.site/1309a41b7c4680f6b47ff0426dbb7f18

### 기술 스택

  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white">
  <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black">
  <img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white">
  <br>

### ERD
![ERD](https://github.com/user-attachments/assets/2711e0af-5aed-4cbb-bda3-122f51ccc609)

### 시스템 구조도
![시스템구조도](https://github.com/user-attachments/assets/cf6aec1d-a081-4f12-bfa2-26028bebad4d)

### ![화면설계서](https://github.com/nature1216/the-next-home/blob/master/backend/resources/%ED%99%94%EB%A9%B4%EC%84%A4%EA%B3%84%EC%84%9C.pdf)

## :pushpin: 화면 구성
### 메인페이지
![Image](https://github.com/user-attachments/assets/4f61489f-67a7-48f2-8d9e-6dbb5fc78fca)

### 회원 관리
#### 회원가입
![Image](https://github.com/user-attachments/assets/35b203c6-4cbd-4de5-839a-7ce894ea035a)

#### 로그인
![Image](https://github.com/user-attachments/assets/2a309e03-5018-4753-af22-e6a1546b7ba5)

#### 회원정보 수정
![Image](https://github.com/user-attachments/assets/8f5d7d28-4cc1-4055-899f-0cdde8e1002a)

#### 주소지 관리
![Image](https://github.com/user-attachments/assets/04398b61-f7fd-4d1c-9441-ef4ac92c3f9b)
![Image](https://github.com/user-attachments/assets/bf525837-99bd-420e-8527-9b1d7a237c76)
![Image](https://github.com/user-attachments/assets/aaa5c0c9-3178-4cdb-b46c-66bb22c04666)

### QnA 게시판
#### QnA 게시판 목록
![Image](https://github.com/user-attachments/assets/d64f05e5-5792-4b41-9a42-a7b0f016fa2c)

#### QnA 게시글 등록
![Image](https://github.com/user-attachments/assets/0c8cc27c-5264-4953-bcdd-f567aa56b5c4)

#### QnA 게시글 조회 및 답변 등록
![Image](https://github.com/user-attachments/assets/3b687288-096c-4fcf-8b77-e9a2caca250e)

### 매물 검색
#### 메인페이지에서 검색
![Image](https://github.com/user-attachments/assets/0c8b6034-8e34-4f49-9a03-d26501a5c1e1)<br />

#### 매물 실거래가 목록 및 상세정보
![Image](https://github.com/user-attachments/assets/9029d5c7-e1f8-40a5-896a-5297ab9831e0)
![Image](https://github.com/user-attachments/assets/f61571c5-04a6-4bb6-9bcf-9d70a0893cd8)
![Image](https://github.com/user-attachments/assets/a8ac4ac8-5e0c-49bb-b874-52e995dd4f4f)
 <br />

### 관심매물
#### 관심매물 조회 및 삭제
![image](https://github.com/user-attachments/assets/103cd67b-5508-4010-bc73-23c3f413b7b5)


# :pushpin: Commit Convention

feat : 기능 (새로운 기능)

fix : 버그 (버그 수정)

refactor : 리팩토링

design : CSS 등 사용자 UI 디자인 변경

comment : 필요한 주석 추가 및 변경

style : 스타일 (코드 형식, 세미콜론 추가: 비즈니스 로직에 변경 없음)

docs : 문서 수정 (문서 추가, 수정, 삭제, README)

test : 테스트 (테스트 코드 추가, 수정, 삭제: 비즈니스 로직에 변경 없음)

chore : 기타 변경사항 (빌드 스크립트 수정, assets, 패키지 매니저 등)

init : 초기 생성

rename : 파일 혹은 폴더명을 수정하거나 옮기는 작업만 한 경우

remove : 파일을 삭제하는 작업만 수행한 경우

# :pushpin: Git Branch 전략 - GitHub Flow

1. master
2. feature
3. release / hotfix → 필요 시

1) master branch, develop branch

2) feature branch: 이슈추적 방식 채택
<br><br>feature/{issue-number}-{feature-name} 

3) release branch
<br><br>release/{version}

4) hotfix branch
<br><br>hotfix/{issue-number}
