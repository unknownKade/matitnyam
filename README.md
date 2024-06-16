# 맛잇냠(Matitnyam) 맛집 서비스

사용자 주변의 맛집을 검색하고 리뷰를 남길 수 있는 서비스를 각자 개발하여 리뷰하는 팀프로젝트

## 기능
- 회원 가입
	- 회원 가입 후 서비스에 로그인 가능
	- 회원 정보 수정
	- JWT 토큰으로 로그인 관리
- 맛집 조회
	- OpenApi에서 식당 데이터를 XML로 수집 후 파싱
	- 수집된 데이터가 서비스에 적합하도록 후가공하여 보관
	- 사용자 근처에 있는 맛집 목록을 조회
	- 각 맛집의 리뷰 조회
- 리뷰 작성
	- 회원은 리뷰를 작성하고 수정

<br>

## 기술 스택

![SPRING BOOT](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white) ![](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) ![](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white) ![](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) <img src = "https://img.shields.io/badge/JWT-EB5424=?style=for-the-badge&logo=auth0&logoColor=white">

<br>

## API 상세
### 1. 사용자
#### 1-1. 사용자 로그인
- **URL:** `/user`
- **Method:** `POST`
- **Description:** 사용자 로그인 시, server-only 쿠키로 access token과 refresh token 발급 됩니다. 
- **Request Body:**
  ```json
  {
    "id": "user1",
    "password": "password", 
  }
  ```

#### 1-2.  사용자 등록
- **URL:** `/user`
- **Method:** `POST`
- **Description:** 신규 회원 가입을 합니다.
- **Request Body:**
  ```json
  {
    "id": "user1",
    "username": "사용자1",
    "password": "password", 
    "address": "서울시 마포구 마포로 12길",
    "latitude" : 1.1232149,
    "longitude": 0.20981983,
    "role" : "USER",
    "useRecommendLunch" : "false"
  }
  ```

#### 1-3. 사용자 정보 조회
- **URL:** `/user`
- **Method:** `GET`
- **Description:** 토큰으로 회원 정보를 조회합니다 
- **Response Body:**
```JSON
  {
    "id": "user1",
    "username": "사용자1",
    "address": "서울시 마포구 마포로 12길",
    "useRecommendLunch" : false
  }
```
#### 1-4.  사용자 등록
- **URL:** `/user`
- **Method:** `PUT`
- **Description:** 회원정보를 수정합니다. 
- **Request Body:**
  ```json
  {
	  "id": "user1",
    "username": "사용자1",
    "address": "서울시 마포구 마포로 12길",
    "latitude" : 1.1232149,
    "longitude": 0.20981983,
    "role" : "USER",
    "useRecommendLunch" : "true"
  }
  ```

### 2. 맛집
#### 2-1. 맛집 조회
- **URL:** `/restaurant/list`
- **Method:** `GET`
- **Description:** 내 근처 맛집 목록을 조회합니다. 
- **Query Parameters:**
  - `lat` : 내 위치  위도 
  - `lon` : 내 위치 경도
  - `range` : 검색범위
  - `orderBy` : 정렬기준
  - `orderDirection` : 정렬 순서
- **Response:**
  ```json
  [
    {
      "id": 1,
      "name": "맛집 이름",
      "latitude": 0.21389125,
      "longitude": 1.32123583
    },
    {
      "id": 2,
      "name": "맛집 이름2",
      "latitude": 0.1231445,
      "longitude": 1.12421549
    },
  ]
  ```

#### 2-2. 맛집 상세 조회
- **URL:** `/restaurant/{id}`
- **Method:** `GET`
- **Description:** 특정 맛집의 상세 정보를 조회합니다.
- **Query Parameters:**
  - `id` : 맛집의 고유 ID
- **Response:**
  ```json
  {
    "id": 1,
    "name": "맛집 이름",
    "district": "주소",
    "address" : "마포구 마포로 8길 12",
    "reviews": [
      {
        "id" : 1,
        "user": "사용자1",
        "comment": "리뷰 내용",
        "rating": 5,
        "editedDate": "2024-03-11",
        "isModified": true
      },
      {
        "id" : 2,
        "user": "사용자2",
        "comment": "리뷰 내용",
        "rating": 3,
        "editedDate": "2024-03-12",
        "isModified": false
      },
    ]
  }
  ```

#### 2-3 공공API에서 데이터 조회

### 3. 리뷰
#### 3-1. 리뷰 작성
- **URL:** `/review`
- **Method:** `POST`
- **Description:** 특정 맛집에 리뷰를 작성합니다.
- **Request Body:**
  ```json
  {
    "user": "사용자 이름",
    "comment": "리뷰 내용",
    "rating": 5
  }
  ```
  
#### 3-2. 리뷰 수정
- **URL:** `/review`
- **Method:** `PUT`
- **Description:** 특정 리뷰를 수정합니다.
- **Request Body:**
  ```json
  {
    "user": "사용자 이름",
    "comment": "리뷰 내용",
    "rating": 5
  }
  ```
