import sys
import numpy as np

def AsignarDatos(N):
    MatA = np.zeros((N,N))
    MatB = np.zeros((N,N))    
    for i in range(0,N):
        for j in range(0,N):
            MatA[i][j] = 2 * i + 3*j
            MatB[i][j] = 2 * i - 3*j
    return MatA , MatB


if(len(sys.argv)==2 and ((int(sys.argv[1])**2)%4 == 0)):
    MatA,MatB = AsignarDatos(int(sys.argv[1]))
    print("\nMatriz A\n",MatA)
    print("\nMatriz B\n",MatB)
    print("\nMatriz BT\n",np.transpose(MatB))
    MatC = np.dot(MatA,MatB)
    print("\nMatriz C\n",MatC)
    print("\nCheckC: ",np.sum(MatC))
else:
    print ("Número de parámetros: ", len(sys.argv))
    print ("Lista de argumentos: ", sys.argv)

