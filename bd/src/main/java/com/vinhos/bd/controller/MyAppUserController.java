package com.vinhos.bd.controller;

import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.model.MyAppUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value="/user")
public class MyAppUserController {

    DAO<MyAppUser, String> dao;

    @GetMapping(value = "/read")
    public MyAppUser getUser(@RequestBody MyAppUser user, HttpServletResponse response) throws ServletException, IOException {
        MyAppUser userAux;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();

            userAux = dao.read(user.getEmail());

            if (userAux == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Usuário não encontrado.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return userAux;
    }

    @GetMapping(value = "/all")
    public List<MyAppUser> getAllUsers(HttpServletResponse response) throws ServletException, IOException {
        List<MyAppUser> listAllUsers;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();

            listAllUsers = dao.all();

            if (listAllUsers.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Ainda não há usuários.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return  listAllUsers;
    }

    @PostMapping(value="/create")
    public void createUser(@RequestBody MyAppUser user, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();

            // Agora os dados do usuário vêm do corpo da requisição JSON
            dao.create(user);
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            response.getWriter().write("Usuário criado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/update")
    public void updateUser(@RequestBody MyAppUser user, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();

            // Agora os dados do usuário vêm do corpo da requisição JSON
            dao.update(user);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Usuário atualizado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/delete")
    public void deleteUser(@RequestBody MyAppUser user, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getMyAppUserDAO();

            // O email a ser deletado ainda pode ser passado como parâmetro
            dao.delete(user.getEmail());
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Usuário deletado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }
}
