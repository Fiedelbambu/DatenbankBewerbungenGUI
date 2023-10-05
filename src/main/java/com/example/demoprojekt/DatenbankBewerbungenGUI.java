package com.example.demoprojekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;



public class DatenbankBewerbungenGUI implements Initializable {


    @FXML
    public MenuBar MenuBar_Bewerbungen;
    @FXML
    public MenuItem MenuItem_close;
    @FXML
    public MenuItem menuItem_lehstellenborse;
    @FXML
    public Button button_abgesagt;
    @FXML
    public Button button_datenEintragen;
    @FXML
    public Button button_loeschen;

    @FXML
    public TextField textField_name;
    @FXML
    public TextField textField_Datum;
    @FXML
    public TextField textField_adresse;
    @FXML
    public TableView<ModelTabelleDaten> TableView_datenTabelle;
    @FXML
    public TableColumn<ModelTabelleDaten, String> tableCloumn_name;
    @FXML
    public TableColumn<ModelTabelleDaten, String> tableCloumn_datum;
    @FXML
    public TableColumn<ModelTabelleDaten, String> tableCloumn_adresse;
    @FXML
    public TableColumn<ModelTabelleDaten, String> tableCloumn_abgesagt;

    //////////////////////////////////////////////////////////////////////////

    public ResultSet resultSet;

    ObservableList<ModelTabelleDaten> observableList = FXCollections.observableArrayList();

    public static String  name , datum, adresse ;





//////////////////////////////////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        /////////////////////////String setzen /////////////////////////////////////////////////////////


        textField_name.setText(String.valueOf(name));
        textField_Datum.setText(String.valueOf(datum));
        textField_adresse.setText(String.valueOf(adresse));

/////////////////////////////////Button speichern////////////////////////////////////////////


        button_datenEintragen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Connection conn = null;
                PreparedStatement pr = null;

                try {
                    String name = textField_name.getText();
                    String inputDate = textField_Datum.getText();
                    String adresse = textField_adresse.getText();

                    // Parse the input date and format it to 'YYYY-MM-DD' format
                    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    java.util.Date parsedDate = inputDateFormat.parse(inputDate);
                    String formattedDate = mysqlDateFormat.format(parsedDate);

                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bewerbung", "ubuntu", "ubuntu");
                    String insertQuery = "INSERT INTO firmen (name, datum, adresse) VALUES (?, ?, ?)";
                    pr = conn.prepareStatement(insertQuery);

                    pr.setString(1, name);
                    pr.setString(2, formattedDate);
                    pr.setString(3, adresse);

                    pr.executeUpdate();

                    // Show a success alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Erfolg");
                    alert.setHeaderText(null);
                    alert.setContentText("Daten erfolgreich hinzugefügt.");
                    alert.showAndWait();

                    // Rufen Sie die Methode lesenTabelle auf, um die TableView zu aktualisieren
                    lesenTabelle(TableView_datenTabelle);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                } finally {
                    // Schließen Sie nur PreparedStatement
                    if (pr != null) {
                        try {
                            pr.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    // Sie können ResultSet und Connection offen lassen
                }
            }
        });

        ///////////////////////////////////////////MenuItemClose/////////////////////////////////////////////

        MenuItem_close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
                try {

                    Parent root = loader.load();
                    Stage stage = (Stage) MenuBar_Bewerbungen.getScene().getWindow();
                    stage.setScene(new Scene(root,880,735));
                    stage.setTitle("Login");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /////////////////////MenuItem_lehstellenbörse////////////////////////////7

//        menuItem_lehstellenborse.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//
//                FXMLLoader loader = new FXMLLoader(WebController.class.getResource(""));
//                try {
//
//                    Parent root = loader.load();
//                    Stage stage = (Stage) MenuBar_Bewerbungen.getScene().getWindow();
//                    stage.setScene(new Scene(root,675,510));
//                    stage.setTitle("WebView");
//                    stage.show();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });






        //////////////////Button löschen //////////////////////////////////////
        button_loeschen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Establish a database connection here
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bewerbung", "ubuntu", "ubuntu");

                    int selectedRow = TableView_datenTabelle.getSelectionModel().getSelectedIndex();
                    if (selectedRow != -1) {
                        int columnCount = TableView_datenTabelle.getColumns().size();
                        Object[] rowData = new Object[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            rowData[i] = TableView_datenTabelle.getColumns().get(i).getCellData(selectedRow);
                        }

                        // Create the DELETE query
                        String deleteQuery = "DELETE FROM firmen WHERE name = ? AND datum = ? AND adresse = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, rowData[0].toString());

                        // Convert java.util.Date to java.sql.Date
                        java.util.Date utilDate = (java.util.Date) rowData[1];
                        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                        deleteStatement.setDate(2, sqlDate);

                        deleteStatement.setString(3, rowData[2].toString());
                        deleteStatement.executeUpdate();

                        // Close the PreparedStatement
                        deleteStatement.close();

                        // Close the database connection
                        connection.close();

                        // Refresh the TableView after deletion
                        lesenTabelle(TableView_datenTabelle);
                        aktualisiereTabelle();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });



        ///////////////////////////Button abgesagt////////////////////////////////////////////////////////////7


        button_abgesagt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Connection connection = null; // Initialize the connection here
                PreparedStatement preparedStatement = null;

                int selectedRow = TableView_datenTabelle.getSelectionModel().getSelectedIndex();
                if (selectedRow != -1) {
                    String name = TableView_datenTabelle.getItems().get(selectedRow).getName();

                    try {
                        // Initialize the connection and preparedStatement here
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bewerbung", "ubuntu", "ubuntu");
                        String updateQuery = "UPDATE firmen SET abgesagt = 'ja' WHERE name = ?";
                        preparedStatement = connection.prepareStatement(updateQuery);

                        preparedStatement.setString(1, name);
                        preparedStatement.executeUpdate();

                        // Tabelle aktualisieren
                        lesenTabelle(TableView_datenTabelle);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } finally {
                        // Close the preparedStatement and connection in the finally block
                        try {
                            if (preparedStatement != null) {
                                preparedStatement.close();
                            }
//                            if (connection != null) {
//                                connection.close();
//                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });




////////////////////Daten Lesen und in Tabelle setzen//////////////////////////////

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            try {
                connection = conn.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (connection != null) {
                System.out.println("Database connection established.");
                try {
                    // Initialize the preparedStatement here
                    preparedStatement = connection.prepareStatement("SELECT * FROM firmen");
                    // Execute the query
                    resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        observableList.add(new ModelTabelleDaten(
                                resultSet.getString("name"),
                                resultSet.getString("datum"),
                                resultSet.getString("adresse"),
                                resultSet.getString("abgesagt")
                        ));
                    }

                    tableCloumn_name.setCellValueFactory(new PropertyValueFactory<>("name"));
                    tableCloumn_datum.setCellValueFactory(new PropertyValueFactory<>("datum"));
                    tableCloumn_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
                    tableCloumn_abgesagt.setCellValueFactory(new PropertyValueFactory<>("abgesagt"));

                    TableView_datenTabelle.setItems(observableList);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Close the preparedStatement and connection in the finally block
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
//                    if (connection != null) {
//                        connection.close();
//                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
//////////////////////////////Tabelle aktuallisieren//////////////////////////////////////
    public void aktualisiereTabelle() {
        try {
            String query = "SELECT * FROM firmen";
            ObservableList<ModelTabelleDaten> data = FXCollections.observableArrayList();

            // Öffnen Sie eine neue Verbindung zur Datenbank
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bewerbung", "ubuntu", "ubuntu");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String datum = resultSet.getString("datum");
                String adresse = resultSet.getString("adresse");
                String abgesagt = resultSet.getString("abgesagt");

                ModelTabelleDaten entry = new ModelTabelleDaten(name, datum, adresse, abgesagt);
                data.add(entry);
            }

            // Daten in TableView setzen
            TableView_datenTabelle.setItems(data);

            // Schließen Sie die Verbindung zur Datenbank
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////Tabelle lesen//////////////////////////////////////////////////////////////

    public void lesenTabelle(TableView<ModelTabelleDaten> TableView_datenTabelle) {
        try {
            String query = "SELECT * FROM firmen";
            ObservableList<ModelTabelleDaten> data = FXCollections.observableArrayList();

            // Don't close the resultSet here
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String datum = resultSet.getString("datum");
                String adresse = resultSet.getString("adresse");
                String abgesagt = resultSet.getString("abgesagt");

                ModelTabelleDaten entry = new ModelTabelleDaten(name, datum, adresse, abgesagt);
                data.add(entry);
            }

            // Daten in TableView setzen
            TableView_datenTabelle.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resultSet here after using it
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


