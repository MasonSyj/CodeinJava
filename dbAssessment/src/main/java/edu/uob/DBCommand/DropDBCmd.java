package edu.uob.DBCommand;


import edu.uob.DBCommand.Command;

import java.io.File;

public class DropDBCmd extends Command {

	public DropDBCmd(String DBName, String tableName) {
		super(DBName, tableName);
	}

	public void deleteDirectory(File directory){
		try{
			if (directory.isDirectory()){
				File[] files = directory.listFiles();
				if (files != null) {
					System.out.println(files.length);
					for (int i = 0; i < files.length; i++) {
						if (files[i].isDirectory()){
							deleteDirectory(files[i]);
						}else{
							files[i].delete();
						}
					}
				}
				directory.delete();
			}

		} catch (Exception e){
			throw new RuntimeException("[ERROR] happened , Database failed to drop");
		}
	}
	@Override
	public String execute() {
		File directoryRepresentDB = new File("databases" + File.separator + getDBName());
		if (!directoryRepresentDB.exists()){
			return "[ERROR] , Database doesn't exist";
		}

		deleteDirectory(directoryRepresentDB);
		directoryRepresentDB.delete();

		if (directoryRepresentDB.exists()){
			return "[ERROR] , Database failed to drop";
		}else{
			return "[OK] ， Database droped succesfully";
		}

//		Path path = Paths.get("databases" + File.separator + getDBName());
//		try {
//			Files.walk(path)
//					.sorted(java.util.Comparator.reverseOrder())
//					.map(Path::toFile)
//					.forEach(File::delete);
//					Files.delete(path);
//			return "[OK] ， Database drop successfully";
//		} catch (Exception e) {
//			return "[ERROR] , Database failed to drop: " + e.getMessage();
//		}


	}
}