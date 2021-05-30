package pdelam.neuro;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MainNeurona {
    /* Valores columnas datos texto entreno */
    static ArrayList<Double> primeros = new ArrayList<>();
    static ArrayList<Double> segundos = new ArrayList<>();
    static ArrayList<Double> terceros = new ArrayList<>();
    static ArrayList<Double> cuartos = new ArrayList<>();

    /* Valores columnas datos texto validación */
    static ArrayList<Double> primerosTest = new ArrayList<>();
    static ArrayList<Double> segundosTest = new ArrayList<>();
    static ArrayList<Double> tercerosTest = new ArrayList<>();
    static ArrayList<Double> cuartosTest = new ArrayList<>();

    static ArrayList<Double> d1 = new ArrayList<>(); /* Salida esperada validacion */
    static ArrayList<Double> d2 = new ArrayList<>(); /* Salida esperada entreno */

    static ArrayList<Double> umbralTrain = new ArrayList<>();
    static ArrayList<Double> umbralTest = new ArrayList<>();

    static ArrayList<Double> wj = new ArrayList<>(); /* Lista de pesos */
    static int [] rj = {1,3,1,1,1}; /* Exponentes del cálculo potencial */

    static String textoTrain = "./datos/irisModificadoTrain.txt";
    static String textoTest = "./datos/irisModificadoTest.txt";

    private static int n;   /* Dimensión espacio entrada */
    private static int s;   /* Cardinal conjunto de muestras */
    private static int s1;  /* Cardinal conjunto validacion */
    private static int s2;  /* Cardinal conjunto entrenamiento */

    private static final double ea1 = 0.2;  /* Error medio aceptable validación */
    private static final double ea2 = 0.1;  /* Error medio aceptable entreno */

    private static final int tmax1 = 1; /* Tiempo máximo validación */
    private static double tmax2;    /* Tiempo máximo entreno */
    private static int contadorMalClasif=0;

    private static final int alfa = 4;  /* Valor influye en la pendiente (caida) de la función gamma */

    private static final int opcionf = 1; /* 0 -> identidad, 1 -> sen(p), 2 -> sigmoide, 3 -> gauss  4 -> nueva*/

    private static ArrayList<Double> clasif = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("===== Inicio del algoritmo ====="+"\n");
        Funciones func = new Funciones();
        leerArchivoTrainYTest();
        inicializacionVariables();
        pesosDeWJ();

        double aux = 0;

        int T = 0, t = 0;

        double pk = 0, yk = 0;

        double et1 = 0, et2 = 0;
        double em1 = 0 ,em2 = 0;

        et1=errorEt1();
        em1=et1/s1;

        et2=errorEt2();
        em2=et2/s2;

        System.out.println("Inicialización de pesos aleatorios: "+wj.toString());
        System.out.println("Error medio de validación inicial: "+em1);
        System.out.println("Error medio de entrenamiento inicial: "+em2);

        System.out.println("\n"+"===== Inicio del alg de entreno ====="+"\n");
        while((em1 > ea1) && (T < tmax1)){
            while((em2 > ea2) && (t < tmax2)){
                for (int k = 0; k < s2; k++) {
                    pk=p(k,true);
                    yk=y(pk);
                    for (int j = 0; j <= n; j++) {
                        aux = wj.get(j);
                        aux += -gamma(t) * 0.5 * 2 * (yk - d2.get(k)) * deryp(pk) * derypwj(k,j);
                        wj.set(j,aux);
                    }
                    t++;

                }
                System.out.println("Pesos durante el entreno: "+wj.toString());
                et2=errorEt2();
                em2=et2/s2;
            }

            et1=errorEt1();
            em1=et1/s1;
            T++;
        }

        double p=0,y=0;
        for (int i = 0; i < s1; i++) {
            p=p(i,false);
            y=y(p);
            clasif.add(y);
        }
        /* Ejercicio 7 */
        System.out.println("El total de errores es de: "+compararMuestras(clasif,d1));

        System.out.println("\n"+"===== Tras la ejecución ====="+"\n");
        System.out.println("Error medio de validación final: "+em1);
        System.out.println("Error medio de entrenamiento final: "+em2);
        System.out.println("Pesos finales: "+wj.toString());
    }

    /* Ejercicio 7 */
    private static int compararMuestras(ArrayList<Double> clasif, ArrayList<Double> d1){
        for (int i = 0; i < s1; i++) {
            if(Math.round(clasif.get(i)) != d1.get(i)){
                contadorMalClasif++;
            }
        }
        return contadorMalClasif;
    }

    private static void inicializacionVariables(){
        s2=primeros.size();
        s1=primerosTest.size();
        s=s1+s2;
        n=columnasArchivoTrain();
        tmax2=3*s2;

        for (int i = 0; i < s1; i++) {
            umbralTest.add(1.0);
        }

        for (int i = 0; i < s2; i++) {
            umbralTrain.add(1.0);
        }
    }

    /* Ejercicio 6 (alfa está definida como global más arriba) */
    private static double gamma(int t){
        double a = (7 * tmax2) / 8;
        return -1/(1+Math.exp(-alfa*(t - a))) + 1;
    }

    private static void pesosDeWJ(){
        for (int i = 0; i <= n; i++) {
            wj.add((Math.random() * (1 - (-1)) + (-1)));
        }
    }

    private static double p(int k, boolean test_train){
        double sumatorio = 0.0;
        if (test_train){
            for (int j = 0; j <= n; j++) {
                sumatorio += wj.get(j) * Math.pow(getListaEntreno(j).get(k),rj[j]);
            }
        }else{
            for (int j = 0; j <= n; j++) {
                sumatorio += wj.get(j) * Math.pow(getListaTest(j).get(k),rj[j]);
            }
        }

        return sumatorio;
    }

    private static double y(double pk){
        if(opcionf==0){
            return Funciones.funcionIdent(pk);
        }else if(opcionf==1){
            return Funciones.funcionSin(pk);
        }else if(opcionf==2){
            return Funciones.funcionSig(pk);
        }else if(opcionf==3){
            return Funciones.funcionGauss(pk);
        }else{
            /* Ejercicio 5 */
            return Funciones.funcionNueva(pk);
        }
    }

    private static double errorEt1(){
        double et=0, pk=0, yk=0;
        for (int k = 0; k < s1; k++) {
            pk=p(k,false);
            yk=y(pk);
            et += 0.5 * Math.pow(yk-d1.get(k),2);
        }
        return et;
    }

    private static double errorEt2(){
        double et=0, pk=0, yk=0;
        for (int k = 0; k < s2; k++) {
            pk=p(k,true);
            yk=y(pk);
            et += 0.5 * Math.pow(yk-d2.get(k),2);
        }
        return et;
    }

    private static double deryp(double pk){
        if(opcionf==0){
            return Funciones.funcionDerIdent();
        }else if(opcionf==1){
            return Funciones.funcionDerSin(pk);
        }else if(opcionf==2){
            return Funciones.funcionDerSig(pk);
        }else if(opcionf==3){
            return Funciones.funcionDerGauss(pk);
        }else{
            /* Ejercicio 5 */
            return Funciones.funcionDerNueva(pk);
        }
    }

    private static double derypwj(int k, int j) {
        return Math.pow(getListaEntreno(j).get(k),rj[j]);
    }

    private static ArrayList<Double> getListaEntreno(int valor){
        switch (valor){
            case 0:
                return primeros;
            case 1:
                return segundos;
            case 2:
                return terceros;
            case 3:
                return cuartos;
            default:
                return umbralTrain;
        }
    }

    private static ArrayList<Double> getListaTest(int valor){
        switch (valor){
            case 0:
                return primerosTest;
            case 1:
                return segundosTest;
            case 2:
                return tercerosTest;
            case 3:
                return cuartosTest;
            default:
                return umbralTest;
        }
    }

    private static void leerArchivoTrainYTest() {
        try {
            Scanner scan = new Scanner(new FileReader(textoTrain));
            Scanner scan2 = new Scanner(new FileReader(textoTest));

            while (scan.hasNext()) {
                primeros.add(Double.parseDouble(scan.next().replace(",",".")));
                segundos.add(Double.parseDouble(scan.next().replace(",",".")));
                terceros.add(Double.parseDouble(scan.next().replace(",",".")));
                cuartos.add(Double.parseDouble(scan.next().replace(",",".")));
                d2.add(Double.parseDouble(scan.next().replace(",",".")));
            }

            while (scan2.hasNext()) {
                primerosTest.add(Double.parseDouble(scan2.next().replace(",",".")));
                segundosTest.add(Double.parseDouble(scan2.next().replace(",",".")));
                tercerosTest.add(Double.parseDouble(scan2.next().replace(",",".")));
                cuartosTest.add(Double.parseDouble(scan2.next().replace(",",".")));
                d1.add(Double.parseDouble(scan2.next().replace(",",".")));
            }

            scan.close();
            scan2.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static int columnasArchivoTrain(){
        try {
            int col=0;
            Scanner scan = new Scanner(new FileReader(textoTrain));

            if (scan.hasNextLine()) {
                col = scan.nextLine().split("\\s+").length-1;
            }

            scan.close();

            return col;
        }catch (IOException e){
            e.printStackTrace();
        }

        return -1;
    }

}
