/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


// passwordToggle.js

// JavaScript dosyasının başında 'use strict' kullanılmasını öneririm



'use strict';

console.log("JavaScript dosyası yüklendi.");

function togglePasswordVisibility(passwordFieldId, toggleButtonId) {
    var passwordField = document.getElementById(passwordFieldId);
    var toggleButton = document.getElementById(toggleButtonId);

    if (passwordField.type === 'password') {
        passwordField.type = 'text';
        toggleButton.classList.remove('pi-eye-slash');
        toggleButton.classList.add('pi-eye');
    } else {
        passwordField.type = 'password';
        toggleButton.classList.remove('pi-eye');
        toggleButton.classList.add('pi-eye-slash');
    }
}




