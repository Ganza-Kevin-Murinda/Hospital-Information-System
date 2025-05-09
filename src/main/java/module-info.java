module com.his.hospitalinfosystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens com.his.hospitalinfosystem to javafx.fxml;
    exports com.his.hospitalinfosystem;
}