# Sistema Backup de Arquivos Agendado

## Descrição
Este sistema, desenvolvido em Java, permite configurar e agendar backups automáticos de arquivos de um diretório de origem para um diretório de backup em intervalos regulares. A interface gráfica (GUI) é construída com Swing para facilitar a interação com o usuário.

## Bibliotecas Utilizadas
- javax.swing: Para criação da interface gráfica.
- javax.swing.filechooser.FileNameExtensionFilter: Para filtros no seletor de arquivos.
- java.awt: Para manipulação de componentes gráficos.
- java.awt.event.ActionListener: Para tratamento de eventos de ação.
- java.io: Para operações de entrada e saída de arquivos.
- java.nio.file: Para manipulação de caminhos de arquivos.
- java.text.SimpleDateFormat: Para formatação de datas.
- java.util.Date: Para trabalhar com datas.
- java.util.Timer: Para agendamento de tarefas.
- java.util.TimerTask: Para definir tarefas agendadas.

## Funcionalidades

### Configuração do Backup

- Selecionar Diretório de Origem: O usuário seleciona o diretório de onde os arquivos serão copiados.
- Selecionar Diretório de Backup: O usuário seleciona o diretório para onde os arquivos serão copiados.
- Definir Intervalo de Backup: O usuário especifica o intervalo em horas para a realização dos backups automáticos.

### Execução do Backup

- Agendar Backup: O sistema agenda o backup para ocorrer no intervalo especificado.
- Realizar Backup: No horário agendado, o sistema copia os arquivos do diretório de origem para o diretório de backup.
- Registro de Log: Cada operação de backup é registrada em um arquivo de log (backup_log.txt).

### Como Usar
1. Clique no executavel
2. Uma janela será aberta. Preencha os campos "Local de Origem", "Local de Backup" e "Intervalo de Backup (horas)".
3. Clique em "Selecionar" ao lado de "Local de Origem" e "Local de Backup" para escolher os diretórios.
4. Clique em "Confirmar" para iniciar o agendamento do backup.
5. O backup será realizado automaticamente no intervalo definido, e o status será exibido na janela.
