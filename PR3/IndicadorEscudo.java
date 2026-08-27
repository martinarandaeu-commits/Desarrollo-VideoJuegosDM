import greenfoot.*;

public class IndicadorEscudo extends Actor {

    private Nave nave;

    public IndicadorEscudo(Nave nave) {
        this.nave = nave;
        actualizar();
    }

    public void act() {
        actualizar();
    }

    private void actualizar() {

        GreenfootImage img =
            new GreenfootImage(180, 32);

        if (nave != null &&
            nave.isEscudoActivo()) {

            int progreso =
                nave.getProgresoEscudo();

            img.setColor(Color.CYAN);

            img.drawString(
                "SHIELD: " +
                progreso +
                "%",
                5,
                20
            );

        } else {

            img.setColor(Color.GRAY);

            img.drawString(
                "SHIELD: --",
                5,
                20
            );
        }

        setImage(img);
    }
}
