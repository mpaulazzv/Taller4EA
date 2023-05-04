/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package taller4;

/**
 *
 * @author MPaula
 */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.BinarySearchST;
import java.util.ArrayList;
import edu.princeton.cs.algs4.MaxPQ;
import java.util.Date;
import java.util.List;

public class AnalisisDatos {

    public static void main(String[] args) {

        String fileroute = "D:\\Universidad\\upb\\Cuarto semestre\\Estructuras de datos y algoritmos\\548634059_T_ONTIME_REPORTING.csv";

        ArrayList<DelayRecord> arrDelays = DelayRecord.readCSVFile(fileroute);

        ST<String, Integer> st = meanRouteDelay(arrDelays);
        
        int m = 10;
        ST<String, Integer> topM = topMDelayed(st, m);
        System.out.println("The " + m + " most delayed routes were: ");
        listTopMDelayed(topM);
        
        
        Date from = new Date(2019, 0, 12);
        Date to = new Date(2019, 0, 25);
        System.out.println("");
        System.out.println(longestDelayCarrier(arrDelays, from, to));

    }

    //Método para obtener el retraso promedio 
    public static ST<String, Integer> meanRouteDelay(ArrayList<DelayRecord> delays) {

        ST<String, Integer> st = new ST<>();
        ST<String, Integer> count = new ST<>();

        int delay = 0, cnt = 0;

        for (DelayRecord dr : delays) {
            if (st.isEmpty()) {
                st.put(dr.route(), dr.calcDelay());
                count.put(dr.route(), 1);

            } else {

                if (st.contains(dr.route()) && count.contains(dr.route())) {
                    delay = st.get(dr.route());
                    cnt = count.get(dr.route());
                    st.put(dr.route(), dr.calcDelay() + delay);
                    count.put(dr.route(), cnt + 1);
                } else {
                    st.put(dr.route(), dr.calcDelay());
                    count.put(dr.route(), 1);
                }

            }

        }

        for (DelayRecord key : delays) {
            st.put(key.route(), (st.get(key.route()) / count.get(key.route())));
        }

        return st;
    }

    //Método para obtener las Top-M rutas con mayor retraso
    public static ST<String, Integer> topMDelayed(ST<String, Integer> meanDelayRoute, int m) {

        MinPQ<Integer> avg = new MinPQ<>();
        ST<String, Integer> topM = new ST<>();
        BinarySearchST<String, Integer> bst = new BinarySearchST<>();

        int n = 0;

        for (String key : meanDelayRoute.keys()) {
            bst.put(key, meanDelayRoute.get(key));
        }

        String min = "";

        for (int j = 0; j < m +1 ; j++) {
            min = meanDelayRoute.min();
            avg.insert(meanDelayRoute.get(min));
            meanDelayRoute.delete(min);           
        }

        for (String key : meanDelayRoute.keys()) {
            if (meanDelayRoute.get(key) > avg.min()) {
                avg.delMin();
                avg.insert(meanDelayRoute.get(key));
            }
        }

        int min1 = 0;
        int t = avg.size();

        for (int i = 0; i < t; i++) {
            min1 = avg.min();
            topM.put(bst.select(min1), min1);
            avg.delMin();
        }

        return topM;

    }

    //Método para listar en order descendiente el retraso promedio de las topM
    public static void listTopMDelayed(ST<String, Integer> topM) {
        int i = 1;
        String max = "";
        int t = topM.size();

        for (int j = 0; j < t; j++) {
            max = topM.max();
            System.out.println(i + " Route: " + max + " , mean delay: " + topM.get(max));
            topM.delete(max);
            i++;
        }

    }

    //Método para obtener la aerolínea con más retraso en un determinado rango de fechas
    public static String longestDelayCarrier(ArrayList<DelayRecord> delays, Date from, Date to) {
        
        BinarySearchST<String, Integer> st = new BinarySearchST<>();
        BinarySearchST<String, Integer> count = new BinarySearchST<>();
        ArrayList<DelayRecord> arrdr = new ArrayList<>();
      
        int delay=0, cnt =0;
        
        for(DelayRecord dr :delays){
            
            if((dr.getDate().compareTo(from)==0 || dr.getDate().compareTo(from)>0)
                    && (dr.getDate().compareTo(to)==0) || dr.getDate().compareTo(to)<0)
                
                arrdr.add(dr);
        }
        
        for(DelayRecord dr: arrdr){
            
            if(st.isEmpty() && count.isEmpty()){
                st.put(dr.getAirline(), dr.calcDelay());
                count.put(dr.getAirline(), 1);
            }else{
                if(st.contains(dr.getAirline()) && count.contains(dr.getAirline())){
                    delay = st.get(dr.getAirline());
                    cnt = count.get(dr.getAirline());
                    st.put(dr.getAirline(), dr.calcDelay()+delay);
                    count.put(dr.getAirline(), cnt + 1);
                }else{
                    st.put(dr.getAirline(), dr.calcDelay());
                    count.put(dr.getAirline(), 1);
                }
            }
        
        }
        
        for(String key: st.keys()){
            st.put(key, (st.get(key)/count.get(key)));
        }
     
        MaxPQ<String> maxpq = new MaxPQ();
        for(String key: st.keys()){
            maxpq.insert(st.get(key) + "," + key);       
        }
        
        String[] max = maxpq.max().split(",");
     
        return max[1] + " was the airline with the highest mean delay, from " +
                from + " to " + to + " , and its mean was: " + max[0];

    }

    //Método auxiliar para obtener una tabla de símbolos con la aerolínea en lugar de la ruta
    

}
