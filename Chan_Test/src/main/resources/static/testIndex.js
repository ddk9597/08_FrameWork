const selectUserBtn = document.querySelector("#selectUserBtn");
const searchMember = document.querySelector("#searchMember");


selectUserBtn.addEventListener("click", () => {
    // 입력 받은 회원 아이디 얻어오기
    const inputUser = searchMember.value;
    if(inputUser.trim().length == 0){
        alert("회원 아이디를 입력해 주세요");
        return;
    }
    
});

