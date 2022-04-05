package com.fges.ckonsoru;

import java.util.List;

public interface InterfaceRequests {
    public void addRdv(String date, String nVeto, String nClient);
    public void supprRdv(String date, String N_Client);
    public List<Disponibilites> afficherCreneaux(int year, int month, int day);
    public List<RendezVous> afficheRdv(String nomcli);
}
