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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginAdminController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    public void entrar(ActionEvent event) throws Exception {

        String jsonRequest = String.format(
                "{\"email\":\"%s\",\"senha\":\"%s\"}",
                txtEmail.getText(),
                txtSenha.getText()
        );

        URL url = new URL("http://localhost:8080/login");

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        try (var os = connection.getOutputStream()) {
            os.write(jsonRequest.getBytes("UTF-8"));
        }

        int respostaApi = connection.getResponseCode();

        if (respostaApi == 200) {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8")
            );

            String resposta = reader.readLine();
            reader.close();

            Pattern pattern = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(resposta);

            if (matcher.find()) {

                SessaoController.token = matcher.group(1);

                showMessage(
                        Alert.AlertType.INFORMATION,
                        "Login do ADMIN efetuado com sucesso!"
                );

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/back/menu-view.fxml")
                );

                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) ((Node) event.getSource())
                        .getScene().getWindow();

                stage.setScene(scene);

            } else {
                showMessage(Alert.AlertType.ERROR, "Não foi possível obter o token!");
            }

        } else {
            showMessage(Alert.AlertType.ERROR, "Email ou senha inválidos!");
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
