import java.util.List;
import java.util.ArrayList;
class Lista {

  public static void main(String[] args) {
    List<String> ejemploLista;
    ejemploLista = new ArrayList<String>();
    ejemploLista.add("Juan");
    ejemploLista.add("Pedro");
    ejemploLista.add("José");
    ejemploLista.add("María");
    ejemploLista.add("Sofía");    
    System.out.println(ejemploLista);
    System.out.println(ejemploLista.size());
    System.out.println(ejemploLista.get(0));
    ejemploLista.remove("Juan");
    ejemploLista.remove(0);
  }
}
