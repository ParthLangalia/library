window.onload = function () {
   var user = JSON.parse(localStorage.getItem("currentUser"));
   if(user && user.name){
	   var welcomeTag= document.getElementById("welcomeUser")
	   welcomeTag.innerText = "Welcome, " + user.name;
   }
   loadBorrowedBooks();
   var borrowForm = document.getElementById("borrowForm");
   if (borrowForm) {
       borrowForm.onsubmit = function (e) {
           e.preventDefault();
           var bookId = document.getElementById("bookId").value;
           if (confirm("Are you sure you want to borrow this book?")) {
               borrowBookById(bookId);
           }
       };
   }
   var searchForm = document.getElementById("searchForm");
   if (searchForm) {
       searchForm.onsubmit = function (e) {
           e.preventDefault();
           searchBooks();
       };
   }
};
function logout() {
   localStorage.clear();
   window.location.href = "index.html";
}
function searchBooks() {
   var title = document.getElementById("searchTitle").value;
   var author = document.getElementById("searchAuthor").value;
   var genre = document.getElementById("searchGenre").value;
   var url = "http://localhost:8080/LibraryManagement/api/books/all";
   if (title) {
       url = "http://localhost:8080/LibraryManagement/api/books/title/" + title;
   } else if (author) {
       url = "http://localhost:8080/LibraryManagement/api/books/author/" + author;
   } else if (genre) {
       url = "http://localhost:8080/LibraryManagement/api/books/genre/" + genre;
   }
   var xhr = new XMLHttpRequest();
   xhr.open("GET", url, true);
   xhr.onreadystatechange = function () {
       if (xhr.readyState === 4 && xhr.status === 200) {
           var books = JSON.parse(xhr.responseText);
           var resultDiv = document.getElementById("searchResults");
           resultDiv.innerHTML = "";
           for (var i = 0; i < books.length; i++) {
               var book = books[i];
               var card = document.createElement("div");
               card.className = "col-md-4";
               card.innerHTML =
                   '<div class="card mb-3"><div class="card-body">' +
                   '<h5>' + book.title + '</h5>' +
                   '<p>Author: ' + book.author + '<br>Genre: ' + book.genre + '<br>Available: ' + book.availableCopies + '</p>' +
                   '<button class="btn btn-success" onclick="confirmBorrow(' + book.id + ')" ' + (book.availableCopies === 0 ? 'disabled' : '') + '>Borrow</button>' +
                   '</div></div>';
               resultDiv.appendChild(card);
           }
       }
   };
   xhr.send();
}
function confirmBorrow(bookId) {
   if (confirm("Are you sure you want to borrow this book?")) {
       borrowBookById(bookId);
   }
}
function borrowBookById(bookId) {
   var userId = localStorage.getItem("userId");
   var url = "http://localhost:8080/LibraryManagement/api/borrowed/borrow?bookId=" + bookId + "&userId=" + userId;
   var xhr = new XMLHttpRequest();
   xhr.open("POST", url, true);
   xhr.onreadystatechange = function () {
       if (xhr.readyState === 4) {
           alert(xhr.responseText);
           document.getElementById("searchForm").dispatchEvent(document.createEvent("Event")); // force refresh
           loadBorrowedBooks();
       }
   };
   xhr.send();
}
function loadBorrowedBooks() {
   var userId = localStorage.getItem("userId");
   var xhr = new XMLHttpRequest();
   xhr.open("GET", "http://localhost:8080/LibraryManagement/api/borrowed/all", true);
   xhr.onreadystatechange = function () {
       if (xhr.readyState === 4 && xhr.status === 200) {
           var allBorrowed = JSON.parse(xhr.responseText);
           var borrowedBooks = [];
           for (var i = 0; i < allBorrowed.length; i++) {
               if (allBorrowed[i].user.id == userId) {
                   borrowedBooks.push(allBorrowed[i]);
               }
           }
           var list = document.getElementById("borrowedBooksList");
           list.innerHTML = "";
           for (var j = 0; j < borrowedBooks.length; j++) {
               var bb = borrowedBooks[j];
               var card = document.createElement("div");
               card.className = "col";
               card.innerHTML =
                   '<div class="card h-100"><div class="card-body">' +
                   '<h5>' + bb.book.title + '</h5>' +
                   '<p>Author: ' + bb.book.author + '<br>' +
                   'Borrowed: ' + new Date(bb.borrowDate).toLocaleDateString() + '<br>' +
                   'Due: ' + bb.returnDate + '<br>' +
                   'Status: ' + (bb.returned ? "Returned" : "Borrowed") + '</p>' +
                   (!bb.returned ? '<button class="btn btn-warning" onclick="returnBook(' + bb.id + ')">Return</button>' : "") +
                   '</div></div>';
               list.appendChild(card);
           }
       }
   };
   xhr.send();
}
function returnBook(borrowId) {
   if (!confirm("Are you sure you want to return this book?")) return;
   var url = "http://localhost:8080/LibraryManagement/api/borrowed/return?borrowId=" + borrowId + "&returned=true&daysOverdue=0";
   var xhr = new XMLHttpRequest();
   xhr.open("PUT", url, true);
   xhr.onreadystatechange = function () {
       if (xhr.readyState === 4) {
           alert("Book returned successfully!");
           loadBorrowedBooks();
       }
   };
   xhr.send();
}