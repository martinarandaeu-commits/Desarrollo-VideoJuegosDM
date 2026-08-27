import greenfoot.*;

public class Nave extends Actor {

    private EstrategiaDisparo estrategia = new DisparoSimple();
    private PoolDeBalas pool;

    private int recarga = 0;

    // Evita activar la tecla 2 muchas veces por mantenerla presionada
    private boolean tecla2Anterior = false;

    // Mensajes temporales
    private int tiempoMensaje = 0;
    
    // Sistema de escudo temporal
    private boolean escudoActivo = false;
    private int tiempoEscudo = 0;

    // Aproximadamente 6 segundos
    private static final int DURACION_ESCUDO = 360;

    public Nave(PoolDeBalas pool) {
        this.pool = pool;

        crearImagenNave();
    }

    private void crearImagenNave() {
        GreenfootImage img = new GreenfootImage(48, 32);

        // Cuerpo principal
        img.setColor(Color.LIGHT_GRAY);
        img.fillOval(3, 4, 32, 24);

        // Dos extensiones delanteras
        img.fillRect(25, 7, 18, 6);
        img.fillRect(25, 19, 18, 6);

        // Centro de la nave
        img.setColor(Color.DARK_GRAY);
        img.fillOval(10, 9, 14, 14);

        // Cabina
        img.setColor(Color.CYAN);
        img.fillOval(29, 12, 6, 6);

        // Detalles
        img.setColor(Color.GRAY);
        img.drawOval(5, 6, 28, 20);

        setImage(img);
    }

    public void act() {
        GameManager gm = GameManager.getInstancia();

        if (gm.isJuegoTerminado()) {
            return;
        }
        
        actualizarEscudo();

        // Controla la duracion del power-up
        gm.actualizarTriple();

        // Mientras este activo siempre utilizamos disparo triple
        if (gm.isTripleActivo()) {
            estrategia = new DisparoTriple();

        } else if (estrategia instanceof DisparoTriple) {
            estrategia = new DisparoSimple();
        }

        mover();
        cambiarArma();
        disparar();
        actualizarMensaje();
    }

    private void mover() {
        if (Greenfoot.isKeyDown("up") && getY() > 15) {
            setLocation(
                getX(),
                getY() - 4
            );
        }

        if (Greenfoot.isKeyDown("down")
                && getY() < getWorld().getHeight() - 15) {

            setLocation(
                getX(),
                getY() + 4
            );
        }
    }

    private void cambiarArma() {
        GameManager gm = GameManager.getInstancia();

        // 1 vuelve al disparo simple
        // pero no puede cancelar el triple mientras esta activo
        if (Greenfoot.isKeyDown("1") && !gm.isTripleActivo()) {
            estrategia = new DisparoSimple();
        }

        boolean tecla2Actual = Greenfoot.isKeyDown("2");

        // Detectamos solamente el momento en que se presiona
        if (tecla2Actual && !tecla2Anterior) {

            if (gm.activarTriple()) {
                estrategia = new DisparoTriple();

                mostrarMensaje(
                    "TRIPLE SHOT ON!",
                    100
                );

            } else if (!gm.isTripleActivo()) {

                mostrarMensaje(
                    "TRIPLE NO DISPONIBLE",
                    70
                );
            }
        }

        tecla2Anterior = tecla2Actual;
    }

    private void disparar() {
        if (recarga > 0) {
            recarga--;
        }

        if (Greenfoot.isKeyDown("space") && recarga == 0) {
            estrategia.disparar(this, pool);

            recarga = 15;
        }
    }

    private void mostrarMensaje(String mensaje, int duracion) {
        if (getWorld() != null) {
            getWorld().showText(
                mensaje,
                getWorld().getWidth() / 2,
                30
            );

            tiempoMensaje = duracion;
        }
    }

    private void actualizarMensaje() {
        if (tiempoMensaje > 0) {
            tiempoMensaje--;

            if (tiempoMensaje == 0 && getWorld() != null) {
                getWorld().showText(
                    "",
                    getWorld().getWidth() / 2,
                    30
                );
            }
        }
    }
    
        public void activarEscudo() {
    
        escudoActivo = true;
        tiempoEscudo = DURACION_ESCUDO;
    
        mostrarMensaje(
            "SHIELD ON!",
            80
        );
    }
    
    private void actualizarEscudo() {
    
        if (!escudoActivo) {
            return;
        }
    
        tiempoEscudo--;
    
        if (tiempoEscudo <= 0) {
    
            tiempoEscudo = 0;
            escudoActivo = false;
    
            mostrarMensaje(
                "SHIELD OFF",
                60
            );
        }
    }
    
    public boolean isEscudoActivo() {
        return escudoActivo;
    }
    
    public int getProgresoEscudo() {
    
        if (!escudoActivo) {
            return 0;
        }
    
        return (
            tiempoEscudo * 100
        ) / DURACION_ESCUDO;
    }

    public void morir() {
        
        if (escudoActivo) {
        return;
        }
        
        GameManager gm = GameManager.getInstancia();

        if (gm.isJuegoTerminado()) {
            return;
        }

        gm.terminarJuego();

        if (getWorld() != null) {
            World mundo = getWorld();

            mundo.showText(
                "GAME OVER!",
                mundo.getWidth() / 2,
                mundo.getHeight() / 2
            );

            mundo.showText(
                "Puntaje final: " + gm.getPuntos(),
                mundo.getWidth() / 2,
                mundo.getHeight() / 2 + 35
            );

            mundo.showText(
                "Enemigos eliminados: " + gm.getEnemigosEliminados(),
                mundo.getWidth() / 2,
                mundo.getHeight() / 2 + 60
            );
        }

        Greenfoot.stop();
    }
}