package com.fges.ckonsoru;
//import org.jdom2.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.util.*;


public class XMLRequests {
    public void afficherCreneaux(int year, int month, int day){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        LocalDate date = LocalDate.of(year, month, day);
        String nomjour = date.format(DateTimeFormatter.ofPattern("EEEE",Locale.FRENCH)); // trouve le jour de la semaine correspondant à la date
        try {
            // charger le fichier xml
            builder = factory.newDocumentBuilder();
            String filepath = "src/main/resources/ckonsoru.xml";
            Document xmldoc = builder.parse(filepath);
            // créer la requête XPATH
            String requeteXPATH = "/ckonsoru/disponibilites/disponibilite[starts-with(jour,'"+nomjour+"')]";
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(requeteXPATH);
            // evaluer la requête XPATH
            NodeList nodes = (NodeList) expr.evaluate(xmldoc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node nNode = nodes.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element eElement = (Element) nNode;
                    String jour = eElement.getElementsByTagName("jour").item(0).getTextContent();
                    String debut = eElement.getElementsByTagName("debut").item(0).getTextContent();
                    String fin = eElement.getElementsByTagName("fin").item(0).getTextContent();
                    String veto = eElement.getElementsByTagName("veterinaire").item(0).getTextContent();
                    // Ici : afficher chaque 20 minutes de début à fin
                    String partiesdebut[] = debut.split(":");
                    String partiesfin[] = fin.split(":");
                    LocalDateTime dateTimedeb = LocalDateTime.of(year, month, day, Integer.parseInt(partiesdebut[0]), Integer.parseInt(partiesdebut[1]));
                    LocalDateTime dateTimefin = LocalDateTime.of(year, month, day, Integer.parseInt(partiesfin[0]), Integer.parseInt(partiesfin[1]));
                    while(dateTimedeb.isEqual(dateTimefin) == false){
                        System.out.println(veto + " : " + dateTimedeb);
                        dateTimedeb = dateTimedeb.plus(20, ChronoUnit.MINUTES);
                    }
                    //System.out.println(veto + " : " + dateTimefin);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException  | XPathExpressionException e) {
            System.err.println("Erreur à l'ouverture du fichier xml");
            e.printStackTrace(System.err);
        }
    }

    public void afficheRDVCli(String nomcli){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        //LocalDate date = LocalDate.of(year, month, day);
        //String sdate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        try {
            // charger le fichier xml
            builder = factory.newDocumentBuilder();
            String filepath = "src/main/resources/ckonsoru.xml";
            Document xmldoc = builder.parse(filepath);
            // créer la requête XPATH
            String requeteXPATH = "/ckonsoru/rdvs/rdv[starts-with(client,'"+nomcli+"')]";
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(requeteXPATH);
            // evaluer la requête XPATH
            NodeList nodes = (NodeList) expr.evaluate(xmldoc, XPathConstants.NODESET);
            System.out.println(nodes.getLength() + " rendez-vous trouvé(s) pour " + nomcli);
            for (int i = 0; i < nodes.getLength(); i++)
            {
                Node nNode = nodes.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                {
                    Element eElement = (Element) nNode;
                    String debut = eElement.getElementsByTagName("debut").item(0).getTextContent();
                    String client = eElement.getElementsByTagName("client").item(0).getTextContent();
                    String veto = eElement.getElementsByTagName("veterinaire").item(0).getTextContent();
                    System.out.println(debut + " avec " + veto);
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException  | XPathExpressionException e) {
            System.err.println("Erreur à l'ouverture du fichier xml");
            e.printStackTrace(System.err);
        }
    }

    public void AddRdv(String date, String N_Veto, String N_Client)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        //String sdate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        try {
            // charger le fichier xml
            builder = factory.newDocumentBuilder();
            String filepath = "src/main/resources/ckonsoru.xml";
            Document xmldoc = builder.parse(filepath);

            // cree un rdv avec client & veterinaire
            Element rdv = xmldoc.createElement("rdv");
            
            Element daterdv = xmldoc.createElement("debut");
            daterdv.appendChild(xmldoc.createTextNode(date));
            rdv.appendChild(daterdv);
            
            Element client = xmldoc.createElement("client");
            client.appendChild(xmldoc.createTextNode(N_Client));
            rdv.appendChild(client);

            Element veterinaire = xmldoc.createElement("veterinaire");
            veterinaire.appendChild(xmldoc.createTextNode(N_Veto));
            rdv.appendChild(veterinaire);

            // ajout au noeud rdvs
            NodeList nodes = xmldoc.getElementsByTagName("rdvs");
            nodes.item(0).appendChild(rdv);

            System.out.println("Un rendez-vous pour " + N_Client + " avec " +  N_Veto + " a été réservé le " + date);

            // enregistrer le fichier
            DOMSource source = new DOMSource(xmldoc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(filepath);
            transformer.transform(source, result);

        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            System.err.println("Erreur à l'ouverture du fichier xml");
            e.printStackTrace(System.err);
        }
    }

    public void supprRdv(String date, String N_Client){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        //String sdate = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        try {
            // charger le fichier xml
            builder = factory.newDocumentBuilder();
            String filepath = "src/main/resources/ckonsoru.xml";
            Document xmldoc = builder.parse(filepath);

            // créer la requête XPATH
            String requeteXPATH = "/ckonsoru/rdvs/rdv[starts-with(client,'"+N_Client+"') and starts-with(debut, '"+date+"')]";
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile(requeteXPATH);

            // Supprime le noeud trouvé
            NodeList nodes = (NodeList) expr.evaluate(xmldoc, XPathConstants.NODESET);
            //System.out.println(nodes.getLength() + " noeud(s) trouvé(s)");
            for (int i = nodes.getLength() - 1; i >= 0; i--){
                nodes.item(i).getParentNode().removeChild(nodes.item(i));
            }

            System.out.println("Le rendez-vous du " + date + " de " +  N_Client + " a été supprimé");

            // enregistrer le fichier
            DOMSource source = new DOMSource(xmldoc);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(filepath);
            transformer.transform(source, result);

        } catch (IOException | ParserConfigurationException | SAXException | TransformerException | XPathException e) {
            System.err.println("Erreur à l'ouverture du fichier xml");
            e.printStackTrace(System.err);
        }

    }
}
