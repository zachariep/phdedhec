package com.biserv.victorp.research.phdedhec;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhDEDHECUtils {
    private static final Logger logger = LogManager.getLogger(PhDEDHECUtils.class.getName());

    public String readFileString(File file){
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text);
            }
        } catch (FileNotFoundException ex) {
            logger.error("The Following file cannot be found :"+file.getAbsolutePath(), System.Logger.Level.ERROR, ex);
        } catch (IOException ex) {
            logger.error("The Following file cannot be access :"+file.getAbsolutePath(), System.Logger.Level.ERROR, ex);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                logger.error(System.Logger.Level.ERROR, ex);
            }
        }

        return stringBuffer.toString();
    }

    public List<String> readFileStrings(File file){
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;
        List<String> list = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                list.add(text);
            }
        } catch (FileNotFoundException ex) {
            logger.error("The Following file cannot be found :"+file.getAbsolutePath(), System.Logger.Level.ERROR, ex);
        } catch (IOException ex) {
            logger.error("The Following file cannot be access :"+file.getAbsolutePath(), System.Logger.Level.ERROR, ex);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                logger.error(System.Logger.Level.ERROR, ex);
            }
        }

        return list;
    }

    public Properties readFileProp(File file) {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try  {
            inputStream = new FileInputStream(file);

            // load a properties file
            prop.load(inputStream);

        } catch (FileNotFoundException ex) {
            logger.error("The Following file cannot be found :"+file.getAbsolutePath(), System.Logger.Level.ERROR, ex);
        } catch (IOException ex) {
            logger.error("The Following file cannot be access :"+file.getAbsolutePath(), System.Logger.Level.ERROR, ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                logger.error(System.Logger.Level.ERROR, ex);
            }
        }

        return prop;
    }

    public boolean isValidYear(String year) {
        if (isNumeric(year))  {
            try {
                double minVal = Double.parseDouble(year);
                return (minVal > 1900 && minVal < 2100);
            } catch (NumberFormatException nfe) {
            }
        }

        return false;
    }

    public boolean isValidYearRange(String yearMin, String yearMax) {
        if (isNumeric(yearMin) && isNumeric(yearMax) && isValidYear(yearMin) && isValidYear(yearMax)) {
            try {
                double minVal = Double.parseDouble(yearMin);
                double maxVal = Double.parseDouble(yearMax);
                return (maxVal >= minVal);
            } catch (NumberFormatException nfe) {
            }
        }

        return false;
    }

    public boolean isNumeric(String strNum) {
        if (StringUtils.isBlank(strNum)) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean isFileExist(String fileNameOrFolder) {
        if (StringUtils.isBlank(fileNameOrFolder)) {
            return false;
        }
        boolean exist = new File(fileNameOrFolder).exists();
        return exist;
    }

    public boolean isFileExist(String[] fileNameOrFolders) {
        if (fileNameOrFolders == null || fileNameOrFolders.length ==0) {
            return false;
        }
        
        for (String fileNameOrFolder : fileNameOrFolders) {
            if (isFileExist(fileNameOrFolder)) {
                return true;
            }
        }
        return false;
    }

    public long countExistingFiles(String[] fileNames) {
        long count = 0;

        if (fileNames != null && fileNames.length>0) {
            for (String fileName : fileNames) {
                File file = new File(fileName);
                if (file.isFile()) {
                    count++;
                }
            }
        }

        return count;
    }

    public long countExistingFilesInDirectory(String[] paths) {
        long count = 0;

        for (String path : paths) {
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    count++;
                }
            }
        }

        return count;
    }
}
