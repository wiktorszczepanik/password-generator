## Password Generator
The Password Generator is a Java library designed to create pseudorandom passwords with flexible rules and customization options. It allows you to specify the length of the password, set length ranges, and define the percentage distribution of character types (upper case, lower case, numbers, and special characters). The library also allows the inclusion and exclusion of selected characters. 
### Getting Started
To get started, create an instance of the **PasswordGenerator**.
```Java
PasswordGenerator password = new PasswordGenerator();
```
### Password Length
You can set the exact length of the password or define a range. By default, password length is set to **30**.
```Java
password.setLength(27); // Set exact length of password
password.setRange(20, 30); // Set password length range
```
### Character Type Distribution
You can specify the percentage distribution for each character type (*upper*, *lower*, *number*, *special*), using integers or decimals. Integers must add up to **100** and decimals to **1.0**. By default, the password distribution is set at **25%** for each type. 
```Java
password.setConstantShare(30, 15, 30, 15); // 30% upper, 15% lower, 30% number, 15% special
password.setConstantShare(0.3, 0.15, 0.3, 0.15); // Same as above using decimals
```
You can also set absolute random distribution or distribution with minimum values for each case type. As with setting the regular distribution integers and decimals parameters can also be used.
```Java
password.setRandomShare(); // Absolute random usage between character types 
password.setRandomShare(10); // Random with minimum value of 10% for each type
password.setRandomShare(20, 20, 20, 20); // Random with minimum value (20%) usage for each type
```
### Including and Excluding Characters
You can include or exclude specific characters in all groups (*upper, lower, number, special*).
The **include()** method adds characters or character arrays to the character sets used for generating passwords, while the **exclude()** method removes characters or character arrays from these sets.
#### Include examples
```Java
char[] array = {'x', 'y', 'z'}; // Sample array
password.include(' '); // Automatically include space character in "special" set
password.include('1', "upper"); // Append '1' to "upper" type set
password.include('X', "lower", true); // Allow duplicates in the "lower" set
password.include(array); // Automatically appends array (in that case to "lower" set)
```
#### Exclude examples
```Java
char[] array = {'&', '^', '~'}; // Sample array
password.exclude(' '); // Exclude space character from "special" set
password.exclude('3', "number"); // Delete '3' from "number" type set
password.exclude('X', "special", true); // Allow ignoring non-existent characters 
password.exclude(array); // Automatically delete provided characters (in that case from "special" set)
```
#### Method arguments that can be used interchangeably:
| Original  | Equivalent |
|:---------:|:----------:|
|  "upper"  |     1      |
|  "lower"  |     2      |
| "number"  |     3      |
| "special" |     4      |
### Generating Password
To generate a password based on the defined rules, you must assign the result to a variable of type **String** as shown below. The full source code for the basic password generation program can be found in the *src/Main.java* file.
```Java
String samplePassword = password.generate();
```
### Retrieving Information
For password generating rules **length()** and **range()** must be assigned to variables. Other methods print values to the console. In **showCaseCollection("\n")** it is necessary to specify a character to separate groups of characters. 
```Java
int length = password.length(); // Return length rule of current password
int[] range = password.range(); // Return range rule of current password
password.showCaseCollection("\t"); // Print characters collection
password.showRules(); // Prints character distribution
```
### Default Rules and Collections
If you want to return to the default setting for character length and distribution you can by using **setDefaultRules();**. For default characters you can apply **setDefaultCollection();**.This method retrieves the basis values from the ASCII table.
```Java
password.setDefaultRules(); // Default length and character type distribution
password.setDefaultCollection(); // Default collections (upper, lower, number special)
```