package vidmot.plantmania.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

/**
 * Bara svo það sé á hreinu er þetta af netinu. Ég var að vesenast með þetta í heilan dag, sneri mér svo
 * til ChatGPT og vesenaðist aðeins þar, og fékk þetta
 * Ég er hætt að nota þetta
 */
class ObservableListDeserializer extends JsonDeserializer<ObservableList<?>> {

    // @Override
    public ObservableList<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ArrayNode node = mapper.readTree(jp);
        ObservableList<Object> list = FXCollections.observableArrayList();
        for (JsonNode elementNode : node) {
            Object element = mapper.readValue(elementNode.toString(), Object.class);
            list.add(element);
        }
        return list;
    }
}
