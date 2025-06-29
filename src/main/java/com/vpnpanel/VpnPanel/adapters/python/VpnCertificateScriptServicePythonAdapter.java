package com.vpnpanel.VpnPanel.adapters.python;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vpnpanel.VpnPanel.adapters.exceptions.VpnCertificateScriptException;
import com.vpnpanel.VpnPanel.application.ports.VpnCertificateScriptService;

import lombok.extern.slf4j.Slf4j;

@Slf4j // Log
public class VpnCertificateScriptServicePythonAdapter implements VpnCertificateScriptService {

    /**
     * Cria um certificado VPN para o usuário especificado.
     *
     * @param userNickname O apelido do usuário para o qual o certificado será criado.
     * @return O nome do arquivo do certificado gerado.
     * @throws VpnCertificateScriptException Se ocorrer um erro ao criar o certificado.
     */
    @Override
    public String createVpnCertificate(String userNickname) {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "/etc/openvpn/scripts/create_vpn_client.py",
                userNickname);
        processBuilder.redirectErrorStream(true); // Redireciona o erro para a saida padrao

        try {
            log.info("Criando certificado VPN para o usuário: {}", userNickname);

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                try {
                    return getLastOutputProcess(process);
                } catch (RuntimeException e) {
                    log.error("Erro ao obter nome do certificado VPN: {}", e.getMessage());

                    throw new VpnCertificateScriptException("Erro ao obter o nome do certificado: ", e);
                }
            } else {
                String errorMessage = getLastOutputProcess(process);

                log.error("Erro ao criar o certificado para o usuário {}: {}", userNickname, errorMessage);

                throw new VpnCertificateScriptException(
                        "Erro ao criar o certificado para o usuário " + userNickname + ": " + errorMessage);
            }
        } catch (IOException e) {
            log.error("Erro ao iniciar o processo para criar certificado VPN: {}", e.getMessage());

            throw new VpnCertificateScriptException("Erro ao iniciar o processo para criar certificado VPN", e);
        } catch (InterruptedException e) {
            log.error("O processo foi interrompido ao criar o certificado VPN: {}", e.getMessage());

            Thread.currentThread().interrupt(); // Restaura e repassa o estado da interrupcao para evitar erros futuros
            throw new VpnCertificateScriptException("O processo foi interrompido ao criar o certificado VPN", e);
        } catch (RuntimeException e) {
            log.error("Falha ao obter o erro do certificado para ao usuário: {}", e.getMessage());
            throw new VpnCertificateScriptException(
                    "Falha ao criar o certificado para ao usuário" + userNickname + "e obter erro do processo: ", e);
        }
    }

    /**
     * Lê a saída do processo e retorna a última linha.
     *
     * @param process O processo cujo output será lido.
     * @return A última linha da saída do processo.
     * @throws RuntimeException Se ocorrer um erro ao ler a saída do processo.
     */
    private String getLastOutputProcess(Process process) throws RuntimeException {
        // Pega apenas a ultima linha da saida do script
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line, lastLine = null;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            return lastLine != null ? lastLine.trim() : "";
        } catch (Exception e) {
            log.error("Erro ao ler a saída do processo: {}", e.getMessage());
            throw new RuntimeException("Erro ao ler a saída do processo", e);
        }
    }

    /**
     * Revoga o certificado VPN para o usuário especificado.
     *
     * @param userNickname O apelido do usuário cujo certificado será revogado.
     * @param file O arquivo do certificado a ser revogado.
     * @throws VpnCertificateScriptException Se ocorrer um erro ao revogar o certificado.
     */
    @Override
    public void revokeVpnCertificate(String userNickname, String file) {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "/etc/openvpn/scripts/revoke_vpn_client.py",
                userNickname,
                file);
        processBuilder.redirectErrorStream(true); // Redireciona o erro para a saida padrao

        try {
            log.info("Revogando certificado VPN para o usuário: {}", userNickname);
            log.info("Arquivo do certificado: {}", file);

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info(file + " - Certificado revogado com sucesso para o usuário: {}", userNickname);
            } else {

                String errorMessage = getLastOutputProcess(process);

                log.error("Erro ao revogar o certificado para o usuário {}: {}", userNickname, errorMessage);

                throw new VpnCertificateScriptException(
                        "Erro ao revogar o certificado para o usuário " + userNickname + ": " + errorMessage);
            }

        } catch (IOException e) {
            log.error("Erro ao iniciar o processo para revogar certificado VPN: {}", e.getMessage());

            throw new VpnCertificateScriptException("Erro ao iniciar o processo para revogar certificado VPN", e); 
        } catch (InterruptedException e) {
            log.error("O processo foi interrompido ao revogar o certificado VPN: {}", e.getMessage());

            Thread.currentThread().interrupt(); // Restaura e repassa o estado da interrupcao para evitar erros futuros
            throw new VpnCertificateScriptException("O processo foi interrompido ao revogar o certificado VPN", e);
        } catch (Exception e) {
            log.error("Erro ao executar o processo de revogação: {}", e.getMessage());
            throw new VpnCertificateScriptException("Erro ao executar o processo de revogação", e);
        }
    }

}
