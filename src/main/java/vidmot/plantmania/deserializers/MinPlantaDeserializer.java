package vidmot.plantmania.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.enums.Eitrun;
import vinnsla.plantmania.enums.Ljosstyrkur;
import vinnsla.plantmania.enums.Uppruni;
import vinnsla.plantmania.enums.Vatnsthorf;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;


//@JsonDeserialize(using = MinPlantaDeserializer.class)
// notum MinPlantaDeserializer til að lesa í klasann í staðinn fyrir sjálfgefinn?

public class MinPlantaDeserializer extends JsonDeserializer<MinPlanta> {
    public MinPlantaDeserializer() {

    }

    @Override
    public MinPlanta deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        JsonNode node = objectMapper.readTree(parser);
        MinPlanta minPlanta = new MinPlanta();


        //venjuleg gildi
        minPlanta.setLatnesktNafn(node.get("latnesktNafn").asText());
        minPlanta.setAlmenntNafn(node.get("almenntNafn").asText());
        minPlanta.setMyndaslod(node.get("myndaslod").asText());
        minPlanta.setTexti(node.get("texti").asText());
        minPlanta.setAlmennurTimiMilliVokvana(node.get("almennurTimiMilliVokvana").asInt());
        minPlanta.setLjosStundir(node.get("ljosStundir").asInt());
        minPlanta.setNickName(node.get("nickName").asText());
        minPlanta.setNotesFraNotanda(node.get("notesFraNotanda").asText());
        minPlanta.setMedaltimiMilliVokvana(node.get("medaltimiMilliVokvana").asInt());
        minPlanta.setThinnTimiMilliVokvana(node.get("thinnTimiMilliVokvana").asInt());
        minPlanta.setNaestaVokvun(node.get("naestaVokvun").asInt());

        //og LocalDate (var objectProperty).
        Integer[] dagur = objectMapper.treeToValue(node.get("sidastaVokvun"), Integer[].class);
        minPlanta.setSidastaVokvun(LocalDate.of(dagur[0], dagur[1], dagur[2]));


        //og enum. er þetta rétt?
        minPlanta.setUppruni(Uppruni.valueOf(node.get("uppruni").asText()));
        minPlanta.setLjosstyrkur(Ljosstyrkur.valueOf(node.get("ljosstyrkur").asText()));
        minPlanta.setEitrun(Eitrun.valueOf(node.get("eitrun").asText()));
        minPlanta.setVatnsthorf(Vatnsthorf.valueOf(node.get("vatnsthorf").asText()));


        //og venjulegir listar. hvernig er þetta?
        minPlanta.setOllHeiti(Arrays.asList(objectMapper.treeToValue(node.get("ollHeiti"), String[].class)));
        minPlanta.setEinkenniPlontu(Arrays.asList(objectMapper.treeToValue(node.get("einkenniPlontu"), String[].class)));
        minPlanta.setKjorhitastig(Arrays.asList(objectMapper.treeToValue(node.get("kjorhitastig"), Integer[].class)));


        //veit að þetta virkar fyrir venjulega lista!
        /*JsonNode childNodes1 = node.get("ollHeiti");
        List<String> children1 = new ArrayList<>();
        for (JsonNode childNode : childNodes1) {
            String child = childNode.asText();
            children1.add(child);
        }
        minPlanta.setOllHeiti(children1);

        JsonNode childNodes2 = node.get("einkenniPlontu");
        List<String> children2 = new ArrayList<>();
        for (JsonNode childNode : childNodes2) {
            String child = childNode.asText();
            children2.add(child);
        }
        minPlanta.setEinkenniPlontu(children2);

        JsonNode childNodes3 = node.get("kjorhitastig");
        List<Integer> children3 = new ArrayList<>();
        for (JsonNode childNode : childNodes3) {
            int child = childNode.asInt();
            children3.add(child);
        }
        minPlanta.setKjorhitastig(children3);*/


        //observableList fyrir vökvanir. Hef ekki hugmynd
        JsonNode vokvanirNodes = node.get("vokvanir");
        ObservableList<LocalDate> vokvanir = FXCollections.observableArrayList();
        for (JsonNode vokvunNode : vokvanirNodes) {
            Integer[] dates = objectMapper.treeToValue(vokvunNode, Integer[].class);
            vokvanir.add(LocalDate.of(dates[0], dates[1], dates[2]));
        }
        minPlanta.setVokvanir(vokvanir);

        JsonNode planaarVokvanirNodes = node.get("planadarVokvanir");
        ObservableList<LocalDate> planadarVokvanir = FXCollections.observableArrayList();
        for (JsonNode plonudVokvunNode : planaarVokvanirNodes) {
            Integer[] dates = objectMapper.treeToValue(plonudVokvunNode, Integer[].class);
            planadarVokvanir.add(LocalDate.of(dates[0], dates[1], dates[2]));
        }
        minPlanta.setPlanadarVokvanir(planadarVokvanir);

        return minPlanta;
    }
}
