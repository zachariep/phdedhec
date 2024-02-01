package com.biserv.victorp.research.phdedhec;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PhDEDHECFile {
    private static final Logger logger = LogManager.getLogger(PhDEDHECFile.class.getName());
    private File file;
    private PhDEDHECConfig phDEDHECConfig;

    private long fileSize;
    private long recordSize;
    private long numberOfRecord;


    public PhDEDHECFile(File file, PhDEDHECConfig phDEDHECConfig) {
        this.file = file;
        this.phDEDHECConfig = phDEDHECConfig;
        this.fileSize = 0;
        this.recordSize = 0;
        this.numberOfRecord = 0;
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));

            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                } else {
                    break;
                }
            }
            if (line != null) {
                this.recordSize = line.length();
                this.fileSize = file.length();
                this.numberOfRecord = this.fileSize / this.recordSize;
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
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getRecordSize() {
        return recordSize;
    }

    public long getNumberOfRecord() {
        return numberOfRecord;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public PhDEDHECConfig getPhDEDHECConfig() {
        return phDEDHECConfig;
    }

    public void setPhDEDHECConfig(PhDEDHECConfig phDEDHECConfig) {
        this.phDEDHECConfig = phDEDHECConfig;
    }
}
