package com.fges.ckonsoru;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BDDRequests implements InterfaceRequests{

    private static BDDRequests bddRequests;

    private BDDRequests(){}

    public static BDDRequests getInstance()
    {
        if(bddRequests == null)
        {
            bddRequests = new BDDRequests();
        }
        return bddRequests;
    }

    public Connection connexion(){
        try{
            // Chargement de la configuration de la bdd (le config.properties)
            ConfigLoader cf = new ConfigLoader();
            Properties properties = cf.getProperties();
                
            // Récupération de chaque propriété nécessaire
            String dbConnUrl = properties.getProperty("bdd.url");
            String dbUserName = properties.getProperty("bdd.login");
            String dbPassword = properties.getProperty("bdd.mdp");

            if(!"".equals(dbConnUrl)) {
                // Le driver jdbc de postgresql
                Class.forName("org.postgresql.Driver");
                    
                // Création de la connexion à la base de données
                Connection dbConn = DriverManager.getConnection(dbConnUrl, dbUserName, dbPassword);

                return dbConn;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Disponibilites> afficherCreneaux(int year, int month, int day){
        List<Disponibilites> donnees = new ArrayList<>();
        String dateTest = String.valueOf(day)+'-'+String.valueOf(month)+'-'+String.valueOf(year);
        // La requête est celle qui était dans ckonsoru.bdd
        String requetesql = "WITH creneauxDisponibles AS (SELECT vet_nom, generate_series(?::date+dis_debut, ?::date+dis_fin-'00:20:00'::time, '20 minutes'::interval) debut FROM disponibilite INNER JOIN veterinaire ON veterinaire.vet_id = disponibilite.vet_id WHERE dis_jour = EXTRACT('DOW' FROM ?::date) ORDER BY vet_nom, dis_id), creneauxReserves AS (SELECT vet_nom, rv_debut debut FROM rendezvous INNER JOIN veterinaire ON veterinaire.vet_id = rendezvous.vet_id WHERE rv_debut BETWEEN ?::date AND ?::date +'23:59:59'::time), creneauxRestants AS (SELECT * FROM creneauxDisponibles EXCEPT SELECT * FROM creneauxReserves) SELECT * FROM creneauxRestants ORDER BY vet_nom, debut";
        try {
            Connection con = connexion();
            PreparedStatement request = con.prepareStatement(requetesql);
            request.setString(1, dateTest);
            request.setString(2, dateTest);
            request.setString(3, dateTest);
            request.setString(4, dateTest);
            request.setString(5, dateTest);
            ResultSet res = request.executeQuery();

            while(res.next()){
                donnees.add(new Disponibilites(res.getString(1), res.getTime(2), res.getTime(3), res.getString(4)));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return donnees;
    }

    @Override
    public void addRdv(String date, String nVeto, String nClient){
        Timestamp daterdv = Timestamp.valueOf(date);
        String requetesql = "INSERT INTO rendezvous (vet_id, rv_debut, rv_client) VALUES((SELECT vet_id FROM veterinaire WHERE vet_nom = ?), ?, ?)";
        try{
            Connection con = connexion();
            PreparedStatement request = con.prepareStatement(requetesql);
            request.setString(1, nVeto);
            request.setTimestamp(2, daterdv);
            request.setString(3, nClient);
            request.executeUpdate();
            System.out.println("Un rendez-vous pour " + nClient + " avec " + nVeto + " le " + daterdv + " a été ajouté.");
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    // Fonction qui donne le nombre de lignes renvoyées par une requête select
    public int getRows(ResultSet res){
        int totalRows = 0;
        try {
            res.last();
            totalRows = res.getRow();
            res.beforeFirst();
        } 
        catch(Exception ex)  {
            return 0;
        }
        return totalRows;    
    }

    @Override
    public List<RendezVous> afficheRdv(String nomcli){
        List<RendezVous> donnees = new ArrayList<>();
        String requetesql = "SELECT rv_id, rv_debut, vet_nom FROM rendezvous, veterinaire WHERE rv_client = ? AND veterinaire.vet_id = rendezvous.vet_id ORDER BY rv_debut DESC";
        try{
            Connection con = connexion();
            PreparedStatement request = con.prepareStatement(requetesql);
            request.setString(1, nomcli);
            ResultSet res = request.executeQuery();
            System.out.println(getRows(res) + " rendez-vous trouvés pour " + nomcli);
            while(res.next()){
                donnees.add(new RendezVous(res.getTimestamp(3), res.getString(4), res.getString(2)));
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return donnees;
    }

    @Override
    public void supprRdv(String nomcli, String date){
        Timestamp daterdv = Timestamp.valueOf(date);
        String requetesql = "DELETE FROM rendezvous WHERE rv_client = ? AND rv_debut = ?";
        try{
            Connection con = connexion();
            PreparedStatement request = con.prepareStatement(requetesql);
            request.setString(1, nomcli);
            request.setTimestamp(2, daterdv);
            request.executeUpdate();
            System.out.println("Un rendez-vous pour " + nomcli + " le " + daterdv + " a été supprimé.");
        }catch (Exception e) {
            System.out.println(e);
        }
    }
}
