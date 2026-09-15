import greenfoot.*;

/**
 * Controlador central del combate. Crea a los combatientes, sus
 * acciones, la interfaz (botones, barras, indicador), maneja la
 * máquina de estados explícita (Estado) y resuelve cada turno.
 * Ninguna otra clase decide reglas de combate: JuegoTurnos solo la
 * invoca, las unidades solo se representan a sí mismas y los botones
 * solo comunican una selección.
 */
public class GestorCombate
{
    /**
     * Máquina de estados explícita del combate. No reemplazar por
     * booleanos independientes.
     */
    public enum Estado
    {
        TURNO_JUGADOR,
        ANIMANDO_JUGADOR,
        TURNO_ENEMIGO,
        ANIMANDO_ENEMIGO,
        FIN
    }

    // --- Disposición en el mundo (900x600) ---
    private static final int POS_X_JUGADOR = 250;
    private static final int POS_X_ENEMIGO = 650;
    private static final int POS_Y_PERSONAJES = 360;

    private static final int POS_Y_BARRAS = 35;
    private static final int POS_X_BARRA_JUGADOR = 150;
    private static final int POS_X_BARRA_ENEMIGO = 750;

    private static final int POS_Y_INDICADOR = 90;

    private static final int POS_Y_BOTONES = 555;
    private static final int ESPACIO_BOTONES = 240;

    // Esperas (en frames de act()) para que el feedback visual sea legible
    // antes de continuar la máquina de estados.
    private static final int ESPERA_POST_RESOLUCION = 30;
    private static final int ESPERA_INICIO_TURNO_ENEMIGO = 30;

    private JuegoTurnos mundo;

    private InfanteriaJugador jugador;
    private InfanteriaEnemiga enemigo;

    private Estado estado;

    private AccionCombate[] accionesJugador;
    private AccionCombate[] accionesEnemigo;

    private BotonAccion[] botones;

    private BarraHP barraJugador;
    private BarraHP barraEnemigo;

    private IndicadorTurno indicador;

    private AccionCombate accionActual;

    // Variables de control adicionales para evitar procesamiento repetido
    // (actualizar() se ejecuta muchas veces por segundo).
    private boolean resultadoProcesado;
    private int contadorEspera;

    public GestorCombate(JuegoTurnos mundo)
    {
        this.mundo = mundo;
    }

    /**
     * Crea combatientes, acciones, botones, barras de HP e indicador,
     * y deja el combate listo en TURNO_JUGADOR.
     */
    public void inicializar()
    {
        jugador = new InfanteriaJugador();
        enemigo = new InfanteriaEnemiga();
        mundo.addObject(jugador, POS_X_JUGADOR, POS_Y_PERSONAJES);
        mundo.addObject(enemigo, POS_X_ENEMIGO, POS_Y_PERSONAJES);
        jugador.reproducirIdle();
        enemigo.reproducirIdle();

        accionesJugador = new AccionCombate[] {
            new AccionCombate("Ataque rápido", 8, 90),
            new AccionCombate("Ataque normal", 12, 75),
            new AccionCombate("Golpe fuerte", 20, 50)
        };

        accionesEnemigo = new AccionCombate[] {
            new AccionCombate("Corte", 9, 85),
            new AccionCombate("Estocada", 14, 65)
        };

        botones = new BotonAccion[accionesJugador.length];
        int xInicial = mundo.getWidth() / 2 - ((botones.length - 1) * ESPACIO_BOTONES) / 2;
        for (int i = 0; i < botones.length; i++)
        {
            botones[i] = new BotonAccion(accionesJugador[i], this);
            mundo.addObject(botones[i], xInicial + i * ESPACIO_BOTONES, POS_Y_BOTONES);
        }

        barraJugador = new BarraHP(jugador, "JUGADOR");
        barraEnemigo = new BarraHP(enemigo, "ENEMIGO");
        mundo.addObject(barraJugador, POS_X_BARRA_JUGADOR, POS_Y_BARRAS);
        mundo.addObject(barraEnemigo, POS_X_BARRA_ENEMIGO, POS_Y_BARRAS);

        indicador = new IndicadorTurno();
        mundo.addObject(indicador, mundo.getWidth() / 2, POS_Y_INDICADOR);

        estado = Estado.TURNO_JUGADOR;
    }

    /**
     * Debe llamarse una vez por ciclo desde JuegoTurnos.act(). Coordina
     * la máquina de estados; nunca aplica daño más de una vez por
     * resolución gracias a resultadoProcesado.
     */
    public void actualizar()
    {
        switch (estado)
        {
            case TURNO_JUGADOR:
                // Esperando que el jugador haga clic en un botón; ver
                // seleccionarAccion().
                break;

            case ANIMANDO_JUGADOR:
                actualizarAnimandoJugador();
                break;

            case TURNO_ENEMIGO:
                actualizarTurnoEnemigo();
                break;

            case ANIMANDO_ENEMIGO:
                actualizarAnimandoEnemigo();
                break;

            case FIN:
                // Combate terminado: no se acepta ninguna entrada nueva.
                break;
        }
    }

    /**
     * Llamado por BotonAccion al hacer clic. Ignora la selección si no
     * es el turno del jugador (evita doble clic / ataques inválidos).
     */
    public void seleccionarAccion(AccionCombate accion)
    {
        if (estado != Estado.TURNO_JUGADOR)
        {
            return;
        }

        accionActual = accion;
        resultadoProcesado = false;
        deshabilitarBotones();
        indicador.setMensaje("ATACANDO...");
        jugador.reproducirAtaque();
        estado = Estado.ANIMANDO_JUGADOR;
    }

    private void actualizarAnimandoJugador()
    {
        if (!resultadoProcesado)
        {
            if (!jugador.animacionTerminada())
            {
                return;
            }

            resolverAtaque(enemigo, accionActual, barraEnemigo);
            resultadoProcesado = true;
            contadorEspera = 0;

            if (!enemigo.estaViva())
            {
                enemigo.reproducirMuerte();
                indicador.setMensaje("VICTORIA");
                estado = Estado.FIN;
                mundo.detenerMusica();
                return;
            }
        }

        contadorEspera++;
        if (contadorEspera < ESPERA_POST_RESOLUCION)
        {
            return;
        }

        jugador.reproducirIdle();
        enemigo.reproducirIdle();
        indicador.setMensaje("TURNO ENEMIGO");
        contadorEspera = 0;
        estado = Estado.TURNO_ENEMIGO;
    }

    private void actualizarTurnoEnemigo()
    {
        contadorEspera++;
        if (contadorEspera < ESPERA_INICIO_TURNO_ENEMIGO)
        {
            return;
        }

        // Seleccionar UNA sola vez el ataque enemigo para este turno.
        int indice = Greenfoot.getRandomNumber(accionesEnemigo.length);
        accionActual = accionesEnemigo[indice];
        resultadoProcesado = false;

        indicador.setMensaje("ENEMIGO ATACA...");
        enemigo.reproducirAtaque();
        estado = Estado.ANIMANDO_ENEMIGO;
    }

    private void actualizarAnimandoEnemigo()
    {
        if (!resultadoProcesado)
        {
            if (!enemigo.animacionTerminada())
            {
                return;
            }

            resolverAtaque(jugador, accionActual, barraJugador);
            resultadoProcesado = true;
            contadorEspera = 0;

            if (!jugador.estaViva())
            {
                jugador.reproducirMuerte();
                indicador.setMensaje("DERROTA");
                estado = Estado.FIN;
                mundo.detenerMusica();
                return;
            }
        }

        contadorEspera++;
        if (contadorEspera < ESPERA_POST_RESOLUCION)
        {
            return;
        }

        jugador.reproducirIdle();
        enemigo.reproducirIdle();
        indicador.setMensaje("TU TURNO");
        habilitarBotones();
        estado = Estado.TURNO_JUGADOR;
    }

    /**
     * Resuelve un ataque ya animado: comprueba acierto UNA sola vez,
     * aplica daño, dispara la animación de daño y el texto flotante
     * correspondiente, y actualiza la barra de HP del objetivo.
     */
    private void resolverAtaque(Unidad objetivo, AccionCombate accion, BarraHP barraObjetivo)
    {
        int xTexto = objetivo.getX();
        int yTexto = objetivo.getY() - 90;

        if (accion.acierta())
        {
            objetivo.recibirDanio(accion.getDanio());
            objetivo.reproducirHerido();
            mundo.addObject(new TextoFlotante("-" + accion.getDanio() + " HP"), xTexto, yTexto);
            barraObjetivo.actualizar();
        }
        else
        {
            mundo.addObject(new TextoFlotante("FALLO"), xTexto, yTexto);
        }
    }

    /**
     * true una vez que el combate llegó a FIN (VICTORIA o DERROTA).
     * Lo usa JuegoTurnos para no reiniciar la música si el jugador
     * presiona Run después de que el combate ya terminó.
     */
    public boolean combateTerminado()
    {
        return estado == Estado.FIN;
    }

    private void habilitarBotones()
    {
        for (BotonAccion boton : botones)
        {
            boton.setHabilitado(true);
        }
    }

    private void deshabilitarBotones()
    {
        for (BotonAccion boton : botones)
        {
            boton.setHabilitado(false);
        }
    }
}
