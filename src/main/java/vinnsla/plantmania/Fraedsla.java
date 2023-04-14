package vinnsla.plantmania;

import java.util.ArrayList;
import java.util.List;

/**
 * Þessi klasi er ekki góður, og verður tekinn út og í staðinn lesið inn úr skrá. Bara til bráðabirgða.
 * todo: laga þetta
 */
public class Fraedsla {
    private String misting;
    private String gardkanna;
    private String bottom;
    private String gradual;

    private String almennt1;
    private String almennt2;

    private List<String> vandamalListi = new ArrayList<>();

    private List<String> almenntListi;

    public Fraedsla() {
        almennt1 = "Athugið að dagsetningar eru aðeins til að miða við, og ef forritið segir að það eigi að vökva " +
                "plöntuna í dag þýðir það einfaldlega að það ætti að fara að athuga með hana. Þetta kemur líka ekki í " +
                "staðin fyrir að fylgjast með plöntunum sjálfur, þar sem margir þættir hafa áhrif á það hversu oft þarf " +
                "að vökva sem ekki er hægt að reikna með.\n" +
                "Það er sérstaklega mikilvægt að fylgjast vel með plöntunni í fyrstu og lagfæra/adjust tímann milli " +
                "vökvana eftir því sem hentar, og þegar umhverfisaðstæður breytast.\n";

        almennt2 = "Almennt skal vökva oftar á sumrin en á veturna. \nYfirleitt er best að gegnbleyta moldina við " +
                "vökvun en leyfa henni að þorna vel á milli. Mikilvægt er að planta sitji ekki í vatni, þar sem það " +
                "getur valdið \"root rot\". Það er í lagi að vökva flestar plöntur neðan frá svo lengi sem moldin er " +
                "rétt og nógu góð.\n";


        //Samantekt á nokkrum algengum vövkunaraðferðum:

        misting = "Þessi aðferð felur í sér að spreyja plöntuna með úðabrúsa. Þetta er yfirleitt ekki nóg eitt og sér, " +
                "heldur er gert samhliða öðrum vökvunaraðferðum. Þetta er yfirleitt gert við plöntur sem koma frá svæðum með " +
                "miklum raka, t.d. úr \"the tropics\", svo að ef loftið er þurrt getur þetta hjálpað verulega, þó það sé " +
                "almennt ekki talið vera nauðsyn. Varast skal að úða of mikið, en of mikill raki til lengri tíma " +
                "getur valdið ýmsum vandamálum, s.s. skemmdum á plöntunni, sveppamyndun, og laðað að sér pestir. \n";

        gardkanna = "Með garðkönnu: Þetta er ein algengasta og einfaldasta leiðin til að vökva pottaplöntur, en hér er vatni helt yfir " +
                "plöntuna eða í moldina hjá plöntunni. Þetta er fljótlegt, en getur verið ónákvæmara en aðrar aðferðir. Það er auðvelt " +
                "að ofvökva með þessari aðferð, svo það þarf að vera meðvitaður um það, og gott getur verið að hafa garðkönnu " +
                "sem dreifir vatninu í stað þess að hella því út um einn stút\n";

        bottom = "Þessi aðferð felur í sér að setja pottinn í standandi vatn svo plantan nái að sjúga vatnið upp " +
                "neðan frá. Þetta gengur aðeins fyrir plöntur í pottum með götum. Skilja plöntuna eftir í vatninu í nokkrar klukkustundir, " +
                "þangað til hún er hætt að draga í sig vatn. Tæma skal ílátið og leyfa umfram vatni að leka af pottunum. Svona er " +
                "hægt að vökva margar plöntur í einu, en getur dreift sjúkdómum og pestum milli planta, svo æskilegt er að " +
                "gæta þess að allar plönturnar séu heilbrigðar að þessu leiti. Þessi aðferð er sérstaklega góð fyrir þá sem eiga það til " +
                "að vökva of lítið eða of mikið, þar sem rakinn er \"consistent\"\n";

        gradual = "Þessi aðferð er sérstaklega hentug fyrir þá sem ferðast mikið eða eru lítið heima. Maður setur vatn " +
                "í sérstakt ílát sem sem hefur langan stút sem stungið er í moldina og hleypir vatni hægt og rólega niður. " +
                "Hægt er að kaupa það í búð eða nota plastflösku\n";

        String vandamal = "Algeng vandamál:\n" +
                "Þurr og hörð mold: ef þú finnur engan raka þegar þú stingur fingrinum í moldina þarf að vökva oftar\n" +
                "Brún og stökk (crisp) blöð: of mikið eða of lítið vatn (úff)\n" +
                "Blómstrar ekki: Ef plantan ætti að vera í blóma en er það ekki er líklegt að hún sé ekki að fá rétt magn af vatni\n" +
                "Lin eða hangandi (drooping or sagging): ekki rétt magn af vatni\n" +
                "Soggy soil: Plantan ætti ekki að vera í blautri mold til lengri tíma, heldur ætti að leyfa moldinni að þorna inn á milli til að halda rótunum heilbrigðum\n" +
                "Ýmis vandamál geta líka gefið til kynna að sjúkdómar eða pestir hafi áhrif, en það er ekki hér\n";


        String umVandamal = "Hér fyrir neðan eru upplýsingar um nokkur vandamál sem plöntueigandi getur glímt við. Því miður" +
                " er listinn ekki tæmandi, og stundum geta mismunandi vandamál haft mjög svipuð einkenni, svo erfitt er að" +
                " dæma um hvað það er.\n";

        String solbruni = "Vandamál: Sólbruni/sólbrunnin lauf\n" +
                "Einkenni: lauf eða aðrir hlutar plöntunnar verða brúnir\n" +
                "Lausn: Fjarlægja plöntuna úr beinu sólarljósi. Það gæti þurft að fjarlægja einhver lauf eða aðra hluta plöntunnar\n";

        String ofvokvun = "Vandamál: Ofvökvun\n" +
                "Einkenni: gulnandi lauf, root rot... getur laðað að ýmis skordýr og pestir\n" +
                "Lausn: Passa að það eru göt (e. drainage holes) á pottinum og að umfram vatn safnist ekki á botninum. Vökva bara þegar moldin er þurr viðkomu.\n";
        String brunLauf = "Vandamál: Brún lauf/ Undirvökvun/vatnsskortur\n" +
                "Einkenni: wilting, drooping eða brún lauf. sérstaklega algengt með stórar plöntur innandyra, þær þurfa meira vatn en meðal planta. Ef potturinn er óeðlilega léttur þarf að vökva\n" +
                "Lausn: ef moldin er alveg þurr skal leggja hana vel í bleyti. Ef plantan hafði tímabil með miklum þurrki getur verið erfitt að lagast alveg\n";

        String teygja = "Vandamál: Planta teygir sig - Skortur á ljósi\n" +
                "Einkenni: Gróðurinn verður ljósgrænn og plantan hallast í átt að ljósi, og hækkar oft\n" +
                "Lausn: Geyma í/við suðurglugga, auka birtu, fá jafnvel ræktunarljós\n";

        String gul = "Vandamál: gulnandi lauf - ofvökvun, lítið ljós, lítill raki, lélegt frárennsli\n" +
                "Einkenni: gulnuð lauf\n" +
                "Lausn: breyta rútínunni, vökva e.t.v. minna, auka birtu og/eða raka, bæta frárennsli við pottinn\n";

        String skordyr = "Skordýr: fer eftir dýrinu, halda sig oft í moldinni eða undir laufunum og geta valdið því að plantan afmyndast að einhverju leiti. Lausnin fer eftir skordýrinu, en meðal annars er hægt að fá sérstök \"pesticide\" fyrir pottaplöntur. Stundum virkar líka að skola plöntuna undir rennandi vatni, og jafnvel með uppþvottalegi eða plöntusápu\n" +
                "Root rot: ræturnar verða svartar og mjúkar. Plantan verður oft veikluleg. Vökva sjaldnar. í slæmum tilfellum skal umpotta og taka burt þá hluta rótarinnar sem eru skemmdir\n" +
                "mygla og sveppir: ves, smitandi. Stundum þarf að taka sýkta hluta af plöntunni. Yfirleitt er gott að auka loftflæði og minnka raka í umhverfinu\n" +
                "Blelltir á laufum (e. leaf spots): svartir, brúnir, gulir eða drappaðir blettir á laufum. Fjarlægja sýkt lauf, auka loftflæði og passa að vökva á daginn svo plantan nái að þorna aðeins fyrir nóttina\n";


        String annad = "Annað?\n" +
                "Plöntusturta?\n" +
                "Um potta! Alltaf hafa göt á botninum, en hægt að nota tvo potta saman svo ytri potturinn safni vatninu og maður hellir því úr\n" +
                "Sumir setja plöntuna jafnvel á bólakaf.\n" +
                "Ath að þegar skemmdir hlutar eru fjarlægðir af plöntunni er það yfirleitt til að hún sé ekki að eyða orku í að reyna að laga þá\n" +
                "er ryk slæmt??";

        String almennt3 = "Ekki er til nein ein besta leið til að vökva allar plöntur, heldur eru ýmsir þættir sem geta haft áhrif. Mestu máli skiptir hvernig planta þetta er og hvaðan hún kemur, en það getur verið gott að líkja eftir náttúrulegu umhverfi hennar. En einnig þarf að taka aðra þætti með í reikninginn, eins og hvernig mold og pott plantan er í, hversu mikið ljós hún fær, rakastig og hita.\n";


        //vandamalListi = new ArrayList<>(List.of(vandamal, solbruni, ofvokvun, brunLauf, teygja, gul, skordyr));
        System.out.println();
        List<String> listi = new ArrayList<>(List.of(umVandamal, solbruni, ofvokvun, brunLauf, teygja, gul));//vandamal, skordyr
        //vandamalListi.addAll(List.of(vandamal, solbruni, ofvokvun, brunLauf, teygja, gul, skordyr));
        vandamalListi = listi;

        almenntListi = new ArrayList<>(List.of(almennt1, almennt2, almennt3));
    }

    public String getMisting() {
        return misting;
    }

    public String getGardkanna() {
        return gardkanna;
    }

    public String getBottom() {
        return bottom;
    }

    public String getGradual() {
        return gradual;
    }

    public List<String> getVandamalListi() {
        return vandamalListi;
    }

    public List<String> getAlmenntListi() {
        return almenntListi;
    }
}
