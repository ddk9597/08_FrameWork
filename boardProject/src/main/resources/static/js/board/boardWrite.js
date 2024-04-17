/* 올린 파일(이미지) 보여지게 하기 (이미지 미리보기)*/
/**
 * quertSelector(), querySelectorAll()의 주의 사항
 * 호출 되었을 시점의 요소를 그대로 얻어옴
 * -> 나중의 입력이 반영되지 않음
 * 
 * getElemetsByClassName()
 * 요소를 얻어와 계속 추적함 -> 실시간으로 변경된 값 확인이 가능
 *  
 * 
 */


// .preview img 태그 5개
const previewList = document.getElementsByClassName("preview");

// input태그 5개
const inputImageList = document.getElementsByClassName("inputImage");

// 삭제 버튼
const deleteImageList = document.getElementsByClassName("delete-image"); // x버튼 5개


// 이미지 선택 이후 취소를 누를 경우를 대비한 백업 이미지
// (백업 원리 -> 복제품으로 기존 요소를 대체함)
const backupInputList = new Array(inputImageList.length);

/* ***** input태그 값 변경(파일 선택)시 실행 할 함수 ***** */
/**
 * @param inputImage : 파일이 선택된 input 태그
 * @param order : 이미지 순서
 *  */ 
const changeImageFn= (inputImage, order) => {

  // 파일 최대 크기 제한 : 바이트단위로 선언.
  // 10메가
  const maxSize = 1024 * 1024 * 10; 

  // 업로드된 파일 정보가 담긴 객체를 얻어와 변수에 저장
  const file = inputImage.files[0];

  // 파일 선택 후 취소 하여 파일이 없는 경우
  if(file == undefined){
    console.log("파일 선택 취소됨");
    console.log("order :" + order);

    // 같은 순서(order)번째 backupInputList 요소를 얻어 와 대체하기
    /** 한번 사용된 요소는 재사용이 불가능함 
      *  == 한번 화면에 추가된 요소는 다른 곳에 또 추가가 불가능함*/ 

    // 백업본을 한 번 더 복제
    const temp = backupInputList[order].cloneNode(true);
    
    inputImage.after(temp); // 백업본을 다음 요소로 추가
    inputImage.remove();    // 원본을 삭제
    inputImage = temp;      // 원본 변수에 백업본을 참조할 수 있게 대입

    // 백업본에 없는 이벤트 핸들러를 추가
    inputImage.addEventListener("change", e => {
      changeImageFn(e.target, order);
    })
    return;
  }

  // 선택된 파일이 최대 크기 초과 시
  if(file.size > maxSize){
    alert("10MB 이하의 이미지를 선택해 주세요");

    // 해당 순서의 백업 요소가 없거나
    // 요소는 있는데 값이 없는 경우(비어있는 경우) == 아무 파일도 선택된 적 없을 때
    if(backupInputList[order] == undefined || backupInputList[order].value == ''){
      inputImage.value = ""; // 잘못 업로드된 파일 값 삭제
      return;
    }
    // 이전에 정상 선택 -> 다음 선택에세 이미지 크기 초과한 경우
    // 백업본을 한 번 더 복제
    const temp = backupInputList[order].cloneNode(true);
    
    inputImage.after(temp); // 백업본을 다음 요소로 추가
    inputImage.remove();    // 원본을 삭제
    inputImage = temp;      // 원본 변수에 백업본을 참조할 수 있게 대입

    // 백업본에 없는 이벤트 핸들러를 추가
    inputImage.addEventListener("change", e => {
      changeImageFn(e.target, order);
    })
    return;
  }
  // ------------------ 선택된 이미지 미리 보기 ------------------

  const reader = new FileReader(); // JS에서 파일을 읽고 저장하는 객체
  // 선택된 파일을 JS로 읽어오기 -> reader.result 변수에 저장됨
  reader.readAsDataURL(file);

  reader.addEventListener("load", e => {
    const url = e.target.result;

    // img 태그(.preview)에 src 속성으로 url 값을 대입
    previewList[order].src = url

    // 같은 순서 backupInputList에 input태그를 복제해서 대입
    backupInputList[order] = inputImage.cloneNode(true);

  });

}

for(let i = 0 ; i < inputImageList.length ; i ++){

  // input태그에 이미지가 선택 된 경우(값이 변경된 경우)
  inputImageList[i].addEventListener("change", e=>{
    changeImageFn(e.target, i);
  })

  /** x 버튼 클릭 시 */
  deleteImageList[i].addEventListener("click", () => {

    // img, input, backup의 인덱스가 모두 일치한다는 특징을 이용

    previewList[i].src = "";       // 미리보기 이미지 지우기
    inputImageList[i].value = "" ; // input에 선택된 파일 제고
    backupInputList[i].value = ""; // 백업본도 삭제하겠다.
  });
}

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

