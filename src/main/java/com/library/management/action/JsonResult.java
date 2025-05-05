package com.library.management.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonResult implements Result {
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void execute(ActionInvocation invocation) throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Object action = invocation.getAction();
        Map<String, Object> jsonResponse = new HashMap<>();

        if (action instanceof BookAction) {
            BookAction bookAction = (BookAction) action;
            jsonResponse.put("success", bookAction.isSuccess());
            jsonResponse.put("message", bookAction.getMessage());
            
            if (bookAction.getBorrowedBooks() != null) {
                jsonResponse.put("books", bookAction.getBorrowedBooks());
            }
            
            if (bookAction.getBooks() != null) {
                jsonResponse.put("books", bookAction.getBooks());
            }
        }

        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(jsonResponse));
        out.flush();
    }
} 
