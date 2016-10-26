public class FolderUpdate {

	private FolderUpdate() {}

	public static void run() {
		OFile folderFrom = new OFile(OPrompt.getDirectory("Name of the folder to copy from", true));
		OFile folderTo = new OFile(OPrompt.getDirectory("Name of the folder to copy to  ", false));

		System.out.println();

		double startTime = System.nanoTime();
		int totalCopied = copyContents(folderFrom, folderTo);
		double endTime = System.nanoTime();

		System.out.printf("\nCopied %d files in %.3f seconds.\n\n", totalCopied, (endTime - startTime) / 1E9);
	}

	public static int copyContents(OFile folderFrom, OFile folderTo) {
		OFile[] folderFromContents = folderFrom.listFiles();
		OFile[] folderToContents = folderTo.listFiles();
		OFile fromFile = null, toFile = null;
		int toIndex = 0;
		int [] containsFileData = new int[2];
		String folderToPath = folderTo.getPath() + '/';
		int totalCopied = 0;

		for (int i = 0; i < folderFromContents.length; i++) {
			fromFile = folderFromContents[i];
			containsFileData = containsFile(fromFile, folderToContents, toIndex);
			toIndex = containsFileData[1];

			if (containsFileData[0] == 1) { // contains file
				toFile = folderToContents[toIndex];
				if (fromFile.isDirectory())
					totalCopied += copyContents(fromFile, toFile);
				else if (!fromFile.equals(toFile)) {
					fromFile.copyReplace(folderToPath);
					totalCopied++;
					System.out.println("Copied " + removePathLayer(fromFile.getPath()));
				}
			} else {
				toFile = fromFile.copyReplace(folderToPath);
				totalCopied++;
				System.out.println("Copied " + removePathLayer(fromFile.getPath()));
				if (fromFile.isDirectory())
					totalCopied += copyContents(fromFile, toFile);
			}
		}
		return totalCopied;
	}

	private static int[] containsFile(OFile file, OFile[] fileList, int index) {
		String path = removePathLayer(file.getPath());
		for (int i = index; i < fileList.length; i++)
			if (removePathLayer(fileList[i].getPath()).equals(path))
				return new int[] {1, i};
		return new int[] {0, index};
	}

	private static String removePathLayer(String path) {
		return path.substring(path.indexOf('/') + 1);
	}

	public static void main(String... pumpkins) {
		FolderUpdate.run();
	}
}