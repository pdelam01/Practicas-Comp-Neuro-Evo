package pdelam.neuro;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MainNeurona {
    static ArrayList<Double> primeros = new ArrayList<>();
    static ArrayList<Double> segundos = new ArrayList<>();
    static ArrayList<Double> terceros = new ArrayList<>();
    static ArrayList<Double> cuartos = new ArrayList<>();

    static ArrayList<Double> d = new ArrayList<>(); /* Salida esperada */
    static ArrayList<Double> wj = new ArrayList<>();

    static String textoTrain = "./datos/irisModificadoTrain.txt";
    static String textoTest = "./datos/irisModificadoTest.txt";

    private static int n;   /* Dimensión espacio entrada */
    private static int s;   /* Cardinal conjunto de muestras */
    private static int s1;  /* Cardinal conjunto validacion */
    private static int s2;  /* Cardinal conjunto entrenamiento */
    private static final double ea1 = 0.2;
    private static final double ea2 = 0.1;
    private static final int tmax1 = 1;
    private static double tmax2;
    private static int T = 0;
    private static int t = 0;

    private static final int alfa = 50;
    private static double a;
    private static final int rj = 2;

    private static final int opcionf = 0; /* 0 -> identidad, 1 -> sen(p), 2 -> sigmoide, 3 -> gauss */
    private static final int opciong = 0; /* 0 -> habitual, 1 -> impulsos potenciados y ponderados */


    public static void main(String[] args) {
        leerArchivoTrain();
        inicializacionVariables();
        pesosDeWJ();
        double aux = 0;

        double pk=0;
        double yk=0;

        double em1=0;
        double em2=1;
        double et2=0;

        while((em1 > ea1) && (T < tmax1)){
            while((em2 > ea2) && (t > tmax2)){
                for (int k = 0; k < s2; k++) {
                    pk=p(k);
                    yk=y(k,pk);
                    for (int j = 0; j < n; j++) {
                        aux += -gamma(t) * 0.5 * 2 * (yk - d.get(k)) * deryp(j,pk) * derypwj(k,j);
                        wj.set(j,aux);
                    }
                    t++;
                }

                et2=0;
                for (int k = 0; k < s2; k++) {
                    pk=p(k);
                    yk=y(k,pk);
                    et2 += 0.5 * Math.pow(yk-d.get(k),2);
                }
                em2=et2/s2;
            }

            //Calcular em1
            T++;
        }


        System.out.println("Total n: "+n);
        System.out.println("Total s1: "+s1);
        System.out.println("Total s2: "+s2);
        System.out.println("Total s: "+s);
        System.out.println(tmax2);
        System.out.println(a);
        System.out.println(primeros.toString());
    }

    private static void inicializacionVariables(){
        s2=primeros.size();
        s1=leerArchivoTest();
        s=s1+s2;
        n=columnasArchivoTrain();
        tmax2=3*s2;
    }

    private static double gamma(int t){
        a=(7*tmax2)/8;
        return -1/(1+Math.exp(-alfa*(t-a))) + 1;
    }

    private static double p(int k){
        double sumatorio = 0.0;
        for (int j = 0; j < n; j++) {
            sumatorio += wj.get(j) * getLista(k).get(j);
        }
        return sumatorio;
    }

    private static double error(double yk,int pos){
        return  0.5 * Math.pow((yk-d.get(pos)),2);
    }

    private static double et1(double ek, int pos){
        double et1 = 0.0;
        for (int k = 0; k < s1-1; k++) {
            et1=0;
        }
        return et1;
    }

    private static double funcionIdent(double p){
        return p;
    }

    private static double funcionSin(double p){
        return Math.sin(p);
    }

    private static double funcionSig(double p){
        return 2/(1+Math.exp(-p))-1;
    }

    private static double funcionGauss(double p){
        return 2*Math.exp(Math.pow(-p,2))-1;
    }

    private static double y(int pos, double pk){
        if(pos==0){
            return funcionIdent(pk);
        }else if(pos==1){
            return funcionSin(pk);
        }else if(pos==2){
            return funcionSig(pk);
        }else{
            return funcionGauss(pk);
        }
    }

    private static double funcionDerIdent(){
        return 1;
    }

    private static double funcionDerSin(double pk){
        return Math.cos(pk);
    }

    private static double funcionDerSig(double pk){
        return 2 * funcionSig(pk) * (1-funcionSig(pk));
    }

    private static double funcionDerGauss(double pk){
        return  2 * Math.exp(Math.pow(-pk,2)) * (-2 * pk);
    }

    private static double derypwj(int k, int j) {
        return Math.pow(getLista(j).get(k),rj);
    }

    private static double deryp(int pos, double pk){
        if(pos==0){
            return funcionDerIdent();
        }else if(pos==1){
            return funcionDerSin(pk);
        }else if(pos==2){
            return funcionDerSig(pk);
        }else{
            return funcionDerGauss(pk);
        }
    }

    private static void pesosDeWJ(){
        for (int i = 0; i < n; i++) {
            wj.add((Math.random() * (1 - (-1)) + (-1)));
        }
    }

    private static ArrayList<Double> getLista(int valor){
        if(valor==0){
            return primeros;
        }else if(valor==1){
            return segundos;
        }else if(valor==2){
            return terceros;
        }else{
            return cuartos;
        }
    }

    private static void leerArchivoTrain() {
        try {
            Scanner scan = new Scanner(new FileReader(textoTrain));

            while (scan.hasNext()) {
                primeros.add(Double.parseDouble(scan.next().replace(",",".")));
                segundos.add(Double.parseDouble(scan.next().replace(",",".")));
                terceros.add(Double.parseDouble(scan.next().replace(",",".")));
                cuartos.add(Double.parseDouble(scan.next().replace(",",".")));
                d.add(Double.parseDouble(scan.next().replace(",",".")));
            }

            scan.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static int leerArchivoTest() {
        try {
            int filasTest;
            BufferedReader br = new BufferedReader(new FileReader(textoTest));

            filasTest = (int) br.lines().count();

            br.close();

            return filasTest;
        }catch (IOException e){
            e.printStackTrace();
        }

        return -1;
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
