package com.huongbien.dao;

import com.huongbien.entity.Cuisine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CuisineDAO extends GenericDAO<Cuisine> {
    private static final CuisineDAO instance = new CuisineDAO();
    private final CategoryDAO categoryDao;

    public CuisineDAO() {
        super();
        this.categoryDao = CategoryDAO.getInstance();
    }

    public static CuisineDAO getInstance() {
        return instance;
    }

    @Override
    public Cuisine resultMapper(ResultSet resultSet) throws SQLException {
        Cuisine cuisine = new Cuisine();
        cuisine.setCuisineId(resultSet.getString("id"));
        cuisine.setName(resultSet.getString("name"));
        cuisine.setPrice(resultSet.getDouble("price"));
        cuisine.setDescription(resultSet.getString("description"));
        cuisine.setImage(resultSet.getBytes("image"));
        cuisine.setCategory(categoryDao.getById(resultSet.getString("categoryId")));
        return cuisine;
    }

    public List<Cuisine> getByName(String name) {
        return getMany("SELECT id, name, price, description, image, categoryID FROM cuisine WHERE name LIKE ?", "%" + name + "%");
    }

    public List<Cuisine> getAll() {
        return getMany("SELECT id, name, price, description, image, categoryID FROM cuisine");
    }

    public  List<Cuisine> getLookUpCuisine(String name, String category, int pageIndex){
        return getMany("SELECT * FROM cuisine WHERE name LIKE N'%" + name + "%' AND categoryId LIKE N'%"+ category +"%' ORDER BY categoryId OFFSET " + pageIndex + " ROWS FETCH NEXT 7 ROWS ONLY");
    }

    public  int getCountLookUpCuisine(String name, String category){
        return count("SELECT COUNT (*) AS countRow FROM cuisine WHERE name LIKE N'%" + name + "%' AND categoryId LIKE N'%"+ category +"%'");
    }

    public Cuisine getById(String id) {
        return getOne("SELECT id, name, price, description, image, categoryID FROM cuisine WHERE id = ?", id);
    }

    public List<String> getCuisineCategory(){
        List<String> categoryList = new ArrayList<String>();
        String sql = "SELECT DISTINCT categoryId FROM Cuisine";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                categoryList.add(rs.getString("categoryId"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryList;
    }

    public boolean updateCuisineInfo(Cuisine cuisine) {
        String sql = "UPDATE cuisine SET name = ?, price = ?, description = ?, image = ?, categoryID = ? WHERE id = ?";
        return update(sql, cuisine.getName(), cuisine.getPrice(), cuisine.getDescription(), cuisine.getImage(), cuisine.getCategory().getCategoryId(), cuisine.getCuisineId());
    }

    @Override
    public boolean add(Cuisine object) {
        String sql = "INSERT INTO cuisine (id, name, price, description, image, categoryID) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql, object.getCuisineId(), object.getName(), object.getPrice(), object.getDescription(), object.getImage(), object.getCategory().getCategoryId());
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM cuisine WHERE id = ?";
        try {
            PreparedStatement statement = statementHelper.prepareStatement(sql, id);
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}