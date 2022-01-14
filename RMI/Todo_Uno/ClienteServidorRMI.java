import java.rmi.Naming;
public class ClienteServidorRMI{
    static public String url = "rmi://localhost/prueba";
    static class Worker extends Thread {    
        public void run() {
            try {
                // en este caso el objeto remoto se llama "prueba", notar que se utiliza el puerto default 1099
                
                // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
                InterfaceRMI r = (InterfaceRMI)Naming.lookup(url);
                System.out.println(r.mayusculas("hola"));
                System.out.println("suma=" + r.suma(10,20));
                int[][] m = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};
                System.out.println("checksum=" + r.checksum(m));
            } catch (Exception e) {
                System.out.println(e);
            }            
        }
    }
    public static void main(String[] args) throws Exception {
        Worker w = new Worker();        
        ClaseRMI obj = new ClaseRMI();    
        // registra la instancia en el rmiregistry
        Naming.rebind(url,obj);
        w.start();
        w.join();
        System.exit(0);
    }
}