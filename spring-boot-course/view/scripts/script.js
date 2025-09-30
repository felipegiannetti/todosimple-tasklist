const tasksEndpoint = "http://localhost:8080/task/user";
const taskEndpoint = "http://localhost:8080/task";

function hideLoader() {
  document.getElementById("loading").style.display = "none";
}

function showMessage(message, isError = false) {
  const responseDiv = document.getElementById("taskResponse");
  if (responseDiv) {
    responseDiv.style.color = isError ? "#f19999ff" : "#4efc8d";
    responseDiv.innerText = message;
    setTimeout(() => {
      responseDiv.innerText = "";
    }, 3000);
  } else {
    console.log(message);
  }
}

function show(tasks) {
  let tab = `<thead>
            <th scope="col">#</th>
            <th scope="col">Descrição</th>
        </thead>`;

  if (tasks.length === 0) {
    tab += `
      <tr>
        <td colspan="2" class="text-center text-muted">
          Nenhuma tarefa encontrada. Adicione uma nova tarefa acima!
        </td>
      </tr>
    `;
  } else {
    for (let task of tasks) {
      tab += `
              <tr>
                  <td scope="row">${task.id}</td>
                  <td>${task.description}</td>
              </tr>
          `;
    }
  }

  document.getElementById("tasks").innerHTML = tab;
}

async function getTasks() {
  let key = "Authorization";
  const token = localStorage.getItem(key);
  
  if (!token) {
    window.location = "login.html";
    return;
  }

  try {
    const response = await fetch(tasksEndpoint, {
      method: "GET",
      headers: new Headers({
        Authorization: token,
      }),
    });

    if (response.status === 401) {
      localStorage.removeItem(key);
      window.location = "login.html";
      return;
    }

    var data = await response.json();
    console.log(data);
    if (response) hideLoader();
    show(data);
  } catch (error) {
    console.error("Erro ao carregar tarefas:", error);
    showMessage("Erro ao carregar tarefas. Tente novamente.", true);
    hideLoader();
  }
}

async function addTask() {
  const taskInput = document.getElementById("newTaskInput");
  const description = taskInput.value.trim();
  
  if (!description) {
    showMessage("Digite uma descrição para a tarefa!", true);
    return;
  }

  const addBtn = document.getElementById("addTaskBtn");
  addBtn.disabled = true;
  addBtn.innerText = "Adicionando...";

  let key = "Authorization";
  const token = localStorage.getItem(key);

  try {
    const response = await fetch(taskEndpoint, {
      method: "POST",
      headers: new Headers({
        "Content-Type": "application/json",
        Authorization: token,
      }),
      body: JSON.stringify({
        description: description
      }),
    });

    if (response.ok) {
      showMessage("Tarefa adicionada com sucesso!");
      taskInput.value = "";
      getTasks(); 
    } else if (response.status === 401) {
      localStorage.removeItem(key);
      window.location = "login.html";
    } else {
      showMessage("Erro ao adicionar tarefa. Tente novamente.", true);
    }
  } catch (error) {
    console.error("Erro ao adicionar tarefa:", error);
    showMessage("Erro ao adicionar tarefa. Verifique sua conexão.", true);
  } finally {
    addBtn.disabled = false;
    addBtn.innerText = "➕ Adicionar";
  }
}

function logout() {
  localStorage.removeItem("Authorization");
  window.location = "login.html";
}

// Adicionar funcionalidade de Enter para adicionar tarefa
document.addEventListener("DOMContentLoaded", function (event) {
  if (!localStorage.getItem("Authorization")) {
    window.location = "login.html";
    return;
  }

  // Permitir adicionar tarefa pressionando Enter
  const taskInput = document.getElementById("newTaskInput");
  if (taskInput) {
    taskInput.addEventListener("keypress", function(event) {
      if (event.key === "Enter") {
        addTask();
      }
    });
  }
});

getTasks();