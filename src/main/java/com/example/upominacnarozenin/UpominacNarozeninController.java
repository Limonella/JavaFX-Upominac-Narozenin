package com.example.upominacnarozenin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpominacNarozeninController implements Initializable {

    @FXML
    private ListView<Osoba> osobyListView;
    @FXML
    private Label dnesLabel;
    @FXML
    private Label nejblizsiLabel;
    @FXML
    private Label narozeninyLabel;
    @FXML
    private Label vekLabel;

    private final SpravceOsob spravceOsob = new SpravceOsob();

    @FXML
    protected void handlePridatButtonAction(ActionEvent event) {
        Dialog<Osoba> dialog = new Dialog<>();
        dialog.setTitle("Nová osoba");
        dialog.setWidth(350);
        dialog.setHeight(250);
        vytvorObsahDialogu(dialog);
        Optional<Osoba> vysledek = dialog.showAndWait();
        if (vysledek.isPresent()) {
            spravceOsob.pridej(vysledek.get());
            obnovNejblizsi();
            try {
                spravceOsob.uloz();
            } catch (IOException ex) {
                System.out.println("Chyba: " + ex.getMessage());
            }
        }
    }

    @FXML
    protected void handleOdebratButtonAction(ActionEvent event) {
        Osoba vybrana = osobyListView.getSelectionModel().getSelectedItem();
        if (vybrana != null) {
            spravceOsob.odeber(vybrana);
            obnovNejblizsi();
            try {
                spravceOsob.uloz();
            } catch (IOException ex) {
                System.out.println("Chyba: " + ex.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            spravceOsob.nacti();
        } catch (IOException ex) {
            System.out.println("Chyba: " + ex.getMessage());
        }

        dnesLabel.setText(Datum.zformatuj(LocalDate.now()));
        osobyListView.setItems(spravceOsob.getOsoby());

        osobyListView.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            String narozeniny = "?", vek = "?";
            if (newValue != null) {
                narozeniny = Datum.zformatuj(newValue.getNarozeniny());
                vek = newValue.spoctiVek() + "";
            }
            narozeninyLabel.setText(narozeniny);
            vekLabel.setText(vek);
        });

        if (!spravceOsob.getOsoby().isEmpty()) {
            osobyListView.getSelectionModel().select(0);
        }
        obnovNejblizsi();
    }

    private void obnovNejblizsi() {
        if (!spravceOsob.getOsoby().isEmpty()) {
            Osoba nejblizsi = spravceOsob.najdiNejblizsi();
            int vek = nejblizsi.spoctiVek();
            LocalDate dnes = LocalDate.now();
            if (dnes.getDayOfYear() != nejblizsi.getNarozeniny().getDayOfYear()) {
                ++vek;
                nejblizsiLabel.setText(nejblizsi.getJmeno() + " (" + vek + " let) za " + nejblizsi.zbyvaDni() + " dní");
            } else {
                nejblizsiLabel.setText(nejblizsi.getJmeno() + " (" + vek + " let) dnes!!!");
            }
        } else {
            nejblizsiLabel.setText("Žádné osoby v seznamu");
        }
    }

    private void vytvorObsahDialogu(Dialog<Osoba> dialog) {
        // Vytvoření "potvrzovacího" tlačítka pro potvrzení dialogu
        ButtonType createButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        // Nastavení tlačítek dialogu
        dialog.getDialogPane().getButtonTypes().setAll(createButtonType, ButtonType.CANCEL);
        // Mřížka
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        // Komponenty
        TextField jmenoTextField = new TextField();
        TextField datumTextField = new TextField();
        Label jmenoLabel = new Label("Jméno");
        Label datumLabel = new Label("Datum narození");
        grid.add(jmenoLabel, 0, 0);
        grid.add(jmenoTextField, 1, 0);
        grid.add(datumLabel, 0, 1);
        grid.add(datumTextField, 1, 1);

        dialog.setResultConverter(new Callback<ButtonType, Osoba>() {
            @Override
            public Osoba call(ButtonType param) {
                // Obsluha tlačítek dialogu
                try {
                    LocalDate narozeniny = Datum.naparsuj(datumTextField.getText());
                    return new Osoba(jmenoTextField.getText(), narozeniny);
                } catch (DateTimeParseException | IllegalArgumentException ex) {
                    System.out.printf("Chyba: " + ex.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Osobu se nepodařilo naparsovat!");
                    alert.showAndWait();
                    return null;
                }
            }
        });

        dialog.getDialogPane().setContent(grid);
    }
}