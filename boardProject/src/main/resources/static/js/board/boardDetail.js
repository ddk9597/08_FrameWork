/* 좋아요 버튼(하트)클릭 시 비동기로 좋아요 INSERT/DELTE */

// Thytmeleaf의 구성
// - html코드(+css , js)
// - th:코드(java), Spring EL

// Thymeleaf코드 해석 순서
// 1. th:코드(java), Spring EL
// 2. html코드(+css , js)

// -> session 에서 얻어오기(sessionStorage)
// 세션은 서버에서 관리하기 때문에 JS에서 바로 얻어오는 방법은 없다.
// Thymeleaf를 이용하면 됨

// 1) 로그인 한 회원 번호 준비

// 2) 현재 게시글 번호 준비

// 3) 좋아요 여부 준비

// 1. #boardLike가 클릭 되었을 때
const boardLike = document.querySelector("#boardLike");
boardLike.addEventListener("click", e => {

  // 2. 로그인 상태가 아닌 경우 동작 x
  if(loginMemberNo == null){
    alert("로그인 후 이용해 주세요");
    return;
  }

  // 3. 준비된 3개의 변수를 객체로 저장(JSON 변환 예정)
  const obj = {
    "memberNo"  : loginMemberNo,
    "boardNo"   : boardNo,
    "likeCheck" : likeCheck
  };

  // 4. 좋아요 INSERT/DELETE 비동기 요청
  fetch("/board/like", {
    method  : "post",
    headers : {"Content-Type" : "application/json"},
    body    : JSON.stringify(obj)
  })
  .then (resp => resp.text()) // 반환 결과 text 형태로 변환
  .then(count => {
    // count : 첫 번째 then에 파싱되어 반환된 값('-1' 또는 '게시글 좋아요 수' )
    // console.log("count : " + count);

    if(count == -1 ){
      alert("좋아요 처리 실패");
      return;
    }

    // 5. likeCheck 값을 0 <-> 1 변환
    // ( 왜? : 클릭될 때 마다 INSERT/DELETE 동작을 번갈아 가면서 할 수 있음)
    likeCheck = likeCheck == 0? 1:0 ;

    // 6. 화면의 하트를 채웠다 비웠다(클래스 추가/제거를 토글로)
    e.target.classList.toggle("fa-regullar");
    e.target.classList.toggle("fa-solid");

    // 7. 게시글 좋아요 수 수정하기
    e.target.nextElementSibling.innerText = count;
  })
});

/* 게시물 삭제 버튼 클릭 */

/** 작동 방법 
 * [게시글 삭제]

1. 삭제 버튼 클릭 시
  "삭제 하시겠습니까?"  (확인/취소)  출력
  
2. 취소 -> alert("취소됨")

3. 확인 -> "/editBoard/{boardCode}/{boardNo}/delete"
           GET 방식 요청
           
4. {boardCode} 게시판의 {boardNo} 글의
   BOARD_DEL_FL 값을 'Y'로 변경
   
5. 변경 성공 -> 해당 게시판 목록 1page로 리다이렉트

   실패 -> 보고있던 글 상세조회 페이지로 리다이렉트

*/

// 게시물을 get 방식 삭제하기
const deleteForm = document.querySelector("#deleteForm");

if (deleteForm !== null) {
  deleteForm.addEventListener("submit", (event) => {
    event.preventDefault(); // 기본 동작 방지

    let delBtnResult = confirm("게시글을 삭제하시겠습니까?");
    if (delBtnResult) {
      location.href = `/editBoard/${boardCode}/${boardNo}/delete`;
      alert("게시글이 삭제되었습니다.");

    } else {
      alert("게시글 삭제가 취소되었습니다.");
    }
  });
}


// 게시물을 POST 방식으로 삭제하기

/** 작동 방법 
 * [게시글 삭제]

  1. 삭제 버튼 클릭 시
    "삭제 하시겠습니까?"  (확인/취소)  출력
    
  2. 취소 -> alert("취소됨")

  3. 확인 -> "/editBoard/{boardCode}/{boardNo}/delete"
  POST 방식 요청

  4. {boardCode} 게시판의 {boardNo} 글의
    BOARD_DEL_FL 값을 'Y'로 변경 
    (로그인한 회원이 작성한 글일 경우에만) 
    --> 애초에 삭제 버튼이 없음

  5. 변경 성공 -> 해당 게시판 목록 1page로 리다이렉트

    실패 -> 보고있던 글 상세조회 페이지로 리다이렉트

*/

const delFormPost = document.querySelector("#delFormPost");
if(delFormPost != null){
  delFormPost.addEventListener("submit", e => {
    e.preventDefault();

    let delBtnResultPost = confirm("정말로 삭제?");
    // 확인 버튼 누른 경우
    if(delBtnResultPost){
      location.href = `/editBoard/${boardCode}/${boardNo}/deleteAsPost`;
      alert("삭제됨");
    } else {
      alert("삭제 취소됨");
    }
    
  });

}