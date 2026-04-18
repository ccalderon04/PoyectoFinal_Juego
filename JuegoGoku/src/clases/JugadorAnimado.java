package clases;

import java.util.ArrayList;
import java.util.HashMap;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;


public class JugadorAnimado extends Jugador {

    private HashMap<String, Animacion> animaciones;
    private String animacionActual;
    private int xImagen;
    private int yImagen;
    private int anchoImagen;
    private int altoImagen;

    private int puntuacion;
    private int vidas;
    private double tiempoUltimoDisparo;
    private int xAnterior;
    private int yAnterior;

    public JugadorAnimado(int x, int y, String indiceImagen, int velocidad, String animacionActual) {
        super(x, y, indiceImagen, velocidad);
        this.animacionActual = animacionActual;
        this.vidas = 3;
        this.puntuacion = 0;
        this.tiempoUltimoDisparo = 0;
        inicializarAnimaciones();
    }

 
    public int getPuntuacion() {
    	return puntuacion; }
    
    public int getVidas()      {
    	return vidas; }

    public void sumarPuntos(int pts) {
        this.puntuacion += pts;
    }
    
    public void perderVida() {
        this.vidas--;
    }


    public void actualizarAnimacion(double t) {
        Rectangle coords = this.animaciones.get(animacionActual).calcularFrame(t);
        this.xImagen    = (int) coords.getX();
        this.yImagen    = (int) coords.getY();
        this.anchoImagen = (int) coords.getWidth();
        this.altoImagen  = (int) coords.getHeight();
    }

    
    @Override
    public void mover() {
      
        xAnterior = this.x;
        yAnterior = this.y;

        if (Juego.derecha)   this.x += velocidad;
        if (Juego.izquierda) this.x -= velocidad;
        if (Juego.arriba)    this.y -= velocidad;
        if (Juego.abajo)     this.y += velocidad;

        if (this.x < 70)                     this.x = 70; 
        if (this.x > 980 - anchoImagen)       this.x = 980 - anchoImagen; 

        if (this.y < 0) this.y = 0;

        if (this.y > Juego.yMinJugador) this.y = Juego.yMinJugador;
    }

    @Override
    public void pintar(GraphicsContext graficos) {
        int screenY = this.y - Juego.camaraY;
        graficos.drawImage(
        	    Juego.imagenes.get(this.indiceImagen),
        	    this.xImagen, this.yImagen,
        	    this.anchoImagen, this.altoImagen,
        	    this.x, screenY,
        	    40, 50 
        	);
    }

    
    @Override
    public Rectangle obtenerRectangulo() {
    	return new Rectangle(this.x, this.y, 40, 50);
    }

    
    public void intentarDisparar(ArrayList<Proyectil> proyectiles, double t) {
        if (t - tiempoUltimoDisparo >= 0.3) {
            tiempoUltimoDisparo = t;
            int proyectilX = this.x + anchoImagen / 2 - 5;
            int proyectilY = this.y - 15;
            proyectiles.add(new Proyectil(proyectilX, proyectilY, 10));
        }
    }
    public void verificarColisiones(Item item) {
        if (this.obtenerRectangulo().intersects(item.obtenerRectangulo().getBoundsInLocal())) {
            if (!item.isCapturado()) {
                this.puntuacion += 50;
                item.setCapturado(true);
            }
        }
    }

   
    public void verificarColisionConTiles(ArrayList<Tile> tiles) {
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            if (this.obtenerRectangulo().intersects(tile.obtenerRectangulo().getBoundsInLocal())) {
                this.x = xAnterior;
                this.y = yAnterior;
                break;
            }
        }
    }

    
    private void inicializarAnimaciones() {
        animaciones = new HashMap<String, Animacion>();
        Rectangle coordenadasCorrer[] = {
            new Rectangle(13,  229, 75, 68),
            new Rectangle(100, 229, 75, 68),
            new Rectangle(171, 229, 75, 68),
            new Rectangle(230, 229, 75, 68),
            new Rectangle(287, 224, 75, 73),
            new Rectangle(423, 229, 75, 68),
            new Rectangle(500, 229, 75, 68),
            new Rectangle(576, 229, 75, 68),
            new Rectangle(640, 229, 75, 68),
            new Rectangle(699, 229, 75, 68),
            new Rectangle(764, 229, 75, 68),
            new Rectangle(836, 229, 75, 73),
            new Rectangle(907, 229, 75, 68)
        };
        animaciones.put("correr", new Animacion("correr", coordenadasCorrer, 0.05));

        Rectangle coordenadasDescanso[] = {
            new Rectangle(26,  16, 63, 73),
            new Rectangle(89,  16, 63, 73),
            new Rectangle(154, 16, 63, 73),
            new Rectangle(226, 16, 63, 73)
        };
        animaciones.put("descanso", new Animacion("descanso", coordenadasDescanso, 0.2));
    }
}