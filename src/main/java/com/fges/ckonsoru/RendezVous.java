package com.fges.ckonsoru;

import java.sql.Timestamp;

public class RendezVous {
    private Timestamp debut;
    private String client;
    private String veterinaire;

    public RendezVous(Timestamp debut, String client, String veterinaire) {
        this.debut = debut;
        this.client = client;
        this.veterinaire = veterinaire;
    }
    public Timestamp getDebut() {
        return debut;
    }
    public void setDebut(Timestamp debut) {
        this.debut = debut;
    }
    public String getClient() {
        return client;
    }
    public void setClient(String client) {
        this.client = client;
    }
    public String getVeterinaire() {
        return veterinaire;
    }
    public void setVeterinaire(String veterinaire) {
        this.veterinaire = veterinaire;
    }

    @Override
    public String toString() {
        return "RendezVous [client=" + client + ", debut=" + debut + ", veterinaire=" + veterinaire + "]";
    }
}
