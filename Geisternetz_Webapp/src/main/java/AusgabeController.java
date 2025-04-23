import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * Klasse "AusgabeController" für Methoden die verschiedene Geisternetze auf die Seite ausgeben
 */
@Named
@ViewScoped
public class AusgabeController implements Serializable {

    private List<Geisternetz> geisternetze;

    // Filter für den Status, default false (0 oder 1)
    private boolean filterStatus1 = false;

    /**
     * Methode um nur die nicht geborgenen Netze zu laden
     *
     * @return Liste der nicht geborgenen Netze
     */
    public List<Geisternetz> getZuBergendeNetze() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gnPersistenceUnit");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Standardabfrage nach status 0 oder 1
        String queryString = "SELECT e FROM Geisternetz e WHERE e.status = 0 OR e.status = 1";

        // Wenn der Filter auf status = 1 gesetzt ist, filtere nur nach status = 1
        if (filterStatus1) {
            queryString = "SELECT e FROM Geisternetz e WHERE e.status = 1";
        }
        // oder nach skript: Query q = entityManager.createQuery(..);
        // geisternetze = q.getResultList();
        // return geisternetze

        // Rückgabe muss ein Geisternetz aus der Liste sein
        return entityManager.createQuery(queryString, Geisternetz.class).getResultList();
    }

    // Getter und Setter für den Filterstatus
    public boolean isFilterStatus1() {
        return filterStatus1;
    }
    public void setFilterStatus1(boolean filterStatus1) {
        this.filterStatus1 = filterStatus1;
    }
}
