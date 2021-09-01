document.getElementById("login").addEventListener("click", (event) => {
    login();
});

document.getElementById("password").addEventListener("keyup", (event) => {
    if (event.key) {
        if (event.key === 'Enter') {
            login();
        }
    } else if (event.keyCode) {
        if (event.keyCode === 13) {
            login();
        }
    }
});

document.getElementById("show-pwd").addEventListener('mousedown', (event) => {
    document.getElementById("password").type = "text";
});

document.getElementById("show-pwd").addEventListener('mouseup', (event) => {
    document.getElementById("password").type = "password";
});

function login() {
    var failedAuth = document.getElementById('failed-auth');
    axios({
        method: 'post',
        url: '/iot/patlego/servlet/authenticate.action',
        data: {
            "username": document.getElementById("username").value,
            "password": document.getElementById("password").value
        }
    }).then((resp) => {
        failedAuth.classList.add('hidden');
        document.getElementById('failed-auth').style.display = "none";
        window.location.href = `/iot/patlego/events.html?token=${resp.data.token}`;
    }).catch((error) => {
        failedAuth.classList.remove('hidden');
        console.error("Failed to authenticate please validate");
    });
}