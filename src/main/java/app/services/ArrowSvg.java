package app.services;

public class ArrowSvg {

    private int customerWidth;
    private int customerLength;
    private Svg arrowSvg;

    public ArrowSvg(int customerWidth, int customerLength) {
        this.customerWidth = customerWidth;
        this.customerLength = customerLength;
        arrowSvg = new Svg(0,0,"100%", "0 0 850 690");
        addLinesToArrows(customerLength);
        addText(customerWidth,customerLength);

    }

    public Svg getArrowSvg2() {
        return arrowSvg;
    }

    void addLinesToArrows(int customerLength){
        arrowSvg.addArrowLine2(20,635,20,50,"stroke:#000000; marker-start: url(#beginArrow); marker-end: url(#endArrow);");
        arrowSvg.addArrowLine2(45,660,customerLength + 20,660,"stroke:#000000; marker-start: url(#beginArrow); marker-end: url(#endArrow);");
    }

    void addText(int customerWidth, int customerLength){

        String customerWidthAsString = String.valueOf(customerWidth);
        String customerLengthAsString = String.valueOf(customerLength);

        //<text style="text-anchor: middle"  transform="translate(15,300) rotate(-90)">600 cm</text>
        //<text style="text-anchor: middle"  transform="translate(420,675)" >800 cm</text>
        arrowSvg.addText2("text-anchor: middle",15,300,-90, customerWidthAsString);
        arrowSvg.addText2("text-anchor: middle",customerLength/2,675,0, customerLengthAsString);

    }

    @Override
    public String toString() {
        return arrowSvg.toString()+ "</svg>";
    }
}



