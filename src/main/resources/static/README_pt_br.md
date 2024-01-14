<!-- title -->
<h1 align="center">
    <span>Todo List API</span>
    <img src="./img/readme/todo_list_h1_image.svg" alt="todo list image icon" width="400px" align="center">
</h1>

<!-- badges -->
<div align="left">
    <img src="https://img.shields.io/badge/license-MIT-yellow" alt="badge icon"></img>
    <img src="https://img.shields.io/badge/version-1.0-green" alt="badge icon"></img>
    <img src="https://img.shields.io/badge/repo size-5.15 MB-orange" alt="badge icon"></img>
</div>

<!-- languages -->
<div align="right">
    <span><em>language</em></span>
    <!-- BR -->
    <a href="#">
        <img src="https://cdn.icon-icons.com/icons2/83/PNG/512/brazil_15818.png" alt="brazil flag icon" width="20x" align="center">
    </a>
    <!-- US -->
    <a href="../../../../README.md">
        <img src="https://cdn.icon-icons.com/icons2/3665/PNG/512/usa_flag_united_states_america_icon_228698.png" alt="america flag icon" width="20px" align="center">
    </a>
</div>

<!-- About -->
## <img src="https://cdn2.iconfinder.com/data/icons/flat-pack-1/64/Computer-512.png" alt="todo list image icon" width="40px" align="center"> Sobre o Projeto
Essa é uma todolist criada para manipular tarefas diárias. O Objetivo aqui é praticar as operações CRUD, usando como plano de fundo uma lista de tarefas.

### Como a API funciona?

1. O usuário vai criar uma conta.
2. O usuário vai criar uma categoria de tarefas.
3. Depois dessas 2 etapas, o usuário vai criar suas tarefas e gerenciá-las.

<hr>
<br>

<!-- Technologies -->
## <img src="https://cdn4.iconfinder.com/data/icons/general-office/91/General_Office_48-256.png" alt="todo list image icon" width="40px" align="center"> Tecnologias
- Java
- Maven
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- OpenAPI (Swagger)
- Spring Security
- Mockito and AssertJ

<hr>
<br>

<!-- Functionalities -->
## <img src="https://cdn2.iconfinder.com/data/icons/75-market-research-wildberry-vol-1/256/Guideline-256.png" alt="todo list image icon" width="40px" align="center"> Funcionalidades

- [x] Relacionamento entre Entidades
- [x] Autenticação (Basic Auth)
- [x] Create, Read, Update e Delete tasks
- [x] Validação de attributes inputados
- [x] Lancamento de exceções customizadas
- [x] Testes unitários, trabalhando com AssertJ e Mockito
- [x] Documentação no Swagger

<br>
<hr>

<!-- Diagram -->
### <img src="https://cdn3.iconfinder.com/data/icons/web-design-development-flat-colors/48/flow_chart-512.png" alt ="image icon" width="40px" align="center"> Diagrama de relacionamento entre Entidades

A aplicação trabalha com 4 entidades:

- User
- Role (*ADMIN*, *REGULAR_USER*)
- Category
- Tasks

![entities relationship diagram](./img/readme/entities_relationship_diagram.png)

<hr>
<br>

<!-- Authentication -->
### <img src="https://cdn3.iconfinder.com/data/icons/business-startup-57/64/289-512.png" alt ="image icon" width="40px" align="center"> Authentication (Basic Auth)
Quando falamos sobre autenticação básica, estamos falando sobre trabalhar com **usuarios** e **cargos/autoridades**.

Todas as rotas estão restritas e necessitam de autorização para serem acessadas. Além disso, o usuário precisa ter um cargo específico para conseguir acessá-las.

Nossa autenticação básica vai trabalhar com 2 tipos de cargos:

- **REGULAR_USER** -> Como o nome sugere, é um usuário normal/regular. Por padrão, toda vez que um usuário é criado, esse novo usuário recebe o cargo de *REGULAR_USER*.

<br>

- **ADMIN** -> Cargo especial que permite o usuário a acessar rotas 'sensíveis'. Por padrão, a aplicação vai criar um usuário chamado "admin" com a senha "123". Essa 'conta de admin padrao' vai ser responsável em dar cargos de *admin* para usuários normais. Depois de você já possuir usuários com cargo de admin, não é mais necessário utilizar essa 'conta de admin padrao'.

Trabalhando com usuários e cargos, nós garantimos que usuários consigam manipular apenas suas próprias tarefas. Não sendo permitido acessar informações de tarefas de outros usuários.

<br>

#### <img src="https://cdn4.iconfinder.com/data/icons/small-n-flat/24/image-256.png" alt ="image icon" width="40px" align="center"> Imagens de exemplo:

**403 - Forbidden**

![forbidden image example](./img/readme/forbidden_example.png)

<br>

**401 - Unauthorized**
![unauthorized image example](./img/readme/unauthorized_example.png)

<br>

**406 - Not Acceptable**
![information from other person image example](./img/readme/task_from_another_person_example.png)

<hr>
<br>

## <img src="https://cdn1.iconfinder.com/data/icons/internet-45/64/http-link-internet-domain-1024.png" alt ="image icon" width="40px" align="center"> Endpoints

### Person Controller

| Método Http | URI                                                               | Descrição                                   | Cargo necessário |
| :---:       | :---                                                              |  :---                                       |  :---:           | 
| POST        | `http://localhost:8080/v1/api/person`                             | Criar usuário                               | permissao total  |
| PUT         | `http://localhost:8080/v1/api/person/give-admin-permission/{id}`  | Dar permissao de admin para um usuario      | ADMIN            |
| GET         | `http://localhost:8080/v1/api/person`                             | Listar todos os usuarios                    | ADMIN            |

<br>

### Category Controller
| Método Http    | URI                                           | Descrição                         | Cargo necessário |
| :---:          | :---                                          |  :---                             |  :---:           | 
| POST           | `http://localhost:8080/v1/api/category`       | Criar categoria                   | REGULAR_USER     |
| GET            | `http://localhost:8080/v1/api/category`       | Listar todas as categorias        | REGULAR_USER     |
| GET            | `http://localhost:8080/v1/api/category/{id}`  | Ler uma categoria específica      | REGULAR_USER     |
| PUT            | `http://localhost:8080/v1/api/category/{id}`  | Atualizar categoria               | REGULAR_USER     |
| DELETE         | `http://localhost:8080/v1/api/category/{id}`  | Deletar categoria                 | REGULAR_USER     |

<br>

### Task Controller
| Método Http    | URI                                                | Descrição                       | Cargo necessário |
| :---:          | :---                                               |  :---                           |  :---:           | 
| POST           | `http://localhost:8080/v1/api/task`                | Criar tarefa                    | REGULAR_USER     |
| GET            | `http://localhost:8080/v1/api/task`                | Listar todas as tarefas         | REGULAR_USER     |
| PUT            | `http://localhost:8080/v1/api/task/{id}`           | Atualizar tarefa                | REGULAR_USER     |
| DELETE         | `http://localhost:8080/v1/api/task/{id}`           | Deletar tarefa                  | REGULAR_USER     |

<br>

⚠️ Como voce pode perceber acima, a única rota que você consegue acessar sem precisar de autenticação é a rota para criar um novo usuário. Para acessar qualquer outra rota, você precisa estar autenticado. Caso contrário, você irá receber o erro 401 - Unauthorized.

<hr>
<br>

<!-- Validations -->
## <img src="https://cdn4.iconfinder.com/data/icons/rating-validation-3/128/validation_stamp_approval_approve_check-512.png" alt ="image icon" width="40px" align="center"> Validações
Para evitar qualquer problema, todos os atributos foram validados.

As validações implementadas foram:
### Validações de input da entidade (Person)

-  **CREATE**
    - 'username':
        1. username não pode ser nulo
		2. username não pode ter espaços em branco
		3. username deve possuir entre 5 e 20 caracteres
		4. username deve ser único

    <br>

    - 'password':
        1. password não pode ser nulo
		2. password deve possuir entre 8 e 20 caracteres
		3. password não pode ter espaços em branco
		4. password deve possuir pelo menos 1 letra MAIÚSCULA
		5. password deve possuir pelo menos 1 número
		6. password deve ter pelo menos 2 caraceteres especiais ('!', '@', '#' '=', etc...)
<br>

### Validações de input da entidade (Category)
- **CREATE**
    - 'name':
        1. name não pode ser nulo
        2. name deve possuir entre 1 e 20 caracteres
        3. name deve ser único

<br>

- **UPDATE**
    - 'name':
        1. name deve possuir entre 1 e 20 caracteres
        2. name deve ser único

<br>

### Validações de input da entidade (Task)
- **CREATE**
    - 'title':
        1. title não pode ser nulo
        2. title deve possuir entre 1 e 50 caracteres
        3. title deve ser único

    <br>

    - 'description':
        1. description não pode ser nulo e deve possuir entre 0 e 100 caracteres

    <br>

    - 'priority':
        1. priority não pode ser nulo
		2. priority o número deve ser entre 1 e 3
		3. priority não pode ser uma String

    <br>

     - 'category_id':
        1. category_id não pode ser nulo
        2. category_id não pode ser uma String
        3. category_id não pode ser de outro usuário

	<br>

- **UPDATE**
    - 'title':
        1. title deve possuir entre 1 e 50 caracteres
        2. title deve ser único

    <br>

    - 'description' attribute
        1. description não pode ser nulo e deve possuir entre 0 e 100 caracteres

    <br>

    - 'priority'
        1. priority o número deve ser entre 1 e 3
	    2. priority não pode ser uma String

    <br>
    
    - 'category_id' attribute
        1. category_id não pode ser uma String
        2. category_id não pode ser de outro usuário
<hr>
<br>

<!-- Custom Exception Handler -->
## <img src="https://cdn4.iconfinder.com/data/icons/common-app-symbols-round-colored/1024/caveat_proviso_disclaimer_exception_app_round_colored-512.png" alt ="image icon" width="40px" align="center"> Lancamento de exceções customizadas

Todas as exceções foram personalizadas para um melhor entendimento do usuário

**Example 1**
![information from other person image example](./img/readme/username_wrong_example.png)	
	
**Example 2**
![information from other person image example](./img/readme/password_wrong_example.png)	

<hr>
<br>

## Documentação no Swagger

![information from other person image example](./img/readme/swagger_documentation.png)	

<br>

For each operation, we show all the possible responses
![information from other person image example](./img/readme/swagger_possible_responses.png)	

<hr>
<br>

<!-- Build and run -->
## <img src="https://cdn3.iconfinder.com/data/icons/start-up-4/44/rocket-256.png" alt="todo list image icon" width="40px" align="center"> Rodando a aplicação

### Requisitos
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [PostgreSQL 16](https://www.postgresql.org/download/)
- [git 2.34.1](https://git-scm.com/download)

<br>

### Passo a passo

1. Clone esse repositório para um repositório local na sua máquina
    ```bash
    git clone git@github.com:lGabrielDev/todolist_backend.git
    ```
<br>

2. Configure o arquivo 'application.properties':

    ```properties
    # Database path
    spring.datasource.url=jdbc:postgresql://localhost:5432/banco_de_dados

    # postgres username
    spring.datasource.username=seuUsuarioPostgres

    # postgres password
    spring.datasource.password=suaSenhaPostgres

    # Every time we update a entity, the reference table will update as well
    spring.jpa.hibernate.ddl-auto=update

    # Show the SQL commands
    spring.jpa.show-sql=true

    # Swagger sorted operations
    springdoc.swagger-ui.operationsSorter=method
    ```
<br>

3. Rode a aplicação e acesse a rota: `http://localhost:8080/swagger-ui/index.html`

<br>

4. Crie um usuário

5. Crie uma categoria

6. Crie suas tarefas e se divirta! 😎

<br>

<div>
    <span>Se ainda tiver dúvidas, assista o video tutorial aqui</span>
    <a href="https://www.youtube.com/watch?v=IGdVJ5rxb6o&ab_channel=lGabrielDev">
        <img src="https://cdn1.iconfinder.com/data/icons/logotypes/32/youtube-256.png" alt="video icon" width="30px" align="center">
    </a>
</div>

<hr>
<br>

<!-- License -->
## <img src="https://cdn4.iconfinder.com/data/icons/jetflat-2-multimedia-vol-3/60/0042_049_license_agreement_file_document_paper_page_sheet-512.png" alt="todo list image icon" width="40px" align="center"> Licença --> MIT

O projeto está sob a licença do [MIT](../../../../LICENSE.txt).

<hr>
<br>

<!-- Author -->
## <img src="https://cdn1.iconfinder.com/data/icons/office-work-3/200/copywriting-512.png" alt="todo list image icon" width="40px" align="center"> Autor

<br>

<div align="center">
    <img src="./img/readme/profile_circular.png" alt="profile avatar" width="150px">
    <p> <a href="https://github.com/lGabrielDev">Gabriel Freitas</a> 😎 </p>
</div>
