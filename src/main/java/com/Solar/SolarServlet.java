package com.Solar;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/SolarServlet")
public class SolarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String panel_id = request.getParameter("panel_id");
        String log_date = request.getParameter("log_date");
        String kwh = request.getParameter("kwh");

        try (Connection con = DBConnection.getConnection()) {

            String insertSql =
                    "INSERT INTO solar_log (panel_id, log_date, kwh) "
                            + "VALUES (?, ?, ?)";

            PreparedStatement insert =
                    con.prepareStatement(insertSql);

            insert.setString(1, panel_id);
            insert.setDate(2, java.sql.Date.valueOf(log_date));
            insert.setDouble(3, Double.parseDouble(kwh));

            insert.executeUpdate();

            String sql =
                    "SELECT id, panel_id, log_date, kwh "
                            + "FROM solar_log "
                            + "ORDER BY log_date DESC, id DESC "
                            + "LIMIT 5";

            List<Map<String, Object>> solarList =
                    new ArrayList<>();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Map<String, Object> solar = new HashMap<>();

                solar.put("id", rs.getInt("id"));
                solar.put("panel_id", rs.getString("panel_id"));
                solar.put("log_date", rs.getDate("log_date"));
                solar.put("kwh", rs.getDouble("kwh"));

                solarList.add(solar);
            }
            String totalSql =
                    "SELECT SUM(kwh) AS total FROM solar_log";

            PreparedStatement totalPs = con.prepareStatement(totalSql);
            ResultSet totalRs = totalPs.executeQuery();

            double totalKwh = 0;

            if (totalRs.next()) {
                totalKwh = totalRs.getDouble("total");
            }

            request.setAttribute("solarList", solarList);
            request.setAttribute("totalKwh", totalKwh);

            request.getRequestDispatcher("result.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                    "Error retrieving solar records", e
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(405, "Please submit the form");
    }
}
