package pde.two;

import javax.swing.*;

public enum Type{
    wave("wave", 1, 0),
    heat("heat", 1, 1);
    
    private final String asText;
    final int sleepTime;
    private final int idx;
    private Type(String text, int sleepTime, int idx){
        asText = text;
        this.sleepTime = sleepTime;
        this.idx = idx;
    }
    @Override
    public String toString(){
        return asText;
    }
    private static final String[] descriptions = {
        "The wave equation,\n∂²u/∂t² - c²Δu = 0",
        "The heat equation,\n∂u/∂t - αΔu = 0",
        "The transport equation,\n∂u/∂t + c∂u/∂x = 0",
        "The damped transport equation,\n∂u/∂t + c∂u/∂x + au = 0\nStill a tiny bit buggy :("};
    String getDescription(){
        return descriptions[idx];
    }
    
    JPanel makeOptions(){
        JPanel p = new JPanel();
        
        
        
        return p;
    }
}
