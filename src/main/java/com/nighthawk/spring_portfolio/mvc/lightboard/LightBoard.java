package com.nighthawk.spring_portfolio.mvc.lightboard;

import lombok.Data;

@Data // Annotations to simplify writing code (ie constructors, setters)
public class LightBoard {
    private Light[][] lights;
    private int rows;
    private int columns;

    /* Initialize LightBoard and Lights */
    public LightBoard(int numRows, int numCols, int rows, int columns) {
        this.lights = new Light[numRows][numCols];
        this.rows = rows;
        this.columns = columns;
        // 2D array nested loops, used for initialization
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                lights[row][col] = new Light(); // each cell needs to be constructed
            }
        }
    }

    public void setAllOn() {
        for (int i = 0; i < lights.length; i++) {
            for (int j = 0; j < lights[i].length; j++) {
                lights[i][j].setOn(true);
            }
        }
    }

    /* Output is intended for API key/values */
    public String toString() {
        String outString = "[";
        // 2D array nested loops, used for reference
        for (int row = 0; row < lights.length; row++) {
            for (int col = 0; col < lights[row].length; col++) {
                outString +=
                        // data
                        "{" +
                                "\"row\": " + row + "," +
                                "\"column\": " + col + "," +
                                "\"light\": " + lights[row][col] + // extract toString data
                                "},";
            }
        }
        // remove last comma, newline, add square bracket, reset color
        outString = outString.substring(0, outString.length() - 1) + "]";
        return outString;
    }

    /* Output is intended for Terminal, effects added to output */
    public String toTerminal() {
        String outString = "[";
        // 2D array nested loops, used for reference
        for (int row = 0; row < lights.length; row++) {
            for (int col = 0; col < lights[row].length; col++) {
                outString +=
                        // reset
                        "\033[m" +

                        // color
                                "\033[38;2;" +
                                lights[row][col].getRed() + ";" + // set color using getters
                                lights[row][col].getGreen() + ";" +
                                lights[row][col].getBlue() + ";" +
                                lights[row][col].getEffect() + "m" +
                                // data, extract custom getters
                                "{" +
                                "\"" + "isOn\": " + lights[row][col].getOn() +
                                "," +
                                "\"" + "RGB\": " + "\"" + lights[row][col].getRGB() + "\"" +
                                "," +
                                "\"" + "Effect\": " + "\"" + lights[row][col].getEffectTitle() + "\"" +
                                "}," +
                                // newline
                                "\n";
            }
        }
        // remove last comma, newline, add square bracket, reset color
        outString = outString.substring(0, outString.length() - 2) + "\033[m" + "]";
        return outString;
    }

    /* Output is intended for Terminal, draws color palette */
    public String toColorPalette() {
        // block sizes
        // final int ROWS = 4;
        // final int COLS = 4;

        // Build large string for entire color palette
        String outString = "";
        // find each row
        for (int row = 0; row < lights.length; row++) {
            // repeat each row for block size
            for (int i = 0; i < this.rows; i++) {
                // find each column
                for (int col = 0; col < lights[row].length; col++) {
                    // repeat each column for block size
                    for (int j = 0; j < this.columns; j++) {
                        // print single character, except at midpoint print color code
                        if (lights[row][col].getOn()) {
                            String c = (i == (int) (this.rows / 2) && j == (int) (this.columns / 2))
                                    ? lights[row][col].getRGB()
                                    : (j == (int) (this.columns / 2)) // nested ternary
                                            ? " ".repeat(lights[row][col].getRGB().length())
                                            : " ";

                            outString +=
                                    // reset
                                    "\033[m" +

                                    // color
                                            "\033[38;2;" +
                                            lights[row][col].getRed() + ";" +
                                            lights[row][col].getGreen() + ";" +
                                            lights[row][col].getBlue() + ";" +
                                            "7m" +

                                            // color code or blank character
                                            c +

                                            // reset
                                            "\033[m";
                        }
                    }
                }
                outString += "\n";
            }
        }
        // remove last comma, newline, add square bracket, reset color
        outString += "\033[m";
        return outString;
    }

    public void setColor(int row, int col, short red, short green, short blue) {
        lights[row][col].setNewColor(red, green, blue);
    }

    static public void main(String[] args) {
        // create and display LightBoard
        LightBoard lightBoard = new LightBoard(4, 4, 4, 4);
        System.out.println(lightBoard); // use toString() method
        System.out.println(lightBoard.toTerminal());
        System.out.println(lightBoard.toColorPalette());
        lightBoard.setAllOn();
        System.out.println(lightBoard.toColorPalette());
        short red = 255;
        short green = 255;
        short blue = 255;
        lightBoard.setColor(1, 0, red, green, blue);
        System.out.println(lightBoard.toColorPalette());

    }
}
