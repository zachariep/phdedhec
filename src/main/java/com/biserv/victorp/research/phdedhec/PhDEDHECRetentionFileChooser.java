package com.biserv.victorp.research.phdedhec;

import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PhDEDHECRetentionFileChooser {
    public static final String NAME_OPEN_FILE = "Open File";

    private static FileChooser instance = null;
    private static final SimpleObjectProperty<File> lastKnownDirectoryProperty = new SimpleObjectProperty<>();

    private PhDEDHECRetentionFileChooser(){ }

    public enum FilterMode {
        //Setup supported filters
        PROP_FILES("prop files (*.properties)", "*.properties"),
        TXT_FILES("txt files (*.txt)", "*.txt");

        private final FileChooser.ExtensionFilter extensionFilter;

        FilterMode(String extensionDisplayName, String... extensions){
            extensionFilter = new FileChooser.ExtensionFilter(extensionDisplayName, extensions);
        }

        public FileChooser.ExtensionFilter getExtensionFilter(){
            return extensionFilter;
        }
    }

    private static FileChooser getInstance(FilterMode... filterModes){
        if(instance == null) {
            instance = new FileChooser();
            instance.setTitle(NAME_OPEN_FILE);
            instance.initialDirectoryProperty().bindBidirectional(lastKnownDirectoryProperty);
        }
        //Set the filters to those provided
        //You could add check's to ensure that a default filter is included, adding it if need be
        instance.getExtensionFilters().setAll(
                Arrays.stream(filterModes)
                        .map(FilterMode::getExtensionFilter)
                        .collect(Collectors.toList()));
        return instance;
    }

    public static File showOpenDialog(String initialFileName, FilterMode... filterModes){
        return showOpenDialog(initialFileName, null, filterModes);
    }

    public static File showOpenDialog(String initialFileName, Window ownerWindow, FilterMode...filterModes){
        File chosenFile;
        FileChooser fileChooser = getInstance(filterModes);
        if (initialFileName !=null && !initialFileName.trim().isEmpty()) {
            fileChooser.setInitialFileName(initialFileName);
        }
        chosenFile = fileChooser.showOpenDialog(ownerWindow);
        if(chosenFile != null){
            lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }

    public static File showSaveDialog(String initialFileName, FilterMode... filterModes){
        return showSaveDialog(initialFileName, null, filterModes);
    }

    public static File showSaveDialog(String initialFileName, Window ownerWindow, FilterMode... filterModes){
        File chosenFile;
        FileChooser fileChooser = getInstance(filterModes);
        if (initialFileName !=null && !initialFileName.trim().isEmpty()) {
            fileChooser.setInitialFileName(initialFileName);
        }
        chosenFile = fileChooser.showSaveDialog(ownerWindow);
        if(chosenFile != null){
            lastKnownDirectoryProperty.setValue(chosenFile.getParentFile());
        }
        return chosenFile;
    }
}
