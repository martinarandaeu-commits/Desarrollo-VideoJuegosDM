import greenfoot.*;

/**
 * Clase Jugador.
 *
 * Implementa fielmente la logica descrita en la guia
 * "Sprites, estados y salto parabolico":
 *
 *   Entrada -> Estado -> Comportamiento -> Representacion visual
 *
 * Drivers arquitectonicos aplicados (seccion 2 de la guia):
 *  - Jugabilidad:      controlarMovimientoHorizontal() separa lectura
 *                       de teclado, movimiento y estado.
 *  - Respuesta visual:  cada Estado tiene asociado uno o mas sprites
 *                       (actualizarAnimacion()).
 *  - Movimiento natural: el salto se modela con la ecuacion cuadratica
 *                       y(t) = y0 - v0*t + 0.5*g*t^2 (actualizarSalto()).
 *  - Mantenibilidad:    el estado se representa con un enum en vez de
 *                       valores numericos ambiguos (1, 2, 3...).
 *  - Simplicidad:       no se usa un motor fisico, solo la ecuacion
 *                       cuadratica explicita.
 *  - Extensibilidad:    ver bloque "EXTENSIONES" mas abajo (Desafios
 *                       1, 2 y 3 de la guia).
 *  - Rendimiento:       AnimadorJugador controla la frecuencia de
 *                       cambio de frames mediante su contador interno.
 *
 * -----------------------------------------------------------------------
 * CAMBIO GRANDE (camara scroll): el jugador ya NO razona en coordenadas
 * de pantalla (getX()/getY()). Guarda su posicion AUTORITATIVA en
 * coordenadas de MUNDO (mundoX, mundoY: pixeles del mapa 958x550) y en
 * que PISO esta (1..4). El movimiento, el salto cuadratico y la fisica de
 * piso operan sobre esas variables. Mundo.act() se encarga, DESPUES del
 * act() de este Actor, de traducir (mundoX,mundoY) a la posicion de
 * pantalla via setLocation() y de mover la camara. Ver Mundo.java.
 *
 * Subir/bajar de piso: tecla ARRIBA/ABAJO estando sobre una escalera
 * (controlarEscaleras()); produce un deslizamiento corto y diagonal
 * hasta el piso destino (estado EN_ESCALERA).
 */
public class Jugador extends Actor
{
    // ---------------------------------------------------------------
    // Paso 2: estados del jugador (Estado + CAYENDO, ver Desafio 2)
    // ---------------------------------------------------------------
    private enum Estado
    {
        QUIETO,
        CAMINANDO_DERECHA,
        CAMINANDO_IZQUIERDA,
        SALTANDO,
        CAYENDO,      // EXTENSION: Desafio 2 de la guia
        ATACANDO,     // EXTENSION: ataque de patada
        EN_ESCALERA,  // EXTENSION: desplazamiento entre pisos
        BAILANDO      // EXTENSION: baile D / F / automatico
    }

    private Estado estado = Estado.QUIETO;

    // ---------------------------------------------------------------
    // Coordenadas de MUNDO (no de pantalla) y piso actual
    // ---------------------------------------------------------------
    private double mundoX = 0;
    private double mundoY = 0;
    private int pisoActual = Mundo.TOTAL_PISOS;

    // ---------------------------------------------------------------
    // Responsabilidades delegadas
    // ---------------------------------------------------------------
    private AnimadorJugador animador;
    private AtaqueJugador ataque;
    private BaileJugador baile;
    private MovimientoEscaleraJugador movimientoEscalera;
    private SaludJugador salud;

    // ---------------------------------------------------------------
    // Paso 11: variables del salto
    // ---------------------------------------------------------------
    private boolean saltando = false;

    private int tiempoSalto = 0;
    private double yInicial;

    private double velocidadInicial = 12.0;
    private double gravedad = 1.0;

    // Velocidad horizontal.
    private static final int VELOCIDAD_HORIZONTAL = 2;

    // Orientacion actual del jugador.
    private boolean mirandoIzquierda = false;

    // ---------------------------------------------------------------
    // BAILE
    // Jugador conserva el estado logico BAILANDO y este booleano para
    // mantener exactamente el mismo flujo de act().
    // ---------------------------------------------------------------
    private boolean bailando = false;

    // ---------------------------------------------------------------
    // Fisica de salto / caida
    // ---------------------------------------------------------------
    private double impulsoActual = 0.0;
    private boolean caidaLibre = false;

    // ---------------------------------------------------------------
    // Puerta final
    // ---------------------------------------------------------------
    private static final int PUERTA_FINAL_PISO = 1;
    private static final int PUERTA_FINAL_X = 130;
    private static final int PUERTA_FINAL_TOL = 32;
    private boolean juegoCompletado = false;

    // ---------------------------------------------------------------
    // Puntuacion
    // ---------------------------------------------------------------
    private int puntos = 0;

    public Jugador()
    {
        animador = new AnimadorJugador(this);
        ataque = new AtaqueJugador(this, animador);
        baile = new BaileJugador(this, animador);
        movimientoEscalera = new MovimientoEscaleraJugador(this);
        salud = new SaludJugador(this);

        animador.mostrarQuietoBase();
    }

    /**
     * Coloca al jugador en coordenadas de MUNDO.
     */
    public void colocar(double mundoX, int piso, double mundoY)
    {
        this.mundoX = mundoX;
        this.pisoActual = piso;
        this.mundoY = mundoY;
    }

    public double getMundoX()
    {
        return mundoX;
    }

    public double getMundoY()
    {
        return mundoY;
    }

    public int getPiso()
    {
        return pisoActual;
    }

    /**
     * Uso interno por MovimientoEscaleraJugador.
     * Actualiza la posicion autoritativa de mundo sin tocar directamente
     * las coordenadas de pantalla.
     */
    void actualizarPosicionMundo(double nuevoX, double nuevoY)
    {
        mundoX = nuevoX;
        mundoY = nuevoY;
    }

    /**
     * Uso interno por MovimientoEscaleraJugador al completar
     * una transicion entre pisos.
     */
    void actualizarPiso(int nuevoPiso)
    {
        pisoActual = nuevoPiso;
    }

    // ---------------------------------------------------------------
    // Paso 15: ciclo principal
    // Entrada -> Estado -> Movimiento -> Animacion
    // ---------------------------------------------------------------
    public void act()
    {
        if (salud.estaMuerto() || juegoCompletado)
        {
            return;
        }

        // El cooldown ahora pertenece al sistema de escaleras.
        movimientoEscalera.actualizarCooldown();

        // Mientras se desplaza por una escalera, el jugador no responde
        // a otras entradas: solo avanza la interpolacion.
        if (movimientoEscalera.estaDeslizando())
        {
            if (movimientoEscalera.actualizar())
            {
                estado = Estado.QUIETO;
            }

            actualizarAnimacion();
            return;
        }

        // Mientras baila solo avanza la coreografia y puede recibir dano.
        if (bailando)
        {
            actualizarBaile();

            salud.actualizarInvulnerabilidad();

            salud.verificarContactoEnemigo(
                movimientoEscalera.estaDeslizando()
            );

            return;
        }

        controlarMovimientoHorizontal();

        verificarSuelo();
        controlarSalto();
        controlarEscaleras();
        controlarPuertaFinal();
        controlarBaile();

        if (bailando)
        {
            return;
        }

        controlarAtaque();
        actualizarSalto();
        actualizarAnimacion();

        salud.actualizarInvulnerabilidad();

        salud.verificarContactoEnemigo(
            movimientoEscalera.estaDeslizando()
        );
    }

    // ---------------------------------------------------------------
    // BAILE
    // ---------------------------------------------------------------
    private void controlarBaile()
    {
        if (baile.intentarIniciar(
            estado == Estado.QUIETO,
            saltando,
            ataque.estaAtacando()))
        {
            bailando = true;
            estado = Estado.BAILANDO;
        }
    }

    private void actualizarBaile()
    {
        if (baile.actualizar(mirandoIzquierda))
        {
            bailando = false;
            estado = Estado.QUIETO;
        }
    }

    /**
     * Reubica los pies del jugador sobre la linea de su piso.
     * Utilizado por BaileJugador.
     */
    void ajustarAlSueloActual()
    {
        mundoY = ((Mundo) getWorld()).getSuelo(
            pisoActual,
            getImage().getHeight()
        );
    }

    // ---------------------------------------------------------------
    // Paso 5: movimiento horizontal
    // ---------------------------------------------------------------
    private void controlarMovimientoHorizontal()
    {
        if (ataque.estaAtacando())
        {
            return;
        }

        if (Greenfoot.isKeyDown("right"))
        {
            mundoX += VELOCIDAD_HORIZONTAL;
            mirandoIzquierda = false;

            if (!saltando)
            {
                estado = Estado.CAMINANDO_DERECHA;
            }
        }
        else if (Greenfoot.isKeyDown("left"))
        {
            mundoX -= VELOCIDAD_HORIZONTAL;
            mirandoIzquierda = true;

            if (!saltando)
            {
                estado = Estado.CAMINANDO_IZQUIERDA;
            }
        }
        else
        {
            if (!saltando)
            {
                estado = Estado.QUIETO;
            }
        }

        // Muro duro: no se puede salir del mapa.
        if (mundoX < Mundo.X_MIN_MAPA)
        {
            mundoX = Mundo.X_MIN_MAPA;
        }

        if (mundoX > Mundo.X_MAX_MAPA)
        {
            mundoX = Mundo.X_MAX_MAPA;
        }
    }

    // ---------------------------------------------------------------
    // Paso 12: iniciar salto
    // ---------------------------------------------------------------
    private void controlarSalto()
    {
        if (Greenfoot.isKeyDown("space")
            && !saltando
            && !ataque.estaAtacando())
        {
            iniciarSalto();
        }
    }

    // ---------------------------------------------------------------
    // ESCALERAS
    //
    // La lectura de ARRIBA/ABAJO, seleccion de escalera,
    // interpolacion y cooldown se delegan completamente a
    // MovimientoEscaleraJugador.
    //
    // Jugador conserva solamente Estado.EN_ESCALERA.
    // ---------------------------------------------------------------
    private void controlarEscaleras()
    {
        if (movimientoEscalera.intentarIniciar(
            saltando,
            ataque.estaAtacando()))
        {
            estado = Estado.EN_ESCALERA;
        }
    }

    // ---------------------------------------------------------------
    // ATAQUE
    // ---------------------------------------------------------------
    private void controlarAtaque()
    {
        if (ataque.intentarIniciar(saltando))
        {
            estado = Estado.ATACANDO;
        }

        if (ataque.actualizar(mirandoIzquierda))
        {
            estado = Estado.QUIETO;
        }
    }

    void sumarPuntos(int cantidad)
    {
        puntos += cantidad;
    }

    public int getPuntos()
    {
        return puntos;
    }

    // ---------------------------------------------------------------
    // Vida, dano e invulnerabilidad delegados a SaludJugador
    // ---------------------------------------------------------------
    public void recibirDano()
    {
        salud.recibirDano();
    }

    public int getVida()
    {
        return salud.getVida();
    }

    public boolean isJuegoTerminado()
    {
        return salud.estaMuerto();
    }

    public boolean isJuegoCompletado()
    {
        return juegoCompletado;
    }

    // ---------------------------------------------------------------
    // PUERTA FINAL
    // ---------------------------------------------------------------
    private void controlarPuertaFinal()
    {
        if (movimientoEscalera.estaDeslizando()
            || saltando)
        {
            return;
        }

        if (pisoActual == PUERTA_FINAL_PISO
            && Math.abs(
                mundoX - PUERTA_FINAL_X
            ) <= PUERTA_FINAL_TOL
            && Greenfoot.isKeyDown("up"))
        {
            juegoCompletado = true;
            estado = Estado.QUIETO;
        }
    }

    // ---------------------------------------------------------------
    // SALTO
    // ---------------------------------------------------------------
    private void iniciarSalto()
    {
        saltando = true;
        tiempoSalto = 0;
        yInicial = mundoY;

        // Salto real: impulso completo.
        impulsoActual = velocidadInicial;

        caidaLibre = false;

        estado = Estado.SALTANDO;
    }

    // ---------------------------------------------------------------
    // Fisica de piso
    // ---------------------------------------------------------------
    private void verificarSuelo()
    {
        if (saltando
            || ataque.estaAtacando()
            || movimientoEscalera.estaDeslizando())
        {
            return;
        }

        double sueloEsperado =
            calcularSueloDebajo();

        double diferencia =
            mundoY - sueloEsperado;

        if (Math.abs(diferencia) > 2)
        {
            if (diferencia < 0)
            {
                // Flotando: inicia caida libre.
                saltando = true;
                tiempoSalto = 0;
                yInicial = mundoY;

                impulsoActual = 0.0;
                caidaLibre = true;

                estado = Estado.CAYENDO;
            }
            else
            {
                // Hundido: reposicion directa.
                mundoY = sueloEsperado;
            }
        }
        else if (diferencia != 0)
        {
            mundoY = sueloEsperado;
        }
    }

    /**
     * Centro Y esperado del jugador para que sus pies queden
     * sobre la linea de piso.
     */
    private double calcularSueloDebajo()
    {
        return ((Mundo) getWorld()).getSuelo(
            pisoActual,
            getImage().getHeight()
        );
    }

    // ---------------------------------------------------------------
    // Paso 13: ecuacion cuadratica
    //
    // y(t) = y0 - v0*t + 0.5*g*t^2
    // ---------------------------------------------------------------
    private void actualizarSalto()
    {
        if (!saltando)
        {
            return;
        }

        tiempoSalto++;

        double y =
            yInicial
            - impulsoActual * tiempoSalto
            + 0.5
                * gravedad
                * tiempoSalto
                * tiempoSalto;

        double tMax =
            impulsoActual / gravedad;

        estado =
            (tiempoSalto < tMax)
                ? Estado.SALTANDO
                : Estado.CAYENDO;

        mundoY = y;

        if (caidaLibre)
        {
            double sueloFinal =
                ((Mundo) getWorld()).getSuelo(
                    pisoActual,
                    getImage().getHeight()
                );

            if (y >= sueloFinal)
            {
                aterrizar(sueloFinal);
            }
        }
        else if (y >= yInicial
            && tiempoSalto > 1)
        {
            aterrizar(yInicial);
        }
    }

    private void aterrizar(double ySuelo)
    {
        mundoY = ySuelo;

        saltando = false;
        caidaLibre = false;

        estado = Estado.QUIETO;
    }

    // ---------------------------------------------------------------
    // Paso 8: asociar estados a imagenes
    // ---------------------------------------------------------------
    private void actualizarAnimacion()
    {
        if (estado == Estado.QUIETO)
        {
            animador.mostrarQuieto(
                mirandoIzquierda
            );
        }
        else if (
            estado
            == Estado.CAMINANDO_DERECHA)
        {
            animador.animarCaminata(false);
        }
        else if (
            estado
            == Estado.CAMINANDO_IZQUIERDA)
        {
            animador.animarCaminata(true);
        }
        else if (
            estado
            == Estado.SALTANDO)
        {
            animador.mostrarSaltando(
                mirandoIzquierda
            );
        }
        else if (
            estado
            == Estado.CAYENDO)
        {
            animador.mostrarCayendo(
                mirandoIzquierda
            );
        }
        else if (
            estado
            == Estado.ATACANDO)
        {
            animador.animarAtaque(
                mirandoIzquierda
            );
        }
        else if (
            estado
            == Estado.EN_ESCALERA)
        {
            // Mientras cambia de piso reutiliza
            // la animacion de caminata.
            animador.animarCaminata(
                mirandoIzquierda
            );
        }
    }
}