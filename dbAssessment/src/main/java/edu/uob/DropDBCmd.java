package edu.uob;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DropDBCmd extends Command{

	public DropDBCmd(String DBName, String tableName) {
		super(DBName, tableName);
	}

	@Override
	public String execute() {
		Path directoryRepresentDB = Paths.get(getDBName());

		try {
			// Recursively delete all files and subdirectories
			Files.walk(directoryRepresentDB)
					.sorted(java.util.Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(java.io.File::delete);

			// Delete the top-level directory itself
			Files.deleteIfExists(directoryRepresentDB);

			return "[OK]， Database droped succesfully";
		} catch (IOException e) {
			return "[ERROR], Database failed to drop";
		}

	}
}
