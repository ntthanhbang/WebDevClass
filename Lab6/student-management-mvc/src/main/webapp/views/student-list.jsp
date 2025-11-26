<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List - MVC</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }

        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 32px;
        }

        .subtitle {
            color: #666;
            margin-bottom: 15px;
            font-style: italic;
        }

        .toolbar {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            align-items: center;
            gap: 10px;
            margin-bottom: 20px;
        }

        .search-box form,
        .filter-box form {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 8px;
        }

        .search-input {
            padding: 8px 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
            min-width: 220px;
        }

        .filter-select {
            padding: 8px 10px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-weight: 500;
        }

        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .btn {
            display: inline-block;
            padding: 10px 18px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 500;
            transition: all 0.3s;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        .btn-danger {
            background-color: #dc3545;
            color: white;
            padding: 8px 16px;
            font-size: 13px;
        }

        .btn-danger:hover {
            background-color: #c82333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            font-weight: 600;
            text-transform: uppercase;
            font-size: 13px;
            letter-spacing: 0.5px;
        }

        th a {
            color: inherit;
            text-decoration: none;
        }

        th a:hover {
            text-decoration: underline;
        }

        tbody tr {
            transition: background-color 0.2s;
        }

        tbody tr:hover {
            background-color: #f8f9fa;
        }

        .actions {
            display: flex;
            gap: 10px;
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }

        .empty-state-icon {
            font-size: 64px;
            margin-bottom: 20px;
        }

        .pagination {
            margin: 20px 0 5px;
            text-align: center;
        }

        .pagination a,
        .pagination span {
            padding: 6px 10px;
            margin: 0 3px;
            border-radius: 4px;
            border: 1px solid #ddd;
            text-decoration: none;
            font-size: 13px;
            color: #333;
        }

        .pagination a:hover {
            background-color: #f1f1f1;
        }

        .pagination .active-page {
            background-color: #667eea;
            color: white;
            border-color: #667eea;
        }

        .page-info {
            text-align: center;
            color: #666;
            font-size: 13px;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <div class="navbar">
        <h2>📚 Student Management System</h2>
        <div class="navbar-right">
            <div class="user-info">
                <span>Welcome, ${sessionScope.fullName}</span>
                <span class="role-badge role-${sessionScope.role}">
                    ${sessionScope.role}
                </span>
            </div>
            <a href="dashboard" class="btn-nav">Dashboard</a>
            <a href="logout" class="btn-logout">Logout</a>
        </div>
    </div>

<div class="container">
    <h1>📚 Student List</h1>

    <!-- Success / Error Messages -->
    <c:if test="${not empty param.message}">
        <div class="message success">
            ✅ ${param.message}
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="message error">
            ❌ ${param.error}
        </div>
    </c:if>

    <!-- Toolbar: Search, Filter, Add New -->
    <div class="toolbar">

        <!-- Search box  -->
        <div class="search-box">
            <form action="student" method="get">
                <input type="hidden" name="action" value="search">
                <input type="text"
                       name="keyword"
                       class="search-input"
                       placeholder="🔍 Search by code, name, or email"
                       value="${keyword}">
                <button type="submit" class="btn btn-secondary">Search</button>
                <c:if test="${not empty keyword}">
                    <a href="student?action=list" class="btn btn-secondary">Clear</a>
                </c:if>
            </form>
        </div>

        <!-- Filter by major -->
        <div class="filter-box">
            <form action="student" method="get">
                <input type="hidden" name="action" value="filter">
                <label for="filterMajor">Filter by Major:</label>
                <select id="filterMajor" name="major" class="filter-select">
                    <option value="">All Majors</option>
                    <option value="Computer Science"
                        ${selectedMajor == 'Computer Science' ? 'selected' : ''}>
                        Computer Science
                    </option>
                    <option value="Information Technology"
                        ${selectedMajor == 'Information Technology' ? 'selected' : ''}>
                        Information Technology
                    </option>
                    <option value="Software Engineering"
                        ${selectedMajor == 'Software Engineering' ? 'selected' : ''}>
                        Software Engineering
                    </option>
                    <option value="Business Administration"
                        ${selectedMajor == 'Business Administration' ? 'selected' : ''}>
                        Business Administration
                    </option>
                </select>
                <button type="submit" class="btn btn-secondary">Apply</button>
                <c:if test="${not empty selectedMajor}">
                    <a href="student?action=list" class="btn btn-secondary">Clear Filter</a>
                </c:if>
            </form>
        </div>

        <!-- Add button - Admin only -->
        <c:if test="${sessionScope.role eq 'admin'}">
            <div style="margin: 20px 0;">
                <a href="student?action=new" class="btn-add">➕ Add New Student</a>
            </div>
        </c:if>
    </div>

    <!-- State messages: search / filter / sort -->
    <c:if test="${not empty keyword}">
        <p class="subtitle">Search results for: <strong>${keyword}</strong></p>
    </c:if>

    <c:if test="${not empty selectedMajor}">
        <p class="subtitle">Filtered by major: <strong>${selectedMajor}</strong></p>
    </c:if>

    <c:if test="${not empty sortBy}">
        <p class="subtitle">
            Sorted by: <strong>${sortBy}</strong>
            <c:choose>
                <c:when test="${order == 'desc'}">(descending)</c:when>
                <c:otherwise>(ascending)</c:otherwise>
            </c:choose>
        </p>
    </c:if>

    <!-- Student Table -->
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Code</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Major</th>
                    <c:if test="${sessionScope.role eq 'admin'}">
                        <th>Actions</th>
                    </c:if>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="student" items="${students}">
                    <tr>
                        <td>${student.id}</td>
                        <td>${student.studentCode}</td>
                        <td>${student.fullName}</td>
                        <td>${student.email}</td>
                        <td>${student.major}</td>
                        
                        <!-- Action buttons - Admin only -->
                        <c:if test="${sessionScope.role eq 'admin'}">
                            <td>
                                <a href="student?action=edit&id=${student.id}" 
                                   class="btn-edit">Edit</a>
                                <a href="student?action=delete&id=${student.id}" 
                                   class="btn-delete"
                                   onclick="return confirm('Delete this student?')">Delete</a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>
                
                <c:if test="${empty students}">
                    <tr>
                        <td colspan="6" style="text-align: center;">
                            No students found
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>

    <!-- Pagination -->
    <c:if test="${not empty totalPages and totalPages > 1}">
        <div class="pagination">
            <!-- Previous -->
            <c:if test="${currentPage > 1}">
                <a href="student?action=list&page=${currentPage - 1}">« Previous</a>
            </c:if>

            <!-- Page numbers -->
            <c:forEach begin="1" end="${totalPages}" var="i">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <span class="active-page">${i}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="student?action=list&page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!-- Next -->
            <c:if test="${currentPage < totalPages}">
                <a href="student?action=list&page=${currentPage + 1}">Next »</a>
            </c:if>
        </div>
        <p class="page-info">
            Showing page ${currentPage} of ${totalPages}
        </p>
    </c:if>
</div>
</body>
</html>