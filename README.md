## Password Generator
The Password Generator is a Java library designed to create random passwords with flexible rules and customization options. It allows you to specify the length of the password, set length ranges, and define the percentage distribution of character types (upper case, lower case, numbers, and special characters). The library also allows the inclusion and exclusion of selected characters. 
### Getting Started
To get started, create an instance of the **PasswordGenerator**.
```Java
PasswordGenerator password = new PasswordGenerator();
```
### Password Length
You can set the exact length of the password or define a range. By default, password length is set to 30.
```Java
password.setLength(27); // Set exact length of password
password.setRange(20, 30); // Set password length range
```
### Character Type Distribution
You can specify the percentage distribution for each character type (*upper*, *lower*, *number*, *special*), using integers or decimals. Integers must add up to **100** and decimals to **1.0**. By default, the password distribution is set at 25% for each type. 
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
You can include or exclude specific characters and types:
```Java
char[] array = {'x', 'y', 'z'}; // Sample array
password.include(' ');               // Include space character
password.include('X', "upper");       // Append 'X' to upper type set
password.include('X', "lower", true); // Allow duplicates in set
password.include(array);              // Automatically appends array
password.exclude(' ');               // Exclude space character
password.exclude('X', "upper");       // Delete 'X' from upper type set
```
#### Method arguments that can be used interchangeably
| Oryginal  | Equivalent |
|:---------:|:----------:|
|  "upper"  | 1 |
|  "lower"  | 2 |
| "number"  | 3 |
| "special" | 4 |
### Generating Password
To generate a password based on the defined rules, you must assign the result to a variable of type **String**.
```Java
String samplePassword = password.generate();
```
### Retrieving Information
For password generating rules *length()* and *range()* must be assigned to variables. Other methods print values to the console. In *showCaseCollection("\n")* it is necessary to specify a character to separate groups of characters. 
```Java
int length = password.length(); // Return length rule of current password
int[] range = password.range(); // Return range rule of current password
password.showCaseCollection("\t"); // Print characters collection ("\t" - separates types arrays)
password.showRules(); // Prints character distribution
```
### Default Rules and Collections
If you want to return to the default setting for character length and distribution you can by using *setDefaultRules();*. For default characters you can apply *setDefaultCollection();*.This method retrieves the basis values from the ASCII table.
```Java
password.setDefaultRules(); // Default length and character type distribution
password.setDefaultCollection(); // Default collections (upper, lower, number special)
```