package PasswordGenerator;
import static java.lang.Math.*;

public class PasswordGenerator {

    private boolean isPasswordRange;
    private int exactPasswordLength;
    private int[] minMaxRange;

    private boolean includeGeneralShare;
    private boolean[] caseTypeState;
    private int[] generalShare;
    private char[][] charCollection;

    private boolean checkScopeStatus, checkShareSum;

    public PasswordGenerator() {

        // Length / range of exit password
        this.isPasswordRange = false;
        this.exactPasswordLength = 25;
        this.minMaxRange = new int[] {-1, -1};

        /* Array collects percentage use between:
        regular characters (upper | lower) | numbers | special characters */
        this.includeGeneralShare = true; // random value
        this.caseTypeState = new boolean[] {true, true, true, true};
        this.generalShare = new int[] {25, 25, 25, 25};
        this.charCollection = fillCollection();

        // Internal additional
        this.checkScopeStatus = true;
        this.checkShareSum = true;

    }

    // Sets share between types of the characters in password
    public void setRandomShare() {
        int[] share = new int[4];
        int maxValue = 100;
        int tempRandom;
        for (int i = 0; i < share.length - 1; i++) {
            tempRandom = (int) (random() * maxValue + 1);
            maxValue -= tempRandom;
            this.generalShare[i] = tempRandom;
            if (tempRandom == 0) this.caseTypeState[i] = false;
            if (maxValue == 0) {
                for (int j = i + 1; j < share.length - 1; j++) {
                    this.generalShare[j] = 0;
                    this.caseTypeState[j] = false;
                }
                break;
            }
        }
        this.generalShare[3] = maxValue;
        if (maxValue == 0) this.caseTypeState[3] = false;
        this.includeGeneralShare = false;
    }

    // Sets share between case types
    public void setConstantShare(int regularUpper, int regularLower, int number, int special) {
        int[] shareValue = {regularUpper, regularLower, number, special};
        if (checkScopeStatus) this.checkScopeStatus = Validation.shareScopeStatus(shareValue, 'i');
        if (checkShareSum) this.checkShareSum = Validation.shareSumStatus(shareValue, 'i');
        if ((!checkShareSum) && (!checkScopeStatus)) {
            for (int i = 0; i < generalShare.length; i++) {
                if (shareValue[i] == 0) {
                    caseTypeState[i] = false;
                }
                generalShare[i] = shareValue[i];
            }
            this.includeGeneralShare = true;
            this.checkScopeStatus = true;
            this.checkShareSum = true;
        }
    }

    public void setConstantShare(double regularUpper, double regularLower, double number, double special) {
        double[] decimalRules = {regularUpper, regularLower, number, special};
        int[] rules = Validation.decimalPlacesStatus(decimalRules);
        if (checkShareSum) this.checkShareSum = Validation.shareSumStatus(rules, 'd');
        if (checkScopeStatus) this.checkScopeStatus = Validation.shareScopeStatus(rules, 'd');
        if ((!checkShareSum) && (!checkScopeStatus)) {
            setConstantShare(rules[0], rules[1], rules[2], rules[3]);
        }
    }

    private static class Validation {

        private static boolean shareScopeStatus(int[] numberArray, char type) throws ValueShareException {
            for (int i : numberArray) {
                if (i < 0 || i > 100) {
                    if (type == 'i') {
                        throw new ValueShareException(0, 100);
                    } else {
                        throw new ValueShareException(0, 1);
                    }
                }
            }
            return false;
        }

        private static boolean shareSumStatus(int[] numberArray, char type) throws ValueShareException {
            int sum = 0;
            for (int i : numberArray) sum += i;
            if (sum != 100 && type == 'i') throw new ValueShareException("integer", 100);
            if (sum != 100 && type == 'd') throw new ValueShareException("decimal", 1);
            return false;
        }

        private static int[] decimalPlacesStatus(double[] numberArray) throws ValueShareException {
            int tempChecker1, tempChecker2;
            for (int i = 0; i < numberArray.length; i++) {
                tempChecker1 = ((int) (numberArray[i] * 100)) * 100000;
                tempChecker2 = (int) (numberArray[i] * 10000000);
                if (tempChecker1 != tempChecker2) {
                    throw new ValueShareException(
                        "The value entered can have a maximum of 2 decimal places\n" +
                        "* The value provided was " + numberArray[i]
                    );
                }
            }
            int[] rules = new int[4];
            for (int i = 0; i < numberArray.length; i++) {
                rules[i] = (int) (numberArray[i] * 100);
            }
            return rules;
        }

    }

    // Sets the exact length of the password
    public void setLength(int exactPasswordLength) {
        if (exactPasswordLength > 0) {
            isPasswordRange = false;
            this.exactPasswordLength = exactPasswordLength;
            this.minMaxRange[0] = -1;
            this.minMaxRange[1] = -1;
        } else {
            throw new ExactValueException("Provide value higher than zero.");
        }
    }

    public void setLength(int minCharLength, int maxCharLength) {
        throw new ExactValueException(
            "Provide only one vale to set length.\n " +
            "* To set range use .setPasswordRange() method."
        );
    }

    // Sets the range of the password
    public void setRange(int minCharLength, int maxCharLength) {
        if (minCharLength > 0 && maxCharLength > 0) {
            this.isPasswordRange = true;
            this.exactPasswordLength = -1;
            if (minCharLength < maxCharLength) {
                minMaxRange[0] = minCharLength;
                minMaxRange[1] = maxCharLength;
            } else if (minCharLength == maxCharLength) {
                setLength(minCharLength);
            } else {
                minMaxRange[0] = maxCharLength;
                minMaxRange[1] = minCharLength;
            }
        } else {
            StringBuilder errorMessage = new StringBuilder(" value cannot be less than 1");
            if (minCharLength <= 0 && maxCharLength <= 0) {
                errorMessage.insert(0, "Minimum and Maximum");
            } else if (minCharLength <= 0) {
                errorMessage.insert(0, "Minimum");
            } else {
                errorMessage.insert(0, "Maximum");
            }
            throw new RangeValueException(errorMessage.toString());
        }
    }

    public void setRange(int exactCharLength) {
        throw new RangeValueException(
                "Provide second value to set range.\n * To set exact length use .setPasswordLength() method."
        );
    }

    // Sets rules same as at the beginning of the instance
    public void setDefaultRules() {
        setConstantShare(25, 25, 25, 25);
        setLength(30);
    }

    // Generates password based on set rules or default
    public String generate() {
        if (!includeGeneralShare) {
            setRandomShare();
        }
        int currentLength;
        if (isPasswordRange) {
            currentLength = setCurrentLength();
        } else {
            currentLength = this.exactPasswordLength;
        }
        int[] caseUsage = getCaseUsage(currentLength);
        StringBuilder blob = new StringBuilder();
        int blobLength = 0;
        int randomChar, randomInsertSpace;
        for (int i = 0; i < caseUsage.length; i++) {
            int charSetLength = charCollection[i].length;
            for (int j = 0; j < caseUsage[i]; j++) {
                randomChar = (int) (random() * (charSetLength));
                randomInsertSpace = (int) (random() * (blobLength));
                blob.insert(randomInsertSpace, charCollection[i][randomChar]);
                blobLength++;
            }
        }
        return blob.toString();
    }

    private int setCurrentLength() {
        return ((int) (random() * ((minMaxRange[1] + 1) - minMaxRange[0]))) + minMaxRange[0];
    }

    private int[] getCaseUsage(int length) {
        int[] casePerType = new int[4];
        double[] caseMantissa = new double[4];
        for (int i = 0; i < casePerType.length; i++) {
            double mantissa = (generalShare[i] * 0.01) * length;
            caseMantissa[i] = mantissa - floor(mantissa);
            casePerType[i] = (int) (floor(mantissa));
        }
        int sum = 0;
        for (int len : casePerType) {
            sum += len;
        }
        while (sum < length) {
            int indexOfMax = 0;
            for (int i = 1; i < caseMantissa.length; i++) {
                if (caseMantissa[i] > caseMantissa[indexOfMax]) {
                    indexOfMax = i;
                }
            }
            casePerType[indexOfMax] += 1;
            caseMantissa[indexOfMax] = 0;
            sum++;
        }
        return casePerType;
    }

    private char[][] fillCollection() {
        char[][] collection = new char[4][];
        char[][] upperLower = Collection.fillUpperLower();
        collection[0] = upperLower[0];
        collection[1] = upperLower[1];
        collection[2] = Collection.fillNumber();
        collection[3] = Collection.fillSpecial();
        return collection;
    }

    private static class Collection {

        private static char[] fillSpecial() {
            char[] special = new char[32];
            int counter = 0;
            for (int i = 33; i < 127; i++) {
                if (i < 48) {
                    special[counter] = (char) i;
                    counter++;
                } else if (i > 57 && i < 65) {
                    special[counter] = (char) i;
                    counter++;
                } else if (i > 90 && i < 97) {
                    special[counter] = (char) i;
                    counter++;
                } else if (i > 122) {
                    special[counter] = (char) i;
                    counter++;
                }
            }
            return special;
        }

        private static char[][] fillUpperLower() {
            char[][] upperLower = new char[2][26];
            int counter = 0;
            for (int i = 65; i <= 90; i++) {
                upperLower[0][counter] = (char) i;
                upperLower[1][counter] = (char) (upperLower[0][counter] + 32);
                counter++;
            }
            return upperLower;
        }

        private static char[] fillNumber() {
            char[] number = new char[10];
            int counter = 0;
            for (int i = 48; i < 58; i++) {
                number[counter] = (char) i;
                counter++;
            }
            return number;
        }

    }

    // Include selected characters
    public void include() {}

    // Exclude selected characters
    public void exclude() {}

    // Print out characters that are used in instance
    public void showCaseCollection(String separator) {
        for (int i = 0; i < charCollection.length; i++) {
            for (int j = 0; j < charCollection[i].length; j++) {
                System.out.print(charCollection[i][j]);
            }
            System.out.print(separator);
        }
    }

    // Print out the password rules of the instance
    public void showRules() {
        StringBuilder allRules = new StringBuilder("\n");
        if (isPasswordRange) {
            allRules.append("Range: "
                + minMaxRange[0] + " - "
                + minMaxRange[1] + "\n"
            );
        } else {
            allRules.append("Length: " + exactPasswordLength + "\n");
        }
        allRules.append("Random share: " + !includeGeneralShare + "\n");
        if (caseTypeState[0]) {
            allRules.append(
                "Upper case: " + generalShare[0] + "%\n"
            );
        }
        if (caseTypeState[1]) {
            allRules.append(
                "Lower case: " + generalShare[1] + "%\n"
            );
        }
        if (caseTypeState[2]) {
            allRules.append(
                "Number case: " + generalShare[2] + "%\n"
            );
        }
        if (caseTypeState[3]) {
            allRules.append(
                "Special case: " + generalShare[3] + "%\n"
            );
        }
        System.out.println(allRules.toString());
    }

}