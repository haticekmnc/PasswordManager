/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// passwordToggle.js

// JavaScript dosyasının başında 'use strict' kullanılmasını öneririm
'use strict';

// Fonksiyonların tanımlanmasının üzerine 'togglePasswordVisibility' fonksiyonunu ekleyelim
// togglePasswordVisibility fonksiyonunu yorum satırı yapın
/*
function togglePasswordVisibility(inputId) {
    var passwordInput = document.getElementById(inputId);
    var eyeIcon = document.getElementById("eye-icon");
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        eyeIcon.classList.remove("fa-eye-slash");
        eyeIcon.classList.add("fa-eye");
    } else {
        passwordInput.type = "password";
        eyeIcon.classList.remove("fa-eye");
        eyeIcon.classList.add("fa-eye-slash");
    }
}
*/

// Tıklama işlevselliğini içeren kodu çalıştırın
const togglePassword = document.getElementById('eye-icon');
togglePassword.addEventListener('click', function () {
    const password = document.getElementById('password');
    const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);
    this.classList.toggle('fa-eye-slash');
});


console.log("togglePasswordVisibility çalışıyor");



