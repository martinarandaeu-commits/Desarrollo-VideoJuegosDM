/**
 * Write a description of class GameManager here.
 * 
 * @author Martin Aranda y David Pareles 
 * @version (a version number or a date)
 */

import java.util.*;

public class GameManager {

    private static GameManager instancia;

    private int puntos = 0;
    private int enemigosEliminados = 0;

    private List<Observador> observadores = new ArrayList<>();

    private int cargaTriple = 0;
    private static final int CARGA_MAXIMA = 100;

    private static final int DURACION_TRIPLE = 300;
    private int tiempoTriple = 0;

    private boolean tripleActivo = false;
    private boolean juegoTerminado = false;

    private GameManager() { }

    public static GameManager getInstancia() {

        if (instancia == null) {
            instancia = new GameManager();
        }

        return instancia;
    }

    public void suscribir(Observador o) {
        observadores.add(o);
    }

    public void sumarPuntos(int p) {
        puntos += p;
        notificar();
    }

    public void registrarEnemigoDestruido(
        int puntosGanados,
        int cargaGanada
    ) {

        puntos += puntosGanados;
        enemigosEliminados++;

        if (!tripleActivo) {

            cargaTriple += cargaGanada;

            if (cargaTriple > CARGA_MAXIMA) {
                cargaTriple = CARGA_MAXIMA;
            }
        }

        notificar();
    }

    public boolean activarTriple() {

        if (!tripleActivo &&
            cargaTriple >= CARGA_MAXIMA &&
            !juegoTerminado) {

            tripleActivo = true;
            tiempoTriple = DURACION_TRIPLE;
            cargaTriple = 0;

            return true;
        }

        return false;
    }

    public void actualizarTriple() {

        if (tripleActivo) {

            tiempoTriple--;

            if (tiempoTriple <= 0) {

                tiempoTriple = 0;
                tripleActivo = false;
            }
        }
    }

    public boolean isTripleActivo() {
        return tripleActivo;
    }

    public boolean isTripleListo() {
        return !tripleActivo &&
               cargaTriple >= CARGA_MAXIMA;
    }

    public int getProgresoTriple() {

        if (tripleActivo) {
            return (tiempoTriple * 100)
                    / DURACION_TRIPLE;
        }

        return cargaTriple;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getEnemigosEliminados() {
        return enemigosEliminados;
    }

    public void terminarJuego() {
        juegoTerminado = true;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void reiniciar() {

        puntos = 0;
        enemigosEliminados = 0;

        cargaTriple = 0;
        tiempoTriple = 0;

        tripleActivo = false;
        juegoTerminado = false;

        observadores.clear();
    }

    private void notificar() {

        for (Observador o : observadores) {
            o.actualizar(puntos);
        }
    }
}