package pde;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public enum Type{
    wave("wave", 2, 0),
    heat("heat", 2, 1),
    transport("transport", 10, 2),
    combination("combined heat and wave", 2, 3);
    
    private final String asText;
    public final int sleepTime;
    private final int idx;
    public double[] coeffs;
    
    private Type(String text, int sleepTime, int idx){
        this.asText = text;
        this.sleepTime = sleepTime;
        this.idx = idx;
        this.coeffs = initialCoeffs[idx].clone();
    }
    @Override
    public String toString(){
        return asText;
    }
    private static final String[] descriptions = {
        "<html>The damped wave equation,<br>"
            + "∂²u/∂t² - c²Δu + a∂u/∂t = 0</html>",
        "<html>The heat equation,<br>"
            + "∂u/∂t - αΔu = 0</html>",
        "<html>The damped transport equation,<br>"
            + "∂u/∂t + c∂u/∂x + au = 0</html>",
        "<html>Combined heat and wave equations,<br>"
            + "∂²u/∂t² - c²Δu + a∂u/∂t = 0<br>"
            + "and<br>"
            + "∂u/∂t - αΔu = 0</html>"};
    private final double[][] initialCoeffs = {
        {30, 0.1},
        {0.5},
        {0.05, 0.05},
        {30, 0.001, 0.5}
    };
    private static final String[][] settingNames = {
        {"c² (tension)", "a (damping)"},
        {"α (diffuse speed)"},
        {"c (speed)", "a (damping)"},
        {"c² (tension)", "a (damping)", "α (diffuse speed)"}
    };
    private static final int[] settingCount = {
        2, 1, 2, 3
    };
    public JPanel makeOptions(){
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        p.add(new JLabel(descriptions[idx]));
        p.add(Box.createVerticalStrut(10));
        JPanel mid;
        
        for(int i = 0; i < settingCount[idx]; i++){
            mid = new JPanel();
            mid.add(new JLabel(settingNames[idx][i]));
            JSlider sFreq = new JSlider(1, 50, 10);
            sFreq.setAlignmentX(0.5f);
            final int j = i;
            sFreq.addChangeListener((ChangeEvent e) -> {
                double val = 0.01 * sFreq.getValue() * sFreq.getValue();
                coeffs[j] = val * initialCoeffs[idx][j];
            });
            mid.add(sFreq);
            p.add(mid);
        }
        
        return p;
    }
}
