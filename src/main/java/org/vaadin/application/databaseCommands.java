package org.vaadin.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class databaseCommands{

    private JdbcTemplate jdbc = new JdbcTemplate();

    public databaseCommands(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Integer> totalActiveLocation(String area) {
        String sql = "SELECT id FROM centers WHERE status = 1 AND UPPER(TRIM(postcode)) LIKE ?";
        return jdbc.queryForList(sql, Integer.class, area + "%");
    }

    public List<Integer> getCenters() {
        String sql = "SELECT id FROM centers LIMIT 5";
        try {
            return jdbc.queryForList(sql, Integer.class);
        } catch (DataAccessException e) {
            System.err.println("CRITICAL DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<String> centreInfo(int id) {
        List<String> info = new ArrayList<>();

        String sql = "SELECT c.name, c.address_line_1, c.address_line_2, c.city, c.postcode, c.country, c.website, c.email, c.phone , GROUP_CONCAT(p.name SEPARATOR ', ') AS products FROM centers c LEFT JOIN center_products cp ON c.id = cp.center_id LEFT JOIN products p ON cp.product_id = p.id WHERE c.id = ? AND c.status = 1 GROUP BY c.id";

        jdbc.query(sql, rs -> {
            info.add(rs.getString("name"));
            info.add(rs.getString("address_line_1"));
            info.add(rs.getString("address_line_2"));
            info.add(rs.getString("city"));
            info.add(rs.getString("postcode"));
            info.add(rs.getString("country"));
            info.add(rs.getString("products"));
            info.add(rs.getString("website"));
            info.add(rs.getString("email"));
            info.add(rs.getString("phone"));
        }, id);

        return info;
    }

    public String countActive(){
        String sql = "SELECT count(id) FROM centers WHERE status = 1 ";
        return jdbc.queryForObject(sql, String.class);
    }

    public String productInfo(String product){
        String sql = "SELECT price FROM products WHERE name = ? ";
        return jdbc.queryForObject(sql, String.class, product);

    }

    public List<String[]> allProductInfo(){
        List<String[]> info = new ArrayList<>();

        String sql = "SELECT p.name, p.price, p.SKU, COUNT(CASE WHEN c.completed = 1 THEN 1 END) AS completed_count, COUNT(CASE WHEN c.completed = 0 THEN 1 END) AS waiting_count FROM products p LEFT JOIN collections c ON p.id = c.product_id where p.active = 1 GROUP BY p.id, p.name, p.price, p.SKU ";

        jdbc.query(sql, rs -> {
            info.add(new String[]{
                rs.getString("name"),
                rs.getString("price"),
                rs.getString("SKU"),
                rs.getString("completed_count"),
                rs.getString("waiting_count")
            });
        });

        return info;
    }

    public List<String[]> collectedProductInfo(){
        List<String[]> info = new ArrayList<>();

            String sql = "SELECT p.name, COUNT(CASE WHEN c.completed = 1 AND YEAR(c.due_date) = YEAR(CURRENT_DATE()) AND MONTH(c.due_date) = MONTH(CURRENT_DATE()) THEN 1 END) AS month_completed, COUNT(CASE WHEN c.completed = 1 AND YEAR(c.due_date) = YEAR(CURRENT_DATE()) THEN 1 END) AS year_completed, COUNT(CASE WHEN c.completed = 1 THEN 1 END) AS all_time_completed FROM products p LEFT JOIN collections c ON p.id = c.product_id WHERE p.active = 1 GROUP BY p.id, p.name";

        jdbc.query(sql, rs -> {
            info.add(new String[]{
                rs.getString("name"),
                rs.getString("month_completed"),
                rs.getString("year_completed"),
                rs.getString("all_time_completed")
            });
        });

        return info;
    }
    
    public List<String[]> allCentreInfo() {
        List<String[]> info = new ArrayList<>();

        String sql = "SELECT c.name, c.status, c.city, c.join_date, c.postcode,  c.email, GROUP_CONCAT(p.name SEPARATOR ', ') AS products FROM centers c LEFT JOIN center_products cp ON c.id = cp.center_id LEFT JOIN products p ON cp.product_id = p.id GROUP BY c.id";

        jdbc.query(sql, rs -> {
            String statusTrnas = switch (rs.getInt("status")){
                case 1 -> "Active";
                case 0 -> "Inactive";
                default -> "Inactive";
            };

            info.add(new String[]{
                rs.getString("name"),
                statusTrnas,
                rs.getString("city"),
                rs.getString("join_date"),
                rs.getString("postcode"),
                rs.getString("products"),
                rs.getString("email"),
            });
        });

        return info;
    }

    public List<String[]> allCollectionInfo() {
        List<String[]> info = new ArrayList<>();

        String sql = "SELECT c.product_id, ce.name AS center_name, c.due_date, c.completed, c.service FROM collections c LEFT JOIN centers ce ON c.assigned_to = ce.id";

        jdbc.query(sql, rs -> {
            String statusTrnas = switch (rs.getInt("completed")){
                case 1 -> "completed";
                case 0 -> "In-complete";
                default -> "In-complete";
            };

            String productTrnas = switch (rs.getInt("product_id")){
                case 1 -> "Tyres";
                case 2 -> "Tubes";
                case 3 -> "Li-ion Battery";
                default -> "Unknown";
            };

            info.add(new String[]{
                productTrnas,
                rs.getString("center_name"),
                rs.getString("due_date"),
                statusTrnas,
                rs.getString("service")
            });
        });

        return info;
    }

    public void deleteProduct(String product){
        String sql = "update products set active = 0 where name = ?";
        jdbc.update(sql, product);
    }

    public  void addProduct(String name, String price, String sku){
        BigDecimal priceTrans = new BigDecimal(price);
        
        String sql = "insert into products (name, price , SKU) values (?, ?, ?)";
        jdbc.update(sql, name, priceTrans, sku);
    }

    public void updateProduct(String orgName, String name, String price, String sku){
        BigDecimal priceTrans = new BigDecimal(price);
        
        String sql = "update products set name = ?, price = ? , SKU = ? where name = ?";
        jdbc.update(sql, name, priceTrans, sku, orgName);
    }
    
    public List<String[]> upcomingCollections(){
                List<String[]> info = new ArrayList<>();

        String sql = "SELECT c.product_id, ce.name AS center_name, c.due_date, c.completed, c.service FROM collections c LEFT JOIN centers ce ON c.assigned_to = ce.id where c.completed = 0 order by c.due_date limit 5";

        jdbc.query(sql, rs -> {
            String productTrnas = switch (rs.getInt("product_id")){
                case 1 -> "Tyres";
                case 2 -> "Tubes";
                case 3 -> "Li-ion Battery";
                default -> "Unknown";
            };

            info.add(new String[]{
                productTrnas,
                rs.getString("center_name"),
                rs.getString("due_date"),
                rs.getString("service")
            });
        });

        return info;
    }

    public String commonArea(){
        String sql = "SELECT city FROM centers GROUP BY city ORDER BY COUNT(*) DESC LIMIT 1";
    return jdbc.queryForObject(sql, String.class);
}

    public String growth(){
        String sql = "SELECT ROUND(((SUM(CASE WHEN c.completed = 1 AND YEAR(c.due_date) = YEAR(CURRENT_DATE()) AND MONTH(c.due_date) = MONTH(CURRENT_DATE()) THEN 1 ELSE 0 END) - SUM(CASE WHEN c.completed = 1 AND YEAR(c.due_date) = YEAR(CURRENT_DATE() - INTERVAL 1 MONTH) AND MONTH(c.due_date) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) THEN 1 ELSE 0 END)) / NULLIF(SUM(CASE WHEN c.completed = 1 AND YEAR(c.due_date) = YEAR(CURRENT_DATE() - INTERVAL 1 MONTH) AND MONTH(c.due_date) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) THEN 1 ELSE 0 END), 0)) * 100, 1) FROM collections c";
        
        Double growth = jdbc.queryForObject(sql, Double.class);
        return (growth != null ? growth : 0.0) + "%";
    }

    public List<Integer[]> collectedProductbreakdown(){
        List<Integer[]> info = new ArrayList<>();

            String sql = "SELECT COUNT(CASE WHEN c.completed = 1 THEN 1 END) AS total_returned FROM products p LEFT JOIN collections c ON p.id = c.product_id AND c.completed = 1 WHERE p.active = 1 GROUP BY p.id, p.name";
        
            jdbc.query(sql, rs -> {
            info.add(new Integer[]{
                (rs.getInt("total_returned")),
            });
        });

        return info;
    }
}