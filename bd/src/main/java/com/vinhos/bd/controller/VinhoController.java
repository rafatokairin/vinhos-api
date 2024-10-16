package com.vinhos.bd.controller;

import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.dao.VinhoDAO;
import com.vinhos.bd.dto.MenoresEstoquesDTO;
import com.vinhos.bd.dto.VinhosMaisVendidosDTO;
import com.vinhos.bd.model.Vinho;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/vinho")
public class VinhoController {

    VinhoDAO vinhoDAO;
    DAO<Vinho, Integer> dao;

    @GetMapping(value = "/all")
    public List<Vinho> getAllVinhos(HttpServletResponse response) throws ServletException, IOException {
        List<Vinho> listaVinhos;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            listaVinhos = vinhoDAO.all();

            if (listaVinhos.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Nenhum vinho encontrado.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return listaVinhos;
    }

    @GetMapping(value = "/read")
    public Vinho getVinho(@RequestBody Vinho vinho, HttpServletResponse response) throws ServletException, IOException {
        Vinho vinhoAux;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            vinhoAux = vinhoDAO.read(vinho.getNumero());

            if (vinhoAux == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Vinho não encontrado.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }
        return vinhoAux;
    }

    @PostMapping(value = "/search")
    public String getVinhosComposto(@RequestBody Vinho vinho, HttpServletResponse response)
            throws ServletException, IOException {

        List<Vinho> listaVinhos;
        Gson gson = new Gson();

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
        return gson.toJson(listaVinhos);
    }

    @PostMapping(value = "/create")
    public void createVinho(@RequestBody Vinho vinho, HttpServletResponse response) throws ServletException, IOException {
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

    @PostMapping(value = "/update")
    public void updateVinho(@RequestBody Vinho vinho, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            vinhoDAO.update(vinho);
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Vinho atualizado com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/delete")
    public void deleteVinho(@RequestBody Vinho vinho, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            vinhoDAO.delete(vinho.getNumero());
            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
            response.getWriter().write("Vinho excluído com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @GetMapping(value = "/vinhosPorPeriodo")
    public List<VinhosMaisVendidosDTO> getVinhosByPeriodo(@RequestParam String period, HttpServletResponse response) throws ServletException, IOException {
        List<VinhosMaisVendidosDTO> vinhosMaisVendidosDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            vinhosMaisVendidosDTOList = vinhoDAO.fetchMostSoldWinesRecent(period);

            if (vinhosMaisVendidosDTOList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Nenhum vinho encontrado para o período fornecido.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
        return  vinhosMaisVendidosDTOList;
    }

    @GetMapping(value = "/vinhosPorData")
    public List<VinhosMaisVendidosDTO> getVinhosByData(@RequestParam Date data1, @RequestParam Date data2, HttpServletResponse response) throws ServletException, IOException {
        List<VinhosMaisVendidosDTO> vinhosMaisVendidosDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            vinhosMaisVendidosDTOList = vinhoDAO.findVinhosByDataVendido(data1, data2);

            if (vinhosMaisVendidosDTOList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Nenhum vinho encontrado para o período fornecido.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
        return  vinhosMaisVendidosDTOList;
    }

    @GetMapping(value = "/menosEstoque")
    public List<MenoresEstoquesDTO> getVinhosMenosEstoques(@RequestParam String limit, HttpServletResponse response) throws ServletException, IOException {
        List<MenoresEstoquesDTO> menoresEstoquesDTOList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            vinhoDAO = daoFactory.getVinhoDAO();
            menoresEstoquesDTOList = vinhoDAO.fetchLessEstoques(limit);

            if (menoresEstoquesDTOList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Nenhum vinho encontrado para o período fornecido.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
        return menoresEstoquesDTOList;
    }
}
