package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Enemigo extends Jugador {

    protected int salud;
    protected boolean activo; 
    protected int anchoSprite;
    protected int altoSprite;

    public Enemigo(int x, int y, String indiceImagen, int velocidad, int salud,
                   int anchoSprite, int altoSprite) {
        super(x, y, indiceImagen, velocidad);
        this.salud = salud;
        this.activo = true;
        this.anchoSprite = anchoSprite;
        this.altoSprite  = altoSprite;
    }

    public boolean isActivo() {
    	return activo; }
    @Override
    public void mover() {
    }

    public void moverHaciaJugador(int jugadorX, int jugadorY) {
        if (!activo) return;

        if (this.x < jugadorX) this.x += velocidad;
        else if (this.x > jugadorX) this.x -= velocidad;

        if (this.y < jugadorY) this.y += velocidad;
        else if (this.y > jugadorY) this.y -= velocidad;
    }

    
    public void recibirDanio() {
        this.salud--;
        if (this.salud <= 0) {
            this.activo = false;
        }
    }

    
    @Override
    public void pintar(GraphicsContext graficos) {
        if (!activo) return;
        int screenY = this.y - Juego.camaraY;
        graficos.drawImage(
            Juego.imagenes.get(this.indiceImagen),
            this.x, screenY,
            this.anchoSprite, this.altoSprite
        );
    }

   
    @Override
    public Rectangle obtenerRectangulo() {
        return new Rectangle(this.x, this.y, anchoSprite, altoSprite);
    }
}