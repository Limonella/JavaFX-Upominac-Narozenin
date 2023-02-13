package com.example.upominacnarozenin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Datum {
    /**
     * Formátovač data
     */
    private static DateTimeFormatter zformatovany = DateTimeFormatter.ofPattern("d.M.yyyy");

    /**
     * Pomocná metoda pro zformátování data do textové podoby
     *
     * @param datum Datum k zformátování
     * @return Zformátování datum
     */
    public static String zformatuj(LocalDate datum) {
        String datumText = datum.format(zformatovany);
        return datumText;
    }

    /**
     * Pomocná metoda pro naparsování datumu z textu
     *
     * @param datumText Datum v textové podobě
     * @return Datum
     * @throws java.text.ParseException
     */
    public static LocalDate naparsuj(String datumText) throws DateTimeParseException {
        LocalDate datum = LocalDate.parse(datumText, zformatovany);
        return datum;
    }
}
