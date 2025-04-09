Projeto Gestao Treinamentos Back-End
====================================

Este é um projeto back-end desenvolvido em Spring utilizando Java SDK 21.
O projeto é gerenciado e executado com IntelliJ IDEA e Maven 3.8.8, e é
containerizado utilizando Docker para facilitar o deploy. Além disso, há
instruções sobre como debugar a aplicação via container Docker dentro do IntelliJ.

Sumário
-------
- Pré-requisitos
  - Java 21
  - IntelliJ IDEA
  - Maven 3.8.8
  - Docker no Windows
- Instalação e Configuração
  - Instalação do Maven
  - Instalação do Docker para Windows
- Build e Deploy com Docker
  - Gerando o .jar com Maven
  - Gerar a Imagem Docker
  - Taguear e Enviar a Imagem para o Docker Hub
- Dockerfile e Depuração via Container Docker no IntelliJ

Pré-requisitos
--------------
1. **Java 21**
   - Instale o JDK 21 e configure a variável de ambiente `JAVA_HOME`.

2. **IntelliJ IDEA**
   - Utilize o IntelliJ IDEA para abrir, editar e executar o projeto.

3. **Maven 3.8.8**
   - Ferramenta de build e gerenciamento de dependências.

4. **Docker no Windows**
   - É necessário ter o Docker Desktop instalado e configurado no ambiente Windows.

Instalação e Configuração
-------------------------
### Instalação do Maven
1. **Baixe o Maven:**
   - Acesse [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) e baixe a versão 3.8.8.

2. **Extraia os Arquivos:**
   - Extraia os arquivos para um diretório, por exemplo:
     `C:\Program Files\Apache\Maven`.

3. **Configure as Variáveis de Ambiente:**
   - Crie uma variável de ambiente chamada `MAVEN_HOME` que aponte para o diretório onde o Maven foi extraído.
   - Adicione `%MAVEN_HOME%\bin` à variável `PATH`.

4. **Verifique a Instalação:**
   - Abra o terminal e execute:
     ```
     mvn -version
     ```
   - Você deverá ver informações sobre a versão do Maven, Java e o ambiente configurado.

### Instalação do Docker para Windows
1. **Baixe o Docker Desktop:**
   - Acesse [https://docs.docker.com/desktop/install/windows-install/](https://docs.docker.com/desktop/install/windows-install/) e faça o download do instalador.

2. **Instale o Docker Desktop:**
   - Execute o instalador e siga as instruções. Habilite a integração com o WSL 2, se necessário, e reinicie o sistema se solicitado.

3. **Verifique a Instalação:**
   - Abra o Docker Desktop e verifique se ele está rodando.
   - No terminal, execute:
     ```
     docker --version
     ```

Build e Deploy com Docker
--------------------------
### Gerando o .jar com Maven
Dentro do IntelliJ IDEA:
1. Abra o painel do Maven.
2. Execute o comando **clean** para limpar builds anteriores.
3. Execute o comando **package** para gerar o arquivo `.jar` do projeto.

### Gerar a Imagem Docker
Após gerar o `.jar`, execute o comando abaixo no terminal do IntelliJ (certifique-se de que o Docker Desktop esteja ativo):

Para gerar a imagem para o ambiente de homologação:
   docker build -t gestao-treinamentos-back-homolog:latest .

Taguear e Enviar a Imagem para o Docker Hub
-------------------------------------------
1. Taguear a imagem (substitua SEU_NOME_DE_USUÁRIO pelo seu nome de usuário no Docker Hub):
   docker tag gestao-treinamentos-back-homolog SEU_NOME_DE_USUÁRIO/gestao-treinamentos-back-homolog:latest

2. Enviar a imagem para o Docker Hub:
   docker push SEU_NOME_DE_USUÁRIO/gestao-treinamentos-back-homolog:latest

Observação: Os comandos de build, tagueamento e push devem ser executados dentro do terminal do IntelliJ, com o Docker Desktop instalado e ativo no Windows.

Dockerfile e Depuração via Container Docker no IntelliJ
--------------------------------------------------------
O seguinte Dockerfile é utilizado para containerizar a aplicação com depuração remota habilitada:

------------------------------------------------------------
FROM openjdk:21

# Copia o JAR da aplicação para o container
COPY target/treinamento-1.0.0.jar /app/app.jar

# Define o diretório de trabalho
WORKDIR /app

# Comando para rodar a aplicação com depuração habilitada
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar", "--spring.profiles.active=prod"]
------------------------------------------------------------

### Como Debugar a Aplicação via Docker no IntelliJ

1. **Executar o Container com Depuração:**
- Após gerar o `.jar` e criar a imagem Docker, inicie o container mapeando a porta de depuração:
  ```
  docker run -p 8080:8080 -p 5005:5005 treinamento-prod:latest
  ```
- Com esse comando, a porta **5005** do container será mapeada para a porta **5005** do host, permitindo conexão remota para debug.

2. **Criar Configuração de Depuração no IntelliJ:**
- No IntelliJ, vá em **Run > Edit Configurations...**.
- Clique no botão **"+"** para adicionar uma nova configuração e selecione **Remote JVM Debug** (ou **Remote**, dependendo da versão do IntelliJ).
- Configure a nova configuração:
  - **Name:** Por exemplo, "Debug Docker Container".
  - **Host:** Insira `localhost` (ou o IP correspondente, se o container estiver em outro ambiente).
  - **Port:** Insira `5005` (a mesma porta configurada no Dockerfile).
- Salve a configuração.

3. **Iniciar a Sessão de Depuração:**
- Com o container rodando, selecione a configuração criada e clique em **Debug**.
- O IntelliJ se conectará à JVM do container, possibilitando o uso de breakpoints e a inspeção de variáveis para depurar a aplicação normalmente.

Observação Final: O projeto ao fim de sua inicialização faz uma verificação nas tabelas, ajustando dados para não haver inserção manual, inclusive de um perfil
base para testes, com o usuário: "Administrador" email:"admin@teste.com.br", e a Senha: "teste@1234", para realizar as requisições de token via PostMan/Insomnia ou
para demais testes no Front-End a tabela de perfil_nivel_permissao e perfil_permissao_perfil será preenchida.