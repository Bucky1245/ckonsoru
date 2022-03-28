/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fges.ckonsoru;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 * Launch the App
 * @author julie.jacques
 */
public class App {
    public static void quitter(){
        System.exit(0);
    }

    public static void main(String args[]){
        Scanner sc = new Scanner(System.in); // Pour la saisie clavier
        
        System.out.println("Bienvenue sur Clinique Konsoru !");
        
        // chargement de la configuration de la persistence
        ConfigLoader cf = new ConfigLoader();
        Properties properties = cf.getProperties();
        
        
        System.out.println("Mode de persistence : "
                +properties.getProperty("persistence"));

        while(true){
            String actionARealiser = ""; // On le redéfinit chaque fois pour éviter une boucle infinie

            System.out.println("Actions disponibles :");
            System.out.println("1. Afficher les créneaux disponibles pour une date donnée");
            System.out.println("2. Lister les rendez-vous passés, présent et à venir d'un client");
            System.out.println("3. Prendre un rendez-vous");
            System.out.println("4. Supprimer un rendez-vous");
            System.out.println("9. Quitter");

            // Boucle qui empêche l'utilisateur de rentrer une action non valide
            while(actionARealiser.equals("1") == false && actionARealiser.equals("2") == false && actionARealiser.equals("3") == false && actionARealiser.equals("4") == false && actionARealiser.equals("9") == false){
                if(actionARealiser.equals("") == false){
                    System.out.println("Veuillez choisir une action valide !");
                }
                System.out.println("Entrez l'action que vous souhaitez réaliser :");
                actionARealiser = sc.nextLine();
            }
            
            // Une fois que l'utilisateur a choisi une action convenable, on l'exécute
            if(actionARealiser.equals("1")){
                System.out.println("De quel jour souhaitez-vous afficher les créneaux (de forme jj/mm/aaaa) ?");
                String datecherchee = sc.nextLine();
                String partiesdates[] = datecherchee.split("/");
                if(properties.getProperty("persistence").equals("xml")){
                    XMLRequests test = new XMLRequests();
                    test.afficherCreneaux(Integer.parseInt(partiesdates[2]), Integer.parseInt(partiesdates[1]), Integer.parseInt(partiesdates[0]));
                }else{
                    BDDRequests test = new BDDRequests();
                    test.bdd_creneaux(Integer.parseInt(partiesdates[2]), Integer.parseInt(partiesdates[1]), Integer.parseInt(partiesdates[0]));
                }
            }else if(actionARealiser.equals("2")){
                System.out.println("Liste de rendez-vous d'un client.");
                System.out.println("Indiquer le nom du client :");
                String nomcli = sc.nextLine();
                if(properties.getProperty("persistence").equals("xml")){
                    XMLRequests test = new XMLRequests();
                    test.afficheRDVCli(nomcli);
                }else{
                    BDDRequests test = new BDDRequests();
                    test.listerdv(nomcli);
                }
            }else if(actionARealiser.equals("3")){
                System.out.println("Prise de rendez-vous.");
                System.out.println("Indiquer une date et heure de début au format JJ/MM/AAAA HH:MM (ex: 18/03/2021 15:00) :");
                String daterdv = sc.nextLine();
                System.out.println("Indiquer le nom du vétérinaire :");
                String nomveto = sc.nextLine();
                System.out.println("Indiquer le nom du client :");
                String nomcli = sc.nextLine();
                if(properties.getProperty("persistence").equals("xml")){
                    XMLRequests test = new XMLRequests();
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        Date parsedDate = dateFormat.parse(daterdv);
                        Timestamp datetimestamp = new java.sql.Timestamp(parsedDate.getTime());
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datetimestamp);
                        test.AddRdv(timeStamp,nomveto,nomcli);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }else{
                    BDDRequests test = new BDDRequests();
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        Date parsedDate = dateFormat.parse(daterdv);
                        Timestamp datetimestamp = new java.sql.Timestamp(parsedDate.getTime());
                        test.prendrerdv(datetimestamp, nomveto, nomcli);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }
            }else if(actionARealiser.equals("4")){
                System.out.println("Suppression d'un rendez-vous.");
                System.out.println("Indiquer une date et heure de début au format JJ/MM/AAAA HH:MM (ex: 18/03/2021 15:00) :");
                String daterdv = sc.nextLine();
                System.out.println("Indiquer le nom du client :");
                String nomcli = sc.nextLine();
                if(properties.getProperty("persistence").equals("xml")){
                    XMLRequests test = new XMLRequests();
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        Date parsedDate = dateFormat.parse(daterdv);
                        Timestamp datetimestamp = new java.sql.Timestamp(parsedDate.getTime());
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datetimestamp);
                        test.supprRdv(timeStamp, nomcli);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }else{
                    BDDRequests test = new BDDRequests();
                    try{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                        Date parsedDate = dateFormat.parse(daterdv);
                        Timestamp datetimestamp = new java.sql.Timestamp(parsedDate.getTime());
                        test.supprrdv(nomcli, datetimestamp);
                    }catch(Exception e){
                        System.out.println(e);
                    }
                }
            }else if(actionARealiser.equals("9")){
                quitter();
            }
        }
    }
    
}
