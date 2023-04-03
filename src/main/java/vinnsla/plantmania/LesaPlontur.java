package vinnsla.plantmania;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LesaPlontur {
    private List<Planta> plontulisti = new ArrayList<>();

    /*
    //fjöldi planta

//String latnesktNafn

//String almenntNafn

//List<String> ollHeiti

//String myndaslod

//String texti

//Uppruni uppruni

//Ljosstyrkur ljosstyrkur

//Eitrun eitrun

//int kjorhitastig

//Vatnsthorf vatnsthorf

//int almennurTimiMilliVokvanna

//int ljosStundir

//List<String> einkenniPlontu

     */
    public LesaPlontur() {
        /*
        File file = new File("plontur");
        Scanner s = null;
        try {
            s = new Scanner(file, StandardCharsets.UTF_8);
            s = new Scanner(file, "UTF-8");
            //s=new Scanner("plontur", StandardCharsets.UTF_8);
            //s.useLocale(Locale.US);
        } catch (IOException e) {
            System.out.println("villa");
        }

         */
        try {
            Scanner s = new Scanner(new File("plontur"));


            //int fjoldi = 0;
            //if (s.hasNextInt()) fjoldi = s.nextInt();//fyrsta talan í skjalinu segir hve margar plöntur eru í því

            //fjoldi = s.nextInt();

            int fjoldi = Integer.parseInt(s.next());

            System.out.println("fjoldi: " + fjoldi);

            String[] latneskNofn = lesaInnStreng(fjoldi, s);
            System.out.println(Arrays.deepToString(latneskNofn));

            String[] almennNofn = lesaInnStreng(fjoldi, s);

            String[] ollHeiti = lesaInnStreng(fjoldi, s);
            List<String>[] heitiFylki = new List[fjoldi];
            for (int i = 0; i < fjoldi; i++) {
                heitiFylki[i] = List.of(ollHeiti[i].split(","));
            }


            String[] myndaslodir = lesaInnStreng(fjoldi, s);

            String[] textar = lesaInnStreng(fjoldi, s);

            String[] uppruniValue = lesaInnStreng(fjoldi, s);
            Uppruni[] upprunis = new Uppruni[fjoldi];
            for (int i = 0; i < fjoldi; i++) {
                upprunis[i] = Uppruni.valueOf(uppruniValue[i]);
            }

            String[] ljosValue = lesaInnStreng(fjoldi, s);
            Ljosstyrkur[] ljoses = new Ljosstyrkur[fjoldi];
            for (int i = 0; i < fjoldi; i++) {
                ljoses[i] = Ljosstyrkur.valueOf(ljosValue[i]);
            }

            String[] eitrunValue = lesaInnStreng(fjoldi, s);
            Eitrun[] eitruns = new Eitrun[fjoldi];
            for (int i = 0; i < fjoldi; i++) {
                eitruns[i] = Eitrun.valueOf(eitrunValue[i]);
            }

            int[] hitastig = lesaInnInt(fjoldi, s);

            String[] vatnValue = lesaInnStreng(fjoldi, s);
            Vatnsthorf[] vatns = new Vatnsthorf[fjoldi];
            for (int i = 0; i < fjoldi; i++) {
                vatns[i] = Vatnsthorf.valueOf(vatnValue[i]);
            }

            int[] millibil = lesaInnInt(fjoldi, s);

            int[] ljosStundir = lesaInnInt(fjoldi, s);

            String[] einkenni = lesaInnStreng(fjoldi, s);
            List<String>[] einkennafylki = new List[fjoldi];
            for (int i = 0; i < fjoldi; i++) {
                einkennafylki[i] = List.of(einkenni[i].split(","));
            }


            //gera plönturnar
            plontulisti = new ArrayList<>(fjoldi);
            for (int i = 0; i < fjoldi; i++) {
                Planta planta = new Planta(latneskNofn[i], almennNofn[i], heitiFylki[i], myndaslodir[i], textar[i], upprunis[i], ljoses[i], eitruns[i], hitastig[i], vatns[i], millibil[i], ljosStundir[i], einkennafylki[i]);
                plontulisti.add(planta);

            }

        } catch (FileNotFoundException e) {
            System.out.println("Villa kom upp");
        }
        //Eitrun.valueOf("MIKIL");
        prenta();
    }

    private String[] lesaInnStreng(int f, Scanner s) {
        //s.nextLine();//les fram yfir enda línunnar?
        //s.nextLine();//les yfir enda tómu línunnar á eftir
        String[] fylki = new String[f];
        for (int i = 0; i < f; i++) {
            fylki[i] = s.nextLine();
            System.out.println(fylki[i]);
        }

        return fylki;
    }

    private int[] lesaInnInt(int f, Scanner s) {
        //s.nextLine();//les fram yfir enda línunnar?
        //s.nextLine();//les yfir enda tómu línunnar á eftir
        int[] fylki = new int[f];
        for (int i = 0; i < f; i++) {
            fylki[i] = s.nextInt();
        }
        return fylki;
    }

    private void prenta() {
        System.out.println(plontulisti.toString());
    }

    public static void main(String[] args) {
        //List<Planta> mainListi=
        LesaPlontur a = new LesaPlontur();
    }

}
