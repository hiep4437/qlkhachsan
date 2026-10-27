
package service;

import java.util.ArrayList;
import dao.InvoiceDAO;
import model.invoices;

public class InvoiceService {
    InvoiceDAO dao = new InvoiceDAO();

    public int them(invoices i) {
        return dao.them(i);
    }

    public int sua(invoices i) {
        return dao.sua(i);
    }

    public int xoa(invoices i) {
        return dao.xoa(i);
    }

    public ArrayList<invoices> xuat() {
        return dao.xuat();
    }

    public invoices timKiem(invoices i) {
        return dao.timKiem(i);
    }
}
