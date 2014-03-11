package edu.ucsb.cs56.projects.utilities.grapher;

import java.util.ArrayList;
/**
   A class that wraps up a custom quadratic function as an implementer of
   the FunctionR1R1 interface. Quadratic takes coefficients from input.
   @author Jenny So
   @version CS56, Winter 2014, Project
 */
public class CustomQuadraticFunction extends ArrayList<Integer> implements FunctionR1R1 {

    /** 
	Constructs from int array of coefficients
	The coefficients are listed in order from lowest to highest degree

	@param coeffsHighToLow array of coefficients from lowest to highest degree
    */
    public CustomQuadraticFunction(int [] coeffsLowToHigh){
	int j = 0;

	// Skip any trailing zeros after the highest power
	for(j = coeffsLowToHigh.length-1; j>0; j--){
	    if(coeffsLowToHigh[j] != 0)
		break;
	}
	// Take coeffs from low to high
	for(int i = 0; i < j+1; i++)
	    this.add(coeffsLowToHigh[i]);
    }

    /**
       Evaluate the function at an independent variable.
       The solution depends on the custom function defined by the user.

       @param indVar the independent variable.
       @return the value of the function.
     */
    public double evaluate(double indVar) {
	double solution = 0.0;
	double product = 1.0; //because x^0 = 1

	// For each coefficient
        for(int i = 0; i<this.size(); i++){

	    // Evaluate x^i
	    for(int j = 1; j<=i; j++){
		product *= indVar;
	    }

	    // Evaluate coefficient times x^i
	    solution += product*this.get(i);

	    // Reset product to 1
	    product = 1.0;
	}
	
	return solution;
    }

    /**
       Check if a number is within the domain of this function.
       In this case, any number is within the domain of the function.
       @param arg the value to check
       @return true if the value is within the domain of the function.
     */
    public boolean isInDomain(double arg) {
	return true;
    }

    public String toString() {
        String poly = "0";
	// Single constant
	if(this.size() == 1){
	    poly = Integer.toString(this.get(0));
	    return "f(x)="+poly;
	}

	// Degree 1
	if(this.size() == 2){
	    if(this.get(1) == 1)
		poly = "x";
	    else if(this.get(1) == -1)
		poly = "-x";
	    else if(this.get(1) > 0)
		poly = this.get(1) + "x";

	    if(this.get(0) > 0)
		poly += " + " + this.get(0);
	    if(this.get(0) < 0)
		poly += " - " + (-1)*this.get(0);

	    return "f(x)="+poly;
	}

	// Configure initial term
	if(this.get(this.size() -1) == 1){
	    poly = "x^" + (this.size()-1);
	}
	else if(this.get(this.size() -1) == -1){
	    poly = "-x^" + (this.size()-1);
	}
	else if(this.get(this.size()-1) != 0){
	    poly = this.get(this.size()-1) + "x^" + (this.size()-1);
	}

	// Run through things without "^" character
	for(int i = (this.size() -2); i>=0; i--){
	    // Skip any 0 terms
	    if(this.get(i) == 0){
		continue;
	    }
	    // For those that needs "^" symbol
	    if(i > 1){
		if(this.get(i) == 1)
		    poly += (" + x^" + i);
		else if(this.get(i) > 0)
		    poly += (" + " + this.get(i) + "x^" + i);
		else if(this.get(i) < 0)
		    poly += (" - " + (-1)*this.get(i) + "x^" + i);
	    }
	    // For a constant
	    if(i == 0){
		if(this.get(i) > 0)
		    poly += (" + " + this.get(i));
		else if(this.get(i) < 0)
		    poly += (" - " + (-1)*this.get(i));
	    }
	    // For which no "^" symbol is needed
	    if(i == 1){
		if(this.get(i) == 1)
		    poly += (" + x");
		else if(this.get(i) > 0)
		    poly += (" + " + this.get(i) + "x");
		else if(this.get(i) < 0)
		    poly += (" - " + (-1)*this.get(i) + "x");	
	    }
	}
	return "f(x)="+poly; // Return full string
    }
}