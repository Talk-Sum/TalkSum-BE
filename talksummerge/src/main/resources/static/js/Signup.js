const pwShowHide = document.querySelectorAll(".eye-icon");

pwShowHide.forEach(eyeIcon => {
    eyeIcon.addEventListener("click", () => {
        let pwField = eyeIcon.previousElementSibling;  // 아이콘의 바로 이전 sibling 요소를 가져옵니다.

        if (pwField.type === "password") {
            pwField.type = "text";
            eyeIcon.classList.replace("bx-hide", "bx-show");
        } else {
            pwField.type = "password";
            eyeIcon.classList.replace("bx-show", "bx-hide");
        }
    });
});


links.forEach(link => {
    link.addEventListener("click", e => {
        e.preventDefault(); //preventing form submit
        forms.classList.toggle("show-signup");
    })
})

function redirectToLogin() {
    window.location.href = "login.html";
}

function checkPasswords() {
    // 비밀번호 입력 필드의 값 가져오기
    var password = document.getElementById("password1").value;
    var confirmPassword = document.getElementById("password2").value;

    // 두 값이 동일한지 확인
    if (password !== confirmPassword) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }
    return true;
}

function checkInputs() {
    const name = document.getElementById('name').value.trim();
    const loginId = document.getElementById('loginId').value.trim();
    const password1 = document.getElementById('password1').value.trim();
    const password2 = document.getElementById('password2').value.trim();

    if (!name || !loginId || !password1 || !password2) {
        alert('회원 정보는 필수 입력값입니다.');
        return false;
    }
    return true;
}

function checkInputsAndPasswords() {
    if (checkInputs() && checkPasswords()) {
        // 모든 검사를 통과하면 폼을 제출
        document.querySelector('form').submit();
    }
}