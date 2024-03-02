package PasswordGenerator;
import java.util.ArrayList;

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

        private static boolean shareScopeStatus(int[] numberArray, char type) {
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

        private static boolean shareSumStatus(int[] numberArray, char type) {
            int sum = 0;
            for (int i : numberArray) sum += i;
            if (sum != 100 && type == 'i') throw new ValueShareException("integer", 100);
            if (sum != 100 && type == 'd') throw new ValueShareException("decimal", 1);
            return false;
        }

        private static int[] decimalPlacesStatus(double[] numberArray) {
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
        setLength(30);
        setConstantShare(25, 25, 25, 25);
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
    public void include(char character, int type) {
        include(character, type, false);
    }

    public void include(char character, int type, boolean allowDuplicate) {
        if (!allowDuplicate) Size.checkCharDuplicate(character, charCollection[type - 1]);
        if (type >= 1 && type <= 4) {
            charCollection[type - 1] = Size.addCharacter(character, charCollection[type - 1]);
        } else {
            throw new IncludeExcludeException(
                "Provided number for collection type do not exist" +
                "* Available options are: 1, 2, 3, 4"
            );
        }
    }

    public void include(char character, String type) {
        include(character, type, false);
    }

    public void include(char character, String type, boolean allowDuplicate) {
        move(character, type, allowDuplicate, 'i', true);
    }

    public void include(char[] list, int type) {
        include(list, type, false);
    }

    public void include(char[] list, int type, boolean allowDuplicate) {
        if (!allowDuplicate) Size.checkListDuplicate(list, charCollection[type - 1]);
        if (type >= 1 && type <= 4) {
            charCollection[type - 1] = Size.addCharList(list, charCollection[type - 1]);
        } else {
            throw new IncludeExcludeException(
                "Provided number for collection type do not exist" +
                "* Available options are: 1, 2, 3, 4"
            );
        }
    }

    public void include(char[] list, String type) {
        include(list, type, false);
    }

    public void include(char[] list, String type, boolean allowDuplicate) {
        move(list, type, allowDuplicate, 'i', false);
    }

    // Exclude selected characters
    public void exclude(char character, int type) {
        exclude(character, type, false);
    }

    public void exclude(char character, int type, boolean allowNotExisting) {
        int innerArray = type - 1;
        if (!allowNotExisting) Size.checkExisting(character, charCollection[innerArray]);
        if (type >= 1 && type <= 4) {
            int[] position = Size.checkPosition(character, charCollection[innerArray]);
            charCollection[innerArray] = Size.finalExclude(position, charCollection[innerArray]);
        } else {
            throw new IncludeExcludeException(
                "Provided number for collection type do not exist" +
                "* Available options are: 1, 2, 3, 4"
            );
        }
    }

    public void exclude(char character, String type) {
        exclude(character, type, false);
    }

    public void exclude(char character, String type, boolean allowNotExisting) {
        move(character, type, allowNotExisting, 'e', true);
    }

    public void exclude(char[] list, int type) {
        exclude(list, type, false);
    }

    public void exclude(char[] list, int type, boolean allowNotExisting) {
        int innerArray = type - 1;
        if (!allowNotExisting) Size.checkExisting(list, charCollection[innerArray]);
        if (type >= 1 && type <= 4) {
            int[] position = Size.checkPosition(list, charCollection[innerArray]);
            charCollection[innerArray] = Size.finalExclude(position, charCollection[innerArray]);
        } else {
            throw new IncludeExcludeException(
                    "Provided number for collection type do not exist" +
                            "* Available options are: 1, 2, 3, 4"
            );
        }
    }

    public void exclude(char[] list, String type) {
        exclude(list, type, false);
    }

    public void exclude(char[] list, String type, boolean allowNotExisting) {
        move(list, type, allowNotExisting, 'e', false);
    }

    private void move(char character, String type, boolean state, char action, boolean single) {
        char[] oneItem = {character};
        move(oneItem, type, state, action, single);
    }

    private void move(char[] list, String type, boolean state, char action, boolean single) {
        switch (type) {
            case "upper":
                if (single) {
                    if (action == 'i') include(list[0], 1, state);
                    if (action == 'e') exclude(list[0], 1, state);
                } else {
                    if (action == 'i') include(list, 1, state);
                    if (action == 'e') exclude(list, 1, state);
                }
                break;
            case "lower":
                if (single) {
                    if (action == 'i') include(list[0], 2, state);
                    if (action == 'e') exclude(list[0], 2, state);
                } else {
                    if (action == 'i') include(list, 2, state);
                    if (action == 'e') exclude(list, 2, state);
                }
                break;
            case "number":
                if (single) {
                    if (action == 'i') include(list[0], 3, state);
                    if (action == 'e') exclude(list[0], 3, state);
                } else {
                    if (action == 'i') include(list, 3, state);
                    if (action == 'e') exclude(list, 3, state);
                }
                break;
            case "special":
                if (single) {
                    if (action == 'i') include(list[0], 4, state);
                    if (action == 'e') exclude(list[0], 4, state);
                } else {
                    if (action == 'i') include(list, 4, state);
                    if (action == 'e') exclude(list, 4, state);
                }
                break;
            default:
                throw new IncludeExcludeException(
                    "Provided String for collection type do not exist" +
                    "*Available options are: upper, lower, number, special "
                );
        }
    }

    private static class Size {
        
        private static char[] finalExclude(int[] position, char[] collection) {
            if (position.length == 1) return finalOneCharExclude(position[0], collection);
            else return finalListCharExclude(position, collection);
        }

        private static char[] finalOneCharExclude(int position, char[] collection) {
            char[] newCollection = new char[collection.length - 1];
            int counter = 0;
            for (int i = 0; i < collection.length; i++) {
                if (i != position) {
                    newCollection[counter] = collection[i];
                    counter++;
                }
            }
            return newCollection;
        }

        private static char[] finalListCharExclude(int[] position, char[] collection) {
            char[] newCollection = new char[collection.length - position.length];
            int counter = 0;
            boolean add;
            for (int i = 0; i < collection.length; i++) {
                add = true;
                for (int j = 0; j < position.length; j++) {
                    if (i == position[j]) add = false;
                }
                if (add) {
                    newCollection[counter] = collection[i];
                    counter++;
                }
            }
            return newCollection;
        }

        private static int[] checkPosition(char character, char[] collection) {
            ArrayList<Integer> position = new ArrayList<Integer>();
            for (int i = 0; i < collection.length; i++) {
                if (character == collection[i]) {
                    position.add(i);
                }
            }
            int[] array = position.stream().mapToInt(i -> i).toArray();
            return array;
        }

        private static int[] checkPosition(char[] list, char[] collection) {
            ArrayList<Integer> position = new ArrayList<Integer>();
            for (int i = 0; i < collection.length; i++) {
                for (int j = 0; j < list.length; j++) {
                    if (list[j] == collection[i]) {
                        position.add(i);
                        break;
                    }
                }
            }
            int[] array = position.stream().mapToInt(i -> i).toArray();
            return array;
        }

        private static void checkCharDuplicate(char character, char[] collection) {
            for (int i = 0; i < collection.length; i++) {
                if (character == collection[i]) {
                    throw new IncludeExcludeException(
                        "Provided character already exist in case collection."
                    );
                }
            }
        }

        private static void checkListDuplicate(char[] characters, char[] collection) {
            for (int i = 0; i < characters.length; i++) {
                for (int j = 0; j < collection.length; j++) {
                    if (characters[i] == collection[j]) {
                        throw new IncludeExcludeException(
                            "Provided character in array already exist in case collection."
                        );
                    }
                }
            }
        }

        private static char[] addCharacter(char character, char[] collection) {
            char[] extendedCollection = new char[collection.length + 1];
            for (int i = 0; i < extendedCollection.length - 1; i++) {
                extendedCollection[i] = collection[i];
            }
            extendedCollection[extendedCollection.length - 1] = character;
            return extendedCollection;
        }

        private static char[] addCharList(char[] list, char[] collection) {
            char[] extendCollection = new char[collection.length + list.length];
            int counter = 0;
            for (int i = 0; i < collection.length; i++) {
                extendCollection[i] = collection[i];
                counter++;
            }
            for (int i = 0; i < list.length; i++) {
                extendCollection[counter] = list[i];
                counter++;
            }
            return extendCollection;
        }

        private static void checkExisting(char character, char[] collection) {
            boolean exist = false;
            for (char collCase : collection) {
                if (collCase == character) {
                    exist = true;
                    break;
                }
            }
            if (!exist) throw new IncludeExcludeException(
                "Provided character do not exits in collection"
            );
        }

        private static void checkExisting(char[] character, char[] collection) {
            boolean exist = false;
            for (int i = 0; i < collection.length; i++) {
                for (int j = 0; j < character.length; j++) {
                    if (collection[i] == character[j]) {
                        exist = true;
                        break;
                    }
                }
            }
            if (!exist) throw new IncludeExcludeException(
                "Provided character do not exits in collection"
            );
        }

    }

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