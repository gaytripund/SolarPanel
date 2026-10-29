<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<html>
<head>
    <title>Solar Panel Records</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div class="container result-container">
    <h1>Saved Latest 5 Records</h1>
    <table>

        <tr>
            <th>ID</th>
            <th>Panel ID</th>
            <th>Date</th>
            <th>Units (kWh)</th>
        </tr>
        <%
        List<Map<String, Object>> solarList =
            (List<Map<String, Object>>) request.getAttribute("solarList");

        if (solarList != null) {
            for (Map<String, Object> solar : solarList) {
        %>
        <tr>
            <td><%= solar.get("id") %></td>
            <td><%= solar.get("panel_id") %></td>
            <td><%= solar.get("log_date") %></td>
            <td><%= solar.get("kwh") %></td>
        </tr>

        <%
            }
        }
        %>

    </table>
    <div class="total">
        Total Units (kWh):
        <b><%= request.getAttribute("totalKwh") %></b>
    </div>
    <br>
    <a href="index.html">
        <button type="button">Add New Record</button>
    </a>

</div>

</body>
</html>
