class Pruba{
    static int [][]Mnodos;
    public static void main(String[] args) {
        int N =4;
        int FI = N/2;//Fila Intermedia
        Mnodos=new int[4][6];
        llenado(FI,N,Mnodos);
        Mostrar(4,6,Mnodos);
    }
    static void Asignacion(int FI,int C,int [][]M){
        for(int i=0;i<4;i++){                           
                M[i][0] = i;
                M[i][1] = (i<2 ? 0 : FI);
                M[i][2] = (i<2 ? FI : C);
                M[i][3] = (i%2==0 ? 0 : FI);
                M[i][4] = (i%2==0 ? FI : C);
                M[i][5] = C ;           
        }
    }
    static void Mostrar(int F,int C, int [][]Matriz){
        for (int j = 0; j < F; j++) {
          for (int i = 0; i < C; i++) {
            System.out.print(Matriz[j][i] + " ");
          }
          System.out.print("\n");
        }
      }
}