import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SistemaBackupArquivosGUI extends JFrame {
    private JPanel painelPrincipal;
    private JPanel painelConfiguracao;
    private JPanel painelStatus;

    private JTextField campoOrigem;
    private JTextField campoBackup;
    private JTextField campoIntervalo;
    private JButton botaoOrigem;
    private JButton botaoBackup;
    private JButton botaoConfirmar;

    private JLabel labelStatus;

    public SistemaBackupArquivosGUI() {
        super("Sistema de Backup de Arquivos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // Impede que o usuário redimensione a janela

        // Painel principal usando CardLayout
        painelPrincipal = new JPanel(new CardLayout());

        // Painel de configuração
        painelConfiguracao = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10); // Espaçamento interno dos componentes

        // Label e TextField para Local de Origem
        constraints.gridx = 0;
        constraints.gridy = 0;
        painelConfiguracao.add(new JLabel("Local de Origem:"), constraints);

        constraints.gridx = 1;
        campoOrigem = new JTextField(20); // Largura preferencial do campo de texto
        painelConfiguracao.add(campoOrigem, constraints);

        constraints.gridx = 2;
        botaoOrigem = new JButton("Selecionar");
        botaoOrigem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecionarDiretorio(campoOrigem);
            }
        });
        painelConfiguracao.add(botaoOrigem, constraints);

        // Label e TextField para Local de Backup
        constraints.gridx = 0;
        constraints.gridy = 1;
        painelConfiguracao.add(new JLabel("Local de Backup:"), constraints);

        constraints.gridx = 1;
        campoBackup = new JTextField(20); // Largura preferencial do campo de texto
        painelConfiguracao.add(campoBackup, constraints);

        constraints.gridx = 2;
        botaoBackup = new JButton("Selecionar");
        botaoBackup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecionarDiretorio(campoBackup);
            }
        });
        painelConfiguracao.add(botaoBackup, constraints);

        // Label e TextField para Intervalo de Backup
        constraints.gridx = 0;
        constraints.gridy = 2;
        painelConfiguracao.add(new JLabel("Intervalo de Backup (horas):"), constraints);

        constraints.gridx = 1;
        campoIntervalo = new JTextField(5); // Largura preferencial do campo de texto
        painelConfiguracao.add(campoIntervalo, constraints);

        // Botão Confirmar
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 1; // Ocupa duas colunas
        botaoConfirmar = new JButton("Confirmar");
        botaoConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarBackupAgendado();
            }
        });
        painelConfiguracao.add(botaoConfirmar, constraints);

        // Painel de status
        painelStatus = new JPanel(new BorderLayout());
        labelStatus = new JLabel("Preencha os campos e clique em 'Confirmar' para iniciar o backup.");
        painelStatus.add(labelStatus, BorderLayout.CENTER);

        // Adiciona os painéis ao painelPrincipal
        painelPrincipal.add(painelConfiguracao, "ConfigPanel");
        painelPrincipal.add(painelStatus, "StatusPanel");

        getContentPane().add(painelPrincipal);
        pack(); // Ajusta o tamanho da janela conforme o conteúdo
        setLocationRelativeTo(null); // Centraliza a janela na tela
    }

    private void selecionarDiretorio(JTextField campoTexto) {
        JFileChooser seletorArquivos = new JFileChooser();
        seletorArquivos.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int opcao = seletorArquivos.showOpenDialog(this);
        if (opcao == JFileChooser.APPROVE_OPTION) {
            File arquivoSelecionado = seletorArquivos.getSelectedFile();
            campoTexto.setText(arquivoSelecionado.getAbsolutePath());
        }
    }

    private void iniciarBackupAgendado() {
        try {
            String origem = campoOrigem.getText().trim();
            String backup = campoBackup.getText().trim();
            int intervaloHoras = Integer.parseInt(campoIntervalo.getText().trim());

            // Validação básica dos campos
            if (origem.isEmpty() || backup.isEmpty() || intervaloHoras <= 0) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos corretamente.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            long intervaloMilissegundos = intervaloHoras * 60 * 60 * 1000; // Converter horas para milissegundos

            // Inicia o backup agendado com base nas configurações fornecidas
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    realizarBackup(origem, backup);
                }
            }, 0, intervaloMilissegundos);

            botaoConfirmar.setEnabled(false); // Desabilita o botão depois de confirmar

            // Mostra o painel de status
            CardLayout layoutCartao = (CardLayout) painelPrincipal.getLayout();
            layoutCartao.show(painelPrincipal, "StatusPanel");

            labelStatus.setText("Backup agendado com sucesso. Intervalo: " + intervaloHoras + " horas.");

            // Minimiza a janela principal
            setState(JFrame.ICONIFIED);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Intervalo de Backup deve ser um número válido.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarBackup(String origem, String backup) {
        File diretorioOrigem = new File(origem);
        File diretorioBackup = new File(backup);

        // Verifica se os diretórios de origem e backup existem
        if (!diretorioOrigem.exists() || !diretorioBackup.exists()) {
            JOptionPane.showMessageDialog(this, "Diretórios de origem ou backup não encontrados.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cria um diretório para o backup com o mesmo nome da pasta de origem
        String nomePastaOrigem = diretorioOrigem.getName();
        File pastaBackup = new File(diretorioBackup, nomePastaOrigem);
        pastaBackup.mkdirs();

        // Lista os arquivos no diretório de origem e copia para o diretório de backup
        File[] arquivosBackup = diretorioOrigem.listFiles();
        if (arquivosBackup != null) {
            for (File arquivo : arquivosBackup) {
                if (arquivo.isFile()) {
                    try {
                        Path caminhoOrigem = arquivo.toPath();
                        Path caminhoDestino = Paths.get(pastaBackup.getAbsolutePath(), arquivo.getName());
                        Files.copy(caminhoOrigem, caminhoDestino, StandardCopyOption.REPLACE_EXISTING);

                        // Log da operação de backup
                        registrarOperacaoBackup(arquivo.getName(), caminhoDestino.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void registrarOperacaoBackup(String nomeArquivo, String caminhoDestino) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        String mensagemLog = String.format("[%s] Backup do arquivo %s realizado com sucesso. Destino: %s",
                timestamp, nomeArquivo, caminhoDestino);

        // Atualiza a interface gráfica com a mensagem de log
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                labelStatus.setText(mensagemLog);
            }
        });

        // Escreve a mensagem de log em um arquivo específico
        try (PrintWriter escritor = new PrintWriter(new FileWriter("backup_log.txt", true))) {
            escritor.println(mensagemLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SistemaBackupArquivosGUI gui = new SistemaBackupArquivosGUI();
                gui.setVisible(true);
            }
        });
    }
}
