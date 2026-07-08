package com.mycompany.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public final class UIStyles {

    public static final Color APP_BG = Color.decode("#f6f8fb");
    public static final Color SURFACE = Color.decode("#ffffff");
    public static final Color PRIMARY = Color.decode("#1d4ed8");
    public static final Color PRIMARY_HOVER = Color.decode("#1e40af");
    public static final Color PRIMARY_LIGHT = Color.decode("#dbeafe");
    public static final Color DANGER = Color.decode("#dc2626");
    public static final Color DANGER_HOVER = Color.decode("#b91c1c");
    public static final Color SIDEBAR = Color.decode("#0f172a");
    public static final Color SIDEBAR_ACTIVE = Color.decode("#1d4ed8");
    public static final Color SIDEBAR_HOVER = Color.decode("#1e293b");
    public static final Color HEADER = Color.decode("#2563eb");
    public static final Color TEXT = Color.decode("#0f172a");
    public static final Color MUTED = Color.decode("#475569");
    public static final Color LINE = Color.decode("#e2e8f0");

    private UIStyles() {
    }

    public static void setupGlobalDefaults() {
        UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Component.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("TextComponent.arc", 10);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.selectionBackground", PRIMARY_LIGHT);
        UIManager.put("Table.selectionForeground", TEXT);
    }

    public static void page(JPanel panel) {
        panel.setBackground(APP_BG);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
    }

    public static void surface(JPanel panel) {
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE),
                new EmptyBorder(14, 16, 14, 16)
        ));
    }

    public static void title(JLabel label) {
        label.putClientProperty("FlatLaf.style", "font: bold 28");
        label.setForeground(TEXT);
    }

    public static void subtitle(JLabel label) {
        label.putClientProperty("FlatLaf.style", "font: 14");
        label.setForeground(MUTED);
    }

    public static void formLabel(JLabel label) {
        label.putClientProperty("FlatLaf.style", "font: bold 13");
        label.setForeground(MUTED);
    }

    public static void textField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setForeground(TEXT);
        textField.setBackground(Color.WHITE);
        textField.putClientProperty("JTextField.padding", new Insets(0, 12, 0, 12));
        textField.putClientProperty("JComponent.roundRect", true);
        textField.putClientProperty("JTextField.showClearButton", true);
    }

    public static void primaryButton(JButton button) {
        button(button, PRIMARY, PRIMARY_HOVER);
    }

    public static void dangerButton(JButton button) {
        button(button, DANGER, DANGER_HOVER);
    }

    public static void button(JButton button, Color background, Color hoverBackground) {
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(new EmptyBorder(9, 16, 9, 16));
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverBackground);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(background);
                }
            }
        });
    }

    public static void navButton(JButton button) {
        button.setBackground(SIDEBAR);
        button.setForeground(Color.decode("#e2e8f0"));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(1, 18, 1, 1));
        button.putClientProperty("JButton.buttonType", "borderless");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!SIDEBAR_ACTIVE.equals(button.getBackground())) {
                    button.setBackground(SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!SIDEBAR_ACTIVE.equals(button.getBackground())) {
                    button.setBackground(SIDEBAR);
                }
            }
        });
    }

    public static void selectedNavButton(JButton selected, JButton... allButtons) {
        for (JButton button : allButtons) {
            button.setBackground(button == selected ? SIDEBAR_ACTIVE : SIDEBAR);
            button.setForeground(Color.WHITE);
        }
    }

    public static void table(JTable table, JScrollPane scrollPane) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setRowHeight(36);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(LINE);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                if (isSelected) {
                    c.setBackground(PRIMARY_LIGHT);
                    c.setForeground(TEXT);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : Color.decode("#f8fafc"));
                    c.setForeground(TEXT);
                }
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.setDefaultRenderer(String.class, cellRenderer);
        table.setDefaultRenderer(Integer.class, cellRenderer);
        table.setDefaultRenderer(int.class, cellRenderer);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.decode("#eef2ff"));
        header.setForeground(TEXT);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, LINE));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(Color.decode("#eef2ff"));
        headerRenderer.setForeground(TEXT);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerRenderer.setBorder(new EmptyBorder(0, 12, 0, 12));
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        scrollPane.setBorder(BorderFactory.createLineBorder(LINE));
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

    public static void columnWidths(JTable table, int... widths) {
        for (int i = 0; i < widths.length && i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }
}
