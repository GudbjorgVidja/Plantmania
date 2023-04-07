package vidmot.plantmania;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Notendaupplysingar;

import java.io.IOException;
import java.time.LocalDate;

//@JsonDeserialize(using = NotendaupplysingarDeserializer.class)
public class NotendaupplysingarDeserializer extends JsonDeserializer<Notendaupplysingar> {
    public NotendaupplysingarDeserializer() {

    }

    @Override
    public Notendaupplysingar deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        Notendaupplysingar notendaupplysingar = new Notendaupplysingar();
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        JsonNode node = objectMapper.readTree(parser);

        // setjum venjulegu gildin - tilviksbreyturnar eru tvær en gætu verið færri eða fleiri
        //engin hér

        //listarnir
        JsonNode childNodes = node.get("minarPlontur");
        ObservableList<MinPlanta> children = FXCollections.observableArrayList();

        //ítrum yfir stökin á listanum
        for (JsonNode childNode : childNodes) {
            //listinn inniheldur MinPlanta hluti, en MinPlanta hefur líka ObservableList, svo það þarf að gera sér
            //deserializer fyrir það. Er þetta þá rétt?
            MinPlanta minPlanta = objectMapper.treeToValue(childNode, MinPlanta.class);
            children.add(minPlanta);
        }
        notendaupplysingar.setMinarPlontur(children); // setjum listann inn í tilviksbreytuna


        // Deserialize fyrriVokvanir
        ObservableList<Pair<MinPlanta, LocalDate>> fyrriVokvanir = FXCollections.observableArrayList();
        JsonNode fyrriVokvanirNode = node.get("fyrriVokvanir");
        if (fyrriVokvanirNode != null) {
            for (JsonNode pairNode : fyrriVokvanirNode) {
                MinPlanta minPlanta = objectMapper.treeToValue(pairNode.get("key"), MinPlanta.class);
                
                LocalDate date = LocalDate.parse(pairNode.get("value").asText());
                fyrriVokvanir.add(new Pair<>(minPlanta, date));
            }
        }
        notendaupplysingar.setFyrriVokvanir(fyrriVokvanir);

        // Deserialize naestuVokvanir
        ObservableList<Pair<MinPlanta, LocalDate>> naestuVokvanir = FXCollections.observableArrayList();
        JsonNode naestuVokvanirNode = node.get("naestuVokvanir");
        if (naestuVokvanirNode != null) {
            for (JsonNode pairNode : naestuVokvanirNode) {
                MinPlanta minPlanta = objectMapper.treeToValue(pairNode.get("key"), MinPlanta.class);
                LocalDate date = LocalDate.parse(pairNode.get("value").asText());
                naestuVokvanir.add(new Pair<>(minPlanta, date));
            }
        }
        notendaupplysingar.setNaestuVokvanir(naestuVokvanir);

        return notendaupplysingar;
    }
}
