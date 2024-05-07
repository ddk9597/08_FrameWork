document.getElementById('test').addEventListener('click', function() {
  var regNum = document.getElementById('regNum').value;
  var dealerName = document.getElementById('dealerName').value;
  var url = `http://openapi.seoul.go.kr:8088/574345677664646b31303243637a7778/xml/landBizInfo/1/5/${regNum}/${dealerName}`;

  fetch(url)
      .then(response => {
          if (!response.ok) {
              throw new Error('Network response was not ok ' + response.statusText);
          }
          return response.text();
      })
      .then(data => {
          document.getElementById('responseArea').textContent = data;
      })
      .catch(error => {
          console.error('There was a problem with the fetch operation:', error);
          alert('Failed to fetch data: ' + error.message);
      });
});
