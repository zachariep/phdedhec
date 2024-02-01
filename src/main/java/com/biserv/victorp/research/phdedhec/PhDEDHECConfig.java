package com.biserv.victorp.research.phdedhec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PhDEDHECConfig {
    private static final Logger logger = LogManager.getLogger(PhDEDHECConfig.class.getName());

    public static final String POS_YEAR = "pos_year";
    public static final String POS_STATE = "pos_state";
    public static final String POS_CITY = "pos_city";
    public static final String POS_COUNTY = "pos_county";
    public static final String POS_RACE_FATHER = "pos_race_father";
    public static final String POS_RACE_MOTHER = "pos_race_mother";
    public static final String POS_HISPANIC_ORIGIN_FATHER = "pos_hispanic_origin_father";
    public static final String POS_HISPANIC_ORIGIN_MOTHER = "pos_hispanic_origin_mother";
    public static final String FORMULA_RACE_CODE_NAME = "formula_race_code_name";
    public static final String FORMULA_HISPANIC_CODE_ORIGIN = "formula_hispanic_code_origin";
    public static final String FORMULA_RACE_NAME_INDEX_NAME = "formula_race_name_index_name";
    public static final String FORMULA_RACE_CHILD_PARENTS_INDEX_NAME = "formula_race_child_parents_index_name";
    public static final String RESULT_YEAR = "result_year";
    public static final String RESULT_STATE = "result_state";
    public static final String RESULT_COUNTY = "result_county";
    public static final String RESULT_CITY = "result_city";
    public static final String RESULT_RACE_CHILD = "result_race_child";
    public static final String RESULT_SUM_CHILD = "result_sum_child";
    public static final String FILES = "files";
    public static final String FOLDER = "folder";
    public static final String PROCESS = "process";
    public static final String POS_YEAR_DISPLAY_NAME  = "Year";
    public static final String POS_STATE_DISPLAY_NAME  = "State";
    public static final String POS_CITY_DISPLAY_NAME  = "City";
    public static final String POS_COUNTY_DISPLAY_NAME  = "County";
    public static final String POS_RACE_FATHER_DISPLAY_NAME  = "Race of Father";
    public static final String POS_RACE_MOTHER_DISPLAY_NAME  = "Race of Mother";
    public static final String POS_HISPANIC_ORIGIN_FATHER_DISPLAY_NAME  = "Hispanic origin of Father";
    public static final String POS_HISPANIC_ORIGIN_MOTHER_DISPLAY_NAME  = "Hispanic origin of Mother";
    public static final String POS_COUNT = "count";
    public static final String POS_COUNT_DISPLAY_NAME  = "Count";

    public static final String[] TAG_LIST = new String[] {
        POS_YEAR ,
        POS_STATE ,
        POS_CITY ,
        POS_COUNTY ,
        POS_RACE_FATHER ,
        POS_RACE_MOTHER ,
        POS_HISPANIC_ORIGIN_FATHER ,
        POS_HISPANIC_ORIGIN_MOTHER
    };

    public static final String[] TAG_DISPLAY_NAME_LIST = new String[] {
            POS_YEAR_DISPLAY_NAME ,
            POS_STATE_DISPLAY_NAME ,
            POS_CITY_DISPLAY_NAME ,
            POS_COUNTY_DISPLAY_NAME ,
            POS_RACE_FATHER_DISPLAY_NAME ,
            POS_RACE_MOTHER_DISPLAY_NAME ,
            POS_HISPANIC_ORIGIN_FATHER_DISPLAY_NAME ,
            POS_HISPANIC_ORIGIN_MOTHER_DISPLAY_NAME
    };

    public static final String[][] US_STATE_MAP = new String[][] {
            {"01", "Alabama"} , {"02", "Alaska"} , {"03", "Arizona"} , {"04", "Arkansas"} , {"05", "California"} ,
            {"06", "Colorado"} , {"07", "Connecticut"} , {"08", "Delaware"} , {"09", "District of Columbia"} , {"10", "Florida"} ,
            {"11", "Georgia"} , {"12", "Hawaii"} , {"13", "Idaho"} , {"14", "Illinois"} , {"15", "Indiana"} ,
            {"16", "Iowa"} , {"17", "Kansas"} , {"18", "Kentucky"} , {"19", "Louisiana"} , {"20", "Maine"} ,
            {"21", "Maryland"} , {"22", "Massachusetts"} , {"23", "Michigan"} , {"24", "Minnesota"} , {"25", "Mississippi"} ,
            {"26", "Missouri"} , {"27", "Montana"} , {"28", "Nebraska"} , {"29", "Nevada"} , {"30", "New Hampshire"} ,
            {"31", "New Jersey"} , {"32", "New Mexico"} , {"33", "New York"} , {"34", "North Carolina"} , {"35", "North Dakota"} ,
            {"36", "Ohio"} , {"37", "Oklahoma"} , {"38", "Oregon"} , {"39", "Pennsylvania"} , {"40", "Rhode Island"} ,
            {"41", "South Carolina"} , {"42", "South Dakota"} , {"43", "Tennessee"} , {"44", "Texas"} , {"45", "Utah"} ,
            {"46", "Vermont"} , {"47", "Virginia"} , {"48", "Washington"} , {"49", "West Virginia"} , {"50", "Wisconsin"} ,
            {"51", "Wyoming"} , {"52", "Puerto Rico"} , {"53", "Virgin Islands"} , {"54", "Guam"} , {"55", "Canada"} ,
            {"56", "Cuba"} , {"57", "Mexico"} , {"58", ""} , {"59", "Remainder of the world"}
    };

    String posYear;
    String posState;
    String posCity;
    String posCounty;
    String posRaceFather;
    String posRaceMother;
    String posHispanicOriginFather;
    String posHispanicOriginMother;

    String formulaRaceCodeName;
    String formulaHispanicCodeOrigin;
    String formulaRaceNameIndexName;
    String formulaRaceChildParentsIndexName;

    String resultYear;
    String resultState;
    String resultCounty;
    String resultCity;
    String resultRaceChild;
    String resultSumChild;

    String files;
    String folder;
    String process;

    MessageDigest md5Digest;

    Map<String, PhDEDHECProp> phDEDHECPropMap;
    Map<String, PhDEDHECFile> phDEDHECFileMap;

    Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap;

    Map<String, Long> birthRecordResultCountMap;
    Map<String, Map<String, String>> birthRecordResultDataMap;

    Map<String, String> usStateCodeNameMap;
    Map<String, String> usStateNameCodeMap;

    private PhDEDHECController phDEDHECController;

    private PhDEDHECUtils phDEDHECUtils;

    public PhDEDHECConfig(PhDEDHECController phDEDHECController) {
        this.phDEDHECController = phDEDHECController;
        this.usStateCodeNameMap = new HashMap<>();
        this.usStateNameCodeMap = new HashMap<>();

        for (int index=0; index < US_STATE_MAP.length; index++) {
            getUsStateCodeNameMap().put(US_STATE_MAP[index][0], US_STATE_MAP[index][1]);
            getUsStateNameCodeMap().put(US_STATE_MAP[index][1], US_STATE_MAP[index][0]);
        }
    }

    public PhDEDHECController getPhDEDHECController() {
        return phDEDHECController;
    }

    public boolean isFileExist(String fileNameOrFolder) {
        if (StringUtils.isBlank(fileNameOrFolder)) {
            return false;
        }
        boolean exist = new File(fileNameOrFolder).exists();
        return exist;
    }

    public boolean isFileExist(File file) {
        if (file == null) {
            return false;
        }
        boolean exist = file.exists();
        return exist;
    }

    public MessageDigest getMd5Digest() {
        if (md5Digest == null) {
            try {
                md5Digest = MessageDigest.getInstance("MD5");
            } catch (java.security.NoSuchAlgorithmException ex) {
                logger.error(ex);
            }
        }
        return md5Digest;
    }

    private String getFileChecksum(MessageDigest digest, File file) throws IOException
    {
        //Get file input stream for reading the file content
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        };

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }

    public String getPosYear() {
        return posYear;
    }

    public void setPosYear(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.posYear = value;
    }

    public String getPosState() {
        return posState;
    }

    public void setPosState(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.posState = value;
    }

    public String getPosCity() {
        return posCity;
    }

    public void setPosCity(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.posCity = value;
    }

    public String getPosCounty() {
        return posCounty;
    }

    public void setPosCounty(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.posCounty = value;
    }

    public String getPosRaceFather() {
        return posRaceFather;
    }

    public void setPosRaceFather(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.posRaceFather = value;
    }

    public String getPosRaceMother() {
        return posRaceMother;
    }

    public void setPosRaceMother(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.posRaceMother = value;
    }

    public String getPosHispanicOriginFather() {
        return posHispanicOriginFather;
    }

    public void setPosHispanicOriginFather(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.posHispanicOriginFather = value;
    }

    public String getPosHispanicOriginMother() {
        return posHispanicOriginMother;
    }

    public void setPosHispanicOriginMother(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.posHispanicOriginMother = value;
    }

    public String getFormulaRaceCodeName() {
        return formulaRaceCodeName;
    }

    public void setFormulaRaceCodeName(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.formulaRaceCodeName = value;
    }

    public String getFormulaHispanicCodeOrigin() {
        return formulaHispanicCodeOrigin;
    }

    public void setFormulaHispanicCodeOrigin(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.formulaHispanicCodeOrigin = value;
    }

    public String getFormulaRaceNameIndexName() {
        return formulaRaceNameIndexName;
    }

    public void setFormulaRaceNameIndexName(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.formulaRaceNameIndexName = value;
    }

    public String getFormulaRaceChildParentsIndexName() {
        return formulaRaceChildParentsIndexName;
    }

    public void setFormulaRaceChildParentsIndexName(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.formulaRaceChildParentsIndexName = value;
    }

    public String getResultYear() {
        return resultYear;
    }

    public void setResultYear(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.resultYear = value;
    }

    public String getResultState() {
        return resultState;
    }

    public void setResultState(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.resultState = value;
    }

    public String getResultCounty() {
        return resultCounty;
    }

    public void setResultCounty(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.resultCounty = value;
    }

    public String getResultCity() {
        return resultCity;
    }

    public void setResultCity(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.resultCity = value;
    }

    public String getResultRaceChild() {
        return resultRaceChild;
    }

    public void setResultRaceChild(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.resultRaceChild = value;
    }

    public String getResultSumChild() {
        return resultSumChild;
    }

    public void setResultSumChild(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.resultSumChild = value;
    }

    public String getFiles() {
        return files;
    }

    private String getFileKey(File file) {
        Path path = Paths.get(file.getAbsolutePath());
        String key = path.getFileName().toString().trim().toLowerCase().replace('.','-');
        return key;
    }

    private void buildFileMap(String value) {
        String[] fileNames = value.trim().split(PhDEDHECController.CHAR_COLON);
        if (fileNames != null && fileNames.length > 0) {
            for (String fileName : fileNames) {
                if (isFileExist(fileName)) {
                    try {
                        File file = new File(fileName);
                        //String checksum = getFileChecksum(getMd5Digest(), file);
                        String key = getFileKey(file);
                        getPhDEDHECFileMap().put(key, new PhDEDHECFile(file, this));
                    } catch (Throwable ex) {
                        logger.error(ex);
                    }
                }
            }
        }
    }

    public void setFiles(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        buildFileMap(value);
        this.files = value;
    }

    public String getFolder() {
        return folder;
    }


    public void buildFileFolderMap(String value) {
        String[] paths= value.trim().split(PhDEDHECController.CHAR_COLON);

        for (String path : paths) {
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (isFileExist(file)) {
                    try {
                        //String checksum = getFileChecksum(getMd5Digest(), file);
                        String key = getFileKey(file);
                        getPhDEDHECFileMap().put(key, new PhDEDHECFile(file, this));
                    } catch (Throwable ex) {
                        logger.error(ex);
                    }
                }
            }
        }
    }

    public void setFolder(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        buildFileFolderMap(value);
        this.folder = value;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(Properties prop, String name) {
        String value = prop.getProperty(name);
        getPhDEDHECPropMap().put(name, new PhDEDHECProp(prop, name, prop.getProperty(name)));
        this.process = value;
    }

    public Map<String, PhDEDHECProp> getPhDEDHECPropMap() {
        if (phDEDHECPropMap == null) {
            phDEDHECPropMap = new ConcurrentHashMap<>();
        }
        return phDEDHECPropMap;
    }

    public Map<String, PhDEDHECFile> getPhDEDHECFileMap() {
        if (phDEDHECFileMap == null) {
            phDEDHECFileMap = new ConcurrentHashMap<>();
        }
        return phDEDHECFileMap;
    }

    public PhDEDHECUtils getPhDEDHECUtils() {
        if (phDEDHECUtils == null) {
            phDEDHECUtils = new PhDEDHECUtils();
        }
        return phDEDHECUtils;
    }

    private Map<Long, Map<String, BirthRecordMetadata>> buildBirthRecordMetadata(Properties prop) {
        Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap = new HashMap<>();
        String propStartTagValue = prop.getProperty(PhDEDHECController.PROP_START_TAG);
        String[] tags = propStartTagValue.split(PhDEDHECController.CHAR_COLON);
        for (int index=0; index< tags.length; index++) {
            String tag = tags[index];
            String value = prop.getProperty(tag);
            buildBirthRecordMetadata(birthRecordMetadataMapMap, tag, value);
        }
        return birthRecordMetadataMapMap;
    }

    private void buildBirthRecordMetadata(Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap, String tag, String value) {
        if (StringUtils.isNotBlank(value)) {
            String[] tagValues = value.split(PhDEDHECController.CHAR_SEMI_COLON);
            for (String tagValue: tagValues) {
                if (StringUtils.isNotBlank(tagValue)) {
                    String[] tagValueElements = tagValue.split(PhDEDHECController.CHAR_COLON);
                    buildBirthRecordMetadata(birthRecordMetadataMapMap, tag, tagValueElements);
                }
            }
        }
    }

    private void buildBirthRecordMetadata(Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap, String tag, String[] tagValueElements) {
        if (tagValueElements.length >= 7) {
            String[] yearRange = tagValueElements[0].split(PhDEDHECController.CHAR_MINUS);
            if (yearRange.length == 2 && getPhDEDHECUtils().isValidYearRange(yearRange[0], yearRange[1])) {
                for (long nYear=Long.valueOf(yearRange[0]); nYear<=Long.valueOf(yearRange[1]); nYear++) {
                    Map<String, BirthRecordMetadata> birthRecordMetadataMap = birthRecordMetadataMapMap.get(nYear);
                    if (birthRecordMetadataMap == null) {
                        birthRecordMetadataMap = new HashMap<>();
                    }
                    String name = String.join(PhDEDHECController.CHAR_UNDERSCORE,tag,String.valueOf(nYear));
                    String metaValue = String.join(PhDEDHECController.CHAR_COLON,
                            tagValueElements[1],
                            tagValueElements[2],
                            tagValueElements[3],
                            tagValueElements[4],
                            tagValueElements[5],
                            tagValueElements[6]);
                    BirthRecordMetadata birthRecordMetadata = new BirthRecordMetadata(tag, name, metaValue);
                    birthRecordMetadataMap.put(tag, birthRecordMetadata);
                    birthRecordMetadataMapMap.put(nYear, birthRecordMetadataMap);
                }
            }
        }
    }

    public Map<Long, Map<String, BirthRecordMetadata>> getBirthRecordMetadataMapMap() {
        return birthRecordMetadataMapMap;
    }

    public Map<String, Long> getBirthRecordResultCountMap() {
        if (birthRecordResultCountMap == null) {
            birthRecordResultCountMap = new HashMap<>();
        }
        return birthRecordResultCountMap;
    }

    public Map<String, Map<String, String>> getBirthRecordResultDataMap() {
        if (birthRecordResultDataMap == null) {
            birthRecordResultDataMap = new HashMap<>();
        }
        return birthRecordResultDataMap;
    }

    public Map<String, String> getUsStateCodeNameMap() {
        return usStateCodeNameMap;
    }

    public Map<String, String> getUsStateNameCodeMap() {
        return usStateNameCodeMap;
    }

    public void init(Properties prop) {
        setPosYear(prop, POS_YEAR);
        setPosState(prop, POS_STATE);
        setPosCity(prop, POS_CITY);
        setPosCounty(prop, POS_COUNTY);
        setPosRaceFather(prop, POS_RACE_FATHER);
        setPosRaceMother(prop, POS_RACE_MOTHER);
        setPosHispanicOriginFather(prop, POS_HISPANIC_ORIGIN_FATHER);
        setPosHispanicOriginMother(prop, POS_HISPANIC_ORIGIN_MOTHER);
        setResultRaceChild(prop, RESULT_RACE_CHILD);

        setFormulaRaceCodeName(prop, FORMULA_RACE_CODE_NAME);
        setFormulaHispanicCodeOrigin(prop, FORMULA_HISPANIC_CODE_ORIGIN);
        setFormulaRaceNameIndexName(prop, FORMULA_RACE_NAME_INDEX_NAME);
        setFormulaRaceChildParentsIndexName(prop, FORMULA_RACE_CHILD_PARENTS_INDEX_NAME);

        setResultYear(prop, RESULT_YEAR);
        setResultState(prop, RESULT_STATE);
        setResultCounty(prop, RESULT_COUNTY);
        setResultCity(prop, RESULT_CITY);
        setResultSumChild(prop, RESULT_SUM_CHILD);

        setFiles(prop, FILES);
        setFolder(prop, FOLDER);
        setProcess(prop, PROCESS);
        this.birthRecordMetadataMapMap = buildBirthRecordMetadata(prop);
    }
}
