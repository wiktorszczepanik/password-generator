import PasswordGenerator.*;
public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();

        password.setLength(27); // Set exact length of password
        password.setRange(20, 30); // Set password length range

        /* Set percentage use between:
        upper case (1) | lower case (2) | number case (3) | special case (4) */
        password.setConstantShare(30, 15, 30, 15); // 30% upper, 15% lower
        password.setConstantShare(0.3, 0.15, 0.3, 0.15); // Same as above but decimal are used

        password.setRandomShare(); // Absolute random usage
        password.setRandomShare(10); // Random but minimum value for each type is 10%
        password.setRandomShare(0.1); // Same as above but decimal are used
        password.setRandomShare(20, 20, 20, 20); // Random with minimum value usage for each type
        password.setRandomShare(0.2, 0.2, 0.2, 0.2); // Same as above but decimal are used

        password.setDefaultRules();
        password.setDefaultCollection();

        char[] array = {'x', 'y', 'z'}; // Sample array

        password.include(' '); // Include space character
        password.include('X', "upper"); // Appends 'X' to upper type set
        password.include('X', "lower", true); // "true" allow duplicates in set
        password.include('X', 1); // Same as above (1 = "upper")
        password.include('X', 2, true); // Same as above (2 = "lower")

        password.include(array); // Automatically appends array
        password.include(array, true); // Disregard type consistency checker of array
        password.include(array, "number"); // Appends array to "number" set
        password.include(array, "number", true); // "true" allow duplicates in set
        password.include(array, 3); // // Same as above (3 = "number")
        password.include(array, 3, true); // "true" allow duplicates ("Number")

        password.exclude(' '); // Exclude space character
        password.exclude('X', "upper"); // Delete 'X' from upper type set
        password.exclude('X', "lower", true); // "true" disregards not existing characters
        password.exclude('X', 1); // Same as above (1 = "upper")
        password.exclude('X', 2, true); // Same as above (2 = "lower")

        password.exclude(array); // Automatically delete provided characters form set
        password.exclude(array, true); // Disregard type consistency checker of array
        password.exclude(array, "number"); // Delete array from upper type set
        password.exclude(array, "number", true); // "true" disregards not existing characters
        password.exclude(array, 3); // Same as above (3 = "number")
        password.exclude(array, 3, true); // "true" disregards not existing characters

        password.generate(); // Generates password based on given rules

        password.length(); // return length of current password
        password.showCaseCollection("\t"); // Print characters collection
        password.showRules(); // Print all rules of the instance

        // test all
        // Add readme.md
    }
}