// preview img 태그 5개
const previewList = document.getElementsByClassName("preview");

// input태그 5개
const inputImageList = document.getElementsByClassName("inputImage");

// 삭제 버튼
const deleteImageList = document.getElementsByClassName("delete-image"); // x버튼 5개

// x버튼 누러져 삭제된 이미지의 순서를 저장
// * Set : 중복 저장 x, 순서 유지 x
const deleteOrder = new Set();

const boardUpadateFrm = document.querySelector("boardUpadateFrm");
boardUpadateFrm.addEventListener("submit", e => {

  /* 제목, 내용 미입력 방지 */
  const boardTitle = document.querySelector("#boardTitle");
  const boardContent = document.querySelector("#boardContent");
  const boardWriteFrm = document.querySelector("#boardWriteFrm");

  boardWriteFrm.addEventListener("submit", (event) => {

    if (boardTitle.value.trim().length == 0 ||
      boardContent.value.trim().length == 0) {
      alert("제목 및 내용을 입력해 주세요");
      event.preventDefault();
      return;
    }
  });

  // input 태그에 삭제할 이미지 순서(Set)를 배열로 만든 후 대입
  // -> value(문자열) 저장 시 배열은 toString() 호출되어 양쪽[]가 사라짐
  document.querySelector("[name = 'deleteOrder']").value = Array.from(deleteOrder); 
  document.querySelector("[name = 'querystring']").value = location.search;
});