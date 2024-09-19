package com.vinhos.bd.controller;

import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.model.MyAppUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "MyAppUserController",
        urlPatterns = {
            "/user/create",
            "/user/update",
            "/user/delete"
})
public class MyAppUserController extends HttpServlet {

    @Override
     protected void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DAO<MyAppUser, String> dao;
        MyAppUser user = new MyAppUser();
        HttpSession session = request.getSession();

        String servletPath = request.getServletPath();

        if (request.getParameter("nome") == null || request.getParameter("email") == null || request.getParameter("senha") == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Parâmetros necessários estão faltando.");
            return;
        }

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();

            switch (servletPath) {
                case "/user/create": {
                    user.setNome(request.getParameter("nome"));
                    user.setEmail(request.getParameter("email"));
                    user.setSenha(request.getParameter("senha"));
                    dao.create(user);
                    response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
                    response.getWriter().write("Usuário criado com sucesso!");
                    break;
                }
                case "/user/update": {
                    user.setNome(request.getParameter("nome"));
                    user.setEmail(request.getParameter("email"));
                    user.setSenha(request.getParameter("senha"));
                    dao.update(user);
                    response.setStatus(HttpServletResponse.SC_OK); // 200 OK
                    response.getWriter().write("Usuário atualizado com sucesso!");
                    break;
                }
                case "/user/delete": {
                    String emailToDelete = request.getParameter("email");
                    dao.delete(emailToDelete);
                    response.setStatus(HttpServletResponse.SC_OK); // 200 OK
                    response.getWriter().write("Usuário deletado com sucesso!");
                    break;
                }
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
                    response.getWriter().write("Ação inválida.");
                    break;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }
}
