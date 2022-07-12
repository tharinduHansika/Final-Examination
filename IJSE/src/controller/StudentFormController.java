package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Student;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentFormController {
    public TableView<Student> tblStudent;
    public TableColumn colStudentID;
    public TableColumn colStudentName;
    public TableColumn colEmail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNIC;
    public JFXButton btnDelete;
    public JFXButton btnSave;
    public JFXTextField txtStudentID;
    public JFXTextField txtStudentName;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextField txtAddress;
    public JFXTextField txtNIC;

    public void initialize(){
        colStudentID.setCellValueFactory(new PropertyValueFactory<>("student_id"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("student_name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));

        try {
            loadAllStudent();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllStudent() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Student");
        ObservableList<Student> obList = FXCollections.observableArrayList();

        while (resultSet.next()){
            obList.add(
                    new Student(
                            resultSet.getString("student_id"),
                            resultSet.getString("student_name"),
                            resultSet.getString("email"),
                            resultSet.getString("contact"),
                            resultSet.getString("address"),
                            resultSet.getString("nic")
                    )
            );

            tblStudent.setItems(obList);
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        tblStudent.refresh();
        Student student =  new Student(txtStudentID.getText(),txtStudentName.getText(),txtEmail.getText(),txtContact.getText(), txtAddress.getText(),txtNIC.getText());

        

    }
}
