  const totalCount = document.querySelector("#totalCount");
  const completeCount = document.querySelector("#completeCount");
  const reloadBtn = document.querySelector("#reloadBtn");

  const todoTitle = document.querySelector("#todoTitle");
  const todoContent = document.querySelector("#todoContent");
  const addBtn = document.querySelector("#addBtn");

// 할 일 목록 조회 관련 요소
const tbody = document.querySelector("#tbody")

// 할 일 상세 조회 관련 요소
const popupLayer = document.querySelector("#popupLayer");
const popupTodoNo = document.querySelector("#popupTodoNo");
const popupTodoTitle = document.querySelector("#popupTodoTitle");
const popupComplete = document.querySelector("#popupComplete");
const popupRegDate = document.querySelector("#popupRegDate");
const popupTodoContent = document.querySelector("#popupTodoContent");
const popupClose = document.querySelector("#popupClose");

// 상세조회 버튼
const deleteBtn = document.querySelector("#deleteBtn");
const NYChangeBtn = document.querySelector("#NYChangeBtn");
const updateView = document.querySelector("#updateView");

// 수정 레이어 버튼
const updateLayer = document.querySelector("#updateLayer");
const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");
const updateBtn = document.querySelector("#updateBtn");
const updateCancel = document.querySelector("#updateCancel");



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
body     : JSON.stringify(param) // param 객체를 json을 이용해서 문자화 함
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

    // 목록 비동기로 불러오기
    selectTodoList();


} else {
    alert("추가 실패..");
}

}) // 첫 then 에서 반환된 값 중 변환된 text를 temp에 저장한다

});

// -----------------------------------------------------------------------------------------

// 비동기(ajax)로 할 일 목록을 조회하는 함수
const selectTodoList = () => {

fetch("/ajax/selectList") // 비동기 요청(get 방식)
.then(response => response.text() ) // 응답데이터를 text 형태로 parsing

.then(result => {
console.log(result); // result == 첫번째 then에서 반환된 결과값
console.log(typeof result); // String 나옴. 
// JSON은 객체가 아님. JS 모양을 한 문자열!!! 

// 문자열은 가공은 할 수 있으나 힘들어
// -> JSON.parse(JSON데이터)이용하면 쉽게 변환 가능

// JSON.parse(JSON데이터) : string 형태의 JSON 데이터를 JS Object 타입으로 변환

// JSON.stringify(JS Object) : JS Object 타입을 String 형태의 JSON 데이터로 변환

const todoList = JSON.parse(result);
console.log(todoList);
console.log(typeof todoList);

// --------

/* 기존에 출력되어 있던 할 일 목록을 모두 삭제 */
tbody.innerHTML = "";


// #tbody 에 tr/td 요소를 생성해서 내용 추가

for (let todo of todoList){ // 향상된 for문
    // tr태그 생성
    const tr = document.createElement("tr");
    
    const arr = ['todoNo', 'todoTitle', 'complete', 'regDate'];
    for(let key of arr){

        const td = document.createElement("td");
        
        if(key === 'todoTitle'){
            const a = document.createElement("a"); //a태그 생성
            a.innerText = todo[key]; // 제목을 a 태그 내용으로 대입
            a.href = "/ajax/detail?todoNo=" + todo.todoNo;
            td.append(a);
            tr.append(td);
            // a태그 클릭 시 e.preventDefault()
            // 이후 비동기 요청
            a.addEventListener("click", e =>{
                e.preventDefault();
                // 할 일 상세 조회 비동기 요청
                // e.target.href : 클릭된 a 태그의 href 속성 값
                selectTodo(e.target.href);
                } ); 

            continue;
        }
        td.innerText = todo[key];
        tr.append(td);

    }
    tbody.append(tr); // tbody의 자식으로 tr 한 줄 추가

    
        }
    });
}



// 새로고침 버튼이 클릭되었을 때 
reloadBtn.addEventListener("click", () => {

getTotalCount(); // 전체 할일 개수 조회(비동기)
getCompleteCount(); // 비동기로 완료된 할일 개수 조회(비동기)

})

// -----------------------------------------------------------------------------------------

// 비동기(ajax)로 할 일 상세 조회
const selectTodo = (url) => {
    // 매개변수 url == "/ajax/detail?todoNo=10" 형태의 문자열
    // response.json() : 응답 데이터가 JSON인 경우
    // 이를 자동으로 Object 형태로 변환하는 메서드
    // == JSON.parse(JSON 데이터)
    fetch(url)
    .then(response => response.json())
    .then(todo => {
        // 매개변수 todo : 서버 응답이 object로 변환된 값
        // (첫번째 then 반환 결과)
        console.log(todo);

        /* popup layer에 조회된 값을 출력 */
        popupTodoNo.innerText = todo.todoNo;
        popupTodoTitle.innerText = todo.todoTitle;
        popupComplete.innerText = todo.complete;
        popupRegDate.innerText = todo.regDate;
        popupTodoContent.innerText = todo.todoContent;

        // popup layer를 화면에 보이게 하기
        popupLayer.classList.remove("popup-hidden");

        // updateLayer가 열려 있으면 숨기기
        updateLayer.classList.add("popup-hidden");

        /* 요소.classList.toggle("클래스명")
        - 요소에 해당 클래스가 있다면 제거
        - 요소에 해당 클래스가 없으면 추가

          요소.classList.add("클래스명")
        - 요소에 해당 클래스가 없으면 추가
        
          요소.classList.remove("클래스명")
        - 요소에 해당 클래스가 있으면 제거
        */
    });
};

// -----------------------------------------------------------------------------------------

// popupClose x 버튼 클릭 시 닫기
popupClose.addEventListener("click", () => {
    // 숨기는 class 추가
    popupLayer.classList.add("popup-hidden");
});

// -----------------------------------------------------------------------------------------
// deleteBtn 클릭 시 동작
deleteBtn.addEventListener("click", () => {

    // !confirm 삭제 - 확인(취소) 버튼 클릭 시 아무것도 안함
    if(!confirm("ㄹㅇ로다가 삭제할거임?")) {return;}

    // 삭제할 할 일 번호(PK) 얻어오기
    const todoNo = popupTodoNo.innerText; // #popupTodoNumber 내용 얻어오기

    // 비동기 delete 방식 요청
    // 비동기 요청에서만 사용 가능하다
    fetch("/ajax/delete", {
        method : "DELETE", // DELETE 방식 요청 -> @DeleteMapping 방식으로 요청
        headers : {"Content-Type" : "application/json"}, // 데이터 하나를 전달해도 application/json 작성
        body : todoNo // todoNo값을 body에 담아서 전달함 -> @RequestBody로 꺼내오면 됨
    })
    .then(response => response.text()) // 요청 결과를 text 형태로 parsing
    .then(result => {
        if(result > 0){ // 삭제 성공
            alert("삭제되었습니다");
            // 상세조회 창 닫기
            popupLayer.classList.add("popup-hidden");

            // 전체 완료된 할 일 개수 다시 조회
            // + 할 일 목록 다시 조회
            getTotalCount();
            getCompleteCount();
            selectTodoList();
        } else{
            alert("삭제 실패");
        }

    } )



});

// -----------------------------------------------------------------------------------------
// NYChangeBtn 클릭 시 동작
NYChangeBtn.addEventListener("click", () => {
    // 변경할 할 일 번호
    const todoNo = popupTodoNo.innerText;
    const changeNY = popupComplete.innerText === 'Y' ? 'N' : 'Y' ;

    // SQL 수행에 필요한 값을 객체로 묶음
    const obj ={"todoNo" : todoNo, "changeNY " : changeNY };

    // 비동기로 완료 여부 변경하기
    fetch("/ajax/changeNY", {
        method : "PUT",
        headers : {"Content-Type" :"application/json"},
        body : JSON.stringify(obj) // obj를 JSON으로 변경
    })
    .then(response => response.text() )
    .then( result => {

        if(result > 0){
            
            // update된 DB 데이터를 다시 조회해서 화면에 출력
            // -> 서버 부하가 큼

            // selectTodo();  
            // 서버 부하를 줄이기 위해 상세 조회에서 Y/N만 바꾸기
            popupComplete.innerText = changeNY; 

            //getCompleteCount();
            // 서버 부하를 줄이기 위해 완료된 Todo 개수 +-1

            const count = Number(completeCount.innerText);

            if(changeNY === 'Y') completeCount.innerText = count + 1;
            else                 completeCount.innerText = count - 1;

            selectTodoList();
            // 서버 부하 줄이기 가능!! -> 코드가 조금 복잡

        }else{
            alert("완료 여부 변경 실패!!");
        }

    });


});

// -----------------------------------------------------------------------------------------
// 상세조회에서 수정버튼(#updateView) 클릭 시
updateView.addEventListener("click", () => {

    // 기존 팝업 레이어는 숨기고
    popupLayer.classList.add("popup-hidden");

    // 수정 레이어 보이게
    updateLayer.classList.remove("popup-hidden");

    // 수정 레이어 보일 때
    // 팝업 레이어에 작성된 제목, 내용 얻어와 세팅
    updateTitle.value = popupTodoTitle.innerText ;

    // html 화면에서 줄바꿈이 <br>로 인식도고 있는데
    // textarea에서는 \으로 바꿔야 줄 바꿈으로 인식된다..!
    updateContent.value = popupTodoContent.innerHTML.replaceAll("<br>", "\n") ;

    // 수정 레이어 -> 수정 버튼에 data-todo-no 속성 추가
    updateBtn.setAttribute("data-todo-no", popupTodoNo.innerText);

});

// -----------------------------------------------------------------------------------------
// 수정 레이어에서 취소버튼(#updateCancel)이 클릭 되었을 때
updateCancel.addEventListener('click', () => {
    // 수정레이어 숨기기
    updateLayer.classList.add("popup-hidden"); 

    // 팝업 레이어 보이기
    popupLayer.classList.add("popup-hidden"); 
});

// -----------------------------------------------------------------------------------------
/* 수정레이어 수정버튼(#updateBtn) 클릭 시 */
updateBtn.addEventListener("click", e => {
    
    // 서버로 전달해야하는 값을 객체로 묶어줌
    const obj = {
        "todoNo" : e.target.dataset.todoNo,
        "todoTitle" : updateTitle.value,
        "todoContent" : updateContent.value
    };

    // 비동기 요청
    fetch("/ajax/update", {
        method : "PUT",
        headers : {"Content-Type" : "application/json" },
        body : JSON.stringify(obj)
    })
    .then(response => response.text()) // 문자열화 시켜서 반환한다
    .then(result => {
        if(result > 0){ //성공
            alert("수정 성공");
            // 수정 레이어 숨기기
            updateLayer.classList.add("popup-hidden");
            

            // selectTodo(); 상세조회 재요청
            // 성능 개선
            popupTodoTitle.innerText = updateTitle.value;
            popupTodoContent.innerHTML = updateContent.value.replaceAll("\n", "<br>")
            popupLayer.classList.remove("popup-hidden");

            selectTodoList(); // 목록 다시 조회



            updateTitle.value ="";    // 남은 흔적 제거
            updateContent.value = ""; // 남은 흔적 제거
            updateBtn.removeAttribute("data-todo-no");
        } else {
            alert("수정 실패");
        }
    })

})


// -----------------------------------------------------------------------------------------

// js 파일에 이런 함수 호출 코드를 작성한 경우 페이지 로딩 시 바로 실행됨(와우!)
getTotalCount();
getCompleteCount();
selectTodoList();

// -----------------------------------------------------------------------------------------
 