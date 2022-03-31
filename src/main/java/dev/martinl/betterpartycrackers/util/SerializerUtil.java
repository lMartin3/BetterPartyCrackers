package dev.martinl.betterpartycrackers.util;

import dev.martinl.betterpartycrackers.data.PartyCracker;
import dev.martinl.betterpartycrackers.data.SerializeEnumAsString;
import dev.martinl.betterpartycrackers.data.SerializeEnumListAsStringList;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SerializerUtil {
    @SuppressWarnings({"unchecked"})
    public static PartyCracker createPartyCrackerFromSerializedData(Map<?, ?> data) {
        PartyCracker partyCracker = new PartyCracker();
        for (Field field : PartyCracker.class.getFields()) {
            try {
                String fieldNameInYaml = StringUtil.convertToSnakeCase(field.getName());
                Object valueInConfig = data.get(fieldNameInYaml);
                if (valueInConfig == null) {
                    continue;
                }

                SerializeEnumAsString serializeAsStringAnnotation = field.getAnnotation(SerializeEnumAsString.class);
                SerializeEnumListAsStringList serializeAsStringListAnnotation = field.getAnnotation(SerializeEnumListAsStringList.class);

                if (serializeAsStringAnnotation != null) {
                    Object correctValue = Enum.valueOf(serializeAsStringAnnotation.enumType(), (String) valueInConfig);
                    field.set(partyCracker, correctValue);

                } else if (serializeAsStringListAnnotation != null) {
                    List<String> stringList = (List<String>) valueInConfig;
                    for (String inConfig : stringList) {
                        Object correctValue = Enum.valueOf(serializeAsStringListAnnotation.enumType(), inConfig);
                        ((List<Object>) field.get(partyCracker)).add(correctValue);
                    }
                } else {
                    field.set(partyCracker, valueInConfig);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return partyCracker;
    }

    public static Map<String, Object> serializePartyCracker(PartyCracker cracker) {
        Map<String, Object> mappedValues = new LinkedHashMap<>();
        for (Field field : cracker.getClass().getFields()) {
            try {
                String configFieldName = StringUtil.convertToSnakeCase(field.getName());
                Object fieldValue = field.get(cracker);
                if (field.getAnnotation(SerializeEnumAsString.class) != null) {
                    mappedValues.put(configFieldName, fieldValue.toString());
                } else if (field.getAnnotation(SerializeEnumListAsStringList.class) != null && fieldValue instanceof List<?> fieldAsList) {
                    mappedValues.put(configFieldName, fieldAsList.stream().map(Object::toString).toList());
                } else {
                    mappedValues.put(StringUtil.convertToSnakeCase(field.getName()), fieldValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mappedValues;
    }
}
