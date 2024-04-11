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
		});


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


const searchBookListBtn = document.querySelector("#searchBookListBtn");
if (searchBookListBtn != null) {
	searchBookListBtn.addEventListener("click", () => {
			const searchTitle = document.querySelector("#searchTitle").value; // 검색어를 가져옴

			fetch("/book/searchBookList", {
					method: "POST",
					headers: {
							"Content-Type": "application/x-www-form-urlencoded" // 폼 데이터 형식으로 전송
					},
					body: new URLSearchParams({ searchTitle: searchTitle }) // 검색어를 서버로 전송
			})
			.then(response => response.json())
			.then(result => {
					console.log(result);
					console.log("반환 타입 : " + typeof result);

					const tbody = document.querySelector("#bookList");
					tbody.innerHTML = "";

					for (let book of result) {
							const tr = document.createElement("tr");

							for (let key in book) {
									const td = document.createElement("td");
									td.innerText = book[key];
									tr.append(td);
							}

							const selectTd = document.createElement("td"); // 새로운 td 요소 생성
							const selectBtn = document.createElement("button");
							selectBtn.innerText = "선택";
							selectBtn.addEventListener("click", () => {
									// 해당 책을 선택하는 동작 수행

									const newPrice = prompt("새로운 가격을 입력하세요");
									if(newPrice != null && newPrice !== null){
										
										console.log(searchTitle); // 입력했던 제목 값 남아있음 확인
										console.log(newPrice);
										const param = {"searchTitle" : searchTitle, "newPrice": newPrice}
										fetch("book/changePrice", {
											method : "post",
											headers : {"Content-Type" : "application/json"},
											body : JSON.stringify(param)
										}).then (resp => resp.text()) // text로 변환 
										.then (res)
										
									}
									
							});
							selectTd.append(selectBtn); // 선택 버튼을 새로운 td 요소에 추가
							tr.append(selectTd); // 새로운 td 요소를 행에 추가

							const deleteTd = document.createElement("td"); // 새로운 td 요소 생성
							const deleteBtn = document.createElement("button");
							deleteBtn.innerText = "삭제";
							deleteBtn.addEventListener("click", () => {
									// 해당 책을 삭제하는 동작 수행
									if(!confirm("목록을 정말 삭제 하시겠습니까?")){return;}
							});
							deleteTd.append(deleteBtn); // 삭제 버튼을 새로운 td 요소에 추가
							tr.append(deleteTd); // 새로운 td 요소를 행에 추가

							tbody.append(tr);
					}
			});
	});
}




