public class FolderUpdate {

	private FolderUpdate() {}

	public static void run() {
		OFile folderFrom = new OFile(OPrompt.getDirectory("Name of the folder to copy from", true));
		OFile folderTo = new OFile(OPrompt.getDirectory("Name of the folder to copy to  ", true));
	}

	public static void main(String... pumpkins) {
		FolderUpdate.run();
	}
}