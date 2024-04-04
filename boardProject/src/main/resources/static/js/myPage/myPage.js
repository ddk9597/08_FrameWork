// 마이페이지 전부이 지금 js를 다 쓸거다
// 그러므로 오류를 방지하는 방식으로 코드 작성

/* 회원정보 수정 페이지 */
const updateInfo = document.querySelector("#updateInfo"); // form 태그

// #updateInfo 요소가 존재할때만 수행
if(updateInfo != null){

	// form 제출 시 동작
	updateInfo.addEventListener("submit", e => {

		const memberNickname = document.querySelector("#memberNickname");
		const memberTel      = document.querySelector("#memberTel");
		const memberAddress  = document.querySelectorAll("[name='memberAddress']");

		// 닉네임 유효성 검사

		// 1. 미입력 검사
		if(memberNickname.value.trim().length == 0){
			alert("닉네임을 입력해 주세요.");
			e.preventDefault();
			return;
		}
		
		// 2. 정규식 검사
		let regExp = /^[가-힣\w\d]{2,10}$/;
		// 정규식에 위배되면
		if( !regExp.test(memberNickname.value)){
			alert("조건에 맞는 닉네임을 입력해주세요.");
			e.preventDefault();
			return;
		}

		// **************************************************************** //
		// 중복검사는 나중에 추가 예정
		// (비동기 -> 동기식으로 돌려서 하는 방법으로)

		// 테스트시 닉네임 중복 안되게 조심..!
		// **************************************************************** //


		// 전화번호 유효성 검사
		// 1. 제출 안된경우
		if(memberTel.value.trim() == 0){
			alert("전화번호를 입력하세요");
			e.preventDefault();
			return;
		}

		// 2. 정규식 검사
		regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;
		if( !regExp.test(memberTel.value) ){
			alert("전화번호가 올바르지 않습니다.");
			e.preventDefault;
			return;
		}

		// 주소 유효성 검사
		// (입력 : 전부 입력 시 제출. 일부 입력시 제출x, 전부 미입력시 제출)

		const addr0 = memberAddress[0].value.trim().length == 0; // t/f
		const addr1 = memberAddress[1].value.trim().length == 0; // t/f
		const addr2 = memberAddress[2].value.trim().length == 0; // t/f

		// 모두 true인 경우만 true 저장
		const result1 = addr0 && addr1 && addr2;

		// 모두 false인 경우만 true 저장
		const result2 = !(addr0 || addr1 || addr2);

		// 둘 다 true 인경우 -> 아무것도 입력 안했거나 모두 입력했거나

		// 모두 입력 또는 모두 미입력이 아닐 경우 제출 막기
		if( !(result1 || result2) ){
			alert("주소를 모두 작성 또는 미작성 해주세요");
			e.preventDefault();
		}


	});


}

/* 비밀번호 수정 */
const changePw = document.querySelector("#changePw");
if(changePw != null){
  // 제출 되었을 때
  changePw.addEventListener("submit", e => {

    const currentPw = document.querySelector("#currentPw");
    const newPw = document.querySelector("#newPw");
    const newPwConfirm = document.querySelector("#newPwConfirm");

    // - 값 모두 입력되었는지 확인

    // 출력할 메세지
    let str; // 값이 없다 => 자료형이 없다(undefined 상태)

    if( currentPw.value.trim().length == 0 ) str = "현재 비밀번호를 입력해주세요"
    else if( newPw.value.trim().length == 0 ) str = "새 비밀번호를 입력해주세요"
    else if( newPwConfirm.value.trim().length == 0 ) str = "새 비밀번호 확인을 입력해주세요"
    
    // str에 값이 대입됨 == if문 중 하나 실행됨
    if(str != undefined){
      alert(str);
      e.preventDefault();
      return;
    }

    // - 새 비밀번호 정규식에 맞는지
    const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;

    if( !regExp.test(newPw.value) ){// 정규식 통과 못한 경우
      alert("새 비밀번호가 유효하지 않습니다");
      e.preventDefault();
      return;
    }


    // - 새 비밀번호 == 새 비밀번호 확인
    if(newPw.value != newPwConfirm.value){
      alert("새 비밀번호가 일치하지 않습니다");
      e.preventDefault();
      return false;
    }

  });
}

// 탈퇴하기

const secession = document.querySelector("#secession")

if(secession != null){
  secession.addEventListener("submit", e => {

    const memberPw = document.querySelector("#memberPw");
    const agree = document.querySelector("#agree");

    // 비밀번호가 입력 되었는지 확인
    if(memberPw.value.trim().length == 0 ){
      alert("비밀번호를 입력해 주세요");
      e.preventDefault();
      return;
    }

    // 약관 동의 체크 확인
    // checkBox 또는 radio 에 checked 속성 : 
    // - checked -> 체크시 true, 미체크시 false
    // - checked = ture -> 체크하기
    // - checked = false -> 체크 해제 하기

    // !agree.checked => agree.checked == false
    if(!agree.checked){ // 체크 안된 경우
      alert("약관 동의에 체크해 주세요");
      e.preventDefault();
      return;
    }

    // 정말 탈퇴? 물어보기
    if( !confirm("정말 탈퇴하시겠습니까?") ){ // 취소 선택 시
      alert("취소 되었습니다");
      e.preventDefault();
      return;
    }
    
  });
}