/* EXPANDER MENU */
const showMenu = (toggleId, navbarId, bodyId) => {
  const toggle = document.getElementById(toggleId),
  navbar = document.getElementById(navbarId),
  bodypadding = document.getElementById(bodyId)

  if( toggle && navbar ) {
      toggle.addEventListener('click', ()=>{
          navbar.classList.toggle('expander');

          bodypadding.classList.toggle('body-pd')
      })
  }
}

showMenu('nav-toggle', 'navbar', 'body-pd')

/* LINK ACTIVE */
const linkColor = document.querySelectorAll('.nav__link')
function colorLink() {
  linkColor.forEach(l=> l.classList.remove('active'))
  this.classList.add('active')
}
linkColor.forEach(l=> l.addEventListener('click', colorLink))

/* COLLAPSE MENU */
const linkCollapse = document.getElementsByClassName('collapse__link')
var i

for(i=0;i<linkCollapse.length;i++) {
  linkCollapse[i].addEventListener('click', function(){
      const collapseMenu = this.nextElementSibling
      collapseMenu.classList.toggle('showCollapse')

      const rotate = collapseMenu.previousElementSibling
      rotate.classList.toggle('rotate')
  });
}


/*컨텐츠 메뉴*/
document.addEventListener('DOMContentLoaded', function() {
const navLinks = document.querySelectorAll('.nav__link');
const contents = document.querySelectorAll('.content');

navLinks.forEach((link, index) => {
    link.addEventListener('click', function(e) {
        e.preventDefault();

        // 모든 컨텐츠와 메뉴의 활성화 상태를 제거
        contents.forEach(content => {
            content.classList.remove('active');
        });
        navLinks.forEach(link => {
            link.classList.remove('active');
        });

        // 클릭한 메뉴에 해당하는 컨텐츠와 메뉴 활성화
        if (index < contents.length) {
            contents[index].classList.add('active');
            this.classList.add('active');
        }
    });
});
});

// 노트만들기 버튼 숨기기
const navToggle = document.getElementById('nav-toggle');
const createNoteBtn = document.getElementById('create-note-btn');

navToggle.addEventListener('click', () => {
  createNoteBtn.classList.toggle('hidden');
});

// 모달
document.addEventListener('DOMContentLoaded', function() {
  // 메뉴 확장
  const toggle = document.getElementById('nav-toggle');
  const navbar = document.getElementById('navbar');
  const bodyPadding = document.getElementById('body-pd');
  toggle.addEventListener('click', ()=>{
      navbar.classList.toggle('expander');
      bodyPadding.classList.toggle('body-pd');
  });

  // 메뉴 항목 클릭 시, 컨텐츠 표시
  const navLinks = document.querySelectorAll('.nav__link');
  const contents = document.querySelectorAll('.content');
  navLinks.forEach((link, index) => {
      link.addEventListener('click', function(e) {
          e.preventDefault();
          contents.forEach(content => content.classList.remove('active'));
          this.classList.add('active');
          if (index < contents.length) contents[index].classList.add('active');
      });
  });
});

$(document).ready(function() {
  // 사이드바 토글
  $('#nav-toggle').on('click', function() {
      $('.l-navbar').toggleClass('expander');
  });

 
  $('.nav__link').on('click', function(e) {
      e.preventDefault(); // 링크의 기본 동작을 방지
      $('.nav__link').removeClass('active'); // 모든 링크에서 활성 상태 제거
      $(this).addClass('active'); // 클릭한 링크 활성화

      // 모든 컨텐츠 숨기기
      $('.content').removeClass('active');

      // 해당하는 컨텐츠만 보이기
      if ($(this).hasClass('nav__link--btn')) { // 새 노트 버튼 클릭 시 아무것도 보이지 않게 처리
          return;
      }
      const contentId = '#content' + $(this).index('.nav__link');
      $(contentId).addClass('active');
  });

  $('.btn-open-popup').on('click', function() {
      $('.modal').fadeIn();
  });

  // 모달 닫기
  $('.btn-close-modal').on('click', function() {
      $('.modal').fadeOut();
  });
});

function DropFile(dropAreaId, fileListId) {
  let dropArea = document.getElementById(dropAreaId);
  let fileList = document.getElementById(fileListId);

  function preventDefaults(e) {
    e.preventDefault();
    e.stopPropagation();
  }

  function highlight(e) {
    preventDefaults(e);
    dropArea.classList.add("highlight");
  }

  function unhighlight(e) {
    preventDefaults(e);
    dropArea.classList.remove("highlight");
  }

  function handleDrop(e) {
    unhighlight(e);
    let dt = e.dataTransfer;
    let files = dt.files;

    handleFiles(files);

    const fileList = document.getElementById(fileListId);
    if (fileList) {
      fileList.scrollTo({ top: fileList.scrollHeight });
    }
  }

  function handleFiles(files) {
    files = [...files];
    files.forEach(previewFile);
  }

  function previewFile(file) {
    console.log(file);
    fileList.appendChild(renderFile(file));
  }

  function renderFile(file) {
    let fileDOM = document.createElement("div");
    fileDOM.className = "file";
    fileDOM.innerHTML = `
      <div class="thumbnail">
        <img src="https://img.icons8.com/pastel-glyph/2x/image-file.png" alt="파일타입 이미지" class="image">
      </div>
      <div class="details">
        <header class="header">
          <span class="name">${file.name}</span>
          <span class="size">${file.size}</span>
        </header>
        <div class="progress">
          <div class="bar"></div>
        </div>
        <div class="status">
          <span class="percent">100% done</span>
          <span class="speed">90KB/sec</span>
        </div>
      </div>
    `;
    return fileDOM;
  }

  dropArea.addEventListener("dragenter", highlight, false);
  dropArea.addEventListener("dragover", highlight, false);
  dropArea.addEventListener("dragleave", unhighlight, false);
  dropArea.addEventListener("drop", handleDrop, false);

  return {
    handleFiles
  };
}

const dropFile = new DropFile("drop-file", "files");

//   select
$('.selected-language').click(function() {
  $('.dropdown-menu').toggle();
});

$('.dropdown-menu li').click(function() {
  var selectedLang = $(this).data('lang');
  $('.selected-language').text(selectedLang);
  $('.dropdown-menu').hide();
});

// html 불러오기
document.addEventListener("DOMContentLoaded", function() {
const elements = document.querySelectorAll('[include-html]');

elements.forEach(element => {
    const filePath = element.getAttribute('include-html');

    fetch(filePath).then(response => {
        return response.text();
    }).then(data => {
        element.innerHTML = data;
    }).catch(error => {
        console.warn('Error fetching the include file.');
    });
});
});


function goToPage(pageURL) {
window.location.href = pageURL;
}