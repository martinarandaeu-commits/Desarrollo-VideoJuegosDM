import greenfoot.*;

public abstract class Enemigo extends Actor {

    private int vida;
    private int puntosAlMorir;
    private int cargaAlMorir;

    private int flashImpacto = 0;

    public Enemigo(
        int vida,
        int puntosAlMorir,
        int cargaAlMorir
    ) {

        this.vida = vida;
        this.puntosAlMorir = puntosAlMorir;
        this.cargaAlMorir = cargaAlMorir;
    }

    public void act() {

        if (GameManager
                .getInstancia()
                .isJuegoTerminado()) {
            return;
        }

        actualizarFlash();

        actualizarComportamiento();

        if (getWorld() == null) {
            return;
        }

        Nave nave =
            (Nave) getOneIntersectingObject(
                Nave.class
            );

        if (nave != null) {
            nave.morir();
        }
    }

    protected abstract void actualizarComportamiento();

    public void recibirImpacto() {

        vida--;

        if (getImage() != null) {
            getImage().setTransparency(120);
            flashImpacto = 6;
        }

        if (vida <= 0) {
            destruir();
        }
    }

    private void actualizarFlash() {

        if (flashImpacto > 0) {

            flashImpacto--;

            if (flashImpacto == 0 &&
                getImage() != null) {

                getImage().setTransparency(255);
            }
        }
    }

    protected void destruir() {

        alSerDestruido();

        GameManager
            .getInstancia()
            .registrarEnemigoDestruido(
                puntosAlMorir,
                cargaAlMorir
            );

        if (getWorld() != null) {
            getWorld().removeObject(this);
        }
    }

    protected void alSerDestruido() {
        // Las subclases pueden limpiar
        // objetos especiales aqui.
    }

    public int getVida() {
        return vida;
    }
}