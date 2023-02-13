package com.example.upominacnarozenin;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Osoba implements Comparable<Osoba> {

    private String jmeno;
    private LocalDate narozeniny;

    public Osoba(String jmeno, LocalDate narozeniny) throws IllegalArgumentException {

        if (jmeno.length() < 3){
            throw new IllegalArgumentException("Jméno je příliš krátké.");
        }
        if (narozeniny.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Narozeniny nesmí být v budoucnosti.");
        }
        if (jmeno.contains(";"))
            throw new IllegalArgumentException("Jméno nesmí obsahovat ;");
        this.jmeno = jmeno;
        this.narozeniny = narozeniny;
    }

    public int spoctiVek() {
        return (int) ChronoUnit.YEARS.between(narozeniny, LocalDate.now());
    }

    public int zbyvaDni() {
        LocalDate dnes = LocalDate.now();
        LocalDate dalsiNarozeniny = (narozeniny.getDayOfYear() < dnes.getDayOfYear()) ?
                narozeniny.withYear(dnes.getYear() + 1) : narozeniny.withYear(dnes.getYear());
        return (int) ChronoUnit.DAYS.between(dnes, dalsiNarozeniny);
    }

    public String getJmeno() {
        return jmeno;
    }

    public LocalDate getNarozeniny() {
        return narozeniny;
    }

    @Override
    public String toString() {
        return jmeno;
    }

    @Override
    public int compareTo(Osoba osoba) {
        return this.zbyvaDni() - osoba.zbyvaDni();
    }
}
