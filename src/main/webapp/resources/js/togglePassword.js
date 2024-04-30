/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

//login sayfasının togglePassword fonksiyonu
function togglePasswordVisibilityLogin() {
    var passwordElement = document.getElementById('loginForm:password'); // JSF tarafından oluşturulan gerçek ID'yi buraya yazın
    var eyeIcon = document.getElementById('eye-icon');
    if (!passwordElement) {
        console.error('Password elementi bulunamadı. Lütfen ID\'yi kontrol edin.');
        return;
    }
    if (passwordElement.type === 'password') {
        passwordElement.type = 'text';
        eyeIcon.classList.remove('fa-eye-slash');
        eyeIcon.classList.add('fa-eye');
    } else {
        passwordElement.type = 'password';
        eyeIcon.classList.remove('fa-eye');
        eyeIcon.classList.add('fa-eye-slash');
    }
}

window.addEventListener('load', function () {
    document.getElementById('loginForm').reset();
    document.getElementById('eye-icon').addEventListener('click', function () {
        console.log('Göz simgesine tıklandı');
        togglePasswordVisibilityLogin();
    });
});


window.addEventListener('load', function () {
    document.getElementById('loginForm').reset();
    document.getElementById('eye-icon').addEventListener('click', function () {
        console.log('Göz simgesine tıklandı');
        togglePasswordVisibilityLogin();
    });
});


//register sayfasının togglePassword fonksiyonu
function togglePasswordVisibilityRegister() {
    // ID'ler gerçek sayfa ID'lerine göre güncellendi
    var passwordElement = document.getElementById('registerForm:password');
    var confirmPasswordElement = document.getElementById('registerForm:confirmPassword');
    var eyeIcon = document.querySelector('.password-toggle i');

    if (!passwordElement || !confirmPasswordElement) {
        console.error('Password elementleri bulunamadı. Lütfen ID\'leri kontrol edin.');
        return;
    }

    if (passwordElement.type === 'password') {
        passwordElement.type = 'text';
        confirmPasswordElement.type = 'text';
        eyeIcon.classList.remove('fa-eye-slash');
        eyeIcon.classList.add('fa-eye');
    } else {
        passwordElement.type = 'password';
        confirmPasswordElement.type = 'password';
        eyeIcon.classList.remove('fa-eye');
        eyeIcon.classList.add('fa-eye-slash');
    }
}

window.addEventListener('load', function () {
    document.getElementById('registerForm').reset(); // Form ID'nizi doğru şekilde güncelleyin
    var toggleIcons = document.querySelectorAll('.password-toggle i');
    toggleIcons.forEach(icon => {
        icon.addEventListener('click', togglePasswordVisibilityLogin);
    });
});