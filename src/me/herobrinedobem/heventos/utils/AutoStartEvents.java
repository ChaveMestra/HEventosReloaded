package me.herobrinedobem.heventos.utils;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.herobrinedobem.heventos.HEventos;
import me.herobrinedobem.heventos.api.EventoType;
import me.herobrinedobem.heventos.api.events.StartEvent;

public class AutoStartEvents {

	public static void AutoStart() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(HEventos.getHEventos(), new Runnable() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int minute = cal.get(Calendar.MINUTE);
				int day = cal.get(Calendar.DAY_OF_WEEK);
				for (String s : HEventos.getHEventos().getConfig().getStringList("Horarios")) {
					String[] separadorVip = s.split(";");
					String[] separadorEventos = separadorVip[0].split("-");
					if (HEventos.getHEventos().getEventosController().getEvento() != null)
						return;
					if (separadorEventos.length == 2) {
						if (hour == getHora(separadorEventos[0].split(":")[0])
								&& minute == getMinuto(separadorEventos[0].split(":")[1])
								&& cal.get(Calendar.SECOND) <= 10) {
							if (!HEventos.getHEventos().getEventosController().hasEvento(separadorEventos[1]))
								return;
							YamlConfiguration eventoType = HEventos.getHEventos().getEventosController()
									.getConfigFile(separadorEventos[1]);
							HEventos.getHEventos().getEventosController().setEvento(separadorEventos[1],
									EventoType.getEventoType(eventoType.getString("Config.Evento_Type")));
							if (separadorVip.length == 2) {
								if (separadorVip[1].equals("vip")) {
									HEventos.getHEventos().getEventosController().getEvento().setVip(true);
								} else {
									HEventos.getHEventos().getEventosController().getEvento().setVip(false);
								}
							}
							HEventos.getHEventos().getEventosController().getEvento().run();
							StartEvent event = new StartEvent(HEventos.getHEventos().getEventosController().getEvento(),
									true);
							HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
						}
					} else if (separadorEventos.length == 3) {
						if (day != getDia(separadorEventos[0])) {
							return;
						}
						if (hour == getHora(separadorEventos[1].split(":")[0])
								&& minute == getMinuto(separadorEventos[1].split(":")[1])
								&& cal.get(Calendar.SECOND) <= 10) {
							if (!HEventos.getHEventos().getEventosController().hasEvento(separadorEventos[2]))
								return;
							YamlConfiguration eventoType = HEventos.getHEventos().getEventosController()
									.getConfigFile(separadorEventos[2]);
							HEventos.getHEventos().getEventosController().setEvento(separadorEventos[2],
									EventoType.getEventoType(eventoType.getString("Config.Evento_Type")));
							if (separadorVip.length == 2) {
								if (separadorVip[1].equals("vip")) {
									HEventos.getHEventos().getEventosController().getEvento().setVip(true);
								} else {
									HEventos.getHEventos().getEventosController().getEvento().setVip(false);
								}
							}
							HEventos.getHEventos().getEventosController().getEvento().run();
							StartEvent event = new StartEvent(HEventos.getHEventos().getEventosController().getEvento(),
									true);
							HEventos.getHEventos().getServer().getPluginManager().callEvent(event);
						}
					}
				}
			}
		}, 0L, 100L);
	}

	public static int getDia(String dia) {
		switch (dia) {
		case "domingo":
			dia = "1";
			break;
		case "segunda":
			dia = "2";
			break;
		case "terca":
			dia = "3";
			break;
		case "quarta":
			dia = "4";
			break;
		case "quinta":
			dia = "5";
			break;
		case "sexta":
			dia = "6";
			break;
		case "sabado":
			dia = "7";
			break;
		default:
			break;
		}
		return Integer.parseInt(dia);
	}

	public static int getHora(String hora) {
		switch (hora) {
		case "00": {
			hora = "0";
			break;
		}
		case "01": {
			hora = "1";
			break;
		}
		case "02": {
			hora = "2";
			break;
		}
		case "03": {
			hora = "3";
			break;
		}
		case "04": {
			hora = "4";
			break;
		}
		case "05": {
			hora = "5";
			break;
		}
		case "06": {
			hora = "6";
			break;
		}
		case "07": {
			hora = "7";
			break;
		}
		case "08": {
			hora = "8";
			break;
		}
		case "09": {
			hora = "9";
			break;
		}
		default:
			break;
		}
		return Integer.parseInt(hora);
	}

	public static int getMinuto(String minuto) {
		switch (minuto) {
		case "00": {
			minuto = "0";
			break;
		}
		case "01": {
			minuto = "1";
			break;
		}
		case "02": {
			minuto = "2";
			break;
		}
		case "03": {
			minuto = "3";
			break;
		}
		case "04": {
			minuto = "4";
			break;
		}
		case "05": {
			minuto = "5";
			break;
		}
		case "06": {
			minuto = "6";
			break;
		}
		case "07": {
			minuto = "7";
			break;
		}
		case "08": {
			minuto = "8";
			break;
		}
		case "09": {
			minuto = "9";
			break;
		}
		default:
			break;
		}
		return Integer.parseInt(minuto);
	}
}
