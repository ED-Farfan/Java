import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.sql.Timestamp;

class EjemploGSON {

  static String cadena;

  static void muestraContenido(String archivo)
    throws FileNotFoundException, IOException {
    FileReader f = new FileReader(archivo);
    BufferedReader b = new BufferedReader(f);
    while ((cadena = b.readLine()) != null) {
      System.out.println(cadena);
    }
    b.close();
  }
  
  static byte[] lee_archivo(String nombre_archivo) throws Exception {
    FileInputStream f = new FileInputStream(nombre_archivo);
    byte[] buffer;
    try {
      buffer = new byte[f.available()];
      f.read(buffer);
    } finally {
      f.close();
    }
    return buffer;
  }

  static class Coordenada {

    int x;
    int y;
    int z;

    Coordenada(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }

  ////

  public static void main(String[] args) {
    try {
      byte[] e = lee_archivo("coordenadas.txt");
      int x = 0, y = 0, z = 0;      
      Gson j = new GsonBuilder().create();
      String s = new String(e, "UTF-8");      
      Coordenada[] v = (Coordenada[])j.fromJson(s, Coordenada[].class);
      System.out.println(v);
      for (int i = 0; i < v.length; i++) {
        x += v[i].x;
        y += v[i].y;
        z += v[i].z;
      }
      
      System.out.println("Valor de x "+x);
      System.out.println("Valor de y "+y);
      System.out.println("Valor de z "+z);      
    } catch (Exception e) {
      System.out.println(e);
    }    

  }
}
