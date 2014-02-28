package RPN;

/*  RPNForm.java */



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ListIterator;
import javax.swing.*;
/**
 * GUI for Reverse Polish Notation Calculator
 * @author Paul Bladek, Bret A. Van Hof
 */
public class RPNForm extends JFrame
{
    
    public static final int FRAME_WIDTH = 660;
    public static final int FRAME_HEIGHT = 330;
    public static final String MacroFile = "macroFile.txt";
    private Container contentPane;
    private JPanel displayPanel;
    private JLabel RPNLabel; 
    private JTextField displayTextField;
    private JPanel buttonPanel;
    private JButton[][] buttonGrid;
    
    private RPNCalculator calc;
    private boolean helpMode = false;
    private boolean recordMode = false;
    private boolean msgOn = false;
    private boolean commandPerformed = false;
    private boolean getOn = false;
    private boolean setOn = false;
    private String inString = "";
    private String displayString = "";
    private String error = "error!";
    private boolean wasDigit = false;
        
    /**
     * Creates and displays a window of the class RPNClaculator.
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        RPNForm gui = new RPNForm();
        gui.setVisible(true);
    }
    
    /**
     * constructor -- set up the form
     */
    public RPNForm()
    {  
        calc = new RPNCalculator();
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("  RPN Calculator");
        setLocation(40, 40);
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        setDisplayPanel();
    }
    
    /**
     * sets up the displayPanel
     */
    public final void setDisplayPanel()
    {
        /**
         * inner class -- listens for any button actions
         */
        class StatusListener implements ActionListener
        {
            /**
            * deal with an action
            * @param event --the actionEvent performed
            */
            @Override
            public void actionPerformed(ActionEvent event)
            { 
                dealWith(event.getActionCommand());
                displayTextField.requestFocusInWindow();
            }  
        }
        
        /**
         * inner class -- listens for any button actions
         */
        class DisplayListener implements KeyListener
        {
            /**
            * not implemented
            * @param event --the actionEvent performed
            */
            @Override
            public void keyPressed(KeyEvent event)
            {}
            /**
            * deal with a keystroke
            * @param event --the actionEvent performed
            */
            @Override
            public void keyReleased(KeyEvent event)
            {
                char c = event.getKeyChar();
                if(event.getKeyCode() == KeyEvent.VK_SHIFT)
                    return;
                if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        event.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    displayString = displayTextField.getText();
                    return;
                }
                
                displayTextField.setText(displayString);
                if(validChar(c))
                    dealWith(String.valueOf(c)); 
                displayTextField.requestFocusInWindow();
            }
            /**
            * not implemented
            * @param event --the actionEvent performed
            */
            @Override
            public void keyTyped(KeyEvent event)
            {}   
        }
        KeyListener displayListener = new DisplayListener();
        ActionListener buttonListener = new StatusListener();

        
        displayPanel = new JPanel( );
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));
        displayPanel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
                new Color(0XCC, 0X99, 0XFF)));   
        RPNLabel = new JLabel(" RPN ");
        RPNLabel.setBackground(new Color(0X33, 0X00, 0X66));
        RPNLabel.setFont(new Font("Courier New", 1, 36));
        RPNLabel.setForeground(new Color(0x66, 0x33, 0x66));      
        displayPanel.add(RPNLabel);

        displayTextField = new JTextField("");
        displayTextField.setFont(new Font("Courier New", 1, 24));
        displayTextField.setHorizontalAlignment(JTextField.RIGHT);
        displayTextField.setActionCommand("Enter");
        displayTextField.addKeyListener(displayListener);
        displayPanel.add(displayTextField);
        contentPane.add(displayPanel, BorderLayout.NORTH);        

        buttonPanel = new JPanel( );
        buttonPanel.setBackground(new Color(0xff, 0xef, 0xdf));
        buttonPanel.setLayout(new GridLayout(RPNCalculator.ROWS,
                RPNCalculator.COLS));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
                new Color(0X99, 0XFF, 0XFF)));
        buttonGrid = new JButton[RPNCalculator.ROWS][RPNCalculator.COLS];

        for(int i = 0; i < RPNCalculator.ROWS; i++)
        {
            for(int j = 0; j < RPNCalculator.COLS; j++)
            {
                buttonGrid[i][j]  = new JButton();
                buttonGrid[i][j].setFont(new Font("Courier New", 1, 16));
                buttonGrid[i][j].addActionListener(buttonListener);
                buttonGrid[i][j].setBorder(BorderFactory.createBevelBorder(1));
                buttonPanel.add(buttonGrid[i][j]);
            }
        }
        buttonGrid[0][0].setText("eXit");
        buttonGrid[0][1].setText("C");
        buttonGrid[0][2].setText("CE");
        buttonGrid[0][3].setFont(new Font("Courier New", 1, 20));
        buttonGrid[0][3].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[0][3].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[0][3].setText("7");
        buttonGrid[0][4].setFont(new Font("Courier New", 1, 20));
        buttonGrid[0][4].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[0][4].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[0][4].setText("8");
        buttonGrid[0][5].setFont(new Font("Courier New", 1, 20));
        buttonGrid[0][5].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[0][5].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[0][5].setText("9");
        buttonGrid[0][6].setText("+");
        buttonGrid[0][7].setText("x");
        buttonGrid[1][0].setText("Set");
        buttonGrid[1][1].setText("Get");
        buttonGrid[1][2].setText("Up");
        buttonGrid[1][3].setFont(new Font("Courier New", 1, 20));
        buttonGrid[1][3].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[1][3].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[1][3].setText("4");
        buttonGrid[1][4].setFont(new Font("Courier New", 1, 20));
        buttonGrid[1][4].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[1][4].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[1][4].setText("5");
        buttonGrid[1][5].setFont(new Font("Courier New", 1, 20));
        buttonGrid[1][5].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[1][5].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[1][5].setText("6");
        buttonGrid[1][6].setText("-");
        buttonGrid[1][7].setText("/");
        buttonGrid[2][0].setText("Load");
        buttonGrid[2][1].setText("Save");
        buttonGrid[2][2].setText("Down");
        buttonGrid[2][3].setFont(new Font("Courier New", 1, 20));
        buttonGrid[2][3].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[2][3].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[2][3].setText("1");
        buttonGrid[2][4].setFont(new Font("Courier New", 1, 20));
        buttonGrid[2][4].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[2][4].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[2][4].setText("2");
        buttonGrid[2][5].setFont(new Font("Courier New", 1, 20));
        buttonGrid[2][5].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[2][5].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[2][5].setText("3");
        buttonGrid[2][6].setText("^");
        buttonGrid[2][7].setText("%");
        buttonGrid[3][0].setText("Rec");
        buttonGrid[3][1].setText("Run");
        buttonGrid[3][2].setText("?");
        buttonGrid[3][3].setText("+/-");
        buttonGrid[3][4].setFont(new Font("Courier New", 1, 20));
        buttonGrid[3][4].setBackground(new Color(0xf0, 0xf6, 0xff));
        buttonGrid[3][4].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[3][4].setText("0");
        buttonGrid[3][5].setText(".");
        buttonGrid[3][6].setText("1/n");
        buttonGrid[3][7].setFont(new Font("Courier New", 1, 15));
        buttonGrid[3][7].setBackground(new Color(0xf6, 0xf0, 0xff));
        buttonGrid[3][7].setForeground(new Color(0x99, 0x00, 0x66));
        buttonGrid[3][7].setText("Enter");

        contentPane.add(buttonPanel, BorderLayout.CENTER);
        disableAlpha(); 
    }
    
    /**
     * method: enter
     * sets displayString to an empty string, pushes the String displayed on 
     * displayTextField to the stack if it can be parsed to a double, and sets
     * the displayTextField to display inString, commandPerformed is set to 
     * false
     */
    public void enter()
    {
        if(!displayTextField.getText().equals("") || !displayTextField.getText().
                equals(error) || !displayTextField.getText().equals("?"))  
        {
            calc.theStack.push(Double.parseDouble(displayTextField.getText
                        ()));
            displayString = "";
            inString = calc.theStack.peek() + "";
            displayTextField.setText(inString);
            commandPerformed = false;
        }
    }
    
    /**
     * deal with an action
     * @param actionCommand --the actionEvent performed
     */
    public void dealWith(String actionCommand)
    {
        if(msgOn)
        {
            msgOn = false;
            displayTextField.setForeground(Color.BLACK);
            if(recordMode)
                displayTextField.setForeground(Color.MAGENTA);
            displayTextField.setText("");
        }
        try
        {
            if(helpMode)
            {
                displayHelp(actionCommand);
                helpMode = false;
                return;
            }
            else
                inString = displayTextField.getText();
            if(getOn)
            {
                getOn = false;
                String test = actionCommand;
                if(recordMode)
                    calc.instructions.add(test);
                if(Character.isDigit(test.charAt(0)))
                {
                    double aDigit = calc.register[Integer.parseInt
                            (test.charAt(0) + "")];
                    displayTextField.setText
                            (aDigit + "");
                    displayString = aDigit + "";
                }
                return;
            }
            if(setOn)
            {
                setOn = false;
                String test = actionCommand;
                if(recordMode)
                    calc.instructions.add(test);
                if(Character.isDigit(test.charAt(0)))
                    calc.register[Integer.parseInt(test.charAt(0) + "")] = 
                        Double.parseDouble(displayTextField.getText());
                return;
            }
            if(recordMode)
            {
                displayTextField.setForeground(Color.MAGENTA);
                if(!actionCommand.equals("Run"))
                {        
                    if(Character.isDigit(actionCommand.charAt(0)))
                    {
                        calc.instructions.add(actionCommand);
                        wasDigit = true;
                    }
                    else
                    {
                        if(!wasDigit)
                        {
                            calc.instructions.add(actionCommand);
                        }
                        else if(!actionCommand.equals("Rec"))
                        {
                            calc.instructions.add(actionCommand);
                        }
                    }
                }
            }
            if(actionCommand.equals("?"))
            {
                helpMode = true;
                displayTextField.setForeground(new Color(0, 0X99, 0X66));
                displayTextField.setText(inString = "?");
            }
            else if(actionCommand.equals("eXit"))
                System.exit(0);
            else if(actionCommand.equals("Save"))
            {
                PrintWriter out = new PrintWriter(new FileOutputStream
                       (MacroFile));
                Iterator itr = calc.instructions.iterator();
                while(itr.hasNext())
                    out.println((String)itr.next());
                out.close();
            }
            else if(actionCommand.equals("Load"))
            {
                BufferedReader in
                    = new BufferedReader(new FileReader(MacroFile));
                calc.instructions.clear();
                while(in.ready())
                    calc.instructions.add(in.readLine());
                in.close();
            }
            else if(actionCommand.equals("Rec"))
            {
                if(recordMode)
                {
                    recordMode = false;
                    displayTextField.setForeground(Color.BLACK);
                }
                else
                {
                    recordMode = true;
                    calc.instructions.clear();
                }
            }
            else if(actionCommand.equals("Run"))
            {
                if(recordMode)
                    displayTextField.setForeground(Color.BLACK);
                recordMode = false;
                displayString = "";
                inString = "";
                displayTextField.setText("");
                ListIterator itr = calc.instructions.listIterator();
                while(itr.hasNext())
                    dealWith((String)itr.next());
            }
            else if(actionCommand.equals("C")
                    || actionCommand.equals("c"))
            {           
                displayTextField.setText("");
                displayString = "";
                inString = "";
                calc.theStack.pop();
                if(calc.theStack.size() > 0)
                    displayTextField.setText(calc.theStack.peek() + "");
            }
            else if(actionCommand.equals("CE"))
            {
                displayTextField.setText("");
                displayString = "";
                inString = "";
                calc.theStack.clear();
            }
            else if(actionCommand.equals("Get"))
            {           
              getOn = true;
            }
            else if(actionCommand.equals("Set"))
            {           
               setOn = true;
            }
            else if(actionCommand.equals("Enter")
                    || actionCommand.equals("\\n"))
            {           
                enter();
            }
            else if (actionCommand.equals("1/n"))                 
            {           
                displayString = displayTextField.getText();
                if(displayString.equals(error))
                    displayString = calc.theStack.peek() + "";
                double aDouble = Double.parseDouble(displayString);
                if(aDouble != 0)
                    aDouble = 1 / aDouble;
                displayString = "" + aDouble;
                displayTextField.setText(displayString);
            }
            else if(actionCommand.equals("+/-"))
            {
                displayString = displayTextField.getText();
                if(displayString.substring(0,1).equals("-"))
                {
                    displayString = displayString.substring(1);
                    displayTextField.setText(displayString);
                }
                else if(Double.parseDouble(displayString) != 0)
                    displayString = "-" + displayString;
                    displayTextField.setText(displayString);
            }
            else if (actionCommand.equals("."))                 
            {   
                if(displayString.contains("."))
                    return;
                if(displayString.contains("E"))
                    return;
                if(displayString.equals(""))
                    displayString += "0.";
                else
                    displayString += ".";
                displayTextField.setText(displayString);
            }
            else if(Character.isDigit(actionCommand.charAt(0)))    
            { 
                displayString += actionCommand.charAt(0);
                displayTextField.setText(displayString);
                commandPerformed = false;
            }
            else if(actionCommand.equals("x")
                || actionCommand.equals("X")
                || actionCommand.equals("*"))
            {
                if(!displayString.equals("") && !commandPerformed)
                    enter();
                if(calc.theStack.size() < RPNCalculator.BINARY)
                    displayTextField.setText(error);
                else
                {
                    double answer = calc.multiply();
                    inString = "" + answer;
                    displayTextField.setText(inString);
                    commandPerformed = true;
                }
            }
            else if(actionCommand.equals("+"))
            {
                if(!displayString.equals("") && !commandPerformed)
                    enter();
                if(calc.theStack.size() < RPNCalculator.BINARY)
                    displayTextField.setText(error);
                else
                {
                    double answer = calc.add();
                    inString = "" + answer;
                    displayTextField.setText(inString);
                    commandPerformed = true;
                }
            }
            else if(actionCommand.equals("-"))
            {
                if(!displayString.equals("") && !commandPerformed)
                    enter();
                if(calc.theStack.size() < RPNCalculator.BINARY)
                    displayTextField.setText(error);
                else
                {
                    double answer = calc.subtract();
                    inString = "" + answer;
                    displayTextField.setText(inString);
                    commandPerformed = true;
                }
            }
            else if(actionCommand.equals("/"))
            {
                if(!displayString.equals("") && !commandPerformed)
                    enter();
                if(calc.theStack.size() < RPNCalculator.BINARY)
                    displayTextField.setText(error);
                else
                {
                    double answer = calc.divide();
                    inString = "" + answer;
                    displayTextField.setText(inString);
                    commandPerformed = true;
                }
            }
            else if(actionCommand.equals("^"))
            {
                if(!displayString.equals("") && !commandPerformed)
                   enter();
                if(calc.theStack.size() < RPNCalculator.BINARY)
                   displayTextField.setText(error);
                else
                {
                    double answer = calc.exponentiation();
                    inString = "" + answer;
                    displayTextField.setText(inString);
                    commandPerformed = true;
                }
            }
            else if(actionCommand.equals("%"))
            {
                if(!displayString.equals("") && !commandPerformed)
                   enter();
                if(calc.theStack.size() < RPNCalculator.BINARY)
                   displayTextField.setText(error);
                else
                {
                    double answer = calc.modOperator();
                    inString = "" + answer;
                    displayTextField.setText(inString);
                    commandPerformed = true;
                }
            }
            else if(actionCommand.equals("Up"))
            {           
                calc.up();
                displayTextField.setText(calc.theStack.peek() + "");
            }
            else if(actionCommand.equals("Down"))
            {
                calc.down();
                displayTextField.setText(calc.theStack.peek() + "");
            }
            
        }
        catch(Exception e)
        {
            displayTextField.setText("");
        }
       
        
    } 

    /**
     * displays the appropriate help
     * @param actionCommand the command from the triggering event
     */
    private void  displayHelp(String actionCommand)
    {
        msgOn = true;
        if(actionCommand.equals("eXit"))
                displayTextField.setText("eXit: Exits program");
        else if(actionCommand.equals("C")
                || actionCommand.equals("c"))
                displayTextField.setText("C: Clears top element");
        else if(actionCommand.equals("CE"))
                displayTextField.setText("CE: Clears entire stack");
        else if(actionCommand.equals("+"))
                displayTextField.setText("+: adds top  2 elements");
        else if(actionCommand.equals("x")
                || actionCommand.equals("X")
                || actionCommand.equals("*"))
                displayTextField.setText("x: multiplies top 2 elements");
        else if(actionCommand.equals("Set"))
                displayTextField.setText("Set: Sets register (0-9)");
        else if(actionCommand.equals("Get"))
                displayTextField.setText("Get: gets register (0-9)");
        else if(actionCommand.equals("Up"))
                displayTextField.setText("Up: Rotates stack up");
        else if(actionCommand.equals("-"))
                displayTextField.setText("-: subtracts top 2 elements");
        else if(actionCommand.equals("/"))
                displayTextField.setText("/: divides top 2 elements");
        else if(actionCommand.equals("Load"))
                displayTextField.setText("Load: Loads program from file");
        else if(actionCommand.equals("Save"))
                displayTextField.setText("Save: Saves program to file");
        else if(actionCommand.equals("Down"))
                displayTextField.setText("Down: Rotates stack down");
        else if(actionCommand.equals("^"))
                displayTextField.setText("^: exponent");
        else if(actionCommand.equals("%"))
                displayTextField.setText("%: Mods top 2 elements");
        else if(actionCommand.equals("Rec"))
                displayTextField.setText("Rec: Program mode on/off");
        else if(actionCommand.equals("Run"))
                displayTextField.setText("Run: Runs program");
        else if(actionCommand.equals("?"))
                displayTextField.setText("?: press ? then key for help");
        else if(actionCommand.equals("+/-"))
                displayTextField.setText("+/-: changes sign of number");
        else if(actionCommand.equals("1/n"))
                displayTextField.setText("1/n: inverts the number");
        else if(actionCommand.equals("Enter"))
                displayTextField.setText("Enter: element to stack");
        else
                displayTextField.setText("");
    }  

    /**
     * 
     * @param c the character to test
     * @return true is c is valid, false otherwise
     */
    private boolean validChar(char c)
    {
        if(Character.isDigit(c))
            return true;
        switch(c)
        {
            case '+':
            case '-':
            case '*':
            case 'x':
            case 'X':
            case '/':
            case 'C':
            case 'c':
            case '^':
            case '%':
            case '?':
            case '.':
            case '\r':
            case '\n':
            return true;
        }
        return false;
    }
    
    /**
     * disables non-numeric-related keys
     */
    private void disableAlpha()
    {
        for(char c = '\0'; c < '%'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = '&'; c < '*'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = ':'; c <= '?'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = '@'; c <= 'C'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = 'D'; c <= 'X'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = 'Y'; c <= '^'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = '_'; c <= 'c'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = 'd'; c <= 'x'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        for(char c = 'y'; c <= '~'; c++)
            displayTextField.getInputMap().put(KeyStroke.getKeyStroke(c),
                            "none");
        
        displayTextField.getInputMap().put(KeyStroke.getKeyStroke('/'),
                            "none");
    }
}





