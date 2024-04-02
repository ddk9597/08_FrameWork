
// 5) 유효한 이메일 형식인 경우 중복 검사 수행
// 비동기(ajax)
fetch("/member/checkEmail?memberEmail="+inputEmail)
.then( response => response.text())
.then ( count => {
    // count : 1이면 중복, 0이면 중복 아님
    if(count == 1){ // 중복
        emailMessage.innerText = "이미 사용중인 이메일 입니다."
        emailMessage.classList.add('error');
        emailMessage.classList.remove('confirm');
        checkObj.memberEmail = false; // 중복은 유효하지 않음
        return;
    } 
    if(count == 0){ // 중복아님
        emailMessage.innerText = "사용 가능한 이메일 입니다."
        emailMessage.classList.add('confirm');
        emailMessage.classList.remove('error');
        checkObj.memberEmail = true; // 유효함
    }
})
.catch(e => {
    // fetch 수행 중 예외 발생 처리
    console.log(e); // 발생한 예외(e)출력하는 코드
});