import { initializeApp } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-app.js";
import { getAuth, GoogleAuthProvider, signInWithPopup, FacebookAuthProvider } from "https://www.gstatic.com/firebasejs/10.4.0/firebase-auth.js";

const firebaseConfig = {
    apiKey: "AIzaSyCRGajMi8qc4fRGe9ynsVSC3XQbhmfKPlo",
    authDomain: "talksum-a3deb.firebaseapp.com",
    projectId: "talksum-a3deb",
    storageBucket: "talksum-a3deb.appspot.com",
    messagingSenderId: "964787280654",
    appId: "1:964787280654:web:5b5d3d009f11d64e00d55f"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

document.addEventListener("DOMContentLoaded", function() {
    const googleLoginBtn = document.getElementById("google-login-btn");
    if (googleLoginBtn) {
        googleLoginBtn.addEventListener("click", googleLogin);
    }
});

document.addEventListener("DOMContentLoaded", function() {
    const facebookLoginBtn = document.getElementById("facebook-login-btn");
    if (facebookLoginBtn) {
        facebookLoginBtn.addEventListener("click", facebookLogin);
    }
});


const googleLogin = (event) => {
    console.log("Google login function called");
    event.preventDefault();
    const provider = new GoogleAuthProvider();
    signInWithPopup(auth, provider).then((result) => {
        console.log("Google 로그인 성공", result);
    }).catch((error) => {
        console.log("Google 로그인 오류", error);
    });
};

const facebookLogin = (event) => {
    event.preventDefault();
    const provider = new FacebookAuthProvider();
    signInWithPopup(auth, provider).then((result) => {
        console.log("Facebook 로그인 성공", result);
    }).catch((error) => {
        console.log("Facebook 로그인 오류", error);
    });
};


const forms = document.querySelector(".forms"),
    pwShowHide = document.querySelectorAll(".eye-icon"),
    links = document.querySelectorAll(".link");

pwShowHide.forEach(eyeIcon => {
    eyeIcon.addEventListener("click", () => {
        let pwFields = eyeIcon.parentElement.parentElement.querySelectorAll(".password");

        pwFields.forEach(password => {
            if(password.type === "password"){
                password.type = "text";
                eyeIcon.classList.replace("bx-hide", "bx-show");
                return;
            }
            password.type = "password";
            eyeIcon.classList.replace("bx-show", "bx-hide");
        })

    })
})

links.forEach(link => {
    link.addEventListener("click", e => {
        e.preventDefault(); //preventing form submit
        forms.classList.toggle("show-signup");
    })
})




