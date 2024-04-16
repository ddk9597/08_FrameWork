/* 글쓰기 버튼 클릭 시 동작 */

const insertBtn = document.querySelector("#insertBtn");

// 로그인 상태인 경우(글쓰기 버튼이 존재 할 경우)
if(insertBtn != null){
  insertBtn.addEventListener("click", () => {

    // * boardCode 얻어오는 방법
    // 1. @PathVariable("boardCode")얻어와 전역 변수 저장
    // 2. location.pathname.split("/")[2]

    // get 방식 요청
    location.href = `/editBoard/${boardCode}/insert`;

  });
}
