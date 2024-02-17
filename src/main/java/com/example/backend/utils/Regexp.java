package com.example.backend.utils;

import com.example.backend.dao.ConstantDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class Regexp {

    @Autowired
    @Qualifier("constantDaoImpl")
    private ConstantDaoImpl constantDao;

    public String getPodImage(String podName){
        String regex = (String) constantDao.get("podNameRegex");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(podName);
        boolean find = matcher.find();
        if(!find){
            log.warn("podName {} does not match the pattern {}",podName,regex);
            return "";
        }
        return matcher.group();
    }

    public int getPodId(String podName){
        String regex = (String) constantDao.get("podIdRegex");
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(podName);
        boolean find = matcher.find();
        if(!find){
            log.warn("podName {} does not match the pattern {}",podName,regex);
            return -1;
        }
        return Integer.parseInt(matcher.group());
    }
}
