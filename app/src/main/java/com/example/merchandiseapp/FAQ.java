package com.example.merchandiseapp;

import java.util.HashMap;

public class FAQ {
    private String Question;
    private String Answer;

    public FAQ(String question, String answer) {
        Question = question;
        Answer = answer;
    }

    public FAQ()
    {

    }

    public HashMap<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>() ;
        result.put("Question", Question) ;
        result.put("Answer", Answer) ;

        return result ;

    }
    
}
