package actividad_ut1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Proceso padre: lanza un hijo por fichero datosX.txt (en src/data),
 * espera su finalización y muestra un informe por fichero leyendo .res.
 */
public class Main {

    /** Lee un entero desde un fichero .res; devuelve 0 si hay problema. */
    private static int leerEntero(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String s = br.readLine();
            if (s == null) return 0;
            s = s.trim();
            return Integer.parseInt(s);
        } catch (Exception e) {
            System.err.println("Aviso: no se pudo leer/parsing int de " + ruta + " -> " + e.getMessage());
            return 0;
        }
    }

    public static void main(String[] args) throws Exception {
        // Base relativa al directorio desde el que ejecutas (raíz del proyecto)
        String sep = File.separator;
        String baseProyecto = System.getProperty("user.dir");
        String base = baseProyecto + sep + "src" + sep + "data";
        String classpath = System.getProperty("java.class.path");

        for (int i = 1; i <= 4; i++) {
            String archivo = base + sep + "datos" + i + ".txt";
            String sufijo = String.valueOf(i);

            System.out.println("\n>>> Lanzando hijo para: " + archivo);

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-cp", classpath, "actividad_ut1.Contador", archivo
            );

            Process p = pb.start();

            // Salida estándar del hijo
            try (BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String linea;
                while ((linea = out.readLine()) != null) {
                    System.out.println("Hijo[" + i + "] OUT: " + linea);
                }
            }
            // Errores del hijo
            try (BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
                String linea;
                while ((linea = err.readLine()) != null) {
                    System.err.println("Hijo[" + i + "] ERR: " + linea);
                }
            }

            int exit = p.waitFor();
            if (exit != 0) {
                System.err.println("ATENCIÓN: Hijo datos" + i + ".txt terminó con código " + exit +
                        ". Puede que falten .res o estén incompletos.");
            } else {
                System.out.println("Hijo datos" + i + ".txt finalizado correctamente.");
            }

            // Carpeta donde están los .res (misma que el .txt)
            String carpeta = new File(archivo).getParent();
            String vocalesRes  = carpeta + sep + "vocales-"  + sufijo + ".res";
            String palabrasRes = carpeta + sep + "palabras-" + sufijo + ".res";

            int totalVocales  = leerEntero(vocalesRes);
            int totalPalabras = leerEntero(palabrasRes);
            double promedio   = (totalPalabras > 0) ? (double) totalVocales / totalPalabras : 0.0;

            // Informe por fichero
            System.out.println("=== Informe datos" + sufijo + ".txt ===");
            System.out.println("Palabras procesadas: " + totalPalabras);
            System.out.println("Total de vocales:    " + totalVocales);
            System.out.printf ("Promedio vocales/palabra: %.3f%n", promedio);
        }
    }
}
