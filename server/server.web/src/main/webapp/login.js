document.getElementById("login").addEventListener("click", (event) => {
    axios({
        method: 'post',
        url: '/iot/patlego/servlet/authenticate.action',
        data: {
            "username": document.getElementById("username").value,
            "password": document.getElementById("password").value
        }
    }).then((resp) => {
        window.iot.data.token = resp.data.token;
        window.location.href = `/iot/patlego/events.html?token=${window.iot.data.token}`;
    }).catch((error) => {
        console.error("Failed to authenticate please validate");
    });
});