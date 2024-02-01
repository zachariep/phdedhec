package com.biserv.victorp.research.phdedhec;

import java.util.List;

public class BirthRecordMetadata {
    public static final String ALIGN_CENTER = "center";
    public static final String ALIGN_JUSTIFY = "justify";
    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_RIGHT = "right";
    public static final String CHAR_BRACKET_LEFT = "{";
    public static final String CHAR_BRACKET_RIGHT = "}";
    public static final String CHAR_COLON = ",";
    public static final String CHAR_DOLLAR = "$";
    public static final String CHAR_EQUAL = "=";
    public static final String CHAR_MINUS = "-";
    public static final String CHAR_TILDA = "~";
    public static final String CHAR_SEMICOLON = ";";
    public static final String STYLE_TEXT_FORMAT_CENTER = "-fx-alignment: CENTER;";
    public static final String STYLE_TEXT_FORMAT_JUSTIFY = "-fx-alignment: CENTER-JUSTIFY;";
    public static final String STYLE_TEXT_FORMAT_LEFT = "-fx-alignment: CENTER-LEFT;";
    public static final String STYLE_TEXT_FORMAT_RIGHT = "-fx-alignment: CENTER-RIGHT;";
    public static final String NAME_COLUMN = "Column";
    public static final String SORT_TYPE_PROP_ASC = "asc";
    public static final String SORT_TYPE_PROP_DESC = "desc";
    public static final String TYPE_PROP_DATE = "date";
    public static final String TYPE_PROP_LONG = "long";
    public static final String TYPE_PROP_STRING = "string";
    public static final String FORMAT_PROP_NONE = "none";

    private String tag;
    private String name;
    private String value;
    private String[] values;

    public BirthRecordMetadata(String tag, String name, String value) {
        this.tag = tag;
        this.name = name;
        this.value = value;
        this.values = value.split(CHAR_COLON);
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getDisplayName() {
        if (values.length>0 && !values[0].trim().isEmpty()) {
            return values[0].trim();
        } else {
            return String.format("%s %03d",NAME_COLUMN, 1);
        }
    }

    public String getDataType() {
        if (values.length>1 && !values[1].trim().isEmpty()) {
            return values[1].trim();
        } else {
            return TYPE_PROP_STRING;
        }
    }

    public int getPositionMin() {
        if (values.length>2 && !values[2].trim().isEmpty()) {
            try {
                return Double.valueOf(values[2].trim()).intValue();
            } catch (NumberFormatException nfe) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getPositionMax() {
        if (values.length>3 && !values[3].trim().isEmpty()) {
            try {
                return Double.valueOf(values[3].trim()).intValue();
            } catch (NumberFormatException nfe) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public String getSortType() {
        if (values.length>4 && !values[4].trim().isEmpty()) {
            return values[4].trim().equalsIgnoreCase(SORT_TYPE_PROP_DESC)?SORT_TYPE_PROP_DESC:SORT_TYPE_PROP_ASC;
        } else {
            return SORT_TYPE_PROP_ASC;
        }
    }

    public String getDataAlignment() {
        if (values.length>5 && !values[5].trim().isEmpty()) {
            String align = values[5].trim();
            if (align.equals(ALIGN_CENTER)) {
                return ALIGN_CENTER;
            } else if (align.equals(ALIGN_RIGHT)) {
                return ALIGN_RIGHT;
            } else if (align.equals(ALIGN_JUSTIFY)) {
                return ALIGN_JUSTIFY;
            }else {
                return ALIGN_LEFT;
            }
        } else {
            return FORMAT_PROP_NONE;
        }
    }

    public String getDataFormat() {
        if (values.length>6 && !values[6].trim().isEmpty()) {
            return values[6].trim();
        } else {
            return FORMAT_PROP_NONE;
        }
    }

    public String getValue(String data, List<BirthRecordMetadata> birthRecordMetadataList, int height) {
        if (getPositionMin() > getPositionMax() || height >= 10) {
            return "";
        } else if (getDataFormat().indexOf(CHAR_DOLLAR) >-1) {
            String value = "";
            //Contain variable
            String[] varFormats = getDataFormat().trim().replace(CHAR_BRACKET_LEFT, "")
                    .replace(CHAR_BRACKET_RIGHT, "")
                    .split(CHAR_SEMICOLON);
            for (String varFormat : varFormats) {
                String[] varOptions = varFormat.split(CHAR_TILDA);
                if (varOptions.length == 3 && varOptions[0].startsWith(CHAR_DOLLAR)) {
                    String tag = varOptions[0].substring(CHAR_DOLLAR.length()).trim();
                    if (!tag.equals(getTag())) {
                        BirthRecordMetadata birthRecordMetadata = getBirthRecordMetadata(tag,  birthRecordMetadataList);
                        if (birthRecordMetadata != null) {
                            String valueInner = birthRecordMetadata.getValue(data, birthRecordMetadataList, height + 1);
                            if (valueInner.equals(varOptions[1])) {
                                return varOptions[2];
                            }
                        }
                    }
                }
            }
            return value;
        } else if (getPositionMin() >= 1 && getPositionMax() <= data.length()) {
            int beginIndex = getPositionMin() - 1;
            int endIndex = getPositionMax();
            String value = data.substring(beginIndex, endIndex);
            return value;
        }

        return "";
    }

    public BirthRecordMetadata getBirthRecordMetadata(String tag, List<BirthRecordMetadata> birthRecordMetadataList) {
        for (BirthRecordMetadata birthRecordMetadata : birthRecordMetadataList) {
            if (birthRecordMetadata.getTag().equals(tag)) {
                return birthRecordMetadata;
            }
        }
        return null;
    }
}
