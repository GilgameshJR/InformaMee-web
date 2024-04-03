document.addEventListener("DOMContentLoaded", function () {
    var PageOrchestratorInst = PageOrchestrator();
    PageOrchestratorInst.start();
});

function PageOrchestrator() {
    var UserId = document.getElementById("id");
    var UserPwd = document.getElementById("pwd");
    var Form = document.getElementById("formtosub");
    var ErrorBox = document.getElementById("errorbox");

    function CheckCompiled() {
        var UserIdStr = UserId.value.trim();
        ErrorBox.innerHTML = "";
        if (UserIdStr.length === 0 || UserPwd.value.trim().length === 0) {
            ErrorBox.innerHTML = "Inserisci ID e password";
            return false;
        }
        if (isNaN(UserIdStr)) {
            ErrorBox.innerHTML = "L'ID deve essere esclusivamente numerico";
            return false;
        }
        return true;
    }

    function PostCredentials() {
        ErrorBox.innerHTML = "Validazione in corso";
        var url = "login";
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 401)
                    ErrorBox.innerHTML = xhr.responseText;
                else if (xhr.status === 200) {
                    window.location.replace(xhr.responseText);
                }
            }
        };
        //        xhr.send(UserId.localName+"="+UserId.value+"&"+UserPwd.localName+"="+UserPwd.value);
        xhr.send("id=" + UserId.value + "&pwd=" + UserPwd.value);
    }

    function registerListeners() {
        Form.addEventListener("submit", function (event) {
            event.preventDefault();
            event.stopPropagation();
            if (CheckCompiled()) {
                PostCredentials();
            }
        });
    }

    return {
        start: function () {
            registerListeners();
        }
    }
}