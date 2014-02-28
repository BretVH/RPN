package RPN;

/*
 * RPNCalculator.java
 * version 1.0 Completed 5/22/2012
 */


import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * RPNCalculator
 *
 * 
 *
 * @author Bret A. Van Hof
 * @version 1.0
 *
 * Compiler: Java 1.71 
 * OS: Windows 7 
 * Hardware: PC 
 *
 * May 22 2012
 * BVH completed v 1.0
 */ 
public class RPNCalculator
{
    public static final int ROWS = 4;
    public static final int COLS = 8;
    public static final int NUMBER_OF_REGISTERS = 10;
    public static final int BINARY = 2;
    protected Deque<Double> theStack;   
    protected LinkedList<String> instructions;
    protected double[] register;
    
    /**
     * Creates a new instance of RPNCalculator
     */
    public RPNCalculator()
    {
       theStack = new LinkedList<>();
       instructions = new LinkedList<>();
       register = new double[10];
    }
    
    /**
     * method: binaryOperation
     * pops two elements off the stack and returns a String containing the two
     * elements popped
     * @return a String containing the top two elements of the stack separated 
     * by a space
     */
    public String binaryOperation()
    {
        return theStack.pop() + " " + theStack.pop();
    }
    
    /**
     * method: testBinary
     * tests if the stack contains at least 2 elements
     * @return true if the stack contains at least 2 elements, false otherwise
     */
    public boolean testBinary()
    {
        return theStack.size() >= BINARY;
    }
    
    /**
     * method: add
     * Calls testBinary to ensure that the divide operation is possible then
     * calls binaryOperation to pop the first two elements off the stack
     * it returns the first element plus the second 
     * @return double: the first element popped off the stack plus the second 
     */
    public double add()
    {
        String aString = "";
        if(testBinary())
           aString = binaryOperation();
        Scanner in = new Scanner(aString);
        double first = in.nextDouble();
        double second = in.nextDouble();
        theStack.push(first + second);
        return first + second;
    }
    
    /**
     * method: subtract
     * Calls testBinary to ensure that the divide operation is possible then
     * calls binaryOperation to pop the first two elements off the stack
     * it returns the value obtained by subtracting the first element 
     * from the second  
     * @return double: the second element popped off the stack minus the first
     */
    public double subtract()
    {
        String aString = "";
        if(testBinary())
           aString = binaryOperation();
        Scanner in = new Scanner(aString);
        double first = in.nextDouble();
        double second = in.nextDouble();
        theStack.push(second - first);
        return second - first;
    }
    
    /**
     * Method: multiply
     * Calls testBinary to ensure that the divide operation is possible then
     * calls binaryOperation to pop the first two elements off the stack
     * it returns the first element multiplied by the second 
     * @return double: the top two elements popped off the stack multiplied together
     */
    public double multiply()
    {
        String aString = "";
        if(testBinary())
           aString = binaryOperation();
        Scanner in = new Scanner(aString);
        double first = in.nextDouble();
        double second = in.nextDouble();
        theStack.push(first * second);
        return first * second;
    }
    
    /**
     * Method: divide
     * Calls testBinary to ensure that the divide operation is possible then
     * calls binaryOperation to pop the first two elements off the stack
     * it returns the second element divided by the first 
     * @return double: the second value popped off the stack divided by the first 
     * element popped off the stack
     */
    public double divide()
    {
        String aString = "";
        if(testBinary())
           aString = binaryOperation();
        Scanner in = new Scanner(aString);
        double first = in.nextDouble();
        double second = in.nextDouble();
        theStack.push(second / first);
        return second / first;
    }
    
    /**
     * Method: modOperator
     * calls testBinary to ensure that the mod operation is possible then
     * calls binaryOperation to pop the top two elements off the stack 
     * it returns the 2nd element popped off the stack % the 1st element 
     * popped off the stack
     * @return double value of the 2nd element popped off the stack % the 
     * first element popped off the stack
     */
    public double modOperator()
    {
       String aString = "";
        if(testBinary())
           aString = binaryOperation();
        Scanner in = new Scanner(aString);
        double first = in.nextDouble();
        double second = in.nextDouble();
        theStack.push(second % first);
        return second % first; 
    }
    
    /**
     * Method: exponentiation
     * calls testBinary to ensure that the exponentiation is possible then
     * calls binaryOperation to pop the top two elements off the stack
     * it then returns the double representing the 2nd element popped off the
     * stack raised to the power of the first element popped off the stack
     * @return double primitive of the 2nd element that was in the stack raised 
     * to the power of the element that was the top element in the stack 
     */
    public double exponentiation()
    {
        String aString = "";
        if(testBinary())
           aString = binaryOperation();
        Scanner in = new Scanner(aString);
        double first = in.nextDouble();
        double second = in.nextDouble();
        theStack.push(Math.pow(second, first));
        return Math.pow(second, first);
    }
    
    /**
     * Method: up
     * takes the top element of the stack and makes it the bottom element
     */
    public void up()
    {
        double last = theStack.pop();
        theStack.addLast(last);
    }
    
    /**
     * Method: down
     * takes the bottom element of the stack and makes it the top element
     */
    public void down()
    {
        double first = theStack.pollLast();
        theStack.push(first);
    }
}
