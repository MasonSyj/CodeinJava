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
		File directoryRepresentDB = new File("databases" + File.separator + getDBName());

//		try {
			// Recursively delete all files and subdirectories
//			Files.walk(directoryRepresentDB)
//					.sorted(java.util.Comparator.reverseOrder())
//					.map(Path::toFile)
//					.forEach(java.io.File::delete);
			if (!directoryRepresentDB.exists()){
				return "[ERROR] , Database doesn't exist";
			}

			try{
				if (directoryRepresentDB.isDirectory()){
					File[] files = directoryRepresentDB.listFiles();
					for (int i = 0; i < files.length; i++){
						files[i].delete();
					}
				}

				if(!directoryRepresentDB.delete()){
					return "[ERROR] , Database failed to drop";
				}else{
					return "[OK] ， Database droped succesfully";
				}
				// Delete the top-level directory itself
//			Files.deleteIfExists(directoryRepresentDB);


			} catch (Exception e){
				return "[ERROR] , Database failed to drop";
			}

//		} catch (Exception e) {
//			return "[ERROR] , Database failed to drop";
//		}

	}
}
