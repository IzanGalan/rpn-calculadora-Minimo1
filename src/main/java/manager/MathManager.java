package manager;

import models.Alumne;
import models.Institut;
import models.Operacio;

import java.util.List;

public interface MathManager {

    // Registrar institut (si existe lo actualiza)
    void afegirInstitut(Institut institut);

    // Registrar alumne (lanza excepción si el institut no existe)
    void afegirAlumne(Alumne alumne) throws Exception;

    // Encolar operació (lanza excepción si el alumne no existe)
    void requerirOperacio(Operacio operacio) throws Exception;

    // Procesar la primera operació pendiente (FIFO)
    Operacio processarOperacio() throws Exception;

    // Listado operaciones de un institut
    List<Operacio> llistatOperacionsPerInstitut(String idInstitut) throws Exception;

    // Listado operaciones de un alumne
    List<Operacio> llistatOperacionsPerAlumne(String idAlumne) throws Exception;

    // Listado instituts ordenados descendentemente por num operaciones
    List<Institut> llistatInstitutsPorOperacions();

    // Para los tests
    void clear();
    int numOperacionsPendents();
}