package vinnsla.plantmania;

import edu.princeton.cs.algs4.In;

//TODO: er þetta notað/á að nota þetta, guðbjörg?
public class FraedslaLestur {


    public static void main(String[] args) {
        //In inn = new In("nyfraedsla.txt");
        In inn = new In("src/main/java/vinnsla/plantmania/nyfraedsla.txt");
        String alltSkjalid = inn.readAll();
        System.out.println(alltSkjalid);


        String[] paneskipting = new String[0];
        if (alltSkjalid.startsWith(",,,,")) {
            alltSkjalid = alltSkjalid.replaceFirst(",,,,", "");
            paneskipting = alltSkjalid.split(",,,,");
        }

        for (int i = 0; i < paneskipting.length; i++) {
            System.out.println("\nHluti " + i + ": \n" + paneskipting[i]);
        }
        //System.out.println(Arrays.toString(paneskipting));

        String[][] fyrirsagnaskipt = new String[0][];
    }
}
