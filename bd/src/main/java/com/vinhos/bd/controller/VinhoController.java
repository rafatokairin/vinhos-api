package com.vinhos.bd.controller;

import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.dao.VinhoDAO;
import com.vinhos.bd.model.MyAppUser;
import com.vinhos.bd.model.Vinho;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/vinho")
public class VinhoController {

    VinhoDAO vinhoDAO;
    DAO<Vinho, Integer> dao;

    @GetMapping(value = "/search")
    public List<Vinho> getVinhosComposto(@RequestBody Vinho vinho, HttpServletResponse response)
        throws ServletException, IOException {

        List<Vinho> listaVinhos;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();

            listaVinhos = vinhoDAO.findVinhos(vinho);

            if (listaVinhos.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Vinhos não encontrados.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return  listaVinhos;
    }

    @PostMapping(value = "/create")
    public void createVinho(@RequestBody Vinho vinho, HttpServletResponse response) throws ServletException, IOException{
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getVinhoDAO();

            dao.create(vinho);
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            response.getWriter().write("Vinho criado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/update/{numero}")
    public void updateVinho(@PathVariable Integer numero, @RequestBody Vinho vinho, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            vinho.setNumero(numero);
            vinhoDAO.update(vinho);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Vinho atualizado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/delete/{numero}")
    public void deleteVinho(@PathVariable Integer numero, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            vinhoDAO.delete(numero);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Vinho excluído com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }
}
