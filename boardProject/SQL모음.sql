/* 계정 생성(관리자 계정으로 접속)*/
ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE ;

CREATE USER SPRING_KCH IDENTIFIED BY SPRING1234;

GRANT CONNECT, RESOURCE TO SPRING_KCH;

ALTER USER SPRING_KCH
DEFAULT TABLESPACE USERS
QUOTA 20M ON USERS;

--> 계정 생성 후 접속 방법(새 데이터 베이스) 추가

--------------------------------------------------------------------

/* SPRING 계정 접속 */
--  "" : 내부에 작성된 글(모양)그대로 인식 -> 대/소문자 구분
--> "" 작성 권장

-- CHAR(10)		 : 고정 길이 문자열 10바이트(최대 2000 바이트)
-- VARCHAR2(10)  : 가변 길이 문자열 10바이트(최대 4000 바이트)
-- NVARCHAR2(10) : 가변 길이 문자열 10글자(유니코드, 최대 4000 바이트)
-- CLOB	 : 가변 길이 문자열 (최대 4GB)

/* MEMBER 테이블 생성 */
CREATE TABLE "MEMBER"(
	"MEMBER_NO" 	  NUMBER CONSTRAINT "MEMBER_PK" PRIMARY KEY,
	"MEMBER_EMAIL"	  NVARCHAR2(50)  NOT NULL,
	"MEMBER_PW"       NVARCHAR2(100) NOT NULL,
	"MEMBER_NICKNAME" NVARCHAR2(10)  NOT NULL,
	"MEMBER_TEL"	  CHAR(11) 		 NOT NULL,
	"MEMBER_ADDRESS"  NVARCHAR2(150),
	"PROFILE_IMG"	  VARCHAR2(300),
	"ENROLL_DATE"	  DATE			 DEFAULT SYSDATE NOT NULL,
	"AUTHORITY"		  NUMBER 		 DEFAULT 1 CHECK("AUTHORITY" IN ('1','2') ),
	
	-- 회원 삭제 플래그 (DELETE로 데이터를 아주 지우지 않는다.)
	"MEMBER_DEL_FL"	  CHAR(1) DEFAULT 'N'  CHECK("MEMBER_DEL_FL" IN ('Y','N') )
	);
	




