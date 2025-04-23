import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * Klasse "Geisternetz", verwaltet die Entität Geisternetz
 */
@Entity
@Table(name = "geisternetz")
public class Geisternetz{
    // Primary Key Kennzeichnung und automatische Inkrementierung
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal lgrad;
    private BigDecimal bgrad;
    private int flaeche;
    private Status status;
    // Foreign key Variablen
    @ManyToOne
    @JoinColumn(name = "meldendePersonId", referencedColumnName = "id", nullable = true)
    private Person meldendePerson;
    @ManyToOne
    @JoinColumn(name = "bergendePersonId", referencedColumnName = "id", nullable = true)
    private Person bergendePerson;

    /**
     * Leerer Konstruktor für jpa
     */
    public Geisternetz() {}

    // Getter und Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public BigDecimal getBgrad() {
        return bgrad;
    }
    public void setBgrad(BigDecimal bgrad) {
        this.bgrad = bgrad;
    }

    public Person getMeldendePerson() { return meldendePerson; }
    public void setMeldendePerson(Person meldendePerson) { this.meldendePerson = meldendePerson; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) {
        this.status = status;
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

    public Person getBergendePerson() {
        return bergendePerson;
    }
    public void setBergendePerson(Person bergendePerson) {
        this.bergendePerson = bergendePerson;
    }
}
