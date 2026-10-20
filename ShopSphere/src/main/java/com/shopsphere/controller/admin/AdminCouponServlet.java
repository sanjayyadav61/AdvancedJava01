package com.shopsphere.controller.admin;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Controller for Admin Coupon Management.
 */
@WebServlet(name = "AdminCouponServlet", urlPatterns = {"/admin/coupons"})
public class AdminCouponServlet extends HttpServlet {

    private CouponDAO couponDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        couponDAO = new CouponDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Coupon> coupons = couponDAO.findAll();
        request.setAttribute("coupons", coupons);

        request.getRequestDispatcher("/admin/coupons.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("save".equalsIgnoreCase(action)) {
                String idStr = request.getParameter("couponId");
                int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;
                String code = request.getParameter("code");
                String discountType = request.getParameter("discountType");
                double discountValue = Double.parseDouble(request.getParameter("discountValue"));
                double minimumOrder = Double.parseDouble(request.getParameter("minimumOrder"));
                double maximumDiscount = Double.parseDouble(request.getParameter("maximumDiscount"));
                String expiryStr = request.getParameter("expiryDate");
                Date expiryDate = (expiryStr != null && !expiryStr.isEmpty()) ? Date.valueOf(expiryStr) : null;
                boolean status = request.getParameter("status") != null;

                Coupon c = new Coupon();
                c.setCouponId(id);
                c.setCode(code);
                c.setDiscountType(discountType);
                c.setDiscountValue(discountValue);
                c.setMinimumOrder(minimumOrder);
                c.setMaximumDiscount(maximumDiscount);
                c.setExpiryDate(expiryDate);
                c.setStatus(status);

                if (id > 0) {
                    couponDAO.update(c);
                } else {
                    couponDAO.create(c);
                }
                request.getSession().setAttribute("adminSuccess", "Coupon saved successfully!");
            } else if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("couponId"));
                couponDAO.delete(id);
                request.getSession().setAttribute("adminSuccess", "Coupon deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("adminError", "Operation failed: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/admin/coupons");
    }
}
