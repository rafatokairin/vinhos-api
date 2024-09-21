package com.vinhos.bd.controller;

import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.dao.MyAppUserDAO;
import com.vinhos.bd.model.MyAppUser;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(value="")
public class LoginController {

    MyAppUserDAO userDAO;

    @GetMapping(value = "/logout")
    public void logoutUser(HttpServletResponse response, HttpServletRequest request) throws SQLException, SecurityException, IOException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Usuario deslogado.");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro ao deslogar usuario.");
        }

    }

    @PostMapping(value = "/login")
    public void loginUser(@RequestBody MyAppUser user, HttpServletResponse response, HttpServletRequest request) throws SQLException, SecurityException, IOException {
        HttpSession session = request.getSession();
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            userDAO = daoFactory.getMyAppUserDAO();

            userDAO.authenticate(user);

            session.setAttribute("usuario", user);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Usuário logado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException | SecurityException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }
}
