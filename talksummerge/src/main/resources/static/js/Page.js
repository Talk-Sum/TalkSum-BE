document.getElementById('shareLinkButton').addEventListener('click', function() {
    // 현재 페이지의 URL을 가져옴
    const currentPageURL = window.location.href;

    // Clipboard API를 사용하여 URL을 클립보드에 복사
    navigator.clipboard.writeText(currentPageURL).then(function() {
        alert('링크가 클립보드에 복사되었습니다.');
    }).catch(function(err) {
        console.error('복사 실패:', err);
    });
});

document.getElementById('saveIcons').addEventListener('click', function() {
    const { jsPDF } = window.jspdf;
    const pdf = new jsPDF();

    // _fonts 변수는 gulim.js에서 가져옴
    const fontData = _fonts;

    // 폰트를 VFS에 추가
    pdf.addFileToVFS('gulim.ttf', fontData);
    // 폰트를 jsPDF에 등록
    pdf.addFont('gulim.ttf', 'gulim', 'normal');
    // 폰트 설정
    pdf.setFont('gulim');
    pdf.setFontSize(12); // 여기서 폰트 크기를 조정

    // ".recordcontent" 클래스를 가진 모든 div를 가져옴
    const contents = document.querySelectorAll('.recordcontent');
    let fullContent = "";

    contents.forEach(content => {
        fullContent += content.innerText + "\n\n";
    });

    const lines = pdf.splitTextToSize(fullContent, 180); // 180mm를 최대 폭으로 설정
    let yPosition = 10;

    lines.forEach(line => {
        if (yPosition > 280) {
            pdf.addPage();
            yPosition = 10;
        }
        pdf.text(line, 10, yPosition);
        yPosition += 10;
    });

    // PDF 파일을 저장
    pdf.save('recordcontent.pdf');
});





document.getElementById('searchIcon').addEventListener('click', function() {
  const searchTerm = prompt("검색어를 입력하세요");

  if (!searchTerm) {
      alert('검색어를 입력하세요.');
      return;
  }

  
  const chats = document.querySelectorAll('.recordcontent');
  let found = false;

  chats.forEach(chat => {
      
      chat.innerHTML = chat.innerHTML.replace(/<mark>/g, '').replace(/<\/mark>/g, '');

      if (chat.innerText.includes(searchTerm)) {
         
          const regex = new RegExp(searchTerm, 'gi');
          chat.innerHTML = chat.innerHTML.replace(regex, `<mark>${searchTerm}</mark>`);
          found = true;
      }
  });

  if (!found) {
      alert('일치하는 내용이 없습니다.');
  }
});




document.getElementById('copyIcon').addEventListener('click', function() {
  const content = document.getElementById('summaryContent').innerText;


  const textarea = document.createElement('textarea');
  textarea.value = content;
  document.body.appendChild(textarea);
  textarea.select();
  document.execCommand('copy');
  document.body.removeChild(textarea);

  alert('내용이 클립보드에 복사되었습니다.');
});


const modal = document.getElementById("myModal");
const closeBtn = document.querySelector(".close");

closeBtn.onclick = function() {
  modal.style.display = "none";
}

function customAlert(text) {
  document.getElementById("modalText").innerText = text;
  modal.style.display = "block";
}


document.getElementById('saveIcon').addEventListener('click', function() {
    const { jsPDF } = window.jspdf;
    const pdf = new jsPDF();

    // _fonts 변수는 gulim.js에서 가져옴
    const fontData = _fonts;

    // 폰트를 VFS에 추가
    pdf.addFileToVFS('gulim.ttf', fontData);
    // 폰트를 jsPDF에 등록
    pdf.addFont('gulim.ttf', 'gulim', 'normal');
    // 폰트 설정
    pdf.setFont('gulim');
    pdf.setFontSize(9);

    const content = document.getElementById('summaryContent').innerText;

    const lines = pdf.splitTextToSize(content, 180); 
    let yPosition = 10;

    lines.forEach(line => {
        if (yPosition > 280) {
            pdf.addPage();
            yPosition = 10;
        }
        pdf.text(line, 10, yPosition);
        yPosition += 10;
    });

    // PDF 파일을 저장
    pdf.save('summary_content.pdf');
});


