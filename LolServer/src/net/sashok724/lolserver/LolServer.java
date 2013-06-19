package net.sashok724.lolserver;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LolServer implements ActionListener, LolServerCall
{
	private int totalc = 0;
	private JTextField port, version, protocol, current_players, maximum_players, mood, kick;
	private JButton start_button;
	private JFrame frame;
	private JLabel total;
	
	public LolServer()
	{
		initialize();
	}
	
	@Override public void actionPerformed(ActionEvent e)
	{
		try
		{
			LolServerWorker worker = new LolServerWorker(this);
			tryConfigureServer(worker);
			for(Component component : frame.getContentPane().getComponents())
			{
				if(component instanceof JLabel) continue;
				component.setEnabled(false);
			}
			start_button.setText("Запуск ЛОЛ-сервера...");
			new Thread(worker).start();
		} catch(LolServerException e1)
		{
			JOptionPane.showMessageDialog(frame, e1.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void tryConfigureServer(LolServerWorker worker) throws LolServerException
	{
		try
		{
			worker.port = new Integer(port.getText());
			if(worker.port < 0 || worker.port > 65535) throw new IllegalArgumentException();
		} catch(Exception e)
		{
			throw new LolServerException("Вы указали неправильный порт");
		}
		try
		{
			String[] splitted = version.getText().split("\\.");
			if(splitted.length != 3) throw new IllegalArgumentException();
			worker.mc_version = new int[] { new Integer(splitted[0]), new Integer(splitted[1]), new Integer(splitted[2]) };
			if(worker.mc_version[2] < 0 || worker.mc_version[2] > 9) throw new IllegalArgumentException();
			if(worker.mc_version[1] < 0 || worker.mc_version[1] > 9) throw new IllegalArgumentException();
			if(worker.mc_version[0] != 1) throw new IllegalArgumentException();
		} catch(Exception e)
		{
			throw new LolServerException("Вы указали несуществующюю версию майнкрафта.");
		}
		try
		{
			worker.nt_version = new Integer(protocol.getText());
			if(worker.nt_version < 0 || worker.nt_version > 0xFF) throw new IllegalArgumentException();
		} catch(Exception e)
		{
			throw new LolServerException("Вы указали несуществующюю версию протокола.");
		}
		try
		{
			worker.maximum_players = new Integer(maximum_players.getText());
			if(worker.maximum_players < 0) throw new IllegalArgumentException();
		} catch(Exception e)
		{
			throw new LolServerException("Вы указали неправильное количество максимально возможных игроков.");
		}
		try
		{
			worker.current_players = new Integer(current_players.getText());
			if(worker.maximum_players < 0) throw new IllegalArgumentException();
		} catch(Exception e)
		{
			throw new LolServerException("Вы указали неправильное количество текущих игроков.");
		}
		if(worker.maximum_players < worker.current_players) throw new LolServerException("У вас игроков больше, чем максимально возможно =D");
		if(kick.getText().isEmpty()) throw new LolServerException("Вы не указали сообщение для статуса.");
		if(kick.getText().isEmpty()) throw new LolServerException("Вы не указали сообщение для кика.");
		worker.kick = kick.getText().replaceAll("&", String.valueOf((char)167));
		worker.mood = mood.getText().replaceAll("&", String.valueOf((char)167));
	}

	private void initialize()
	{
		frame = new JFrame();
		frame.setTitle("LolServer");
		frame.setResizable(false);
		frame.setSize(392, 281);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ================ И ПОЛИЛОСЬ ГОВНО ОТСЮДА ============
		start_button = new JButton("Запустить сервер ЛОЛ");
		start_button.setBounds(10, 198, 366, 44);
		start_button.addActionListener(this);
		JLabel port_label = new JLabel("Порт сервера:");
		port_label.setHorizontalAlignment(SwingConstants.RIGHT);
		port_label.setBounds(10, 11, 150, 14);
		JLabel version_label = new JLabel("Версия сервера:");
		version_label.setHorizontalAlignment(SwingConstants.RIGHT);
		version_label.setBounds(10, 36, 150, 14);
		JLabel protocol_label = new JLabel("Версия протокола:");
		protocol_label.setHorizontalAlignment(SwingConstants.RIGHT);
		protocol_label.setBounds(10, 61, 150, 14);
		JLabel current_players_label = new JLabel("Сейчас игроков:");
		current_players_label.setHorizontalAlignment(SwingConstants.RIGHT);
		current_players_label.setBounds(10, 86, 150, 14);
		JLabel max_players_label = new JLabel("Всего игроков:");
		max_players_label.setHorizontalAlignment(SwingConstants.RIGHT);
		max_players_label.setBounds(10, 111, 150, 14);
		JLabel mood_label = new JLabel("Статус сервера:");
		mood_label.setHorizontalAlignment(SwingConstants.RIGHT);
		mood_label.setBounds(10, 136, 150, 14);
		JLabel kick_label = new JLabel("Кик-сообщение:");
		kick_label.setHorizontalAlignment(SwingConstants.RIGHT);
		kick_label.setBounds(10, 161, 150, 14);
		port = new JTextField();
		port.setText("25565");
		port.setBounds(170, 8, 206, 20);
		frame.getContentPane().add(port);
		port.setColumns(10);
		version = new JTextField();
		version.setText("1.5.2");
		version.setColumns(10);
		version.setBounds(170, 33, 206, 20);
		protocol = new JTextField();
		protocol.setText("61");
		protocol.setColumns(10);
		protocol.setBounds(170, 58, 206, 20);
		current_players = new JTextField();
		current_players.setText("50");
		current_players.setColumns(10);
		current_players.setBounds(170, 83, 206, 20);
		maximum_players = new JTextField();
		maximum_players.setText("100");
		maximum_players.setColumns(10);
		maximum_players.setBounds(170, 108, 206, 20);
		mood = new JTextField();
		mood.setText("Best fake server!");
		mood.setColumns(10);
		mood.setBounds(170, 133, 206, 20);
		kick = new JTextField();
		kick.setText("You tried connect to fake server!");
		kick.setColumns(10);
		kick.setBounds(170, 158, 206, 20);
		total = new JLabel("Всего было обработано подключений: " + totalc);
		total.setBounds(12, 185, 356, 14);
		// ==================== ДО СЮДА! =================
		frame.getContentPane().add(current_players_label);
		frame.getContentPane().add(max_players_label);
		frame.getContentPane().add(current_players);
		frame.getContentPane().add(protocol_label);
		frame.getContentPane().add(version_label);
		frame.getContentPane().add(start_button);
		frame.getContentPane().add(maximum_players);
		frame.getContentPane().add(mood_label);
		frame.getContentPane().add(port_label);
		frame.getContentPane().add(kick_label);
		frame.getContentPane().add(protocol);
		frame.getContentPane().add(version);
		frame.getContentPane().add(total);
		frame.getContentPane().add(mood);
		frame.getContentPane().add(kick);
		frame.setVisible(true);
	}
	
	@Override public void onErrorLaunching()
	{
		for(Component component : frame.getContentPane().getComponents())
		{
			if(component instanceof JLabel) continue;
			component.setEnabled(true);
		}
		start_button.setText("Ошибка! Пробуем еще раз?");
	}
	
	@Override public void onPlayerConnected(String ip)
	{
		total.setText("Всего было обработано подключений: " + totalc++);
	}
	
	@Override public void onSuccessLaunching()
	{
		start_button.setText("Сервер LOL успешно запущен!");
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
	{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new LolServer();
	}
}