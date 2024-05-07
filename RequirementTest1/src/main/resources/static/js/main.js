const btn = document.querySelector("#btn");
const resultContainer = document.querySelector("#tbody");

btn.addEventListener("click", e => {
  fetch("/btn", {
    method: "GET",
    headers: { "Content-Type": "application/json" }
  })
    .then(response => response.json())
    .then(data => {
      resultContainer.innerHTML = ""; 

      data.forEach(obj => {
        const tr = document.createElement("tr");
        const arr = ['studentNo', 'studentName', 'studentMajor', 'studentGender'];

        arr.forEach(key => {
          const td = document.createElement("td");
          td.textContent = obj[key];
          tr.appendChild(td);
        });

        resultContainer.appendChild(tr);
      });
    })
    .catch(error => {
      console.error("오류났어요 : ", error);
    });
});
