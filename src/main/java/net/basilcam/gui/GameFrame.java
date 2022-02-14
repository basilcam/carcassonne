package net.basilcam.gui;

import net.basilcam.core.Player;
import net.basilcam.core.TurnState;
import net.basilcam.core.api.CarcassonneApi;
import net.basilcam.core.api.CarcassonneHandler;
import net.basilcam.core.tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class GameFrame extends JFrame implements CarcassonneHandler {
    private static final int BOARD_SIZE = 720;
    private static final String NAME = "Carcassonne";
    private CarcassonneApi api;
    private ImageProvider imageProvider;

    private Player player;
    private TurnState turnState;

    private JPanel board;
    private JTable table;
    private JLabel currentPlayerMeepleImage;
    private JLabel currentPlayerName;
    private JLabel currentTileImage;
    private JButton rotateTileButton;
    private JButton placeMeepleButton;
    private JButton endTurnButton;

    public GameFrame(CarcassonneApi api) {
        super(NAME);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);

        this.api = api;
        this.imageProvider = new ImageProvider();

        createBoard();
        JScrollPane scrollPane = new JScrollPane(this.board);
        scrollPane.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        JPanel infoPanel = new JPanel();
        createTable(infoPanel);
        createCurrentPlayerPanel(infoPanel);
        createCurrentTileImage(infoPanel);
        createRotateTileButton(infoPanel);
        createPlaceMeepleButton(infoPanel);
        createEndTurnButton(infoPanel);
        add(infoPanel);

        this.api.register(this);
        this.api.startGame();

        pack();
        setVisible(true);
    }

    @Override
    public void turnStarted(Player player, TurnState turnState) {
        this.player = player;
        this.turnState = turnState;

        this.currentPlayerName.setText(player.getName());
        this.currentPlayerMeepleImage.setIcon(new ImageIcon(this.imageProvider.getMeepleImage(player.getColor())));

        Image image = this.imageProvider.getTileImage(turnState.getTile());
        ImageIcon icon = new ImageIcon(image);
        this.currentTileImage.setIcon(icon);
    }

    @Override
    public void scoreUpdate(Player player) {
        // todo: update score table
    }

    @Override
    public void gameEnded() {
        // todo: show winner
        // todo: open new start frame
    }


    private void createBoard() {
        this.board = new JPanel();
        this.board.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        this.board.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!GameFrame.this.turnState.hasPlacedTile()) {
                    return;
                }

                Tile tile = GameFrame.this.turnState.getTile();
                // todo handle
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });


        Tile startTile = this.api.getStartTile();
        plotTile(startTile, 0, 0);
        // todo: add start piece
    }

    private void plotTile(Tile tile, int xPosition, int yPosition) {
        BufferedImage tileImage = this.imageProvider.getTileImage(tile);
        ImageIcon icon = new ImageIcon(tileImage);

        JButton button = new JButton(icon);
        button.setBounds(xPosition, yPosition, tileImage.getWidth(), tileImage.getHeight());
        button.setIcon(icon);

        this.board.add(button);
        this.board.repaint();

    }

    private void createTable(JPanel panel) {
        this.table = new JTable();
        panel.add(this.table);
    }

    private void createCurrentPlayerPanel(JPanel panel) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));

        this.currentPlayerMeepleImage = new JLabel();
        this.currentPlayerMeepleImage.setPreferredSize(new Dimension(50, 50));
        playerPanel.add(this.currentPlayerMeepleImage);

        this.currentPlayerName = new JLabel();
        this.currentPlayerName.setFont(new Font("Courier New", Font.PLAIN, 30));
        playerPanel.add(this.currentPlayerName);

        panel.add(playerPanel);
    }

    private void createCurrentTileImage(JPanel panel) {
        this.currentTileImage = new JLabel();
        panel.add(this.currentTileImage);
    }

    private void createRotateTileButton(JPanel panel) {
        this.rotateTileButton = new JButton("rotate tile");
        this.rotateTileButton.setFont(new Font("Courier New", Font.PLAIN, 30));
        this.rotateTileButton.addActionListener(event -> {

        });
        panel.add(this.rotateTileButton);
    }

    private void createPlaceMeepleButton(JPanel panel) {
        this.placeMeepleButton = new JButton("place meeple");
        this.placeMeepleButton.setFont(new Font("Courier New", Font.PLAIN, 30));
        panel.add(this.placeMeepleButton);
    }

    private void createEndTurnButton(JPanel panel) {
        this.endTurnButton = new JButton("end turn");
        this.endTurnButton.setFont(new Font("Courier New", Font.PLAIN, 30));
        panel.add(this.endTurnButton);
    }
}




































