package app.services;

public class Svg {


    private static final String SVG_OUTER_PortNBox = "<svg version=\"1.1\"\n" +
            "x=\"%d\" y=\"%d\"\n" +
            "width=\"%s\" \n" +
            "viewBox=\"%s\" preserveAspectRatio=\"xMinYMin\">";

    private static final String SVG_INNER_PortNBox = "<svg version=\"1.1\"\n" +
            "x=\"%d\" y=\"%d\"\n" +
            "width=\"%s\" \n" +
            "viewBox=\"%s\" preserveAspectRatio=\"xMinYMin\">";

    private static final String SVG2_ArrowHeads = "<defs>\n" +
            "    <marker id=\"beginArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
            "        <path d=\"M0,6 L12,0 L12,12 L0,6\" style=\"fill: #000000;\" />\n" +
            "    </marker>\n" +
            "    <marker id=\"endArrow\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
            "        <path d=\"M0,0 L12,6 L0,12 L0,0 \" style=\"fill: #000000;\" />\n" +
            "    </marker>\n" +
            "</defs>";

    private static final String SVG2_RECT_TEMPLATE = "<rect x=\"%.2f\" y=\"%.2f\" height=\"%.2f\" width=\"%.2f\"\n" +
            "              style=\"%s\"/>";

    private static final String SVG2_LINE = "<line x1=\"%.2f\" y1=\"%.2f\" x2=\"%.2f\" y2=\"%.2f\" style=\"%s\" />";

    private static final String SVG2_TEXT = "<text style=\"%s\"  " +
            "transform=\"translate(%d,%d) rotate(%d)\">%s cm</text>";


    private StringBuilder svg2StringBuilder = new StringBuilder();


    public Svg(int x, int y, String width, String viewBox){

        svg2StringBuilder.append(String.format(SVG_OUTER_PortNBox, x, y, width ,viewBox));
    }


    public void addRectangle2(double x, double y, double width, double height, String style){

        svg2StringBuilder.append(String.format(SVG2_RECT_TEMPLATE, x, y, height, width, style));
    }
    public void addLine2(double x1, double y1, double x2, double y2, String style){

        svg2StringBuilder.append(String.format(SVG2_LINE, x1, y1, x2, y2, style));
    }


    public void addArrowLine2(double x1, double y1, double x2, double y2, String style){
        svg2StringBuilder.append(String.format(SVG2_ArrowHeads));
        svg2StringBuilder.append(String.format(SVG2_LINE,x1,y1,x2,y2,style));
    }


    public void addText2(String style, int x, int y, int rotation, String text){

        svg2StringBuilder.append(String.format(SVG2_TEXT,style,x,y,rotation,text));
    }

    public String addSvg2(Svg innerSVG){

        return svg2StringBuilder.append(innerSVG.toString()).toString();
    }



    @Override
    public String toString() {

        return svg2StringBuilder.toString() + "</svg>" + "</svg>";
    }
}
