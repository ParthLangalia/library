// Auth operations
const authOperations = {
    // Login user
    login: function(email, password) {
        // For simplicity, we're using a direct query to check credentials
        // In a real application, you'd use a secure endpoint with proper authentication
        return new Promise((resolve, reject) => {
            // Simple validation
            if (!email || !password) {
                reject(new Error('Email and Password are required'));
                return;
            }
            
            fetch(`/api/users/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Login failed');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        // Store user info in localStorage
                        localStorage.setItem('userId', data.userId);
                        localStorage.setItem('userName', data.name || email.split('@')[0]);
                        resolve(data);
                    } else {
                        reject(new Error(data.message || 'Invalid credentials'));
                    }
                })
                .catch(error => {
                    // If API fails, we'll simulate login for demo purposes
                    // REMOVE THIS IN PRODUCTION - this is just for demonstration
                    console.warn('Using simulated login. Remove in production.');
                    const simulatedUsers = [
                        { email: 'user1@example.com', password: 'password123', userId: '1', name: 'User One' },
                        { email: 'librarian1@example.com', password: 'password123', userId: '2', name: 'Librarian' }
                    ];
                    
                    const user = simulatedUsers.find(u => 
                        u.email === email && u.password === password);
                    
                    if (user) {
                        localStorage.setItem('userId', user.userId);
                        localStorage.setItem('userName', user.name);
                        resolve({ success: true, ...user });
                    } else {
                        reject(new Error('Invalid credentials'));
                    }
                });
        });
    },
    
    // Register new user
    register: function(name, email, password, role) {
        // In a real app, you'd make an API call to register the user
        // For this demo, we'll simulate success
        return new Promise((resolve, reject) => {
            // Simple validation
            if (!name || !email || !password || !role) {
                reject(new Error('All fields are required'));
                return;
            }
            
            // Simulate successful registration
            setTimeout(() => {
                resolve({
                    success: true,
                    message: 'Registration successful',
                    name: name,
                    email: email,
                    role: role
                });
            }, 500);
        });
    },
    
    // Check if user is logged in
    isLoggedIn: function() {
        return !!localStorage.getItem('userId');
    },
    
    // Logout user
    logout: function() {
        localStorage.removeItem('userId');
        localStorage.removeItem('userName');
    }
};

// Initialize login form
document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
            
            const email = document.querySelector('input[name="email"]').value;
            const password = document.querySelector('input[name="password"]').value;
            
            // Display loading state
            const submitBtn = loginForm.querySelector('button[type="submit"]');
            const originalText = submitBtn.textContent;
            submitBtn.textContent = 'Logging in...';
            submitBtn.disabled = true;
            
            // Attempt login
            authOperations.login(email, password)
                .then(data => {
                    // Redirect to dashboard on success
                    window.location.href = 'userDash.html';
                })
                .catch(error => {
                    // Display error message
                    const messageDiv = document.getElementById('message');
                    if (messageDiv) {
                        messageDiv.textContent = error.message;
                    } else {
                        alert(error.message);
                    }
                    
                    // Reset button
                    submitBtn.textContent = originalText;
                    submitBtn.disabled = false;
                });
        });
    }
    
    // Initialize register form
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            
            const name = document.querySelector('input[name="name"]').value;
            const email = document.querySelector('input[name="email"]').value;
            const password = document.querySelector('input[name="password"]').value;
            const role = document.querySelector('select[name="role"]').value;
            
            // Display loading state
            const submitBtn = registerForm.querySelector('button[type="submit"]');
            const originalText = submitBtn.textContent;
            submitBtn.textContent = 'Registering...';
            submitBtn.disabled = true;
            
            // Attempt registration
            authOperations.register(name, email, password, role)
                .then(data => {
                    // Redirect to welcome page with parameters
                    const params = new URLSearchParams({
                        name: name,
                        email: email,
                        role: role
                    });
                    window.location.href = `welcome.jsp?${params.toString()}`;
                })
                .catch(error => {
                    // Display error message
                    const messageDiv = document.getElementById('message');
                    if (messageDiv) {
                        messageDiv.textContent = error.message;
                    } else {
                        alert(error.message);
                    }
                    
                    // Reset button
                    submitBtn.textContent = originalText;
                    submitBtn.disabled = false;
                });
        });
    }
    
    // Check if user is already logged in and redirect if appropriate
    if (authOperations.isLoggedIn()) {
        // If on login/register page, redirect to dashboard
        const currentPath = window.location.pathname;
        if (currentPath.endsWith('index.html')) {
            window.location.href = 'userDash.html';
        }
    } else {
        // If on dashboard, redirect to login
        const currentPath = window.location.pathname;
        if (currentPath.endsWith('userDash.html')) {
            window.location.href = 'index.html';
        }
    }
});