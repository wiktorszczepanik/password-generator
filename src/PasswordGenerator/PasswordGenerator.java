package PasswordGenerator;

public class PasswordGenerator {

    private boolean isPasswordRange;
    private int exactPasswordLength;
    private int[] minMaxRange;

    private boolean[] caseType;
    private boolean includeGeneralShare;
    private int[] generalShare;

    private boolean checkElementStatus, checkShareSum;

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
        this.checkElementStatus = true;
        this.checkShareSum = true;

    }

    public PasswordGenerator(String templateFilePath) {};

    // Sets share between types of the characters in password
    public void setGeneralShare(int regularUpper, int regularLower, int number, int special) throws ValueShareException {
        int[] shareValue = {regularUpper, regularLower, number, special};
        if (checkElementStatus) {shareElementsStatus(shareValue);}
        if (checkShareSum) {shareSumStatus(shareValue);}
        if (!checkShareSum) {
            for (int i = 0; i < generalShare.length; i++) {
                if (shareValue[i] == 0) {caseType[i] = false;}
                generalShare[i] = shareValue[i];
            }
            this.checkElementStatus = true;
            this.checkShareSum = true;
        } else {
            throw new ValueShareException("integers", 100);
        }
    }

    public void setGeneralShare(double regularUpper, double regularLower, double number, double special) throws ValueShareException {
        if (checkShareSum) {
            shareSumStatus(regularUpper, regularLower, number, special);
        }
        if (checkElementStatus) {
            shareElementsStatus(regularUpper, regularLower, number, special);
        }
        double shareSum = regularUpper + regularLower + number + special;
        if (!checkShareSum) {
            double[] decimalRules = {regularUpper, regularLower, number, special};
            int[] rules = new int[4];
            for (int i = 0; i < decimalRules.length; i++) {
                rules[i] = (int) (decimalRules[i] * 100);
            }
            setGeneralShare(rules[0], rules[1], rules[2], rules[3]);
        } else {
            throw new ValueShareException("decimal", 1);
        }
    }

    public void setGeneralShare(float regularUpper, float regularLower, float number, float special) {
        shareElementsStatus(regularUpper, regularLower, number, special);
        shareSumStatus(regularUpper, regularLower, number, special);
        setGeneralShare(regularUpper, regularLower, number, special);
    }

    private void shareElementsStatus(int[] numberArray) throws ValueShareException {
        for (int i : numberArray) {
            if (i < 0 || i > 100) {
                throw new ValueShareException(0, 100);
            }
        }
    }

    private void shareElementsStatus(double regularUpper, double regularLower, double number, double special) throws ValueShareException {
        if ((regularUpper < 0 || regularUpper > 100) ||
            (regularLower < 0 || regularLower > 100) ||
            (number < 0 || number > 100) ||
            (special < 0 || special > 100)) {
            throw new ValueShareException(0, 1);
        }
        this.checkElementStatus = false;
    }

    private void shareSumStatus(int[] numberArray) throws ValueShareException {
        int sum = 0;
        for (int i = 0; i < numberArray.length; i++) {
            sum += numberArray[i];
        }
        if (sum != 100) {
            throw new ValueShareException("integer", 100);
        }
    }

    private void shareSumStatus(double regularUpper, double regularLower, double number, double special) throws ValueShareException {
        double sum = regularUpper + regularLower + number + special;
        if (sum != 1.0) {
            throw new ValueShareException("decimal", 1);
        }
        this.checkShareSum = false;
    }

    // Sets the exact length of the password
    public void setPasswordLength(int exactPasswordLength) throws ExactValueException {
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
    public void setPasswordRange(int minCharLength, int maxCharLength) {
        if (minCharLength > 0 && maxCharLength > 0) {
            this.isPasswordRange = true;
            this.exactPasswordLength = disregardValue;
            if (minCharLength < maxCharLength) {
                minMaxRange[0] = minCharLength;
                minMaxRange[1] = maxCharLength;
            } else if (minCharLength == maxCharLength) {
                setPasswordLength(minCharLength);
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

    public void setPasswordRange(int exactCharLength) throws RangeValueException {
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

    // print out the password rules of the instance
    @Override
    public String toString() {
        StringBuilder allRules = new StringBuilder("Password rules: \n");
        if (isPasswordRange) {
            allRules.append("Password range: "
                + minMaxRange[0] + ", "
                + minMaxRange[1] + "\n"
            );
        } else {
            allRules.append("Password length: " + exactPasswordLength);
        }
        return allRules.toString();
    }
}