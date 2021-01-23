import javafx.util.Pair;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

import java.util.ArrayList;


public class CustomStepHandler implements StepHandler {

    ArrayList<Pair<Double, Pair<Double,Double>>> results;

    public CustomStepHandler() {
        results = new ArrayList<>();
    }

    public ArrayList<Pair<Double, Pair<Double, Double>>> getResults() {
        return results;
    }

    @Override
    public void init(double t0, double[] y0, double t) {
    }

    @Override
    public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
        double t = interpolator.getCurrentTime();
        double [] x = interpolator.getInterpolatedState();
        results.add(new Pair<>(t, new Pair<>(x[0], x[1])));
    }
}
