function registerForNewsletter() {
    var xhr = new XMLHttpRequest();
    var email = document.getElementById("subscribe").value;


    xhr.open("POST", '/newsletter/' + email, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function () {
        if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            document.getElementById("newsletter-lbl").innerHTML = xhr.response;
        }
    }
    xhr.send("");
}