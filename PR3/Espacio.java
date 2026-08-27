import greenfoot.*;

public class Espacio extends World {

    private PoolDeBalas pool;

    private int contadorAparicion = 0;

    private int tiempoPartida = 0;

    private int nivel = 1;
    
    private int contadorPowerUp = 0;
    
    private int proximoPowerUp = 700;

    public Espacio() {

        super(
            600,
            400,
            1
        );

        crearFondo();

        GameManager
            .getInstancia()
            .reiniciar();

        crearEstrellas();

        pool =
            new PoolDeBalas(30);
            
        Nave nave =
            new Nave(pool);

        addObject(
            nave,
            60,
            getHeight() / 2
        );

        addObject(
            new Marcador(),
            80,
            20
        );

        addObject(
            new BarraTriple(),
            120,
            getHeight() - 28
        );
        
        addObject(
            new IndicadorEscudo(nave),
            getWidth() - 85,
            50
        );

        showText(
            "Nivel 1",
            getWidth() - 45,
            20
        );
    }

    private void crearFondo() {

        GreenfootImage fondo =
            new GreenfootImage(
                getWidth(),
                getHeight()
            );

        fondo.setColor(Color.BLACK);

        fondo.fill();

        setBackground(fondo);
    }

    private void crearEstrellas() {

        for (int i = 0; i < 55; i++) {

            Estrella estrella =
                new Estrella();

            addObject(
                estrella,
                Greenfoot.getRandomNumber(
                    getWidth()
                ),
                Greenfoot.getRandomNumber(
                    getHeight()
                )
            );
        }
    }

    public void act() {

        if (GameManager
                .getInstancia()
                .isJuegoTerminado()) {
            return;
        }

        tiempoPartida++;

        actualizarNivel();
        
        actualizarPowerUp();

        contadorAparicion++;

        int intervalo =
            obtenerIntervalo();

        if (contadorAparicion >=
            intervalo) {

            generarEnemigo();

            contadorAparicion = 0;
        }
    }

    private void actualizarNivel() {

        int nuevoNivel;

        // ~0-30 segundos
        if (tiempoPartida < 1800) {

            nuevoNivel = 1;

        // ~30-60 segundos
        } else if (
            tiempoPartida < 3600) {

            nuevoNivel = 2;

        // ~60-90 segundos
        } else if (
            tiempoPartida < 5400) {

            nuevoNivel = 3;

        } else {

            nuevoNivel = 4;
        }

        if (nuevoNivel != nivel) {

            nivel = nuevoNivel;

            showText(
                "Nivel " + nivel,
                getWidth() - 45,
                20
            );
        }
    }

    private int obtenerIntervalo() {

        if (nivel == 1) {
            return 80;
        }

        if (nivel == 2) {
            return 65;
        }

        if (nivel == 3) {
            return 55;
        }

        return 45;
    }

    private void generarEnemigo() {

        Enemigo enemigo =
            FabricaEnemigos.crear(
                nivel
            );

        int y =
            30 +
            Greenfoot
                .getRandomNumber(
                    getHeight() - 60
                );

        addObject(
            enemigo,
            getWidth() - 25,
            y
        );
    }
    
    private void actualizarPowerUp() {
    
        contadorPowerUp++;
    
        if (contadorPowerUp >= proximoPowerUp) {
    
            int y =
                50 +
                Greenfoot.getRandomNumber(
                    getHeight() - 100
                );
    
            addObject(
                new PowerUpEscudo(),
                getWidth() - 30,
                y
            );
    
            contadorPowerUp = 0;
    
            // Proxima aparicion aleatoria
            proximoPowerUp =
                700 +
                Greenfoot.getRandomNumber(
                    500
                );
        }
    }
}