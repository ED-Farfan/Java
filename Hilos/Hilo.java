class A extends Thread {
  static long n;
  public  A(String msg) {
      super(msg);
  }
  public void run() {
    for (int i = 0; i < 100000; i++){
        n++;        
        System.out.println(this.getName());
    } 
  }

  public static void main(String[] args) throws Exception {
    A t1 = new A("Hilo 1");
    A t2 = new A("Hilo 2");
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    System.out.println(n);
  }
}
