# SSAFY12_광주5_관통_Final_3팀_김자연_정현수

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
![erd](/uploads/25857b388e990be34940d3852b071e2c/erd.png)

### 시스템 구조도
![시스템_구조도](/uploads/56cd0b8a384dc68953e963f3a7838123/시스템_구조도.png)

### Figma
![초기화면설계서](/uploads/b2a652eb355eeddadb0f002f3b2183d8/초기화면설계서.png)
[go to Figma](https://www.figma.com/design/pDmPt7Uku5j0OIBwLVzeJB/ssafy-project?node-id=0-1&m=dev&t=eETbAL6Y2r2h5Dcc-1)

## :pushpin: 화면 구성
### 메인페이지
![image](/uploads/7cd9210b63b5baab432800bc13317e75/image.png)

### 회원 관리
#### 회원가입
![image](/uploads/64370a31b9e44f4571cbfbca30387a14/image.png)

#### 로그인
![image](/uploads/2441433deff79cb345a17bbd96d80ce4/image.png)

#### 회원정보 수정
![image](/uploads/0692fbc1a9076f95008000ed3344e2ed/image.png)

#### 주소지 관리
![image](/uploads/f7aa9fdeb7b7dc319e73cdd9ed4cf950/image.png)
![image](/uploads/d69e04b5002a9661382a1f04f1c2d226/image.png)
![image](/uploads/7a47184e47a2a7fff8d9d6a00eacf1a3/image.png)

### QnA 게시판
#### QnA 게시판 목록
![image](/uploads/732cc89e223fa4562c3dd839470205e6/image.png)

#### QnA 게시글 등록
![image](/uploads/0d160877221afba459a6a11f62d8f21c/image.png)

#### QnA 게시글 조회 및 답변 등록
![image](/uploads/d0da450d38d114135f5eaba27fa4ed07/image.png)

### 매물 검색
#### 메인페이지에서 검색
![image](/uploads/7cd9210b63b5baab432800bc13317e75/image.png)<br />

#### 매물 실거래가 목록 및 상세정보
![image](/uploads/4c7c45c5921bf46a723a87a23437adbf/image.png)
![image](/uploads/33ce1a52e1b9085ddc83e458f3108ca2/image.png)
![image](/uploads/827cb4e22d09f2955be641912cf02952/image.png)
 <br />

### 관심매물
#### 관심매물 조회 및 삭제
![image](/uploads/e91cc8155b9741c7db8e94833cc0684f/image.png)

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
