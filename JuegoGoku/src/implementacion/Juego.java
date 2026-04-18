package implementacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import clases.EnemigoBasico;
import clases.EnemigoFuerte;
import clases.EnemigoRapido;
import clases.Enemigo;
import clases.Item;
import clases.JugadorAnimado;
import clases.Proyectil;
import clases.Tile;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Juego extends Application {

    public static boolean derecha    = false;
    public static boolean izquierda  = false;
    public static boolean arriba     = false;   //estas son las Teclas presionadas (true mientras la tecla esta sostenida)
    public static boolean abajo      = false;
    public static boolean disparo    = false;
    public static HashMap<String, Image> imagenes;
    public static int camaraY = 900;  // esta es la camara de desplazamiento vertical del mundo visible
    public static int yMinJugador = 1400;  //para que no pueda retroceder en el escenario
    private Scene escena;
    private Group root;
    private Canvas canvas;
    private GraphicsContext graficos;

    private JugadorAnimado jugadorAnimado;
    private ArrayList<Tile>      tiles;
    private ArrayList<Item>      items;
    private ArrayList<Enemigo>   enemigos;
    private ArrayList<Proyectil> proyectiles;

    private Puntajes puntajes;
    private String estado = "JUGANDO";

    // ------------------------------------------------------------------
    // MAPA DEL NIVEL (20 filas x 14 columnas de 70x70 px)
    // Coordenadas: x = columna*70, y = fila*70
    // Mundo: 980 px ancho, 1400 px alto
    // 0 = espacio vacio | 1,2,3,4,5,6 = diferentes tiles del tilemap
    // El jugador comienza en la fila 18 y debe llegar a la fila 1
    // ------------------------------------------------------------------
    private int[][] mapa = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
        {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 0, 1},  
        {1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1},  
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},  
        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1}, 
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
        {1, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 2, 1},  
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
        {1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 1},  
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
        {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1},  
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
        {1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1},  
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
        {1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1},  
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, 
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},  
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},  
    };

    
    // SPAWNS DE enemigos planificados, no aleatorios, asi como lo pide en el PDF del proyecti)
    private int[][] spawns = {
        {350,  1050, 1},   // fila 15 - primer encuentro
        {560,  1050, 2},
        {210,   840, 1},   // fila 12 - segundo encuentro
        {700,   840, 3},   // enemigo fuerte
        {350,   630, 2},   // fila 9
        {560,   630, 2},
        {140,   420, 3},   // fila 6
        {700,   420, 1},
        {280,   210, 2},   // fila 3 - zona final
        {630,   210, 3},
        {420,    70, 1},   // fila 1 - guardian final
    };
    private boolean[] spawnsActivados;

    // ------------------------------------------------------------------

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ventana) throws Exception {
        inicializarComponentes();
        graficos = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        ventana.setScene(escena);
        ventana.setTitle("Aventura de Megaman");
        gestionarEventos();
        ventana.show();
        cicloJuego();
    }

    

    public void inicializarComponentes() {
        root    = new Group();
        escena  = new Scene(root, 1000, 500);
        canvas  = new Canvas(1000, 500);

        imagenes = new HashMap<String, Image>();
        cargarImagenes();

        
        jugadorAnimado = new JugadorAnimado(450, 1260, "megaman", 3, "correr");   // El jugador empieza abajo del mundo en la fila 18

        tiles       = new ArrayList<Tile>();
        items       = new ArrayList<Item>();
        enemigos    = new ArrayList<Enemigo>();
        proyectiles = new ArrayList<Proyectil>();
        puntajes    = new Puntajes();
        spawnsActivados = new boolean[spawns.length];

        cargarTiles();
        cargarItems();
        camaraY      = 880;
        yMinJugador  = camaraY + 400; // el jugador no puede retroceder mas de esto
    }

    public void cargarImagenes() {
        imagenes.put("goku",          new Image("goku.png"));
        imagenes.put("goku-furioso",  new Image("goku-furioso.png"));
        imagenes.put("tilemap",       new Image("tilemap.png"));
        imagenes.put("megaman",       new Image("megaman.png"));
        imagenes.put("item",          new Image("item.png"));
    }

    
    public void cargarTiles() {
        for (int fila = 0; fila < mapa.length; fila++) {
            for (int col = 0; col < mapa[fila].length; col++) {
                if (mapa[fila][col] != 0) {
                    int mundoX = col  * 70;
                    int mundoY = fila * 70;
                    tiles.add(new Tile(mapa[fila][col], mundoX, mundoY, "tilemap", 0));
                }
            }
        }
    }

    
    public void cargarItems() {
        items.add(new Item(490,  280, 0, 0, "item")); // fila 4
        items.add(new Item(280,  560, 0, 0, "item")); // fila 8  izquierda del muro
        items.add(new Item(700,  560, 0, 0, "item")); // fila 8  derecha del muro
        items.add(new Item(490,  770, 0, 0, "item")); // fila 11
        items.add(new Item(350,  980, 0, 0, "item")); // fila 14
        items.add(new Item(630,  980, 0, 0, "item")); // fila 14
    }

    public void gestionarEventos() {
        escena.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evento) {
                switch (evento.getCode().toString()) {
                    case "RIGHT":  derecha   = true;  break;
                    case "LEFT":   izquierda = true;  break;
                    case "UP":     arriba    = true;  break;
                    case "DOWN":   abajo     = true;  break;
                    case "SPACE":  disparo   = true;  break;
                }
            }
        });

        escena.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent evento) {
                switch (evento.getCode().toString()) {
                    case "RIGHT":  derecha   = false; break;
                    case "LEFT":   izquierda = false; break;
                    case "UP":     arriba    = false; break;
                    case "DOWN":   abajo     = false; break;
                    case "SPACE":  disparo   = false; break;
                }
            }
        });
    }



    public void cicloJuego() {
        final long tiempoInicial = System.nanoTime();  //  Ciclo del juego a 60 FPS 
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long tiempoActual) {
                double t = (tiempoActual - tiempoInicial) / 1_000_000_000.0;
                pintar();
                actualizar(t);
            }
        };
        timer.start();
    }

   

    public void pintar() {
       
        graficos.setFill(Color.BLACK);
        graficos.fillRect(0, 0, 1000, 500);

        
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).pintar(graficos);
        }

        
        for (int i = 0; i < items.size(); i++) {
            items.get(i).pintar(graficos);
        }

        
        for (int i = 0; i < enemigos.size(); i++) {
            enemigos.get(i).pintar(graficos);
        }

       
        for (int i = 0; i < proyectiles.size(); i++) {
            proyectiles.get(i).pintar(graficos);
        }

       
        jugadorAnimado.pintar(graficos);

        
        graficos.setFont(Font.font("Arial", 16));
        graficos.setFill(Color.WHITE);
        graficos.fillText("Puntuacion: " + jugadorAnimado.getPuntuacion(), 10, 20);
        graficos.fillText("Vidas: " + jugadorAnimado.getVidas(), 10, 40);
        graficos.fillText("ESPACIO = Disparar   Flechas = Moverse", 650, 20);

        if (estado.equals("GAME_OVER")) {
            graficos.setFill(new Color(0, 0, 0, 0.65));
            graficos.fillRect(0, 0, 1000, 500);

            graficos.setFont(Font.font("Arial", 52));
            graficos.setFill(Color.RED);
            graficos.fillText("GAME OVER", 310, 200);

            graficos.setFont(Font.font("Arial", 18));
            graficos.setFill(Color.WHITE);
            graficos.fillText("Top 10 Puntuaciones:", 370, 250);

            ArrayList<String> top = puntajes.getPuntajesFormateados();
            for (int i = 0; i < top.size(); i++) {
                graficos.fillText((i + 1) + ". " + top.get(i), 370, 270 + i * 22);
            }
        }

        
        if (estado.equals("GANASTE")) {
            graficos.setFill(new Color(0, 0, 0, 0.65));
            graficos.fillRect(0, 0, 1000, 500);

            graficos.setFont(Font.font("Arial", 52));
            graficos.setFill(Color.GOLD);
            graficos.fillText("!GANASTE!", 320, 220);

            graficos.setFont(Font.font("Arial", 22));
            graficos.setFill(Color.WHITE);
            graficos.fillText("Puntuacion final: " + jugadorAnimado.getPuntuacion(), 350, 270);
        }
    }

   

    public void actualizar(double t) {
        if (!estado.equals("JUGANDO")) return;

        // 1) Mover jugador
        jugadorAnimado.mover();
        jugadorAnimado.actualizarAnimacion(t);

        // 2) Scroll de camara, si el jugador sube bastante, la camara lo sigue
        int screenY = jugadorAnimado.getY() - camaraY;
        if (screenY < 180 && camaraY > 0) {
            camaraY -= jugadorAnimado.getVelocidad();
            if (camaraY < 0) camaraY = 0;
        }
        // Actualizar el limite inferior osea no se puede retroceder
        yMinJugador = camaraY + 420;

        // 3) Disparo con ESPACIO
        if (disparo) {
            jugadorAnimado.intentarDisparar(proyectiles, t);
        }

        // 4) Mover proyectiles y comprobar colisiones proyectil-enemigo
        for (int i = proyectiles.size() - 1; i >= 0; i--) {
            Proyectil p = proyectiles.get(i);
            p.mover();
            // Eliminar si salio de pantalla por arriba
            if (p.getY() - camaraY < -20) {
                proyectiles.remove(i);
                continue;
            }
            // Verificar colision con cada enemigo
            boolean impacto = false;
            for (int j = enemigos.size() - 1; j >= 0; j--) {
                Enemigo e = enemigos.get(j);
                if (p.obtenerRectangulo().intersects(e.obtenerRectangulo().getBoundsInLocal())) {
                    e.recibirDanio();
                    proyectiles.remove(i);
                    impacto = true;
                    if (!e.isActivo()) {
                        enemigos.remove(j);
                        jugadorAnimado.sumarPuntos(100);
                    }
                    break;
                }
            }
            if (impacto) continue;
        }

        // Activar spawns planificados cuando el jugador se acerque
        for (int i = 0; i < spawns.length; i++) {
            if (!spawnsActivados[i]) {
                int distancia = jugadorAnimado.getY() - spawns[i][1];
                if (distancia >= 0 && distancia < 500) {
                    crearEnemigo(spawns[i][0], spawns[i][1], spawns[i][2]);
                    spawnsActivados[i] = true;
                }
            }
        }

        //  Mover enemigos hacia el jugador y verificar colision del jugador con el enemigo
        for (int i = enemigos.size() - 1; i >= 0; i--) {
            Enemigo e = enemigos.get(i);
            e.moverHaciaJugador(jugadorAnimado.getX(), jugadorAnimado.getY());

            if (jugadorAnimado.obtenerRectangulo().intersects(e.obtenerRectangulo().getBoundsInLocal())) {
                jugadorAnimado.perderVida();
                enemigos.remove(i); // el enemigo desaparece al chocar
                if (jugadorAnimado.getVidas() <= 0) {
                    terminarJuego();
                }
            }
        }

        //  Colision jugador con items
        for (int i = 0; i < items.size(); i++) {
            jugadorAnimado.verificarColisiones(items.get(i));
        }

        //  Colision jugador con tiles (muros solidos)
        jugadorAnimado.verificarColisionConTiles(tiles);

        //  Condicion de victoria, jugador llego a la fila 1  donde y < 140
        if (jugadorAnimado.getY() < 140) {
            jugadorAnimado.sumarPuntos(500); // bonus por completar
            verificarRecordYGanar();
        }
    }

    private void crearEnemigo(int x, int y, int tipo) {
        Enemigo e;
        switch (tipo) {
            case 1:  e = new EnemigoBasico(x, y);  break;
            case 2:  e = new EnemigoRapido(x, y);  break;
            default: e = new EnemigoFuerte(x, y);  break;
        }
        enemigos.add(e);
    }

 
    private void terminarJuego() {
        estado = "GAME_OVER";
        if (puntajes.esTopDiez(jugadorAnimado.getPuntuacion())) {
            pedirNombreYGuardar();
        }
    }

   
    private void verificarRecordYGanar() {
        estado = "GANASTE";
        if (puntajes.esTopDiez(jugadorAnimado.getPuntuacion())) {
            pedirNombreYGuardar();
        }
    }

    
    private void pedirNombreYGuardar() {
        TextInputDialog dialogo = new TextInputDialog("Jugador");
        dialogo.setTitle("Nuevo Record!");
        dialogo.setHeaderText("Puntuacion: " + jugadorAnimado.getPuntuacion());
        dialogo.setContentText("Ingresa tu nombre:");
        Optional<String> resultado = dialogo.showAndWait();
        if (resultado.isPresent() && !resultado.get().isEmpty()) {
            puntajes.agregar(resultado.get(), jugadorAnimado.getPuntuacion());
        }
    }
}





