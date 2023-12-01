package com.bichotitas.votacionesmvc.views;

import com.bichotitas.votacionesmvc.controllers.VotesController;
import com.bichotitas.votacionesmvc.models.StageAndController;
import com.bichotitas.votacionesmvc.repositories.BrokerProductsRepository;
import com.bichotitas.votacionesmvc.repositories.BrokerResultsRepository;
import com.bichotitas.votacionesmvc.repositories.ProductsRepository;
import com.bichotitas.votacionesmvc.repositories.ResultsRepository;
import com.bichotitas.votacionesmvc.utils.JavaFXUtils;
import com.bichotitas.votacionesmvc.utils.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class VotesCountView implements Initializable {
    @FXML
    public HBox productsContainer;
    @FXML
    public Button barChartButton;
    @FXML
    public Button pieChartButton;

    private final ProductsRepository productsRepository;
    private final ResultsRepository resultsRepository;
    private final VotesController votesController;
    private BarChartView barChartViewController;
    private PieChartView pieChartViewController;

    public VotesCountView() {
        productsRepository = new BrokerProductsRepository();
        resultsRepository = new BrokerResultsRepository();

        votesController = new VotesController(resultsRepository);
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        Logger.log(this.getClass().getSimpleName(), "Initializing the votes count view");
        updateVotesCountUI();
    }

    public void updateVotesCountUI() {
        Logger.log(this.getClass().getSimpleName(), "Updating the votes count view");

        productsContainer.getChildren().clear();

        productsRepository.getAllProducts().forEach(productName -> {
            VBox container = new VBox();

            Text productNameLabel = new Text(productName);
            Button addVoteButton = getAddVoteButtonByProductName(productName);
            Text votesCountLabel = new Text("Votos: " + resultsRepository.getResultsByProductName(productName).size());

            productNameLabel.setFill(Color.WHITE);
            votesCountLabel.setFill(Color.WHITE);

            container.setId("product");
            container.getChildren().addAll(productNameLabel, addVoteButton, votesCountLabel);

            productsContainer.getChildren().add(container);
        });
    }

    private Button getAddVoteButtonByProductName(String productName) {
        Button addVoteButton = new Button("Votar");

        addVoteButton.setOnAction(event -> {
            this.votesController.vote(productName);

            this.updateVotesCountUI();

            if (Objects.nonNull(barChartViewController)) {
                this.barChartViewController.updateUI();
            }

            if (Objects.nonNull(pieChartViewController)) {
                this.pieChartViewController.updateUI();
            }
        });

        return addVoteButton;
    }

    public void openBarChart() throws IOException {
        Logger.log(this.getClass().getSimpleName(), "Opening the bar chart window");

        StageAndController stageAndController = JavaFXUtils.openNewWindow("views/bar-chart.fxml", "Gráfica de barras");
        barChartViewController = stageAndController.getController();

        barChartButton.setDisable(true);
        stageAndController.getStage().setOnCloseRequest(event -> barChartButton.setDisable(false));
    }

    public void openPieChart() throws IOException {
        Logger.log(this.getClass().getSimpleName(), "Opening the pie chart window");

        StageAndController stageAndController = JavaFXUtils.openNewWindow("views/pie-chart.fxml", "Gráfica de pastel");
        pieChartViewController = stageAndController.getController();

        pieChartButton.setDisable(true);
        stageAndController.getStage().setOnCloseRequest(event -> pieChartButton.setDisable(false));
    }
}