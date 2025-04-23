<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Library Management - Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="text-center mb-4">
            <h2>Library Management System</h2>
            <p class="lead">Welcome <span id="userName"></span> (<span id="userEmail"></span>)</p>
        </div>
        
        <div class="row">
            <div class="col-md-12 mb-4">
                <div class="card">
                    <div class="card-header">
                        <h4>Search Books</h4>
                    </div>
                    <div class="card-body">
                        <form id="searchForm" action="${pageContext.request.contextPath}/books/search" method="post">
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <input type="text" class="form-control" name="title" placeholder="Title">
                                </div>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="author" placeholder="Author">
                                </div>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="genre" placeholder="Genre">
                                </div>
                                <div class="col-md-2">
                                    <button type="submit" class="btn btn-primary w-100">Search</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h4>Books Currently Borrowed</h4>
                        <a href="${pageContext.request.contextPath}/books/userBooks" class="btn btn-outline-primary">View All Borrowed Books</a>
                    </div>
                    <div class="card-body">
                        <div id="borrowedBooks" class="row">
                            <!-- Borrowed books will be displayed here -->
                            <p class="text-center text-muted">No books currently borrowed</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Hidden user ID field to use with API calls -->
    <input type="hidden" id="userId" value="1">
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Simple script to get URL parameters
        function getParameterByName(name, url = window.location.href) {
            name = name.replace(/[\[\]]/g, '\\$&');
            var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
                results = regex.exec(url);
            if (!results) return null;
            if (!results[2]) return '';
            return decodeURIComponent(results[2].replace(/\+/g, ' '));
        }
        
        // Populate user info
        document.getElementById('userName').textContent = getParameterByName('name') || 'User';
        document.getElementById('userEmail').textContent = getParameterByName('email') || 'user@example.com';
        
        // Load borrowed books
        window.addEventListener('DOMContentLoaded', function() {
            const userId = document.getElementById('userId').value;
            
            // Make API call to get user's borrowed books
            fetch(`${pageContext.request.contextPath}/books/userBooks?userId=${userId}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    // This would redirect to the user books page
                    // For now, we'll just note that it was called
                    console.log('Borrowed books API called');
                })
                .catch(error => {
                    console.error('Error fetching borrowed books:', error);
                });
        });
    </script>
</body>
</html> 