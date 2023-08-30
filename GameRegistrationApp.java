import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class Game {
    String title;
    String description;
    List<String> genres;
    boolean isCompleted;

    public Game(String title, String description, List<String> genres, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.isCompleted = isCompleted;
    }
}

public class GameRegistrationApp extends JFrame {
    private List<Game> gamesList = new ArrayList<>();
    private int selectedGameIndex = -1;

    private JTextField titleField = new JTextField(20);
    private JTextField descriptionField = new JTextField(20);
    private JCheckBox adventureCheckBox = new JCheckBox("Aventura");
    private JCheckBox sportsCheckBox = new JCheckBox("Esporte");
    private JCheckBox rpgCheckBox = new JCheckBox("RPG");
    private JCheckBox completedCheckBox = new JCheckBox("Platinado");
    private JCheckBox notCompletedCheckBox = new JCheckBox("Zerado");
    private JTextArea outputArea = new JTextArea(10, 40);
    private JButton editButton = new JButton("Editar");
    private JButton deleteButton = new JButton("Excluir");

    private JList<Game> gamesJList = new JList<>();
    private DefaultListModel<Game> gamesListModel = new DefaultListModel<>();

    public GameRegistrationApp() {
        setTitle("Cadastro de Jogos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.DARK_GRAY);
        JLabel titleLabel = new JLabel("Cadastro de Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Título:"));
        formPanel.add(titleField);

        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(descriptionField);

        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new GridLayout(1, 3));
        genrePanel.add(adventureCheckBox);
        genrePanel.add(sportsCheckBox);
        genrePanel.add(rpgCheckBox);
        formPanel.add(new JLabel("Gênero(s):"));
        formPanel.add(genrePanel);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(1, 2));
        statusPanel.add(completedCheckBox);
        statusPanel.add(notCompletedCheckBox);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusPanel);

        JButton registerButton = new JButton("Cadastrar/Atualizar");
        registerButton.addActionListener(new RegisterButtonListener());
        formPanel.add(registerButton);

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.setBackground(Color.WHITE);

        JLabel outputLabel = new JLabel("Games Cadastrados");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 20));
        outputLabel.setForeground(Color.DARK_GRAY);
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        outputPanel.add(buttonPanel, BorderLayout.SOUTH);

        outputArea.setEditable(false);

        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);

        gamesJList.setModel(gamesListModel);
        gamesJList.setCellRenderer(new GameListRenderer());
        gamesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gamesJList.addListSelectionListener(new GameListSelectionListener());
        outputPanel.add(new JScrollPane(gamesJList), BorderLayout.WEST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
    }

    private class RegisterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            List<String> genres = new ArrayList<>();
            if (adventureCheckBox.isSelected()) {
                genres.add("Aventura");
            }
            if (sportsCheckBox.isSelected()) {
                genres.add("Esporte");
            }
            if (rpgCheckBox.isSelected()) {
                genres.add("RPG");
            }
            boolean isCompleted = completedCheckBox.isSelected();
            boolean isNotCompleted = notCompletedCheckBox.isSelected();

            // Definir isCompleted com base nas seleções das caixas de seleção
            if (isCompleted && isNotCompleted) {
                // Ambos selecionados - deixar isCompleted como true
                isNotCompleted = false;
            } else if (!isCompleted && !isNotCompleted) {
                // Nenhum selecionado - considerar como Zerado
                isCompleted = false;
                isNotCompleted = true;
            }

            if (selectedGameIndex == -1) {
                Game newGame = new Game(title, description, genres, isCompleted);
                gamesList.add(newGame);
            } else {
                Game selectedGame = gamesList.get(selectedGameIndex);
                selectedGame.title = title;
                selectedGame.description = description;
                selectedGame.genres = genres;
                selectedGame.isCompleted = isCompleted;
            }

            selectedGameIndex = -1; // Resetar o índice selecionado

            updateOutputArea();
            clearFields();
        }
    }

    private class EditButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // ... (código de edição anterior)
        }
    }

    private class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedGameIndex != -1) {
                gamesList.remove(selectedGameIndex);
                selectedGameIndex = -1; // Resetar o índice selecionado
                updateOutputArea();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(GameRegistrationApp.this, "Selecione um jogo para excluir.");
            }
        }
    }

    private class GameListRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Game game = (Game) value;
            label.setText(game.title);
            return label;
        }
    }

    private class GameListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            selectedGameIndex = gamesJList.getSelectedIndex();
            if (selectedGameIndex != -1) {
                Game selectedGame = gamesList.get(selectedGameIndex);
                titleField.setText(selectedGame.title);
                descriptionField.setText(selectedGame.description);
                adventureCheckBox.setSelected(selectedGame.genres.contains("Aventura"));
                sportsCheckBox.setSelected(selectedGame.genres.contains("Esporte"));
                rpgCheckBox.setSelected(selectedGame.genres.contains("RPG"));
                completedCheckBox.setSelected(selectedGame.isCompleted);
                notCompletedCheckBox.setSelected(!selectedGame.isCompleted);
            }
        }
    }

    private void updateOutputArea() {
        outputArea.setText("Jogos cadastrados:\n");
        gamesListModel.clear();
        for (int i = 0; i < gamesList.size(); i++) {
            Game game = gamesList.get(i);
            outputArea.append("ID: " + i + "\n");
            outputArea.append("Título: " + game.title + "\n");
            outputArea.append("Descrição: " + game.description + "\n");
            outputArea.append("Gênero(s): " + String.join(", ", game.genres) + "\n");
            outputArea.append("Status: " + (game.isCompleted ? "Platinado" : "Zerado") + "\n");
            outputArea.append("------------------------------\n");
            gamesListModel.addElement(game);
        }
    }

    private void clearFields() {
        titleField.setText("");
        descriptionField.setText("");
        adventureCheckBox.setSelected(false);
        sportsCheckBox.setSelected(false);
        rpgCheckBox.setSelected(false);
        completedCheckBox.setSelected(false);
        notCompletedCheckBox.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameRegistrationApp());
    }
}
