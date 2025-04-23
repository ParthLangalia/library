<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration Successful</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light">
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center text-success">Registration Successful!</h3>
                    </div>
                    <div class="card-body text-center">
                        <p>Welcome <strong><span id="userName"></span></strong>!</p>
                        <p>Your account has been successfully created.</p>
                        <p>Email: <span id="userEmail"></span></p>
                        <p>Role: <span id="userRole"></span></p>
                        <div class="mt-4">
                            <a href="login.jsp" class="btn btn-primary">Proceed to Login</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
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
        document.getElementById('userName').textContent = getParameterByName('name') || 'New User';
        document.getElementById('userEmail').textContent = getParameterByName('email') || 'user@example.com';
        document.getElementById('userRole').textContent = getParameterByName('role') || 'USER';
    </script>
</body>
</html> 