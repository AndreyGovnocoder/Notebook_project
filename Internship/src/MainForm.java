import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

public class MainForm
{
    static ArrayList<Person> _allPersons;
    static ArrayList<Position> _allPositions;
    private TableView<Person> _personTableView;
    private Scene _mainFormScene;
    private Stage _mainFormStage;
    private Menu _helpMenu;
    static MenuItem _helpMenuItem;
    private TextField _searchTextField;
    private TextField _fioTextField;
    private TextArea _addressTextArea;
    private TextArea _workStudPlaceTextArea;
    private TextArea _datingNatureTextArea;
    private TextArea _businessQualitytTextArea;
    private TextField _positionTextField;
    private ContextMenu _contextMenu;
    private MenuItem _addPersonMenuItem;
    private MenuItem _editPersonMenuItem;
    private MenuItem _removePersonMenuItem;
    private MenuItem _createCelebrateMenuItem;
    private final Text _placeholder = new Text("Записная книжка пуста. Внесите записи для отображения");
    private Button _addPersonButton;
    private Button _editPersonButton;
    private Button _removePersonButton;
    private Button _createCelebrateButton;
    private TitledPane _searchTitledPane;
    private RadioButton _byWorkStudPlaceRBtn;
    private RadioButton _byDatingNatureRBtn;
    private RadioButton _bySecondNameRBtn;

    MainForm()
    {
        _allPersons = Database.getPersonsList();
        _allPositions = Database.getPositionsList();
        set_contextMenu();
    }

    public void show()
    {
        _mainFormStage = new Stage();
        BorderPane mainFormBorderPane = new BorderPane();

        _mainFormScene = new Scene(mainFormBorderPane, 900,600);
        _mainFormScene.addEventFilter(KeyEvent.KEY_RELEASED, this::bindingKeys);

        mainFormBorderPane.setTop(getTop());
        mainFormBorderPane.setCenter(getCenter());
        mainFormBorderPane.setBottom(getBottom());

        _mainFormStage.setScene(_mainFormScene);
        _mainFormStage.setTitle("Записная книжка");
        _mainFormStage.getIcons().add(getIcon("mainLogo.png"));
        _mainFormStage.setOnCloseRequest(event ->
        {
            if(!getAlertAskConfirmationDialog("Вы уверены, что хотите выйти из программы?"))
                event.consume();
        });
        _mainFormStage.show();
    }

    private MenuBar getTop()
    {
        MenuBar menuBar = new MenuBar();
        _helpMenu = new Menu("Помощь");
        _helpMenuItem = new MenuItem("Справка");
        MenuItem aboutMenuItem = new MenuItem("О программе");
        ImageView helpImageView = new ImageView(MainForm.getIcon("help_24.png"));
        ImageView aboutImageView = new ImageView(MainForm.getIcon("about_24.png"));

        helpImageView.setFitHeight(24);
        helpImageView.setFitWidth(24);
        aboutImageView.setFitHeight(24);
        aboutImageView.setFitWidth(24);

        aboutMenuItem.setGraphic(aboutImageView);
        _helpMenuItem.setGraphic(helpImageView);

        aboutMenuItem.setAccelerator(KeyCombination.keyCombination("ALT+A"));
        aboutMenuItem.setOnAction(event ->
        {
            AboutForm aboutForm = new AboutForm();
            aboutForm.showAndWait(_mainFormStage);
        });

        _helpMenuItem.setAccelerator(KeyCombination.keyCombination("F1"));
        _helpMenuItem.setOnAction(event ->
        {
            if (_searchTextField.isFocused())
            {
                HelpForm helpForm = new HelpForm("search");
                helpForm.showAndWait(_mainFormStage);
            } else
            {
                HelpForm helpForm = new HelpForm();
                helpForm.showAndWait(_mainFormStage);
            }
        });

        _helpMenu.getItems().addAll(_helpMenuItem, aboutMenuItem);
        menuBar.getMenus().addAll( _helpMenu);
        return menuBar;
    }

    private VBox getCenter()
    {
        VBox centerVBox = new VBox();
        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();
        HBox centerHBox = new HBox();
        _searchTitledPane = new TitledPane();
        ImageView searchImageView = new ImageView(getIcon("search_24.png"));

        searchImageView.setFitWidth(24);
        searchImageView.setFitHeight(24);

        setTitledPane(_searchTitledPane, getSearchVBox(), "Поиск записей");
        _searchTitledPane.setCollapsible(true);
        _searchTitledPane.setExpanded(false);
        _searchTitledPane.setGraphic(searchImageView);
        _searchTitledPane.expandedProperty().addListener((observable, oldValue, newValue) ->
        {
            Task<Void> sleeper = new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
                {
                    try
                    {
                        Thread.sleep(100);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            sleeper.setOnSucceeded(event ->
            {
                if(_searchTitledPane.isExpanded())
                    _searchTextField.requestFocus();
            });
            new Thread(sleeper).start();

            if(!newValue)
            {
                _searchTextField.clear();
                _personTableView.getItems().clear();
                _personTableView.getItems().addAll(_allPersons);
            }
        });
        leftVBox.setPrefWidth(_mainFormScene.getWidth()/2.8 - 10);
        leftVBox.getChildren().addAll(_searchTitledPane, get_personTableView());

        rightVBox.setPrefWidth(_mainFormScene.getWidth()/4-10);
        rightVBox.getChildren().addAll(getPersonDetailsPane());

        HBox.setHgrow(leftVBox, Priority.ALWAYS);
        HBox.setHgrow(rightVBox, Priority.ALWAYS);
        HBox.setMargin(leftVBox, new Insets(10));
        HBox.setMargin(rightVBox, new Insets(10));
        centerVBox.setStyle("-fx-background-color: #f0f8ff");
        centerHBox.setStyle("-fx-background-color: #f0f8ff");
        centerHBox.getChildren().addAll(leftVBox, rightVBox);

        centerVBox.getChildren().addAll(centerHBox);
        VBox.setVgrow(centerHBox, Priority.ALWAYS);

        return centerVBox;
    }

    private VBox getBottom()
    {
        VBox bottomVBox = new VBox();
        HBox bottomHBox = new HBox();
        HBox leftBottomHBox = new HBox();
        HBox rightBottomHBox = new HBox();
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        _addPersonButton = new Button("Добавить запись");
        _editPersonButton = new Button("Редактировать запись");
        _removePersonButton = new Button("Удалить запись");
        _createCelebrateButton = new Button("Поздравление на печать");

        ImageView addPersonImageView = new ImageView(MainForm.getIcon("add_person_32.png"));
        ImageView editPersonImageView = new ImageView(MainForm.getIcon("edit_person_32.png"));
        ImageView removePersonImageView = new ImageView(MainForm.getIcon("remove_person_32.png"));
        ImageView createCelebrateImageView = new ImageView(MainForm.getIcon("celebrate.png"));

        addPersonImageView.setFitHeight(32);
        addPersonImageView.setFitWidth(32);
        editPersonImageView.setFitHeight(32);
        editPersonImageView.setFitWidth(32);
        removePersonImageView.setFitHeight(32);
        removePersonImageView.setFitWidth(32);
        createCelebrateImageView.setFitHeight(32);
        createCelebrateImageView.setFitWidth(32);

        _addPersonButton.setOnAction(event -> addPerson());
        _addPersonButton.setGraphic(addPersonImageView);
        _editPersonButton.setDisable(true);
        _editPersonButton.setOnAction(event -> editPerson());
        _editPersonButton.setGraphic(editPersonImageView);
        _removePersonButton.setDisable(true);
        _removePersonButton.setOnAction(event -> removePerson());
        _removePersonButton.setGraphic(removePersonImageView);
        _createCelebrateButton.setDisable(true);
        _createCelebrateButton.setOnAction(event -> createCelebrate());
        _createCelebrateButton.setGraphic(createCelebrateImageView);

        rightBottomHBox.setAlignment(Pos.CENTER_RIGHT);
        rightBottomHBox.getChildren().addAll(_createCelebrateButton);
        HBox.setHgrow(rightBottomHBox, Priority.ALWAYS);
        leftBottomHBox.setSpacing(10);
        leftBottomHBox.getChildren().addAll(_addPersonButton, _editPersonButton, _removePersonButton);
        bottomHBox.getChildren().addAll(leftBottomHBox, rightBottomHBox);
        bottomHBox.setPadding(new Insets(10));
        bottomVBox.getChildren().addAll(separator, bottomHBox);
        return bottomVBox;
    }

    private VBox getPersonDetailsPane()
    {
        VBox personDetailsVBox = new VBox();
        TitledPane fioTitledPane = new TitledPane();
        TitledPane addressTitledPane = new TitledPane();
        TitledPane workStudPlaceTitledPane = new TitledPane();
        TitledPane datingNatureTitledPane = new TitledPane();
        TitledPane businessQualityTitledPane = new TitledPane();
        TitledPane positionTitledPane = new TitledPane();
        String textAreaStyle = "-fx-background-color: transparent, transparent, transparent, transparent;\n" +
                "    -fx-background-radius: 0, 0, 0, 0;\n" +
                "    -fx-padding: 0.166667em;";
        _fioTextField = new TextField();
        _fioTextField.setContextMenu(new ContextMenu());
        _fioTextField.setEditable(false);
        _fioTextField.setStyle(textAreaStyle);
        _addressTextArea = new TextArea();
        _addressTextArea.setContextMenu(new ContextMenu());
        _addressTextArea.setEditable(false);
        _addressTextArea.setStyle(textAreaStyle);
        _addressTextArea.setWrapText(true);
        _workStudPlaceTextArea = new TextArea();
        _workStudPlaceTextArea.setContextMenu(new ContextMenu());
        _workStudPlaceTextArea.setEditable(false);
        _workStudPlaceTextArea.setStyle(textAreaStyle);
        _workStudPlaceTextArea.setWrapText(true);
        _datingNatureTextArea = new TextArea();
        _datingNatureTextArea.setContextMenu(new ContextMenu());
        _datingNatureTextArea.setEditable(false);
        _datingNatureTextArea.setStyle(textAreaStyle);
        _datingNatureTextArea.setWrapText(true);
        _businessQualitytTextArea = new TextArea();
        _businessQualitytTextArea.setContextMenu(new ContextMenu());
        _businessQualitytTextArea.setEditable(false);
        _businessQualitytTextArea.setStyle(textAreaStyle);
        _businessQualitytTextArea.setWrapText(true);
        _positionTextField = new TextField();
        _positionTextField.setContextMenu(new ContextMenu());
        _positionTextField.setEditable(false);
        _positionTextField.setStyle(textAreaStyle);

        setTitledPane(fioTitledPane, _fioTextField, "Ф.И.О.");
        setTitledPane(addressTitledPane, _addressTextArea, "Адрес");
        setTitledPane(workStudPlaceTitledPane, _workStudPlaceTextArea, "Место учебы или работы");
        setTitledPane(positionTitledPane, _positionTextField, "Должность");
        setTitledPane(datingNatureTitledPane, _datingNatureTextArea, "Характер знакомства");
        setTitledPane(businessQualityTitledPane, _businessQualitytTextArea, "Деловые качества");

        personDetailsVBox.setSpacing(10);
        personDetailsVBox.getChildren().addAll(
                fioTitledPane,
                addressTitledPane,
                workStudPlaceTitledPane,
                positionTitledPane,
                datingNatureTitledPane,
                businessQualityTitledPane
        );
        return personDetailsVBox;
    }

    private VBox getSearchVBox()
    {
        VBox searchVBox = new VBox();
        HBox rBtnsHBox = new HBox();
        _searchTextField = new TextField();
        Button clearSearchFieldBtn = new Button("очистить");
        ToggleGroup searchGroup = new ToggleGroup();

        _byWorkStudPlaceRBtn = new RadioButton("по месту работы или учёбы");
        _byDatingNatureRBtn = new RadioButton("по характеру знакомства");
        _bySecondNameRBtn = new RadioButton("по фамилии");

        _bySecondNameRBtn.setSelected(true);
        _bySecondNameRBtn.setToggleGroup(searchGroup);
        _byDatingNatureRBtn.setToggleGroup(searchGroup);
        _byWorkStudPlaceRBtn.setToggleGroup(searchGroup);

        _byWorkStudPlaceRBtn.setWrapText(true);
        _byDatingNatureRBtn.setWrapText(true);

        clearSearchFieldBtn.setOnAction( event -> _searchTextField.clear());

        searchGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
        {
            if(searchGroup.getSelectedToggle() != null)
            {
                if(_searchTextField.getText().equals("") || _searchTextField.getText().equals(" "))
                {
                    setDisabledToButtons();
                    _personTableView.getItems().clear();
                    _personTableView.getItems().addAll(_allPersons);
                } else
                {
                    if(_byWorkStudPlaceRBtn.isSelected())
                        searchByWorkStudPlace(_searchTextField.getText());
                    else if (_byDatingNatureRBtn.isSelected())
                        searchByDatingNature(_searchTextField.getText());
                    else if(_bySecondNameRBtn.isSelected())
                        searchBySecondName(_searchTextField.getText());
                }
            }
        });

        _searchTextField.setContextMenu(new ContextMenu());

        _searchTextField.textProperty().addListener((observable, oldValue, newValue) ->
        {

            if(newValue.equals("") || newValue.equals(" "))
            {
                clearTextAreas();
                setDisabledToButtons();
                _personTableView.getItems().clear();
                _personTableView.getItems().addAll(_allPersons);
            }else
            {
                if(_byWorkStudPlaceRBtn.isSelected())
                    searchByWorkStudPlace(_searchTextField.getText());
                else if (_byDatingNatureRBtn.isSelected())
                    searchByDatingNature(_searchTextField.getText());
                else if(_bySecondNameRBtn.isSelected())
                    searchBySecondName(_searchTextField.getText());
            }
        });


        rBtnsHBox.getChildren().addAll(
                _bySecondNameRBtn,
                _byWorkStudPlaceRBtn,
                _byDatingNatureRBtn);
        rBtnsHBox.setSpacing(10);
        rBtnsHBox.setAlignment(Pos.CENTER_LEFT);
        searchVBox.setSpacing(10);
        searchVBox.setAlignment(Pos.CENTER_LEFT);
        searchVBox.getChildren().addAll(_searchTextField, rBtnsHBox);
        return searchVBox;
    }

    static void setTitledPane(TitledPane titledPane, Node node, String title)
    {
        titledPane.setCollapsible(false);
        titledPane.setExpanded(true);
        titledPane.setText(title);
        titledPane.setContent(node);
    }

    private TableView<Person> get_personTableView()
    {
        _personTableView = new TableView<>();

        TableColumn<Person, String> nameCol = new TableColumn<>("Ф.И.О.");
        TableColumn<Person, String> phoneCol = new TableColumn<>("Телефон");
        TableColumn<Person, String> addressCol = new TableColumn<>("Адрес");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("_fio"));
        nameCol.prefWidthProperty().bind(_personTableView.widthProperty().multiply(0.26));
        /*
        nameCol.setCellFactory(tc ->
        {
            TableCell<Person, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(nameCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
         */

        phoneCol.setCellValueFactory(new PropertyValueFactory<>("_phone"));
        phoneCol.prefWidthProperty().bind(_personTableView.widthProperty().multiply(0.1935));
        phoneCol.setStyle("-fx-alignment: CENTER;");

        addressCol.setCellValueFactory(new PropertyValueFactory<>("_address"));
        addressCol.prefWidthProperty().bind(_personTableView.widthProperty().multiply(0.538));
        addressCol.setStyle("-fx-alignment: CENTER;");
        /*
        addressCol.setCellFactory(tc ->
        {
            TableCell<Person, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(addressCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
         */

        _personTableView.getColumns().addAll(nameCol, phoneCol, addressCol);
        _personTableView.setItems(FXCollections.observableArrayList(_allPersons));
        VBox.setVgrow(_personTableView, Priority.ALWAYS);
        HBox.setHgrow(_personTableView, Priority.ALWAYS);
        _personTableView.setPlaceholder(_placeholder);
        _personTableView.setContextMenu(_contextMenu);
        _personTableView.setOnMouseClicked(event ->
        {
            if(event.getButton() == MouseButton.SECONDARY)
            {
                _contextMenu.show(_personTableView, 0, 0);
                _contextMenu.hide();
            }
        });

        _personTableView.setRowFactory(new Callback<TableView<Person>, TableRow<Person>>()
        {
            @Override
            public TableRow<Person> call(TableView<Person> tableView)
            {
                TableRow<Person> row = new TableRow<Person>()
                {
                    @Override
                    protected void updateItem(Person person, boolean empty)
                    {
                        super.updateItem(person, empty);
                        this.setFocused(true);
                    }
                };

                row.setOnMouseClicked(event ->
                {
                    if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                            && event.getClickCount() == 1)
                    {
                        setEnabledToButtons();
                        onClickOnRowPerson(row.getItem());
                    } else if(!row.isEmpty() && event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY)
                    {
                        editPerson();
                    } else if ((!row.isEmpty() && event.getButton() == MouseButton.SECONDARY))
                    {
                        row.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>()
                        {
                            @Override
                            public void handle(ContextMenuEvent event)
                            {
                                if(row.getItem()!=null)
                                {
                                    setEnabledToButtons();
                                    _editPersonMenuItem.disableProperty().set(false);
                                    _removePersonMenuItem.disableProperty().set(false);
                                    _createCelebrateMenuItem.disableProperty().set(false);
                                    _contextMenu.show(row, event.getScreenX() + 10, event.getScreenY() + 5);
                                    onClickOnRowPerson(row.getItem());
                                }
                            }
                        });
                    }
                });
                return row;
            }
        });

        _personTableView.setOnKeyPressed(event ->
        {
            if(event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP)
                if(_personTableView.getSelectionModel().getSelectedItem() != null)
                    onClickOnRowPerson(_personTableView.getSelectionModel().getSelectedItem());
        });
        return _personTableView;
    }

    private void clearTextAreas()
    {
        _fioTextField.clear();
        _addressTextArea.clear();
        _workStudPlaceTextArea.clear();
        _datingNatureTextArea.clear();
        _businessQualitytTextArea.clear();
        _positionTextField.clear();
    }

    private void set_contextMenu()
    {
        _contextMenu = new ContextMenu();
        _addPersonMenuItem = new MenuItem("Добавить");
        _editPersonMenuItem = new MenuItem("Редактировать");
        _removePersonMenuItem = new MenuItem("Удалить");
        _createCelebrateMenuItem = new MenuItem("Поздравление на печать");

        ImageView addPersonImageView = new ImageView(MainForm.getIcon("add_person_24.png"));
        ImageView editPersonImageView = new ImageView(MainForm.getIcon("edit_person_24.png"));
        ImageView removePersonImageView = new ImageView(MainForm.getIcon("remove_person_24.png"));
        ImageView createCelebrateImageView = new ImageView(MainForm.getIcon("celebrate.png"));

        addPersonImageView.setFitHeight(24);
        addPersonImageView.setFitWidth(24);
        editPersonImageView.setFitHeight(24);
        editPersonImageView.setFitWidth(24);
        removePersonImageView.setFitHeight(24);
        removePersonImageView.setFitWidth(24);
        createCelebrateImageView.setFitHeight(24);
        createCelebrateImageView.setFitWidth(24);

        _addPersonMenuItem.setGraphic(addPersonImageView);
        _editPersonMenuItem.setGraphic(editPersonImageView);
        _removePersonMenuItem.setGraphic(removePersonImageView);
        _createCelebrateMenuItem.setGraphic(createCelebrateImageView);

        _addPersonMenuItem.setOnAction(event -> addPerson());
        _editPersonMenuItem.setOnAction(event -> editPerson());
        _removePersonMenuItem.setOnAction(event -> removePerson());
        _createCelebrateMenuItem.setOnAction(event -> createCelebrate() );

        _contextMenu.getItems().addAll(
                _addPersonMenuItem,
                _editPersonMenuItem,
                _removePersonMenuItem,
                new SeparatorMenuItem(),
                _createCelebrateMenuItem);

        _contextMenu.onHidingProperty().set(event ->
        {
            _editPersonMenuItem.disableProperty().set(true);
            _removePersonMenuItem.disableProperty().set(true);
            _createCelebrateMenuItem.disableProperty().set(true);
        });
    }

    private void searchByWorkStudPlace(String searchText)
    {
        clearTextAreas();
        setDisabledToButtons();
        _personTableView.getItems().clear();
        String delimetr = " ";
        searchText = searchText.toLowerCase();
        char[] searchCharArray = searchText.toCharArray();

        for(Person person:_allPersons){
            String workStudPlace = person.get_workStudPlace().toLowerCase();
            String[] wsPlaceWordsArray = workStudPlace.split(delimetr); //массив слов

            label1: for (String word: wsPlaceWordsArray){
                char [] wsPlaceCharArray = word.toCharArray();

                for (int i=0; i<searchCharArray.length; i++)
                {
                    if (i==wsPlaceCharArray.length) break;
                    if (searchCharArray[i]!=wsPlaceCharArray[i]) break;
                    if (i==searchCharArray.length-1)
                    {
                        _personTableView.getItems().addAll(person);
                        break label1;
                    }
                }
            }
        }

        if(_personTableView.getItems().isEmpty())
            _personTableView.setPlaceholder(new Text("Поиск не дал результатов"));
        else
        {
            _personTableView.setPlaceholder(_placeholder);
            _personTableView.getFocusModel().focus(0);
            _personTableView.getSelectionModel().select(0);
            setEnabledToButtons();
            onClickOnRowPerson(_personTableView.getFocusModel().getFocusedItem());
        }
    }

    private void searchByDatingNature(String searchText)
    {
        clearTextAreas();
        setDisabledToButtons();
        _personTableView.getItems().clear();
        String delimetr = " ";
        searchText = searchText.toLowerCase();
        char[] searchCharArray = searchText.toCharArray();

        for(Person person:_allPersons){
            String workStudPlace = person.get_datingsNature().toLowerCase();
            String[] wsPlaceWordsArray = workStudPlace.split(delimetr); //массив слов

            label1: for (String word: wsPlaceWordsArray){
                char [] wsPlaceCharArray = word.toCharArray();

                for (int i=0; i<searchCharArray.length; i++)
                {
                    if (i==wsPlaceCharArray.length) break;
                    if (searchCharArray[i]!=wsPlaceCharArray[i]) break;
                    if (i==searchCharArray.length-1)
                    {
                        _personTableView.getItems().addAll(person);
                        break label1;
                    }
                }
            }
        }

        if(_personTableView.getItems().isEmpty())
            _personTableView.setPlaceholder(new Text("Поиск не дал результатов"));
        else
        {
            _personTableView.setPlaceholder(_placeholder);
            _personTableView.getFocusModel().focus(0);
            _personTableView.getSelectionModel().select(0);
            setEnabledToButtons();
            onClickOnRowPerson(_personTableView.getFocusModel().getFocusedItem());
        }
    }

    private void searchBySecondName(String searchText)
    {
        clearTextAreas();
        setDisabledToButtons();
        _personTableView.getItems().clear();
        String delimetr = " ";
        searchText = searchText.toLowerCase();
        char[] searchCharArray = searchText.toCharArray();

        for(Person person:_allPersons){
            String workStudPlace = person.get_secondName().toLowerCase();
            String[] wsPlaceWordsArray = workStudPlace.split(delimetr); //массив слов

            label1: for (String word: wsPlaceWordsArray){
                char [] wsPlaceCharArray = word.toCharArray();

                for (int i=0; i<searchCharArray.length; i++)
                {
                    if (i==wsPlaceCharArray.length) break;
                    if (searchCharArray[i]!=wsPlaceCharArray[i]) break;
                    if (i==searchCharArray.length-1)
                    {
                        _personTableView.getItems().addAll(person);
                        break label1;
                    }
                }
            }
        }

        if(_personTableView.getItems().isEmpty())
            _personTableView.setPlaceholder(new Text("Поиск не дал результатов"));
        else
        {
            _personTableView.setPlaceholder(_placeholder);
            _personTableView.getFocusModel().focus(0);
            _personTableView.getSelectionModel().select(0);
            setEnabledToButtons();
            onClickOnRowPerson(_personTableView.getFocusModel().getFocusedItem());
        }
    }

    private void onClickOnRowPerson(Person person)
    {
        setEnabledToButtons();
        clearTextAreas();
        final StringBuilder sb = new StringBuilder();
        if(person.get_secondName() != null)
            sb.append(person.get_secondName()).append(" ");
        if(person.get_name() != null)
            sb.append(person.get_name()).append(" ");
        if(person.get_patronymic() != null)
            sb.append(person.get_patronymic());
        _fioTextField.setText(sb.toString());
        if(person.get_address() != null)
            _addressTextArea.setText(person.get_address());
        if(person.get_workStudPlace() != null)
            _workStudPlaceTextArea.setText(person.get_workStudPlace());
        if(person.get_position() != 0)
            if(getPositonById(person.get_position()) != null)
                _positionTextField.setText(getPositonById(person.get_position()).get_position());
        if(person.get_datingsNature() != null)
            _datingNatureTextArea.setText(person.get_datingsNature());
        if(person.get_businessQuality() != null)
            _businessQualitytTextArea.setText(person.get_businessQuality());
    }

    private void setEnabledToButtons()
    {
        _editPersonButton.setDisable(false);
        _removePersonButton.setDisable(false);
        _createCelebrateButton.setDisable(false);
    }

    private void setDisabledToButtons()
    {
        _editPersonButton.setDisable(true);
        _removePersonButton.setDisable(true);
        _createCelebrateButton.setDisable(true);
    }

    private void addPerson()
    {
        PersonForm personForm = new PersonForm();
        personForm.showAndWait(_mainFormStage);
        if(personForm.get_ok())
        {
            if(Database.addPerson(personForm.get_person()))
            {
                getAlertInformationDialog("Запись успешно добавлена");
                personForm.get_person().set_id(Database.getLastId(Database.PERSONALDATA_TABLE));
                _allPersons.add(personForm.get_person());
                _personTableView.getItems().add(personForm.get_person());
                _personTableView.scrollTo(personForm.get_person());
                _personTableView.getFocusModel().focus(_personTableView.getItems().size() -1);
                _personTableView.getSelectionModel().select(_personTableView.getItems().size() -1);
                onClickOnRowPerson(personForm.get_person());
            }
        }
    }

    private void editPerson()
    {
        if(_personTableView.getSelectionModel().getSelectedItem() != null)
        {
            int index = _personTableView.getSelectionModel().getSelectedIndex();
            PersonForm personForm = new PersonForm(_personTableView.getSelectionModel().getSelectedItem());
            personForm.showAndWait(_mainFormStage);
            if(personForm.get_ok())
            {
                Person person = personForm.get_person();
                if(Database.editPerson(person))
                {
                    getAlertInformationDialog("Изменения успешно сохранены");
                    int indexInArray = _allPersons.indexOf(_personTableView.getSelectionModel().getSelectedItem());
                    int indexInTableView = _personTableView.getSelectionModel().getSelectedIndex();
                    _allPersons.set(indexInArray, person);
                    _personTableView.getItems().set(indexInTableView, person);
                    _personTableView.getFocusModel().focus(index);
                    _personTableView.getSelectionModel().select(index);
                    _personTableView.scrollTo(index);
                    onClickOnRowPerson(person);
                }
            }
        }
    }

    private void removePerson()
    {
        if(_personTableView.getSelectionModel().getSelectedItem() != null)
        {
            Person person = _personTableView.getSelectionModel().getSelectedItem();
            if(getAlertAskConfirmationDialog(
                    "Вы уверены что хотите удалить запись '" + person.toString() + "'?"  ))
            {
                if(Database.removeObject(person.get_id(), Database.PERSONALDATA_TABLE))
                {
                    getAlertInformationDialog("Запись успешно удалена");
                    _allPersons.remove(person);
                    _personTableView.getItems().remove(person);
                    clearTextAreas();
                    if(_personTableView.getItems().isEmpty())
                        setDisabledToButtons();
                } else
                {
                    getAlertErrorDialog("Возникла непредвиденная ошибка");
                    clearTextAreas();
                    if(_personTableView.getItems().isEmpty())
                        setDisabledToButtons();
                }
            }
        }
    }

    private void createCelebrate()
    {
        if(_personTableView.getSelectionModel().getSelectedItem() != null)
        {
            PrintCongratulation printCongratulation = new PrintCongratulation(
                    _personTableView.getSelectionModel().getSelectedItem()
            );
            printCongratulation.showAndWait(_mainFormStage);
        }
    }

    private void bindingKeys(KeyEvent event)
    {
        final KeyCombination keyCombAltM = new KeyCodeCombination(KeyCode.M, KeyCombination.ALT_DOWN);
        final KeyCombination keyCombCtrlF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombCtrlD = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombCtrlG = new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombCtrlW = new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombCtrlE = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombCtrlR = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        final KeyCombination keyCombCtrlP = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);

        if(event.getCode() == KeyCode.ESCAPE)
            if(getAlertAskConfirmationDialog("Закрыть программу?"))
                _mainFormStage.close();

        if (keyCombAltM.match(event))
        {
            _helpMenu.show();
            event.consume(); // <-- stops passing the event to next node
        }

        if(keyCombCtrlF.match(event))
        {
            if(!_searchTitledPane.isExpanded())
            {
                _searchTitledPane.setExpanded(true);
                _searchTitledPane.setFocusTraversable(false);
            }
            _searchTextField.requestFocus();
            event.consume();
        }

        if(_searchTitledPane.isExpanded())
        {
            if(keyCombCtrlD.match(event))
            {
                if(_byWorkStudPlaceRBtn.isSelected())
                    _bySecondNameRBtn.setSelected(true);
                else if(_bySecondNameRBtn.isSelected())
                    _byDatingNatureRBtn.setSelected(true);
                else if(_byDatingNatureRBtn.isSelected())
                    _byWorkStudPlaceRBtn.setSelected(true);
            }

            if(keyCombCtrlG.match(event))
            {
                if(_byWorkStudPlaceRBtn.isSelected())
                    _byDatingNatureRBtn.setSelected(true);
                else if( _byDatingNatureRBtn.isSelected())
                    _bySecondNameRBtn.setSelected(true);
                else if(_bySecondNameRBtn.isSelected())
                    _byWorkStudPlaceRBtn.setSelected(true);
            }

            if(_searchTextField.isFocused())
            {
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB)
                {
                    _personTableView.requestFocus();
                } else if(event.getCode() == KeyCode.BACK_SPACE)
                {
                    _searchTextField.getOnKeyPressed();
                }
            }
            event.consume();
        }

        if(keyCombCtrlW.match(event))
        {
            addPerson();
            event.consume();
        }

        if(keyCombCtrlE.match(event))
        {
            editPerson();
            event.consume();
        }

        if(keyCombCtrlR.match(event))
        {
            removePerson();
            event.consume();
        }

        if(keyCombCtrlP.match(event))
        {
            createCelebrate();
            event.consume();
        }
    }

    static void getAlertErrorDialog(String text)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        ImageView imageView = new ImageView(getIcon("error_48.png"));
        alert.setTitle("Ошибка!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.setGraphic(imageView);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        alertStage.showAndWait();
    }

    static void getAlertWarningDialog(String text)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        ImageView imageView = new ImageView(getIcon("warning_48.png"));
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.setGraphic(imageView);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        alertStage.showAndWait();
    }

    static void getAlertInformationDialog(String text)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        ImageView imageView = new ImageView(getIcon("info_48.png"));
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.setGraphic(imageView);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        alertStage.showAndWait();
    }

    static boolean getAlertAskConfirmationDialog(String text)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ImageView imageView = new ImageView(getIcon("ask_48.png"));
        alert.setHeaderText(null);
        alert.setTitle("Вы уверены?");
        alert.setContentText(text);
        alert.setGraphic(imageView);
        ButtonType buttonYes = new ButtonType("Да");
        ButtonType buttonNo = new ButtonType("Нет");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(buttonYes, buttonNo);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(MainForm.getIcon("mainLogo.png"));
        Optional<ButtonType> option = alert.showAndWait();
        return option.get() == buttonYes;
    }

    static Position getPositonById(int id)
    {
        for (Position position : _allPositions)
        {
            if(position.get_id() == id)
                return position;
        }
        return null;
    }

    static Person getPersonById(int id)
    {
        for (Person person : _allPersons)
        {
            if(person.get_id() == id)
                return person;
        }
        return null;
    }

    static Image getIcon(String nameIcon)
    {
        Image image = null;
        try
        {
            FileInputStream fs = new FileInputStream(Database.path + "\\images\\icons\\" + nameIcon);
            image = new Image(fs);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return image;
    }
}
