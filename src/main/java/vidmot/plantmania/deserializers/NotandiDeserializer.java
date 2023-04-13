package vidmot.plantmania.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import vinnsla.plantmania.MinPlanta;
import vinnsla.plantmania.Notandi;

import java.io.IOException;

public class NotandiDeserializer extends JsonDeserializer<Notandi> {
    public static void main(String[] args) {

    }

    public Notandi deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) parser.getCodec();
        JsonNode node = objectMapper.readTree(parser);
        Notandi notandi = new Notandi();

        notandi.setLykilord(node.get("lykilord").asText());
        notandi.setNotendanafn(node.get("notendanafn").asText());


        JsonNode minarPlonturNodes = node.get("minarPlontur");
        ObservableList<MinPlanta> minarPlontur = FXCollections.observableArrayList();
        for (JsonNode minPlantaNode : minarPlonturNodes) {
            MinPlanta minPlanta = objectMapper.treeToValue(minPlantaNode, MinPlanta.class);
            minarPlontur.add(minPlanta);
        }
        notandi.setMinarPlontur(minarPlontur);

        return notandi;
    }
}
