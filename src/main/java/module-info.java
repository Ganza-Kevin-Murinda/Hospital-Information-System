module com.his.hospitalinfosystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens com.his.hospitalinfosystem to javafx.fxml;
    opens com.his.hospitalinfosystem.controller to javafx.fxml;
    opens com.his.hospitalinfosystem.model to javafx.base;
    opens com.his.hospitalinfosystem.dao to javafx.base;
    opens com.his.hospitalinfosystem.util to javafx.base;

    exports com.his.hospitalinfosystem;
}
