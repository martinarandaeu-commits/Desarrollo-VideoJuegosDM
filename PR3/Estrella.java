import greenfoot.*;

public class Estrella extends Actor {

    private int velocidad;

    public Estrella() {

        velocidad =
            1 +
            Greenfoot
                .getRandomNumber(3);

        int tamanio =
            1 +
            Greenfoot
                .getRandomNumber(3);

        GreenfootImage img =
            new GreenfootImage(
                tamanio,
                tamanio
            );

        if (velocidad == 1) {
            img.setColor(Color.GRAY);

        } else {
            img.setColor(Color.WHITE);
        }

        img.fillOval(
            0,
            0,
            tamanio,
            tamanio
        );

        setImage(img);
    }

    public void act() {

        setLocation(
            getX() - velocidad,
            getY()
        );

        if (getX() <= 1) {

            setLocation(
                getWorld().getWidth() - 2,
                Greenfoot.getRandomNumber(
                    getWorld().getHeight()
                )
            );
        }
    }
}
