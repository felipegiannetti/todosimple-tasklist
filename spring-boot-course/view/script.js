const url = "http://localhost:8080/task/user/";

async function getAPI(url) {
    const response = await fetch(url, {method: "GET"}); // await é tipo "espera carregar isso"

    var data = await response.json(); // transforma em json

    show(data);
}

// esconder o loader
async function hideLoader() {
    await new Promise(resolve => setTimeout(resolve, 2000)); // espera 2 segundos
    document.getElementById("loading").style.display = "none";
}

// exibir as tasks
function show(tasks) {
    let tab = ` 
    <thead>
        <th scope="col">#</th>
        <th scope="col">Description</th>
        <th scope="col">Username</th>
        <th scope="col">User Id</th>
    <thead>
    `;

    for(let task of tasks) {
        tab += `
        <tr>
            <td scope="row">${task.id}</td>
            <td scope="row">${task.description}</td>
            <td scope="row">${task.user.username}</td>
            <td scope="row">${task.user.id}</td>
        </tr>
        `;
    }

    document.getElementById("tasks").innerHTML = tab;
}

// inicia o app
async function startApp() {
    await hideLoader();
    getAPI();
}

// starta o app
startApp();