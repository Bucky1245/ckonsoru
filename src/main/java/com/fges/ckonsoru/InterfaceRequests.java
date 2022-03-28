package com.fges.ckonsoru;

public interface InterfaceRequests {
    public void addRdv(String date, String nVeto, String nClient);
    public void supprRdv(String date, String N_Client);
    public void afficherCreneaux(int year, int month, int day);
    public void afficheRdv(String nomcli);
}
