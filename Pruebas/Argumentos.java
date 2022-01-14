class Arg{
    public static void main(String[] args) {
        int tam = args.length;
        System.out.println("\nHola Usted mando por parametros: ");
        System.out.println("\n");
        for(int i=0;i<tam;i++){
            System.out.println(args[i]);
        }        
        System.out.println("\nEl numero de parametros es: "+tam);
    }
}
//java Arg 1 2 3 4 5 6 7 8 9 10