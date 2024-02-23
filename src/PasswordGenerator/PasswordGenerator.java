package PasswordGenerator;

public class PasswordGenerator {

    private boolean isPasswordRange;
    private int exactPasswordLength;
    private int[] minMaxRange;

    private boolean[] caseType;
    private boolean includeGeneralShare;
    private int[] generalShare;

    private boolean checkScopeStatus, checkShareSum;

    // Constant value
    private static final int disregardValue = -1;

    public PasswordGenerator() {

        // Length / range of exit password
        this.isPasswordRange = false;
        this.exactPasswordLength = 25;
        this.minMaxRange = new int[] {-1, -1};

        /* Array collects percentage use between:
        regular characters (upper | lower) | numbers | special characters */
        this.includeGeneralShare = true; // random value
        this.caseType = new boolean[] {true, true, true, true};
        this.generalShare = new int[] {25, 25, 25, 25};

        // Internal additional
        this.checkScopeStatus = true;
        this.checkShareSum = true;

    }

    public PasswordGenerator(String templateFilePath) {};

    // Sets share between types of the characters in password
    public void setRandomShare() {
        int[] share = new int[4];
        int maxValue = 100;
        int tempRandom;
        for (int i = 0; i < share.length - 1; i++) {
            tempRandom = (int) (Math.random() * maxValue + 1);
            maxValue -= tempRandom;
            this.generalShare[i] = tempRandom;
            if (tempRandom == 0) this.caseType[i] = false;
            if (maxValue == 0) {
                for (int j = i + 1; j < share.length - 1; j++) {
                    this.generalShare[j] = 0;
                    this.caseType[j] = false;
                }
                break;
            }
        }
        this.generalShare[3] = maxValue;
        if (maxValue == 0) this.caseType[3] = false;
        this.includeGeneralShare = false;
    }

    public void setConstantShare(int regularUpper, int regularLower, int number, int special) throws ValueShareException {
        int[] shareValue = {regularUpper, regularLower, number, special};
        if (checkScopeStatus) shareScopeStatus(shareValue, 'i');
        if (checkShareSum) shareSumStatus(shareValue, 'i');
        if ((!checkShareSum) && (!checkScopeStatus)) {
            for (int i = 0; i < generalShare.length; i++) {
                if (shareValue[i] == 0) {caseType[i] = false;}
                generalShare[i] = shareValue[i];
            }
            this.includeGeneralShare = true;
            this.checkScopeStatus = true;
            this.checkShareSum = true;
        }
    }

    public void setConstantShare(double regularUpper, double regularLower, double number, double special) throws ValueShareException {
        double[] decimalRules = {regularUpper, regularLower, number, special};
        int[] rules = decimalPlacesStatus(decimalRules);
        if (checkShareSum) shareSumStatus(rules, 'd');
        if (checkScopeStatus) shareScopeStatus(rules, 'd');
        if ((!checkShareSum) && (!checkScopeStatus)) {
            setConstantShare(rules[0], rules[1], rules[2], rules[3]);
        }
    }

    private void shareScopeStatus(int[] numberArray, char type) throws ValueShareException {
        for (int i : numberArray) {
            if (i < 0 || i > 100) {
                if (type == 'i') {
                    throw new ValueShareException(0, 100);
                } else {
                    throw new ValueShareException(0, 1);
                }
            }
        }
        this.checkScopeStatus = false;
    }

    private void shareSumStatus(int[] numberArray, char type) throws ValueShareException {
        int sum = 0;
        for (int i = 0; i < numberArray.length; i++) {
            sum += numberArray[i];
        }
        if (sum != 100 && type == 'i') throw new ValueShareException("integer", 100);
        if (sum != 100 && type == 'd') throw new ValueShareException("decimal", 1);
        this.checkShareSum = false;
    }

    private int[] decimalPlacesStatus(double[] numberArray) throws ValueShareException {
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

    // Sets the exact length of the password
    public void setLength(int exactPasswordLength) throws ExactValueException {
        if (exactPasswordLength > 0) {
            isPasswordRange = false;
            this.exactPasswordLength = exactPasswordLength;
            this.minMaxRange[0] = disregardValue;
            this.minMaxRange[1] = disregardValue;
        } else {
            throw new ExactValueException("Provide value higher than zero.");
        }
    }

    public void setExactCharLength(int minCharLength, int maxCharLength) throws ExactValueException {
        throw new ExactValueException(
                "Provide only one vale to set length.\n * To set range use .setPasswordRange() method."
        );
    }


    // Sets the range of the password
    public void setRange(int minCharLength, int maxCharLength) {
        if (minCharLength > 0 && maxCharLength > 0) {
            this.isPasswordRange = true;
            this.exactPasswordLength = disregardValue;
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

    public void setRange(int exactCharLength) throws RangeValueException {
        throw new RangeValueException(
                "Provide second value to set range.\n * To set exact length use .setPasswordLength() method."
        );
    }

    public void generateTemplateFile(String templateFilePath) {
        //
    }

    public void readTemplateFile(String templateFilePath) {
        //
    }

    public String generate() {
        // ogs
        return null;
    }

    // print out the password rules of the instance
    @Override
    public String toString() {
        StringBuilder allRules = new StringBuilder("Password rules: \n");
        if (isPasswordRange) {
            allRules.append("Range: "
                + minMaxRange[0] + " - "
                + minMaxRange[1] + "\n"
            );
        } else {
            allRules.append("Length: " + exactPasswordLength + "\n");
        }
        allRules.append("Random share: " + !includeGeneralShare + "\n");
        if (caseType[0]) {
            allRules.append(
                "Upper case: " + generalShare[0] + "%\n"
            );
        }
        if (caseType[1]) {
            allRules.append(
                "Lower case: " + generalShare[1] + "%\n"
            );
        }
        if (caseType[2]) {
            allRules.append(
                "Number case: " + generalShare[2] + "%\n"
            );
        }
        if (caseType[3]) {
            allRules.append(
                "Special case: " + generalShare[3] + "%\n"
            );
        }
        return allRules.toString();
    }
}