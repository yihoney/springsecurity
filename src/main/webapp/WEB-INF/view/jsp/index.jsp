<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8" />
    <title>Main</title>
</head>
<body>
    <h1>Main</h1>

    <div sec:authorize access="isAuthenticated()">
        <span sec:authentication property="name" />,&nbsp;
        <a href="/logout">Logout</a>
    </div>
    <div sec:authorize access="!isAuthenticated()">
        <a href="/redirect-index">Login</a>
    </div>
    <br />
    <a href="/">Go to Main</a>
    <br />
    <br />
    <br />

    <ul>
        <li sec:authorize access="hasRole('ROLE_ADMIN')">
            <a href="/admin/manage">Admin Tool</a>
        </li>
        <li sec:authorize url="/project/1">
            <a href="/project/1">Public Project</a>
        </li>
        <li sec:authorize access= "hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')">
            <a href="/private-project/2">Private Project</a>
        </li>
    </ul>

    <br />

</body>
</html>
