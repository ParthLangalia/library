<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Borrow Confirmation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .success-container {
            text-align: center;
            padding: 40px;
            margin-top: 50px;
        }
        .success-message {
            color: #4CAF50;
            font-size: 18px;
            margin-bottom: 20px;
        }
        .button-container {
            margin-top: 30px;
        }
        .btn {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 4px;
        }
        .btn-secondary {
            background-color: #2196F3;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="success-container">
            <h1>Book Borrowed Successfully</h1>
            <div class="success-message">
                <s:property value="message"/>
            </div>
            <div class="button-container">
                <a href="${pageContext.request.contextPath}/books/search" class="btn">Find More Books</a>
                <a href="${pageContext.request.contextPath}/books/userBooks?userId=<s:property value="userId"/>" class="btn btn-secondary">View My Books</a>
            </div>
        </div>
    </div>
</body>
</html> 