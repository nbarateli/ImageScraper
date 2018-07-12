function submit(e) {
    e.preventDefault();
}

function ready() {
    document.getElementById('url-form').addEventListener('submit', submit);
}

document.addEventListener('DOMContentLoaded', ready)