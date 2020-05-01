package br.com.segala.domotica.enums;

public enum SensorEnum {

	FRENTE(2614286, "Porta da frente"),
	ESCRITORIO(469022, "Porta do escritório"),
	QUARTO(2434334, "Porta do quarto"),
	SUITE(2224398, "Porta da suite"),
	FUNDOS(2286862, "Porta dos fundos"),
	COZINHA(15147294, "Janela da cozinha"),
	SALA(534558, "Janela da sala"),
	
	CONTROLE_1_A(15610952, "Controle 1 Armando"),
	CONTROLE_1_D(15610948, "Controle 1 Disarmando"),
	
	CONTROLE_2_A(5409960, "Controle 2 Armando"),
	CONTROLE_2_D(5409956, "Controle 2 Disarmando"),
	CONTROLE_2_P(5409953, "Controle 2 Parcial"),
	CONTROLE_2_S(5409954, "Controle 2 Sonoro"),
	
	MOVIMENTO_1(5680262, "Movimento 1");
	
	private Integer rfid;
	private String description;
	
	private SensorEnum(Integer rfid, String deLocal) {
		this.rfid = rfid;
		this.description = deLocal;
	}

	public static SensorEnum sensor(Integer rfid) {
		for (SensorEnum sensor : values()) {
			if (sensor.rfid.equals(rfid)) {
				return sensor;
			}
		}
		
		System.out.println("Rfid desconhecido: " + rfid);
		return null;
	}

	public Integer getRfid() {
		return rfid;
	}

	public String getDescription() {
		return description;
	}

}
