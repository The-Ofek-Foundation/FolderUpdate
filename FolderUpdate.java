public class FolderUpdate {

	private FolderUpdate() {}

	public static void run(String... params) {
		boolean exact = false;
		for (String param : params)
			switch (param) {
				case "exact":
					exact = true;
					break;
			}
		OFile folderFrom = new OFile(OPrompt.getDirectory("Name of the folder to copy from", true));
		OFile folderTo = new OFile(OPrompt.getDirectory("Name of the folder to copy to  ", false));

		System.out.println();

		double startTime = System.nanoTime();
		int totalParsed = copyContents(folderFrom, folderTo, exact);
		double endTime = System.nanoTime();

		System.out.printf("\nParsed %d files in %.3f seconds.\n\n", totalParsed, (endTime - startTime) / 1E9);
	}

	public static int copyContents(OFile folderFrom, OFile folderTo, boolean exact) {
		OFile[] folderFromContents = folderFrom.listFiles();
		OFile[] folderToContents = folderTo.listFiles();
		OFile fromFile = null, toFile = null;
		int toIndex = 0;
		int [] containsFileData = new int[2];
		String folderToPath = folderTo.getPath() + '/';
		int totalParsed = 0;

		for (int i = 0; i < folderFromContents.length; i++) {
			fromFile = folderFromContents[i];
			containsFileData = containsFile(fromFile, folderToContents, toIndex);
			toIndex = containsFileData[1];

			if (containsFileData[0] == 1) { // contains file
				toFile = folderToContents[toIndex];
				if (fromFile.isDirectory())
					totalParsed += copyContents(fromFile, toFile, exact);
				else if (!fromFile.equals(toFile)) {
					fromFile.copyReplace(folderToPath);
					System.out.println("Replaced " + removePathLayer(fromFile.getPath()));
				}
				folderToContents[toIndex] = null;
				toIndex++;
			} else {
				toFile = fromFile.copyReplace(folderToPath);
				System.out.println("Copied " + removePathLayer(fromFile.getPath()));
				if (fromFile.isDirectory())
					totalParsed += copyContents(fromFile, toFile, exact);
			}
			totalParsed++;
		}

		if (exact)
			for (int i = 0; i < folderToContents.length; i++)
				if (folderToContents[i] != null) {
					System.out.println("Deleted " + folderToContents[i].getPath());
					folderToContents[i].delete();
				}
		return totalParsed;
	}

	private static int[] containsFile(OFile file, OFile[] fileList, int index) {
		String name = file.getName();
		for (int i = index; i < fileList.length; i++)
			if (name.equals(fileList[i].getName()))
				return new int[] {1, i};
		return new int[] {0, index};
	}

	private static String removePathLayer(String path) {
		return path.substring(path.indexOf('/') + 1);
	}

	public static void main(String... pumpkins) {
		FolderUpdate.run(pumpkins);
	}
}