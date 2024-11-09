package servlets;

import clases.Usuario;
import clases.ConexionBaseDeDatos;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("guardar".equals(accion)) {
            String nombre = request.getParameter("nombre");
            String correo = request.getParameter("correo");
            String contraseña = request.getParameter("contraseña");

            
            Usuario usuario = new Usuario(0, nombre, correo, contraseña);

            
            try (Connection conn = ConexionBaseDeDatos.conectar()) {
                String sql = "INSERT INTO usuarios (nombre, correo, contraseña) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, usuario.getNombre());
                    stmt.setString(2, usuario.getCorreo());
                    stmt.setString(3, usuario.getContraseña());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

       
        response.sendRedirect("index.html");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("mostrar".equals(accion)) {
            StringBuffer usuariosList = new StringBuffer();

            try (Connection conn = ConexionBaseDeDatos.conectar()) {
                String sql = "SELECT * FROM usuarios";
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        usuariosList.append("<tr>");
                        usuariosList.append("<td>").append(rs.getInt("id")).append("</td>");
                        usuariosList.append("<td>").append(rs.getString("nombre")).append("</td>");
                        usuariosList.append("<td>").append(rs.getString("correo")).append("</td>");
                        usuariosList.append("<td>").append(rs.getString("contraseña")).append("</td>");
                        usuariosList.append("<td><a href='UsuarioServlet?accion=eliminar&id=")
                                      .append(rs.getInt("id")).append("'>Eliminar</a></td>");
                        usuariosList.append("</tr>");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            request.setAttribute("usuarios", usuariosList.toString());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }

        if ("eliminar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));

            try (Connection conn = ConexionBaseDeDatos.conectar()) {
                String sql = "DELETE FROM usuarios WHERE id=?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            response.sendRedirect("UsuarioServlet?accion=mostrar");
        }
    }
}