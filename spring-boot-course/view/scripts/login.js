async function login() {
  let username = document.getElementById("username").value;
  let password = document.getElementById("password").value;

  console.log(username, password);

  const response = await fetch("http://localhost:8080/login", {
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

  let key = "Authorization";
  let token = response.headers.get(key);
  window.localStorage.setItem(key, token);

  if (response.ok) {
  document.getElementById("response").style.setProperty("color", "#4efc8d", "important");
    document.getElementById("response").innerText = "Login realizado com sucesso! Redirecionando...";
    setTimeout(() => {
      window.location = "index.html";
    }, 2000);
  } else {
  document.getElementById("response").style.setProperty("color", "#f19999ff", "important");
    document.getElementById("response").innerText = "Usuário ou senha inválidos. Tente novamente.";
  }
}
