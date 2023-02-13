package com.example.upominacnarozenin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


public class SpravceOsob {

    private ObservableList<Osoba> osoby = FXCollections.observableArrayList();
    Path cesta = Path.of(System.getProperty("user.home"), "itnetwork", "osoby.csv");

    public ObservableList<Osoba> getOsoby() {
        return osoby;
    }


    public void uloz() throws IOException {
        Files.createDirectories(cesta.getParent());
        Files.writeString(cesta, "", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        for (Osoba osoba : getOsoby()) {
            String radek = osoba.getJmeno() + ";" + Datum.zformatuj(osoba.getNarozeniny()) + System.lineSeparator();
            Files.writeString(cesta, radek, StandardOpenOption.APPEND);
        }
    }

    public void nacti() throws IOException {
        if (Files.exists(cesta)) {
            List<Osoba> docasneOsoby = new ArrayList<>();
            for (String radek : Files.readAllLines(cesta)) {
                String[] casti = radek.split(";");
                docasneOsoby.add(new Osoba(casti[0], Datum.naparsuj(casti[1])));
            }
            osoby.clear();
            osoby.addAll(docasneOsoby);
        }
    }

    public Osoba najdiNejblizsi() {
        FXCollections.sort(osoby);
        return osoby.get(0);
    }

    public void pridej(Osoba osoba) {
        osoby.add(osoba);
    }

    public void odeber(Osoba osoba) {
        osoby.remove(osoba);
    }

}
