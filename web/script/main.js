function displayMessage(message) {
    document.getElementById('message').innerText = message;
}

function renderUrl(url, src) {
    let tmp = document.getElementById('template').innerHTML;
    Mustache.parse(tmp);
    let rendered = Mustache.render(tmp, {'href': url, 'src': src})
    let row = document.createElement('tr');
    row.innerHTML = rendered;
    document.getElementById('links').prepend(row);
}

function displayData(data) {
    let key = (data => {
        for (k in data) return k;
    })(data);
    displayMessage("Loading finished! fetched " + data[key].length + " urls.")
    for (let i = 0; i < data[key].length; i++) {
        renderUrl(data[key][i], key);
    }
}

async function submit(e) {
    e.preventDefault();
    displayMessage("loading...")
    try {
        let response = await fetch("/api/process?url=" + e.target[0].value, {method: "POST"});
        let data = await response.json();
        displayData(data)
    } catch (e) {
        displayMessage("fetching error")
    }
}

function ready() {
    document.getElementById('url-form').addEventListener('submit', submit);
}

document.addEventListener('DOMContentLoaded', ready)