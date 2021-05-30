package pdelam.neuro;

public class Funciones {

    public Funciones() {

    }

    public static double funcionIdent(double pk){
        return pk;
    }

    /* Ejercicio 1 */
    public static double funcionSin(double pk){
        return Math.sin(pk);
    }

    /* Ejercicio 2 */
    public static double funcionSig(double pk){
        return 2/(1+Math.exp(-pk))-1;
    }

    /* Ejercicio 3 */
    public static double funcionGauss(double pk){
        return 2*Math.exp(-Math.pow(pk,2))-1;
    }

    /* Ejercicio 4 */
    public static double funcionDerIdent(){
        return 1;
    }

    /* Ejercicio 5 */
    public static double funcionNueva(double pk){
        return (2*pk)/(1+Math.pow(pk,2));
    }

    public static double funcionDerSin(double pk){
        return Math.cos(pk);
    }

    public static double funcionDerSig(double pk){
        return 2 * funcionSig(pk) * (1-funcionSig(pk));
    }

    public static double funcionDerGauss(double pk){
        return  2 * Math.exp(-Math.pow(pk,2)) * (-2 * pk);
    }

    /* Ejercicio 5 */
    public static double funcionDerNueva(double pk){
        return - 2*(Math.pow(pk,2)-1)/(Math.pow(Math.pow(pk,2)+1,2));
    }

}
