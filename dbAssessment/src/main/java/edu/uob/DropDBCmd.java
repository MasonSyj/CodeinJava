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

	public String deleteDirectory(File directory){
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

			if(!directory.delete()){
				return "[ERROR] , Database failed to drop";
			}else{
				return "[OK] ， Database droped succesfully";
			}
		} catch (Exception e){
			return "[ERROR] , Database failed to drop";
		}
	}
	@Override
	public String execute() {
		File directoryRepresentDB = new File("databases" + File.separator + getDBName());
			if (!directoryRepresentDB.exists()){
				return "[ERROR] , Database doesn't exist";
			}

			return deleteDirectory(directoryRepresentDB);



	}
}
