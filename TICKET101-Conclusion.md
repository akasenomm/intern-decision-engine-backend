## What was done well?

**Requirements**
- **Credit scoring**: The credit scoring algorithm is implemented very beautifully inside the method highestValidLoanAmount (mathematical proof below). Multiplying credit modifier with the loan period returns the maximum approved amount by essentially fixing the credit score to be exactly 1, i.e any higher amount would not be approved. However, it might not be clear at first and one might miss it when trying to verify system requirements or debug. <br> ![img.png](img.png)
- **Return maximum sum**: The decision engine returns the maximum approved sum correctly (which is exactly the modifier multiplied with the loan period). While the maximum approved sum is not displayed to the user, it was only specified what the decision engine should output and not what the user should see.
- **Find suitable period**: Engine finds the suitable period correctly. However, it found the suitable period by incrementing current loan period and checking if max approved sum was at least minimum allowed (2000). I simplified it to just minumum allowed divided by credit modifier, which is more efficient and does the same thing.
- **Frontend**: UI looks great and is functional. It did have one mistake that I will point out in the next section. 
## What can be done better?

- **Shortcoming**: The code could benefit from better use of Java’s inheritance. The way the intern did it makes the code less readable and maintainable. For instance, multiple exceptions in the decision engine could be generalized to a parent exception class. The intern created several identical exception classes, violating the DRY principle. This could be improved by creating a parent class, DecisionEngineException, for common functionality. This approach enhances readability, maintainability, and adherence to DRY principles.  
- **Frontend**: The loan period slider had the minimum value displayed as 6 months when per the requirements the minimum loan period is 12 months.