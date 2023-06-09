package vinnsla.plantmania;

import edu.princeton.cs.algs4.In;
import vinnsla.plantmania.enums.Eitrun;
import vinnsla.plantmania.enums.Ljosstyrkur;
import vinnsla.plantmania.enums.Uppruni;
import vinnsla.plantmania.enums.Vatnsthorf;

import java.util.ArrayList;
import java.util.List;

/**
 * Höfundur: Guðbjörg Viðja
 * Klasinn les inn plöntur úr skránni plontur.txt og gerir lista af Planta hlutum. Er með getter á listann. Þannig er
 * hægt að fá þessar plöntur í öðrum klösum: gera tilvik af LesaPlontur og svo kalla á getterinn (getPlontur()) á það tilvik.
 */
public class LesaPlontur {
    private List<Planta> plontulisti;//listi yfir plöntur sem eru lesnar úr skránni

    public LesaPlontur() {
        In inn = new In("src/main/java/vinnsla/plantmania/plontur.txt");

        int fjoldi = inn.readInt();
        inn.readLine();
        inn.readLine();

        String[] ollHeiti = lesaInnStreng(fjoldi, inn);
        List<String>[] heitiFylki = new List[fjoldi];
        for (int i = 0; i < fjoldi; i++) {
            heitiFylki[i] = List.of(ollHeiti[i].split(", "));
        }

        String[] myndaslodir = lesaInnStreng(fjoldi, inn);

        String[] textar = lesaInnStreng(fjoldi, inn);

        String[] uppruniValue = lesaInnStreng(fjoldi, inn);
        Uppruni[] upprunis = new Uppruni[fjoldi];
        for (int i = 0; i < fjoldi; i++) {
            upprunis[i] = Uppruni.valueOf(uppruniValue[i]);
        }

        String[] ljosValue = lesaInnStreng(fjoldi, inn);
        Ljosstyrkur[] ljoses = new Ljosstyrkur[fjoldi];
        for (int i = 0; i < fjoldi; i++) {
            ljoses[i] = Ljosstyrkur.valueOf(ljosValue[i]);
        }

        String[] eitrunValue = lesaInnStreng(fjoldi, inn);
        Eitrun[] eitruns = new Eitrun[fjoldi];
        for (int i = 0; i < fjoldi; i++) {
            eitruns[i] = Eitrun.valueOf(eitrunValue[i]);
        }

        List<Integer>[] hitastig = new List[fjoldi];

        for (int i = 0; i < fjoldi; i++) {
            List<Integer> o = new ArrayList<>();
            o.add(inn.readInt());
            o.add(inn.readInt());
            o.add(inn.readInt());

            hitastig[i] = o;
        }
        inn.readLine();
        inn.readLine();

        String[] vatnValue = lesaInnStreng(fjoldi, inn);
        Vatnsthorf[] vatns = new Vatnsthorf[fjoldi];
        for (int i = 0; i < fjoldi; i++) {
            vatns[i] = Vatnsthorf.valueOf(vatnValue[i]);
        }

        int[] millibil = lesaInnInt(fjoldi, inn);

        int[] ljosStundir = lesaInnInt(fjoldi, inn);

        String[] einkenni = lesaInnStreng(fjoldi, inn);
        List<String>[] einkennafylki = new List[fjoldi];
        for (int i = 0; i < fjoldi; i++) {
            einkennafylki[i] = List.of(einkenni[i].split(", "));
        }

        inn.close();
        plontulisti = new ArrayList<>(fjoldi);
        for (int i = 0; i < fjoldi; i++) {
            Planta planta = new Planta(heitiFylki[i], myndaslodir[i], textar[i], upprunis[i], ljoses[i], eitruns[i], hitastig[i], vatns[i], millibil[i], ljosStundir[i], einkennafylki[i]);
            plontulisti.add(planta);
        }
    }

    private String[] lesaInnStreng(int f, In in) {
        String[] fylki = new String[f];
        for (int i = 0; i < f; i++) {
            fylki[i] = in.readLine();
        }
        in.readLine();
        return fylki;
    }

    private int[] lesaInnInt(int f, In in) {
        int[] fylki = new int[f];
        for (int i = 0; i < f; i++) {
            fylki[i] = in.readInt();
        }
        in.readLine();
        in.readLine();
        return fylki;
    }

    public List<Planta> getPlontur() {
        return plontulisti;
    }

    public static void main(String[] args) {
        LesaPlontur a = new LesaPlontur();
    }

}
