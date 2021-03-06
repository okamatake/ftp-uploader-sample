package jp.okamatake.ftpsample.firm;

import java.util.HashMap;
import java.util.Map;

import jp.okamatake.ftpsample.firm.command.JDBCDELE;
import jp.okamatake.ftpsample.firm.command.JDBCLIST;
import jp.okamatake.ftpsample.firm.command.JDBCSTOR;
import jp.okamatake.ftpsample.firm.usermanager.JDBCUserManagerFactory;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.command.Command;
import org.apache.ftpserver.command.CommandFactory;
import org.apache.ftpserver.command.CommandFactoryFactory;
import org.apache.ftpserver.command.impl.CWD;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.UserManagerFactory;

/**
 * FtpServerの起動テスト用.<br>
 *
 * @author okamoto
 */
public class FtpServerManager {

	public FtpServer createFtpServer() throws FtpException {
		FtpServerFactory serverFactory = new FtpServerFactory();

		serverFactory.setCommandFactory(getCommandFactory());

		ListenerFactory listenerFactory = new ListenerFactory();
		serverFactory.addListener("default", listenerFactory.createListener());

		UserManagerFactory userManagerFactory = new JDBCUserManagerFactory();
		serverFactory.setUserManager(userManagerFactory.createUserManager());

		return serverFactory.createServer();
	}

	/**
	 * 使用する{@link Command}を持った{@link CommandFactory}を取得します。
	 *
	 * @return {@linkplain CommandFactory}
	 */
	private CommandFactory getCommandFactory() {
		final CommandFactoryFactory cff = new CommandFactoryFactory();

		// TODO: 多分DefaultCommands使った方が良い。それとも、FtpIoSessionが絡むところはすべて危険か？
//		cff.setUseDefaultCommands(false);
		Map<String, Command> commandMap = new HashMap<>();
//		commandMap.put("USER", new USER());
//		commandMap.put("PASS", new PASS());
		/*
		 * TODO: FileSystremViewに頼らない独自のものに変更する。
		 */
		commandMap.put("CWD", new CWD());
//		commandMap.put("TYPE", new TYPE());
//		commandMap.put("PASV", new PASV());
		/*
		 * TODO: FileSystremViewに頼らない独自のものに変更する。
		 */
		commandMap.put("STOR", new JDBCSTOR());
//		commandMap.put("QUIT", new QUIT());

		commandMap.put("LIST", new JDBCLIST());
		commandMap.put("DELE", new JDBCDELE());

		cff.setCommandMap(commandMap);

		return cff.createCommandFactory();
	}
}
