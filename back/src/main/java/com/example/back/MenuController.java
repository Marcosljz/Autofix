
package com.example.back;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button btnCriarAdmin;

    @FXML
    public void initialize() {
        // Se o ADMIN já estiver logado, bloqueia novo cadastro de ADMIN.
        btnCriarAdmin.setDisable(
                SessaoController.token != null &&
                        !SessaoController.token.isEmpty()
        );
    }

    @FXML
    public void abrirCadastro(ActionEvent event) throws IOException {

        if (SessaoController.token != null &&
                !SessaoController.token.isEmpty()) {
            return;
        }

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/example/back/cadastroUsuario-view.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void abrirLoginAdmin(ActionEvent event) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/example/back/loginAdmin-view.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void abrirCadastroMecanica(ActionEvent event) throws IOException {

        if (SessaoController.token == null ||
                SessaoController.token.isEmpty()) {
            return;
        }

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/example/back/cadastroMecanica-view.fxml"));

        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void sair() {
        System.exit(0);
    }
}
