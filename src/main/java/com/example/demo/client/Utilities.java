package com.example.demo.client;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Utilities {
    public static final Color TRANSPARENT_COLOR = new Color(0,0,0,0);
    public static final Color PRIMARY_COLOR = Color.decode("#9CC6DB");
    public static final Color SECONDARY_COLOR = Color.decode("#FCF6D9");
    public static final Color TEXT_COLOR = Color.darkGray;

    public static EmptyBorder addPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}
