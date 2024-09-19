package com.vinhos.bd.controller;

import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.model.MyAppUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(value="/user")
public class MyAppUserController {

    DAO<MyAppUser, String> dao;
    @PostMapping (value="/create")
    public void createUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();

            MyAppUser user = new MyAppUser();
            HttpSession session = request.getSession();

            user.setNome(request.getParameter("nome"));
            user.setEmail(request.getParameter("email"));
            user.setSenha(request.getParameter("senha"));
            dao.create(user);
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            response.getWriter().write("Usuário criado com sucesso!");
        } catch (SQLException | ClassNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/update")
    public void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();

            MyAppUser user = new MyAppUser();
            HttpSession session = request.getSession();

            user.setNome(request.getParameter("nome"));
            user.setEmail(request.getParameter("email"));
            user.setSenha(request.getParameter("senha"));
            dao.update(user);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Usuário atualizado com sucesso!");
        } catch (SQLException | ClassNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping (value = "/delete")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();
            HttpSession session = request.getSession();

            String emailToDelete = request.getParameter("email");
            dao.delete(emailToDelete);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Usuário deletado com sucesso!");
        } catch (SQLException | ClassNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }
}
