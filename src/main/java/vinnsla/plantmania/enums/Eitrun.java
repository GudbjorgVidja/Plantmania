/**
 * enum fyrir það hvort plantan er eitruð eður ei.
 * mjög gott, en væri kannski hægt að lengja einhver skilaboðanna og birta beint í plöntuglugganum?
 * TIL_MANNELDIS gæti verið eitthvað eins og "*plantan* er æt, og er víða notuð til manneldis"
 */
package vinnsla.plantmania.enums;

public enum Eitrun {
    TIL_MANNELDIS("Til manneldis"),
    SAKLAUS("Alveg skaðlaus"),
    BARA_SMA("Eitruð í miklu magni"),
    ERTING("Ertir húð"),
    ERTING_MIKIL("Ertir húð verulega"),
    NIDURGANGUR_ISH("Getur valdið niðurgangi og óþægindum eftir neyslu"),
    SJUKRAHUS("Mjög eitruð. Leitið til læknis eftir neyslu"),
    BANEITRUD("Baneitruð! Hringið strax á neyðarlínuna eftir neyslu"),
    BARA_FYRIR_BORN("Eitruð börnum en ekki fullorðnum"),
    BARA_FYRIR_KETTI("Eitruð köttum"),
    BARA_FYRIR_HUNDA("Eitruð hundum"),
    BARA_FYRIR_FUGLA("Eitruð fuglum"),
    BARA_FYRIR_DYR("Eitruð flestum dýrum"),
    MADUR_VERDUR_SKAKKUR("Vímugjafi");

    private String eitrunarSkilabod;

    Eitrun(String eitrunarSkilabod) {
        this.eitrunarSkilabod = eitrunarSkilabod;
    }
}
