class MultiplicaMatriz {
  static int N = 0;
  static int[][] A; 
  static int[][] B ;
  static int[][] C ;

  public static void main(String[] args) {
    if (args.length == 1) {
      N=Integer.parseInt(args[0]);      
      A= new int[N][N];
      B= new int[N][N];
      C= new int[N][N];
      long t1 = System.currentTimeMillis();

      // inicializa las matrices A y B

      for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) {
        A[i][j] = 2 * i - j;
        B[i][j] = i + 2 * j;
        C[i][j] = 0;
      }

      // multiplica la matriz A y la matriz B, el resultado queda en la matriz C

      for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) for (
        int k = 0;
        k < N;
        k++
      ) C[i][j] += A[i][k] * B[k][j];

      long t2 = System.currentTimeMillis();
      System.out.println("\nMatriz_1:\n\t->Tiempo: " + (t2 - t1) + "ms");
    }else{
      System.out.println("Agrege el numero de elementos de la matriz");
    }
  }
}
