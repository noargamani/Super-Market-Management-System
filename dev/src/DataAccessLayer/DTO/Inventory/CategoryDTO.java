package DataAcessLayer.DTO.Inventory;

public class CategoryDTO {
    private int categoryId;
    private String categoryName;
    private int fatherCategory;

    /**
     * Constructs a CategoryDTO object with the specified category ID and name.
     * The fatherCategory is set to -1 by default.
     *
     * @param categoryId   The ID of the category.
     * @param categoryName The name of the category.
     */
    public CategoryDTO(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.fatherCategory = -1;
    }

    /**
     * Constructs a CategoryDTO object with the specified category ID, name, and father category.
     *
     * @param categoryId     The ID of the category.
     * @param categoryName   The name of the category.
     * @param fatherCategory The ID of the father category.
     */
    public CategoryDTO(int categoryId, String categoryName, int fatherCategory) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.fatherCategory = fatherCategory;
    }

    /**
     * Returns the ID of the category.
     *
     * @return The category ID.
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the ID of the category.
     *
     * @param categoryId The category ID to set.
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Returns the name of the category.
     *
     * @return The category name.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the name of the category.
     *
     * @param categoryName The category name to set.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Returns the ID of the father category.
     *
     * @return The father category ID.
     */
    public int getFatherCategory() {
        return fatherCategory;
    }
    /**
     * Sets the ID of the father category.
     *
     * @param fatherCategory The father category ID to set.
     */
    public void setFatherCategory(int fatherCategory) {
        this.fatherCategory = fatherCategory;
    }

    /**
     * Returns a string representation of the CategoryDTO object.
     *
     * @return The string representation of the object.
     */
    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryID=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", fatherCategory=" + fatherCategory +
                '}';
    }
}