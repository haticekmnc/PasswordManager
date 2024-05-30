//login sayfasının togglePassword fonksiyonu
function togglePasswordVisibilityRegister() {
    var passwordFields = document.querySelectorAll('.password-input');
    passwordFields.forEach(function (field) {
        if (field.type === "password") {
            field.type = "text";
        } else {
            field.type = "password";
        }
    });

    var eyeIcons = document.querySelectorAll('.password-toggle i');
    eyeIcons.forEach(function (icon) {
        if (icon.classList.contains("fa-eye")) {
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        } else {
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        }
    });

    console.log('Toggle password visibility');
}
