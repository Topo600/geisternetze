import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Klasse "Person", verwaltet die Entität Person
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {
    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String vorname;
    private String nachname;
    private String telefonnummer;
    private Funktion funktion;
    // Leerer Konstruktor für jpa
    public Person() {}
    // Getter und Setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public Funktion getFunktion() {
        return funktion;
    }
    public void setFunktion(Funktion funktion) {
        this.funktion = funktion;
    }
}