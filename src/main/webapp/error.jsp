<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .error-container {
            text-align: center;
            padding: 40px;
            margin-top: 50px;
        }
        .error-message {
            color: #f44336;
            font-size: 18px;
            margin-bottom: 20px;
        }
        .back-btn {
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
    </style>
</head>
<body>
    <div class="container">
        <div class="error-container">
            <h1>Error</h1>
            <div class="error-message">
                <s:if test="message != null">
                    <s:property value="message"/>
                </s:if>
                <s:else>
                    An error occurred while processing your request.
                </s:else>
            </div>
            <a href="${pageContext.request.contextPath}/home.jsp" class="back-btn">Back to Home</a>
        </div>
    </div>
</body>
</html> 