import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Optional;

public class PersonForm
{
    private boolean _isEdit = false;
    private boolean _ok = false;
    private Person _person;
    private Stage _personFormStage;
    private Scene _personFormScene;
    private TextField _nameTextField;
    private TextField _secondNameTextField;
    private TextField _patronymicTextField;
    private TextField _phoneTextField;
    private TextArea _addressTextArea;
    private TextArea _workStrudPlaceTextArea;
    private TextArea _datingsNatureTextArea;
    private TextArea _businessQualityTextArea;
    private ComboBox<Position> _positionComboBox;
    private TextField _positionTextField;
    private HBox _positionHBox;
    private Button _createButton;
    private Button _saveButton;
    private Button _cancelButton;
    private ArrayList<Position> _foundPositions;

    PersonForm(Person person)
    {
        this._person = person;
        this._isEdit = true;
        setTextFields();
    }

    PersonForm()
    {
        setTextFields();
    }

    public void showAndWait(Stage primaryStage)
    {
        _personFormStage = new Stage();
        BorderPane personFormBorderPane = new BorderPane();
        _personFormScene = new Scene(personFormBorderPane, 800,600);
        _personFormScene.addEventFilter(KeyEvent.KEY_RELEASED, this::bindingKeys);

        personFormBorderPane.setCenter(getCenter());
        personFormBorderPane.setBottom(getBottom());

        if(_isEdit)
            _personFormStage.setTitle("Редактирование записи");
        else
            _personFormStage.setTitle("Создание новой записи");
        _personFormStage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        _personFormStage.initModality(Modality.WINDOW_MODAL);
        _personFormStage.initOwner(primaryStage);
        _personFormStage.setScene(_personFormScene);
        _personFormStage.setOnCloseRequest(event ->
        {
            if(_isEdit)
            {
                if (!MainForm.getAlertAskConfirmationDialog("Вы уверены, что хотите закрыть окно редактирования записи?"))
                    event.consume();
            } else
            {
                if (!MainForm.getAlertAskConfirmationDialog("Вы уверены, что хотите закрыть окно добавления записи?"))
                    event.consume();
            }
        });
        _personFormStage.showAndWait();
    }

    private VBox getCenter()
    {
        VBox centerVBox = new VBox();
        VBox fioVBox = new VBox();
        _positionHBox = new HBox();
        Button addPositionButton = new Button();
        Button editPositionButton = new Button();
        Button removePositionButton = new Button();
        GridPane centerGridPane = new GridPane();
        TitledPane nameTitledPane = new TitledPane();
        TitledPane secondNameTitledPane = new TitledPane();
        TitledPane patronymicTitledPane = new TitledPane();
        TitledPane addressTitledPane = new TitledPane();
        TitledPane phoneTitledPane = new TitledPane();
        TitledPane workStudPlaceTitledPane = new TitledPane();
        TitledPane datingsNatureTitledPane = new TitledPane();
        TitledPane businessQualityTitledPane = new TitledPane();
        TitledPane positonTitledPane = new TitledPane();
        _foundPositions = new ArrayList<>();
        ImageView addPositionImageView = new ImageView(MainForm.getIcon("add_pos.png"));
        ImageView editPositionImageView = new ImageView(MainForm.getIcon("edit_pos.png"));
        ImageView deletePositionImageView = new ImageView(MainForm.getIcon("delete_pos.png"));

        addPositionImageView.setFitWidth(16);
        addPositionImageView.setFitHeight(16);
        addPositionButton.setGraphic(addPositionImageView);
        addPositionButton.setTooltip(new Tooltip("Добавить новую должность"));

        editPositionImageView.setFitWidth(16);
        editPositionImageView.setFitHeight(16);
        editPositionButton.setGraphic(editPositionImageView);
        editPositionButton.setTooltip(new Tooltip("Редактировать выбранную должность"));

        deletePositionImageView.setFitWidth(16);
        deletePositionImageView.setFitHeight(16);
        removePositionButton.setGraphic(deletePositionImageView);
        removePositionButton.setTooltip(new Tooltip("Удалить выбранную должность"));

        _positionTextField.setPrefWidth(300);
        _positionTextField.setMinWidth(300);

        addPositionButton.setTooltip(new Tooltip("Добавить должность"));
        addPositionButton.setOnAction(event ->
        {
            addPosition();
        });

        editPositionButton.setTooltip(new Tooltip("Редактировать должность"));
        editPositionButton.setOnAction(event ->
        {
            if(_positionComboBox.getValue() != null)
                editPosition(_positionComboBox.getValue());
        });

        removePositionButton.setTooltip(new Tooltip("Удалить должность"));
        removePositionButton.setOnAction(event ->
        {
            if(_positionComboBox.getValue() != null)
                removePosition(_positionComboBox.getValue());
        });

        set_positionComboBox();

        _positionTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if(_positionComboBox.isShowing()) _positionComboBox.hide();
            if(_positionTextField.isFocused())
            {
                _positionComboBox.getItems().clear();
                _positionComboBox.setItems(FXCollections.observableArrayList(searchPosition(newValue)));
                _positionComboBox.show();
            }

            if(_positionTextField.getText().isEmpty())
            {
                _positionComboBox.getItems().clear();
                _positionComboBox.setItems(FXCollections.observableArrayList(MainForm._allPositions));
                _positionComboBox.show();
            }
        });
        _positionHBox.setPadding(new Insets(0.48));
        _positionHBox.setSpacing(3);
        _positionHBox.getChildren().addAll(
                _positionComboBox,
                _positionTextField,
                addPositionButton,
                editPositionButton,
                removePositionButton);

        fioVBox.setSpacing(5);
        fioVBox.getChildren().addAll(secondNameTitledPane, nameTitledPane, patronymicTitledPane);

        setTitledPane(nameTitledPane, _nameTextField, "Имя");
        setTitledPane(secondNameTitledPane, _secondNameTextField, "Фамилия");
        setTitledPane(patronymicTitledPane, _patronymicTextField, "Отчество");
        setTitledPane(addressTitledPane, _addressTextArea, "Адрес");
        setTitledPane(phoneTitledPane, _phoneTextField, "Телефон");
        setTitledPane(workStudPlaceTitledPane, _workStrudPlaceTextArea, "Место работы или учебы");
        setTitledPane(datingsNatureTitledPane, _datingsNatureTextArea, "Характер знакомства");
        setTitledPane(businessQualityTitledPane, _businessQualityTextArea, "Деловые качества");
        setTitledPane(positonTitledPane, _positionHBox, "Должность");

        centerGridPane.add(fioVBox, 0,0);
        centerGridPane.add(addressTitledPane, 1,0);
        centerGridPane.add(phoneTitledPane, 0,1);
        centerGridPane.add(positonTitledPane, 1,1);
        centerGridPane.add(workStudPlaceTitledPane, 0,2);
        centerGridPane.add(datingsNatureTitledPane, 1,2);
        centerGridPane.add(businessQualityTitledPane, 0,3,2,1);
        centerGridPane.setPadding(new Insets(15));
        centerGridPane.setHgap(10);
        centerGridPane.setVgap(10);

        centerVBox.setStyle("-fx-background-color: #f0f8ff");
        centerVBox.getChildren().addAll(centerGridPane);

        return centerVBox;
    }

    private VBox getBottom()
    {
        VBox bottomVBox = new VBox();
        AnchorPane bottomAnchorPane = new AnchorPane();
        _createButton = new Button("Внести запись");
        _saveButton = new Button("Сохранить изменения");
        _cancelButton = new Button("Отмена");
        ImageView createImageView = new ImageView(MainForm.getIcon("ok_btn_32.png"));
        ImageView saveImageView = new ImageView(MainForm.getIcon("save_btn_32.png"));
        ImageView cancelImageView = new ImageView(MainForm.getIcon("cancel_btn_32.png"));
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        createImageView.setFitHeight(32);
        createImageView.setFitWidth(32);
        saveImageView.setFitHeight(32);
        saveImageView.setFitWidth(32);
        cancelImageView.setFitHeight(32);
        cancelImageView.setFitWidth(32);

        _createButton.setGraphic(createImageView);
        _saveButton.setGraphic(saveImageView);
        _cancelButton.setGraphic(cancelImageView);

        _createButton.setOnAction(event ->
        {
            if(setPerson())
            {
                _ok = true;
                _personFormStage.close();
            }
        });

        _saveButton.setOnAction(event ->
        {
            if(setPerson())
            {
                _ok = true;
                _personFormStage.close();
            }
        });

        _cancelButton.setOnAction(event ->
        {
            if(_isEdit)
            {
                if (MainForm.getAlertAskConfirmationDialog("Вы уверены, что хотите закрыть окно редактирования записи?"))
                    _personFormStage.close();
            } else
            {
                if (MainForm.getAlertAskConfirmationDialog("Вы уверены, что хотите закрыть окно добавления записи?"))
                    _personFormStage.close();
            }
        });

        AnchorPane.setTopAnchor(_createButton, 5.0);
        AnchorPane.setLeftAnchor(_createButton, 5.0);
        AnchorPane.setBottomAnchor(_createButton, 5.0);

        AnchorPane.setTopAnchor(_saveButton, 5.0);
        AnchorPane.setLeftAnchor(_saveButton, 5.0);
        AnchorPane.setBottomAnchor(_saveButton, 5.0);

        AnchorPane.setTopAnchor(_cancelButton, 5.0);
        AnchorPane.setRightAnchor(_cancelButton, 5.0);
        AnchorPane.setBottomAnchor(_cancelButton, 5.0);

        if(_isEdit)
        {
            bottomAnchorPane.getChildren().addAll(_saveButton, _cancelButton);
        } else
        {
            bottomAnchorPane.getChildren().addAll(_createButton, _cancelButton);
        }

        bottomVBox.getChildren().addAll(separator, bottomAnchorPane);
        return bottomVBox;
    }

    public Person get_person()
    {
        return _person;
    }
    private boolean setPerson()
    {
        if (_person == null)
            _person = new Person();

        if(chekFields())
        {
            _person.set_secondName(_secondNameTextField.getText());
            if(!_nameTextField.getText().isEmpty())
                _person.set_name(_nameTextField.getText());
            if(!_patronymicTextField.getText().isEmpty())
                _person.set_patronymic(_patronymicTextField.getText());
            if(!_addressTextArea.getText().isEmpty())
                _person.set_address(_addressTextArea.getText());
            if(!_phoneTextField.getText().isEmpty())
                _person.set_phone(_phoneTextField.getText());
            if(!_workStrudPlaceTextArea.getText().isEmpty())
                _person.set_workStudPlace(_workStrudPlaceTextArea.getText());
            if(!_datingsNatureTextArea.getText().isEmpty())
                _person.set_datingsNature(_datingsNatureTextArea.getText());
            if(!_businessQualityTextArea.getText().isEmpty())
                _person.set_businessQuality(_businessQualityTextArea.getText());
            if(_positionComboBox.getValue() != null && !(_positionTextField.getText().isEmpty()))
                _person.set_position(_positionComboBox.getValue().get_id());
            else
                _person.set_position(0);
            return true;
        }
        return false;
    }

    public boolean get_ok() { return _ok; }

    private void setTitledPane (TitledPane titledPane, Node node, String title)
    {
        titledPane.setCollapsible(false);
        titledPane.setExpanded(true);
        titledPane.setText(title);
        titledPane.setContent(node);
    }

    private void set_positionComboBox()
    {
        _positionComboBox = new ComboBox<>();
        _positionComboBox.getItems().addAll(MainForm._allPositions);

        _positionComboBox.itemsProperty().getName();
        _positionComboBox.setPrefWidth(25);
        _positionComboBox.setMaxWidth(25);
        _positionComboBox.setMinWidth(25);

        _positionComboBox.setVisibleRowCount(15);

        _positionComboBox.setOnAction(event ->
        {
            if(_positionComboBox.getValue() != null)
            {
                _positionHBox.requestFocus();
                _positionTextField.setText(_positionComboBox.getSelectionModel().getSelectedItem().toString());
            }
        });

        _positionComboBox.setOnAction(event -> {
            if(_positionComboBox.getValue() != null) {
                _positionHBox.requestFocus();
                _positionTextField.setText(_positionComboBox.getSelectionModel().getSelectedItem().toString());
            }
        });

        _positionComboBox.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB)
                _workStrudPlaceTextArea.requestFocus();
        });

        if(_isEdit)
        {
            _positionComboBox.setValue(MainForm.getPositonById(_person.get_position()));
        }
    }

    private ArrayList<Position> searchPosition(String searchPosition)
    {
        if(!_foundPositions.isEmpty())
        {
            _foundPositions.clear();
        }

        String delimetr = " ";
        searchPosition = searchPosition.toLowerCase();
        char[] searchCharArray = searchPosition.toCharArray();


        for(Position position : MainForm._allPositions)
        {
            String name = position.get_position().toLowerCase();
            String[] nameWordsArray = name.split(delimetr);

            label1: for(String word: nameWordsArray)
            {
                char[] nameCharArray = word.toCharArray();

                for(int i = 0; i<searchCharArray.length; i++)
                {
                    if(i==nameCharArray.length) break;
                    if(searchCharArray[i] != nameCharArray[i]) break;
                    if(i == searchCharArray.length-1){
                        _foundPositions.add(position);
                        break label1;
                    }
                }
            }
        }

        if(_foundPositions.isEmpty())
        {
            for(Position position : MainForm._allPositions)
            {
                String name = position.get_position().toLowerCase();
                char[] nameCharArray = name.toCharArray();
                for(int i = 0; i < searchCharArray.length; i++)
                {
                    if (i == nameCharArray.length) break;
                    if (searchCharArray[i] != nameCharArray[i]) break;
                    if (i == searchCharArray.length - 1)
                    {
                        _foundPositions.add(position);
                    }
                }
            }
        }

        return new ArrayList<>(_foundPositions);
    }

    private void addPosition()
    {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.graphicProperty().set(null);
        inputDialog.setTitle("Новая должность");
        inputDialog.setHeaderText("Введите название должности");
        inputDialog.setContentText("Название должности: ");
        Stage stage = (Stage) inputDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent())
        {
            if(!inputDialog.getEditor().getText().isEmpty())
            {
                if (Database.addPosition(inputDialog.getEditor().getText()))
                {
                    Position position = new Position();
                    position.set_id(Database.getLastId(Database.POSITONS_TABLE));
                    position.set_position(inputDialog.getEditor().getText());
                    MainForm._allPositions.add(position);
                    _positionComboBox.getItems().add(position);
                    _positionTextField.setText(position.get_position());
                    _positionComboBox.setValue(position);
                }
            } else
                MainForm.getAlertWarningDialog("Вы не ввели название должности!");
        }
    }

    private void editPosition(Position position)
    {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.graphicProperty().set(null);
        inputDialog.setTitle("Редактирование должности");
        inputDialog.setHeaderText("Введите название должности");
        inputDialog.setContentText("Название должности: ");
        Stage stage = (Stage) inputDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        inputDialog.getEditor().setText(position.get_position());
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent())
        {
            if(!inputDialog.getEditor().getText().isEmpty())
            {
                int indexInArray = MainForm._allPositions.indexOf(position);
                int indexInComboBox = _positionComboBox.getItems().indexOf(position);
                position.set_position(inputDialog.getEditor().getText());
                if (Database.editPosition(position))
                {
                    MainForm._allPositions.set(indexInArray, position);
                    _positionComboBox.getItems().set(indexInComboBox, position);
                    _positionTextField.setText(position.get_position());
                    _positionComboBox.setValue(position);
                }
            }
        }
    }

    private void removePosition(Position position)
    {
        if(MainForm.getAlertAskConfirmationDialog("Вы уверены что хотите удалить должность '" +
                position.get_position() + "'?"))
        {
            if(Database.removeObject(position.get_id(), Database.POSITONS_TABLE))
            {
                MainForm._allPositions.remove(position);
                _positionComboBox.getItems().remove(position);
                _positionTextField.clear();
            }
        }
    }

    private boolean chekFields()
    {
        boolean check = false;
        if(_secondNameTextField.getText().isEmpty())
            MainForm.getAlertErrorDialog("Не заполнено поле 'Фамилия'");
        else if(_phoneTextField.getText().isEmpty())
            MainForm.getAlertErrorDialog("Не заполнено поле 'Телефон'");
        else if(_addressTextArea.getText().isEmpty())
            MainForm.getAlertErrorDialog("Не заполнено поле 'Адрес'");
        else if(!_positionTextField.getText().isEmpty() && _positionComboBox.getValue() == null)
            MainForm.getAlertErrorDialog("Должность отсутствует в списке\n" +
                    "Выберите должность из списка или добавьте новую должность");
        else
            check = true;

        return check;
    }

    private void setTextFields()
    {
        _secondNameTextField = new TextField();
        _nameTextField = new TextField();
        _patronymicTextField = new TextField();
        _phoneTextField = new TextField();
        _positionTextField = new TextField();
        _addressTextArea = new TabIgnoringTextArea();
        _workStrudPlaceTextArea = new TabIgnoringTextArea();
        _datingsNatureTextArea = new TabIgnoringTextArea();
        _businessQualityTextArea = new TabIgnoringTextArea();

        _secondNameTextField.setContextMenu(new ContextMenu());
        _nameTextField.setContextMenu(new ContextMenu());
        _patronymicTextField.setContextMenu(new ContextMenu());
        _phoneTextField.setContextMenu(new ContextMenu());
        _positionTextField.setContextMenu(new ContextMenu());
        _addressTextArea.setContextMenu(new ContextMenu());
        _workStrudPlaceTextArea.setContextMenu(new ContextMenu());
        _datingsNatureTextArea.setContextMenu(new ContextMenu());
        _businessQualityTextArea.setContextMenu(new ContextMenu());

        _addressTextArea.setWrapText(true);
        _workStrudPlaceTextArea.setWrapText(true);
        _datingsNatureTextArea.setWrapText(true);
        _businessQualityTextArea.setWrapText(true);

        _phoneTextField.textProperty().addListener(getChangeListener(_phoneTextField));
        _secondNameTextField.requestFocus();
        _secondNameTextField.setStyle("-fx-border-color:red");
        _secondNameTextField.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _nameTextField.requestFocus();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _cancelButton.requestFocus();
                else
                    _nameTextField.requestFocus();
            }
        });
        _secondNameTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (_secondNameTextField.getText().isEmpty())
                _secondNameTextField.setStyle("-fx-border-color:red");
            else
                _secondNameTextField.setStyle("-fx-border-color:green");
        });
        _nameTextField.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _patronymicTextField.requestFocus();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _secondNameTextField.requestFocus();
                else
                    _patronymicTextField.requestFocus();
            }
        });
        _patronymicTextField.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _phoneTextField.requestFocus();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _nameTextField.requestFocus();
                else
                    _phoneTextField.requestFocus();
            }
        });
        _phoneTextField.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _addressTextArea.requestFocus();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _patronymicTextField.requestFocus();
                else
                    _addressTextArea.requestFocus();
            }
        });
        _phoneTextField.setStyle("-fx-border-color:red");
        _phoneTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (_phoneTextField.getText().isEmpty())
                _phoneTextField.setStyle("-fx-border-color:red");
            else
                _phoneTextField.setStyle("-fx-border-color:green");
        });
        _addressTextArea.setStyle("-fx-border-color:red");

        _addressTextArea.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (_addressTextArea.getText().isEmpty())
                _addressTextArea.setStyle("-fx-border-color:red");
            else
                _addressTextArea.setStyle("-fx-border-color:green");
        });

        _positionTextField.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.ENTER)
                _workStrudPlaceTextArea.requestFocus();
            else if (event.getCode() == KeyCode.TAB)
            {
                if(event.isShiftDown())
                    _addressTextArea.requestFocus();
                else
                    _workStrudPlaceTextArea.requestFocus();
            }
        });

        if(_isEdit)
        {
            if(_person.get_name() != null)
                _nameTextField.setText(_person.get_name());
            if(_person.get_secondName() != null)
                _secondNameTextField.setText(_person.get_secondName());
            if(_person.get_patronymic() != null)
                _patronymicTextField.setText(_person.get_patronymic());
            if(_person.get_phone() != null)
                _phoneTextField.setText(_person.get_phone());

            if(_person.get_address() != null)
                _addressTextArea.setText(_person.get_address());
            if(_person.get_position() != 0)
                if(MainForm.getPositonById(_person.get_position()) != null)
                    _positionTextField.setText(MainForm.getPositonById(_person.get_position()).get_position());
            if(_person.get_workStudPlace() != null)
                _workStrudPlaceTextArea.setText(_person.get_workStudPlace());
            if(_person.get_datingsNature() != null)
                _datingsNatureTextArea.setText(_person.get_datingsNature());
            if(_person.get_businessQuality() != null)
                _businessQualityTextArea.setText(_person.get_businessQuality());
        }
    }

    static ChangeListener<String> getChangeListener(TextField txtpoint)
    {
        return (observable, oldValue, newValue) ->
        {
            if (!newValue.isEmpty())
            {
                txtpoint.setStyle("-fx-border-color:green");
                try
                {
                    long pointI = Long.parseLong(newValue);
                    txtpoint.setText(String.valueOf(pointI));
                } catch (Exception e) {
                    txtpoint.clear();
                    txtpoint.setText(getNumber(oldValue));
                }
            }
            else
                txtpoint.setStyle("-fx-border-color:red");
        };
    }

    static String getNumber(String value)
    {
        String n = "";
        try
        {
            return String.valueOf(Long.parseLong(value));
        } catch (Exception e)
        {
            String[] array = value.split("");
            for (String tab : array)
            {
                try
                {
                    n = n.concat(String.valueOf(Long.parseLong(String.valueOf(tab))));
                } catch (Exception ignored){}
            }
            return n;
        }
    }

    class TabIgnoringTextArea extends TextArea
    {
        final TextArea myTextArea = this;

        TabIgnoringTextArea()
        {
            addEventFilter(KeyEvent.KEY_PRESSED, new TabAndEnterHandler());
        }

        class TabAndEnterHandler implements EventHandler<KeyEvent>
        {
            private KeyEvent recodedEvent;

            @Override
            public void handle(KeyEvent event)
            {
                if (recodedEvent != null)
                {
                    recodedEvent = null;
                    return;
                }

                Parent parent = myTextArea.getParent();
                if (parent != null)
                {
                    switch (event.getCode())
                    {
                        case TAB:
                            if (event.isControlDown())
                            {
                                recodedEvent = recodeWithoutControlDown(event);
                                myTextArea.fireEvent(recodedEvent);
                            } else if(event.isShiftDown())
                            {
                                focusIfShiftDown();
                            } else
                            {
                                focusIfNotShiftDown();
                            }
                            event.consume();
                            break;

                        case ENTER:
                            if (!event.isControlDown())
                            {
                                recodedEvent = recodeWithoutControlDown(event);
                                myTextArea.fireEvent(recodedEvent);
                            }
                            event.consume();
                            break;
                    }
                }
            }

            private KeyEvent recodeWithoutControlDown(KeyEvent event)
            {
                return new KeyEvent(
                        event.getEventType(),
                        event.getCharacter(),
                        event.getText(),
                        event.getCode(),
                        event.isShiftDown(),
                        false,
                        event.isAltDown(),
                        event.isMetaDown()
                );
            }
        }
    }

    private void bindingKeys(KeyEvent event)
    {
        final KeyCombination keyCombCtrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

        if(event.getCode() == KeyCode.ESCAPE)
            _cancelButton.fire();

        if(event.getCode() == KeyCode.F1)
        {
            if(_isEdit)
            {
                HelpForm helpForm = new HelpForm("edit person");
                helpForm.showAndWait(_personFormStage);
            } else
            {
                HelpForm helpForm = new HelpForm("add person");
                helpForm.showAndWait(_personFormStage);
            }
        }

        if(keyCombCtrlS.match(event))
        {
            if(_isEdit)
                _saveButton.fire();
            else
                _createButton.fire();
            event.consume();
        }
    }

    private void focusIfShiftDown()
    {
        if(_secondNameTextField.isFocused())
            _cancelButton.requestFocus();
        else if(_cancelButton.isFocused())
        {
            if (_isEdit)
                _saveButton.requestFocus();
            else
                _createButton.requestFocus();
        }
        else if(_saveButton.isFocused() || _createButton.isFocused())
            _businessQualityTextArea.requestFocus();
        else if(_businessQualityTextArea.isFocused())
            _datingsNatureTextArea.requestFocus();
        else if(_datingsNatureTextArea.isFocused())
            _workStrudPlaceTextArea.requestFocus();
        else if(_workStrudPlaceTextArea.isFocused())
            _positionTextField.requestFocus();
        else if(_positionTextField.isFocused())
            _addressTextArea.requestFocus();
        else if(_addressTextArea.isFocused())
            _phoneTextField.requestFocus();
        else if(_phoneTextField.isFocused())
            _patronymicTextField.requestFocus();
        else if(_patronymicTextField.isFocused())
            _nameTextField.requestFocus();
        else if(_nameTextField.isFocused())
            _secondNameTextField.requestFocus();
    }

    private void focusIfNotShiftDown()
    {
        if(_secondNameTextField.isFocused())
            _nameTextField.requestFocus();
        else if(_nameTextField.isFocused())
            _patronymicTextField.requestFocus();
        else if(_patronymicTextField.isFocused())
            _phoneTextField.requestFocus();
        else if(_phoneTextField.isFocused())
            _addressTextArea.requestFocus();
        else if(_addressTextArea.isFocused())
            _positionTextField.requestFocus();
        else if(_positionTextField.isFocused())
            _workStrudPlaceTextArea.requestFocus();
        else if(_workStrudPlaceTextArea.isFocused())
            _datingsNatureTextArea.requestFocus();
        else if(_datingsNatureTextArea.isFocused())
            _businessQualityTextArea.requestFocus();
        else if(_businessQualityTextArea.isFocused())
        {
            if (_isEdit)
                _saveButton.requestFocus();
            else
                _createButton.requestFocus();
        }
        else if(_saveButton.isFocused() || _createButton.isFocused())
            _secondNameTextField.requestFocus();
    }
}
