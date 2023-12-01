package com.bichotitas.votacionesmvc.views;

import com.bichotitas.votacionesmvc.repositories.BrokerProductsRepository;
import com.bichotitas.votacionesmvc.repositories.BrokerResultsRepository;
import com.bichotitas.votacionesmvc.repositories.ProductsRepository;
import com.bichotitas.votacionesmvc.repositories.ResultsRepository;
import com.bichotitas.votacionesmvc.utils.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BarChartView implements Initializable {
    @FXML
    public HBox barChartContainer;

    private final ProductsRepository productsRepository;
    private final ResultsRepository resultsRepository;

    public BarChartView() {
        productsRepository = new BrokerProductsRepository();
        resultsRepository = new BrokerResultsRepository();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        Logger.log(this.getClass().getSimpleName(), "Initializing the bar chart view");
        updateUI();
    }

    public void updateUI() {
        Logger.log(this.getClass().getSimpleName(), "Updating the bar chart view");

        barChartContainer.getChildren().clear();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Productos");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total de votos");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> data = new XYChart.Series<>();
        data.setName("Votos");

        productsRepository.getAllProducts().forEach(productName ->
                data.getData().add(
                        new XYChart.Data<>(
                                productName,
                                resultsRepository.getResultsByProductName(productName).size()
                        )
                )
        );

        barChart.getData().add(data);
        barChartContainer.getChildren().add(barChart);
    }
}
