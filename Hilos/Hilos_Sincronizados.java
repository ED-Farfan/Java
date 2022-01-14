class A_S extends Thread {
    static long n;
    static Object obj = new Object();
    public  A_S(String msg) {
        super(msg);
    }
    public void run() {
      for (int i = 0; i < 100000; i++)
        synchronized(obj)
        {
          n++;
          System.out.println(this.getName()+' '+n);
        }
         
          
      
    }
  
    public static void main(String[] args) throws Exception {
      A_S t1 = new A_S("Hilo 1");
      A_S t2 = new A_S("Hilo 2");
      t1.start();
      t2.start();
      t1.join();
      t2.join();
      System.out.println(n);
    }
  }
  