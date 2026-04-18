package implementacion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Puntajes {

    private static final String ARCHIVO = "puntajes.txt";
    private ArrayList<String> nombres;
    private ArrayList<Integer> puntuaciones;

    public Puntajes() {
        nombres      = new ArrayList<String>();
        puntuaciones = new ArrayList<Integer>();
        cargar();
    }

  
    public void cargar() {
        try {
            BufferedReader lector = new BufferedReader(new FileReader(ARCHIVO));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    nombres.add(partes[0].trim());
                    puntuaciones.add(Integer.parseInt(partes[1].trim()));
                }
            }
            lector.close();
        } catch (IOException e) {
          
        }
    }

    
    public void guardar() {
        try {
            BufferedWriter escritor = new BufferedWriter(new FileWriter(ARCHIVO));
            for (int i = 0; i < nombres.size(); i++) {
                escritor.write(nombres.get(i) + "," + puntuaciones.get(i));
                escritor.newLine();
            }
            escritor.close();
        } catch (IOException e) {
            System.out.println("Error al guardar puntajes: " + e.getMessage());
        }
    }

    
    public boolean esTopDiez(int puntuacion) {
        if (puntuaciones.size() < 10) return true;
        int minimo = puntuaciones.get(puntuaciones.size() - 1);
        return puntuacion > minimo;
    }

    
    public void agregar(String nombre, int puntuacion) {
        nombres.add(nombre);
        puntuaciones.add(puntuacion);

       
        for (int i = 0; i < nombres.size() - 1; i++) {
            for (int j = 0; j < nombres.size() - 1 - i; j++) {
                if (puntuaciones.get(j) < puntuaciones.get(j + 1)) {
                    int tempPun = puntuaciones.get(j);
                    puntuaciones.set(j, puntuaciones.get(j + 1));
                    puntuaciones.set(j + 1, tempPun);
                    String tempNom = nombres.get(j);
                    nombres.set(j, nombres.get(j + 1));
                    nombres.set(j + 1, tempNom);
                }
            }
        }

        while (nombres.size() > 10) {
            nombres.remove(nombres.size() - 1);
            puntuaciones.remove(puntuaciones.size() - 1);
        }

        guardar();
    }

    public ArrayList<String> getPuntajesFormateados() {
        ArrayList<String> resultado = new ArrayList<String>();
        for (int i = 0; i < nombres.size(); i++) {
            resultado.add(nombres.get(i) + " - " + puntuaciones.get(i));
        }
        return resultado;
    }
}



