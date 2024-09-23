package com.vinhos.bd.controller;

import com.vinhos.bd.dao.CarrinhoVinhoDAO;
import com.vinhos.bd.dao.DAO;
import com.vinhos.bd.dao.DAOFactory;
import com.vinhos.bd.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "")
public class AcaoCompraController {

    CarrinhoVinhoDAO carrinhoVinhoDAO;
    DAO<CompraCarrinhoVinho, CompraCarrinhoVinhoID> daoCompraCarrinhoVinho;
    DAO<Compras, Integer> daoCompras;

    DAO<Carrinho, Integer> daoCarrinho;

    @PostMapping(value = "/comprar")
    public void realizarCompra (@RequestBody Carrinho carrinho, HttpServletResponse response, HttpServletRequest request) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            response.getWriter().write("Usuário não logado!");
            return;
        }

        List<CarrinhoVinho> carrinhoVinhoList;

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            carrinhoVinhoDAO = daoFactory.getCarrinhoVinhoDAO();
            daoCompraCarrinhoVinho = daoFactory.getCompraCarrinhoVinhoDAO();
            daoCompras = daoFactory.getComprasDAO();
            daoCarrinho = daoFactory.getCarrinhoDAO();

            carrinhoVinhoList = carrinhoVinhoDAO.findAllVinhosOfCarrinho(carrinho.getNumero());

            if (carrinhoVinhoList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404 Not Found
                response.getWriter().write("Carrinho não encontrado.");
            } else {
                Random random = new Random();
                int randomId = random.nextInt((1000000 - 1)) + 1;

                Compras compras = new Compras();
                compras.setNumero(randomId);
                daoCompras.create(compras);

                for (CarrinhoVinho item : carrinhoVinhoList) {
                    CarrinhoVinhoID carrinhoVinhoID = new CarrinhoVinhoID(item.getId().getNumeroCarrinho(), item.getId().getNumeroVinho());
                    CompraCarrinhoVinhoID compraCarrinhoVinhoID = new CompraCarrinhoVinhoID(randomId, carrinhoVinhoID);
                    CompraCarrinhoVinho compraCarrinhoVinho = new CompraCarrinhoVinho();

                    compraCarrinhoVinho.setId(compraCarrinhoVinhoID);
                    compraCarrinhoVinho.setQuantidade(item.getQuantidade());
                    compraCarrinhoVinho.setSubtotal(item.getSubtotal());
                    daoCompraCarrinhoVinho.create(compraCarrinhoVinho);
                }

                daoCarrinho.delete(carrinho.getNumero());

                response.setStatus(HttpServletResponse.SC_OK); // 200 Created
                response.getWriter().write("Compra realizada com sucesso!");
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);   // 500 Internal server error
            response.getWriter().write("Erro: " + ex.getMessage());
        }

    }
}
