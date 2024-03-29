/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// passwordToggle.js

// JavaScript dosyasının başında 'use strict' kullanılmasını öneririm



'use strict';

console.log("JavaScript dosyası yüklendi.");

function togglePasswordVisibility() {
    var passwordInput = document.getElementById('password');
    var eyeIcon = document.getElementById('eye-icon');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        eyeIcon.className = 'fas fa-eye';
    } else {
        passwordInput.type = 'password';
        eyeIcon.className = 'fas fa-eye-slash';
    }
}



