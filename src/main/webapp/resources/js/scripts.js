// Parola gösterme/gizleme ve şifre işlevlerini yöneten fonksiyonlar
function togglePasswordVisibility(passwordId) {
    var passwordElement = document.getElementById('password_' + passwordId);
    var isPasswordVisible = passwordElement.classList.contains('visible-password');
    console.log("Parola kimliği için görünürlüğü değiştirme:", passwordId);

    if (isPasswordVisible) {
        console.log("Şifre şu anda görünür durumda. GİZLE.");
        passwordElement.classList.remove('visible-password');
        passwordElement.textContent = '********';
    } else {
        console.log("Şifre şu anda gizli. GÖSTER!.");
        passwordElement.classList.add('visible-password');
        passwordElement.textContent = passwordElement.dataset.password;
        hidePasswordAutomatically(3);
    }
}

// Sayfa değişikliği algılandığında, otomatik olarak gizlenen şifreyi durdur
window.addEventListener('beforeunload', function () {
    clearTimeout(timeoutHidePassword);
});

// Şifrenin otomatik olarak gizlenmesini sağlayan zamanlayıcı
var timeoutHidePassword;
function hidePasswordAutomatically(timeout) {
    console.log("Otomatik şifre gizleme zamanlaması", timeout, "saniye.");
    timeoutHidePassword = setTimeout(() => {
        var passwordFields = document.querySelectorAll('.password-visible');
        passwordFields.forEach(field => {
            field.textContent = '********';
            field.classList.remove('password-visible');
            field.classList.add('password-hidden');
        });
        document.querySelectorAll('.pi-eye').forEach(icon => {
            icon.classList.remove('pi-eye');
            icon.classList.add('pi-eye-slash');
        });
    }, timeout * 1000);
}

// Oturum zaman aşımı uyarıları ve yönlendirmeler
var timeoutWarning;
var timeoutLogout;
var logSessionTimeout;

window.onload = function () {
    resetTimer();
    // Önceki oturum zaman aşımı logunu engelle
    clearTimeout(logSessionTimeout);
    // Önceki oturum zaman aşımı uyarılarını ve oturum sonlandırma işlemlerini durdur
    clearTimeout(timeoutWarning);
    clearTimeout(timeoutLogout);
    var usernameElement = document.getElementById('username');
    var passwordElement = document.getElementById('password');
    if (usernameElement) usernameElement.value = '';
    if (passwordElement) passwordElement.value = '';
};

document.addEventListener("DOMContentLoaded", function () {
    resetTimer();
    // Önceki oturum zaman aşımı logunu engelle
    clearTimeout(logSessionTimeout);
    // Önceki oturum zaman aşımı uyarılarını ve oturum sonlandırma işlemlerini durdur
    clearTimeout(timeoutWarning);
    clearTimeout(timeoutLogout);
});

// Sayfa değişikliği algılandığında, otomatik olarak gizlenen şifreyi durdur
window.onbeforeunload = function () {
    clearTimeout(timeoutWarning);
    clearTimeout(timeoutLogout);
};

// Oturum zaman aşımı uyarıları ve oturumu sonlandırma işlemlerini durdur
function handleSessionTimeout() {
    timeoutWarning = setTimeout(() => {
        alert('Oturumunuz 1 saat içinde sona erecek. Oturumu yenilemek için herhangi bir sayfayı yenileyin.');
    }, 3600000); // 1 saat sonra uyarı
    timeoutLogout = setTimeout(() => {
        window.location.href = "login.xhtml"; // Oturum sonlandırma ve logout sayfasına yönlendirme
    }, 3660000); // 1 saat 1 dakika sonra logout
}

// Kullanıcı etkinliğini izleyerek oturum zamanlayıcılarını sıfırlama
function resetTimer() {
    clearTimeout(timeoutWarning);
    clearTimeout(timeoutLogout);
    handleSessionTimeout(); // Zamanlayıcıları yeniden başlat
}

document.querySelectorAll('.auto-expand').forEach(field => {
    field.addEventListener('input', function () {
        this.style.height = 'auto';
        this.style.height = this.scrollHeight + 'px';
    });
    autoExpandField(field); // Başlangıçta da boyutlandır
});

// autoExpandField fonksiyonunun tanımlanması
function autoExpandField(field) {
    field.style.height = 'auto';
    field.style.height = (field.scrollHeight) + 'px';
}

function logCheckboxClick() {
    console.log('Checkbox clicked!');
}
