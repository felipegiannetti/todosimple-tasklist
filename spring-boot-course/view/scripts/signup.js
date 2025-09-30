async function signup() {
  let username = document.getElementById("username").value;
  let password = document.getElementById("password").value;
  let confirmPassword = document.getElementById("confirmPassword").value;

  if (password !== confirmPassword) {
    document.getElementById("response").style.color = "#f19999ff";
    document.getElementById("response").innerText = "As senhas não coincidem.";
    return;
  }

  console.log(username, password);

  const response = await fetch("http://localhost:8080/user", {
    method: "POST",
    headers: new Headers({
      "Content-Type": "application/json; charset=utf8",
      Accept: "application/json",
    }),
    body: JSON.stringify({
      username: username,
      password: password,
    }),
  });

  if (response.ok) {
    document.getElementById("response").style.color = "#4efc8d";
    document.getElementById("response").innerText = "Usuário criado com sucesso! Redirecionando para a página de login...";
    setTimeout(() => {
      window.location = "login.html";
    }, 2000);
  } else {
    document.getElementById("response").style.color = "#f19999ff";
    document.getElementById("response").innerText = "Erro ao criar usuário.";
  }
}
