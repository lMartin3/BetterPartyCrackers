package dev.martinl.betterpartycrackers.util;

import dev.martinl.betterpartycrackers.data.PartyCrackerReward;
import dev.martinl.betterpartycrackers.data.serialization.SerializeEnumAsString;
import dev.martinl.betterpartycrackers.data.serialization.SerializeEnumListAsStringList;
import dev.martinl.betterpartycrackers.data.serialization.SerializeRewardList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SerializerUtil {
    @SuppressWarnings({"unchecked"})
    public static <T> T deserialize(Object obj, Map<?, ?> data) {
        for (Field field : obj.getClass().getFields()) {
            try {
                String fieldNameInYaml = StringUtil.convertToSnakeCase(field.getName());
                Object valueInConfig = data.get(fieldNameInYaml);
                if (valueInConfig == null) {
                    continue;
                }

                SerializeEnumAsString serializeAsStringAnnotation = field.getAnnotation(SerializeEnumAsString.class);
                SerializeEnumListAsStringList serializeAsStringListAnnotation = field.getAnnotation(SerializeEnumListAsStringList.class);
                SerializeRewardList serializeRewardListAnnotation = field.getAnnotation(SerializeRewardList.class);

                if (serializeAsStringAnnotation != null) {
                    Object correctValue = Enum.valueOf(serializeAsStringAnnotation.enumType(), (String) valueInConfig);
                    field.set(obj, correctValue);

                } else if (serializeAsStringListAnnotation != null) {
                    List<String> stringList = (List<String>) valueInConfig;
                    for (String inConfig : stringList) {
                        Object correctValue = Enum.valueOf(serializeAsStringListAnnotation.enumType(), inConfig);
                        ((List<Object>) field.get(obj)).add(correctValue);
                    }
                } else if (serializeRewardListAnnotation != null) {
                    List<PartyCrackerReward> rewardList = new ArrayList<>();
                    List<Map<?, ?>> serializedRewards = (List<Map<?, ?>>) valueInConfig;
                    for (Map<?, ?> serializedReward : serializedRewards.stream().toList()) {
                        PartyCrackerReward deserializedReward = deserialize(new PartyCrackerReward(), serializedReward);
                        rewardList.add(deserializedReward);
                    }
                    field.set(obj, rewardList);
                } else {
                    field.set(obj, valueInConfig);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) obj;
    }

    public static Map<String, Object> serialize(Object cracker) {
        Map<String, Object> mappedValues = new LinkedHashMap<>();
        for (Field field : cracker.getClass().getFields()) {
            try {
                String configFieldName = StringUtil.convertToSnakeCase(field.getName());
                Object fieldValue = field.get(cracker);
                if (field.getAnnotation(SerializeEnumAsString.class) != null) {
                    mappedValues.put(configFieldName, fieldValue.toString());
                } else if (field.getAnnotation(SerializeEnumListAsStringList.class) != null && fieldValue instanceof List<?> fieldAsList) {
                    mappedValues.put(configFieldName, fieldAsList.stream().map(Object::toString).toList());
                } else if (field.getAnnotation(SerializeRewardList.class) != null) {
                    List<Map<String, Object>> serializedRewards = new ArrayList<>();
                    for (PartyCrackerReward reward : (List<PartyCrackerReward>) fieldValue) {
                        serializedRewards.add(serialize(reward));
                    }
                    mappedValues.put(StringUtil.convertToSnakeCase(field.getName()), serializedRewards);
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
