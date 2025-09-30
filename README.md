# ToDo Simple - Spring Boot & Modern Frontend

ToDo Simple é um projeto simples e completo de autenticação e gerenciamento de tarefas, desenvolvido com backend em Spring Boot e frontend moderno em HTML, CSS e JavaScript.

## Funcionalidades
- Cadastro e login de usuários com validação de senha
- Autenticação JWT
- Interface responsiva e visual moderno
- Integração com banco de dados MySQL
- Estrutura de código organizada para fácil manutenção

## Tecnologias Utilizadas
- **Backend:** Java 17, Spring Boot 2.7, Maven, JWT, MySQL
- **Frontend:** HTML5, CSS3, JavaScript

## Estrutura do Projeto
```
├── src/main/java/com/lucasangeloSpring/...   # Backend Java
├── src/main/resources/application.properties # Configuração do banco/JWT
├── view/
│   ├── login.html                           # Página de login
│   ├── signup.html                          # Página de cadastro
│   ├── index.html                           # Página principal (após login)
│   ├── css/
│   │   ├── login.css                        # Estilo do login
│   │   └── signup.css                       # Estilo do cadastro
│   └── scripts/
│       ├── login.js                         # Lógica de login
│       └── signup.js                        # Lógica de cadastro
```

## Como rodar o projeto

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+ 
- MySQL 8.0+ ou Docker
- Git
- Navegador web moderno

### Opções de execução

## 🚀 Execução com Docker

### Passo 1: Clone o repositório
```bash
git clone https://github.com/felipegiannetti/spring-boot-course.git
cd spring-boot-course
```

### Passo 2: Configure as variáveis de ambiente
O arquivo `.env` já existe no projeto com as configurações padrão. Se necessário, você pode editá-lo:
```env
MYSQLDB_USER=root
MYSQLDB_ROOT_PASSWORD=root
MYSQLDB_DATABASE=todosimple
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8080
```

### Passo 3: Execute os comandos
```bash
# Subir os containers (irá compilar automaticamente)
docker-compose up -d

# OU executar apenas o backend localmente (após configurar MySQL)
mvn spring-boot:run

# Verificar se os containers estão rodando
docker-compose ps

# Ver logs da aplicação
docker-compose logs -f app

# Ver logs do MySQL
docker-compose logs -f mysqldb
```

### Passo 4: Acessar a aplicação
- Backend: http://localhost:8080
- Frontend: Abrir `view/login.html` no navegador

##  Comandos úteis do Docker

```bash
# Parar os containers
docker-compose down

# Parar e remover volumes (limpar dados)
docker-compose down -v

# Reconstruir as imagens
docker-compose build --no-cache

# Ver logs específicos
docker-compose logs mysql
docker-compose logs app

# Executar comando dentro do container
docker-compose exec mysql mysql -u root -p todosimple
docker-compose exec app bash
```

## 🔧 Troubleshooting

### Problema: Porta 8080 ocupada
```bash
# Ver qual processo está usando a porta
netstat -ano | findstr :8080  # Windows
lsof -i :8080                 # Mac/Linux

# Matar o processo (substitua PID pelo número encontrado)
taskkill /PID <PID> /F        # Windows
kill -9 <PID>                 # Mac/Linux
```

### Problema: Erro de conexão com MySQL
```bash
# Verificar se MySQL está rodando
docker-compose ps               # Se usando Docker
sudo service mysql status      # Linux
brew services list | grep mysql # Mac

# Testar conexão manual
mysql -u root -p -h localhost -P 3306
```

### Problema: CORS no frontend
- Certifique-se de que está abrindo o arquivo HTML diretamente no navegador
- Não use `http://localhost` para o frontend, use `file://`

## 📝 Comandos de desenvolvimento

```bash

# Executar o Spring Boot diretamente
mvn spring-boot:run

# Executar com profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Hot reload (desenvolvimento)
mvn spring-boot:run -Dspring-boot.run.fork=false

# Gerar JAR sem executar testes
mvn clean package -DskipTests
```

### URLs importantes
- **Backend API:** http://localhost:8080
- **Frontend:** Abrir `view/login.html` diretamente no navegador
- **Health Check:** http://localhost:8080/actuator/health


### Solução de Problemas
- **Erro de conexão com MySQL:** Verifique se o MySQL está rodando e as credenciais estão corretas
- **Porta 8080 ocupada:** Adicione `server.port=8081` no `application.properties`
- **Erro de CORS:** O frontend deve ser acessado via arquivo local, não via servidor web
- **Erro de autenticação JWT:** Verifique se o token está sendo enviado corretamente no header Authorization
- **Banco não cria automaticamente:** Execute `CREATE DATABASE todosimple;` manualmente no MySQL

## 🎯 Primeiros passos após instalar

1. **Teste o backend:**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

2. **Crie seu primeiro usuário:**
   - Abra `view/signup.html` no navegador
   - Cadastre um usuário com username e password
   - Ou use o endpoint via curl/Postman

3. **Faça login:**
   - Abra `view/login.html` no navegador  
   - Use as credenciais criadas
   - Você será redirecionado para a página de tarefas

4. **Gerencie suas tarefas:**
   - Crie, visualize e gerencie suas tarefas na interface

## Diferenciais
- Visual moderno e responsivo
- Validação de senha no cadastro
- Feedback visual claro para o usuário
- Código limpo e organizado

## Autor
Projeto criado por Felipe Giannetti Fontenelle, com apoio do curso Spring Boot Lucas Angelo.
