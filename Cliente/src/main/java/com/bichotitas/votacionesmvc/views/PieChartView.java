package com.bichotitas.votacionesmvc.views;

import com.bichotitas.votacionesmvc.repositories.BrokerProductsRepository;
import com.bichotitas.votacionesmvc.repositories.BrokerResultsRepository;
import com.bichotitas.votacionesmvc.repositories.ProductsRepository;
import com.bichotitas.votacionesmvc.repositories.ResultsRepository;
import com.bichotitas.votacionesmvc.utils.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PieChartView implements Initializable {
    @FXML
    public HBox pieChartContainer;

    private final ProductsRepository productsRepository;
    private final ResultsRepository resultsRepository;

    public PieChartView() {
        productsRepository = new BrokerProductsRepository();
        resultsRepository = new BrokerResultsRepository();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        Logger.log(this.getClass().getSimpleName(), "Initializing the bar chart view");
        updateUI();
    }

    public void updateUI() {
        Logger.log(this.getClass().getSimpleName(), "Updating the pie chart view");

        pieChartContainer.getChildren().clear();

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        productsRepository.getAllProducts().forEach(productName ->
                data.add(new PieChart.Data(
                                productName,
                                resultsRepository.getResultsByProductName(productName).size()
                        )
                )
        );

        PieChart pieChart = new PieChart(data);
        pieChartContainer.getChildren().add(pieChart);
    }
}
