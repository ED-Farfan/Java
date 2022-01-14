import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Ejecutor {

  public static void main(String[] args) {
    ProcessBuilder pb = new ProcessBuilder();
    pb.command("Java", "Arg");
    try {
        Process process = pb.start(); 
           
    } catch ((IOException | InterruptedException)e) {
        System.out.println("La ejecución ha fallado");
    }
    
  }

}

