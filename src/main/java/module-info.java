module com.biserv.victorp.research.phdedhec {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;

    opens com.biserv.victorp.research.phdedhec to javafx.fxml;
    exports com.biserv.victorp.research.phdedhec;
}