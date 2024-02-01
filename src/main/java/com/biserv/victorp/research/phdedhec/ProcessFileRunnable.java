package com.biserv.victorp.research.phdedhec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class ProcessFileRunnable implements Runnable {
    private static final Logger logger = LogManager.getLogger(ProcessFileRunnable.class.getName());

    private Properties prop;
    private PhDEDHECFile phDEDHECFile;
    private PhDEDHECController phDEDHECController;
    private PhDEDHECConfig phDEDHECConfig;

    public ProcessFileRunnable(PhDEDHECController phDEDHECController,
                               Properties prop,
                               PhDEDHECConfig phDEDHECConfig,
                               PhDEDHECFile phDEDHECFile) {
        this.phDEDHECController = phDEDHECController;
        this.prop = prop;
        this.phDEDHECConfig = phDEDHECConfig;
        this.phDEDHECFile = phDEDHECFile;
    }

    @Override
    public void run() {
        logger.info("Start Thread: " + Thread.currentThread().getName() + ". File: "+ phDEDHECFile.getFile().getAbsolutePath());
        long fileCount = phDEDHECController.getFileCount();
        int count = phDEDHECController.processFile(phDEDHECFile, this.phDEDHECConfig);
        long dataCount = phDEDHECController.getDataCount();
        fileCount = phDEDHECController.addFileCount(1);
        phDEDHECController.getProcessFileSlider().setValue(fileCount);
        logger.info("End Thread: " + Thread.currentThread().getName()+ ". Total file count= " + fileCount + ". Total data count= " + dataCount + ". This file has "+ count + " data read");
    }
}
