package me.herobrinedobem.heventos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.herobrinedobem.heventos.api.EventoBaseAPI;
import me.herobrinedobem.heventos.api.EventoCancellType;
import me.herobrinedobem.heventos.api.events.StopEvent;
import me.herobrinedobem.heventos.databases.Database;
import me.herobrinedobem.heventos.databases.DatabaseType;
import me.herobrinedobem.heventos.hooks.LegendChat;
import me.herobrinedobem.heventos.utils.AutoStartEvents;
import me.herobrinedobem.heventos.utils.ConfigUtil;
import me.herobrinedobem.heventos.utils.EventosController;
import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

/**
 * Classe principal do projeto, aqui onde o plugin sera iniciado e todas as suas
 * funcoes serao chamadas! Refeito por GabrielDev (DeathRush)!
 * 
 * @author Herobrinedobem (Gabriel Henrique)
 * @author GabrielDev (DeathRush)
 * @version 1.6.0
 */
public class HEventos extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");
	private List<EventoBaseAPI> externalEventos = new ArrayList<>();
	private EventosController eventosController;
	private Database databaseManager;
	private Economy economy = null;
	private SimpleClans sc;
	private LegendChat lc;
	private ConfigUtil configUtil;
	private MainListeners mainListeners = new MainListeners();

	/**
	 * M�todo que e chamado quando o plugin iniciar, serao criadas as configs, as
	 * dependencias serao ligadas a database sera selecionada, os comandos e
	 * listeners serao criados e a instancia das classes {@link EventosController} e
	 * {@link AutoStartEvents} serao criadas.
	 */
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fIniciando o plugin!");
		loadConfigs();
		loadDependencies();
		loadDatabase();
		loadCommands();
		loadListeners();
		loadEventos();
		Bukkit.getConsoleSender().sendMessage(
				"�9[HEventosReloaded] �fPlugin Habilitado - (Versao �9" + this.getDescription().getVersion() + "�f)");
		Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fRefeito por �aGabrielDev");
	}

	/**
	 * Metodo que sera chamado quando o plugin for desativado, nele iremos verificar
	 * se existe algum evento ocorrendo, se tiver iremos cancelar e mandar os
	 * jogadores para o local de saida.
	 */
	@Override
	public void onDisable() {
		// Verificamos se nao ha nenhum evento ocorrendo no momento
		if (this.eventosController.getEvento() != null) {
			// Caso tenha algum evento ocorrendo criamos o listener relacionado a quando um
			// evento e cancelado
			StopEvent event = new StopEvent(HEventos.getHEventos().getEventosController().getEvento(),
					EventoCancellType.SERVER_STOPED);
			HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
			// Cancelando o evento
			this.eventosController.getEvento().stopEvent();
			eventosController.setEvento(null);
		}
		Bukkit.getConsoleSender().sendMessage(
				"�9[HEventosReloaded] �fPlugin Desabilitado - (Versao �9" + this.getDescription().getVersion() + "�f)");
	}

	/**
	 * Metodo que ira carregar as configs do plugin.
	 */
	private void loadConfigs() {
		if (!new File(this.getDataFolder(), "config.yml").exists()) {
			this.saveDefaultConfig();
			Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fConfig.yml criada com sucesso!");
		} else {
			Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fConfig.yml carregada com sucesso!");
		}
		File eventosFile = new File(this.getDataFolder() + File.separator + "Eventos");
		if (!eventosFile.exists()) {
			eventosFile.mkdirs();
			Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fPasta 'Eventos' criada com sucesso!");
		}
		if (this.getConfig().getBoolean("Ativar_Configs_Exemplos")) {
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "eventoexemplo.yml")
					.exists()) {
				this.saveResource("Eventos" + File.separator + "eventoexemplo.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "batataquente.yml")
					.exists()) {
				this.saveResource("Eventos" + File.separator + "batataquente.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "spleef.yml").exists()) {
				this.saveResource("Eventos" + File.separator + "spleef.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "killer.yml").exists()) {
				this.saveResource("Eventos" + File.separator + "killer.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "minamortal.yml")
					.exists()) {
				this.saveResource("Eventos" + File.separator + "minamortal.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "paintball.yml")
					.exists()) {
				this.saveResource("Eventos" + File.separator + "paintball.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "semaforo.yml")
					.exists()) {
				this.saveResource("Eventos" + File.separator + "semaforo.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "bowspleef.yml")
					.exists()) {
				this.saveResource("Eventos" + File.separator + "bowspleef.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "frog.yml").exists()) {
				this.saveResource("Eventos" + File.separator + "frog.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Eventos" + File.separator + "fight.yml").exists()) {
				this.saveResource("Eventos" + File.separator + "fight.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "Tags.yml").exists()) {
				this.saveResource("Tags.yml", false);
			}
			if (!new File(this.getDataFolder() + File.separator + "GUIA PARA ITENS.yml").exists()) {
				this.saveResource("GUIA PARA ITENS.yml", false);
			}
			Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fConfigs de exemplos criadas!");
		}
		this.configUtil = new ConfigUtil();
	}

	/**
	 * Metodo que ira carregar as dependencias do plugin.
	 */
	private void loadDependencies() {
		if (!setupSimpleClans()) {
			log.severe(String.format("[%s] - SimpleClans n�o encontrado!", getDescription().getName()));
		}
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
		}
		if (!setupLegendChat()) {
			log.severe(String.format("[%s] - LegendChat n�o encontrado, Listeners com chat desativados!",
					getDescription().getName()));
		}
	}

	/**
	 * Metodo que ira carregar o banco de dados do plugin.
	 */
	private void loadDatabase() {
		if (this.getConfig().getBoolean("MySQL.Ativado") == true) {
			databaseManager = new Database(DatabaseType.MYSQL, this.getConfig().getString("MySQL.Usuario"),
					this.getConfig().getString("MySQL.Senha"), this.getConfig().getString("MySQL.Database"),
					this.getConfig().getString("MySQL.Host"));
			Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fMySQL Habilitado!");
		} else {
			File databaseFile;
			databaseFile = new File(this.getDataFolder() + File.separator + "database.db");
			if (!databaseFile.exists()) {
				try {
					databaseFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fDatabase.db criada com sucesso!");
			}
			databaseManager = new Database(DatabaseType.SQLITE);
			Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fSQLite Habilitado!");
		}
	}

	/**
	 * Metodo que ira carregar os comandos do plugin.
	 */
	private void loadCommands() {
		this.getCommand("evento").setExecutor(new Comandos(this));
	}

	/**
	 * Metodo que ira carregar os listeners do plugin.
	 */
	private void loadListeners() {
		this.getServer().getPluginManager().registerEvents(this.mainListeners, this);
	}

	/**
	 * Metodo que ira criar a instancia das classes {@link EventosController} e
	 * {@link AutoStartEvents}.
	 */
	private void loadEventos() {
		this.eventosController = new EventosController(this);
		this.eventosController.setEvento(null);
		AutoStartEvents.AutoStart();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}

	private boolean setupSimpleClans() {
		Plugin plug = this.getServer().getPluginManager().getPlugin("SimpleClans");
		if (plug != null) {
			Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fSimpleClans 1 encontrado com sucesso!");
			this.sc = ((SimpleClans) plug);
			return true;
		}
		return false;
	}

	private boolean setupLegendChat() {
		Plugin plug = getServer().getPluginManager().getPlugin("LegendChat");
		if (plug != null) {
			Bukkit.getConsoleSender().sendMessage("�9[HEventosReloaded] �fLegendChat encontrado com sucesso!");
			this.lc = new LegendChat();
			this.getServer().getPluginManager().registerEvents(this.lc, this);
			return true;
		}
		this.lc = null;
		return false;
	}

	public Economy getEconomy() {
		return this.economy;
	}

	public EventosController getEventosController() {
		return this.eventosController;
	}

	public SimpleClans getSc() {
		return this.sc;
	}

	public LegendChat getLc() {
		return lc;
	}
	
	public ConfigUtil getConfigUtil() {
		return this.configUtil;
	}
	
	public Database getDatabaseManager() {
		return databaseManager;
	}

	public List<EventoBaseAPI> getExternalEventos() {
		return externalEventos;
	}
	
	public static HEventos getHEventos() {
		return (HEventos) Bukkit.getServer().getPluginManager().getPlugin("HEventosReloaded");
	}
}
