public class Converter {
    //Attributes of converter
    public String category; // catgeory of conversion: length, mass, time, temp, base
    public String originalUnit; // unit that will be converted
    public String newUnit; // unit after conversion
    public double factor; // factor to multiply
    public double shift; // add or subtract after multiplying (for temperature conversion)
    public String type; // type of conversion: arithemetic or base

    // Constructors
    public Converter(String category, String originalUnit, String newUnit, double factor, double shift, String type) {
        this.category = category;
        this.originalUnit = originalUnit;
        this.newUnit = newUnit;
        this.factor = factor;
        this.shift = shift;
        this.type = type;
    }
}