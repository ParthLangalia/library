<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Book Search Results</title>
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
        }
        .book-title {
            font-weight: bold;
            font-size: 18px;
            margin-bottom: 10px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            cursor: pointer;
        }
        .book-details {
            display: none;
            margin-top: 10px;
            padding-top: 10px;
            border-top: 1px solid #eee;
        }
        .expand-icon {
            font-size: 20px;
        }
        .borrow-btn {
            background-color: #4CAF50;
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
        <h1>Book Search Results</h1>
        
        <!-- Search form -->
        <form action="search" method="post">
            <div class="search-form">
                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" value="<s:property value="title"/>">
                </div>
                <div class="form-group">
                    <label for="author">Author:</label>
                    <input type="text" id="author" name="author" value="<s:property value="author"/>">
                </div>
                <div class="form-group">
                    <label for="genre">Genre:</label>
                    <input type="text" id="genre" name="genre" value="<s:property value="genre"/>">
                </div>
                <button type="submit">Search</button>
            </div>
        </form>
        
        <!-- Results display -->
        <div class="book-container">
            <s:if test="books != null && !books.isEmpty()">
                <s:iterator value="books">
                    <div class="book-box">
                        <div class="book-title" onclick="toggleDetails(this)">
                            <span><s:property value="title"/></span>
                            <span class="expand-icon">▼</span>
                        </div>
                        <div class="book-details">
                            <p><strong>Author:</strong> <s:property value="author"/></p>
                            <p><strong>Genre:</strong> <s:property value="genre"/></p>
                            <p><strong>Available Copies:</strong> <s:property value="availableCopies"/></p>
                            <button class="borrow-btn" onclick="borrowBook(<s:property value="bookId"/>)">Borrow</button>
                        </div>
                    </div>
                </s:iterator>
            </s:if>
            <s:else>
                <p>No books found matching your criteria.</p>
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
        function toggleDetails(element) {
            const details = element.nextElementSibling;
            if (details.style.display === "none" || details.style.display === "") {
                details.style.display = "block";
                element.querySelector(".expand-icon").innerHTML = "▲";
            } else {
                details.style.display = "none";
                element.querySelector(".expand-icon").innerHTML = "▼";
            }
        }
        
        function borrowBook(bookId) {
            // Add the current user ID - this should come from session in a real app
            const userId = 1; // Example user ID
            
            // Make AJAX call to borrow endpoint
            fetch('${pageContext.request.contextPath}/books/borrow', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `bookId=${bookId}&userId=${userId}`
            })
            .then(response => response.text())
            .then(data => {
                document.getElementById('popupMessage').innerText = "Book borrowed successfully!";
                document.getElementById('confirmationPopup').style.display = "block";
            })
            .catch(error => {
                document.getElementById('popupMessage').innerText = "Error: " + error;
                document.getElementById('confirmationPopup').style.display = "block";
            });
        }
        
        function closePopup() {
            document.getElementById('confirmationPopup').style.display = "none";
        }
        
        // Initialize all book details to be hidden
        document.addEventListener('DOMContentLoaded', function() {
            const details = document.querySelectorAll('.book-details');
            details.forEach(detail => {
                detail.style.display = "none";
            });
        });
    </script>
</body>
</html> 