import java.util.*; // will use an array list

public class ConverterLogic {
    // An array list that will hold the conversions
    private static final List<Converter> conversions = new ArrayList<>();

    static {
        //Length conversions
        conversions.add(new Converter("Length", "Meters", "Kilometers", 0.001, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Kilometers", "Meters", 1000, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Meters", "Centimeters", 100, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Centimeters", "Meters", 0.01, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Inches", "Centimeters", 2.54, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Centimeters", "Inches", 0.393701, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Miles", "Kilometers", 1.60934, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Kilometers", "Miles", 0.621371, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Miles", "Meters", 1609.34, 0, "arithmetic"));
        conversions.add(new Converter("Length", "Meters", "Miles", 0.00062137, 0, "arithmetic"));

        //Mass conversions
        conversions.add(new Converter("Mass", "Grams", "Kilograms", 0.001, 0, "arithmetic"));
        conversions.add(new Converter("Mass", "Kilograms", "Grams", 1000, 0, "arithmetic"));

        //Temperature conversions
        conversions.add(new Converter("Temperature", "Celsius", "Kelvin", 1, 273.15, "arithmetic"));
        conversions.add(new Converter("Temperature", "Kelvin", "Celsius", 1, -273.15, "arithmetic"));
        conversions.add(new Converter("Temperature", "Celsius", "Fahrenheit", 1.8, 32, "arithmetic"));
        conversions.add(new Converter("Temperature", "Fahrenheit", "Celsius", 0.5556, -32, "arithmetic"));

        //Volume Conversions
        conversions.add(new Converter("Volume", "Liters", "Milliliters", 1000, 0, "arithmetic"));
        conversions.add(new Converter("Volume", "Milliliters", "Liters", 0.001, 0, "arithmetic"));
        conversions.add(new Converter("Volume", "Liters", "Cubic Meters", 0.001, 0, "arithmetic"));
        conversions.add(new Converter("Volume", "Cubic Meters", "Liters", 1000, 0, "arithmetic"));
        conversions.add(new Converter("Volume", "Gallons", "Liters", 3.78541, 0, "arithmetic"));
        conversions.add(new Converter("Volume", "Liters", "Gallons", 0.264172, 0, "arithmetic"));

        //Time conversions
        conversions.add(new Converter("Time", "Seconds", "Minutes", 1.0 / 60, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Seconds", "Hours", 1.0 / 3600, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Seconds", "Days", 1.0 / 86400, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Minutes", "Seconds", 60, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Minutes", "Hours", 1.0 / 60, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Minutes", "Days", 1.0 / 1440, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Hours", "Seconds", 3600, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Hours", "Minutes", 60, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Hours", "Days", 1.0 / 24, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Days", "Seconds", 86400, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Days", "Minutes", 1440, 0, "arithmetic"));
        conversions.add(new Converter("Time", "Days", "Hours", 24, 0, "arithmetic"));

        //Speed conversions
        conversions.add(new Converter("Speed", "Meters/Second", "Kilometers/Hour", 3.6, 0, "arithmetic"));
        conversions.add(new Converter("Speed", "Kilometers/Hour", "Meters/Second", 1.0/3.6, 0, "arithmetic"));
        conversions.add(new Converter("Speed", "Miles/Hour", "Kilometers/Hour", 1.60934, 0, "arithmetic"));
        conversions.add(new Converter("Speed", "Kilometers/Hour", "Miles/Hour", 0.621371, 0, "arithmetic"));
        conversions.add(new Converter("Speed", "Miles/Hour", "Meters/Second", 0.44704, 0, "arithmetic"));
        conversions.add(new Converter("Speed", "Meters/Second", "Miles/Hour", 0.223694, 0, "arithmetic"));
        
    }

    //Will process the conversion request by the user
    //Returns conversion as a string
    public static String convert(String category, String originalUnit, String newUnit, String value) {

        //If category is base, a base conversion will happen
        if (category.equalsIgnoreCase("Base")) {
            return baseConversion(value, originalUnit, newUnit);
        }

        // if user enters a category, an orignal unit, and new unit, method will convert the unit
        for (Converter i: conversions) {
            if (i.category.equalsIgnoreCase(category) && i.originalUnit.equalsIgnoreCase(originalUnit) && i.newUnit.equalsIgnoreCase(newUnit)) {
                double result = Double.parseDouble(value); //Will convert input from string to double
                return String.valueOf(result * i.factor + i.shift); // does computation and then converts to string
            }
        }
            return "Invalid Conversion"; //else give an "error: invalid conversion"
    }


    private static String baseConversion(String value, String originalUnit, String newUnit) {
        int originalBase = switch(originalUnit) {
            //base of input
            case "Binary" -> 2;
            case "Octal" -> 8;
            case "Decimal" -> 10;
            case "Hexadecimal" -> 16;
            default -> 2; // Binary is the default
        };

        int newBase = switch(newUnit) {
            //base of new unit
            case "Binary" -> 2;
            case "Octal" -> 8;
            case "Decimal" -> 10;
            case "Hexadecimal" -> 16;
            default -> 10; // decimal is the default
        };

        try {
            int decimal = Integer.parseInt(value, originalBase); //Converts input to a decimal
            return Integer.toString(decimal, newBase).toUpperCase(); // converts the decimal to the desired base
        } catch (NumberFormatException e) { //The user enters an invalid base
            return "Error: Invalid Base";
        }
    }
}