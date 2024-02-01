package com.biserv.victorp.research.phdedhec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BirthRecord {

    public static final String STRING_NULL = "<null>";
    private String value;

    private Map<String, BirthRecordMetadata> birthRecordMetadataMap;

    private Map<String, String> dataMap;
    private List<String> datas;

    public BirthRecord() {
        setValue(null);
        dataMap = new HashMap<>();
        datas = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField(int posMin, int posMax) {
        String result = "";
        if (this.value == null ||
                posMax < posMin ||
                posMax > this.value.length() ||
                posMin < 0 ||
                posMax < 0) {
            return result;
        }
        result = value.substring(posMin-1, posMax);
        return result;
    }

    public Map<String, BirthRecordMetadata> getBirthRecordMetadataMap() {
        return birthRecordMetadataMap;
    }

    public void setBirthRecordMetadataMap(Map<String, BirthRecordMetadata> birthRecordMetadataMap) {
        this.birthRecordMetadataMap = birthRecordMetadataMap;
    }

    public void buildDataMap(String[] tags) {
        dataMap.clear();
        datas.clear();
        for (String tag: tags) {
            BirthRecordMetadata birthRecordMetadata = getBirthRecordMetadataMap().get(tag);
            String value;
            if (birthRecordMetadata != null) {
                value = getField(birthRecordMetadata.getPositionMin(), birthRecordMetadata.getPositionMax());
                dataMap.put(tag, value);
            } else {
                value = STRING_NULL;
            }
            datas.add(value);
        }
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public List<String> getDatas() {
        return datas;
    }
}
