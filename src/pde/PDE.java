package pde;

import javax.swing.*;

public class PDE {

    public static void main(String[] args) {
        
        Object[] options = {"1-dimensional", "2-dimensional"};
        int choice = JOptionPane.showOptionDialog(null,
                                                "How many dimensions to model?",
                                                "Choose dimensionality",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.PLAIN_MESSAGE,
                                                null,
                                                options,
                                                options[0]);
        
        switch(choice){
            case 0:
                System.out.println("1D");
                //new Thread(new pde.one.Starter()).start();
                new pde.one.Starter().run();
                break;
            case 1:
                System.out.println("1D");
                //new Thread(new pde.one.Starter()).start();
                new pde.two.Starter().run();
                break;
        }
    }

}
