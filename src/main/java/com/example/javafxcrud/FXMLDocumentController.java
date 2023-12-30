package com.example.javafxcrud;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
/**
 *
 * @author kobinath
 */
public class FXMLDocumentController implements Initializable {


    @FXML
    private Label label;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtCourse;

    @FXML
    private TableView<javafxcrudd.Student> table;

    @FXML
    private TableColumn<javafxcrudd.Student, String> idColumn;

    @FXML
    private TableColumn<javafxcrudd.Student, String> nameColumn;

    @FXML
    private TableColumn<javafxcrudd.Student, String> phoneColumn;

    @FXML
    private TableColumn<javafxcrudd.Student, String> courseColumn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    void Add(ActionEvent event) {

        String stname,mobile,course;
        stname = txtName.getText();
        mobile = txtPhone.getText();
        course = txtCourse.getText();
        try
        {
            pst = con.prepareStatement("insert into registration(name,mobile,course)values(?,?,?)");
            pst.setString(1, stname);
            pst.setString(2, mobile);
            pst.setString(3, course);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Registration");


            alert.setHeaderText("Student Registration");
            alert.setContentText("Record Addedddd!");

            alert.showAndWait();

            table();

            txtName.setText("");
            txtPhone.setText("");
            txtCourse.setText("");
            txtName.requestFocus();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void table()
    {
        Connect();
        ObservableList<javafxcrudd.Student> students = FXCollections.observableArrayList();
        try
        {
            System.out.println("con = " + con);
            pst = con.prepareStatement("select id,name,mobile,course from registration");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next())
                {
                    javafxcrudd.Student st = new javafxcrudd.Student();
                    st.setId(rs.getString("id"));
                    st.setName(rs.getString("name"));
                    st.setMobile(rs.getString("mobile"));
                    st.setCourse(rs.getString("course"));
                    students.add(st);
                }
            }
            table.setItems(students);
            idColumn.setCellValueFactory(f -> f.getValue().idProperty());
            nameColumn.setCellValueFactory(f -> f.getValue().nameProperty());
            phoneColumn.setCellValueFactory(f -> f.getValue().mobileProperty());
            courseColumn.setCellValueFactory(f -> f.getValue().courseProperty());



        }

        catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        table.setRowFactory( tv -> {
            TableRow<javafxcrudd.Student> myRow = new TableRow<>();
            myRow.setOnMouseClicked (event ->
            {
                if (event.getClickCount() == 1 && (!myRow.isEmpty()))
                {
                    myIndex =  table.getSelectionModel().getSelectedIndex();

                    id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));
                    txtName.setText(table.getItems().get(myIndex).getName());
                    txtPhone.setText(table.getItems().get(myIndex).getMobile());
                    txtCourse.setText(table.getItems().get(myIndex).getCourse());

                }
            });
            return myRow;
        });


    }

    @FXML
    void Delete(ActionEvent event) {
        myIndex = table.getSelectionModel().getSelectedIndex();

        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));


        try
        {
            pst = con.prepareStatement("delete from registration where id = ? ");
            pst.setInt(1, id);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Registration");


            alert.setHeaderText("Student Registration");
            alert.setContentText("Deletedd!");

            alert.showAndWait();
            table();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Update(ActionEvent event) {

        String stname,mobile,course;

        myIndex = table.getSelectionModel().getSelectedIndex();

        id = Integer.parseInt(String.valueOf(table.getItems().get(myIndex).getId()));

        stname = txtName.getText();
        mobile = txtPhone.getText();
        course = txtCourse.getText();
        try
        {
            pst = con.prepareStatement("update registration set name = ?,mobile = ? ,course = ? where id = ? ");
            pst.setString(1, stname);
            pst.setString(2, mobile);
            pst.setString(3, course);
            pst.setInt(4, id);
            pst.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Student Registration");


            alert.setHeaderText("Student Registration");
            alert.setContentText("Updateddd!");

            alert.showAndWait();
            table();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    Connection con;
    PreparedStatement pst;
    int myIndex;
    int id;



    public void Connect()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/studcruds","root","root");
            System.out.println("con = " + con);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connect();
        table();
    }

}
