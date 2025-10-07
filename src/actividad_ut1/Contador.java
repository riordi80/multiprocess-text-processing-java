package actividad_ut1;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Proceso hijo: convierte palabras a minúsculas, cuenta vocales (incluyendo acentos y ü)
 * e imprime resultados parciales en ficheros .res
 *
 * Salidas:
 *  - minusculas-X.res : mismas líneas en minúsculas (sin vacías)
 *  - vocales-X.res    : total de vocales encontradas
 *  - palabras-X.res   : total de palabras procesadas (líneas no vacías)
 */
public class Contador {

    /**
     * Procesa un fichero de entrada y genera sus .res en la misma carpeta.
     * @param archivo Ruta al fichero datosX.txt
     */
    public static void procesarArchivo(String archivo) {
        // nombreBase = "datos3" ; sufijo = "3"
        String nombreBase = archivo.substring(archivo.lastIndexOf("/") + 1, archivo.lastIndexOf("."));
        String sufijo = nombreBase.replaceAll("\\D+", "");
        String carpeta = archivo.substring(0, archivo.lastIndexOf("/"));

        String minusculasRes = carpeta + "/minusculas-" + sufijo + ".res";
        String vocalesRes    = carpeta + "/vocales-"    + sufijo + ".res";
        String palabrasRes   = carpeta + "/palabras-"   + sufijo + ".res";

        int totalVocales = 0;
        int totalPalabras = 0;
        final String VOC = "aeiouáéíóúü";

        // try-with-resources con UTF-8
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8));
            BufferedWriter bwMinus = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(minusculasRes), StandardCharsets.UTF_8))
        ) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Quitar BOM si aparece en la primera línea y espacios
                linea = linea.replace("\uFEFF", "").trim();
                if (linea.isEmpty()) continue; // ignorar líneas vacías

                totalPalabras++;

                String minuscula = linea.toLowerCase();
                bwMinus.write(minuscula);
                bwMinus.newLine();

                // Contar vocales
                for (int i = 0; i < minuscula.length(); i++) {
                    char c = minuscula.charAt(i);
                    if (VOC.indexOf(c) != -1) totalVocales++;
                }
            }

            // Escribir .res numéricos (UTF-8)
            try (BufferedWriter bwVoc = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(vocalesRes), StandardCharsets.UTF_8))) {
                bwVoc.write(String.valueOf(totalVocales));
            }
            try (BufferedWriter bwPal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(palabrasRes), StandardCharsets.UTF_8))) {
                bwPal.write(String.valueOf(totalPalabras));
            }

            System.out.println("OK " + nombreBase + " -> palabras=" + totalPalabras + ", vocales=" + totalVocales);

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: No se encuentra el archivo: " + archivo);
        } catch (IOException e) {
            System.err.println("ERROR I/O procesando " + archivo + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR inesperado en " + archivo + ": " + e.getMessage());
        }
    }

    /**
     * Uso: Contador <ruta-archivo>
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Uso: Contador <ruta-archivo>");
            System.exit(2);
        }
        procesarArchivo(args[0]);
    }
}
