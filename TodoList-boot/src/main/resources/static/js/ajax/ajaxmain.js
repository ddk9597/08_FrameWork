const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");
const reloadBtn = document.querySelector("#reloadBtn");

const todoTitle = document.querySelector("#todoTitle");
const todoContent = document.querySelector("#todoContent");
const addBtn = document.querySelector("#addBtn");

// -----------------------------------------------------------------------------------------

// js 파일에 이런 함수 호출 코드를 작성한 경우 페이지 로딩 시 바로 실행됨(와우!)
getTotalCount();
getCompleteCount();

// -----------------------------------------------------------------------------------------

// 전체 Todo 개수 조회 및 출력하는 함수
function getTotalCount(){ // 함수 정의

    // 비동기로 서버(DB)에서 전체 Todo 개수 조회하는
    // fetch() API 코드 작성
    // (fetch : 가지고 오다)
    fetch("/ajax/totalCount") // 비동기 요청 수행 -> Promise 객체 반환
    .then(response => {

        // response : 비동기 요청에대한 응답이 담긴 객체
        // response.text() : 응답 데이터를 문자열/숫자 형태로 변환한 결과를 가지는 Promise 객체 반환
        console.log(response); 
        // console.log(response.text());

        return response.text();
    })
    // 두 번째 then의 매개변수(result)
    // == 첫 번째 then에서 반환된 Promise 객체의 PromiseResult 값
    .then(result => {

        // result 매개변수 == Controller 메서드에서 반환된 값
        console.log("result : ", result);

        // ID가 totalCount인 요소의 내용을 20으로 변경
        totalCount.innerText = result;
    });

}

// complete Count 값 비동기 통신으로 얻어와서 화면 출력하기
function getCompleteCount(){

    // fetch(): 비동기로 요청해서 결과 데이터 가져오기

    // 첫 then의 response : 응답결과, 요청 주소, 응답 데이터 등이 담겨있음

    // 두번째 then 의 result : 첫번째 then 에서 text로 변환된 응답 데이터(completeCount값)

    fetch("/ajax/completeCount")
    .then(response => {return response.text()} )// == .then(response => response.text() )

    .then(result => {

        console.log("result : ", result);
        // #completeCount 요소에 내용으로 result 값 출력
        completeCount.innerText = result;

    });
        
    

}

// 새로고침 버튼이 클릭되었을 때 
reloadBtn.addEventListener("click", () => {

    getTotalCount(); // 전체 할일 개수 조회(비동기)
    getCompleteCount(); // 비동기로 완료된 할일 개수 조회(비동기)

})

// -----------------------------------------------------------------------------------------

// 할 일 추가버튼 클릭 시 동작
addBtn.addEventListener('click', () => {

    // 비동기로 할 일 추가하는 fetch() API 코드 작성
    // - 요청주소 : "/ajax/add"
    // - 데이터 전달 방식(method) : post 방식

    const param = { // 파라미터를 저장한 JS 객체
        // key      : value 
        "todoTitle" : todoTitle.value,
        "todoContent" : todoContent.value
        }
    fetch("/ajax/add", {
        // key   : value 식으로 작성
        method   : "POST", // POST 방식 요청임을 기록
        headers  : {"Content-Type" : "application/json"}, // 요청데이터(파라미터)의 형식을 Json으로 지정
        body     : JSON.stringify(param) // param 객체를 json으로 변환함 4
    })
    .then(resp => resp.text()) // 반환된 값을 text로 변환
    .then(temp => {

        if(temp > 0) { 
            alert("추가 성공!!");
            // 추가 성공한 제목 + 내용 지우기
            todoTitle.value = "";
            todoContent.value = "";

            // 할 일이 추가되었으므로 전체 Todo 개수 다시 조회
            getTotalCount();
        } else {
            alert("추가 실패..");
        }

    }) // 첫 then 에서 반환된 값 중 변환된 text를 temp에 저장한다

})

// -----------------------------------------------------------------------------------------





