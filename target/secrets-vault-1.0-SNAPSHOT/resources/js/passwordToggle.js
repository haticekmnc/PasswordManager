/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// passwordToggle.js

// JavaScript dosyasının başında 'use strict' kullanılmasını öneririm

'use strict';

console.log("JavaScript dosyası yüklendi.");

function togglePasswordVisibility() {
    console.log("togglePasswordVisibility fonksiyonu çağrıldı.");

    var passwordInput = document.getElementById('password');
    var eyeIcon = document.getElementById('eye-icon');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        eyeIcon.classList.remove('fa-eye-slash');
        eyeIcon.classList.add('fa-eye');
    } else {
        passwordInput.type = 'password';
        eyeIcon.classList.remove('fa-eye');
        eyeIcon.classList.add('fa-eye-slash');
    }
}

