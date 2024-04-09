const selectBookList = document.querySelector("#selectBookList");

if(selectBookList != null){


	// 리스트 조회 버튼 클릭 시 비동기 동작
	selectBookList.addEventListener("click", () => {

			// 비동기로 책 목록 조회
			// 책 번호, 제목, 글쓴이, 가격, 등록일

			fetch("/book/selectBookList", {
					method : "GET",
					headers : {"Content-Type" : "application/json"}
			})
			.then(response => response.json())
			.then(result =>{
					console.log(result);
					console.log("반환 타입 : " + typeof result);

					const tbody = document.querySelector("#bookList");
					tbody.innerHTML = "";

					for(let list of result){
							const tr = document.createElement("tr");
							const arr = ['bookNo', 'bookTitle', 'author', 'bookPrice', 'regDate']

							for(let key of arr ){
									const td = document.createElement("td");
									td.innerText = list[key];
									tr.append(td);
							}
							tbody.append(tr);
					}
			} );


	});
}

const registBook = document.querySelector(".registBook");
if(registBook != null){
	registBook.addEventListener("submit", e => {
		const inputTitle = document.querySelector(".inputTitle");
		const inputAutohr = document.querySelector(".inputAuthor");
		const inputPrice = document.querySelector(".inputPrice");

		// 각 항목 미입력 검사
		// 1. 제목
		if(inputTitle.value.trim().length == 0){
			alert("책의 제목을 입력하세요")
			return;
		}
		// 2. 저자
		if(inputAutohr.value.trim().length == 0){
			alert("책의 저자를 입력하세요")
			return;
		}
		// 3. 가격
		if(inputPrice.value.trim().length == 0){
			alert("책의 가격을 입력하세요")
			return;
		}
	})
}


// ----------------------------------------------------------------------- //

const searchBookList = document.querySelector("#searchBookList");

if(searchBookList != null){


	// 리스트 조회 버튼 클릭 시 비동기 동작
	searchBookList.addEventListener("click", () => {

			// 비동기로 책 목록 조회
			// 책 번호, 제목, 글쓴이, 가격, 등록일

			fetch("/book/searchBookList", {
					method : "GET",
					headers : {"Content-Type" : "application/json"}
			})
			.then(response => response.json())
			.then(result =>{
					console.log(result);
					console.log("반환 타입 : " + typeof result);

					const tbody = document.querySelector("#bookList");
					tbody.innerHTML = "";

					for(let list of result){
							const tr = document.createElement("tr");
							const arr = ['bookNo', 'bookTitle', 'author', 'bookPrice', 'regDate']
              const trBtn1 = 

							for(let key of arr ){
									const td = document.createElement("td");
									td.innerText = list[key];
									tr.append(td);
							}
							tbody.append(tr);
					}
			} );


	});
}