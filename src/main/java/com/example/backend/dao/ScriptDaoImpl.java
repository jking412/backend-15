package com.example.backend.dao;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Repository
public class ScriptDaoImpl implements ScriptDao{
    @Override
    public String getScript(String scriptName) throws IOException {
        File file = new File(scriptName);
        FileReader fileReader = new FileReader(file);
        char[] buffer = new char[(int) file.length()];
        fileReader.read(buffer);
        fileReader.close();
        return new String(buffer);
    }
}
