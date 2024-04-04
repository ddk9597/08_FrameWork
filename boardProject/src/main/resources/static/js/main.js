/* ************ 쿠키 얻어오기 함수 ************ */

	// 쿠키에서 key 가 일치하는 value 얻어오기 함수
	// 쿠키가 여러개 있으면 "K=V; K=V" 형태로 나열됨
	// 배열.map(함수) : 배열의 각 요소를 이용해 함수 수행 후 결과 값으로 새로운 배열을 만들어 반환
	const getCookie= (key) => {
		const cookies = document.cookie // "K=V; K=V;"

		// cookies 문자열을 배열 형태로 변환
		const cookieList = cookies.split("; ") // ["K=V", "K=V"]
															.map(el => {return el.split("=")}); // ["K", "V"]
		// console.log(cookieList);
		
		// 배열 -> 객체로 변환(다루기 쉽기 때문)
		const obj = {}; // 비어있는 객체 선언
		for(let i = 0 ; i < cookieList.length ; i ++){
			const k = cookieList[i][0]; // key 값
			const v = cookieList[i][1]; // value 값
			obj[k] = v; // 객체에 추가
		}
		// console.log("obj : ", obj);

		return obj[key] // 매개 변수로 전달 받은 key 와 
									  // obj 객체에 저장된 키가 일치하는 요소의 값 반환
	}

/* ************ 아이디 저장 버튼 기능 ************ */
	const loginEmail = document.querySelector("#loginForm input[name='memberEmail']")
	// 로그인 안된 상태인 경우에만 수행
	if(loginEmail != null) { // 로그인 창의 이메일 입력 부분이 있을 때

		// 쿠키 중 key 값이 saveId인 요소의 value를 얻어오기(getCookie())
		const saveId = getCookie("saveId"); // 결과 : undefined 또는 이메일

		// saveId 값이 있을 경우 로그인 이메일 입력 란에 저장한 이메일 출력
		if(saveId != undefined){
			loginEmail.value = saveId; // 쿠키에서 얻어온 값을 input에 value로 세팅

			// 아이디 저장 체크박스에 체크하기
			document.querySelector("input[name='saveId']").checked = true;
		}
	}

/* ************ 이메일, 비밀번호 미작성 시 로그인 시도 막기 ************ */

	// 아이디, 비밀번호 작성되지 않은 경우 로그인 못하게 함
	// -> 이메일, 비밀번호 form태그 제출을 못하게 하겠다
	// form 태그 제출 막는 방법
	// form요소.addEventListener("submit", e=> { e.preventDefault(); });
	// e.preventDefault() : 기본 이벤트 막기

	const loginForm = document.querySelector("#loginForm")
	// #loginForm이 화면에 존재할 때 == 로그인 상태가 아닐 때
	if(loginForm != null){
		// 제출 이벤트 발생 시
		loginForm.addEventListener("submit", e => {
			
			// 이메일, 비밀번호 입력 칸에 입력된 값이 없을 때 입력 막기
			const inputEmail = document.querySelector("input[name='memberEmail']");
			const inputPw = document.querySelector("input[name='memberPw']");

			// 이메일 미작성 시 
			if(inputEmail.value.trim().length <= 0 ){
				alert("이메일을 입력하세요");
				e.preventDefault(); // 기본 이벤트(제출)막기
				inputEmail.focus();
				return;
			}
			if(inputPw.value.trim().length <= 0 ){
				alert("비밀번호를 입력하세요");
				e.preventDefault(); // 기본 이벤트(제출)막기
				inputPw.focus();
				return;
			}
			

		});
	}

/* 빠른 로그인 */
const quickLoginBtns = document.querySelectorAll(".quick-login")
// 배열형태 NodeList로 출력됨 -> 배열에게는 이벤트 추가 불가

// 그래서 forEach문으로 작성
quickLoginBtns.forEach( (item, index) => {
	// item : 현재 반복 시 꺼내온 객체
	// index : 현재 반복 중인 index

	// quicLoginBtns 요소를 하나씩 꺼내서 이벤트 리스너 추가
	item.addEventListener("click", e => {

		const email = item.innerText; // 버튼에 innerText로 작성된 이메일 얻어오기

		// get방식 
		location.href = "/member/quickLogin?memberEmail=" + email;
	})
});

// ---------------------------------------------------------------------

/* 회원 목록 조회(비동기) */

// 조회 버튼
const selectMemberList = document.querySelector("#selectMemberList");

const memberList = document.querySelector("#memberList");



// 조회 버튼 클릭 시
selectMemberList.addEventListener("click", () => {
	// TodoList 비동기 코드 참조

	// 1) 비동기로 회원 목록 조회
	// 포함될 회원 정보 : 회원번호, 이메일, 닉네임, 탈퇴여부

	// fetch 에서 첫번째 then(response => reponse.json()) ->
	// JSON Array -> JS 객체 배열로 변환 [{},{},{},{}]

	
	fetch("/member/selectMemberList", {
		method : "GET",
		headers : {"Content-Type" : "application/json"}
	})
	.then(response => response.json())
	.then(result => {
		
		console.log(result);
		console.log("반환 타입 : " + typeof result); // object 객체로 반환됨(js 객체로 파싱 필요 없음)

		const tbody = document.querySelector("#memberList")

		// 2) 두번째 then
		// 		 tobdy에 이미 작성되어 있던 내용(이전에 조회한 목록) 삭제
		tbody.innerHTML = "";
		
		// 3) 두번째 then
		//    조회된 JS객체 배열을 이용해 tbody에 들어갈 요소를 만들고 값 세팅
		for(let member of result){

			const tr = document.createElement("tr");
			const arr = ['memberNo', 'memberEmail', 'memberNickname', 'memberDelFl'];

			for(let key of arr){
				const td = document.createElement("td");
				td.innerText = member[key];
				tr.append(td);
			}
			tbody.append(tr);
		}
	});

});

// ---------------------------------------------------------

/* 특정 회원 비밀번호 초기화 */
const resetMemberNo = document.querySelector("#resetMemberNo");
const resetPw = document.querySelector("#resetPw");
resetPw.addEventListener("click", () => {

  // 입력 받은 회원 번호 얻어오기
  const inputNo = resetMemberNo.value;

  if(inputNo.trim().length == 0){
    alert("회원 번호를 입력해 주세요");
    return;
  }


  fetch("/resetPw", {
    method : "PUT", // PUT : 수정 요청 방식
    headers : {"Content-Type" : "application/json"} ,
    body : inputNo
  })
  .then(resp => resp.text())
  .then(result => {
    // result == 컨트롤러로 부터 반환받아 TEXT로 파싱한 값

    if(result > 0)  alert("초기화 성공");
    else            alert("해당 회원이 존재하지 않습니다");
  });

});


/* 회원 탈퇴 복구하기 */

const inputRestoreMemNo = document.querySelector("#restoreMemNo"); // 입력한 번호
const restoreFl = document.querySelector("#restoreFl"); // 복구버튼

restoreFl.addEventListener("click", () => {

  // 입력 받은 회원 번호
  const restoreMemNo = inputRestoreMemNo.value;

  // 없으면 실행 x
  if(restoreMemNo.trim().length == 0){
    alert("회원 번호를 입력하세요");
    return;
  }

  fetch("/restore", {
    method : "PUT",
    headers : {"Content-type" : "application/json"},
    body : restoreMemNo
  })
  .then(resp => resp.text())
  .then(result => {
    if(result > 0) alert("복원 성공");
    else alert("해당 회원이 없습니다");
  });


});
