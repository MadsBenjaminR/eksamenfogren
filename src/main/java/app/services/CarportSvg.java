package app.services;

import java.util.ArrayList;
import java.util.List;

public class CarportSvg {

    private int customerWidth;
    private int customerLength;
    private Svg carportSvg;
    double startRafter = 95;

    private List<Double> iValues = new ArrayList<>();
    private List<Double> jValues = new ArrayList<>();
    private List<Double> rafters = new ArrayList<>();


    public CarportSvg(int customerWidth, int customerLength) {
        this.customerWidth = customerWidth;
        this.customerLength = customerLength;
        carportSvg = new Svg(0,0,"100%", "0 0 850 690");
        carportSvg.addRectangle2(40, 40, customerLength, 600, "stroke:#000000; fill:#ffffff");
        addRafters(customerLength);
        addBeams(customerLength);
        addPoles(customerLength);
        addMountingTape();
    }


// Bredder 240, 270, 300, 330, 370, 400, 430, 450, 480, 510, 540, 570, 600
// længder 240, 270, 300, 330, 370, 400, 430, 450, 480, 510, 540, 570, 600, 630, 660, 690, 720, 750, 780

    //Beams er remme
    private void addBeams(int customerLength){

        carportSvg.addRectangle2(40, 75, customerLength,4.5,"stroke-width:1px; stroke:#000000; fill:#ffffff;");
        carportSvg.addRectangle2(40, 605, customerLength,4.5,"stroke-width:1px; stroke:#000000; fill:#ffffff;");
    }

    //Rafters er spær
    private void addRafters(int customerLength){


        for (double i = 95 ; i < customerLength ; i += 55.714){
            rafters.add(i);
            carportSvg.addRectangle2(i, 40, 4.5, 600, "stroke:#000000; fill:#ffffff");
        }


    }

    //Poles er stolper
    private void addPoles(int customerLength){

        double lastRafter = rafters.get(rafters.size()-1);

        double distanceBetwPoles = lastRafter - startRafter;

        for(double i = startRafter ; i < lastRafter ; i += distanceBetwPoles){
            iValues.add(i);
            carportSvg.addRectangle2(i, 72, 9.7, 9.7, "stroke:#000000; fill:#ffffff");
            if(i<lastRafter){
                carportSvg.addRectangle2(lastRafter, 72, 9.7, 9.7, "stroke:#000000; fill:#ffffff");
            }

            for(double j = startRafter ; j < lastRafter ; j += distanceBetwPoles){
                jValues.add(j);
                carportSvg.addRectangle2(j, 602, 10,9.7, "stroke:#000000; fill:#ffffff");
                if(j<lastRafter) {
                    carportSvg.addRectangle2(lastRafter, 602, 9.7, 9.7, "stroke:#000000; fill:#ffffff");

                }

            }

        }
        if(customerLength >= 330) {
            double middlePole = (distanceBetwPoles / 2) + startRafter;

            carportSvg.addRectangle2( middlePole, 72, 9.7, 9.7, "stroke:#000000; fill:#ffffff");
            carportSvg.addRectangle2( middlePole, 602, 9.7, 9.7, "stroke:#000000; fill:#ffffff");

        }


    }

    //Mounting tape er hulbånd
    private void addMountingTape(){

        double lastrafter = rafters.get(rafters.size()-1);

        carportSvg.addLine2(startRafter, 75, lastrafter,609.5,"stroke:#000000; stroke-dasharray: 5 5;");
        carportSvg.addLine2(startRafter, 609.5, lastrafter,75,"stroke:#000000; stroke-dasharray: 5 5;");
    }

    public Svg getCarportSvg2() {
        return carportSvg;
    }

    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
