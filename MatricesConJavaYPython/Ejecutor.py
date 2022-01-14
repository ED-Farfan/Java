import os
N=[50,100, 200, 300, 500, 1000]
for i in N:
    print("----------------------------------------------------")
    print("             Tamaño Matriz : "+str(i)+"*"+str(i))
    os.system("java MultiplicaMatriz "+str(i))
    os.system("java MultiplicaMatriz_2 "+str(i))
    print("----------------------------------------------------")
