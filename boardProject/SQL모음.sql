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
	
	-- 관리자 1: 일반, 2: 관리자
	"AUTHORITY"		  NUMBER 		 DEFAULT 1 CHECK("AUTHORITY" IN ('1','2') ),
	
	-- 회원 삭제 플래그 (DELETE로 데이터를 아주 지우지 않는다.)
	"MEMBER_DEL_FL"	  CHAR(1) DEFAULT 'N'  CHECK("MEMBER_DEL_FL" IN ('Y','N') )
	);

-- 회원 번호 시퀀스 만들기
CREATE SEQUENCE SEQ_MEMBER_NO NOCACHE;



-- 샘플 회원데이터 삽입
INSERT INTO "MEMBER"
VALUES(SEQ_MEMBER_NO.NEXTVAL,
	   'member01@naver.com',
	   '$2a$10$n0TIiJwZNXJKfwJk8uwyKuaKupO5l4m8rP05YCn/J8bmQAO/631d.',
	   '탈퇴시도01',
	   '01012341234',
	   NULL, NULL,
	   DEFAULT, DEFAULT, DEFAULT
	   );

COMMIT;

SELECT * FROM "MEMBER" m ;

DELETE  FROM "MEMBER" m 
WHERE MEMBER_EMAIL = '027620@naver.com'; -- 지우고 커밋해라........

-- 로그인 
-- 비밀번호 조회 : 가져와서 비교해야되니까
-- BCrypt 암호화 사용 중으로 DB에서 비밀번호 비교 불가능함.
-- 검색조건 : 이메일이 일치하는 회원 + 탈퇴 안한 회원
SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_PW,
	 MEMBER_TEL , MEMBER_ADDRESS , PROFILE_IMG , AUTHORITY , 
	 TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일" HH"시" MI"분" SS"초"') ENROLL_DATE  
FROM "MEMBER" 
WHERE MEMBER_EMAIL = ?
AND MEMBER_DEL_FL = 'N';


/* 이메일, 인증키 저장 테이블 생성 */

CREATE TABLE "TB_AUTH_KEY"(
	"KEY_NO" NUMBER PRIMARY KEY,
	"EMAIL"  NVARCHAR2(50) NOT NULL,
	"AUTH_KEY" CHAR(6) NOT NULL,
	"CREATE_TIME" DATE DEFAULT SYSDATE NOT NULL
	);

COMMENT ON COLUMN "TB_AUTH_KEY"."KEY_NO"		IS '인증키 구분 번호(시퀀스)';
COMMENT ON COLUMN "TB_AUTH_KEY"."EMAIL"			IS '인증 이메일';
COMMENT ON COLUMN "TB_AUTH_KEY"."AUTH_KEY"		IS '인증번호';
COMMENT ON COLUMN "TB_AUTH_KEY"."CREATE_TIME"	IS '인증 번호 생성 시간';

CREATE SEQUENCE SEQ_KEY_NO NOCACHE; -- 인증키 구분 번호 시퀀스

DROP TABLE "TB_AUTH-KEY";


SELECT * FROM TB_AUTH_KEY;

SELECT COUNT(*) 
FROM TB_AUTH_KEY
WHERE EMAIL = #{가입하려는 이메일 입력 값}
AND AUTH_KEY = #{위 이메일로 보낸 인증번호}


DELETE FROM TB_AUTH_KEY
WHERE EMAIL = '027620@naver.com';

SELECT *
FROM "MEMBER";

/* 파일 업로드 시험용 테이블 */
-- 파일 저장할 주소를 미리 정한 후 그 주소를 DB에 저장한다.
-- 파일을 DB에 직접 저장하지 않음(가능은 한데 비추천)
-- 파일 업로드 테스트용 테이블
CREATE TABLE "UPLOAD_FILE"(
	FILE_NO NUMBER PRIMARY KEY,
	FILE_PATH VARCHAR2(500) NOT NULL,
	FILE_ORIGINAL_NAME VARCHAR2(300) NOT NULL,
	FILE_RENAME VARCHAR2(100) NOT NULL,
	FILE_UPLOAD_DATE DATE DEFAULT SYSDATE,
	MEMBER_NO NUMBER REFERENCES "MEMBER"
);

COMMIT;

COMMENT ON COLUMN "UPLOAD_FILE".FILE_NO 		IS '파일 번호(PK)';
COMMENT ON COLUMN "FILE_PATH".FILE_PATH 			IS '클라이언트 요청 경로';
COMMENT ON COLUMN "FILE_ORIGINAL_NAME".FILE_ORIGINAL_NAME 	IS '파일 원본명';
COMMENT ON COLUMN "FILE_RENAME".FILE_RENAME 		IS '변경된 파일명';
COMMENT ON COLUMN "FILE_UPLOAD_DATE".FILE_UPLOAD_DATE 	IS '업로드한 날짜';
COMMENT ON COLUMN "MEMBER_NO".MEMBER_NO 			IS '파일을 올린 회원 번호 -> MEMBER테이블의 PK(MEMBER_NO)참조';

CREATE SEQUENCE SEQ_FILE_NO NOCACHE;


SELECT * FROM UPLOAD_FILE uf ;

-- 파일 이름은 변경해서 저장한다. 사용자가 정한 이름을 사용하게 되면 오류가 발생되기 때문


-- 파일 목록 조회


SELECT FILE_NO , FILE_PATH , FILE_ORIGINAL_NAME , FILE_RENAME,
	TO_CHAR(FILE_UPLOAD_DATE, 'YYYY-MM-DD') FILE_UPLOAD_DATE,
	MEMBER_NICKNAME
FROM "UPLOAD_FILE"
JOIN "MEMBER" USING(MEMBER_NO)
ORDER BY FILE_NO DESC ;





----------------------------------------------------------------------------------------------------------------------

CREATE TABLE "MEMBER" (
	"MEMBER_NO"			NUMBER		NOT NULL,
	"MEMBER_EMAIL"		NVARCHAR2(50)		NOT NULL,
	"MEMBER_PW"			NVARCHAR2(100)		NOT NULL,
	"MEMBER_NICKNAME"	NVARCHAR2(10)		NOT NULL,
	"MEMBER_TEL"	CHAR(11)		NOT NULL,
	"MEMBER_ADDRESS"	NVARCHAR2(300)		NULL,
	"PROFILE_IMG"	VARCHAR2(300)		NULL,
	"ENROLL_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MEMBER_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"AUTHORITY"	NUMBER	DEFAULT 1	NOT NULL
);

COMMENT ON COLUMN "MEMBER"."MEMBER_NO" IS '회원 번호(PK)';

COMMENT ON COLUMN "MEMBER"."MEMBER_EMAIL" IS '회원 이메일';

COMMENT ON COLUMN "MEMBER"."MEMBER_PW" IS '회원 비밀번호(암호화)';

COMMENT ON COLUMN "MEMBER"."MEMBER_NICKNAME" IS '회원 닉네임';

COMMENT ON COLUMN "MEMBER"."MEMBER_TEL" IS '회원 전화번호';

COMMENT ON COLUMN "MEMBER"."MEMBER_ADDRESS" IS '회원 주소';

COMMENT ON COLUMN "MEMBER"."PROFILE_IMG" IS '프로필 이미지';

COMMENT ON COLUMN "MEMBER"."ENROLL_DATE" IS '회원 가입일';

COMMENT ON COLUMN "MEMBER"."MEMBER_DEL_FL" IS '회원 탈퇴 여부("Y", "N")';

COMMENT ON COLUMN "MEMBER"."AUTHORITY" IS '권한(1일반, 2관리자)';

CREATE TABLE "UPLOAD_FILE" (
	"FILE_NO"	NUMBER		NOT NULL,
	"FILE_PATH"	VARCHAR2(500)		NOT NULL,
	"FILE_OROGINAL_NAME"	VARCHAR2(300)		NOT NULL,
	"FILE_RENAME"	VARCHAR2(100)		NOT NULL,
	"FILE_UPLOAD_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MMEBER_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_NO" IS '파일번호(PK)';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_PATH" IS '파일 경로';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_ORIGINAL_NAME" IS '파일 원본명';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_RENAME" IS '파일 변경명';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_UPLOAD_DATE" IS '업로드 날짜';

COMMENT ON COLUMN "UPLOAD_FILE"."MEMBER_NO" IS '회원 번호';

CREATE TABLE "BOARD" (
	"BOARD_NO"	NUMBER		NOT NULL,
	"BOARD_TITLE"	NVARCHAR2(100)		NOT NULL,
	"BOARD_CONTENT"	NVARCHAR2(2000)		NOT NULL,
	"BOARD_WRITE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"BOARD_UPDATE_DATE"	DATE		NULL,
	"READ_COUNT"	NUMBER	DEFAULT 0	NOT NULL,
	"BOARD_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"BOARD_CODE"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD"."BOARD_NO" IS '게시글 번호';

COMMENT ON COLUMN "BOARD"."BOARD_TITLE" IS '게시글 제목';

COMMENT ON COLUMN "BOARD"."BOARD_CONTENT" IS '게시글 내용';

COMMENT ON COLUMN "BOARD"."BOARD_WRITE_DATE" IS '게시글 작성일';

COMMENT ON COLUMN "BOARD"."BOARD_UPDATE_DATE" IS '게시글 마지막 수정일';

COMMENT ON COLUMN "BOARD"."READ_COUNT" IS '조회수';

COMMENT ON COLUMN "BOARD"."BOARD_DEL_FL" IS '게시글 삭제 여부("Y", "N")';

COMMENT ON COLUMN "BOARD"."BOARD_CODE" IS '게시판 종류 코드 번호';

COMMENT ON COLUMN "BOARD"."MEMBER_NO" IS '작성한 회원 번호';

CREATE TABLE "BOARD_TYPE" (
	"BOARD_CODE"	NUMBER		NOT NULL,
	"BOARD_NAME"	NVARCHAR2(20)		NOT NULL
);

COMMENT ON COLUMN "BOARD_TYPE"."BOARD_CODE" IS '게시판 종류 코드 번호';

COMMENT ON COLUMN "BOARD_TYPE"."BOARD_NAME" IS '게시판 명';

CREATE TABLE "BOARD_LIKE" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD_LIKE"."MEMBER_NO" IS '회원 번호(PK)';

COMMENT ON COLUMN "BOARD_LIKE"."BOARD_NO" IS '게시글 번호';

CREATE TABLE "BOARD_IMG" (
	"IMG_NO"	NUMBER		NOT NULL,
	"IMG_PATH"	VARCHAR2(200)		NOT NULL,
	"IMG_OROGINAL_NAME"	NVARCHAR2(50)		NOT NULL,
	"IMG_RENAME"	NVARCHAR2(50)		NOT NULL,
	"IMG_ORDER"	NUMBER		NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD_IMG"."IMG_NO" IS '이미지 번호(PK)';

COMMENT ON COLUMN "BOARD_IMG"."IMG_PATH" IS '이미지 요청 경로';

COMMENT ON COLUMN "BOARD_IMG"."IMG_OROGINAL_NAME" IS '이미지 원본명';

COMMENT ON COLUMN "BOARD_IMG"."IMG_RENAME" IS '이미지 변경명';

COMMENT ON COLUMN "BOARD_IMG"."IMG_ORDER" IS '이미지 순서';

COMMENT ON COLUMN "BOARD_IMG"."BOARD_NO" IS '게시글 번호';

CREATE TABLE "COMMENT" (
	"COMMENT_NO"	NUMBER		NOT NULL,
	"COMMENT_CONTENT"	VARCHAR2(4000)		NOT NULL,
	"COMMENT_WRITE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"COMMENT_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL,
	"PARENT_COMMENT_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "COMMENT"."COMMENT_NO" IS '댓글 번호';

COMMENT ON COLUMN "COMMENT"."COMMENT_CONTENT" IS '댓글 내용';

COMMENT ON COLUMN "COMMENT"."COMMENT_WRITE_DATE" IS '댓글작성일';

COMMENT ON COLUMN "COMMENT"."COMMENT_DEL_FL" IS '댓글 삭제 여부("Y" , "N")';

COMMENT ON COLUMN "COMMENT"."BOARD_NO" IS '게시글 번호';

COMMENT ON COLUMN "COMMENT"."MEMBER_NO" IS '회원 번호(PK)';

COMMENT ON COLUMN "COMMENT"."PARENT_COMMENT_NO" IS '부모 댓글 번호';

ALTER TABLE "MEMBER" ADD CONSTRAINT "PK_MEMBER" PRIMARY KEY (
	"MEMBER_NO"
);

ALTER TABLE "UPLOAD_FILE" ADD CONSTRAINT "PK_UPLOAD_FILE" PRIMARY KEY (
	"FILE_NO"
);

ALTER TABLE "BOARD" ADD CONSTRAINT "PK_BOARD" PRIMARY KEY (
	"BOARD_NO"
);

ALTER TABLE "BOARD_TYPE" ADD CONSTRAINT "PK_BOARD_TYPE" PRIMARY KEY (
	"BOARD_CODE"
);

ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "PK_BOARD_LIKE" PRIMARY KEY (
	"MEMBER_NO",
	"BOARD_NO"
);

ALTER TABLE "BOARD_IMG" ADD CONSTRAINT "PK_BOARD_IMG" PRIMARY KEY (
	"IMG_NO"
);


ALTER TABLE "COMMENT" ADD CONSTRAINT "PK_COMMENT" PRIMARY KEY (
	"COMMENT_NO"
);


------------------------------------------------------ FK ---------------------------------------

ALTER TABLE "UPLOAD_FILE" ADD CONSTRAINT "FK_MEMBER_TO_UPLOAD_FILE_1" FOREIGN KEY (
	"MEBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "BOARD" ADD CONSTRAINT "FK_BOARD_TYPE_TO_BOARD_1" FOREIGN KEY (
	"BOARD_CODE"
)
REFERENCES "BOARD_TYPE" (
	"BOARD_CODE"
);

ALTER TABLE "BOARD" ADD CONSTRAINT "FK_MEMBER_TO_BOARD_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "FK_MEMBER_TO_BOARD_LIKE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "FK_BOARD_TO_BOARD_LIKE_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);

ALTER TABLE "BOARD_IMG" ADD CONSTRAINT "FK_BOARD_TO_BOARD_IMG_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_BOARD_TO_COMMENT_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_MEMBER_TO_COMMENT_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_COMMENT_TO_COMMENT_1" FOREIGN KEY (
	"PARENT_COMMENT_NO"
)
REFERENCES "COMMENT" (
	"COMMENT_NO"
);

------------------------------------------ CHECK ------------------------------

-- 게시글 삭제 여부
ALTER TABLE "BOARD" 
ADD
CONSTRAINT "BOARD_DEL_CHECK"
CHECK("BOARD_DEL_FL" IN ('Y', 'N') );

-- 댓글 삭제 여부
ALTER TABLE "COMMENT" 
ADD
CONSTRAINT "COMMENT_DEL_CHECK"
CHECK("COMMENT_DEL_FL" IN ('Y', 'N') );








--------------------------------- 도서 검색 프로그램 --------------------------------

CREATE TABLE "BOOK_SEARCH"(
	"BOOK_NO" 	  	NUMBER CONSTRAINT "BOOK_PK" PRIMARY KEY,
	"BOOK_TITLE"	NVARCHAR2(50)  	NOT NULL,
	"AUTHOR"      	NVARCHAR2(50) 	NOT NULL,
	"BOOK_PRICE" 	number  		NOT NULL,
	"REG_DATE"	DATE 		DEFAULT SYSDATE NOT NULL
	);


DROP TABLE BOOK_SEARCH;


SELECT  * 
FROM BOOK_SEARCH ;

COMMIT ;


INSERT INTO BOOK_SEARCH VALUES (1, '어린왕자', '생텍쥐페리', '9500', DEFAULT);
INSERT INTO BOOK_SEARCH VALUES (2, '이방인', '알베르 카뮈', '13500', DEFAULT);
INSERT INTO BOOK_SEARCH VALUES (3, '장수탕 선녀님', '백희나', '13500', DEFAULT);
INSERT INTO BOOK_SEARCH VALUES (4, '한 사람의 노래가 온 거리에 노래를', '신경림', '6300', DEFAULT);

-- 첵 번호 시퀀스 만들기
CREATE SEQUENCE SEQ_BOOK_NO NOCACHE;



--------------------------------- Interceptor --------------------------------
/* 게시판 종류(BOARD_TYPE)추가 */

CREATE SEQUENCE SEQ_BOARD_CODE NOCACHE;

INSERT INTO BOARD_TYPE VALUES (SEQ_BOARD_CODE.NEXTVAL, '공지 게시판');
INSERT INTO BOARD_TYPE VALUES (SEQ_BOARD_CODE.NEXTVAL, '정보 게시판');
INSERT INTO BOARD_TYPE VALUES (SEQ_BOARD_CODE.NEXTVAL, '자유 게시판');

COMMIT;



-- 게시판 종류 조회 (표기법 변경)
SELECT BOARD_CODE "boardCode", BOARD_NAME  "boardName"
FROM BOARD_TYPE
ORDER BY BOARD_CODE
;

-------------------------------------------------------------------
/* 게시글 번호 시퀀스 생성 */

CREATE SEQUENCE SEQ_BOARD_NO NOCACHE;

/* 게시판(BOARD) 테이블 샘플 데이터 삽입(PL/SQL) */
-- DBMS_RANDOM.VALUE(0.3)
-- 0.0 ~ 2.999 사이 난수 발생 후 CEIL을 이용하여 1단위로 올림처리(1,2,3 만 나옴)

SELECT * FROM "MEMBER"
WHERE MEMBER_DEL_FL = 'N';

BEGIN
	FOR I IN 1..2000 LOOP
		
		INSERT INTO BOARD
		VALUES(
			SEQ_BOARD_NO.NEXTVAL,
			SEQ_BOARD_NO.CURRVAL || '번째 게시글', 
			SEQ_BOARD_NO.CURRVAL || '번째 게시글 내용입니다',
			DEFAULT, DEFAULT, DEFAULT, DEFAULT,
			CEIL( DBMS_RANDOM.VALUE(0, 4) ),
			8
		);
	END LOOP;
END;
;
-- 샘플 데이터 삽입 확인
SELECT COUNT(*) FROM "BOARD";


-- 게시판 종류별 샘플 데이터 삽입 확인
SELECT BOARD_CODE, COUNT(*)
FROM BOARD b 
GROUP BY BOARD_CODE
ORDER BY BOARD_CODE ;

-- 번호 제목[댓글개수] 작성일 작성자 형식으로 출력하기
-- 댓글 샘플데이터 삽입
/* 댓글 번호 시쿠너스 생성*/

CREATE SEQUENCE SEQ_COMMENT_NO NOCACHE;

/* 댓글(COMMENT) 테이블에 샘플 데이터 추가*/
-- 8번 회원이 실행
BEGIN
	FOR I IN 1..2000 LOOP
		
		INSERT INTO "COMMENT"
		VALUES (
			SEQ_COMMENT_NO.NEXTVAL,
			SEQ_COMMENT_NO.CURRVAL || '번째 댓글 입니다.',
			DEFAULT, DEFAULT,
			CEIL(DBMS_RANDOM.VALUE(3, 2003)),
			8,
			NULL
		);
		
	END LOOP;
	
END;

COMMIT;

--- 게시글 번호 최소값, 최대값 조회
SELECT MIN(BOARD_NO), MAX(BOARD_NO)
FROM BOARD b ;
	-- 4, 2003 

-- 테이블 부모댓글번호 NOTNULL-> NULL로 변경
ALTER TABLE "COMMENT"
MODIFY PARENT_COMMENT_NO NUMBER NULL;

BEGIN
	FOR I IN 1..2000 LOOP
		
		INSERT INTO "COMMENT"
		VALUES (
			SEQ_COMMENT_NO.NEXTVAL,
			SEQ_COMMENT_NO.CURRVAL || '번째 댓글 입니다.',
			DEFAULT, DEFAULT,
			CEIL(DBMS_RANDOM.VALUE(3, 2003)),
			1,
			NULL
		);
		
	END LOOP;
	
END;

COMMIT;


-- 댓글 삽입 확인
SELECT COUNT(*) FROM "COMMENT" 
GROUP BY BOARD_NO 
ORDER BY BOARD_NO ;

-- ---------------------------------------------

/* 특정 게시판(BOARD_CODE)에 삭제되지 않은 게시글 목록 조회
 * 
 * 단, 최신 글이 제일 위에 존재
 * 
 * 몇 초/분/시간 전 또는 YYYY-MM-DD 형식으로 작성일 조회
 * 
 * + 댓글 개수
 * + 좋아요 개수
 * 
 * */

--- 나올 화면
-- 번호 / 제목 [댓글개수] / 작성자닉네임 /작성일 / 조회수 / 좋아요 개수

-- 상관 서브 쿼리
-- 1) 메인 쿼리 1행 조회
-- 2) 1행 조회 결과를 이용해서 서브쿼리 수행
--	  메인쿼리 모두 조회할 때 까지 반복
SELECT BOARD_NO, BOARD_TITLE, MEMBER_NICKNAME, READ_COUNT,
	(	SELECT COUNT(*) 
		FROM "COMMENT" C
		WHERE C.BOARD_NO = B.BOARD_NO ) COMMENT_COUNT,	
		
	(	SELECT COUNT(*)
		FROM "BOARD_LIKE" L
		WHERE L.BOARD_NO = B.BOARD_NO ) LIKE_COUNT,
		
		CASE
			WHEN SYSDATE - BOARD_WRITE_DATE < 1 / 24 / 60
			THEN FLOOR((SYSDATE - BOARD_WRITE_DATE) * 24 * 60 * 60) || '초 전'
			
			WHEN SYSDATE - BOARD_WRITE_DATE < 1 / 24
			THEN FLOOR((SYSDATE - BOARD_WRITE_DATE) * 24 * 60) || '분 전'
			
			WHEN SYSDATE - BOARD_WRITE_DATE < 1
			THEN FLOOR((SYSDATE - BOARD_WRITE_DATE) * 24) || '시간 전'
			
			ELSE TO_CHAR(BOARD_WRITE_DATE, 'YYYY-MM-DD')
			
		END D
		
	
FROM "BOARD" B
JOIN "MEMBER" USING(MEMBER_NO)
WHERE BOARD_DEL_FL = 'N'
AND BOARD_CODE = 1
ORDER BY BOARD_NO DESC
;

-- 특정 게시글의 댓글 개수 조회
SELECT COUNT(*) FROM "COMMENT"
WHERE BOARD_NO = 123 
;


-- 현재 시간 - 하루 전 --> 정수 부분 == 일 단위
SELECT ( SYSDATE - TO_DATE('2024-04-10 12:14:30', 'YYYY-MM-DD HH24:MI:SS') ) * 60 * 60 * 24
FROM DUAL;

SELECT * FROM BOARD_TYPE bt ;

-- 지정된 게시판에서 삭제되지 않은 게시글 수를 조회
SELECT COUNT(*)
FROM BOARD
WHERE BOARD_DEL_FL = 'N'
AND   BOARD_CODE = 3
;


------------------------------------------------------------------------------------------------------
/* BOARD_IMG 테이블용 시퀀스 생성 */

CREATE SEQUENCE SEQ_IMG_NO NOCACHE;

--- BOARD_IMG 테이블에 게시판 이미지 샘플 데이터 삽입
INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본1.png', 'test1.png'
	, 0, 2003);

INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본2.jpg', 'test2.jpg'
	, 1, 2003);

INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본3.jpg', 'test3.jpg'
	, 2, 2003);

INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본4.jpg', 'test4.jpg'
	, 3, 2003);

INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본5.png', 'test5.png'
	, 4, 2003);

COMMIT;


-------------------------------------------------------------------------------------------------------
/* 게시글 상세 조회 */
SELECT BOARD_NO, BOARD_TITLE,
BOARD_CONTENT, BOARD_CODE, READ_COUNT,
MEMBER_NO, MEMBER_NICKNAME, PROFILE_IMG,

TO_CHAR(BOARD_WRITE_DATE, 'YYYY"년" MM"월" DD"일" HH24:MI:SS') BOARD_WRITE_DATE,
TO_CHAR(BOARD_UPDATE_DATE, 'YYYY"년" MM"월" DD"일" HH24:MI:SS')
BOARD_UPDATE_DATE,

	(SELECT COUNT(*)
	FROM "BOARD_LIKE"
	WHERE BOARD_NO = 2003) LIKE_COUNT,
	
	(SELECT IMG_PATH || IMG_RENAME
	FROM "BOARD_IMG"
	WHERE BOARD_NO = 2003
	AND IMG_ORDER = 0) THUMBNAIL,
	
 	(SELECT COUNT(*) FROM "BOARD_LIKE"
	WHERE MEMBER_NO = 8
	AND BOARD_NO = 2003) LIKE_CHECK

FROM "BOARD"
JOIN "MEMBER" USING(MEMBER_NO)
WHERE BOARD_DEL_FL = 'N'
AND
BOARD_CODE = 1
AND BOARD_NO = 2003

;

-------------------------------------------------------------------------------------------------------
/* 해당 상세 조회 되는 게시글의 모든 이미지 조회 */
SELECT * FROM "BOARD_IMG" 
WHERE BOARD_NO = 2003
ORDER BY IMG_ORDER;

/* 상세 조회 되는 게시글의 모든 댓글을 조회 */

/* 상세조회 되는 게시글의 모든 댓글 조회 */


/* -- 계층형 쿼리
 * 
 * 
 * */


SELECT LEVEL, C.* FROM
		(SELECT COMMENT_NO, COMMENT_CONTENT,
		    TO_CHAR(COMMENT_WRITE_DATE, 'YYYY"년" MM"월" DD"일" HH24"시" MI"분" SS"초"') COMMENT_WRITE_DATE,
		    BOARD_NO, MEMBER_NO, MEMBER_NICKNAME, PROFILE_IMG, PARENT_COMMENT_NO, COMMENT_DEL_FL
		FROM "COMMENT"
		JOIN MEMBER USING(MEMBER_NO)
		WHERE BOARD_NO = 2003) C
		WHERE COMMENT_DEL_FL = 'N'
		OR 0 != (SELECT COUNT(*) FROM "COMMENT" SUB
						WHERE SUB.PARENT_COMMENT_NO = C.COMMENT_NO
						AND COMMENT_DEL_FL = 'N')
		START WITH PARENT_COMMENT_NO IS NULL
		CONNECT BY PRIOR COMMENT_NO = PARENT_COMMENT_NO
		ORDER SIBLINGS BY COMMENT_NO;




/* 좋아요 테이블(BOARD_LIKE) 샘플 데이터 추가 */
-- 2003번 게시물 8번 회원

INSERT INTO BOARD_LIKE VALUES(8,2003); -- 8번 회원이 2003번 게시물에 좋아요를 클릭함

COMMIT;

/* 좋아요를 눌렀는지 판별하는 SQL -> SELECT 구문으로 조회 */
/* 결과 1:누름, 2:안누름 */
SELECT COUNT(*) FROM "BOARD_LIKE"
WHERE MEMBER_NO = 8
AND BOARD_NO = 2003
;

SELECT COUNT(*) FROM "BOARD_LIKE";


-------------------------------------------------------------------------------------------------------

-- 여러 행을 한 번에 삽입 하는 방법 -> INSERT + SUBQUERY

-- 원래 서브쿼리 안에는 시퀀스 사용 할 수 없음.
-- -> 시퀀스로 번호 생성하는 부분을 별도 함수로 분리 후 호출하면 문제 없음
INSERT INTO "BOARD_IMG"
(
	SELECT NEXT_IMG_NO(), '경로1', '원본1', '변경1', 1, 2003 FROM DUAL 
	UNION
	SELECT NEXT_IMG_NO(), '경로2', '원본2', '변경2', 2, 2003 FROM DUAL
	UNION
	SELECT NEXT_IMG_NO(), '경로3', '원본3', '변경3', 3, 2003 FROM DUAL 
);

SELECT * FROM BOARD_IMG ;


-- ------------- 시퀀스 생성용 함수 시작 -------------
-- SEQ_IMG_NO의 다음 값을 반환 하는 함수 생성
CREATE OR REPLACE FUNCTION NEXT_IMG_NO

-- 반환형
RETURN NUMBER

-- 사용할 변수
IS IMG_NO NUMBER;

BEGIN 
	SELECT SEQ_IMG_NO.NEXTVAL 
	INTO IMG_NO
	FROM DUAL;	

	RETURN IMG_NO; 
END;

-- ------------- 시퀀스 생성용 함수 끝 -------------
-- 구분용 세미콜론
;

-- 시퀀스 확인
SELECT NEXT_IMG_NO() FROM DUAL;


ROLLBACK;









