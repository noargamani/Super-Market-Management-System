package DataAcessLayer.DTO.Suppliers;



public class CategoryDTO {
    private int categoryID;
    private String categoryName;
    private int fatherCategory;

    public CategoryDTO(int categoryID, String categoryName, int fatherCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.fatherCategory = fatherCategory;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getFatherCategory() {
        return fatherCategory;
    }

    public void setFatherCategory(int fatherCategory) {
        this.fatherCategory = fatherCategory;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryID=" + categoryID +
                ", categoryName='" + categoryName + '\'' +
                ", fatherCategory=" + fatherCategory +
                '}';
    }
}
