package com.biserv.victorp.research.phdedhec;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.callback.Callback;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PhDEDHECController {
    private static final Logger logger = LogManager.getLogger(PhDEDHECController.class.getName());

    // http://www.java2s.com/Tutorials/Java/JavaFX/0650__JavaFX_TableView.htm

    public static final String CHAR_COLON = ",";
    public static final String CHAR_MINUS = "-";
    public static final String CHAR_SEMI_COLON = ";";
    public static final String CHAR_UNDERSCORE = "_";
    public static final String NAME_BIRTH_RECORDS = "Birth Records";
    public static final String PROP_FILES_TAG = "files";
    public static final String PROP_FOLDER_TAG = "folder";
    public static final String PROP_PROCESS_TAG = "process";
    public static final String PROP_START_TAG = "start";
    public static final String TAG_BIRTH_RECORDS = "birthrecords";

    public static final String VIEW_FILE_PHDEDHEC_PROPERTIES = "phdedhec.properties";

    public static final int MAX_BLOCK_TO_PROCESS = 1000;

    private PhDEDHECApplication application;

    private Properties configProp;

    private long dataCount;

    private long fileCount;

    private ExecutorService executorProcessFile;

    private Object dataCountMonitor;

    private Object fileCountMonitor;

    private Object birthRecordMonitor;

    private PhDEDHECUtils phDEDHECUtils;

    private Slider processFileSlider;

    private ProgressBar progressBar;

    private ProgressIndicator progressIndicator;

    private double sliderMin;

    private double sliderMax;

    private PhDEDHECContext phDEDHECContext;

    @FXML
    private MenuItem configProperty;

    @FXML
    private MenuItem dataProperty;

    @FXML
    private MenuItem export;

    @FXML
    private MenuItem close;

    @FXML
    private MenuItem exit;

    @FXML
    private MenuItem birthRecords;

    @FXML
    private MenuItem about;

    @FXML
    private ListView<String> configPropListView;

    @FXML
    private MenuItem showConfigPropItem;

    @FXML
    private ListView<String> dataPropListView;

    @FXML
    private MenuItem showDataPropItem;

    @FXML
    private TableView resultTableView;

    @FXML
    private ContextMenu resultContextMenu;

    @FXML
    private MenuItem showResultItem;

    @FXML
    private HBox progressHBox;

    @FXML
    void OnActionShowConfigPropItem(ActionEvent event) {
        logger.info("Start  ...");
        ObservableList<String> list = configPropListView.getSelectionModel().getSelectedItems();
        logger.info("End  ...");
    }

    @FXML
    void OnActionShowDataPropItem(ActionEvent event) {
        logger.info("Start  ...");
        ObservableList<String> list = dataPropListView.getSelectionModel().getSelectedItems();
        logger.info("End  ...");
    }

    @FXML
    void aboutProgram(ActionEvent event) {

    }

    @FXML
    void onActionCloseProgram(ActionEvent event) {
        //Clear all list and table
        clearList(getConfigPropListView());
        clearList(getDataPropListView());
        clearList(getResultTableView());
        getResultTableView().refresh();
        setConfigProp(null);
    }

    private void clearList(ListView<String> list) {
        clearList(list.getItems());
        clearList(list.getStylesheets());
        if (list.getSelectionModel() != null) {
            list.getSelectionModel().clearSelection();
        }
    }

    private void clearList(TableView tableView) {
        clearList(tableView.getItems());
        clearList(tableView.getStylesheets());
        clearList(tableView.getColumns());
        clearList(tableView.getStyleClass());
        tableView.refresh();
    }

    private void clearList(javafx.collections.ObservableList<String> list) {
        if (list != null) {
            list.clear();
        }
    }

    @FXML
    void onActionExitProgram(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void onActionExportProgram(ActionEvent event) {

    }

    private Properties readFileProp(File file) {
        logger.info("Start  ...");
        Properties prop = null;
        if (file != null) {
            prop = getPhDEDHECUtils().readFileProp(file);
        }
        logger.info("End  ...");
        return prop;
    }

    private void readFile(File file, ListView<String> listView, PhDEDHECRetentionFileChooser.FilterMode filterMode) {
        logger.info("Start  ...");
        if (file != null) {
            String absolutePath = file.getAbsolutePath();
            logger.info("Read file {}", absolutePath);
            List<String> props = (ArrayList<String>) getPhDEDHECUtils().readFileStrings(file);
            if (props != null && props.size()>0) {
                ObservableList<String> content = FXCollections.observableArrayList(props);
                listView.setItems(content);
            }
        }
        logger.info("End  ...");
    }

    private void readFileWithOpenDialog(Stage primaryStage, ListView<String> listView, String initialFileName, PhDEDHECRetentionFileChooser.FilterMode filterMode) {
        logger.info("Start  ...");
        //Show Open file dialog
        File file = PhDEDHECRetentionFileChooser.showOpenDialog(initialFileName, primaryStage, filterMode);
        readFile(file, listView, filterMode);
        logger.info("End  ...");
    }

    private Properties readFilePropWithOpenDialog(Stage primaryStage, ListView<String> listView, String initialFileName, PhDEDHECRetentionFileChooser.FilterMode filterMode) {
        logger.info("Start  ...");
        //Show Open file dialog
        File file = PhDEDHECRetentionFileChooser.showOpenDialog(initialFileName, primaryStage, filterMode);
        readFile(file, listView, filterMode);
        logger.info("End  ...");
        return readFileProp(file);
    }

    private void readFileWithSaveDialog(Stage primaryStage, ListView<String> listView, String initialFileName, PhDEDHECRetentionFileChooser.FilterMode filterMode) {
        logger.info("Start  ...");
        //Show Open file dialog
        File file = PhDEDHECRetentionFileChooser.showSaveDialog(initialFileName, primaryStage, filterMode);
        readFile(file, listView, filterMode);
        logger.info("End  ...");
    }

    private Properties readFilePropWithSaveDialog(Stage primaryStage, ListView<String> listView, String initialFileName, PhDEDHECRetentionFileChooser.FilterMode filterMode) {
        logger.info("Start  ...");
        //Show Open file dialog
        Properties prop = null;
        File file = PhDEDHECRetentionFileChooser.showSaveDialog(initialFileName, primaryStage, filterMode);
        readFile(file, listView, filterMode);
        logger.info("End  ...");
        return readFileProp(file);
    }

    @FXML
    void onActionLoadConfigProperties(ActionEvent event) {
        logger.info("Start  ... {}", event.hashCode());
        //listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Show file dialog
        Stage primaryStage = getApplication().getPrimaryStage();
        Properties prop = readFilePropWithOpenDialog(primaryStage,
                getConfigPropListView(),
                PhDEDHECRetentionFileChooser.NAME_OPEN_FILE,
                PhDEDHECRetentionFileChooser.FilterMode.PROP_FILES);

        if (prop != null) {
            setConfigProp(prop);
            initContext(prop);
        }
        logger.info("End  ...");
    }

    private void initContext(Properties prop) {
        getPhDEDHECContext().getPhDEDHECConfig().init(prop);
        resetDataCount(0);
        long fileCount = getPhDEDHECContext().getPhDEDHECConfig().getPhDEDHECFileMap().values().size();
        sliderMin = 0;
        sliderMax = fileCount;
        getProcessFileSlider().setMin(0);
        getProcessFileSlider().setMax(sliderMax);
        for (PhDEDHECFile phDEDHECFile : getPhDEDHECContext().getPhDEDHECConfig().getPhDEDHECFileMap().values()) {
            getExecutorProcessFile().submit(new ProcessFileRunnable(this,
                    prop,
                    getPhDEDHECContext().getPhDEDHECConfig(),
                    phDEDHECFile));
        }

        getExecutorProcessFile().shutdown();
        try {
            long dataCount;
            if (!getExecutorProcessFile().awaitTermination(1, TimeUnit.HOURS)) {
                getExecutorProcessFile().shutdownNow();
                dataCount = getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordResultCountMap().keySet().size();
                logger.info("All thread didn't completed in max 1 hour.  Processed "+ fileCount + " file(s). "+ dataCount + " data(s)");
            } else {
                dataCount = getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordResultCountMap().keySet().size();
                logger.info("All thread completed in less than 1 hour.  Processed "+ fileCount + " file(s). "+ dataCount + " data(s)");
                processResult();
            }
        } catch (InterruptedException ex) {
            getExecutorProcessFile().shutdownNow();
            Thread.currentThread().interrupt();
            dataCount = getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordResultCountMap().keySet().size();
            logger.info("All thread didn't completed in max 1 hour. Exception occurred: "+ ex.getMessage() +".  Processed "+ fileCount + " file(s). "+ dataCount + " data(s)");
        }
    }

    public int processFile(PhDEDHECFile phDEDHECFile, PhDEDHECConfig phDEDHECConfig) {
        File file = phDEDHECFile.getFile();
        int count = 0;
        if (file.isFile()) {
            logger.info("Start Process: " + file.getName());
            Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap = phDEDHECConfig.getBirthRecordMetadataMapMap();

            BufferedReader bufferedReader = null;
            Map<String, BirthRecordMetadata> birthRecordMetadataMap = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));

                String line;
                Map<String, Object> rowItem = new HashMap<>();
                BirthRecord birthRecord = new BirthRecord();
                Long key = null;
                Long prevKey = null;

                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    birthRecord.setValue(line);
                    if (birthRecord.getValue().length() > 4 && getPhDEDHECUtils().isValidYear(birthRecord.getField(1, 4))) {
                        key = Long.valueOf(birthRecord.getField(1, 4));
                        if (key == null || !String.valueOf(key).equalsIgnoreCase(String.valueOf(prevKey))) {
                            birthRecordMetadataMap = birthRecordMetadataMapMap.get(key);
                            if (birthRecordMetadataMap == null) {
                                logger.info("Missing data in configuration for Year "+String.valueOf(key)+" in file: "+file.getAbsolutePath());
                                continue;
                            }
                        }

                        prevKey = key;
                        birthRecord.setBirthRecordMetadataMap(birthRecordMetadataMap);
                        count++;
                        addDataCount(1);
                        birthRecord.buildDataMap(PhDEDHECConfig.TAG_LIST);
                        addBirthRecord(phDEDHECConfig, birthRecord);

                    /*
                    for (BirthRecordMetadata birthRecordMetadata : birthRecordMetadataMap) {
                        rowItem.put(birthRecordMetadata.getTag(), birthRecordMetadata.getValue(line, birthRecordMetadataMap, 0));
                    }*/
                    } else {
                        continue;
                    }
                }
                logger.info(file.getName() + " has " + count + " data read");
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
            logger.info("End Process: " + file.getName());
        }
        return count;
    }

    @FXML
    void onActionLoadDataProperties(ActionEvent event) {
        logger.info("Start  ... {}", event.hashCode());
        //listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Show file dialog
        Stage primaryStage = getApplication().getPrimaryStage();
        readFileWithOpenDialog(primaryStage,
                getDataPropListView(),
                null,
                PhDEDHECRetentionFileChooser.FilterMode.TXT_FILES);
        logger.info("End  ...");
    }

    @FXML
    void onActionShowResultItem(ActionEvent event) {

    }

    @FXML
    void onEditCancelConfigPropListView(ActionEvent event) {

    }

    @FXML
    void onEditCancelDataPropListView(ActionEvent event) {

    }

    @FXML
    void onEditCommitConfigPropListView(ActionEvent event) {

    }

    @FXML
    void onEditCommitDataPropListView(ActionEvent event) {

    }

    @FXML
    void onEditStartConfigPropListView(ActionEvent event) {

    }

    @FXML
    void onEditStartDataPropListView(ActionEvent event) {

    }

    private PhDEDHECUtils getPhDEDHECUtils() {
        if (phDEDHECUtils == null) {
            phDEDHECUtils = new PhDEDHECUtils();
        }
        return phDEDHECUtils;
    }

    private TableColumn<Map, String> makeColumnWithMap(String colMapKey, String displayName, double width, TableView tableView) {
        TableColumn<Map, String> column = new TableColumn<>(displayName);

        column.prefWidthProperty().bind(tableView.widthProperty().multiply(width));
        column.setResizable(false);
        column.setCellValueFactory(new MapValueFactory<>(colMapKey));
        return column;
    }

    private Map<String, Object> createItemMap(String data, List<BirthRecordMetadata> birthRecordMetadataList, TableView tableView) {
        Map<String, Object> item = new HashMap<>();

        for (BirthRecordMetadata birthRecordMetadata : birthRecordMetadataList) {
            item.put(birthRecordMetadata.getTag(), birthRecordMetadata.getValue(data, birthRecordMetadataList, 0));
        }
        return item;
    }

    private void buildBirthRecords(Properties prop,  ListView<String> listView, TableView tableView) {
        Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap = getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordMetadataMapMap();

        //Build TableColumn
        //buildTableColumn( birthRecordMetadataMap, tableView, listView.getItems().size());

        //Build TableColumn Items
        //BuildTableColumnItems(birthRecordMetadataMap, listView.getItems(), tableView);
    }

    private void parseBirthRecords(Properties prop,  ListView<String> listView, TableView<BirthRecord> tableView) {
        tableView.getItems().clear();
        if (!listView.getItems().isEmpty() && prop.containsKey(PROP_START_TAG)) {
            buildBirthRecords(prop,  listView, tableView);
        }
    }

    @FXML
    void parseBirthRecords(ActionEvent event) {
        //Parse item into table view
        parseBirthRecords( getConfigProp(),  getDataPropListView(), getResultTableView());
    }

    public HBox getProgressHBox() {
        return progressHBox;
    }

    public void initProgressHBox() {
        getProgressHBox().setSpacing(5);
        getProgressHBox().setAlignment(Pos.CENTER);
        getProgressHBox().getChildren().addAll(getProcessFileSlider(), getProgressBar(), getProgressIndicator());
    }

    public PhDEDHECApplication getApplication() { return application; }

    public void setApplication(PhDEDHECApplication application) { this.application = application; }

    public ListView<String> getConfigPropListView() { return configPropListView; }

    public ListView<String> getDataPropListView() { return dataPropListView; }

    public TableView getResultTableView() {  return resultTableView; }

    public Properties getConfigProp() {
        return configProp;
    }

    public void setConfigProp(Properties configProp) {
        this.configProp = configProp;
    }

    public ExecutorService getExecutorProcessFile() {
        if (executorProcessFile == null) {
            executorProcessFile = Executors.newFixedThreadPool(10);
        }
        return executorProcessFile;
    }

    public Object getDataCountMonitor() {
        if (dataCountMonitor == null) {
            dataCountMonitor = new Object();
        }
        return dataCountMonitor;
    }

    public long getDataCount() {
        long result;
        synchronized (getDataCountMonitor()) {
            result = dataCount;
            getDataCountMonitor().notifyAll();
        }
        return result;
    }

    public void setDataCount(long dataCount) {
        synchronized (getDataCountMonitor()) {
            this.dataCount = dataCount;
            getDataCountMonitor().notifyAll();
        }
    }

    public long addDataCount(long dataCount) {
        long result;
        synchronized (getDataCountMonitor()) {
            this.dataCount += dataCount;
            result = this.dataCount;
            getDataCountMonitor().notifyAll();
        }
        return result;
    }

    public long subDataCount(long dataCount) {
        long result;
        synchronized (getDataCountMonitor()) {
            this.dataCount -= dataCount;
            result = this.dataCount;
            getDataCountMonitor().notifyAll();
        }
        return result;
    }

    public Object getBirthRecordMonitor() {
        if (birthRecordMonitor == null) {
            birthRecordMonitor = new Object();
        }
        return birthRecordMonitor;
    }

    public void addBirthRecord(PhDEDHECConfig phDEDHECConfig, BirthRecord birthRecord) {
        synchronized (getBirthRecordMonitor()) {
            if (birthRecord != null) {
                Map<String, Long> birthRecordResultCountMap = phDEDHECConfig.getBirthRecordResultCountMap();
                Map<String, Map<String, String>> birthRecordResultDataMap = phDEDHECConfig.getBirthRecordResultDataMap();
                String keyValues = String.join(PhDEDHECController.CHAR_UNDERSCORE, birthRecord.getDatas()).toLowerCase();
                if (birthRecordResultCountMap.containsKey(keyValues)) {
                    Long count = birthRecordResultCountMap.get(keyValues);
                    count++;
                    birthRecordResultCountMap.put(keyValues, count);
                } else {
                    birthRecordResultCountMap.put(keyValues, Long.valueOf(1));
                    Map<String, String> map = new HashMap<>();

                    Iterator<Map.Entry<String, String>> hmIterator = birthRecord.getDataMap().entrySet().iterator();
                    while (hmIterator.hasNext()) {

                        Map.Entry<String, String> mapElement = hmIterator.next();
                        map.put(mapElement.getKey(), mapElement.getValue());
                    }
                    birthRecordResultDataMap.put(keyValues, map);
                }

            }
            getBirthRecordMonitor().notifyAll();
        }
    }

    public void resetDataCount(long dataCount) {
        setDataCount(0);
    }

    public Object getFileCountMonitor() {
        if (fileCountMonitor == null) {
            fileCountMonitor = new Object();
        }
        return fileCountMonitor;
    }

    public long getFileCount() {
        long result;
        synchronized (getFileCountMonitor()) {
            result = fileCount;
            getFileCountMonitor().notifyAll();
        }
        return result;
    }

    public void setFileCount(long fileCount) {
        synchronized (getFileCountMonitor()) {
            this.fileCount = fileCount;
            getFileCountMonitor().notifyAll();
        }
    }

    public long addFileCount(long fileCount) {
        long result;
        synchronized (getFileCountMonitor()) {
            this.fileCount += fileCount;
            result = this.fileCount;
            getFileCountMonitor().notifyAll();
        }
        return result;
    }

    public long subFileCount(long fileCount) {
        long result;
        synchronized (getFileCountMonitor()) {
            this.fileCount -= fileCount;
            result = this.fileCount;
            getFileCountMonitor().notifyAll();
        }
        return result;
    }

    public void resetFileCount(long fileCount) {
        setFileCount(0);
    }

    public Slider getProcessFileSlider() {
        if (processFileSlider == null) {
            processFileSlider = new Slider(0, 1, 0);
            sliderMin = 0;
            sliderMax = 1;
            //Setting the size of the progress bar
            getProgressBar().setPrefSize(100, 30);
            //Creating a slider
            processFileSlider.setShowTickLabels(true);
            processFileSlider.setShowTickMarks(true);
            processFileSlider.setMajorTickUnit(0.1);
            processFileSlider.setBlockIncrement(0.1);
            processFileSlider.valueProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov,
                                    Number old_val, Number new_val) {
                    //getProgressBar().setProgress(new_val.doubleValue()/sliderMax);
                    //getProgressIndicator().setProgress(new_val.doubleValue()/sliderMax);

                    //Setting the angle for the rotation
                    getProgressBar().setProgress((double) new_val);
                    //Setting value to the indicator
                    getProgressIndicator().setProgress((double) new_val);
                }
            });
        }
        return processFileSlider;
    }

    public ProgressBar getProgressBar() {
        if (progressBar == null) {
            progressBar = new ProgressBar(0.0);
        }
        return progressBar;
    }

    public ProgressIndicator getProgressIndicator() {
        if (progressIndicator == null) {
            progressIndicator = new ProgressIndicator(0.0);
        }
        return progressIndicator;
    }

    public PhDEDHECContext getPhDEDHECContext() {
        if (this.phDEDHECContext == null) {
            this.phDEDHECContext = new PhDEDHECContext(this);
        }
        return phDEDHECContext;
    }

    //PropertyValueFactory

    private void buildTableColumn2( Map<Long, BirthRecordMetadata> birthRecordMetadataMap, TableView tableView, int count) {
        List<BirthRecordMetadata> birthRecordMetadataList = new ArrayList(birthRecordMetadataMap.values());

        TableColumn<Map, String> birthRecordTableColumn   = new TableColumn<>(NAME_BIRTH_RECORDS + " ( "+count+" )");
        birthRecordTableColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0));
        birthRecordTableColumn.setResizable(false);
        birthRecordTableColumn.setCellValueFactory(new PropertyValueFactory<>(TAG_BIRTH_RECORDS));
        double widthFix = 1.0*1/birthRecordMetadataList.size();
        double sumWidth = 0.0;
        for (int index=0; index<birthRecordMetadataList.size(); index++) {
            BirthRecordMetadata birthRecordMetadata = birthRecordMetadataList.get(index);
            sumWidth += widthFix;
            double width = (index<birthRecordMetadataList.size()-1)?widthFix:(1.0-(sumWidth-widthFix));
            TableColumn<Map, String> tableColumn = makeColumnWithMap(birthRecordMetadata.getTag(),birthRecordMetadata.getDisplayName(), width, tableView);
            if (birthRecordMetadata.getSortType().equals(BirthRecordMetadata.SORT_TYPE_PROP_DESC)) {
                tableColumn.setSortType(TableColumn.SortType.DESCENDING);
            } else {
                tableColumn.setSortType(TableColumn.SortType.ASCENDING);
            }

            if (birthRecordMetadata.getDataAlignment().equals(BirthRecordMetadata.ALIGN_CENTER)) {
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_CENTER);
            } else if (birthRecordMetadata.getDataAlignment().equals(BirthRecordMetadata.ALIGN_JUSTIFY)) {
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_JUSTIFY);
            } else if (birthRecordMetadata.getDataAlignment().equals(BirthRecordMetadata.ALIGN_RIGHT)) {
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_RIGHT);
            } else {
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_LEFT);
            }

            birthRecordTableColumn.getColumns().add(tableColumn);
        }
        tableView.getColumns().addAll(birthRecordTableColumn);

        getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordResultDataMap();
    }

    //MAX_BLOCK_TO_PROCESS
    private void BuildTableColumnItems2(Map<Long, BirthRecordMetadata> birthRecordMetadataMap, ObservableList<String> list, TableView tableView) {
        List<BirthRecordMetadata> birthRecordMetadataList = new ArrayList(birthRecordMetadataMap.values());
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        for (int index = 0; index<=list.size()-1; index++) {
            String data =  list.get(index);
            if (!data.trim().isEmpty()) {
                items.add(createItemMap(data, birthRecordMetadataList, tableView));
            }
        }

        tableView.getItems().addAll(items);
    }

    private Map<String, BirthRecordMetadata> retrieveBirthRecordMetadata(Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap, Map<String, Long> birthRecordResultCountMap) {
        for (Map.Entry<String, Long> entryBirthRecordResultCountMapp : birthRecordResultCountMap.entrySet()) {
            String keyMap = entryBirthRecordResultCountMapp.getKey();
            Long value = entryBirthRecordResultCountMapp.getValue();

            String[] tagList = keyMap.trim().split(PhDEDHECController.CHAR_UNDERSCORE);
            if (tagList.length > 0) {
                String tag = tagList[0];
                if (getPhDEDHECUtils().isNumeric(tag)) {
                    Long keyMap2 = Long.valueOf(tag);
                    if (birthRecordMetadataMapMap.containsKey(keyMap2)) {
                        Map<String, BirthRecordMetadata> birthRecordMetadataMap = birthRecordMetadataMapMap.get(keyMap2);
                        if (birthRecordMetadataMap != null) {
                            boolean valid = true;
                            for (int index=0; index<PhDEDHECConfig.TAG_LIST.length; index++) {
                                if (!birthRecordMetadataMap.containsKey(PhDEDHECConfig.TAG_LIST[index])) {
                                    valid = false;
                                    break;
                                }
                            }
                            if (valid) {
                                return birthRecordMetadataMap;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private TableColumn makeHeaderWrappable(TableColumn col) {
        String style = col.getStyle();
        Label label = new Label(col.getText());
        label.setStyle("-fx-padding: 8px;"+style);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        //label.setTextAlignment(TextAlignment.CENTER);

        StackPane stack = new StackPane();
        stack.getChildren().add(label);
        stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
        label.prefWidthProperty().bind(stack.prefWidthProperty());
        col.setText(null);
        col.setGraphic(stack);
        return col;
    }

    private void buildTableColumn ( Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap, Map<String, Long> birthRecordResultCountMap, TableView tableView) {
        TableColumn<Map, String> birthRecordTableColumn   = new TableColumn<>(NAME_BIRTH_RECORDS + " ( "+PhDEDHECConfig.TAG_DISPLAY_NAME_LIST.length+" )");
        birthRecordTableColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0));
        birthRecordTableColumn.setResizable(false);
        birthRecordTableColumn.setCellValueFactory(new PropertyValueFactory<>(TAG_BIRTH_RECORDS));
        double widthFix = 1.0*1/(PhDEDHECConfig.TAG_DISPLAY_NAME_LIST.length+1);
        double sumWidth = 0.0;
        Map<String, BirthRecordMetadata> birthRecordMetadataMap = retrieveBirthRecordMetadata(birthRecordMetadataMapMap, birthRecordResultCountMap);

        if (birthRecordMetadataMap == null) {
            return;
        }

        for (int index=0; index<PhDEDHECConfig.TAG_LIST.length; index++) {
            String tag = PhDEDHECConfig.TAG_LIST[index];
            String displayName = PhDEDHECConfig.TAG_DISPLAY_NAME_LIST[index];
            sumWidth += widthFix;
            double width = (index<PhDEDHECConfig.TAG_LIST.length)?widthFix:(1.0-(sumWidth-widthFix));
            BirthRecordMetadata birthRecordMetadata = birthRecordMetadataMap.get(tag);

            TableColumn<Map, String> tableColumn = makeColumnWithMap(tag,displayName, width, tableView);

            if (birthRecordMetadata.getSortType().equals(BirthRecordMetadata.SORT_TYPE_PROP_DESC)) {
                tableColumn.setSortType(TableColumn.SortType.DESCENDING);
            } else {
                tableColumn.setSortType(TableColumn.SortType.ASCENDING);
            }

            if (birthRecordMetadata.getDataAlignment().equals(BirthRecordMetadata.ALIGN_CENTER)) {
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_CENTER);
            } else if (birthRecordMetadata.getDataAlignment().equals(BirthRecordMetadata.ALIGN_JUSTIFY)) {
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_JUSTIFY);
            } else if (birthRecordMetadata.getDataAlignment().equals(BirthRecordMetadata.ALIGN_RIGHT)) {
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_RIGHT);
            } else {
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_LEFT);
            }
            birthRecordTableColumn.getColumns().add(makeHeaderWrappable(tableColumn));
            if (index == PhDEDHECConfig.TAG_LIST.length-1) {
                tableColumn = makeColumnWithMap(PhDEDHECConfig.POS_COUNT,PhDEDHECConfig.POS_COUNT_DISPLAY_NAME, width, tableView);
                tableColumn.setSortType(TableColumn.SortType.ASCENDING);
                tableColumn.setStyle(BirthRecordMetadata.STYLE_TEXT_FORMAT_CENTER);

                birthRecordTableColumn.getColumns().add(makeHeaderWrappable(tableColumn));
            }
        }
        tableView.getColumns().addAll(birthRecordTableColumn);
    }

    //MAX_BLOCK_TO_PROCESS
    private void BuildTableColumnItems(Map<Long, Map<String, BirthRecordMetadata>> birthRecordMetadataMapMap, Map<String, Long> birthRecordResultCountMap, TableView tableView) {
        ObservableList<Map> allData = FXCollections.observableArrayList();

        for (Map.Entry<String, Long> entry : birthRecordResultCountMap.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            String[] list = key.split(PhDEDHECController.CHAR_UNDERSCORE);
            Map<String, String> map = new HashMap<>();

            if (list.length < PhDEDHECConfig.TAG_LIST.length) {
                continue;
            }

            for (int index=0; index<PhDEDHECConfig.TAG_LIST.length; index++) {
                String keyMap      = PhDEDHECConfig.TAG_LIST[index];
                String valueMap    = list[index];
                map.put(keyMap, valueMap);
            }
            map.put(PhDEDHECConfig.POS_COUNT, String.valueOf(value));
            //TODO compute race of child

            allData.add(map);
        }

        tableView.setItems(allData);
    }

    private void processResult() {
        //Build TableColumn

        buildTableColumn( getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordMetadataMapMap(), getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordResultCountMap(), getResultTableView());

        //Build TableColumn Items
        BuildTableColumnItems(getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordMetadataMapMap(), getPhDEDHECContext().getPhDEDHECConfig().getBirthRecordResultCountMap(), getResultTableView());
    }
}