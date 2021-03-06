import java.io.*;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Pair;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.EulerIntegrator;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;

public class FXMLController {

    @FXML
    private LineChart<?, ?> graph;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TextArea consoleLog;

    @FXML
    private TextField input_x0_0;

    @FXML
    private TextField input_x0_1;

    @FXML
    private TextField input_param;

    @FXML
    private TextField input_t;

    @FXML
    private TextField input_epsilon;

    @FXML
    private ChoiceBox<String> stepMethodBox;

    @FXML
    private Button btnPlot;

    @FXML
    private Button btnPlotX;

    @FXML
    private Button btnPlotV;

    @FXML
    private Button btnPlotXV;

    @FXML
    private Button btnClear;


    ObservableList<String>stepMethods = FXCollections.observableArrayList
            ("Euler","Midpoint");
    Console console;
    double x0_0;
    double x0_1;
    double param;
    double t;
    double step;
    CustomODE ode;
    FirstOrderIntegrator integrator;
    double[] x0 = new double[2];
    double[] x = new double[2];

    XYChart.Series seriesX;
    XYChart.Series seriesV;
    XYChart.Series seriesXV;

    @FXML
    void onClickCalculate() {
        String methodType = stepMethodBox.getValue();
        switch (methodType) {
            case "Euler"         -> integrator = new EulerIntegrator(step);
            case "Midpoint"      -> integrator = new MidpointIntegrator(step);
        }

        CustomStepHandler csh = new CustomStepHandler();
        integrator.addStepHandler(csh);
        x0[0]=x0_0;
        x0[1]=x0_1;
        ode = new CustomODE(param);
        integrator.integrate(ode,0,x0,t,x);
        ArrayList<Pair<Double, Pair<Double, Double>>> results = csh.getResults();
        graph.setAnimated(false);
        graph.getData().clear();
         seriesX = new XYChart.Series();
         seriesV = new XYChart.Series();
         seriesXV = new XYChart.Series();
         seriesX.setName("x = f(t)");
         seriesV.setName("v = f(t)");
         seriesXV.setName("v = f(x)");


        for(Pair p : results){
            Pair <Double,Double> xAndV = (Pair<Double, Double>) p.getValue();
            //Struktura pary: key - wartość X, value - wartość V.
            seriesX.getData().add(new XYChart.Data<>(p.getKey(),xAndV.getKey()));
            seriesV.getData().add(new XYChart.Data<>(p.getKey(),xAndV.getValue()));
            seriesXV.getData().add(new XYChart.Data<>(xAndV.getKey(),xAndV.getValue()));

        }
    }

    @FXML
    void onClickClear() {
        graph.getData().clear();
    }

    @FXML
    void onClickPlotV() {
        if(!graph.getData().contains(seriesV))
            graph.getData().add(seriesV);

    }

    @FXML
    void onClickPlotX() {
        if(!graph.getData().contains(seriesX))
            graph.getData().add(seriesX);
    }

    @FXML
    void onClickPlotXV() {
        if(!graph.getData().contains(seriesXV))
            graph.getData().add(seriesXV);
    }

    @FXML
    void onEpsilon_input() {
    step = Double.parseDouble(input_epsilon.getText());
        System.out.println("Step set!");
    }

    @FXML
    void onParam_input() {
        param = Double.parseDouble(input_param.getText());
        System.out.println("Param set!");
    }

    @FXML
    void onT_input() {
        t = Double.parseDouble(input_t.getText());
        System.out.println("T set!");
    }

    @FXML
    void onX0_0_input() {
        x0_0 = Double.parseDouble(input_x0_0.getText());
        System.out.println("x0_0 set!");
    }

    @FXML
    void onX0_1_input() {
        x0_1 = Double.parseDouble(input_x0_1.getText());
        System.out.println("x0_1 set!");
    }


    private static class Console extends OutputStream {
        private final TextArea console;
        public Console(TextArea console) {
            this.console = console;
        }
        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }
        public void write(int b) {
            appendText(String.valueOf((char)b));
        }
    }

    @FXML
    void initialize(){
        assert graph != null : "fx:id=\"graph\" was not injected: check your FXML file 'scene.fxml'.";
        assert xAxis != null : "fx:id=\"xAxis\" was not injected: check your FXML file 'scene.fxml'.";
        assert yAxis != null : "fx:id=\"yAxis\" was not injected: check your FXML file 'scene.fxml'.";
        assert consoleLog != null : "fx:id=\"consoleLog\" was not injected: check your FXML file 'scene.fxml'.";
        assert input_x0_0 != null : "fx:id=\"input_x0_0\" was not injected: check your FXML file 'scene.fxml'.";
        assert input_x0_1 != null : "fx:id=\"input_x0_1\" was not injected: check your FXML file 'scene.fxml'.";
        assert input_param != null : "fx:id=\"input_param\" was not injected: check your FXML file 'scene.fxml'.";
        assert input_t != null : "fx:id=\"input_t\" was not injected: check your FXML file 'scene.fxml'.";
        assert input_epsilon != null : "fx:id=\"input_epsilon\" was not injected: check your FXML file 'scene.fxml'.";
        assert stepMethodBox != null : "fx:id=\"RootFinderMethodBox\" was not injected: check your FXML file 'scene.fxml'.";
        assert btnPlot != null : "fx:id=\"btnPlot\" was not injected: check your FXML file 'scene.fxml'.";
        assert btnPlotX != null : "fx:id=\"btnPlotX\" was not injected: check your FXML file 'scene.fxml'.";
        assert btnPlotV != null : "fx:id=\"btnPlotV\" was not injected: check your FXML file 'scene.fxml'.";
        assert btnPlotXV != null : "fx:id=\"btnPlotXV\" was not injected: check your FXML file 'scene.fxml'.";
        assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'scene.fxml'.";
        this.console = new Console(consoleLog);
        stepMethodBox.setItems(stepMethods);
        PrintStream ps = new PrintStream(new Console(consoleLog));
        System.setOut(ps);
        System.setErr(ps);
    }
}
