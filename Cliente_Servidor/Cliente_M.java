import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

class Cliente {

  // lee del DataInputStream todos los bytes requeridos

  static void read(DataInputStream f, byte[] b, int posicion, int longitud)
    throws Exception {
    while (longitud > 0) {
      int n = f.read(b, posicion, longitud);
      posicion += n;
      longitud -= n;
    }
  }

  public static void main(String[] args) throws Exception {
    try {
        System.out.println("Cliente Modificado\n");
      Socket conexion = new Socket("localhost", 50000);
      DataOutputStream salida = new DataOutputStream(
        conexion.getOutputStream()
      );
      DataInputStream entrada = new DataInputStream(conexion.getInputStream());

      
      
      long inicio = System.currentTimeMillis();      
      
        for(int i=0;i<1000;i++){
            // envia un numero punto flotante
            salida.writeDouble((i+1)*1.0);
      }   
        
         
        long fin = System.currentTimeMillis();
        String aaa = String.valueOf(fin-inicio);
        System.out.println("Tiempo de Envio con writeDouble:");
        System.out.println(aaa+"ms");  

      // envia 5 numeros punto flotante
      inicio = System.currentTimeMillis();
      ByteBuffer b = ByteBuffer.allocate(1000 * 8);
      for(int i=0;i<1000;i++){
        b.putDouble(i*1.0);
      }
      
      byte[] a = b.array();
      salida.write(a);
      fin = System.currentTimeMillis();
      aaa = String.valueOf(fin-inicio);
      System.out.println("Tiempo de Envio con ByteBuffer:");
        System.out.println(aaa+"ms");  
      

      salida.close();
      entrada.close();
      conexion.close();
    } catch (Exception e) {
      System.out.println("No Existe servidor");
      System.out.println(e);
    }
  }
}
