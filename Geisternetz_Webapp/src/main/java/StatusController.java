import jakarta.enterprise.context.SessionScoped;
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
 * Klasse "StatusController" enthält Methoden zum ändern des Status eines Netzes
 */
@Named
@ViewScoped
public class StatusController implements Serializable {
    // Variablen
    private int personId;
    private int netzId;
    private String vorname;
    private String nachname;
    private String telefonnummer;
    private int flaeche;
    private BigDecimal bgrad;
    private BigDecimal lgrad;

    /**
     * Methode welche den Status eines Netzes von "gemeldet" auf "wirdgeborgen" setzt.
     * Prüft zuerst ob das angegebene Netz existiert, gemeldet ist, ob die Benutzer ID existiert
     *
     * @return
     */
    public String netzWirdGeborgen() {
        // Entity Manager
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gnPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            // Angegebene Geisternetz per Primärschlüssel finden
            Geisternetz geisternetz = entityManager.find(Geisternetz.class, netzId);
            // Kein Geisternetz mit passenden Attributen >> Fehlermeldung
            if (geisternetz == null) {
                transaction.rollback();
                return "fehlgeschlagen.xhtml";
            }
            // Geisternetz hat einen anderen Status als "gemeldet" >> Fehlermeldung
            if (!geisternetz.getStatus().equals(Status.GEMELDET)) {
                transaction.rollback();
                return "fehlgeschlagen.xhtml";
            }
            // Holt vorhandene Person über die ID
            Person person = entityManager.find(Person.class, personId);
            // Person existiert nicht >> Fehlermeldung
            if (person == null) {
                transaction.rollback();
                return "fehlgeschlagen.xhtml";
            }
            // Geisternetz existiert, Person existiert >> Erfolgmeldung
            // Funktion setzen
            person.setFunktion(Funktion.BERGEND);
            entityManager.merge(person);

            // Netz als wird geborgen markieren
            geisternetz.setStatus(Status.WIRDGEBORGEN);
            geisternetz.setBergendePerson(person);
            entityManager.merge(geisternetz);

            transaction.commit();

            return "erfolgreich.xhtml";

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            entityManager.close();
            reset();
        }
        return "fehlgeschlagen.xhtml";
    }

    /**
     * Netz wird als schon geborgen markiert
     *
     * @return
     */
    public String netzGeborgen() {
        // System.out.println("netzGeborgen() wurde aufgerufen");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gnPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // Gleiches Vorgehen wie bei "netzWirdGeborgen" Methode
        try {
            Geisternetz geisternetz = entityManager.find(Geisternetz.class, netzId);

            if (geisternetz == null) {
                transaction.rollback();
                return "fehlgeschlagen.xhtml";
            }
            // Geisternetz hat anderen Status als "wirdgeborgen" >> Fehlermeldung
            if (!geisternetz.getStatus().equals(Status.WIRDGEBORGEN)) {
                transaction.rollback();
                return "fehlgeschlagen.xhtml";
            }
            // Holt vorhandene Person über die ID
            Person person = entityManager.find(Person.class, personId);

            if (person == null) {
                transaction.rollback();
                return "fehlgeschlagen.xhtml";
            }

            person.setFunktion(Funktion.BERGEND);
            entityManager.merge(person);

            // Netz als wird geborgen markieren
            geisternetz.setStatus(Status.GEBORGEN);
            geisternetz.setBergendePerson(person);
            entityManager.merge(geisternetz);

            transaction.commit();

            return "erfolgreich.xhtml";

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
            reset();
        }
        return "fehlgeschlagen.xhtml";
    }

    /**
     * Methode um die input Felder zurückzusetzen
     */
    public void reset() {
        setPersonId(0);
        setNetzId(0);
        setVorname(null);
        setNachname(null);
        setTelefonnummer(null);
        setFlaeche(0);
        setLgrad(null);
        setBgrad(null);
    }
    // Getter und Setter
    public int getPersonId() {
        return personId;
    }
    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getNetzId() {
        return netzId;
    }
    public void setNetzId(int netzId) {
        this.netzId = netzId;
    }

    public String getVorname() {
        return vorname;
    }
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }
    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public int getFlaeche() {
        return flaeche;
    }
    public void setFlaeche(int flaeche) {
        this.flaeche = flaeche;
    }

    public BigDecimal getLgrad() {
        return lgrad;
    }
    public void setLgrad(BigDecimal lgrad) {
        this.lgrad = lgrad;
    }

    public BigDecimal getBgrad() {
        return bgrad;
    }
    public void setBgrad(BigDecimal bgrad) {
        this.bgrad = bgrad;
    }
}
