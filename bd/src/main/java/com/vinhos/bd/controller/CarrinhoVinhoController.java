package com.vinhos.bd.controller;

import com.vinhos.bd.dao.CarrinhoVinhoDAO;
import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.model.CarrinhoVinho;
import com.vinhos.bd.model.CarrinhoVinhoID;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/carrinhoVinho")
public class CarrinhoVinhoController {

    DAO<CarrinhoVinho, CarrinhoVinhoID> dao;
    CarrinhoVinhoDAO carrinhoVinhoDAO;

    @GetMapping(value = "/search")
    public List<CarrinhoVinho> getAllVinhosOfCarrinho(@RequestBody CarrinhoVinho carrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        List<CarrinhoVinho> carrinhoVinhoList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            carrinhoVinhoDAO = daoFactory.getCarrinhoVinhoDAO();

            carrinhoVinhoList = carrinhoVinhoDAO.findAllVinhosOfCarrinho(carrinhoVinho.getId().getNumeroCarrinho());

            if (carrinhoVinhoList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Carrinho não encontrado.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException  | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return carrinhoVinhoList;
    }

    @GetMapping(value = "/read")
    public CarrinhoVinho readCarrinhoVinho(@RequestBody CarrinhoVinho carrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        CarrinhoVinho carrinhoVinhoAux;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao =daoFactory.getCarrinhoVinhoDAO();

            carrinhoVinhoAux = dao.read(carrinhoVinho.getId());

            if (carrinhoVinhoAux == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);   // 404 Not found
                response.getWriter().write("Item do carrinho não encontrado.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);   // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return carrinhoVinhoAux;
    }

    @GetMapping(value = "/all")
    public List<CarrinhoVinho> getAllCarrinhoVinho(HttpServletResponse response) throws ServletException, IOException{
        List<CarrinhoVinho> carrinhoVinhoList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoVinhoDAO();

            carrinhoVinhoList = dao.all();

            if (carrinhoVinhoList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Ainda não itens em carrinhos.");
                return null;
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Erro: " + ex.getMessage());
            return null;
        }

        return carrinhoVinhoList;
    }

    @PostMapping(value = "/esvazia")
    public void esvaziaCarrinho(@RequestBody CarrinhoVinho carrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            carrinhoVinhoDAO = daoFactory.getCarrinhoVinhoDAO();

            carrinhoVinhoDAO.esvaziaCarrinho(carrinhoVinho.getId().getNumeroCarrinho());
            response.setStatus(HttpServletResponse.SC_OK);  // 200 OK
            response.getWriter().write("Carrinho esvaziado com sucesso.");
        } catch (SQLException | ClassNotFoundException  | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/create")
    public void createCarrinhoVinho(@RequestBody CarrinhoVinho carrinhoVinho, HttpServletResponse response)  throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoVinhoDAO();

            dao.create(carrinhoVinho);
            response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            response.getWriter().write("Vinho adicionado ao carrinho, com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);   // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/update")
    public void updateCarrinhoVinho(@RequestBody CarrinhoVinho carrinhoVinho, HttpServletResponse response) throws ServletException, IOException {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoVinhoDAO();

            dao.update(carrinhoVinho);
            response.setStatus(HttpServletResponse.SC_OK);  // 200 OK
            response.getWriter().write("Relação vinho e carrinho atualizada com sucesso.");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);   // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }

    @PostMapping(value = "/delete")
    public void deleteCarrinhoVinho(@RequestBody CarrinhoVinho carrinhoVinho, HttpServletResponse response) throws ServletException, IOException{
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            dao = daoFactory.getCarrinhoVinhoDAO();

            dao.delete(carrinhoVinho.getId());
            response.setStatus(HttpServletResponse.SC_OK);  // 200 OK
            response.getWriter().write("Vinho deletado do carrinho com sucesso.");
        } catch (SQLException | ClassNotFoundException  | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
        }
    }
}
