import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 *  Klasse "NeuesNetzMeldenController", enthält Methoden um Netze anonym oder mit ID zu melden
 *  Verwaltet auch die Methode neuerBenutzer um einen neuen Benutzer mit ID zu erstellen
 */
@Named
@ViewScoped
public class NeuesNetzMeldenController implements Serializable {
    // Variablen
    private int personId;
    private int netzId;
    private String vorname;
    private String nachname;
    private String telefonnummer;
    private int flaeche;
    private BigDecimal bgrad;
    private BigDecimal lgrad;


    // Persistence xml <property name="hibernate.hbm2ddl.auto" value="update"/> von create nach update gestellt
    // Methode um ein Netz anonym zu melden, es werden keine Personendaten gebraucht
    public String neuesNetzMelden() {

        // Neues Geisternetz erstellen
        Geisternetz netz = new Geisternetz();
        // Parameter die gesetzt werden
        netz.setBgrad(bgrad);
        netz.setFlaeche(flaeche);
        netz.setLgrad(lgrad);
        netz.setStatus(Status.GEMELDET);

        // System.out.println("DEBUG: Durchmesser vor dem Speichern: " + flaeche);

        // Prüfen, ob das Netz bereits existiert
        if (netzExistiertBereits(netz)) {
            // Wenn true wird man auf eine Fehlerseite geleitet
            return "existent.xhtml";
        }

        // Entity Manager
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gnPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            // Nur das Netz wird gespeichert
            entityManager.persist(netz);
            transaction.commit();

            // Weiterleitung zur erfolgreich-Seite
            return "erfolgreich.xhtml";

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    return "fehlgeschlagen.xhtml";
    }

    /**
    * Methode um ein neues Netz zu melden mit einer Benutzer ID
    */
    public String neuesNetzMeldenId() {
        //Entity Mananger
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gnPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        // transaction.begin();
        // Person personExistiert = entityManager.find(Person.class, personId);

        try{
            transaction.begin();
            // Methode find() um den Primärschlüssel zu holen
            Person personExistiert = entityManager.find(Person.class, personId);
            if (personExistiert == null) {
                // Frühzeitig abbrechen, weil die Person nicht existiert
                transaction.rollback(); // Aus Sicherheit, auch wenn noch nichts persistiert wurde
                return "fehlgeschlagen.xhtml";
            }
            // Neues Geisternetz erstellen mit verschiedenen Attributen
            Geisternetz netz = new Geisternetz();
            netz.setBgrad(bgrad);
            netz.setFlaeche(flaeche);
            netz.setLgrad(lgrad);
            netz.setStatus(Status.GEMELDET);
            // Prüfen, ob das Netz bereits existiert
            if (netzExistiertBereits(netz)) {
                // Wenn true wird man auf eine Fehlerseite geleitet
                return "existent.xhtml";
            }
            // Funktion der Person auf meldend setzen
            netz.setMeldendePerson(personExistiert);
            // DB persistieren
            entityManager.persist(netz);
            // Comitten
            transaction.commit();
            // Auf die erfolgreich Seite zurück
            return "erfolgreich.xhtml";

            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                    return "fehlgeschlagen.xhtml";
                }
            } finally {
                entityManager.close();
            }
    return "fehlgeschlagen.xhtml";
    }
    /**
    * Methode neuerBenutzer, erstellt eine neue Person mit einer Benutzer ID als Primärschlüssel der
    * automatisch generiert, und dem Benutzer dann ausgegeben wird
    */
    public void neuerBenutzer(){
        // Neue Person erstellen
        Person person = new Person();
        person.setVorname(vorname);
        person.setNachname(nachname);
        person.setTelefonnummer(telefonnummer);
        // Entity Manager
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gnPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.persist(person);

            transaction.commit();
            // Audgeben der Erfolgsmeldung und der ID
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Super!", "Benutzer wurde gespeichert. Die ID lautet: " + person.getId());
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
                // Ausgeben der Fehlermeldung
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Das hat nicht geklappt.", "Lade die Seite neu und versuche es noch einmal");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    /**
     * Methode, um zu prüfen, ob ein Netz mit denselben geografischen Koordinaten und Fläche bereits existiert
     *
     * @param netz
     * @return true wenn ein Netz schon existiert, false wenn das Netz(Die Kombination der Attribute) neu ist
     */
    private boolean netzExistiertBereits(Geisternetz netz) {
        // Entity Manager
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gnPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // Query erstellen und die nötige Kombination aus Attributen abfragen
        String query = "SELECT e FROM Geisternetz e WHERE e.bgrad = " + netz.getBgrad()
                + " AND e.lgrad = " + netz.getLgrad()
                + " AND e.flaeche = " + netz.getFlaeche();
        /* Es kommt eine Liste zurück aber wir brauchen einen boolean, isEmpty() gibt den boolean
        * zurück und setMaxResults(1) stellt sicher dass nicht die ganze Liste durchsucht wird wenn schon
        * ein Ergebniss gefunden wurde. Das Ergebnis wird in der Variable "existiert" gespeichert und als return Wert
        * zurückgegeben.
         */
        boolean exisitert = !entityManager.createQuery(query).setMaxResults(1).getResultList().isEmpty();

        entityManager.close();
        entityManagerFactory.close();

        return exisitert;
    }

    // Getter und Setter für die Felder
    public int getPersonId() { return personId; }
    public void setPersonId(int personId) { this.personId = personId; }

    public int getNetzId() { return netzId; }
    public void setNetzId(int netzId) { this.netzId = netzId; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public String getTelefonnummer() { return telefonnummer; }
    public void setTelefonnummer(String telefonnummer) { this.telefonnummer = telefonnummer;}

    public void setFlaeche(int flaeche) {
        // System.out.println("DEBUG: Eingehender Durchmesser-Wert: " + flaeche);
        this.flaeche = flaeche;
    }
    public int getFlaeche() {
        return flaeche;
    }

    public BigDecimal getBgrad() { return bgrad; }
    public void setBgrad(BigDecimal bgrad) { this.bgrad = bgrad; }

    public BigDecimal getLgrad() { return lgrad; }
    public void setLgrad(BigDecimal lgrad) { this.lgrad = lgrad; }
}
