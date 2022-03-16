package fr.dinoattitude.anthopia.shops;

public enum ShopName {
	
	HEADER("§8[§cAnthopia Shop§8] "),
	OWNER("Vendeur"),
	CUSTOMER("Acheteur"),
	CREATE("Créer un shop ?"),
	UPDATE_PRICE("Modification"),
	UPDATE_ITEM("Changer l'item ?"),
	DELETE("Supprimer ?");
	
	private String name;
	
	private ShopName(String name) {
		this.name = name;
	}
	
	public String getShopName() {
		return name;
	}
	
	@Override
	public String toString() {
		
		if(this == HEADER) {
			return getShopName();
		}
		
		return HEADER + getShopName();
	}
}
