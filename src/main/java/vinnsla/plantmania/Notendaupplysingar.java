package vinnsla.plantmania;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//athuga hvernig það er greint á milli tveggja eins planta!!! það er nauðsynlegt!
//@JsonDeserialize(using = NotendaupplysingarDeserializer.class)
public class Notendaupplysingar {
    private ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();
    private ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir = FXCollections.observableArrayList();//þarf ekki endilega að vera í skrá? hægt að reikna út þegar forritið er opnað
    private ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir = FXCollections.observableArrayList();//ditto

    public Notendaupplysingar(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
        //finnaNaestuVokvanir();//held þetta sé óþarfi
    }

    public Notendaupplysingar() {
        //kallað á þetta fimm sinnum við upphaf keyrslu, af hverju?
        System.out.println("Notendaupplysingar smidur");
        //finnaNaestuVokvanir();
    }


    //athuga hvar það er brugðist við þegar plantan er einfaldlega tekin af listanum!
    //er þetta sett á plöntu um leið og henni er bætt við í mínarPlöntur? gera kannski sér aðferð sem hægt er að kalla á oftar?
    //það þarf að refactora þetta, en þetta virkar!
    public void finnaFyrriVokvanir() {
        minarPlontur.addListener((ListChangeListener<MinPlanta>) (obs) -> {
            while (obs.next()) {
                if (obs.wasAdded()) {
                    for (int i = 0; i < obs.getAddedSize(); i++) {
                        int finalI = i;
                        obs.getAddedSubList().get(i).getVokvanir().addListener((ListChangeListener<LocalDate>) (observable) -> {
                            while (observable.next()) {
                                if (observable.wasAdded()) {
                                    for (int j = 0; j < observable.getAddedSize(); j++) {
                                        fyrriVokvanir.add(new Pair<>(obs.getAddedSubList().get(finalI), observable.getAddedSubList().get(j)));
                                    }
                                } else if (observable.wasRemoved()) {
                                    List<Pair<MinPlanta, LocalDate>> eytt = new ArrayList<>();
                                    for (int j = 0; j < observable.getRemovedSize(); j++) {
                                        eytt.add(new Pair<>(obs.getAddedSubList().get(finalI), observable.getRemoved().get(j)));
                                    }
                                    fyrriVokvanir.removeAll(eytt);
                                }
                            }
                        });
                    }
                }
            }
            fyrriVokvanir.sort(Comparator.comparing((Pair::getValue)));
        });
    }


    /**
     * Finnur vökvanir þrjá mánuði fram í tímann.
     * Passa að hafa einhverja tilkynningu um að engar upplýsingar séu skráðar um fyrri vökvun
     */
    public void finnaNaestuVokvanir() {
        System.out.println("Notendaupplysingar.finnaNaestuVokvanir(): ");
        //hafa binding/listener hér
        // naestuVokvanir.sort(Comparator.comparing((Pair::getValue)));

        //Þegar nýrri plöntu er bætt við:
        //setja vökvun í dag
        minarPlontur.addListener((ListChangeListener<? super MinPlanta>) change -> {
            //System.out.println("Listenerinn");
            change.next();

            if (change.wasAdded()) {
                System.out.println("Notendaupplysingar.finnaNaestuVokvanir: Plontu var baett vid minarPlontur i notendaupplysingar");
                for (MinPlanta mp : change.getAddedSubList()) {
                    for (LocalDate date : mp.getPlanadarVokvanir()) {
                        naestuVokvanir.add(new Pair<>(mp, date));
                        //System.out.println("naestuVokvanir: " + naestuVokvanir);
                    }
                }
                System.out.println("naestuVokvanir: " + naestuVokvanir);
            }
        });
    }

    public ObservableList<MinPlanta> getMinarPlontur() {
        return minarPlontur;
    }

    public void setMinarPlontur(ObservableList<MinPlanta> minarPlontur) {
        this.minarPlontur = minarPlontur;
    }

    public ObservableList<Pair<MinPlanta, LocalDate>> getFyrriVokvanir() {
        return fyrriVokvanir;
    }

    public void setFyrriVokvanir(ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir) {
        this.fyrriVokvanir = fyrriVokvanir;
    }

    public ObservableList<Pair<MinPlanta, LocalDate>> getNaestuVokvanir() {
        return naestuVokvanir;
    }

    public void setNaestuVokvanir(ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir) {
        this.naestuVokvanir = naestuVokvanir;
    }

    //passa að engar tvær plöntur fái sama nickname
    public void baetaVidPlontu(Planta planta) {
        System.out.println("Notendaupplysingar.baetaVidPlontu(Planta)");
        minarPlontur.add(new MinPlanta(planta));
        //MinPlanta ny = new MinPlanta();
        //ny.setAlmenntNafn(planta.getAlmenntNafn());
        //minarPlontur.add(ny);
    }

    public String toString() {
        return "Notendaupplysingar{" +
                "minarPlontur=" + minarPlontur +
                ", fyrriVokvanir=" + fyrriVokvanir +
                ", naestuVokvanir=" + naestuVokvanir +
                '}';
    }

    public static void main(String[] args) {
        Notendaupplysingar notendaupplysingar = new Notendaupplysingar();
        List<Planta> plontur = new LesaPlontur().getPlontur();
        MinPlanta minPlanta = new MinPlanta(plontur.get(0));
        System.out.println(minPlanta);
        //minPlanta.reiknaPlanadarVokvanir();
        System.out.println(minPlanta.getPlanadarVokvanir());



        /*Notendaupplysingar uppl = new Notendaupplysingar();
        uppl.finnaNaestuVokvanir();
        List<Planta> plontur = new LesaPlontur().getPlontur();

        for (Planta p : plontur) {
            //System.out.println(p);
            uppl.minarPlontur.add(new MinPlanta(p));
        }


        System.out.println(uppl.minarPlontur.get(0));
        System.out.println("Naestu vokvanir: " + uppl.naestuVokvanir);

        System.out.println("fyrri vokvanir: " + uppl.fyrriVokvanir);*/
/*
        uppl.minarPlontur.get(0).baetaVidVokvun(LocalDate.of(2022, 3, 18));
        uppl.minarPlontur.get(0).baetaVidVokvun(LocalDate.of(2023, 3, 14));
        uppl.minarPlontur.get(0).baetaVidVokvun(LocalDate.of(2023, 3, 22));
        uppl.minarPlontur.get(0).baetaVidVokvun(LocalDate.of(2023, 3, 10));
        uppl.minarPlontur.get(0).baetaVidVokvun(LocalDate.of(2023, 3, 26));
        uppl.minarPlontur.get(0).baetaVidVokvun(LocalDate.of(2023, 3, 30));
        uppl.minarPlontur.get(0).baetaVidVokvun(LocalDate.of(2023, 4, 3));

        System.out.println(uppl.minarPlontur.get(0));

        //uppl.minarPlontur.get(0).getVokvanir().remove(LocalDate.of(2023, 3, 10));
        //uppl.minarPlontur.get(0).getVokvanir().remove(LocalDate.of(2023, 4, 3));
        uppl.minarPlontur.get(0).takaUtVokvun(LocalDate.of(2023, 4, 3));

        System.out.println(uppl.minarPlontur.get(0));

        //uppl.finnaFyrriVokvanir();
        System.out.println("fyrri vokvanir: " + uppl.fyrriVokvanir);
        System.out.println("Naestu vokvanir: " + uppl.naestuVokvanir);*/
    }
}
