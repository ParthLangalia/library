<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Borrowed Books</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .book-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            margin-top: 20px;
        }
        .book-box {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 15px;
            width: 300px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            display: flex;
            flex-direction: column;
        }
        .book-info {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }
        .return-btn {
            background-color: #f44336;
            color: white;
            border: none;
            padding: 8px 16px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            margin: 4px 2px;
            cursor: pointer;
            border-radius: 4px;
        }
        .popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.3);
            z-index: 1000;
        }
        .popup-content {
            text-align: center;
        }
        .close-popup {
            position: absolute;
            top: 10px;
            right: 10px;
            cursor: pointer;
            font-size: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>My Borrowed Books</h1>
        
        <!-- Results display -->
        <div class="book-container">
            <s:if test="borrowedBooks != null && !borrowedBooks.isEmpty()">
                <s:iterator value="borrowedBooks">
                    <s:if test="!returned">
                        <div class="book-box">
                            <div class="book-info">
                                <span><strong><s:property value="book.title"/></strong></span>
                                <span>Return by: <s:property value="returnDate"/></span>
                            </div>
                            <button class="return-btn" onclick="returnBook(<s:property value="borrowId"/>)">Return</button>
                        </div>
                    </s:if>
                </s:iterator>
            </s:if>
            <s:else>
                <p>You have no borrowed books.</p>
            </s:else>
        </div>
    </div>
    
    <!-- Popup for confirmation -->
    <div id="confirmationPopup" class="popup">
        <span class="close-popup" onclick="closePopup()">&times;</span>
        <div class="popup-content">
            <p id="popupMessage"></p>
        </div>
    </div>
    
    <script>
        function returnBook(borrowId) {
            // Make AJAX call to return endpoint
            fetch('${pageContext.request.contextPath}/books/return', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `bookId=${borrowId}`
            })
            .then(response => response.text())
            .then(data => {
                document.getElementById('popupMessage').innerText = "Book returned successfully!";
                document.getElementById('confirmationPopup').style.display = "block";
                
                // Refresh the page after a short delay
                setTimeout(() => {
                    location.reload();
                }, 2000);
            })
            .catch(error => {
                document.getElementById('popupMessage').innerText = "Error: " + error;
                document.getElementById('confirmationPopup').style.display = "block";
            });
        }
        
        function closePopup() {
            document.getElementById('confirmationPopup').style.display = "none";
        }
    </script>
</body>
</html> 