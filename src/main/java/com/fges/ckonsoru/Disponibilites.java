package com.fges.ckonsoru;

import java.sql.Time;

public class Disponibilites {
    private String jour;
    private Time debut;
    private Time fin;
    private String nomveto;
    
    public Disponibilites(String jour, Time debut, Time fin, String nomveto) {
        this.jour = jour;
        this.debut = debut;
        this.fin = fin;
        this.nomveto = nomveto;
    }

    public String getJour() {
        return jour;
    }
    public void setJour(String jour) {
        this.jour = jour;
    }
    public Time getDebut() {
        return debut;
    }
    public void setDebut(Time debut) {
        this.debut = debut;
    }
    public Time getFin() {
        return fin;
    }
    public void setFin(Time fin) {
        this.fin = fin;
    }
    public String getNomveto() {
        return nomveto;
    }
    public void setNomveto(String nomveto) {
        this.nomveto = nomveto;
    }

    @Override
    public String toString() {
        return "Disponibilites [debut=" + debut + ", fin=" + fin + ", jour=" + jour + ", nomveto=" + nomveto + "]";
    }
}
