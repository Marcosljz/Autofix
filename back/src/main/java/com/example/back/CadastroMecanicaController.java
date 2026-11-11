
package com.example.back;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CadastroMecanicaController {

    @FXML
    private TextField txtRazaoSocial;

    @FXML
    private TextField txtNomeFantasia;

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private TextField txtCpf;

    @FXML
    public void consultarCnpj() {

        if (SessaoController.token == null || SessaoController.token.isEmpty()) {
            showMessage(Alert.AlertType.ERROR, "Faça login como ADMIN primeiro!");
            return;
        }

        String cnpj = txtCnpj.getText().replaceAll("[^0-9]", "");

        if (cnpj.isEmpty()) {
            showMessage(Alert.AlertType.WARNING, "Informe o CNPJ!");
            return;
        }

        HttpURLConnection connection = null;

        try {
            URL url = new URL("http://localhost:8080/mecanicas/cnpj/" + cnpj);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty(
                    "Authorization",
                    "Bearer " + SessaoController.token
            );

            int respostaApi = connection.getResponseCode();

            if (respostaApi == 200) {

                ObjectMapper objectMapper = new ObjectMapper();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "UTF-8"))) {

                    var resposta = objectMapper.readTree(reader);

                    txtRazaoSocial.setText(resposta.get("razaoSocial").asText());
                    txtNomeFantasia.setText(resposta.get("nomeFantasia").asText());
                }

                showMessage(
                        Alert.AlertType.INFORMATION,
                        "CNPJ consultado com sucesso!"
                );

            } else {
                showMessage(
                        Alert.AlertType.ERROR,
                        "Erro ao consultar CNPJ. Código: " + respostaApi
                );
            }

        } catch (Exception e) {
            showMessage(
                    Alert.AlertType.ERROR,
                    "Erro ao consultar CNPJ: " + e.getMessage()
            );
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @FXML
    public void cadastrarMecanica(ActionEvent event) throws Exception {

        if (SessaoController.token == null || SessaoController.token.isEmpty()) {
            showMessage(Alert.AlertType.ERROR, "Faça login como ADMIN primeiro!");
            return;
        }

        String jsonRequest = String.format(
                "{\"razaoSocial\":\"%s\",\"nomeFantasia\":\"%s\",\"cnpj\":\"%s\",\"nome\":\"%s\",\"email\":\"%s\",\"senha\":\"%s\",\"cpf\":\"%s\",\"secretKey\":\"dfsdvg165sdgdsfds546546\"}",
                txtRazaoSocial.getText(),
                txtNomeFantasia.getText(),
                txtCnpj.getText(),
                txtNome.getText(),
                txtEmail.getText(),
                txtSenha.getText(),
                txtCpf.getText()
        );

        URL url = new URL("http://localhost:8080/usuarios/criar-mecanica");

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + SessaoController.token);

        try (var os = connection.getOutputStream()) {
            os.write(jsonRequest.getBytes("UTF-8"));
        }

        int respostaApi = connection.getResponseCode();

        if (respostaApi == 201) {

            showMessage(
                    Alert.AlertType.INFORMATION,
                    "Mecânica e administrador cadastrados com sucesso!"
            );

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/back/menu-view.fxml")
            );

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();

            stage.setScene(scene);

        } else {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), "UTF-8")
            );

            StringBuilder resposta = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }

            reader.close();

            showMessage(
                    Alert.AlertType.ERROR,
                    "Erro ao cadastrar: " + resposta
            );
        }

        connection.disconnect();
    }

    private void showMessage(Alert.AlertType type, String msg) {
        Alert alerta = new Alert(type);
        alerta.setTitle("Mensagem do Sistema!");
        alerta.setHeaderText(null);
        alerta.setContentText(msg);
        alerta.showAndWait();
    }
}
