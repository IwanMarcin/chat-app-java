package com.example.demo.client;

import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Utilities {
    public static final Color TRANSPARENT_COLOR = new Color(0,0,0,0);
    public static final Color PRIMARY_COLOR = Color.decode("#0F1A2B");
    public static final Color SECONDARY_COLOR = Color.decode("#1C2E4A");
    public static final Color MY_MESSAGE_COLOR = Color.decode("#806375");
    public static final Color OTHER_MESSAGE_COLOR = Color.decode("#A38F80");
    public static final Color TEXT_COLOR = Color.decode("#F5EFEB");

    public static EmptyBorder addPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}
