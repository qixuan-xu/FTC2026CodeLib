package test;

import control.TeleOpController;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Test program with graphical simulation of robot position.
 * WASD to move, QE to rotate.
 */
public class Main extends JFrame {
    private TeleOpController controller;
    private volatile double x = 0.0;
    private volatile double y = 0.0;
    private volatile double rotation = 0.0;
    private volatile long lastXTime = 0;
    private volatile long lastYTime = 0;
    private volatile long lastRotationTime = 0;
    
    private RobotSimPanel simPanel;

    public Main() {
        setTitle("FTC Robot Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Initialize controller
        controller = new TeleOpController(Constants.DEADZONE, Constants.CURVE_POWER);

        // Create simulation panel
        simPanel = new RobotSimPanel(controller);
        add(simPanel);

        setVisible(true);
        setFocusable(true);
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                long now = System.currentTimeMillis();
                switch (key) {
                    case KeyEvent.VK_W:
                        if (x < 1.0) x += 0.05;
                        lastXTime = now;
                        break;
                    case KeyEvent.VK_S:
                        if (x > -1.0) x -= 0.05;
                        lastXTime = now;
                        break;
                    case KeyEvent.VK_A:
                        if (y > -1.0) y -= 0.05;
                        lastYTime = now;
                        break;
                    case KeyEvent.VK_D:
                        if (y < 1.0) y += 0.05;
                        lastYTime = now;
                        break;
                    case KeyEvent.VK_Q:
                        if (rotation < 1.0) rotation += 0.05;
                        lastRotationTime = now;
                        break;
                    case KeyEvent.VK_E:
                        if (rotation > -1.0) rotation -= 0.05;
                        lastRotationTime = now;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        // Start update thread
        Thread updateThread = new Thread(() -> {
            while (true) {
                long now = System.currentTimeMillis();
                // 自动回中
                if (now - lastXTime > 500) {
                    if (x > 0) x -= 0.005;
                    else if (x < 0) x += 0.005;
                }
                if (now - lastYTime > 500) {
                    if (y > 0) y -= 0.005;
                    else if (y < 0) y += 0.005;
                }
                if (now - lastRotationTime > 500) {
                    if (rotation > 0) rotation -= 0.005;
                    else if (rotation < 0) rotation += 0.005;
                }

                // Update controller
                double[] powers = controller.update(x, y, rotation);
                simPanel.updatePowers(powers);


                // Update display
                simPanel.repaint();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        updateThread.setDaemon(true);
        updateThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}

/**
 * Panel for drawing robot simulation.
 */
class RobotSimPanel extends JPanel {
    private TeleOpController controller;
    private double[] motorPowers = {0, 0, 0, 0};
    private double[] wheelAngles = {0, 0, 0, 0}; // 轮子转动角度
    private static final int WHEEL_RADIUS = 20;
    private static final int ROBOT_WIDTH = 150;
    private static final int ROBOT_HEIGHT = 150;

    public RobotSimPanel(TeleOpController controller) {
        this.controller = controller;
        setBackground(new Color(240, 240, 240));
    }

    public void updatePowers(double[] powers) {
        this.motorPowers = powers.clone();
        // 更新轮子角度（根据电机功率）
        for (int i = 0; i < 4; i++) {
            wheelAngles[i] += motorPowers[i] * 20; // 每帧转20° * 功率
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        // Draw robot chassis
        drawChassis(g2d, centerX, centerY);

        // Draw wheels
        drawWheels(g2d, centerX, centerY);

        // Draw info panel
        drawInfoPanel(g2d, width, height);
    }

    private void drawChassis(Graphics2D g2d, int centerX, int centerY) {
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(centerX - ROBOT_WIDTH / 2, centerY - ROBOT_HEIGHT / 2, ROBOT_WIDTH, ROBOT_HEIGHT);

        // 画中心十字
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawLine(centerX - 20, centerY, centerX + 20, centerY);
        g2d.drawLine(centerX, centerY - 20, centerX, centerY + 20);
    }

    private void drawWheels(Graphics2D g2d, int centerX, int centerY) {
        int offsetX = ROBOT_WIDTH / 2 - 20;
        int offsetY = ROBOT_HEIGHT / 2 - 20;

        // 前左 (FL)
        drawWheel(g2d, centerX - offsetX, centerY - offsetY, 0, "FL");
        // 前右 (FR)
        drawWheel(g2d, centerX + offsetX, centerY - offsetY, 1, "FR");
        // 后左 (BL)
        drawWheel(g2d, centerX - offsetX, centerY + offsetY, 2, "BL");
        // 后右 (BR)
        drawWheel(g2d, centerX + offsetX, centerY + offsetY, 3, "BR");
    }

    private void drawWheel(Graphics2D g2d, int x, int y, int wheelIndex, String label) {
        // 绘制轮子圆形
        g2d.setColor(new Color(50, 100, 200));
        g2d.fillOval(x - WHEEL_RADIUS, y - WHEEL_RADIUS, WHEEL_RADIUS * 2, WHEEL_RADIUS * 2);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - WHEEL_RADIUS, y - WHEEL_RADIUS, WHEEL_RADIUS * 2, WHEEL_RADIUS * 2);

        // 绘制轮子旋转指示（一条线）
        double angle = Math.toRadians(wheelAngles[wheelIndex]);
        int x2 = (int) (x + WHEEL_RADIUS * 0.8 * Math.cos(angle));
        int y2 = (int) (y + WHEEL_RADIUS * 0.8 * Math.sin(angle));
        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(x, y, x2, y2);

        // 绘制标签
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(label, x - 15, y + 40);

        // 绘制功率值
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString(String.format("%.2f", motorPowers[wheelIndex]), x - 20, y + 55);
    }

    private void drawInfoPanel(Graphics2D g2d, int width, int height) {
        int panelHeight = 140;
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(10, height - panelHeight - 10, 350, panelHeight);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(10, height - panelHeight - 10, 350, panelHeight);

        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        int y = height - panelHeight;
        g2d.drawString("电机功率显示", 20, y + 25);
        g2d.drawString(String.format("FL:%.3f  FR:%.3f", motorPowers[0], motorPowers[1]), 20, y + 50);
        g2d.drawString(String.format("BL:%.3f  BR:%.3f", motorPowers[2], motorPowers[3]), 20, y + 70);
        g2d.drawString("WASD:移动  QE:旋转", 20, y + 95);
        g2d.drawString("红线：轮子旋转方向", 20, y + 115);
    }
}