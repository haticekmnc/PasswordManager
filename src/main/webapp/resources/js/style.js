/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

/* password list sayfasında yeni password ekleme alanlarına metin yazarken alanın stilerri için */
document.addEventListener("DOMContentLoaded", function() {
    var textAreas = document.querySelectorAll('.auto-expand');
    textAreas.forEach(function(textArea) {
        function autoExpand() {
            textArea.style.height = 'inherit';
            textArea.style.height = `${textArea.scrollHeight}px`;
        }

        textArea.addEventListener('input', autoExpand);
        autoExpand(); // İlk yükleme için genişlet
    });
}); 


$(document).ready(function() {
    function expandTextarea(id) {
        document.getElementById(id).addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = (this.scrollHeight) + 'px';
        }, false);
    }

    // Bu fonksiyon her dialog açıldığında çağrılmalıdır
   PF('editDialog').show = function() {
    var originalShow = PF('editDialog').show;
    return function() {
        originalShow.apply(this, arguments);
        expandTextarea('editDialogForm:title');
        expandTextarea('editDialogForm:url');
        expandTextarea('editDialogForm:username');
        expandTextarea('editDialogForm:password');
    };
};


    PF('addPasswordDialog').jq.on('pfAjaxComplete', function() {
        expandTextarea('addPasswordForm:title');
        expandTextarea('addPasswordForm:url');
        expandTextarea('addPasswordForm:username');
        expandTextarea('addPasswordForm:password');
    });
});
