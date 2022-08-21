package fx.controllers;

import java.util.Iterator;
import java.util.stream.Collectors;

import fx.comps.cell.value.factories.CellValueCustomAction;
import fx.comps.cell.value.factories.CellValueFactoryCheck;
import fx.comps.cell.value.factories.CellValueFactoryLabel;
import fx.controllers.main.MainTableDataRow;
import fx.controllers.main.ModuleStatus;
import fx.controllers.main.comps.StatusBarHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import toolkit.core.app.ToolkitApp;
import toolkit.core.info.StatusMessage.KnownState;
import toolkit.core.modules.ManagedModule;

@SuppressWarnings("restriction")
public class MainController {

	private ToolkitApp app;
	
	@FXML
    private Button test;

    @FXML
    private TableColumn<MainTableDataRow, CellValueFactoryLabel.CustomType> tblColId;
    
    @FXML
    private TableColumn<MainTableDataRow, CellValueFactoryLabel.CustomType> tblColDescription;

    @FXML
    private TableColumn<MainTableDataRow, String> tblColComment;

    @FXML
    private TableColumn<MainTableDataRow, CellValueCustomAction.CustomType> tblColAction;

    @FXML
    private TableColumn<MainTableDataRow, ModuleStatus> tblColStatus;

    @FXML
    private TableColumn<MainTableDataRow, String> tblColLogTail;

    @FXML
    private TableColumn<MainTableDataRow, CellValueFactoryCheck.CustomType> tblColAutorun;

    @FXML
    private TableColumn<MainTableDataRow, Integer> tblColTimesUsed;
    
    @FXML
    private Label lblGeneralStatus;

    @FXML
    private ProgressBar pBar;

    @FXML
    private TableView<MainTableDataRow> table;

    private ObservableList<MainTableDataRow> tableData;
    
    private MainTableDataRow tableData1;
    private MainTableDataRow tableData2;
    private StatusBarHelper statusBarHelper;
    
    @FXML
    public void initialize() {
    	tableData       = FXCollections.observableArrayList();
        statusBarHelper = new StatusBarHelper(lblGeneralStatus, pBar, 50);
        
        tblColId         .setCellValueFactory(new CellValueFactoryLabel(MainTableDataRow::getPropertyId));
        tblColDescription.setCellValueFactory(new CellValueFactoryLabel(MainTableDataRow::getPropertyDescription));
//        tblColComment    .setCellValueFactory(cellData -> cellData.getValue().getPropertyComment());
        tblColStatus     .setCellValueFactory(cellData -> cellData.getValue().getStatus());
        tblColTimesUsed  .setCellValueFactory(cellData -> cellData.getValue().getTimesUsed().asObject());
        tblColAutorun    .setCellValueFactory(new CellValueFactoryCheck());
        tblColAction     .setCellValueFactory(new CellValueCustomAction());
        
//        CellFactoryLabelGeneric<String> simpleLabelFactory = new CellFactoryLabelGeneric<String>(Function.identity());
        
//        tblColId         .setCellFactory(simpleLabelFactory);
//        tblColDescription.setCellFactory(simpleLabelFactory);
//        tblColComment    .setCellFactory(new CellFactoryText(false));
//        tblColAction     .setCellFactory(new CellFactorySplitMenuButton());
//        tblColStatus     .setCellFactory(new CellFactoryLabelGeneric<ModuleStatus>(ModuleStatus::toString));
//        tblColAutorun    .setCellFactory(new CellFactoryCheck());
        
    }
    
    private static int SQ = 1;
    private ObservableValue<Label> createLabel(CellDataFeatures<MainTableDataRow, Label> cellData) {
    	System.out.println("SQ="+SQ);
    	Label lbl = new Label("SQ:"+SQ);
    	
    	SimpleObjectProperty<Label> p = new SimpleObjectProperty<>(lbl);
    	ObservableValue<Label> oLbl = p;
    	
    	SQ++;
    	
    	System.out.println(cellData.getValue().getPropertyId());
    	
    	
		return oLbl;
	}

    private boolean doit = true;
	@FXML
    void testClick(ActionEvent event) {
    	System.out.println("llega");
    	
    	if (doit) {
    		tableData.get(0).getAutoRunText().set("re.");
    		doit = false;
		}
    	
    	for (MainTableDataRow mainTableDataRow : tableData) {
			System.out.println(mainTableDataRow.getPropertyId() + ", autoRun: " + mainTableDataRow.getAutoRunOnStartup().get() + ", autoRunText: " + mainTableDataRow.getAutoRunText().get());
		}
    	
    	if(1==1) {
    		return;
    	}
    	
    	ManagedModule m1 = app.getModuleManager().getModules().get(0);
    	m1.submitTask(m1.getUserInterface()::runAction);
    	
    	System.out.println("fin boton test.");

    	tableData1 = new MainTableDataRow();
    	tableData1.getPropertyId().set("Id.1");
    	tableData2 = new MainTableDataRow();
    	tableData2.getPropertyId().set("Id.2");
    	ObservableList<MainTableDataRow> tableData = FXCollections.observableArrayList();
    	tableData.add(tableData1);
    	tableData.add(tableData2);
    	table.setItems(tableData);
    
    	table.setSelectionModel(null);
    	
    	tblColId.sortableProperty().addListener((observable, oldValue, newValue) -> {
    		System.out.println("sor1: " + newValue);
    	});
    	tblColId.sortNodeProperty().addListener((observable, oldValue, newValue) -> {
    		System.out.println("sor2: " + newValue);
    	});
    	tblColId.sortTypeProperty().addListener((observable, oldValue, newValue) -> {
    		System.out.println("sor3: " + newValue);
    	});
    	table.getSortOrder().addListener(new ListChangeListener<Object>() {

			@Override
			public void onChanged(Change<? extends Object> c) {
				System.out.println("sort order: "+ table.getSortOrder().stream().map(TableColumn::getText).collect(Collectors.joining(", ", "{", "}")));
			}
    		
    	});
    	tblColId.widthProperty().addListener((observable, oldValue, newValue) -> {
    		System.out.println("widthProperty: " + newValue);
    	});
    	
//    	tblColId.setCellValueFactory(cellData -> cellData.getValue().getPropertyId());
    	tblColComment.setCellValueFactory(cellData -> cellData.getValue().getPropertyComment());
    	tblColComment.setCellFactory(param -> {
    		return new TableCell<MainTableDataRow, String>() {
    			private final TextField txt = new TextField();
    			private final Tooltip tooltip = new Tooltip();
    			
                {
                	System.out.println("llaga new TableCell");
                	txt.focusedProperty().addListener((observable, oldValue, newValue) -> {
                		if (newValue) {
							System.out.println("Focus");
						} else {
							System.out.println("OutFocus");
	                		MainTableDataRow data = (MainTableDataRow)getTableRow().getItem();
	                		int index = getIndex();
	                		data.getPropertyComment().set(txt.getText());
	                		
	                        System.out.println("data: " + data.getPropertyComment().get());
						}
                	});
//                	txt.textProperty().addListener((observable, oldValue, newValue) -> {
//                		MainTableDataRow data = getTableRow().getItem();
//                		data.getPropertyComment().set(newValue);
//                        System.out.println("data: " + data.getPropertyComment().get());
//                	});
                	txt.textProperty().addListener((observable, oldValue, newValue) -> {
                		String text = txt.getText();
                		tooltip.setText(text);
                		if (text == null || text.isEmpty()) {
							txt.setTooltip(null);
						} else {
							txt.setTooltip(tooltip);
						}
                	});
                }
                
                {
                	txt.setTooltip(tooltip);
                }

                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(txt);
                    }
                }
    		};
    	});
    	
    	statusBarHelper.setProgress(0.65F);
    	
//    	tblFldId.setCellValueFactory(new Callback<CellDataFeatures<String, String>, ObservableValue<String>>() {
//    	     public ObservableValue<String> call(CellDataFeatures<String, String> p) {
//    	         return new ReadOnlyObjectWrapper<>("texto fijo.");
//    	     }
//    	});
    }
    
    @FXML
    void test2(ActionEvent event) {
    	
    	try {
    		tableData1.getPropertyId().set("Nuevo valor!!.");
    		System.out.println("Valor comentario: " + tableData1.getPropertyComment().get());			
		} catch (Exception e) {}
    	
    	tblColId.setPrefWidth(100);
    	tblColId.setMaxWidth(100);
    	tblColId.setMinWidth(100);
    	
    	tblColId.setMaxWidth(10000);
    	
    	System.out.println("width: " + tblColId.getWidth());
    }
    
	public void setToolkitApp(ToolkitApp app) {
		this.app = app;
		
		this.app.getStatusDeliverer().addObserver((o, status) -> {
			if (status.getState() == KnownState.TEXT) {
				statusBarHelper.hideProgressBar();
				statusBarHelper.addStatus(status.getMessage());
			} else if (status.getState() == KnownState.MODULE_LOADING) {
				statusBarHelper.setProgress(status.getProgressPercent());
				statusBarHelper.showProgressBar();
				statusBarHelper.setStatus(status.getMessage(), KnownState.MODULE_LOADING.toString());
			}
		});
	}

	public void populateTable() {
		Iterator<ManagedModule> iter = app.getModuleManager().getModules().iterator();
		while (iter.hasNext()) {
			ManagedModule managedModule = iter.next();
			
			MainTableDataRow row = new MainTableDataRow();
			if (managedModule.isValid()) {
				row.setManagedModule(managedModule);
				row.getTimesUsed().set(0);
				row.getAutoRunOnStartup().set(false);
				row.getAutoRunText().set(MainTableDataRow.translate(false));
			} else {
				//TODO: Cambiarle el id para evitar conflicto en el cache de la tabla.
				continue;
			}
			
			tableData.add(row);
		}
		
		
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty(SQ2+"");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty(SQ2+"");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty(SQ2+"");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("4");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("5");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("6");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("7");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("8");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("9");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("10");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("11");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("12");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("13");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("14");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("15");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("16");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("17");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("18");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("19");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("20");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("21");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("22");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("23");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("24");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("25");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("26");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("27");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("28");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("29");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("30");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("31");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("32");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("33");
//			}
//		});
//		tableData.add(new MainTableDataRow() {
//			@Override
//			public SimpleStringProperty getPropertyId() {
//				return new SimpleStringProperty("34");
//			}
//		});
		
		Platform.runLater(() -> {
			table.setItems(tableData);
	    	table.setSelectionModel(null);
		});
		
		app.getStatusDeliverer().deliveryStatus("Ready");
	}
}
