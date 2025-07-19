# VPN Panel - Arquitetura

## Arquitetura Hexagonal (Ports and Adapters)

Esta aplicação foi desenvolvida seguindo os princípios da **Arquitetura Hexagonal**, também conhecida como **Ports and Adapters**, visando o isolamento do `core` da aplicação contra tecnologias externas, isso significa que a lógica e as regras de negócios permanecem independentes de tecnologias externas, garantindo a alta coesão do software o e o alto desacoplamento entre regras de negócio e tecnologias externas.<br>
A arquitetura utiliza `portas` e `adaptadores` para realizar a comunicação do core da aplicação com tecnologias específicas, por isso o nome **Ports And Adapter**.

### Core
Contém as entidades do domínio, os casos de uso e seus comportamentos.

### Ports
Interfaces que permitem a comunicação entre o **core** da aplicação com as tecnologias externas.

### Adapters
Podem ser dividos em: **primários** (in) e **secundários** (out)<br>

Adaptadores primários são implementações das portas de entrada da aplicação, são por esses adaptadores que a aplicação receberá a comunicação com o mundo externo.

Adaptadores secundários são implementações das portas de saída que conectam a aplicação a sistemas externos ou tecnologias, permitindo que ela execute ações como persistência de dados, envio de e-mails e chamadas a APIs externas.

---

## Objetivos da Arquitetura

- Separar a lógica de negócio de implementações técnicas;
- Facilitar a manutenibilidade do software (baixo acoplamento);
- Tornar a aplicação adaptável a mudanças tecnológicas, como troca de banco, serviores VPN, protocolos de comunicação.

---

## Pacotes e Responsabilidades
Segue os pacotes principais e suas respectivas responsabilidades para este projeto. 

### `domain`

Camada do **núcleo** da aplicação:
- Contém as **entidades de domínio** (`models/`).
- Define os **comportamentos de negócio** diretamente nas entidades.
- Declara **exceptions** do domínio, representando erros relacionados à lógica de negócio (`exceptions/`).
- Contém os **enums** para os valores constantes de determinados atributos das entidades(`enums/`).
- Contém as classes para encapsulamento dos dados de entrada (`commands/`)
- Não depende de nenhuma tecnologia externa, garantindo portabilidade e isolamento.

---

### `application`

Contém o arranjo da lógica de negócio, coordenando o fluxo entre o domínio e os adaptadores:
- `services/`: Implementações dos casos de uso (`*UseCaseService`), contendo a lógica de coordenação entre entidades e portas.
- `ports/in`: Interfaces que representam os casos de uso. São utilizadas pelos controladores REST e outros adaptadores de entrada.
- `ports/out`: Interfaces que o core utiliza para se comunicar com as dependências externas.
- Totalmente independente de frameworks ou bibliotecas externas, mantendo a aplicação desacoplada.

> As classes de serviço nesta camada possuem uma anotação personalizada (`@UseCase`) para que sejam gerenciadas pelo Spring e executadas dentro de uma transação via AOP (Aspect Oriented Programming).

---

### `adapters`

Camada responsável por conectar o core com as tecnologias externas:

#### `in/rest`
- Controladores REST (`*Controller`) que recebem e respondem a requisições HTTP.
- Fazem mapeamento de `DTOs` para `commands` do domínio.

#### `out/jpa`
- Contém as implementações de persistência de dados.
- `entites`: entidades JPA para persistência no banco.
- `repositories`: Extensões do `JpaRepository`.
- `persistence`: Adapters que implementam os `PersistencePort` do core e utilizam os repositories.
- `mappers`: Conversores entre entidades JPA e modelos do domínio.

#### `out/openvpn` e `out/smtp`
- Adapters responsáveis por comunicação com serviços externos (OpenVPN, SMTP).
- Implementam portas de saída: `VpnCertificateScriptPort` e `MailPort`.

#### `security`
- Integração com Spring Security e JWT (JSON Web Token).
- Implementa autenticação e codificação de senhas via `AuthenticationPort` e `PasswordEncoderPort`.

---

### `config`
Contém configurações globais e as classes de inicialização de dados no banco:
- `SecurityConfig`: Configuração de autenticação, JWT, filtros.
- `DomainConfig`: Scan de beans `@UseCase`.
- `TransactionalUseCaseAspect`: Configuração do aspecto AOP para execução transacional dos serviços.
- `UseCase`: Anotação personalizada para identificar classes de serviços.

---

## Como os fluxos funcionam?

### Exemplo: Geração de Certificado

1. `CertificateController` recebe a requisição HTTP.
2. Converte para o usuário autenticado (`@AuthenticationPrincipal`) e chama a interface `CertificateUseCase`.
3. A implementação `CertificateUseCaseService` executa a lógica de geração do certificado e chama:
   - `VpnCertificateScriptPort`: executa script para gerar o certificado no servidor OpenVPN.
   - `CertificatePersistencePort`: salva o certificado no banco.
4. A resposta é enviada com os dados do certificado.

---

