@echo off
:: habilita o acesso de variaveis dentro do loop
setlocal EnableDelayedExpansion

if not exist .env (
    echo ERRO: Arquivo .env não encontrado
    exit /b 1
)

echo Carregando variaveis de ambiente

:: le o arquivo linha por linha
for /f "usebackq tokens=* delims=" %%a in (.env) do (
    set "line=%%a"

    :: ignora linhas em branco e comentarios (# ou ;)
    if not "!line!"=="" if not "!line!"==" " if not "!line:~0,1!"=="#" if not "!line:~0,1!"==";" (
        set "!line!"
        echo "!line!"
    )
)

:: altere o comando para ficar compativel com o SGBD desejado OU comente esse trecho e faca manualmente
echo Inicializando banco de dados
mysql -u root -p -P 3306 < init-db.sql

echo Iniciando aplicacao 

:: mvn clean install
mvn spring-boot:run

endlocal