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

	const loginForm=document.querySelector("#loginForm")
	// #loginForm이 화면에 존재할 때 == 로그인 상태가 아닐 때
	if(loginForm != null){
		// 제출 이벤트 발생 시
		loginForm.addEventListener("submit", e => {
			
			// 이메일, 비밀번호 입력 칸에 입력된 값이 없을 때 입력 막기
			const inputEmail = document.querySelector("input[name='memberEmail']");
			const inputPw = document.querySelector("input[name='memberPw']");
			const checkInputEmail = inputEmail.value.length;
			const chekInputPw = inputPw.value.length;
			if(checkInputEmail <= 0 || chekInputPw  <= 0){
				
				e.preventDefault(); // 기본 이벤트(제출)막기

			}


		});
	}
