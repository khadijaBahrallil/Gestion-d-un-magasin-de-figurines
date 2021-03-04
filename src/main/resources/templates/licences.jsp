<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
        <title>404 figurines</title>
    </head>
    <body>
        <form action="/addLicence" method="post"  modelAttribute="category">
            <p>Licences</p>
            <form:select path="categoryList">
                <form:option value="-" label="--Choisir une categorie--"/>
                <form:options items="${ categoryList }" itemValue="category" itemLabel="category" ></form:options>
            </form:select>
            <div class="form-group">
                <label for="name">Nom de la nouvelle licence:</label>
                <input type="text" class="form-control" id="name" placeholder="Nom de la licence" name="name">
            </div>
        </form>

    </body>
</html>