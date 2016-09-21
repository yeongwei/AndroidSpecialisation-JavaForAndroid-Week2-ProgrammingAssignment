package mooc.vandy.java4android.diamonds.logic;

import android.util.Log;

import mooc.vandy.java4android.diamonds.ui.OutputInterface;

/**
 * This is where the logic of this App is centralized for this assignment.
 * <p/>
 * The assignments are designed this way to simplify your early
 * Android interactions.  Designing the assignments this way allows
 * you to first learn key 'Java' features without having to beforehand
 * learn the complexities of Android.
 */
public class Logic
        implements LogicInterface {
    /**
     * This is a String to be used in Logging (if/when you decide you
     * need it for debugging).
     */
    public static final String TAG = Logic.class.getName();

    /**
     * This is the variable that stores our OutputInterface instance.
     * <p/>
     * This is how we will interact with the User Interface [MainActivity.java].
     * <p/>
     * It is called 'out' because it is where we 'out-put' our
     * results. (It is also the 'in-put' from where we get values
     * from, but it only needs 1 name, and 'out' is good enough).
     */
    private OutputInterface mOut;

    /**
     * This is the constructor of this class.
     * <p/>
     * It assigns the passed in [MainActivity] instance (which
     * implements [OutputInterface]) to 'out'.
     */
    public Logic(OutputInterface out) {
        mOut = out;
    }

    /**
     * This is the method that will (eventually) get called when the
     * on-screen button labeled 'Process...' is pressed.
     */
    static final private String PLUS = "+";
    static final private String MINUS = "-";
    static final private String ODD = "-";
    static final private String EVEN = "=";
    static final private String SLASH_FWD = "/";
    static final private String SLASH_BWD = "\\";
    static final private String ARROW_LEFT = "<";
    static final private String ARROW_RIGHT = ">";
    static final private String BORDER_VERTICAL = "|";
    static final private String BLANK = " ";

    public void process(int size) {
        int TOTAL_ROWS = (size * 2) + 1;
        int TOTAL_COLUMNS = TOTAL_ROWS + 1;

        for (int ROW = 1; ROW <= TOTAL_ROWS; ROW++) {
            String PATTERN = ODD;
            if (isEven(ROW)) PATTERN = EVEN;

            if (isFirstRow(ROW)) {
                handleHorizontalBorderRow(TOTAL_COLUMNS, true);
            } else if (isUpperRow(ROW, TOTAL_ROWS)) {
                // handleUpperRows(PATTERN, ROW, TOTAL_ROWS, TOTAL_COLUMNS);
                handleRows(true, BORDER_VERTICAL, PATTERN, ROW, TOTAL_ROWS, TOTAL_COLUMNS);
            } else if (isMiddleRow(ROW, TOTAL_ROWS)) {
                handleMiddleRow(PATTERN, TOTAL_COLUMNS);
            } else if (isLowerRow(ROW, TOTAL_ROWS)) {
                // handleLowerRows(PATTERN, ROW, TOTAL_ROWS, TOTAL_COLUMNS);
                handleRows(false, BORDER_VERTICAL, PATTERN, ROW, TOTAL_ROWS, TOTAL_COLUMNS);
            } else if (isLastRow(ROW, TOTAL_ROWS)) {
                handleHorizontalBorderRow(TOTAL_COLUMNS);
            } else {
                // TODO: throw new Exception("Something went really wrong!");
            }
        }
    }

    private int normalizeRowNumber(int rowNum) {
        return normalizeRowNumber(rowNum, 2);
    }

    private int normalizeRowNumber(int rowNum, int normalizeFactor) {
        return rowNum - normalizeFactor;
    }

    private void handleRows(boolean top,
                                   String borderPattern, String fillerPattern,
                                   int currentRow, int totalRows,
                                   int totalColumns) {
        int fillerRepeat = computeFillerRepeat(normalizeRowNumber(currentRow), totalRows, top);

        handleLeftSegment(borderPattern, getDiamondOrientation(top, "left"), fillerPattern, fillerRepeat, currentRow, totalRows, totalColumns);
        handlerRightSegment(borderPattern, getDiamondOrientation(top, "right"), fillerPattern, fillerRepeat, currentRow, totalRows, totalColumns);
        newline();
    }

    private String getDiamondOrientation(boolean top, String direction) {
        if (top == true && direction == "left") return SLASH_FWD;
        else if (top == true && direction == "right") return SLASH_BWD;
        else if (top == false && direction == "left") return SLASH_BWD;
        else if (top == false && direction == "right") return SLASH_FWD;
        else return null; // TODO: Need exception here too
    }

    private int computeFillerRepeat(int normalizedRowIndex, int totalRows, boolean top) {
        if (top) {
            return normalizedRowIndex * 2;
        } else {
            int variantFactor = normalizedRowIndex - computeMiddleRow(totalRows, -2);
            return Math.abs((-1 * normalizedRowIndex) + (2 * variantFactor)) * 2;
        }
    }

    private void handleLeftSegment(String borderPattern,
                                          String diamondOrientation, String fillerPattern, int fillerRepeat,
                                          int currentRow, int totalRows, int totalColumns) {
        printVerticalBorder(borderPattern);
        printSpaces(computeNumberOfSpaces(totalColumns, fillerRepeat));
        printDiamondOrientation(diamondOrientation);
        printFillers(computeNumberOfFillers(fillerRepeat), fillerPattern);
    }

    private void handlerRightSegment(String borderPattern,
                                            String diamondOrientation, String fillerPattern, int fillerRepeat,
                                            int currentRow, int totalRows, int totalColumns) {
        printFillers(computeNumberOfFillers(fillerRepeat), fillerPattern);
        printDiamondOrientation(diamondOrientation);
        printSpaces(computeNumberOfSpaces(totalColumns, fillerRepeat));
        printVerticalBorder(borderPattern);
    }

    private void printVerticalBorder(String pattern) {
        print(pattern);
    }

    private int computeNumberOfSpaces(int totalColumns, int fillerRepeat) {
        return (totalColumns / 2) - (fillerRepeat / 2) - 2;
    }

    private void printSpaces(int repeat) {
        for (int i = 1; i <= repeat; i++) print(BLANK);
    }

    private void printDiamondOrientation(String pattern) {
        print(pattern);
    }

    private int computeNumberOfFillers(int repeat) {
        return repeat / 2;
    }

    private void printFillers(int repeat, String pattern) {
        for (int i = 1; i <= repeat; i++) print(pattern);
    }

    private boolean isFirstRow(int currentRow) {
        int firstRow = 1;
        if (currentRow == firstRow) return true;
        else return false;
    }

    private boolean isLastRow(int currentRow, int totalRows) {
        if (currentRow == totalRows) return true;
        else return false;
    }

    private boolean isMiddleRow(int currentRow, int totalRows) {
        if (currentRow == computeMiddleRow(totalRows, 1)) return true;
        else return false;
    }

    private boolean isUpperRow(int currentRow, int totalRows) {
        boolean isSmallerThanMiddleRow = currentRow < computeMiddleRow(totalRows, 1);
        boolean isLargerThanFirstRow = currentRow > 1;
        if (isSmallerThanMiddleRow && isLargerThanFirstRow) return true;
        else return false;
    }

    private boolean isLowerRow(int currentRow, int totalRows) {
        boolean isLargerThanMiddleRow = currentRow > computeMiddleRow(totalRows, 1);
        boolean isSmallerThanTotalRows = currentRow < totalRows;
        if (isLargerThanMiddleRow && isSmallerThanTotalRows) return true;
        else return false;
    }

    private boolean isEven(int i) {
        int remainder = i % 2;
        if (remainder == 0) return true;
        else return false;
    }

    private int computeMiddleRow(int totalRows, int offset) {
        return (totalRows + offset) / 2;
    }

    private void handleHorizontalBorderRow(int totalColumns) {
        handleHorizontalBorderRow(totalColumns, false);
    }

    private void handleHorizontalBorderRow(int totalColumns, boolean newline) {
        int staticPatterNumber = 2;
        int normalizedColumnNumber = totalColumns - staticPatterNumber;
        print(PLUS);
        for (int i = 1; i <= normalizedColumnNumber; i++) print(MINUS);
        print(PLUS);

        if (newline) newline();
    }

    private void handleMiddleRow(String pattern, int totalColumns) {
        int staticPatternCount = 4;
        print(BORDER_VERTICAL);
        print(ARROW_LEFT);
        for (int i = 1; i <= (totalColumns - staticPatternCount); i++) print(pattern);
        print(ARROW_RIGHT);
        print(BORDER_VERTICAL);
        newline();
    }

    private void print(String line) {
        mOut.print(line);
    }

    private void newline() {
        println("");
    }

    private void println(String line) {
        mOut.println(line);
    }
}
