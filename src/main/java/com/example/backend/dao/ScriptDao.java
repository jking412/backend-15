package com.example.backend.dao;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ScriptDao {
    public String getScript(String scriptName) throws IOException;
}
