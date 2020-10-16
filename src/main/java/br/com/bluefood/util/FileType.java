package br.com.bluefood.util;

public enum FileType {

	PNG("image/png", "png"),
	JPG("image/jpeg", "jpg");
	
	String mimeType;
	String extension;
	
	private FileType(String mimeType, String extension) {
		this.mimeType = mimeType;
		this.extension = extension;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	
	public boolean someOf(String mimeType) {
		return this.mimeType.equalsIgnoreCase(mimeType);
	}
	
	public static FileType of(String mimeType) {
		for(FileType type : values()) {
			if(type.someOf(mimeType)) {
				return type;
			}
		}
		
		return null;
	}
}
