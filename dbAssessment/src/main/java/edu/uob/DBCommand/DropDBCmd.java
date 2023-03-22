package edu.uob.DBCommand;

import edu.uob.DBCommand.Command;
import edu.uob.Exceptions.interpException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class DropDBCmd extends BasicCommand {
	public DropDBCmd(String DBName, String tableName) {
		super(DBName, tableName);
	}

	@Override
	public String execute() throws interpException {
		File directoryDB = new File("databases" + File.separator + getDBName());
		if (!directoryDB.exists()){
			return "[ERROR] , Database doesn't exist";
		}


		try{
			Path directoryPath = directoryDB.toPath();
			Files.walk(directoryPath)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
		} catch (IOException e) {
			throw new interpException("can not delete");
		}

		directoryDB = new File("databases" + File.separator + getDBName());
		directoryDB.delete();
//		String[] entries = directoryDB.list();
//		for(String s: entries){
//			File currentFile = new File(directoryDB.getPath(),s);
//			currentFile.delete();
//		}
//
//		directoryDB.delete();


		if (directoryDB.exists()){
			throw new interpException("[ERROR] happened , Database failed to drop");
		}else{
			return "[OK] ， Database droped succesfully";
		}
	}
}
