import greenfoot.*;

public class BarraTriple extends Actor {

    private static final int ANCHO_BARRA = 150;
    private static final int ALTO_BARRA = 18;

    public BarraTriple() {
        actualizarBarra();
    }

    public void act() {
        actualizarBarra();
    }

    private void actualizarBarra() {
        GameManager gm = GameManager.getInstancia();

        int progreso = gm.getProgresoTriple();

        GreenfootImage img = new GreenfootImage(220, 45);

        // Texto
        img.setColor(Color.BLACK);
        img.drawString("TRIPLE", 5, 17);

        // Fondo de la barra
        img.setColor(Color.DARK_GRAY);
        img.fillRect(60, 4, ANCHO_BARRA, ALTO_BARRA);

        // Parte cargada
        if (progreso > 0) {
            if (gm.isTripleActivo()) {
                img.setColor(Color.ORANGE);
            } else {
                img.setColor(Color.CYAN);
            }

            int anchoCarga = (ANCHO_BARRA * progreso) / 100;

            img.fillRect(
                60,
                4,
                anchoCarga,
                ALTO_BARRA
            );
        }

        // Borde
        img.setColor(Color.BLACK);
        img.drawRect(
            60,
            4,
            ANCHO_BARRA,
            ALTO_BARRA
        );

        // Estado
        if (gm.isTripleActivo()) {
            img.setColor(Color.ORANGE);
            img.drawString("ACTIVE", 60, 40);

        } else if (gm.isTripleListo()) {
            img.setColor(Color.GREEN);
            img.drawString("READY - PRESS 2", 60, 40);

        } else {
            img.setColor(Color.BLACK);
            img.drawString(progreso + "%", 60, 40);
        }

        setImage(img);
    }
}