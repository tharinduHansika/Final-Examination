package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
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

    public void initialize() {
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

        tblStudent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                setData((Student)newValue);
                btnSave.setText("Update");
            }
        });
    }

    private void setData(Student newValue) {
        txtStudentID.setText(newValue.getStudent_id());
        txtStudentName.setText(newValue.getStudent_name());
        txtEmail.setText(newValue.getEmail());
        txtContact.setText(newValue.getEmail());
        txtAddress.setText(newValue.getAddress());
        txtNIC.setText(newValue.getNic());
    }

    private void loadAllStudent() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Student");
        ObservableList<Student> obList = FXCollections.observableArrayList();

        while (resultSet.next()) {
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
        Student student = new Student(txtStudentID.getText(), txtStudentName.getText(), txtEmail.getText(), txtContact.getText(), txtAddress.getText(), txtNIC.getText());

        try {
            if (btnSave.getText().equals("Save")) {
                if (CrudUtil.execute("INSERT INTO Student VALUES(?,?,?,?,?,?)", student.getStudent_id(), student.getStudent_name(), student.getEmail(), student.getContact(),
                        student.getAddress(), student.getNic())) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved!").show();
                    loadAllStudent();
                    clearTextField();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Error!").show();
                }
            } else {
                if (CrudUtil.execute("UPDATE Student SET student_name=?,email=?,contact=?,address=?,nic=? WHERE student_id=?",
                        student.getStudent_name(),student.getEmail(), student.getContact(), student.getAddress(),student.getNic(),student.getStudent_id())){
                    new Alert(Alert.AlertType.CONFIRMATION,"Updated!").show();
                    tblStudent.refresh();
                    loadAllStudent();

                }else {
                    new Alert(Alert.AlertType.ERROR,"Error!").show();
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clearTextField() {
        txtStudentID.clear();
        txtStudentName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNIC.clear();
    }
}
